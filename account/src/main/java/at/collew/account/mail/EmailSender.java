package at.collew.account.mail;

/**
 * Interface that implements the sendEmail method
 * @author Patrick Stadt
 * @version 1.1
 */
public interface EmailSender {
    void sendEmail(EMail mail, String templateName);
}
