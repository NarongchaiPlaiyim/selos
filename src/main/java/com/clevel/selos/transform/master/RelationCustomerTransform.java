package com.clevel.selos.transform.master;

import com.clevel.selos.dao.relation.RelationCustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.relation.RelationCustomer;
import com.clevel.selos.model.view.master.RelationCustomerView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RelationCustomerTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private RelationCustomerDAO relationCustomerDO;

    public RelationCustomerView transformToView(RelationCustomer relationCustomer){
        RelationCustomerView relationCustomerView = new RelationCustomerView();
        if(relationCustomer != null){
            relationCustomerView.setId(relationCustomer.getId());
            relationCustomerView.setBorrowerTypeCusEntityId(relationCustomer.getBorrowerType() == null? 0:relationCustomer.getBorrowerType().getId());
            relationCustomerView.setCustomerEntityId(relationCustomer.getCustomerEntity() == null? 0: relationCustomer.getCustomerEntity().getId());
            relationCustomerView.setRelationId(relationCustomer.getRelation() == null? 0: relationCustomer.getRelation().getId());
            relationCustomerView.setSpouse(relationCustomer.getSpouse());
        }
        return relationCustomerView;
    }

    public Map<Integer, RelationCustomerView> transformToCache(List<RelationCustomer> relationCustomerList){
        if(relationCustomerList == null || relationCustomerList.size() == 0)
            return null;
        Map<Integer, RelationCustomerView> _tmpMap = new ConcurrentHashMap<Integer, RelationCustomerView>();
        for(RelationCustomer relationCustomer : relationCustomerList){
            RelationCustomerView relationCustomerView = transformToView(relationCustomer);
            _tmpMap.put(relationCustomerView.getId(), relationCustomerView);
        }
        return _tmpMap;
    }

}
