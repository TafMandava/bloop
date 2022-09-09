package com.microservices.flash.bloop.common.data.entities.setttings;

import java.util.List;

import org.springframework.util.ObjectUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * A bag that holds a list or collection of "Setting" objects
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingBag {

    private List<Setting> settings;

    /**
     * Return a setting object aggregated by key 
     */
    public Setting getSettingByKey(String key) {

        int index = settings.indexOf(new Setting(key));
        return index >= 0 ? settings.get(index) : null;

    }

    /**
     * Return the value related to a given key as a string
     */
    public String getValueByKey(String key) {

        Setting setting = getSettingByKey(key);
        return !ObjectUtils.isEmpty(setting) ? setting.getValue() : null; 

    }

    /**
     * Update setting for a given key and value 
     */
    public void update(String key, String value) {

        log.debug(key); 
        log.debug(value);

        Setting setting = getSettingByKey(key);

        if (!ObjectUtils.isEmpty(setting) && !ObjectUtils.isEmpty(value)) setting.setValue(value);

    }
    
}
