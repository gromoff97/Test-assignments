package home.grom.urljournaling;

import org.testng.Assert;
import org.testng.annotations.Test;

public class JournalManagingTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void throwsExceptionIfTriesToCreateJournalInstanceWithNullURLSet() {
        new URLHTMLVisitJournal(null);
    }

    @Test
    public void journalAfterRegisteringVisitIsNotEmpty() {
        URLHTMLVisitJournal testJournal = new URLHTMLVisitJournal();
        Assert.assertTrue(testJournal.isEmpty());
        testJournal.registerVisit("https://stackoverflow.com/");
        Assert.assertFalse(testJournal.isEmpty());
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
