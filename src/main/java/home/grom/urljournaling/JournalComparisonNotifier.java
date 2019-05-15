package home.grom.urljournaling;

import org.simplejavamail.mailer.Mailer;

public final class JournalComparisonNotifier {
    private final static String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    private JournalComparisonNotifier(){}

    public void sendComparisonResults(final URLHTMLVisitJournal yesterdayJournal, final URLHTMLVisitJournal todayJournal,
                                      final String emailAddress, final String fullName,
                                      Mailer mailer){
        if ( null == yesterdayJournal || null == todayJournal ) {
            throw new IllegalArgumentException("Journals are supposed to not reference to null");
        }

        if ( yesterdayJournal == todayJournal ) {
            throw new IllegalArgumentException("Journals are supposed to not reference to each other");
        }

    }
}
