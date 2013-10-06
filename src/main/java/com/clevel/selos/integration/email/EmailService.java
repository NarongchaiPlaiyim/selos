package com.clevel.selos.integration.email;

import com.clevel.selos.exception.EmailInterfaceException;
import com.clevel.selos.integration.Email;
import com.clevel.selos.system.Application;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.*;
import java.util.Map;

public abstract class EmailService implements Serializable {
    @Inject
    @Email
    Logger log;
    @Inject
    Application app;

    private static final String FILE_ENCODE = "UTF-8";
    protected static String emailTemplate;

    public void sendMail(String toAddress,String subject,String ccAddress,Map<String,String> valuesMap) throws EmailInterfaceException {
        log.debug("sendMail. (toAddress: {}, subject: {}, ccAddress: {})",toAddress,subject,ccAddress);
//        log.debug("SMTP host : {}",app.getEmailServer());
//
//        if (toAddress == null || "".equalsIgnoreCase(toAddress.trim())) {
//            log.debug("empty email! do nothing.");
//            throw new EmailInterfaceException(msg.key("app.email.exception.address"));
//        }
//
//        Properties p = new Properties();
//        p.put("mail.smtp.host", app.getEmailServer());
//        p.put("mail.smtp.port", app.getEmailPort());
//        p.put("mail.smtp.auth", app.getEmailSmtpAuth());
//
//        try {
//            Session session = Session.getInstance(p, new EmailAuthenticator(app.getEmailUserName(), app.getEmailPassword()));
//            MimeMessage msg = new MimeMessage(session);
//            msg.setFrom(new InternetAddress(app.getEmailUserName()));
//            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress, false));
//            if (ccAddress != null && !"".equalsIgnoreCase(ccAddress.trim())) {
//                msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccAddress, false));
//            }
//
//            msg.setSubject(subject, FILE_ENCODE);
//
//            msg.setContent(replaceTemplate(valuesMap), "text/html; charset="+FILE_ENCODE);
//            msg.setSentDate(new Date());
//
//            Transport.send(msg);
//        } catch (AuthenticationFailedException e) {
//            log.error("",e);
//            throw new EmailInterfaceException("Email login failed!",e);
//        } catch (AddressException e) {
//            log.error("",e);
//            throw new EmailInterfaceException("Email address invalid!",e);
//        } catch (MessagingException e) {
//            log.error("",e);
//            throw new EmailInterfaceException("Email could not be sent!",e);
//        } catch (Exception e) {
//            log.error("Email Exception!",e);
//            throw new EmailInterfaceException("Email could not be sent!",e);
//        }
    }

    private String readTemplateFile(String templateFile) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader bf = null;

        String fileName = templateFile;
        log.debug("Email template file: {}",fileName);
        try {
            fis = new FileInputStream(fileName);
            isr = new InputStreamReader(fis,FILE_ENCODE);
            bf =  new BufferedReader(isr);
        } catch (FileNotFoundException e) {
            log.error("Email template not found (file: {})",fileName, e);
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
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        return sub.replace(messageBody);
    }

    public void setLog(Logger log) {
        this.log = log;
    }

}
