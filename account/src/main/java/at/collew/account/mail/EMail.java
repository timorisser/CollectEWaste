package at.collew.account.mail;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * The purpose of this class is to Generate an Email form with the userdata, that later should be user in the EmailService.java class
 * @author Patrick Stadt
 * @version 1
 */

@Getter
@Setter
public class EMail {
    String to;
    String subject;
    String content;
    private Map< String, Object > model;

    /**
     * Constructor that for "Emailbuiling"
     * @param to Email from user
     * @param subject subject of the mail
     * @param content content of the mail
     * @param model attributes in the mail that need to be changed
     */
    public EMail(String to, String subject, String content, Map<String, Object> model) {
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.model = model;
    }
}
