import java.io.{File, PrintWriter}

import net.liftweb.json._

object Application {

    def main(args: Array[String]) {
        collectData()
    }

    private def collectData() : Unit = {

        val starWarsData = StarWarsData.fromAllFilms

        writeJsonToFile("data.json", starWarsData.asJson)

        println("Success")
    }

    private def writeJsonToFile(file: String, json: JsonAST.JObject): Unit = {

        println("Writing to file " + file)

        val writer = new PrintWriter(new File(file))
        writer.write(pretty(render(json)))
        writer.close()
    }
}
