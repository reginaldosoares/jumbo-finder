package software.jumbo.storesfinder.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

/**
 * CORS setup
 * required for the frontend XHR originated requests
 */
@Configuration
class WebConfiguration {
    @Bean
    fun corsFilter(): CorsWebFilter = CorsWebFilter(
        CorsConfiguration().apply {
            allowCredentials = true
            addAllowedOrigin("*")
            addAllowedHeader("*")
            addAllowedMethod("*")
        }.let { config ->
            UrlBasedCorsConfigurationSource()
                .apply { registerCorsConfiguration("/**", config) }
        })
}


/**
 * Web errors handlers configuration
 */
fun badRequest(t: Throwable) = error(HttpStatus.BAD_REQUEST, t)
fun badRequest(message: String) = error(HttpStatus.BAD_REQUEST, message)

fun methodNotAllowed(t: Throwable) = error(HttpStatus.METHOD_NOT_ALLOWED, t)
fun methodNotAllowed(message: String) = error(HttpStatus.METHOD_NOT_ALLOWED, message)

fun internalServerError(message: String) = error(HttpStatus.INTERNAL_SERVER_ERROR, message)
fun internalServerError(t: Throwable) = error(HttpStatus.INTERNAL_SERVER_ERROR, t)

fun unsupportedMediaType(t: Throwable) = error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, t)
fun unsupportedMediaType(message: String) = error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, message)

private fun error(status: HttpStatus, message: String): Mono<ServerResponse> =
    Error(status.value(), listOf(message))
        .let { error -> ServerResponse.status(status).body(Mono.just(error)) }

private fun error(status: HttpStatus, t: Throwable): Mono<ServerResponse> =
    Error(status.value(), listOf(t.message ?: status.name))
        .let { error -> ServerResponse.status(status).body(Mono.just(error)) }

data class Error(val code: Int, val messages: List<String>)



