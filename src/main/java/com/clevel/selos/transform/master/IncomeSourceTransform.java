package com.clevel.selos.transform.master;

import com.clevel.selos.dao.master.IncomeSourceDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.IncomeSource;
import com.clevel.selos.model.view.master.IncomeSourceView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IncomeSourceTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private IncomeSourceDAO incomeSourceDAO;

    public IncomeSourceView transformToView(IncomeSource incomeSource){
        IncomeSourceView incomeSourceView = new IncomeSourceView();
        if(incomeSource != null){
            incomeSourceView.setId(incomeSource.getId());
            incomeSourceView.setName(incomeSource.getName());
            incomeSourceView.setActive(incomeSource.getActive());
        }
        return incomeSourceView;
    }

    public Map<Integer, IncomeSourceView> transformToCache(List<IncomeSource> incomeSourceList){
        if(incomeSourceList == null || incomeSourceList.size() == 0)
            return null;

        Map<Integer, IncomeSourceView> _tmpMap = new ConcurrentHashMap<Integer, IncomeSourceView>();
        for(IncomeSource incomeSource : incomeSourceList){
            IncomeSourceView incomeSourceView = transformToView(incomeSource);
            _tmpMap.put(incomeSourceView.getId(), incomeSourceView);
        }
        return _tmpMap;
    }
}
