package home.grom.urljournaling;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;

public final class JournalComparisonNotifier {
    private final static String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    private JournalComparisonNotifier(){}

    private static boolean isValidEmailAddress(final String email){
        if ( null == email ) {
            throw new IllegalArgumentException("Email address is not supposed to reference to null");
        }
        return email.matches(emailRegex);
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

        return "content";
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
