package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.UserSysParameterControl;
import com.clevel.selos.integration.SELOS;
import org.primefaces.context.ApplicationContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;

@Singleton
@ApplicationScoped
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
    }

    public void loadCacheDB(){
        logger.debug("begin loadCacheDB");
        baseRateControl.loadData();
        userSysParameterControl.loadData();
        bankAccountTypeControl.loadData();
        state = State.START;
    }

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
