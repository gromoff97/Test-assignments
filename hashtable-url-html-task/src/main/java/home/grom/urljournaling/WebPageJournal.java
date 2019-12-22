package home.grom.urljournaling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static home.grom.utils.ValidationUtils.*;

/**
 * Consists of web-journal represented as a
 * class implementing {@link ConcurrentMap}-interface (i.e. {@link ConcurrentHashMap})
 * and methods manipulating its state (i.e. creating and reading/searching).
 *
 * @see     ConcurrentMap
 * @see     ConcurrentHashMap
 *
 * @author  <a href="mailto:gromoff97@mail.ru">Anton Gromov</a>
 */
public class WebPageJournal {

    /** The map with entries containing URL-link and its HTML-code. */
    private ConcurrentMap<String, String> journalData;

    /** Sets limit of timeout while connecting to URL. */
    private static final int JSOUP_TIMEOUT = 20_000;

    /** Creates empty journal. */
    public WebPageJournal() {
        this.journalData = new ConcurrentHashMap<>();
    }

    /**
     * Creates journal based on URLs provided in its parameter.
     *
     * @param   urlLinks
     *          web-links.
     */
    public WebPageJournal(Iterable<String> urlLinks) {
        requireNonNull(urlLinks);
        this.journalData = new ConcurrentHashMap<>();
        urlLinks.forEach(this::registerVisit);
    }

    /** 
     * Gets HTML-content from entered URL automatically and creates new entry in journal.
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
    public final boolean registerVisit(String newURL) {
        requireValidURL(newURL);

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
    public boolean registerVisit(String newURL, String htmlContent) {
        requireValidURL(newURL);
        requireNonBlank(htmlContent, "Non-blank HTML content is required.");
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
     *          or null if page with passed URL is not found.
     *
     * @throws  IllegalArgumentException
     *          if URL-argument is invalid.
     */
    public String search(String url) {
        return this.journalData.get(requireValidURL(url));
    }

    /**
     * @return the unmodifiable set of URL from journal
     */
    public Set<String> getVisitedURLSet() {
        return Collections.unmodifiableSet(this.journalData.keySet());
    }

    /**
     * @return the size of journal.
     */
    public int getSize() {
        return this.journalData.size();
    }

    /**
     * @return  {@code true} if journal doesn't contain any entry,
     *          otherwise {@code false}.
     */
    public boolean isEmpty() {
        return 0 == getSize();
    }

    @Override
    public boolean equals(Object that) {
        if (that == this) {
            return true;
        }

        if (that instanceof WebPageJournal) {
            return this.journalData.equals(((WebPageJournal) that).journalData);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return journalData.hashCode();
    }

    public static final class Visit {
        private final String url;
        private final String content;
        private final ZonedDateTime date;

        private Visit(String url, String content, ZonedDateTime date) {
            this.url = requireNonBlank(url);
            this.content = requireNonBlank(content);
            this.date = requireNonNull(date);
        }

        public String getUrl() {
            return url;
        }

        public String getContent() {
            return content;
        }

        public ZonedDateTime getDate() {
            return date;
        }

        @Override
        public boolean equals(Object that) {
            if (this == that) {
                return true;
            }

            if (that instanceof Visit) {
                Visit other = (Visit) that;
                return this.url.equals(other.url) &&
                        this.content.equals(other.content) &&
                        this.date.equals(other.date);
            }

            return false;

        }

        @Override
        public int hashCode() {
            return Objects.hash(url, content, date);
        }
    }
}
