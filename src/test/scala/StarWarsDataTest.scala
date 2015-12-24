import org.scalatest.{FlatSpec, Matchers}
import net.liftweb._
/**
  * Created by raoul-gabrielurma on 24/12/2015.
  */
class StarWarsDataTest extends FlatSpec with Matchers {

    "Dummy StarWarsData" should "convert to Json" in {


        val film = Film("CoolMovie", Seq("Falcon"))

        val falconStarship = Starship("Falcon", "fakeImage", "fakeLink")
        val deathstarStarship = Starship("Death Star", "fakeImage", "fakeLink")
        val starships = Seq(falconStarship, deathstarStarship)

        val starWarsData = StarWarsData(Seq((film, starships)))

        val jsonStarWarsData = starWarsData.asJson

        val expectedJsonString =
            """{"name":"starwars",
              | "children":[{"name":"CoolMovie",
              |            "children":
              |            [{
              |               "name":"Falcon",
              |               "img":"fakeImage",
              |               "link":"fakeLink"
              |            },{
              |               "name":"Death Star",
              |               "img":"fakeImage",
              |               "link":"fakeLink"
              |            }]}]}""".stripMargin


        jsonStarWarsData shouldEqual json.parse(expectedJsonString)

    }


}
