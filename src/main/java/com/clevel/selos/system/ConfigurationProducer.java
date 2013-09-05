package com.clevel.selos.system;

import org.slf4j.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationProducer {
    @Inject
    Logger log;

    private volatile static Properties config;
    public static String configFileLocation = "D://Project-Clevel/tmp/selos.properties";

    public synchronized Properties getProperties() {
        if (config==null) {
            config = new Properties();
            log.debug("loading system configuration properties (file: {})",configFileLocation);
            try {
                config.load(new FileInputStream(configFileLocation));
            } catch (IOException e) {
                log.error("config file not found!");
            }
        }
        return config;
    }

    @Produces @Config
    public String getConfiguration(InjectionPoint ip) {
        Config configClass = ip.getAnnotated().getAnnotation(Config.class);
        Properties config = getProperties();
        log.debug("key: {}, value: {}",configClass.name(),config.getProperty(configClass.name()));
        return config.getProperty(configClass.name());
    }
}
