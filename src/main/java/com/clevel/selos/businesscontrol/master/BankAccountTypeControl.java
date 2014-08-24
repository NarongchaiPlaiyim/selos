package com.clevel.selos.businesscontrol.master;


import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.view.BankAccountTypeView;
import com.clevel.selos.transform.BankAccountTypeTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
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

    public Map<Integer, BankAccountTypeView> loadData(){
        List<BankAccountType> bankAccountTypeList = bankAccountTypeDAO.findAll();
        Map<Integer, BankAccountTypeView> _tmpMap = bankAccountTypeTransform.transformToCache(bankAccountTypeList);
        if(_tmpMap == null)
            return new ConcurrentHashMap<Integer, BankAccountTypeView>();
        else {
            cacheLoader.setCacheMap(BankAccountType.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }
}
