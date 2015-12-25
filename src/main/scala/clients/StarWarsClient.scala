package clients

import models.{Starship, Info, Film}

object StarWarsClient {
    def fetchFilmForTitle(filmTitle: String) : Film = {

        def htmlContent = fetchHtmlContentForSearchQuery(filmTitle)
        Film(filmTitle, Info(extractImageUrl(htmlContent), extractLink(htmlContent)))
    }


    def fetchStarshipForName(starshipName: String) : Starship = {

        def htmlContent = fetchHtmlContentForSearchQuery(starshipName)

        Starship(starshipName,
            extractImageUrl(htmlContent),
            extractLink(htmlContent))
    }

    private def fetchHtmlContentForSearchQuery(query: String) : String = {
        val starwarsDataBankUrl = getSearchUrl(query)
        HttpClient.fetchContent(starwarsDataBankUrl)
    }

    private def getSearchUrl(query: String): String = {
        val formattedQuery = query.replaceAll(" ", "%20")
        s"http://www.starwars.com/search?q=${formattedQuery}"
    }

    private def extractLink(htmlContent: String): String = {
        val pattern = """href":"(.+?)"}""".r
        val optLink = pattern.findFirstMatchIn(htmlContent)
        optLink.map(m => m.group(1)).getOrElse("#")
    }

    private def extractImageUrl(htmlContent: String): String = {
        val pattern = """image_url":"(.+?)"""".r
        val optImageUrl = pattern.findFirstMatchIn(htmlContent)
        optImageUrl.map(m => m.group(1)).getOrElse("")
    }
}
