package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.BankAccountPurposeDAO;
import com.clevel.selos.model.db.master.BankAccountPurpose;
import com.clevel.selos.model.view.master.BankAccountPurposeView;
import com.clevel.selos.transform.master.BankAccountPurposeTransform;
import com.clevel.selos.util.Util;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankAccountPurposeControl extends BusinessControl{

    @Inject
    private ApplicationCacheLoader cacheLoader;
    @Inject
    private BankAccountPurposeDAO bankAccountPurposeDAO;
    @Inject
    private BankAccountPurposeTransform bankAccountPurposeTransform;

    @Inject
    public BankAccountPurposeControl(){}

    public List<SelectItem> getSelectItemActiveList(){
        Map<Long, BankAccountPurposeView> bankAccountPurposeViewMap = getInternalCacheMap();
        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(BankAccountPurposeView bankAccountPurposeView : bankAccountPurposeViewMap.values()){
            if(Util.isTrue(bankAccountPurposeView.getActive())){
                SelectItem selectItem = new SelectItem();
                selectItem.setValue(bankAccountPurposeView.getId());
                selectItem.setDescription(bankAccountPurposeView.getName());
                selectItem.setLabel(bankAccountPurposeView.getName());
                selectItemList.add(selectItem);
            }
        }
        return selectItemList;
    }

    public List<BankAccountPurposeView> getBankAccountPurposeViewActiveList(){
        Map<Long, BankAccountPurposeView> bankAccountPurposeViewMap = getInternalCacheMap();

        List<BankAccountPurposeView> bankAccountPurposeViewList = new ArrayList<BankAccountPurposeView>();
        for(BankAccountPurposeView bankAccountPurposeView : bankAccountPurposeViewMap.values()){
            if(Util.isTrue(bankAccountPurposeView.getActive())){
                bankAccountPurposeViewList.add(bankAccountPurposeView);
            }
        }
        return bankAccountPurposeViewList;
    }

    private Map<Long, BankAccountPurposeView> loadData(){
        List<BankAccountPurpose> bankAccountPurposeList = bankAccountPurposeDAO.findAll();
        Map<Long, BankAccountPurposeView> _tmpMap = bankAccountPurposeTransform.transformToCache(bankAccountPurposeList);
        if(_tmpMap == null || _tmpMap.size() == 0)
            return new ConcurrentHashMap<Long, BankAccountPurposeView>();
        else {
            cacheLoader.setCacheMap(BankAccountPurpose.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }

    private Map<Long, BankAccountPurposeView> getInternalCacheMap(){
        Map<Long, BankAccountPurposeView> _tmpMap = cacheLoader.getCacheMap(BankAccountPurpose.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        return _tmpMap;
    }
}