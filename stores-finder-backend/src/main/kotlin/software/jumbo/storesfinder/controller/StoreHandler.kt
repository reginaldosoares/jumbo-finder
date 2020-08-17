package software.jumbo.storesfinder.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.json
import reactor.core.publisher.Mono
import software.jumbo.storesfinder.configuration.asDistance
import software.jumbo.storesfinder.configuration.asDocument
import software.jumbo.storesfinder.configuration.asGeoJsonPoint
import software.jumbo.storesfinder.configuration.asJsonResponseOk
import software.jumbo.storesfinder.domain.StoreDoc
import software.jumbo.storesfinder.domain.StoreJson
import software.jumbo.storesfinder.domain.StoreSearch
import software.jumbo.storesfinder.repository.StoresRepository

@Component
class StoreHandler(private val repository: StoresRepository) {
    private val logger: Logger = getLogger(javaClass)

    fun persist(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(StoreJson::class.java)
            .map { it.asDocument() }
            .flatMap { repository.save(it) }
            .flatMap { ok().json().bodyValue(it.asJsonResponseOk) }
            .doOnError { logger.error("Error while trying to persist store document - $it") }

    fun retrieve(request: ServerRequest): Mono<ServerResponse> =
        Mono.fromSupplier { request.pathVariable("storeId") }
            .flatMap { repository.findById(it) }
            .flatMap { ok().json().body(Mono.just(it), StoreDoc::class.java) }
            .doOnError { logger.error("Error while trying to retrieve a store record - $it") }

    fun nearBySearch(request: ServerRequest): Mono<ServerResponse> =
        Mono.fromSupplier {
            with(request) { (pathVariable("latitude") to pathVariable("longitude")) to queryParams() }
        }
            .map { (coord, params) -> StoreSearch(coord, params) }
            .map { repository.findByLocationIsNear(it.asGeoJsonPoint(), it.asDistance(), it.limit) }
            .flatMap { it.collectList() }
            .flatMap { ok().json().bodyValue(it) }
            .doOnError { logger.error("Error while trying to query nearby stores - $it") }

}

