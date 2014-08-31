package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.transform.*;
import com.clevel.selos.transform.master.*;
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

    @Inject private BankAccountProductDAO bankAccountProductDAO;
    @Inject private BankAccountProductTransform bankAccountProductTransform;

    @Inject private ProductGroupDAO productGroupDAO;
    @Inject private ProductTransform productTransform;

    @Inject private SpecialProgramDAO specialProgramDAO;
    @Inject private SpecialProgramTransform specialProgramTransform;

    @Inject private RequestTypeDAO requestTypeDAO;
    @Inject private RequestTypeTransform requestTypeTransform;

    @Inject private RiskTypeDAO riskTypeDAO;
    @Inject private RiskTypeTransform riskTypeTransform;

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

        List<BankAccountProduct> bankAccountProductList = bankAccountProductDAO.findAll();
        indexHash.put(BankAccountProduct.class.getName(), bankAccountProductTransform.transformToCache(bankAccountProductList));

        List<ProductGroup> productGroupList = productGroupDAO.findAll();
        indexHash.put(ProductGroup.class.getName(), productTransform.transformToCache(productGroupList));

        List<SpecialProgram> specialProgramList = specialProgramDAO.findAll();
        indexHash.put(SpecialProgram.class.getName(), specialProgramTransform.transformToCache(specialProgramList));

        List<RequestType> requestTypeList = requestTypeDAO.findAll();
        indexHash.put(RequestType.class.getName(), requestTypeTransform.transformToCache(requestTypeList));

        List<RiskType> riskTypeList = riskTypeDAO.findAll();
        indexHash.put(RiskType.class.getName(), riskTypeTransform.transformToCache(riskTypeList));

        Util.listMap(indexHash);
    }

    public Map getCacheMap(String className){
        return indexHash.get(className);
    }

    public void setCacheMap(String className, Map cacheMap){
        indexHash.put(className, cacheMap);
    }
}
