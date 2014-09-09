package com.clevel.selos.transform.master;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.KYCLevel;
import com.clevel.selos.model.view.master.KYCLevelView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KYCLevelTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    public KYCLevelTransform(){}

    public KYCLevelView transformToView(KYCLevel kycLevel){
        KYCLevelView kycLevelView = new KYCLevelView();
        if(kycLevel != null){
            kycLevelView.setId(kycLevel.getId());
            kycLevelView.setName(kycLevel.getName());
            kycLevelView.setKycLevel(kycLevel.getKycLevel());
            kycLevelView.setActive(kycLevel.getActive());
        }
        return kycLevelView;
    }

    public Map<Integer, KYCLevelView> transformToCache(List<KYCLevel> kycLevelList){
        if(kycLevelList == null || kycLevelList.size() == 0)
            return null;
        Map<Integer, KYCLevelView> _tmpMap = new ConcurrentHashMap<Integer, KYCLevelView>();
        for(KYCLevel kycLevel : kycLevelList){
            KYCLevelView kycLevelView = transformToView(kycLevel);
            _tmpMap.put(kycLevelView.getId(), kycLevelView);
        }
        return _tmpMap;
    }
}
