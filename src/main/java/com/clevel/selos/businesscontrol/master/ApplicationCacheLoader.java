package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.dao.master.CountryDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Country;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ApplicationCacheLoader implements Serializable{

    @Inject
    @SELOS
    private Logger logger;
    @Inject
    CountryDAO countryDAO;
    Map<Integer,Country> countryMap;


    @Inject
    public ApplicationCacheLoader() {
    }

    @PostConstruct
    public void onCreation() {
        logger.debug("onCreation.");
    }

    public void loadCacheDB() {
        logger.debug("loadCacheDB.");
        List<Country> countryList = countryDAO.findAll();

        // load country
        logger.debug("================= Load country =======================");
        countryMap = new ConcurrentHashMap<Integer, Country>();
        for (Country country: countryList) {
            countryMap.put(country.getId(),country);
        }
        // to verify
        Util.listMap(countryMap);
    }

    public Map<Integer, Country> getCountryMap() {
        return countryMap;
    }

    public void setCountryMap(Map<Integer, Country> countryMap) {
        this.countryMap = countryMap;
    }

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
//    }
//
//    public void loadCacheDB(){
//        logger.debug("begin loadCacheDB");
//        baseRateControl.loadData();
//        userSysParameterControl.loadData();
//        bankAccountTypeControl.loadData();
//        state = State.START;
//    }
//
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
