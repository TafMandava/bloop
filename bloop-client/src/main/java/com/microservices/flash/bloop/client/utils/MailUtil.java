package com.microservices.flash.bloop.client.utils;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.microservices.flash.bloop.common.data.configs.MailServerConfigData;

public class MailUtil {

    public static String getSiteUrl(HttpServletRequest request) {

        /**
         * Return the full URL of the current request
         */
        String siteUrl = request.getRequestURL().toString();

        /**
         * We must delete the extra URL segment and only keep the site URL of the context path of the application
         *    Replace the Servlet path with an empty string so that it returns only the site URL excluding the servlet path
         */
        return siteUrl.replace(request.getServletPath(), "");

    }

    public static JavaMailSenderImpl prepareMailSender(MailServerConfigData mailServerConfigData) {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(mailServerConfigData.getMailHost());
        javaMailSender.setPort(mailServerConfigData.getMailPort());
        javaMailSender.setUsername(mailServerConfigData.getMailUsername());
        javaMailSender.setPassword(mailServerConfigData.getMailPassword());

        /**
         * Set mail properties of our SMTP Authentication and SMTP secure connection
         */
        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.auth", mailServerConfigData.getSmtpAuth());
        mailProperties.setProperty("mail.smtp.starttls.enable", mailServerConfigData.getSmtpSecured());

        javaMailSender.setJavaMailProperties(mailProperties);

        return javaMailSender;
    }
    
}
