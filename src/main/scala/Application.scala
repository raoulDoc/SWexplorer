import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import com.typesafe.scalalogging.Logger
import net.liftweb.json._
import org.slf4j.LoggerFactory

object Application {

    private val logger = Logger(LoggerFactory.getLogger("Application"))

    def main(args: Array[String]) {
        collectData()
    }

    private def collectData() : Unit = {

        writeJsonToFile("data.json", StarWarsData().asJson)
        logger.info("Success")
    }

    private def writeJsonToFile(file: String, json: JsonAST.JObject): Unit = {

        logger.info("Writing to file " + file)

        val data = pretty(render(json))
        Files.write(Paths.get(file), data.getBytes(StandardCharsets.UTF_8))
    }
}
