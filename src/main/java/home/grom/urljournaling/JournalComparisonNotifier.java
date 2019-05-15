package home.grom.urljournaling;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;

import java.util.HashSet;
import java.util.Set;

public final class JournalComparisonNotifier {
    private final static String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    private JournalComparisonNotifier(){}

    private static boolean isValidEmailAddress(final String email){
        if ( null == email ) {
            throw new IllegalArgumentException("Email address is not supposed to reference to null");
        }
        return email.matches(emailRegex);
    }

    private static Set getDisappearedURLs(final URLHTMLVisitJournal yesterdayJournal, final URLHTMLVisitJournal todayJournal){
        Set resultSet = yesterdayJournal.getVisitedURLSet();
        Set todayURLs = todayJournal.getVisitedURLSet();

        if ( todayJournal.isEmpty() ) {
            return resultSet;
        }

        resultSet.removeAll(todayURLs);
        return resultSet;
    }

    private static Set getNewURLs(final URLHTMLVisitJournal yesterdayJournal, final URLHTMLVisitJournal todayJournal){
        Set resultSet = todayJournal.getVisitedURLSet();
        Set yesterdayURLs = yesterdayJournal.getVisitedURLSet();

        if ( yesterdayJournal.isEmpty() ) {
            return resultSet;
        }

        resultSet.removeAll(yesterdayURLs);
        return resultSet;
    }

    private static Set getHTMLModifiedURLs(final URLHTMLVisitJournal yesterdayJournal, final URLHTMLVisitJournal todayJournal){
        Set resultSet = todayJournal.getVisitedURLSet();
        Set yesterdayURLs = yesterdayJournal.getVisitedURLSet();

        /* got interception of given journals */
        resultSet.retainAll(yesterdayURLs);

        /* no point to continue if following condition is true */
        if ( resultSet.isEmpty() ) {
            return resultSet;
        }

        resultSet.removeIf((urlKey)->
            yesterdayJournal.getVisitedHTMLPage((String) urlKey).equals(yesterdayJournal.getVisitedHTMLPage((String) urlKey))
        );

        return resultSet;
    }

    private static String formatURLSet(final Set URLSet){
        if ( URLSet.isEmpty() ) {
            return "--empty--";
        }

        return "{ " + String.join(" , ",URLSet) + " }";
    }

    private static String createForm(final URLHTMLVisitJournal yesterdayJournal, final URLHTMLVisitJournal todayJournal, final String fullName){
        if ( null == yesterdayJournal || null == todayJournal ) {
            throw new IllegalArgumentException("Referencing any Journal to null is not allowed");
        }

        if ( yesterdayJournal == todayJournal ) {
            throw new IllegalArgumentException("Referencing Journals to each other is not allowed");
        }

        if ( null == fullName || fullName.trim().isEmpty() ){
            throw new IllegalArgumentException("Fullname is supposed to not reference to null or be empty");
        }

        return String.format("Dear %s\n\n" +
                        "Here is Journal changes occurred in 24 hours :\n" +
                        "1. Following URLs disappeared : %s\n" +
                        "2. Following URLs appeared : %s\n" +
                        "3. Following URLs has changed its HTML-content : %s\n\n" +
                        "Best Wishes,\n" +
                        "Automatic Monitoring System\n",
                fullName,
                formatURLSet(getDisappearedURLs(yesterdayJournal,todayJournal)),
                formatURLSet(getNewURLs(yesterdayJournal,todayJournal)),
                formatURLSet(getHTMLModifiedURLs(yesterdayJournal,todayJournal))
        );
    }

    public static void sendComparisonResults(final URLHTMLVisitJournal yesterdayJournal, final URLHTMLVisitJournal todayJournal,
                                      final String emailAddress, final String fullName,
                                      Mailer mailer){
        if ( !isValidEmailAddress(emailAddress) ) {
            throw new IllegalArgumentException("Email is supposed to be valid");
        }

        if ( null == mailer ){
            throw new IllegalArgumentException("Referencing Mailer to null is not allowed");
        }

        String mailContent = createForm( yesterdayJournal, todayJournal, fullName );
        Email email = EmailBuilder.startingBlank()
                .from("Journal-comparison notifier", "notificationservice@redsys.net")
                .to(fullName, emailAddress)
                .withSubject("One more journal-comparison notifier's notification")
                .withPlainText(mailContent)
                .buildEmail();

        mailer.sendMail(email);

    }
}
