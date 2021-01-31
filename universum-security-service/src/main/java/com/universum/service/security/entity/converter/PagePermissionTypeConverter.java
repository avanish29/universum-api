package com.universum.service.security.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.universum.service.security.util.PagePermissionType;

@Converter(autoApply = true)
public class PagePermissionTypeConverter implements AttributeConverter<PagePermissionType, String> {

    @Override
    public String convertToDatabaseColumn(PagePermissionType attribute) {
        return attribute != null ? attribute.getPermissionCode() : PagePermissionType.defaultType().getPermissionCode();
    }

    @Override
    public PagePermissionType convertToEntityAttribute(String dbData) {
        return PagePermissionType.findByPermissionCode(dbData);
    }
}
