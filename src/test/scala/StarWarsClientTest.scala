import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by raoul-gabrielurma on 24/12/2015.
  */
class StarWarsClientTest extends FlatSpec with Matchers {

    "StarWars online" should "return a Starship object for a search query" in {

        val starship = StarWarsClient.fetch("Millennium Falcon")

        val expectedLink = "http://www.starwars.com/databank/millennium-falcon"

        "Millennium Falcon" shouldEqual starship.name
        expectedLink shouldEqual starship.link
        starship.imageUrl should include regex "jpg|jpeg|png|gif"

    }

}
