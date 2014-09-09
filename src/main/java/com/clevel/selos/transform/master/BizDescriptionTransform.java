package com.clevel.selos.transform.master;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.view.master.BizDescriptionView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BizDescriptionTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;

    public BizDescriptionView transformToView(BusinessDescription businessDescription){
        BizDescriptionView bizDescriptionView = new BizDescriptionView();
        if(businessDescription != null){
            bizDescriptionView.setId(businessDescription.getId());
            bizDescriptionView.setDescription(businessDescription.getDescription());
            bizDescriptionView.setActive(businessDescription.getActive());
            bizDescriptionView.setAllowDeviate(businessDescription.getAllowDeviate());
            bizDescriptionView.setAp(businessDescription.getAp());
            bizDescriptionView.setAr(businessDescription.getAr());
            bizDescriptionView.setBusinessGroupId(businessDescription.getBusinessGroup() == null?0:businessDescription.getBusinessGroup().getId());
            bizDescriptionView.setBusinessPermission(businessDescription.getBusinessPermission());
            bizDescriptionView.setBusinessPermissionDesc(businessDescription.getBusinessPermissionDesc());
            bizDescriptionView.setCog(businessDescription.getCog());
            bizDescriptionView.setComment(businessDescription.getComment());
            bizDescriptionView.setDeviate(businessDescription.getDeviate());
            bizDescriptionView.setDeviatedBy(businessDescription.getDeviatedBy());
            bizDescriptionView.setEsr(businessDescription.getEsr());
            bizDescriptionView.setFactory4(businessDescription.getFactory4());
            bizDescriptionView.setFoodAndDrug(businessDescription.getFoodAndDrug());
            bizDescriptionView.setHighRisk(businessDescription.getHighRisk());
            bizDescriptionView.setIncomeFactor(businessDescription.getIncomeFactor());
            bizDescriptionView.setInv(businessDescription.getInv());
            bizDescriptionView.setIsicCode(businessDescription.getIsicCode());
            bizDescriptionView.setName(businessDescription.getName());
            bizDescriptionView.setNegative(businessDescription.getNegative());
            bizDescriptionView.setSuspend(businessDescription.getSuspend());
            bizDescriptionView.setTmbCode(businessDescription.getTmbCode());
        }
        return bizDescriptionView;
    }

    public Map<Integer, BizDescriptionView> transformToCache(List<BusinessDescription> businessDescriptionList){
        if(businessDescriptionList == null || businessDescriptionList.size() == 0)
            return null;

        Map<Integer, BizDescriptionView> _tmpMap = new ConcurrentHashMap<Integer, BizDescriptionView>();
        for(BusinessDescription businessDescription : businessDescriptionList){
            BizDescriptionView bizDescriptionView = transformToView(businessDescription);
            _tmpMap.put(bizDescriptionView.getId(), bizDescriptionView);
        }
        return _tmpMap;
    }

}
