package com.microservices.flash.bloop.client.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.flash.bloop.common.data.entities.setttings.Setting;
import com.microservices.flash.bloop.common.data.enums.SettingCategory;

public interface SettingRepository extends JpaRepository<Setting, UUID> {

    public List<Setting> findByCategory(SettingCategory category);
    
}
