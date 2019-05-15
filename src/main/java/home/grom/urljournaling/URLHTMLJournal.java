package home.grom.urljournaling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class URLHTMLJournal  {
    private Map<String, Document> workingMap;

    private static final String URLRegex = "\"^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]\"";

    public URLHTMLJournal() {
        this.workingMap = new HashMap<>();
    }

    private static boolean isInvalidURL( final String url ){
        if ( null == url ){
            throw new IllegalArgumentException("referencing URL to null is not allowed");
        }
        return url.matches(URLRegex);
    }

    /* retrieves HTML code using "JSoup" library */
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

    public int getSize(){
        return this.workingMap.size();
    }

    public boolean isEmpty(){
        return 0 == getSize();
    }
}
