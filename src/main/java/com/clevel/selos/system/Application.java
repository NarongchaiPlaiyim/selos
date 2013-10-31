package com.clevel.selos.system;

import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.UserStatus;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
@Named("app")
public class Application {
    @Inject
    Logger log;

    @Inject
    public Application() {
    }

    @PostConstruct
    public void onCreation() {
    }

    public UserStatus[] getUserStatuses() {
        return UserStatus.values();
    }

    public RadioValue[] getRadioValues(String values) {
        String[] str = values.split("\\|");
        RadioValue[] result = new RadioValue[str.length];
        RadioValue[] radioValue = RadioValue.values();
        log.debug("radioValue length: {}",radioValue.length);
        for (int i=0;i<str.length;i++) {
            for (RadioValue aRadioValue : radioValue) {
                if (aRadioValue.name().equalsIgnoreCase(str[i])) {
                    log.debug("assign value: {}", aRadioValue);
                    result[i] = aRadioValue;
                }
            }
        }
        return result;
    }
}
