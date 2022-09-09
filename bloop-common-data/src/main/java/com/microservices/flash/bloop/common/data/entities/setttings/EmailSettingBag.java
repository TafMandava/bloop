package com.microservices.flash.bloop.common.data.entities.setttings;

import java.util.List;

public class EmailSettingBag extends SettingBag {

    public EmailSettingBag(List<Setting> settings) {
        super(settings);
    }

    /**
     * Read the email settings 
     */
    public String getMailHost() {
        return super.getValueByKey("MAIL_HOST");
    }

    public int getMailPort() {
        return Integer.parseInt(super.getValueByKey("MAIL_PORT"));
    }
    
    public String getMailUsername() {
        return super.getValueByKey("MAIL_USERNAME");
    }

    public String getMailPassword() {
        return super.getValueByKey("MAIL_PASSWORD");
    }
    
    public String getSmtpAuth() {
        return super.getValueByKey("SMTP_AUTH");
    }

    public String getSmtpSecured() {
        return super.getValueByKey("SMTP_SECURED");
    }
    
    public String getMailFrom() {
        return super.getValueByKey("MAIL_FROM");
    }

    public String getMailSenderName() {
        return super.getValueByKey("MAIL_SENDER_NAME");
    }

    public String getCustomerVerifySubject() {
        return super.getValueByKey("CUSTOMER_VERIFY_SUBJECT");
    }
    
    public String getCustomerVerifyContent() {
        return super.getValueByKey("CUSTOMER_VERIFY_CONTENT");
    }    
    
}