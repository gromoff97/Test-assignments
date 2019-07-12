package home.grom.urljournaling;

import org.testng.annotations.Test;

public class JournalManagingTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void throwsExceptionIfTriesToCreateJournalInstanceWithNullURLSet() {
        new URLHTMLVisitJournal(null);
    }
}
