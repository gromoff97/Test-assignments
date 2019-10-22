package home.grom.urljournaling;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is based on a "weak" decorator pattern
 * ( i.e. this class does not implements it's main attribute's interface ).
 *
 * It consists of web-journal represented as a
 * class implementing {@link Map}-interface ( i.e. {@link ConcurrentHashMap} )
 * and methods manipulating its state ( i.e. creating and reading/searching )
 *
 * @see     Map
 * @see     ConcurrentHashMap
 * @author  <a href="mailto:gromoff97@mail.ru">Anton Gromov</a>
 */
public class WebJournal {

    /**
     * The map with entries containing URL-link and its HTML-code
     */
    private Map<String, String> journalData;

    /**
     * Sets limit of timeout while connecting to URLs
     */
    private static final int JSOUP_TIMEOUT = 20_000;

    /**
     * Creates empty journal.
     */
    public WebJournal() {
        this.journalData = new ConcurrentHashMap<>();
    }

    /**
     * Creates journal based on URLs provided in its parameter.
     *
     * @param   urlLinks
     *          web-links.
     */
    public WebJournal( Iterable<String> urlLinks ){
        if ( null == urlLinks ){
            throw new IllegalArgumentException("Referencing to non-null instance is required.");
        }

        this.journalData = new ConcurrentHashMap<>();

        for ( String url : urlLinks ){
            this.registerVisit(url);
        }
    }

    /**
     * Validates entered URL with {@link UrlValidator#isValid(String)}
     *
     * @param   url
     *          contains URL of some web-page.
     *
     * @return  {@code true} if URL is valid,
     *          otherwise {@code false}.
     */
    private static boolean isValidURL( final String url ){
        return UrlValidator.getInstance().isValid(url);
    }

    /**
     * Gets HTML-content from entered URL and creates new entry in journal.
     *
     * @param   newURL
     *          contains URL of some web-page.
     *
     * @return  {@code true} if adding entry to journal successfully finished,
     *          otherwise {@code false}.
     *
     * @throws  IllegalArgumentException
     *          if URL-argument is invalid.
     */
    public final boolean registerVisit( final String newURL ){
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

        this.journalData.put(newURL, newDoc.outerHtml());
        return true;
    }

    /**
     * Gets both HTML-content and URL from its parameters. Then it creates new entry in journal.
     *
     * @param   newURL
     *          contains URL of some web-page.
     *
     * @param   htmlContent
     *          contains HTML-page of URL-parameter.
     *
     * @return  {@code true} if adding entry to journal successfully finished,
     *          otherwise {@code false}.
     *
     * @throws  IllegalArgumentException
     *          if URL-argument is invalid or HTML-content is blank or references to null.
     */
    public boolean registerVisit( final String newURL, final String htmlContent ){
        if ( !isValidURL(newURL) ){
            throw new IllegalArgumentException("Valid URL address is required.");
        }

        if ( null == htmlContent || htmlContent.trim().isEmpty() ){
            throw new IllegalArgumentException("Non-blank HTML content is required.");
        }

        this.journalData.put(newURL, Jsoup.parse(htmlContent).outerHtml());
        return true;
    }

    /**
     * Looks for HTML-page for passed URL-argument.
     *
     * @param   url
     *          URL of page.
     *
     * @return  HTML-content for searched URL
     *          or null if page with this URL is not found.
     *
     * @throws  IllegalArgumentException
     *          if URL-argument is invalid.
     */
    public String search( final String url ) {
        if ( !isValidURL(url) ){
            throw new IllegalArgumentException("Valid URL address is required.");
        }

        return this.journalData.get(url);
    }

    /**
     * @return the unmodifiable set of URL from this journal
     */
    public Set<String> getVisitedURLSet(){
        return Collections.unmodifiableSet(this.journalData.keySet());
    }

    /**
     * @return the size of this journal.
     */
    public int getSize(){
        return this.journalData.size();
    }

    /**
     * @return  {@code true} if this journal doesn't contain any entry,
     *          otherwise {@code false}.
     */
    public boolean isEmpty(){
        return 0 == getSize();
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == this ) {
            return true;
        }

        if ( !(obj instanceof WebJournal) ) {
            return false;
        }

        return this.journalData.equals(((WebJournal) obj).journalData);
    }

    @Override
    public int hashCode() {
        return journalData.hashCode();
    }
}
