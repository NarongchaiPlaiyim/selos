package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.dao.master.BankAccountPurposeDAO;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.BaseRateDAO;
import com.clevel.selos.dao.master.CountryDAO;
import com.clevel.selos.dao.working.OpenAccountPurposeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountPurpose;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.db.master.BaseRate;
import com.clevel.selos.model.db.master.Country;
import com.clevel.selos.transform.*;
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

    private Map<String, Map> indexHash = null;

    @Inject private CountryDAO countryDAO;
    @Inject private CountryTransform countryTransform;

    @Inject private BankAccountTypeDAO bankAccountTypeDAO;
    @Inject private BankAccountTypeTransform bankAccountTypeTransform;

    @Inject private BaseRateDAO baseRateDAO;
    @Inject private BaseRateTransform baseRateTransform;

    @Inject private BankAccountPurposeDAO bankAccountPurposeDAO;
    @Inject private BankAccountPurposeTransform bankAccountPurposeTransform;

    @Inject
    public ApplicationCacheLoader() {
    }

    @PostConstruct
    public void onCreation() {
        logger.debug("onCreation.");
        indexHash = new ConcurrentHashMap<String, Map>();
    }

    public void loadCacheDB() {
        logger.debug("loadCacheDB.");
        List<Country> countryList = countryDAO.findAll();
        indexHash.put(Country.class.getName(), countryTransform.transformToCache(countryList));

        List<BankAccountType> bankAccountTypeList = bankAccountTypeDAO.findAll();
        indexHash.put(BankAccountType.class.getName(), bankAccountTypeTransform.transformToCache(bankAccountTypeList));

        List<BaseRate> baseRateList = baseRateDAO.findAll();
        indexHash.put(BaseRate.class.getName(), baseRateTransform.transformToCache(baseRateList));

        List<BankAccountPurpose> bankAccountPurposeList = bankAccountPurposeDAO.findAll();
        indexHash.put(BankAccountPurpose.class.getName(), bankAccountPurposeTransform.transformToCache(bankAccountPurposeList));

        Util.listMap(indexHash);
    }

    public Map getCacheMap(String className){
        return indexHash.get(className);
    }

    public void setCacheMap(String className, Map cacheMap){
        indexHash.put(className, cacheMap);
    }
}
