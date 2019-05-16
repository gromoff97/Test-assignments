package home.grom.urljournaling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class URLHTMLVisitJournal  {

    /*
     * This field will reference to ConcurrentHashMap's instance,
     * not HashTable's instance as task "says".
     * Reasons are following :
     * 1) HashTable is obsolete
     * 2) Duplicates are not necessary
     *    ( even if url was visited 1 000 000 times,
     *    we need only the fact about last visit with last html content )
     * 3) ConcurrentHashMap and HashTable are both thread-safe but
     *    the first one wins in performance
     *    ( at least because there are no read-locks )
     * Also implementation was made in a way that
     * null-keys and null-values  are not allowed to be stored.
     * So, in my opinion, it's like HashTable, but seems better :)
     * */
    private Map<String, Document> workingMap;

    // It could be better but IMO it seems good enough for attendance test
    // Reference : https://stackoverflow.com/questions/163360/regular-expression-to-match-urls-in-java
    private static final String URLRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    public URLHTMLVisitJournal() {
        this.workingMap = new ConcurrentHashMap<>();
    }

    public URLHTMLVisitJournal( List<String> URLList ){
        if ( null == URLList ){
            throw new IllegalArgumentException("referencing List to null is not allowed");
        }

        this.workingMap = new ConcurrentHashMap<>();

        // "Empty List" means "Empty Map"
        if ( URLList.isEmpty() ){
            return;
        }

        for ( String url : URLList ){
            this.registerVisit(url);
        }
    }

    private static boolean isValidURL( final String url ){
        if ( null == url ){
            throw new IllegalArgumentException("referencing URL to null is not allowed");
        }
        return url.matches(URLRegex);
    }

    // adds Journal's entry using "JSoup" library
    public boolean registerVisit( final String newURL ){
        if ( !isValidURL(newURL) ){
            throw new IllegalArgumentException("URL address is supposed to be valid");
        }

        Document newDoc;
        try {
            newDoc = Jsoup.connect(newURL).maxBodySize(0).timeout(20_000).get();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        this.workingMap.put(newURL, newDoc);
        return true;
    }

    // adds Journal's entry manually
    public boolean registerVisit( final String newURL, final String HTMLContent ){
        if ( !isValidURL(newURL) ){
            throw new IllegalArgumentException("URL address is supposed to be valid");
        }

        if ( null == HTMLContent || HTMLContent.trim().isEmpty() ){
            throw new IllegalArgumentException("HTML content has to be non-empty");
        }

        Document newDoc = Jsoup.parse(HTMLContent);
        this.workingMap.put(newURL,newDoc);
        return true;
    }

    // looks for HTML code in existing Map by entered URL
    public String getVisitedHTMLPage( final String url ){
        if ( !isValidURL(url) ){
            throw new IllegalArgumentException("URL address is supposed to be valid");
        }

        Document resultDoc = this.workingMap.get(url);
        if ( null == resultDoc ) {
            return null;
        }

        return resultDoc.outerHtml();
    }

    public Set<String> getVisitedURLSet(){
        return new HashSet<>(this.workingMap.keySet());
    }

    public int getSize(){
        return this.workingMap.size();
    }

    public boolean isEmpty(){
        return 0 == getSize();
    }
}
