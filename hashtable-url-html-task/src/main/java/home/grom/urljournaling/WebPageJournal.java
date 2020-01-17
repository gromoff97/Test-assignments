package home.grom.urljournaling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

import static home.grom.utils.ValidationUtils.*;

/**
 * Consists of web-journal represented as a {@link ConcurrentLinkedQueue} of
 * {@link VisitEvent}-instances and methods manipulating its state (i.e. creating and reading).
 *
 * @see     ConcurrentLinkedQueue
 * @see     VisitEvent
 *
 * @author  <a href="mailto:gromoff97@mail.ru">Anton Gromov</a>
 */
public final class WebPageJournal {

    /** The queue with visit events. */
    private final ConcurrentLinkedQueue<VisitEvent> eventsData;

    /** Sets limit of timeout while connecting to URL. */
    private static final int JSOUP_TIMEOUT = 20_000;

    private WebPageJournal() {
        eventsData = new ConcurrentLinkedQueue<>();
    }

    /** Creates empty journal. */
    public static WebPageJournal empty() {
        return new WebPageJournal();
    }

    /**
     * Creates copy of passed {@link WebPageJournal}-instance.
     *
     * @param   original
     *          instance to copy content from.
     *
     * @return  reference to new identical {@link WebPageJournal}-instance.
     */
    public static WebPageJournal copyOf(WebPageJournal original) {
        requireNonNull(original);
        WebPageJournal copy = new WebPageJournal();
        copy.eventsData.addAll(original.eventsData);
        return copy;
    }

    /**
     * Creates {@link WebPageJournal}-instance
     * based on provided URL-links
     *
     * @param   urlLinks
     *          url-links.
     *
     * @return  reference to new {@link WebPageJournal}-instance.
     */
    public static WebPageJournal byLinks(Iterable<String> urlLinks) {
        requireNonNull(urlLinks);
        WebPageJournal instance = new WebPageJournal();
        urlLinks.forEach(instance::registerVisit);
        return instance;
    }

    /** 
     * Gets HTML-content from entered URL automatically and creates new {@link VisitEvent} in journal.
     *
     * @param   newURL
     *          contains URL of some web-page.
     *
     * @return  {@code true} if adding event to journal successfully finished,
     *          otherwise {@code false}.
     *
     * @throws  IllegalArgumentException
     *          if URL-argument is invalid.
     */
    public boolean registerVisit(String newURL) {
        requireValidURL(newURL);

        Document newDoc;
        try {
            newDoc = Jsoup.connect(newURL).maxBodySize(0).timeout(JSOUP_TIMEOUT).get();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        VisitEvent visitEvent = new VisitEvent(newURL, newDoc.outerHtml(), ZonedDateTime.now());
        return eventsData.add(visitEvent);
    }

    /**
     * Gets both HTML-content and URL from its parameters.
     * Then it creates new {@link VisitEvent}-instance in journal.
     *
     * @param   newURL
     *          contains URL of some web-page.
     *
     * @param   htmlContent
     *          contains HTML-page of URL-parameter.
     *
     * @return  {@code true} if adding event to journal successfully finished,
     *          otherwise {@code false}.
     *
     * @throws  IllegalArgumentException
     *          if URL-argument is invalid or HTML-content is blank or references to null.
     */
    public boolean registerVisit(String newURL, String htmlContent) {
        requireValidURL(newURL);
        requireNonBlank(htmlContent, "Non-blank HTML content is required.");
        VisitEvent visitEvent = new VisitEvent(newURL, Jsoup.parse(htmlContent).outerHtml(), ZonedDateTime.now());
        return eventsData.add(visitEvent);
    }

    /**
     * @return the {@link Stream} of {@link VisitEvent}-instances.
     */
    public Stream<VisitEvent> visits() {
        return eventsData.stream();
    }

    /**
     * @return the size of journal.
     */
    public int size() {
        return eventsData.size();
    }

    /**
     * @return  {@code true} if journal doesn't contain any events,
     *          otherwise {@code false}.
     */
    public boolean isEmpty() {
        return 0 == size();
    }

    @Override
    public boolean equals(Object that) {
        if (that == this) {
            return true;
        }

        if (that instanceof WebPageJournal) {
            Object[] thisArray = this.eventsData.toArray();
            Object[] thatArray = ((WebPageJournal) that).eventsData.toArray();
            return Arrays.equals(thisArray, thatArray);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventsData.toArray());
    }

    public static final class VisitEvent {

        private final String url;
        private final String content;
        private final ZonedDateTime date;

        private final int hash;

        private VisitEvent(String url, String content, ZonedDateTime date) {
            this.url = requireNonBlank(url);
            this.content = requireNonBlank(content);
            this.date = requireNonNull(date);
            this.hash = Objects.hash(url, content, date);
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

            if (that instanceof VisitEvent) {
                VisitEvent other = (VisitEvent) that;
                return this.url.equals(other.url) &&
                        this.content.equals(other.content) &&
                        this.date.equals(other.date);
            }

            return false;

        }

        @Override
        public int hashCode() {
            return hash;
        }
    }
}
