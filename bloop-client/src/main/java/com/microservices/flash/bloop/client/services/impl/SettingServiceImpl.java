package com.microservices.flash.bloop.client.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.microservices.flash.bloop.client.repositories.SettingRepository;
import com.microservices.flash.bloop.client.services.SettingService;
import com.microservices.flash.bloop.common.data.entities.setttings.EmailSettingBag;
import com.microservices.flash.bloop.common.data.entities.setttings.Setting;
import com.microservices.flash.bloop.common.data.enums.SettingCategory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;

    @Override
    public EmailSettingBag getMailSettings() {
        List<Setting> settings = new ArrayList<>();

        List<Setting> mailServerSettings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
        List<Setting> mailTemplateSettings = settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);

        settings.addAll(mailServerSettings);
        settings.addAll(mailTemplateSettings);

        return new EmailSettingBag(settings);

    }
    
}