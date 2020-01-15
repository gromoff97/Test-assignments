package home.grom.urljournaling;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class JournalManagingTest {

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "^.*[Nn]ull.*$")
    public void throwsExceptionIfTriesToCreateJournalInstanceWithNullURLSet() {
        new WebPageJournal(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "^.*[Vv]alid.*$")
    public void throwsExceptionIfTriesToRegisterNulledURL() {
        WebPageJournal testJournal = new WebPageJournal();
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
        WebPageJournal testJournal = new WebPageJournal();
        testJournal.registerVisit(validURL, blankString);
    }

    @DataProvider(name = "InvalidURLs")
    public static Object[][] invalidURLs() {
        return new Object[][]{
                {"https://www.y'outub\"e.com"},
                {"https://mail...ru/"},
                {"https:///github.com/"},
                {"htps://www.atlassian.com/"}
        };
    }

    @Test(dataProvider = "InvalidURLs", expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "^.*[Vv]alid.*$")
    public void throwsExceptionIfTriesToRegisterInvalidURL( String url ) {
        WebPageJournal testJournal = new WebPageJournal();
        testJournal.registerVisit(url);
    }

    @Test
    public void journalAfterRegisteringVisitIsNotEmpty() {
        WebPageJournal testJournal = new WebPageJournal();
        Assert.assertTrue(testJournal.isEmpty());
        testJournal.registerVisit("https://stackoverflow.com/");
        Assert.assertFalse(testJournal.isEmpty());
    }

    @Test
    public void HTMLContentForURLAfterRegisteringVisitIsNotBlank() {
        String url = "https://www.google.com/";
        WebPageJournal testJournal = new WebPageJournal();
        testJournal.registerVisit(url);

        String HTMLContent = testJournal.search(url);
        Assert.assertNotNull(HTMLContent);
        Assert.assertFalse(HTMLContent.trim().isEmpty());
    }

    @Test
    public void journalSizeAfterRegisteringIsCorrect() {
        WebPageJournal testJournal = new WebPageJournal();
        Assert.assertEquals(testJournal.getSize(), 0);

        testJournal.registerVisit("https://habr.com/");
        Assert.assertEquals(testJournal.getSize(), 1);

        testJournal.registerVisit("https://yandex.ru/");
        Assert.assertEquals(testJournal.getSize(), 2);
    }

    @Test
    public void journalAfterRegisteringTheSameURLHasNoDuplicates() {
        String url = "https://yandex.ru/";
        WebPageJournal testJournal = new WebPageJournal();
        testJournal.registerVisit(url);
        testJournal.registerVisit(url);
        Assert.assertEquals(testJournal.getSize(), 1);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void throwsExceptionIfTriesToAddElementToJournalKeySet() {
        String url = "https://www.youtube.com/";
        WebPageJournal testJournal = new WebPageJournal();
        testJournal.getVisitedURLSet().add(url);
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void throwsExceptionIfTriesToRemoveElementFromJournalKeySet() {
        String url = "https://www.google.com/";
        WebPageJournal testJournal = new WebPageJournal();
        testJournal.registerVisit(url);
        testJournal.getVisitedURLSet().remove(url);
    }

    @Test
    public void journalEqualsMethodWorksCorrectly() {
        String firstURL = "https://se.ifmo.ru/~korg/";
        String secondURL = "https://www.york.ac.uk/teaching/cws/wws/webpage1.html";

        WebPageJournal firstTestJournal = new WebPageJournal();
        WebPageJournal secondTestJournal = new WebPageJournal();

        Assert.assertEquals(firstTestJournal, secondTestJournal);

        firstTestJournal.registerVisit(firstURL);
        firstTestJournal.registerVisit(secondURL);

        secondTestJournal.registerVisit(secondURL);
        secondTestJournal.registerVisit(firstURL);

        Assert.assertEquals(firstTestJournal, secondTestJournal);
    }

    @Test
    public void journalHashCodeMethodWorksCorrectly() {
        String firstURL = "https://se.ifmo.ru/~korg/";
        String secondURL = "https://www.york.ac.uk/teaching/cws/wws/webpage1.html";

        WebPageJournal firstTestJournal = new WebPageJournal();
        WebPageJournal secondTestJournal = new WebPageJournal();

        Assert.assertEquals(firstTestJournal.hashCode(), secondTestJournal.hashCode());

        firstTestJournal.registerVisit(firstURL);
        secondTestJournal.registerVisit(firstURL);

        Assert.assertEquals(firstTestJournal.hashCode(), secondTestJournal.hashCode());

        firstTestJournal.registerVisit(secondURL);
        secondTestJournal.registerVisit(secondURL);

        Assert.assertEquals(firstTestJournal.hashCode(), secondTestJournal.hashCode());
    }

}
