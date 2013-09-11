package com.clevel.selos.system;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

@ApplicationScoped
public class ConfigurationProducer {
    @Inject
    Logger log;

    private volatile static Properties config;
    private static final String internalBundle = "selos";
    private static final String externalLocation = "/tmp/selos.properties";
    private static final boolean useInternalConfig = true;

    @PostConstruct
    public void onCreation() {
        config = loadProperties();
    }

    public Properties loadProperties() {
        log.debug("load configuration properties.");
        if (config==null) {
            if (useInternalConfig) {
                log.debug("using internal configuration file. ({})",internalBundle);
                config = getFromResource(internalBundle);
            } else {
                log.debug("using external configuration file. ({})",externalLocation);
                try {
                    config.load(new FileInputStream(externalLocation));
                } catch (IOException e) {
                    log.error("exception loading configuration file from external location ({})",externalLocation);
                }
            }
        }
        log.debug("load configuration properties done. (size: {})",config.size());
        return config;
    }

    @Produces @Config
    public String getConfiguration(InjectionPoint ip) {
        Config configClass = ip.getAnnotated().getAnnotation(Config.class);
        log.debug("key: {}, value: {}",configClass.name(),config.getProperty(configClass.name()));
        return config.getProperty(configClass.name());
    }

    private static Properties getFromResource(String resourceName) {
        Properties p = new Properties();
        ResourceBundle bundle = ResourceBundle.getBundle(resourceName);
        Enumeration e = bundle.getKeys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            p.setProperty(key,bundle.getString(key));
        }
        return p;
    }
}
