package com.clevel.selos.system.message;

import com.clevel.selos.model.Language;

public interface Message {
    public String get(String key);
    public void setLanguageTh();
    public void setLanguageEn();
}