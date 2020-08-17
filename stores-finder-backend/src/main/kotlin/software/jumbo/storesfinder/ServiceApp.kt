package software.jumbo.storesfinder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServiceApp

fun main(args: Array<String>) {
    runApplication<ServiceApp>(*args)
}
