package lisval.service1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class Service1Application

fun main(args: Array<String>) {
    runApplication<Service1Application>(*args)
}
