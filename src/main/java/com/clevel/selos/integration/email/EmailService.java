package com.clevel.selos.integration.email;

import com.clevel.selos.exception.EmailInterfaceException;
import com.clevel.selos.integration.Email;
import com.clevel.selos.integration.EmailInterface;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.audit.SystemAuditor;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public abstract class EmailService implements EmailInterface,Serializable {
    @Inject
    @Email
    Logger log;

    @Inject
    @Config(name = "interface.email.host")
    String host;
    @Inject
    @Config(name = "interface.email.port")
    String port;
    @Inject
    @Config(name = "interface.email.username")
    String username;
    @Inject
    @Config(name = "interface.email.password")
    String password;
    @Inject
    @Config(name = "interface.email.from")
    String from;

    @Inject
    @ExceptionMessage
    Message msg;

    @Inject
    @Email
    SystemAuditor emailAuditor;

    public static final String userId = "system";
    private static final String FILE_ENCODE = "UTF-8";
    protected static String emailTemplate;

    @Override
    public void sendMail(String toAddress,String subject,String ccAddress,Map<String,String> valuesMap) {
        log.debug("sendMail. (toAddress: {}, subject: {}, ccAddress: {})",toAddress,subject,ccAddress);
        log.debug("SMTP host : {}",host);

        if (toAddress == null || "".equalsIgnoreCase(toAddress.trim())) {
            log.debug("empty email address!");
            throw new EmailInterfaceException(ExceptionMapping.EMAIL_EMPTY_ADDRESS, msg.get(ExceptionMapping.EMAIL_EMPTY_ADDRESS));
        }

        Properties p = new Properties();
        p.put("mail.smtp.host", host);
        p.put("mail.smtp.port", port);
        p.put("mail.smtp.auth", "true");
        Date now = new Date();
        String linkKey = Util.getLinkKey(userId);
        try {
            Session session = Session.getInstance(p, new EmailAuthenticator(username, password));
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(toAddress, false));
            if (ccAddress != null && !"".equalsIgnoreCase(ccAddress.trim())) {
                msg.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse(ccAddress, false));
            }

            msg.setSubject(subject, FILE_ENCODE);
            msg.setContent(replaceTemplate(valuesMap), "text/html; charset="+FILE_ENCODE);
            msg.setSentDate(now);

            Transport.send(msg);
            emailAuditor.add(userId,"sendMail","",now, ActionResult.SUCCESS,"", Util.getLinkKey(userId));
        } catch (AuthenticationFailedException e) {
            log.error("[{}]",linkKey,e);
            emailAuditor.add(userId,"sendMail","",now, ActionResult.EXCEPTION,e.getMessage(), linkKey);
            throw new EmailInterfaceException(e,ExceptionMapping.EMAIL_AUTHENTICATION_FAILED,msg.get(ExceptionMapping.EMAIL_AUTHENTICATION_FAILED));
        } catch (AddressException e) {
            log.error("[{}]",linkKey,e);
            emailAuditor.add(userId, "sendMail", "", now, ActionResult.EXCEPTION, e.getMessage(), linkKey);
            throw new EmailInterfaceException(e,ExceptionMapping.EMAIL_INVALID_ADDRESS,msg.get(ExceptionMapping.EMAIL_INVALID_ADDRESS));
        } catch (MessagingException e) {
            log.error("[{}]",linkKey,e);
            emailAuditor.add(userId, "sendMail", "", now, ActionResult.EXCEPTION, e.getMessage(), linkKey);
            throw new EmailInterfaceException(e,ExceptionMapping.EMAIL_COULD_NOT_BE_SENT,msg.get(ExceptionMapping.EMAIL_COULD_NOT_BE_SENT));
        } catch (Exception e) {
            log.error("[{}] Email Exception!",linkKey,e);
            emailAuditor.add(userId, "sendMail", "", now, ActionResult.EXCEPTION, e.getMessage(), linkKey);
            throw new EmailInterfaceException(e,ExceptionMapping.EMAIL_EXCEPTION,msg.get(ExceptionMapping.EMAIL_EXCEPTION));
        }
    }

    private String readTemplateFile(String templateFile) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader bf = null;

        log.debug("Email template file: {}",templateFile);
        try {
            fis = new FileInputStream(templateFile);
            isr = new InputStreamReader(fis,FILE_ENCODE);
            bf =  new BufferedReader(isr);
        } catch (FileNotFoundException e) {
            log.error("Email template not found (file: {})",templateFile, e);
            throw new EmailInterfaceException(e,ExceptionMapping.EMAIL_TEMPLATE_NOT_FOUND,msg.get(ExceptionMapping.EMAIL_TEMPLATE_NOT_FOUND,templateFile));
        } catch (UnsupportedEncodingException e) {
            log.error("UnSupportEncoding! ({})",e.getMessage());
        }
        String line;
        StringBuilder body = new StringBuilder("");
        try {
            while ((line = bf.readLine()) != null) {
                body.append(line);
            }
            bf.close();
            if (isr!=null) {
                isr.close();
            }
            if (fis!=null) {
                fis.close();
            }
        } catch (IOException e) {
            log.error("IO Exception!", e);
        }

        return body.toString();
    }

    protected String replaceTemplate(Map<String,String> valuesMap) {
        String messageBody = readTemplateFile(emailTemplate);
        return StrSubstitutor.replace(messageBody,valuesMap);
    }
}
