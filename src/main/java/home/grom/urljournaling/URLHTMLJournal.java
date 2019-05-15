package home.grom.urljournaling;

import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public class URLHTMLJournal  {
    private Map<String, Document> workingMap;

    public URLHTMLJournal() {
        this.workingMap = new HashMap<>();
    }

    /* retrieves HTML code using "JSoup" library */
    public boolean registerVisit( final String newURL ){
        return false;
    }

    public int getSize(){
        return this.workingMap.size();
    }

    public boolean isEmpty(){
        return 0 == getSize();
    }
}
