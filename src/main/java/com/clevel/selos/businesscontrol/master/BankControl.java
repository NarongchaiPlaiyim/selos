package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.BankDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Bank;
import com.clevel.selos.model.view.BankView;
import com.clevel.selos.transform.BankTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankControl extends BusinessControl{


    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private BankDAO bankDAO;

    @Inject
    private BankTransform bankTransform;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    public BankControl(){}

    public List<SelectItem> getBankActiveList(){
        logger.debug("-- getBankActiveList --");
        Map<Integer, BankView> _tmpMap = getInternalCacheMap();
        List<SelectItem> bankList = new ArrayList<SelectItem>();
        for(BankView bankView : _tmpMap.values()){
            bankList.add(bankTransform.transformToSelectItem(bankView));
        }
        logger.debug("-- getBankActiveList return bank size: {}", bankList.size());
        return bankList;
    }

    public List<SelectItem> getBankRefinanceList(){
        logger.debug("-- getBankRefinanceList --");
        Map<Integer, BankView> _tmpMap = getInternalCacheMap();
        List<SelectItem> bankList = new ArrayList<SelectItem>();
        for(BankView bankView : _tmpMap.values()){
            if(Util.isTrue(bankView.getActive()) && Util.isTrue(bankView.getRefinance()))
                bankList.add(bankTransform.transformToSelectItem(bankView));
        }
        logger.debug(" getBankRefinanceList return bankList size: {}", bankList.size());
        return bankList;
    }

    private Map<Integer, BankView> loadData(){
        logger.debug("-- begin loadData --");
        List<Bank> bankList = bankDAO.findAll();
        Map<Integer, BankView> _tmpMap = bankTransform.transformToCache(bankList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty BankView");
            return new ConcurrentHashMap<Integer, BankView>();
        } else {
            cacheLoader.setCacheMap(Bank.class.getName(), _tmpMap);
            logger.debug("loadData return BankView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, BankView> getInternalCacheMap(){
        logger.debug("-- getInternalCacheMap --");
        Map<Integer, BankView> _tmpMap = cacheLoader.getCacheMap(Bank.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        logger.debug("getInternalCacheMap return BankView size: {}", _tmpMap.size());
        return _tmpMap;
    }

}
