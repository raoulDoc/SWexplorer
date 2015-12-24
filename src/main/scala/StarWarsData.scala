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
                    ("img" -> film.info.imageUrl) ~
                    ("link" -> film.info.link) ~
                    ("children" ->
                        starships.map(s =>
                            (("name" -> s.name) ~
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
    private val timeout = 15 seconds

    def apply() : StarWarsData = {

        val data = fetchStarWarsData(fetchSWAPIFilms)
        executor.shutdown()

        data
    }

    private def fetchSWAPIFilms: Seq[SWAPIFilm] = {

        println("Fetching Films from SWAPI")

        val futureFilms = 1 to 7 map (index => Future {
            SWAPIClient.fetchSWAPIFilmForIndex(index)
        })

        Await.result(Future.sequence(futureFilms), timeout)
    }

    private def fetchStarWarsData(swapiFilms: Seq[SWAPIFilm]): StarWarsData = {

        println("Fetching All Data")

        val futureData = swapiFilms.map(fetchAllDataForSWAPIFilm(_))
        val data = Await.result(Future.sequence(futureData), timeout)
        StarWarsData(data)
    }

    private def fetchAllDataForSWAPIFilm(swapiFilm: SWAPIFilm): Future[(Film, Seq[Starship])] = {
        for {
            film <- Future { fetchStarWarsFilmForName(swapiFilm) }
            starships <- Future.sequence(fetchStarshipsForFilm(swapiFilm))
        } yield (film, starships)
    }

    private def fetchStarWarsFilmForName(swapiFilm: SWAPIFilm) : Film = {
        println(s"Fetching Film info for ${swapiFilm.title}")

        StarWarsClient.fetchFilmForTitle(swapiFilm.title)
    }

    private def fetchStarshipsForFilm(swapiFilm: SWAPIFilm): Seq[Future[Starship]] = {
        println(s"Fetching starships data for ${swapiFilm.title}")
        swapiFilm.starships
            .map(starshipUrl => Future {
                    val starshipName = SWAPIClient.fetchStarshipNameForUrl(starshipUrl)
                    StarWarsClient.fetchStarshipForName(starshipName)
            })
    }
}
