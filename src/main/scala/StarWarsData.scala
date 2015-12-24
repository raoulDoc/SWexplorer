import java.util.concurrent.{ExecutorService, Executors}

import net.liftweb.json.JsonAST
import net.liftweb.json.JsonDSL._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

case class StarWarsData(data: Seq[(Film, Seq[Starship])]) {
     def asJson(): JsonAST.JObject = {
        (("name" -> "starwars") ~
            ("children" -> this.data.map {
                case (film, starships) =>
                    ("name" -> film.title) ~
                        ("children" -> starships.map(s => (("name" -> s.name) ~
                            ("img" -> s.imageUrl) ~
                            ("link" -> s.link)))
                            )
            }))
    }
}

object StarWarsData {

    import scala.language.postfixOps

    private val executor: ExecutorService = Executors.newCachedThreadPool()
    private implicit val executionContext = ExecutionContext.fromExecutor(executor)
    private val timeout = 10 seconds

    def apply() : StarWarsData = {

        val films = getStarWarsFilms

        val data = fetchDataForFilms(films)
        executor.shutdown()

        data

    }

    private def getStarWarsFilms: Seq[Film] = {

        println("Fetching Films")

        val futureFilms = 1 to 7 map (index => Future {
            SWAPIClient.fetchFilmForIndex(index)
        })

        Await.result(Future.sequence(futureFilms), timeout)
    }

    private def fetchDataForFilms(films: Seq[Film]): StarWarsData = {

        println("Fetching Starships")

        StarWarsData(
            films.map(film => (film, fetchStarshipsForFilm(film)))
                 .map { case (film, starships) => (film, Await.result(Future.sequence(starships), timeout)) })
    }

    private def fetchStarshipsForFilm(film: Film): Seq[Future[Starship]] = {
        film.starships
            .map(starshipUrl => Future { fetchStarship(starshipUrl) })
    }

    private def fetchStarship(starshipUrl: String): Starship = {
        val pattern = """http://swapi.co/api/starships/(\d+)/""".r

        val optIndex = pattern.findFirstMatchIn(starshipUrl)

        // throws if couldn't parse
        val index = optIndex.map(m => m.group(1)).get.toInt

        val starshipName = SWAPIClient.fetchStarshipNameForIndex(index)

        StarWarsClient.fetch(starshipName)
    }
}
