package home.grom.urljournaling;

import org.apache.commons.validator.routines.UrlValidator;
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
    private Map<String, Document> journalData;

    private static final UrlValidator urlValidator = new UrlValidator();

    private static final int JSOUP_TIMEOUT = 20_000;

    public URLHTMLVisitJournal() {
        this.journalData = new ConcurrentHashMap<>();
    }

    public URLHTMLVisitJournal( Set<String> URLSet ){
        if ( null == URLSet ){
            throw new IllegalArgumentException("Referencing Set of urls to non-null instance is required.");
        }

        this.journalData = new ConcurrentHashMap<>();

        // "Empty Set" means "Empty Map"
        if ( URLSet.isEmpty() ){
            return;
        }

        for ( String url : URLSet ){
            this.registerVisit(url);
        }
    }

    private static boolean isValidURL( final String url ){
        if ( null == url ){
            throw new IllegalArgumentException("Referencing URL to non-null instance is required.");
        }
        return urlValidator.isValid(url);
    }

    // adds Journal's entry using "JSoup" library
    public boolean registerVisit( final String newURL ){
        if ( !isValidURL(newURL) ){
            throw new IllegalArgumentException("Valid URL address is required.");
        }

        Document newDoc;
        try {
            newDoc = Jsoup.connect(newURL).maxBodySize(0).timeout(JSOUP_TIMEOUT).get();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        this.journalData.put(newURL, newDoc);
        return true;
    }

    // adds Journal's entry manually
    public boolean registerVisit( final String newURL, final String HTMLContent ){
        if ( !isValidURL(newURL) ){
            throw new IllegalArgumentException("Valid URL address is required.");
        }

        if ( null == HTMLContent || HTMLContent.trim().isEmpty() ){
            throw new IllegalArgumentException("Non-empty HTML content is required.");
        }

        Document newDoc = Jsoup.parse(HTMLContent);
        this.journalData.put(newURL,newDoc);
        return true;
    }

    // looks for HTML code in existing Map by entered URL
    public String getVisitedHTMLPage( final String url ){
        if ( !isValidURL(url) ){
            throw new IllegalArgumentException("Valid URL address is required.");
        }

        Document resultDoc = this.journalData.get(url);
        if ( null == resultDoc ) {
            return null;
        }

        return resultDoc.outerHtml();
    }

    public Set<String> getVisitedURLSet(){
        return Collections.unmodifiableSet(this.journalData.keySet());
    }

    public int getSize(){
        return this.journalData.size();
    }

    public boolean isEmpty(){
        return 0 == getSize();
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == this ) {
            return true;
        }

        if ( !(obj instanceof URLHTMLVisitJournal) ) {
            return false;
        }

        return this.journalData.equals(((URLHTMLVisitJournal) obj).journalData);
    }
}
