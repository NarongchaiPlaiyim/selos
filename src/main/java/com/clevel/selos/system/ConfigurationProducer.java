package com.clevel.selos.system;

import com.clevel.selos.security.encryption.EncryptionService;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

@ApplicationScoped
public class ConfigurationProducer {
    @Inject
    Logger log;

    private volatile static Properties config;
    private static final String bundleName = "selos";
    @Inject
    EncryptionService encryptionService;
    @Config(name = "system.encryption.enable")
    String encryptionEnable;

    @PostConstruct
    public void onCreation() {
        config = loadProperties();
    }

    public Properties loadProperties() {
        log.debug("load configuration properties.");
        if (config == null) {
            log.debug("loading configuration file. (bundleName: {})", bundleName);
            config = getFromResource(bundleName);
        }
        Enumeration keys = config.keys();
        log.debug("===== selos.properties begin =====");
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            log.debug("key: {}, value: {}", key, config.getProperty(key));
        }
        log.debug("===== selos.properties end =====");
        log.debug("load configuration properties done. (size: {})", config.size());
        return config;
    }

    @Produces
    @Config
    public String getConfiguration(InjectionPoint ip) {
        Config configClass = ip.getAnnotated().getAnnotation(Config.class);
        return config.getProperty(configClass.name());
    }

    private static Properties getFromResource(String resourceName) {
        Properties p = new Properties();
        ResourceBundle bundle = ResourceBundle.getBundle(resourceName, new Locale("th", "TH"));
        Enumeration e = bundle.getKeys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            p.setProperty(key, bundle.getString(key));
        }
        return p;
    }
}
