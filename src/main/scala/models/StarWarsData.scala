package models

import net.liftweb.json.JsonAST
import net.liftweb.json.JsonDSL._

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


