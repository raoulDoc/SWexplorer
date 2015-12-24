import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{ThreadFactory, ExecutorService, Executors}

import com.typesafe.scalalogging.Logger
import net.liftweb.json.JsonAST
import net.liftweb.json.JsonDSL._
import org.slf4j.LoggerFactory

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

    private val executor: ExecutorService = Executors.newCachedThreadPool(new StarwarsDataThreadFactory())
    private implicit val executionContext = ExecutionContext.fromExecutor(executor)
    private val timeout = 15 seconds
    private val logger = Logger(LoggerFactory.getLogger("StarWarsData"))

    def apply() : StarWarsData = {

        logger.info("Start fetching Star Wars data")
        val starWarsData: StarWarsData = fetchStarWarsData(fetchSWAPIFilms)
        executor.shutdown()
        starWarsData
    }

    private def fetchSWAPIFilms: Seq[SWAPIFilm] = {

        logger.info("Fetching Films from SWAPI")

        val futureFilms = 1 to 7 map (index => Future {
            SWAPIClient.fetchSWAPIFilmForIndex(index)
        })

        Await.result(Future.sequence(futureFilms), timeout)
    }

    private def fetchStarWarsData(swapiFilms: Seq[SWAPIFilm]): StarWarsData = {

        logger.info("Fetching All Data")

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
        logger.info(s"Fetching Film info for ${swapiFilm.title}")

        StarWarsClient.fetchFilmForTitle(swapiFilm.title)
    }

    private def fetchStarshipsForFilm(swapiFilm: SWAPIFilm): Seq[Future[Starship]] = {
        logger.info(s"Fetching starships data for ${swapiFilm.title}")
        swapiFilm.starships
            .map(starshipUrl => Future {
                    val starshipName = SWAPIClient.fetchStarshipNameForUrl(starshipUrl)
                    StarWarsClient.fetchStarshipForName(starshipName)
            })
    }

    class StarwarsDataThreadFactory extends ThreadFactory {
        private val count = new AtomicInteger()

        override def newThread(r: Runnable) : Thread = {
            val thread = new Thread(r)
            thread.setName(s"SWThread-${count.incrementAndGet}")
            thread
        }
    }

}
