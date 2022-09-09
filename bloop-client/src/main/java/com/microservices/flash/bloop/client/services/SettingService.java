package com.microservices.flash.bloop.client.services;

import com.microservices.flash.bloop.common.data.entities.setttings.EmailSettingBag;

public interface SettingService {

    public EmailSettingBag getMailSettings();

}
