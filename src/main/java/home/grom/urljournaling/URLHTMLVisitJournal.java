package home.grom.urljournaling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;

public class URLHTMLVisitJournal  {
    private Map<String, Document> workingMap;

    private static final String URLRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    public URLHTMLVisitJournal() {
        this.workingMap = new HashMap<>();
    }
    public URLHTMLVisitJournal( List<String> urlList ){
        if ( null == urlList ){
            throw new IllegalArgumentException("referencing List to null is not allowed");
        }
    }

    private static boolean isInvalidURL( final String url ){
        if ( null == url ){
            throw new IllegalArgumentException("referencing URL to null is not allowed");
        }
        return !url.matches(URLRegex);
    }

    /* adds Journal's entry using "JSoup" library */
    public boolean registerVisit( final String newURL ){
        if ( isInvalidURL(newURL) ){
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

    /* adds Journal's entry manually */
    public boolean registerVisit( final String newURL, final String HTMLContent ){
        if ( isInvalidURL(newURL) ){
            throw new IllegalArgumentException("URL address is supposed to be valid");
        }

        if ( null == HTMLContent || HTMLContent.trim().isEmpty() ){
            throw new IllegalArgumentException("HTML content has to be non-empty");
        }

        Document newDoc = Jsoup.parse(HTMLContent);
        this.workingMap.put(newURL,newDoc);
        return true;
    }

    public String getVisitedHTMLPage( final String url ){
        if ( isInvalidURL(url) ){
            throw new IllegalArgumentException("URL address is supposed to be valid");
        }

        Document resultDoc = this.workingMap.get(url);
        if ( null == resultDoc ) {
            return null;
        }

        return resultDoc.outerHtml();
    }

    public Set getVisitedURLSet(){
        return new HashSet<>(this.workingMap.keySet());
    }

    public int getSize(){
        return this.workingMap.size();
    }

    public boolean isEmpty(){
        return 0 == getSize();
    }
}
