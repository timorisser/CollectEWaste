package at.collew.account.mail;

import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * This class is for generating and sending Emails
 * @author Patrick Stadt
 * @version 2
 */

@Service
public class EmailService implements EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    Configuration fmConfiguration;

    @Value("${spring.mail.username}")
    String from;


    /**
     * send the email to specified user with an email template
     * @param mail Email object
     * @param templateName name of the template that should be used
     */
    @Override
    @Async
    public void sendEmail(EMail mail, String templateName) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setSubject(mail.getSubject());
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(mail.getTo());
            mail.setContent(getContentFromTemplate(mail.getModel(), templateName));
            mimeMessageHelper.setText(mail.getContent(), true);

            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets the content for the template
     * @param model attributes that are needed in the template
     * @param templateName name of the template that should be used
     * @return emailform
     */
    private String getContentFromTemplate(Map<String, Object> model, String templateName) {
        StringBuilder content = new StringBuilder();

        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate(templateName), model));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
