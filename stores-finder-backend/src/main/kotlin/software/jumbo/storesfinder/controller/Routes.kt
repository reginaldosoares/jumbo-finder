package software.jumbo.storesfinder.controller

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router
import software.jumbo.storesfinder.configuration.internalServerError
import java.net.URI

@Configuration
class Routes(private val store: StoreHandler) {

    /**
     * reactive router WebFlux API configuration using Kotlin DSL
     * Each route has a handler function, assigned to a given request, eg.
     * (ServerRequest) -> Mono<ServerResponse>
     */
    @Bean
    fun route() = router {
        accept(MediaType.TEXT_HTML).nest {
            GET("/") { permanentRedirect(URI("index.html")).build() }
        }
        resources("/**", ClassPathResource("static/"))

        "/api".nest {
            "stores".nest {
                accept(MediaType.APPLICATION_JSON).nest {
                    GET("/{storeId}", store::retrieve)
                    POST("", store::persist)
                }
                "nearby".nest {
                    accept(MediaType.APPLICATION_JSON).nest {
                        GET("/{longitude}/{latitude}", store::nearBySearch)
                    }
                }
            }
        }
    }.filter { request, next ->
        next.runCatching { handle(request) }
            .fold(
                onSuccess = { response -> response },
                onFailure = { ex -> internalServerError(ex) }
            )
    }
}

