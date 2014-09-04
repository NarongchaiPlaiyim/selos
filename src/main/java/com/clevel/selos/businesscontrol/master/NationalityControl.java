package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.NationalityDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Nationality;
import com.clevel.selos.model.view.master.NationalityView;
import com.clevel.selos.transform.master.NationalityTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NationalityControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private NationalityTransform nationalityTransform;

    @Inject
    private NationalityDAO nationalityDAO;

    @Inject
    public NationalityControl(){}

    public List<SelectItem> getNationalitySelectItemActiveList(){
        Map<Integer, NationalityView> _tmpMap = getInternalCacheMap();
        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(NationalityView nationalityView : _tmpMap.values()){
            if(Util.isTrue(nationalityView.getActive())){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(nationalityView.getName());
                selectItem.setValue(nationalityView.getId());
                selectItemList.add(selectItem);
            }

        }
        return selectItemList;
    }


    private Map<Integer, NationalityView> loadData(){
        logger.debug("-- begin loadData --");
        List<Nationality> nationalityList = nationalityDAO.findAll();
        Map<Integer, NationalityView> _tmpMap = nationalityTransform.transformToCache(nationalityList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty Nationality View");
            return new ConcurrentHashMap<Integer, NationalityView>();
        } else {
            cacheLoader.setCacheMap(Nationality.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }

    private Map<Integer, NationalityView> getInternalCacheMap(){
        Map<Integer, NationalityView> _tmpMap = cacheLoader.getCacheMap(Nationality.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        return _tmpMap;
    }
}
