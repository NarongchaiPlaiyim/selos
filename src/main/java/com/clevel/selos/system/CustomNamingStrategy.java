package com.clevel.selos.system;

import org.hibernate.cfg.ImprovedNamingStrategy;

public class CustomNamingStrategy extends ImprovedNamingStrategy {
    @Override
    public String columnName(String columnName) {
        return columnName.toLowerCase();
    }

    @Override
    public String tableName(String tableName) {
        return tableName.toLowerCase();
    }
}
