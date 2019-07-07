package home.grom.app;

import home.grom.urljournaling.JournalComparisonNotifier;
import home.grom.urljournaling.JournalsDifferenceMailData;
import home.grom.urljournaling.URLHTMLVisitJournal;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

import java.util.HashSet;
import java.util.Set;

/* Just a demonstration */
public class App 
{
    public static void main( String[] args )
    {
        // Suppose it's yesterday
        firstUrlSet.add("https://wiki.archlinux.org/");
        firstUrlSet.add("https://www.google.com/");

        /*
        * It's available to create empty Journal but
        * you may want to create it using List of Strings.
        * */
        URLHTMLVisitJournal yesterdayJournal = new URLHTMLVisitJournal(firstUrlSet);

        /*
        * There are two ways to register the visit
        */

        // registering with help of Jsoup's connection methods
        yesterdayJournal.registerVisit("https://github.com/");

        // manual registering
        yesterdayJournal.registerVisit("http://mydearcustomsite.net/",oldHTMLCode);

        /* ----------------------------------- */

        /* Suppose it's today */
        secondUrlSet.add("https://context.reverso.net/");
        secondUrlSet.add("https://wiki.archlinux.org/");
        secondUrlSet.add("http://www.redsys.ru/");

        /*
         * it will cause *checked* exception during inner registering (i.e. inside constructor)
         * but program will continue working anyway.
         */
        secondUrlSet.add("https://dasdaasdadadda.com/");

        // here it will be caused
        URLHTMLVisitJournal todayJournal = new URLHTMLVisitJournal(secondUrlSet);

        // manual registering ( imitating modification of page )
        todayJournal.registerVisit("http://mydearcustomsite.net/",newHTMLCode);

        /*
         * Everyone has his own settings of mailing.
         * That's why we need to create Mailer's instance before sending.
         * */
        JournalComparisonNotifier.sendComparisonResults(
                new JournalsDifferenceMailData(yesterdayJournal, todayJournal),
                "gromoff97@mail.ru",
                "Sekretar' Sekretar'evna",
                customMailer
        );
    }

    private static Set<String> firstUrlSet = new HashSet<>();
    private static Set<String> secondUrlSet = new HashSet<>();

    private final static String oldHTMLCode =
            "<!doctype html>\n" +
                    "<html>\n" +
                    "  <head>\n" +
                    "    <title>This is the title of the webpage!</title>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "    <p>This is an example of paragraph OLD BUT GOLD</p>\n" +
                    "  </body>\n" +
                    "</html>";

    private final static String newHTMLCode =
            "<!doctype html>\n" +
                    "<html>\n" +
                    "  <head>\n" +
                    "    <title>This is the title of the webpage!</title>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "    <p>This is an example of paragraph SEEMS LIKE NEW</p>\n" +
                    "  </body>\n" +
                    "</html>";

    private final static Mailer customMailer = MailerBuilder
            /* Enter your own arguments */
            .withSMTPServer("smtp.gmail.com",  587, "myemail@somemail.net", "reallygoodpasswd")
            .withTransportStrategy(TransportStrategy.SMTP_TLS)
            .buildMailer();
}
