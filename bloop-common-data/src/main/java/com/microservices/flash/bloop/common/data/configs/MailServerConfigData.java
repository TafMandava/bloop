package com.microservices.flash.bloop.common.data.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "mail-server-config")
public class MailServerConfigData {

    private String mailHost;
    private int mailPort;
    private String mailUsername;
    private String mailPassword;
    private String smtpAuth;
    private String smtpSecured;
    private String mailFrom;
    private String mailSenderName;  
    
}
