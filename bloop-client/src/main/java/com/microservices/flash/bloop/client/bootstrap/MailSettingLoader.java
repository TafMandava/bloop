package com.microservices.flash.bloop.client.bootstrap;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.microservices.flash.bloop.client.repositories.SettingRepository;
import com.microservices.flash.bloop.common.data.entities.setttings.Setting;
import com.microservices.flash.bloop.common.data.enums.SettingCategory;

/**
 *  Run everytime the Spring Context starts
 *      The class can run on startup
 *  "bootstrap" Booting up from a computer that comes from an old phrase of pull yourself up by your bootstraps 
 */
@Component
public class MailSettingLoader implements CommandLineRunner {

    private final SettingRepository settingRepository;

    public MailSettingLoader(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadMailSettingObjects();
    }

    private void loadMailSettingObjects() {
        if(settingRepository.count() == 0) {

            createSiteNameGeneralSettings();
            createMailServerSettings();
            createMailTemplatesSettings();        

        }
    }


    void createSiteNameGeneralSettings() {;

        Setting siteName = Setting.builder()
                                    .key("SITE_NAME")
                                    .value("Bloop")
                                    .category(SettingCategory.GENERAL)
                                    .build();
        
        settingRepository.save(siteName);
        
    }    

    void createMailServerSettings() {

        Setting mailHost = Setting.builder()
                                    .key("MAIL_HOST")
                                    .value("smtp.gmail.com")
                                    .category(SettingCategory.MAIL_SERVER)
                                    .build();

        Setting mailPort = Setting.builder()
                                    .key("MAIL_PORT")
                                    .value("587")
                                    .category(SettingCategory.MAIL_SERVER)
                                    .build();

        Setting mailUsername = Setting.builder()
                                    .key("MAIL_USERNAME")
                                    .value("kuvaka.padombo@gmail.com")
                                    .category(SettingCategory.MAIL_SERVER)
                                    .build();

        Setting smtpAuth = Setting.builder()
                                    .key("SMTP_AUTH")
                                    .value("true")
                                    .category(SettingCategory.MAIL_SERVER)
                                    .build();

        Setting smtpSecured = Setting.builder()
                                    .key("SMTP_SECURED")
                                    .value("true")
                                    .category(SettingCategory.MAIL_SERVER)
                                    .build();       
                                                                 
        Setting mailFrom = Setting.builder()
                                    .key("MAIL_FROM")
                                    .value("kuvaka.padombo@gmail.com")
                                    .category(SettingCategory.MAIL_SERVER)
                                    .build();
                                    
        Setting mailSenderName = Setting.builder()
                                    .key("MAIL_SENDER_NAME")
                                    .value("Bloop Support Team")
                                    .category(SettingCategory.MAIL_SERVER)
                                    .build();

        settingRepository.saveAll(List.of(mailHost, mailPort, mailUsername, smtpAuth, smtpSecured, mailFrom, mailSenderName));
    }


    void createMailTemplatesSettings() {
    
        Setting customerVerifySubject = Setting.builder()
                                    .key("CUSTOMER_VERIFY_SUBJECT")
                                    .value("Please verify your registration to continue blooping")
                                    .category(SettingCategory.MAIL_TEMPLATES)
                                    .build();
                                    
        Setting customerVerifyContent = Setting.builder()
                                    .key("CUSTOMER_VERIFY_CONTENT")
                                    .value(
                                        "<span style=\"font-size:14px;\"></span><span style=\"font-size:14px;\">Dear [[name]],&nbsp;<br>" + 
                                        "<br>" +
                                        "Please click the link below to verify your registration</span><div><br></div><div><a href=\"[[URL]]\" target=\"_self\">Verify email address</a><div><div><span style=\"font-size:14px;\"></span></div><div><span style=\"font-size:14px;\"><br>" +
                                        "Kind regards,<br>" +
                                        "Your Bloop Consumer Support Team</span><div><div><span style=\"font-size:14px;\"></span><span style=\"font-size:12px;\"></span></div></div></div></div></div>"
                                    )
                                    .category(SettingCategory.MAIL_TEMPLATES)
                                    .build();
                                    
        settingRepository.saveAll(List.of(customerVerifySubject, customerVerifyContent));
        
    }


}
