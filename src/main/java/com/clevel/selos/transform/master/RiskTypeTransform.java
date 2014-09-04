package com.clevel.selos.transform.master;

import com.clevel.selos.dao.master.RiskTypeDAO;
import com.clevel.selos.model.db.master.RiskType;
import com.clevel.selos.model.view.master.RiskTypeView;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RiskTypeTransform {

    @Inject
    private RiskTypeDAO riskTypeDAO;

    public RiskTypeView transformToView(RiskType riskType){
        if(riskType == null)
            return null;
        RiskTypeView riskTypeView = new RiskTypeView();
        riskTypeView.setId(riskType.getId());
        riskTypeView.setCode(riskType.getCode());
        riskTypeView.setName(riskType.getName());
        riskTypeView.setActive(riskType.getActive());
        riskTypeView.setDescription(riskType.getDescription());
        return riskTypeView;
    }

    public RiskType transformToModel(RiskTypeView riskTypeView){
        if(riskTypeView == null)
            return null;
        RiskType riskType = new RiskType();
        if(riskTypeView.getId() != 0)
            riskType = riskTypeDAO.findById(riskTypeView.getId());
        riskType.setDescription(riskTypeView.getDescription());
        riskType.setName(riskTypeView.getName());
        riskType.setCode(riskTypeView.getCode());
        riskType.setActive(riskTypeView.getActive());
        return riskType;
    }

    public RiskType transformToModel(SelectItem selectRiskType){
        if(selectRiskType == null || ((Integer)selectRiskType.getValue() == 0))
            return null;
        RiskType riskType = riskTypeDAO.findById((Integer)selectRiskType.getValue());
        return riskType;
    }

    public Map<Integer, RiskTypeView> transformToCache(List<RiskType> riskTypeList){
        if(riskTypeList == null || riskTypeList.size() == 0)
            return null;
        Map<Integer, RiskTypeView> _tmpMap = new ConcurrentHashMap<Integer, RiskTypeView>();
        for(RiskType riskType : riskTypeList){
            RiskTypeView riskTypeView = transformToView(riskType);
            _tmpMap.put(riskTypeView.getId(), riskTypeView);
        }
        return _tmpMap;
    }
}
