import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import net.liftweb.json._

object Application {

    def main(args: Array[String]) {
        collectData()
    }

    private def collectData() : Unit = {

        val starWarsData = StarWarsData()

        writeJsonToFile("data.json", starWarsData.asJson)

        println("Success")
    }

    private def writeJsonToFile(file: String, json: JsonAST.JObject): Unit = {

        println("Writing to file " + file)

        val data = pretty(render(json))
        Files.write(Paths.get(file), data.getBytes(StandardCharsets.UTF_8))
    }
}
