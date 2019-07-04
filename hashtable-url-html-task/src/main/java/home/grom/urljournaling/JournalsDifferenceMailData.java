package home.grom.urljournaling;

import java.util.HashSet;
import java.util.Set;

public final class JournalsDifferenceMailData {
    private URLHTMLVisitJournal oldJournal;
    private URLHTMLVisitJournal freshJournal;

    private Set<String> uniqueOldUrls;
    private Set<String> uniqueFreshUrls;

    public JournalsDifferenceMailData( URLHTMLVisitJournal oldJournal, URLHTMLVisitJournal freshJournal ) {
        if ( null == oldJournal || null == freshJournal ) {
            throw new IllegalArgumentException("Non-null references of journals are required");
        }

        if ( oldJournal == freshJournal ) {
            throw new IllegalArgumentException("Different journals' instances are required");
        }

        this.oldJournal = oldJournal;
        this.freshJournal = freshJournal;

        uniqueOldUrls = getDiffFrom(this.freshJournal.getVisitedURLSet(),this.oldJournal.getVisitedURLSet());
        uniqueFreshUrls = getDiffFrom(this.oldJournal.getVisitedURLSet(),this.freshJournal.getVisitedURLSet());
    }

    private static Set<String> getDiffFrom( Set<String> a, Set<String> b ) {
        Set<String> tmpSet = new HashSet<>(a);
        if ( !b.isEmpty() ) {
            tmpSet.removeAll(b);
        }
        return tmpSet;
    }
}
