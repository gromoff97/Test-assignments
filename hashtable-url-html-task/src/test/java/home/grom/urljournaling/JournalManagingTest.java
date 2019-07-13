package home.grom.urljournaling;

import org.testng.annotations.Test;

public class JournalManagingTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void throwsExceptionIfTriesToCreateJournalInstanceWithNullURLSet() {
        new URLHTMLVisitJournal(null);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void throwsExceptionIfTriesToAddElementToJournalKeySet() {
        String url = "https://www.youtube.com/";
        URLHTMLVisitJournal testJournal = new URLHTMLVisitJournal();
        testJournal.getVisitedURLSet().add(url);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void throwsExceptionIfTriesToRemoveElementFromJournalKeySet() {
        String url = "https://www.google.com/";
        URLHTMLVisitJournal testJournal = new URLHTMLVisitJournal();
        testJournal.registerVisit(url);
        testJournal.getVisitedURLSet().remove(url);
    }

}
