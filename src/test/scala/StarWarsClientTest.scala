import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by raoul-gabrielurma on 24/12/2015.
  */
class StarWarsClientTest extends FlatSpec with Matchers {

    "StarWars online" should "return a Starship object for a search query" in {

        val starship = StarWarsClient.fetchStarshipForName("Millennium Falcon")

        val expectedLink = "http://www.starwars.com/databank/millennium-falcon"

        "Millennium Falcon" shouldEqual starship.name
        expectedLink shouldEqual starship.link
        starship.imageUrl should include regex "jpg|jpeg|png|gif"

    }

    "StarWars online" should "return a Film object for a search query" in {

        val film = StarWarsClient.fetchFilmForTitle("Revenge of the Sith")

        val expectedLink = "http://www.starwars.com/films/star-wars-episode-iii-revenge-of-the-sith"

        "Revenge of the Sith" shouldEqual film.title
        expectedLink shouldEqual film.info.link
        film.info.imageUrl should include regex "jpg|jpeg|png|gif"
    }

}
