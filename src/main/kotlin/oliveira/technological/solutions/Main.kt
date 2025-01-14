package oliveira.technological.solutions

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class App

fun main(args: Array<String>) {

    val dotenv = dotenv()

    System.setProperty("DATABASE_URL", dotenv["DATABASE_URL"])
    System.setProperty("DATABASE_USERNAME", dotenv["DATABASE_USERNAME"])
    System.setProperty("DATABASE_PASSWORD", dotenv["DATABASE_PASSWORD"])

    runApplication<App>(*args)
}






