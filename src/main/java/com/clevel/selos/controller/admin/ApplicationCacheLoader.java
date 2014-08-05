package com.clevel.selos.controller.admin;

import java.io.Serializable;

//@Singleton
//@Startup
public class ApplicationCacheLoader implements Serializable{
//
//    @Inject
//    @SELOS
//    private Logger logger;
//
//    @Inject
//    private BaseRateControl baseRateControl;
//    @Inject
//    private UserSysParameterControl userSysParameterControl;
//    @Inject
//    private BankAccountTypeControl bankAccountTypeControl;
//
//    public enum State {INITIAL, START, STOP};
//
//    private State state;
//
//    @PostConstruct
//    public void onStartUp(){
//        state = State.INITIAL;
//        logger.info("begin onStartUp server state {}", state);
//
//        //loadCacheDB();
//        state = State.START;
//        logger.info("end onStartUp server state {}", state);
//    }
//
//    private void loadCacheDB(){
//        baseRateControl.loadData();
//        userSysParameterControl.loadData();
//        bankAccountTypeControl.loadData();
//    }
//
//    @PreDestroy
//    public void onShutdown(){
//        state = State.STOP;
//    }
//
//    public State getState(){
//        return state;
//    }
//
//    public void setState(State state){
//        this.state = state;
//    }
}
