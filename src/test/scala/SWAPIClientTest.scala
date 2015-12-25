import clients.SWAPIClient
import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by raoul-gabrielurma on 24/12/2015.
  */
class SWAPIClientTest extends FlatSpec with Matchers {

    "SWAPI" should "return the correct name of a spaceship for its index" in {

        val starshipName = SWAPIClient.fetchStarshipNameForIndex(2)

        "CR90 corvette" shouldEqual starshipName
    }

    it should "return the correct name of spaceship for the associated url" in {

        val url = "http://swapi.co/api/starships/10/"
        val starshipName = SWAPIClient.fetchStarshipNameForUrl(url)

        "Millennium Falcon" shouldEqual starshipName
    }

    it should "return a models.Film object for the film index" in {

        val swapiFilm = SWAPIClient.fetchSWAPIFilmForIndex(2)

        "The Empire Strikes Back" shouldEqual swapiFilm.title
    }

}
