package com.clevel.selos.businesscontrol.master;


import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.view.master.BankAccountTypeView;
import com.clevel.selos.transform.master.BankAccountTypeTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Stateless
public class BankAccountTypeControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private BankAccountTypeDAO bankAccountTypeDAO;

    @Inject
    private BankAccountTypeTransform bankAccountTypeTransform;

    @Inject
    public BankAccountTypeControl(){}

    public List<BankAccountTypeView> getBankAccountTypeViewActiveList() {
        logger.debug("-- getBankAccountTypeViewActiveList --");
        Map<Integer, BankAccountTypeView> bankAccountTypeViewMap = getInternalCacheMap();
        List<BankAccountTypeView> bankAccountViewList = new ArrayList<BankAccountTypeView>();
        for(BankAccountTypeView bankAccountTypeView : bankAccountTypeViewMap.values()){
            if(Util.isTrue(bankAccountTypeView.getActive())){
                bankAccountViewList.add(bankAccountTypeView);
            }
        }
        logger.debug("-- getBankAccountTypeViewActiveList return bankAccountViewList size: {}", bankAccountViewList.size());
        return bankAccountViewList;
    }

    public List<BankAccountTypeView> getOpenAccountTypeList(){
        logger.debug("-- getOpenAccountTypeList --");

        Map<Integer, BankAccountTypeView> bankAccountTypeViewMap = getInternalCacheMap();
        List<BankAccountTypeView> bankAccountTypeViewList = new ArrayList<BankAccountTypeView>();

        logger.debug("get getOpenAccountTypeList {}", bankAccountTypeViewList);
        for(BankAccountTypeView bankAccountTypeView : bankAccountTypeViewMap.values()){
            logger.info("--- looping {}", bankAccountTypeView);
            //active = 1, openAccountFlag = 1, othBankStatementFlag = 0, bankStatementFlag = 1
            if( bankAccountTypeView.getActive() == 1 && bankAccountTypeView.getOpenAccountFlag() == 1 &&
                bankAccountTypeView.getOthBankStatementFlag() == 0 && bankAccountTypeView.getBankStatementFlag() == 1){
                bankAccountTypeViewList.add(bankAccountTypeView);
                logger.info("Add Open Account type: {}", bankAccountTypeView);
            }
        }
        logger.debug("getOpenAccountTypeList return bankAccountTypeViewList size: {}", bankAccountTypeViewList.size());
        return bankAccountTypeViewList;
    }

    private Map<Integer, BankAccountTypeView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap --");
        Map<Integer, BankAccountTypeView> _tmpMap = cacheLoader.getCacheMap(BankAccountType.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        logger.debug("getInternalCacheMap return BankAccountTypeView size: {}", _tmpMap.size());
        return _tmpMap;
    }

    private Map<Integer, BankAccountTypeView> loadData(){
        logger.debug("-- loadData --");
        List<BankAccountType> bankAccountTypeList = bankAccountTypeDAO.findAll();
        Map<Integer, BankAccountTypeView> _tmpMap = bankAccountTypeTransform.transformToCache(bankAccountTypeList);
        if(_tmpMap == null) {
            logger.debug("return empty BankAccountTypeView");
            return new ConcurrentHashMap<Integer, BankAccountTypeView>();
        } else {
            cacheLoader.setCacheMap(BankAccountType.class.getName(), _tmpMap);
            logger.debug("loadData return BankAccountTypeView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }
}
