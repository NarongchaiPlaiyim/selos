package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.AddressTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.AddressType;
import com.clevel.selos.model.view.master.AddressTypeView;
import com.clevel.selos.transform.master.AddressTypeTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AddressTypeControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private AddressTypeTransform addressTypeTransform;
    @Inject
    private AddressTypeDAO addressTypeDAO;

    public List<SelectItem> getAddressTypeSelectItemByCustEntity(int customerEntityId){
        Map<Integer, AddressTypeView> _tmpMap = getInternalCacheMap();
        List<AddressTypeView> addressTypeViewList = new ArrayList<AddressTypeView>(_tmpMap.values());
        Collections.sort(addressTypeViewList, new AddressTypeComparator());

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(AddressTypeView addressTypeView : addressTypeViewList){
            if(addressTypeView.getCustomerEntityId() == customerEntityId && Util.isTrue(addressTypeView.getActive())){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(addressTypeView.getName());
                selectItem.setValue(addressTypeView.getId());
                selectItemList.add(selectItem);
            }
        }
        return selectItemList;
    }

    private Map<Integer, AddressTypeView> loadData(){
        logger.debug("-- begin loadData --");
        List<AddressType> addressTypeList = addressTypeDAO.findAll();
        Map<Integer, AddressTypeView> _tmpMap = addressTypeTransform.transformToCache(addressTypeList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty Province View");
            return new ConcurrentHashMap<Integer, AddressTypeView>();
        } else {
            cacheLoader.setCacheMap(AddressType.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }

    private Map<Integer, AddressTypeView> getInternalCacheMap(){
        Map<Integer, AddressTypeView> _tmpMap = cacheLoader.getCacheMap(AddressType.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private class AddressTypeComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            AddressTypeView addressTypeView1 = (AddressTypeView)o1;
            AddressTypeView addressTypeView2 = (AddressTypeView)o2;

            int flag = ((Integer)addressTypeView1.getId()).compareTo(addressTypeView2.getId());
            return flag;
        }
    }

}
