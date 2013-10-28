package com.clevel.selos.system.message;

public interface Message {
    public String get(String key);

    public String get(String key, String... vars);

    public void setLanguageTh();

    public void setLanguageEn();
}
