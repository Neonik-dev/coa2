package lisval.wrapper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WrapperApplication

fun main(args: Array<String>) {
    runApplication<WrapperApplication>(*args)
}
