package com.clevel.selos.controller.admin;

import com.clevel.selos.businesscontrol.UserSysParameterControl;
import com.clevel.selos.businesscontrol.master.BankAccountTypeControl;
import com.clevel.selos.businesscontrol.master.BaseRateControl;
import com.clevel.selos.integration.SELOS;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.io.Serializable;

@Singleton
@Startup
public class ApplicationCacheLoader implements Serializable{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private BaseRateControl baseRateControl;
    @Inject
    private UserSysParameterControl userSysParameterControl;
    @Inject
    private BankAccountTypeControl bankAccountTypeControl;

    public enum State {INITIAL, START, STOP};

    private State state;

    @PostConstruct
    public void onStartUp(){
        state = State.INITIAL;
        logger.info("begin onStartUp server state {}", state);

        loadCacheDB();
        state = State.START;
        logger.info("end onStartUp server state {}", state);
    }

    private void loadCacheDB(){
        baseRateControl.loadData();
        userSysParameterControl.loadData();
        bankAccountTypeControl.loadData();
    }

    @PreDestroy
    public void onShutdown(){
        state = State.STOP;
    }

    public State getState(){
        return state;
    }

    public void setState(State state){
        this.state = state;
    }
}
