package com.clevel.selos.transform.master;

import com.clevel.selos.dao.master.ReferenceDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Reference;
import com.clevel.selos.model.view.master.ReferenceView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReferenceTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ReferenceDAO referenceDAO;

    public ReferenceView transferToView(Reference reference){
        ReferenceView referenceView = new ReferenceView();
        if(reference != null){
            referenceView.setId(reference.getId());
            referenceView.setDescription(reference.getDescription());
            referenceView.setSpouse(reference.getSpouse());
            referenceView.setRelationId(reference.getRelation() == null? 0: reference.getRelation().getId());
            referenceView.setCustomerEntityId(reference.getCustomerEntity() == null? 0: reference.getCustomerEntity().getId());
            referenceView.setActive(reference.getActive());
            referenceView.setBorrowerTypeCusEntityId(reference.getBorrowerType() == null? 0: reference.getBorrowerType().getId());
            referenceView.setBrmsCode(reference.getBrmsCode());
            referenceView.setCollateralOwner(reference.getCollateralOwner());
            referenceView.setCsi(reference.getCsi());
            referenceView.setGroupIncome(reference.getGroupIncome());
            referenceView.setKycFullScreening(reference.getKycFullScreening());
            referenceView.setKycNameScreening(reference.getKycNameScreening());
            referenceView.setLinkRm(reference.getLinkRm());
            referenceView.setMainCustomer(reference.getMainCustomer());
            referenceView.setPendingClaimLG(reference.getPendingClaimLG());
            referenceView.setPercentShare(reference.getPercentShare());
            referenceView.setRelationType(reference.getRelationType());
            referenceView.setSll(reference.getSll());
            referenceView.setUnpaidInsurance(reference.getUnpaidInsurance());
        }
        return referenceView;
    }

    public Map<Integer, ReferenceView> transformToCache(List<Reference> referenceList){
        if(referenceList == null | referenceList.size() == 0)
            return null;
        Map<Integer, ReferenceView> _tmpMap = new ConcurrentHashMap<Integer, ReferenceView>();
        for(Reference reference : referenceList){
            ReferenceView referenceView = transferToView(reference);
            _tmpMap.put(referenceView.getId(), referenceView);
        }
        return _tmpMap;
    }
}
