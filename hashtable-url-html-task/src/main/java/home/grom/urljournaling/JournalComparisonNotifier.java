package home.grom.urljournaling;

import org.apache.commons.validator.routines.EmailValidator;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;

/*
 * This class provides only static method, and only one of them is public
 * ( other methods are just "helpers" to this one )
 * */
public final class JournalComparisonNotifier {

    /*
     * This class won't need to create any instances
     * because there are only static methods
     * */
    private JournalComparisonNotifier() {
        throw new AssertionError("Instance of this class is not supposed to be created because it's utility class.");
    }

    private static String createForm( JournalsDifferenceMailData mailData, final String fullName){
        if ( null == mailData ) {
            throw new IllegalArgumentException("Non-null reference to mail data is required.");
        }

        if ( null == fullName || fullName.trim().isEmpty() ){
            throw new IllegalArgumentException("Non-null and non-blank full name is required.");
        }

        return String.format("Dear %s\n\n" +
                        "Here is Journal changes occurred in 24 hours :\n" +
                        "%s" +
                        "Best Wishes,\n" +
                        "Automatic Monitoring System\n",
                fullName,
                mailData
        );
    }

    public static void sendComparisonResults( JournalsDifferenceMailData mailData, final String emailAddress, final String fullName, Mailer mailer) {
        if ( !EmailValidator.getInstance().isValid(emailAddress) ) {
            throw new IllegalArgumentException("Valid email is required.");
        }

        if ( null == mailer ){
            throw new IllegalArgumentException("Non-null reference to mailer is required.");
        }

        String mailContent = createForm( mailData, fullName );
        Email email = EmailBuilder.startingBlank()
                .from("Journal-comparison notifier", "notificationservice@redsys.net")
                .to(fullName, emailAddress)
                .withSubject("One more journal-comparison notifier's notification.")
                .withPlainText(mailContent)
                .buildEmail();

        mailer.sendMail(email);

    }
}
