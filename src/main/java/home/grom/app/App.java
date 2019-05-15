package home.grom.app;

import home.grom.urljournaling.JournalComparisonNotifier;
import home.grom.urljournaling.URLHTMLVisitJournal;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;

import java.util.ArrayList;
import java.util.List;

/* Just a demonstration */
public class App 
{
    public static void main( String[] args )
    {
        /* Suppose it's yesterday */
        firstUrlList.add("https://wiki.archlinux.org/");
        firstUrlList.add("https://www.google.com/");
        firstUrlList.add("https://github.com/");

        URLHTMLVisitJournal yesterdayJournal = new URLHTMLVisitJournal();
        for (String url : firstUrlList){
            if ( !yesterdayJournal.registerVisit(url)) {
                System.err.println("Couldn't register following url : " + url);
            }
        }
        /* custom registering */
        yesterdayJournal.registerVisit("http://mydearcustomsite.net/",oldHTMLCode);

        /* ----------------------------------- */

        /* Suppose it's today */
        secondUrlList.add("https://context.reverso.net/");
        secondUrlList.add("https://wiki.archlinux.org/");
        secondUrlList.add("http://www.redsys.ru/");
        secondUrlList.add("https://dasdaasdadadda.com/"); /* it will cause exception but program will continue working */

        URLHTMLVisitJournal todayJournal = new URLHTMLVisitJournal();
        for (String url : secondUrlList ){
            if ( !yesterdayJournal.registerVisit(url)) {
                System.err.println("Couldn't register following url : " + url);
            }
        }
        /* custom registering ( imitating modification of page )*/
        todayJournal.registerVisit("http://mydearcustomsite.net/",newHTMLCode);

        JournalComparisonNotifier.sendComparisonResults(
                yesterdayJournal,todayJournal,
                "gromoff97@mail.ru",
                "Sekretar' Sekretar'evna",
                customMailer
        );
    }

    private static List<String> firstUrlList = new ArrayList<>();
    private static List<String> secondUrlList = new ArrayList<>();

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
