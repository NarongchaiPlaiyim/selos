package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.OccupationDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Occupation;
import com.clevel.selos.model.view.master.OccupationView;
import com.clevel.selos.transform.master.OccupationTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OccupationControl extends BusinessControl{

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;
    @Inject
    private OccupationDAO occupationDAO;
    @Inject
    private OccupationTransform occupationTransform;

    public List<SelectItem> getOccupationSelectItemActiveList(){
        Map<Integer, OccupationView> _tmpMap = getInternalCacheMap();
        List<OccupationView> occupationViewList = new ArrayList<OccupationView>();
        Collections.sort(occupationViewList, new OccupationComparator());

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(OccupationView occupationView : _tmpMap.values()){
            if(Util.isTrue(occupationView.getActive())){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(occupationView.getCode() + " - " + occupationView.getDescription());
                selectItem.setValue(occupationView.getId());
                selectItemList.add(selectItem);
            }
        }
        return selectItemList;
    }

    private Map<Integer, OccupationView> loadData(){
        logger.debug("-- begin loadData --");
        List<Occupation> occupationList = occupationDAO.findAll();
        Map<Integer, OccupationView> _tmpMap = occupationTransform.transformToCache(occupationList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty Title View");
            return new ConcurrentHashMap<Integer, OccupationView>();
        } else {
            cacheLoader.setCacheMap(Occupation.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }

    private Map<Integer, OccupationView> getInternalCacheMap(){
        Map<Integer, OccupationView> _tmpMap = cacheLoader.getCacheMap(Occupation.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    public class OccupationComparator implements Comparator{
        @Override
        public int compare(Object o1, Object o2) {
            OccupationView occupationView1 = (OccupationView) o1;
            OccupationView occupationView2 = (OccupationView) o2;

            int flag = ((Integer)occupationView1.getCode()).compareTo(occupationView2.getCode());
            return flag;
        }
    }
}
