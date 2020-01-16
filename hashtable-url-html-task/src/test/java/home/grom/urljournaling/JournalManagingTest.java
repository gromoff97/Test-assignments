package home.grom.urljournaling;

import home.grom.utils.ValidationUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class JournalManagingTest {

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "^.*[Nn]ull.*$")
    public void throwsExceptionIfTriesToCreateJournalInstanceWithNullURLSet() {
        Iterable<String> nullRefIterable = null;
        new WebPageJournal(nullRefIterable);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "^.*[Nn]ull.*$")
    public void throwsExceptionIfTriesToCopyJournalWithNullInstance() {
        WebPageJournal nullRefJournal = null;
        new WebPageJournal(nullRefJournal);
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

        boolean visitsWithBlankContentExists = testJournal.visits()
                .map(WebPageJournal.Visit::getContent)
                .anyMatch(ValidationUtils::isBlank);

        Assert.assertFalse(visitsWithBlankContentExists);
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
    public void journalAfterRegisteringTheSameURLHasDuplicates() {
        String url = "https://yandex.ru/";
        WebPageJournal testJournal = new WebPageJournal();
        testJournal.registerVisit(url);
        testJournal.registerVisit(url);
        Assert.assertEquals(testJournal.getSize(), 2);
    }

    @Test
    public void journalEqualsMethodWorksCorrectly() throws InterruptedException {
        String url = "https://se.ifmo.ru/~korg/";

        // Make sure two empty journals are equal.
        WebPageJournal firstTestJournal = new WebPageJournal();
        WebPageJournal secondTestJournal = new WebPageJournal();
        Assert.assertEquals(firstTestJournal, secondTestJournal);

        // Make sure two journals with same url but different creation time are not equal.
        firstTestJournal.registerVisit(url);
        Thread.sleep(1000);
        secondTestJournal.registerVisit(url);
        Assert.assertNotEquals(firstTestJournal, secondTestJournal);

        // Make sure two identical journals are equal.
        WebPageJournal thirdTestJournal = new WebPageJournal(secondTestJournal);
        Assert.assertEquals(secondTestJournal, thirdTestJournal);
    }

    @Test
    public void journalHashCodeMethodWorksCorrectly() throws InterruptedException {
        String url = "https://www.york.ac.uk/teaching/cws/wws/webpage1.html";

        // Make sure two empty journals have the same hashcode-values.
        WebPageJournal firstTestJournal = new WebPageJournal();
        WebPageJournal secondTestJournal = new WebPageJournal();
        Assert.assertEquals(firstTestJournal.hashCode(), secondTestJournal.hashCode());

        // Make sure two journals with same url but different creation time have different hashcode-values.
        firstTestJournal.registerVisit(url);
        Thread.sleep(1000);
        secondTestJournal.registerVisit(url);
        Assert.assertNotEquals(firstTestJournal.hashCode(), secondTestJournal.hashCode());

        // Make sure two identical journals have the same hashcode-values.
        WebPageJournal thirdTestJournal = new WebPageJournal(secondTestJournal);
        Assert.assertEquals(secondTestJournal.hashCode(), thirdTestJournal.hashCode());
    }

}
