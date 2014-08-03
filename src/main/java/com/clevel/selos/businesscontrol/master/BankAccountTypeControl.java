package com.clevel.selos.businesscontrol.master;


import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.view.BankAccountTypeView;
import com.clevel.selos.transform.BankAccountTypeTransform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankAccountTypeControl extends BusinessControl{


    //Initial Map to cache the data of BaseRateContol
    //Initial mutex object to guaranteed thread safe
    private static Map<Integer, BankAccountTypeView> bankAccountTypeViewMap;
    private static Object _mutexLock = new Object();

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private BankAccountTypeDAO bankAccountTypeDAO;

    @Inject
    private BankAccountTypeTransform bankAccountTypeTransform;

    @Inject
    public BankAccountTypeControl(){}

    public void loadData(){
        if(bankAccountTypeViewMap == null){
            synchronized (_mutexLock){
                if(bankAccountTypeViewMap == null)
                    bankAccountTypeViewMap = new HashMap<Integer, BankAccountTypeView>();
            }
        }

        try{
            Map<Integer, BankAccountTypeView> _tmpMap = new HashMap<Integer, BankAccountTypeView>();

            List<BankAccountType> bankAccountTypeList = bankAccountTypeDAO.findAll();
            for(BankAccountType bankAccountType : bankAccountTypeList){
                BankAccountTypeView bankAccountTypeView = bankAccountTypeTransform.getBankAccountTypeView(bankAccountType);
                _tmpMap.put(bankAccountTypeView.getId(), bankAccountTypeView);
            }

            synchronized (_mutexLock){
                bankAccountTypeViewMap.clear();
                bankAccountTypeViewMap.putAll(_tmpMap);
            }

        }catch (Exception ex){
            logger.error("Cannot load BankAccountType into Cache", ex);
        }

    }
}
