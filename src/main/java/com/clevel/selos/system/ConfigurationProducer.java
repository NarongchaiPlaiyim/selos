package com.clevel.selos.system;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

@ApplicationScoped
public class ConfigurationProducer {
    @Inject
    Logger log;

    private volatile static Properties config;
    private static final String bundleName = "selos";

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
        log.debug("load configuration properties done. (size: {})", config.size());
        return config;
    }

    @Produces
    @Config
    public String getConfiguration(InjectionPoint ip) {
        Config configClass = ip.getAnnotated().getAnnotation(Config.class);
        log.trace("key: {}, value: {}", configClass.name(), config.getProperty(configClass.name()));
        return config.getProperty(configClass.name());
    }

    private static Properties getFromResource(String resourceName) {
        Properties p = new Properties();
        ResourceBundle bundle = ResourceBundle.getBundle(resourceName);
        Enumeration e = bundle.getKeys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            p.setProperty(key, bundle.getString(key));
        }
        return p;
    }
}
