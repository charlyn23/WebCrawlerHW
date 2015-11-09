import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by charlynbuchanan on 11/8/15.
 */
public class SpiderLeg
{
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";




    // Give it a URL and it makes an HTTP request for a web page
    public boolean crawl(String url)   {
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            org.jsoup.nodes.Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            if(connection.response().statusCode() == 200) // 200 is the HTTP OK status code
            // indicating that everything is great.
            {
                System.out.println("\n**Visiting** Received web page at " + url);
            }

//            System.out.println("Received web page at " + url);

            if(!connection.response().contentType().contains("text/html"))
            {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }
            org.jsoup.select.Elements linksOnPage = htmlDocument.select("a[href]");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            for(Element link : linksOnPage) {
                this.links.add(link.absUrl("href"));
            }
            return true;
        }
        catch(IOException ioe) {
            // We were not successful in our HTTP request
            System.out.println("Error in out HTTP request " + ioe);
        }
        // We were not successful in our HTTP request
        return false;
    }

    // Tries to find a word on the page
    public boolean searchForWord(String searchWord) {
        if(this.htmlDocument == null) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return false;
        }
        System.out.println("Searching for the word " + searchWord + "...");
        String bodyText = this.htmlDocument.body().text();
        return bodyText.toLowerCase().contains(searchWord.toLowerCase());
    }

    // Returns a list of all the URLs on the page
    public List<String> getLinks()    {
        return this.links;
    }


    // Just a list of URLs
    private List<String> links = new LinkedList<String>();

    // This is our web page, or in other words, our document
    private org.jsoup.nodes.Document htmlDocument;
}
