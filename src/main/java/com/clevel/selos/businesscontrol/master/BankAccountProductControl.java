package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.BankAccountProductDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountProduct;
import com.clevel.selos.model.view.master.BankAccountProductView;
import com.clevel.selos.transform.master.BankAccountProductTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankAccountProductControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;
    @Inject
    private BankAccountProductDAO bankAccountProductDAO;
    @Inject
    private BankAccountProductTransform bankAccountProductTransform;

    @Inject
    public BankAccountProductControl(){}

    public List<BankAccountProductView> getAccountProductByAccountTypeId(int bankAccountTypeId){
        logger.debug("-- begin getAccountProductByAccountTypeId, bankAccountTypeId: {}", bankAccountTypeId);
        Map<Integer, BankAccountProductView> _tmpMap = getInternalCacheMap();

        List<BankAccountProductView> bankAccountProductViewList = new ArrayList<BankAccountProductView>();
        for(BankAccountProductView bankAccountProductView : _tmpMap.values()){
            if(Util.isTrue(bankAccountProductView.getActive()) && (bankAccountProductView.getBankAccountTypeId() == bankAccountTypeId)){
                bankAccountProductViewList.add(bankAccountProductView);
            }
        }
        logger.debug("getAccountProductByAccountTypeId return BankAccountProductView size: {}", bankAccountProductViewList.size());
        return bankAccountProductViewList;
    }

    private Map<Integer, BankAccountProductView> loadData(){
        logger.debug("-- begin loadData --");
        List<BankAccountProduct> bankAccountProductList = bankAccountProductDAO.findAll();
        Map<Integer, BankAccountProductView> _tmpMap = bankAccountProductTransform.transformToCache(bankAccountProductList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty BankAccountProductView");
            return new ConcurrentHashMap<Integer, BankAccountProductView>();
        } else {
            cacheLoader.setCacheMap(BankAccountProduct.class.getName(), _tmpMap);
            logger.debug("loadData return BankAccountProductView size: {}", _tmpMap.size());
            return _tmpMap;
        }
    }

    private Map<Integer, BankAccountProductView> getInternalCacheMap(){
        logger.debug("-- begin getInternalCacheMap --");
        Map<Integer, BankAccountProductView> _tmpMap = cacheLoader.getCacheMap(BankAccountProduct.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        logger.debug("getInternalCacheMap return BankAccountProductView size: {}", _tmpMap.size());
        return _tmpMap;
    }

}
