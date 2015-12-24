object StarWarsClient {

    def fetch(starshipName: String) : Starship = {

        val formattedQuery = starshipName.replaceAll(" ", "%20")

        val starwarsDataBankUrl
        = s"http://www.starwars.com/search?f%5Bsearch_section%5D=Databank&q=${formattedQuery}"

        def htmlContent = HttpClient.fetchContent(starwarsDataBankUrl)

        Starship(starshipName,
            extractImageUrl(htmlContent),
            extractLink(htmlContent))
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
