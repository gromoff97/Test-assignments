package home.grom.urljournaling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

import static home.grom.utils.ValidationUtils.*;

/**
 * Consists of web-journal represented as a
 * class implementing {@link Queue}-interface (i.e. {@link ConcurrentLinkedQueue})
 * and methods manipulating state of its instances (i.e. creating and reading).
 *
 * @see     Queue
 * @see     ConcurrentLinkedQueue
 *
 * @author  <a href="mailto:gromoff97@mail.ru">Anton Gromov</a>
 */
public class WebPageJournal {

    /** The queue with visits. */
    private Queue<Visit> journalData;

    /** Sets limit of timeout while connecting to URL. */
    private static final int JSOUP_TIMEOUT = 20_000;

    /** Creates empty journal. */
    public WebPageJournal() {
        this.journalData = new ConcurrentLinkedQueue<>();
    }

    /**
     * Creates journal based on URLs provided in its parameter.
     *
     * @param   urlLinks
     *          web-links.
     */
    public WebPageJournal(Iterable<String> urlLinks) {
        requireNonNull(urlLinks);
        this.journalData = new ConcurrentLinkedQueue<>();
        urlLinks.forEach(this::registerVisit);
    }

    public WebPageJournal(WebPageJournal originalJournal) {
        requireNonNull(originalJournal);
        this.journalData = new ConcurrentLinkedQueue<>();
        this.journalData.addAll(originalJournal.journalData);
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

        this.journalData.add(new Visit(newURL, newDoc.outerHtml(), ZonedDateTime.now()));
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
        this.journalData.add(new Visit(newURL, Jsoup.parse(htmlContent).outerHtml(), ZonedDateTime.now()));
        return true;
    }

    /**
     * @return the unmodifiable set of URL from journal
     */
    public Stream<Visit> visits() {
        return this.journalData.stream();
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
            return Arrays.equals(this.journalData.toArray(), ((WebPageJournal) that).journalData.toArray());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.journalData.toArray());
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
