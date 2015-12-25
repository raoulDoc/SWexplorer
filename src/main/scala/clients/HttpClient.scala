package clients

import scala.io.Source

object HttpClient {

    def fetchContent(urlString: String): String = {
        val url = new java.net.URL(urlString)
        val connection = url.openConnection()
        connection.setRequestProperty("User-Agent", "swapi-python");

        Source.fromInputStream(connection.getInputStream).getLines.mkString("\n")
    }
}
