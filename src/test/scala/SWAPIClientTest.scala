import org.scalatest.{Matchers, FlatSpec}

/**
  * Created by raoul-gabrielurma on 24/12/2015.
  */
class SWAPIClientTest extends FlatSpec with Matchers {

    "SWAPI" should "return the correct name of a spaceship for its index" in {

        val starshipName = SWAPIClient.fetchStarshipNameForIndex(2)

        "CR90 corvette" shouldEqual starshipName
    }

    it should "return a Film object for the film index" in {

        val film = SWAPIClient.fetchFilmForIndex(2)

        "The Empire Strikes Back" shouldEqual film.title
    }

}
