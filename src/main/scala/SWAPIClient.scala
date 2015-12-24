import net.liftweb.json
import net.liftweb.json.DefaultFormats

object SWAPIClient {

    def fetchStarshipNameForIndex(index: Int): String = {

        val urlString = s"http://swapi.co/api/starships/${index}/?format=json"
        val data = HttpClient.fetchContent(urlString)

        val starshipJson = json.parse(data)

        implicit val formats = DefaultFormats
        (starshipJson \ "name").extract[String]

    }

    def fetchFilmForIndex(index: Int): Film = {

        val swapiUrl = s"http://swapi.co/api/films/${index}/?format=json"

        val filmJson = json.parse(HttpClient.fetchContent(swapiUrl))

        implicit val formats = DefaultFormats
        filmJson.extract[Film]
    }
}
