package home.grom.urljournaling;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class JournalManagingTest {

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "^.*[Nn]ull.*$")
    public void throwsExceptionIfTriesToCreateJournalInstanceWithNullURLSet() {
        new URLHTMLVisitJournal(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "^.*[Nn]ull.*$")
    public void throwsExceptionIfTriesToRegisterNulledURL() {
        URLHTMLVisitJournal testJournal = new URLHTMLVisitJournal();
        testJournal.registerVisit(null);
    }

    @DataProvider(name = "BlankContent")
    public static Object[][] blankHTMLs() {
        return new Object[][]{
                {null},
                {""},
                {"  "}
        };
    }

    @Test(dataProvider = "BlankContent", expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "^.*[Bb]lank.*$")
    public void throwsExceptionIfTriesToRegisterValidURLWithBlankHTML(String blankString) {
        String validURL = "https://grafana.com/";
        URLHTMLVisitJournal testJournal = new URLHTMLVisitJournal();
        testJournal.registerVisit(validURL, blankString);
    }

    @DataProvider(name = "InvalidURLs")
    public static Object[][] credentials() {
        return new Object[][]{
                {"https://www.y'outub\"e.com"},
                {"https://mail...ru/"},
                {"https:///github.com/"},
                {"htps://www.atlassian.com/"}
        };
    }

    @Test(dataProvider = "InvalidURLs", expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "^.*[Vv]alid.*$")
    public void throwsExceptionIfTriesToRgisterInvalidURL( String url ) {
        URLHTMLVisitJournal testJournal = new URLHTMLVisitJournal();
        testJournal.registerVisit(url);
    }

    @Test
    public void journalAfterRegisteringVisitIsNotEmpty() {
        URLHTMLVisitJournal testJournal = new URLHTMLVisitJournal();
        Assert.assertTrue(testJournal.isEmpty());
        testJournal.registerVisit("https://stackoverflow.com/");
        Assert.assertFalse(testJournal.isEmpty());
    }

    @Test
    public void journalSizeAfterRegisteringIsCorrect() {
        URLHTMLVisitJournal testJournal = new URLHTMLVisitJournal();
        Assert.assertEquals(testJournal.getSize(), 0);

        testJournal.registerVisit("https://habr.com/");
        Assert.assertEquals(testJournal.getSize(), 1);

        testJournal.registerVisit("https://yandex.ru/");
        Assert.assertEquals(testJournal.getSize(), 2);
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
