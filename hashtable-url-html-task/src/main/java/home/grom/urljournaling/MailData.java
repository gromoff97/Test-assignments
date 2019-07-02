package home.grom.urljournaling;

public final class MailData {
    private URLHTMLVisitJournal oldJournal;
    private URLHTMLVisitJournal freshJournal;

    public MailData( URLHTMLVisitJournal oldJournal, URLHTMLVisitJournal freshJournal ) {
        if ( null == oldJournal || null == freshJournal ) {
            throw new IllegalArgumentException("Non-null references of journals are required");
        }

        if ( oldJournal == freshJournal ) {
            throw new IllegalArgumentException("Different journals' instances are required");
        }

        this.oldJournal = oldJournal;
        this.freshJournal = freshJournal;
    }
}
