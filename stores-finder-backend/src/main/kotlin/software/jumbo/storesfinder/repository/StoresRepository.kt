package software.jumbo.storesfinder.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Point
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import reactor.core.publisher.Flux
import software.jumbo.storesfinder.domain.StoreDoc

interface StoresRepository : ReactiveSortingRepository<StoreDoc, String> {
    fun findAllByIdNotNullOrderByIdAsc(page: Pageable): Flux<StoreDoc>
    fun findByLocationIsNear(point: Point, distance: Distance, page: Pageable): Flux<StoreDoc>
}

