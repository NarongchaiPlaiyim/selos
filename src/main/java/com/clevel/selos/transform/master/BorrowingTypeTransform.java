package com.clevel.selos.transform.master;

import com.clevel.selos.dao.master.BorrowingTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BorrowingType;
import com.clevel.selos.model.view.master.BorrowingTypeView;
import com.clevel.selos.transform.Transform;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BorrowingTypeTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private BorrowingTypeDAO borrowingTypeDAO;

    @Inject
    public BorrowingTypeTransform(){}

    public SelectItem transformToSelectItem(BorrowingTypeView borrowingTypeView){
        if(borrowingTypeView == null)
            return null;

        SelectItem selectItem = new SelectItem();
        selectItem.setLabel(borrowingTypeView.getName());
        selectItem.setValue(borrowingTypeView.getId());
        return selectItem;
    }

    public BorrowingTypeView transformToView(BorrowingType borrowingType){
        if(borrowingType == null)
            return new BorrowingTypeView();
        BorrowingTypeView borrowingTypeView = new BorrowingTypeView();
        borrowingTypeView.setId(borrowingType.getId());
        borrowingTypeView.setName(borrowingType.getName());
        borrowingTypeView.setActive(borrowingType.getActive());
        borrowingTypeView.setCode(borrowingType.getCode());
        borrowingTypeView.setCustomerEntityID(borrowingType.getCustomerEntity() == null?0:borrowingType.getCustomerEntity().getId());
        return borrowingTypeView;
    }

    public Map<Integer, BorrowingTypeView> transformToCache(List<BorrowingType> borrowingTypeList){
        if(borrowingTypeList == null && borrowingTypeList.size() == 0)
            return null;

        Map<Integer, BorrowingTypeView> _tmpMap = new ConcurrentHashMap<Integer, BorrowingTypeView>();
        for(BorrowingType borrowingType : borrowingTypeList){
            BorrowingTypeView borrowingTypeView = transformToView(borrowingType);
            _tmpMap.put(borrowingTypeView.getId(), borrowingTypeView);
        }
        return _tmpMap;
    }

}
