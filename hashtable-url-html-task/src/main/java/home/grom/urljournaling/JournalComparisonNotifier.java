package home.grom.urljournaling;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;

import java.util.Set;

/*
 * This class provides only static method, and only one of them is public
 * ( other methods are just "helpers" to this one )
 * */
public final class JournalComparisonNotifier {
    // It could be better but IMO it seems good enough for attendance test
    // Reference : https://howtodoinjava.com/regex/java-regex-validate-email-address/
    private final static String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    /*
     * This class won't need to create any instances
     * because there are only static methods
     * */
    private JournalComparisonNotifier() {
        throw new AssertionError("Instance of this class is not supposed to be created because it's utility class.");
    }

    private static boolean isValidEmailAddress(final String email){
        if ( null == email ) {
            throw new IllegalArgumentException("Email address is not supposed to reference to null");
        }
        return email.matches(emailRegex);
    }

    private static String formatURLSet(final Set<String> URLSet){
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
