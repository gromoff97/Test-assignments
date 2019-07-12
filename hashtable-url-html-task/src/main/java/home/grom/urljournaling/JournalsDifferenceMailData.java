package home.grom.urljournaling;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class JournalsDifferenceMailData {
    private Set<String> uniqueOldURLs;
    private Set<String> uniqueFreshURLs;
    private Set<String> intersectedURLsWithModifiedHTML;

    public JournalsDifferenceMailData( URLHTMLVisitJournal oldJournal, URLHTMLVisitJournal freshJournal ) {
        if ( null == oldJournal || null == freshJournal ) {
            throw new IllegalArgumentException("Non-null references of journals are required");
        }

        if ( oldJournal == freshJournal ) {
            uniqueOldURLs = new HashSet<>();
            uniqueFreshURLs = new HashSet<>();
            intersectedURLsWithModifiedHTML = new HashSet<>();
            return;
        }

        Set<String> freshURLSet = freshJournal.getVisitedURLSet();
        Set<String> oldURLSet = oldJournal.getVisitedURLSet();

        uniqueOldURLs = getDiffFrom(freshURLSet, oldURLSet);
        uniqueFreshURLs = getDiffFrom(oldURLSet, freshURLSet);
        intersectedURLsWithModifiedHTML = getIntersectedURLsWithDifferentHTML(oldJournal, freshJournal);
    }

    private static Set<String> getDiffFrom( Set<String> a, Set<String> b ) {
        Set<String> tmpSet = new HashSet<>(a);
        if ( !b.isEmpty() ) {
            tmpSet.removeAll(b);
        }
        return tmpSet;
    }

    private static Set<String> getIntersectedURLsWithDifferentHTML( URLHTMLVisitJournal firstJournal, URLHTMLVisitJournal secondJournal ) {
        Set<String> tmpSet = new HashSet<>(firstJournal.getVisitedURLSet());

        tmpSet.retainAll(secondJournal.getVisitedURLSet());
        if ( tmpSet.isEmpty() ) {
            return tmpSet;
        }

        tmpSet.removeIf((URLKey)-> {
            String firstHTML = firstJournal.searchPage(URLKey);
            String secondHTML = secondJournal.searchPage(URLKey);
            return firstHTML.equals(secondHTML);
        });

        return tmpSet;
    }

    public Set<String> getUniqueOldURLs() {
        return Collections.unmodifiableSet(uniqueOldURLs);
    }

    public Set<String> getUniqueFreshURLs() {
        return Collections.unmodifiableSet(uniqueFreshURLs);
    }

    public Set<String> getIntersectedURLsWithModifiedHTML() {
        return Collections.unmodifiableSet(intersectedURLsWithModifiedHTML);
    }

    @Override
    public String toString() {
        return String.format("1. Following URLs disappeared : %s\n" +
                        "2. Following URLs appeared : %s\n" +
                        "3. Following URLs has changed its HTML-content : %s\n",
                uniqueOldURLs, uniqueFreshURLs, intersectedURLsWithModifiedHTML);
    }
}
