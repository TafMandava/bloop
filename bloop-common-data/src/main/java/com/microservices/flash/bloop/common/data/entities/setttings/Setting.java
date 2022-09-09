package com.microservices.flash.bloop.common.data.entities.setttings;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.microservices.flash.bloop.common.data.entities.BaseEntity;
import com.microservices.flash.bloop.common.data.enums.SettingCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Setting extends BaseEntity {

    @Column(name = "`key`", nullable = false, length = 128, unique = true)
    private String key;

    @Column(nullable = false, length = 1024)
    private String value;
    
	@Enumerated(EnumType.STRING)
	@Column(length = 45, nullable = false)
	private SettingCategory category;

    @Builder
    public Setting(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, String key, String value,
            SettingCategory category) {

        super(id, version, createdDate, lastModifiedDate);

        this.key = key;
        this.value = value;
        this.category = category;
    }


    public Setting(String key) {

        super();

        this.key = key;
        
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Setting other = (Setting) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        return true;
    }

    
}