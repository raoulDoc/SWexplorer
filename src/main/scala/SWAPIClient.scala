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

    def fetchStarshipNameForUrl(starshipUrl: String): String = {
        val index: Int = extractStarshipIndexFromUrl(starshipUrl)
        SWAPIClient.fetchStarshipNameForIndex(index)
    }

    private def extractStarshipIndexFromUrl(starshipUrl: String): Int = {
        val pattern = """http://swapi.co/api/starships/(\d+)/""".r

        val optIndex = pattern.findFirstMatchIn(starshipUrl)

        // throws if couldn't parse
        val index = optIndex.map(m => m.group(1).toInt).get
        index
    }

    def fetchSWAPIFilmForIndex(index: Int): SWAPIFilm = {

        val swapiUrl = s"http://swapi.co/api/films/${index}/?format=json"

        val filmJson = json.parse(HttpClient.fetchContent(swapiUrl))

        implicit val formats = DefaultFormats
        filmJson.extract[SWAPIFilm]
    }
}
