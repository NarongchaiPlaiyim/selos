package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.dao.master.EducationDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Education;
import com.clevel.selos.model.view.master.EducationView;
import com.clevel.selos.transform.master.EducationTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EducationControl extends BusinessControl {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    private ApplicationCacheLoader cacheLoader;

    @Inject
    private EducationDAO educationDAO;

    @Inject
    private EducationTransform educationTransform;

    public List<SelectItem> getEducationSelectItemActiveList(){
        Map<Integer, EducationView> _tmpMap = getInternalCacheMap();
        List<EducationView> educationViewList = new ArrayList<EducationView>(_tmpMap.values());
        Collections.sort(educationViewList, new EducationComparator());

        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        for(EducationView educationView : educationViewList){
            if(Util.isTrue(educationView.getActive())){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(educationView.getNameTh());
                selectItem.setValue(educationView.getId());
                selectItemList.add(selectItem);
            }
        }
        return selectItemList;
    }


    private Map<Integer, EducationView> loadData(){
        logger.debug("-- begin loadData --");
        List<Education> educationList = educationDAO.findAll();
        Map<Integer, EducationView> _tmpMap = educationTransform.transformToCache(educationList);
        if(_tmpMap == null || _tmpMap.size() == 0) {
            logger.debug("return empty Title View");
            return new ConcurrentHashMap<Integer, EducationView>();
        } else {
            cacheLoader.setCacheMap(Education.class.getName(), _tmpMap);
            return _tmpMap;
        }
    }

    private Map<Integer, EducationView> getInternalCacheMap(){
        Map<Integer, EducationView> _tmpMap = cacheLoader.getCacheMap(Education.class.getName());
        if(_tmpMap == null || _tmpMap.size() == 0){
            _tmpMap = loadData();
        }
        return _tmpMap;
    }

    private class EducationComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            EducationView educationView1 = (EducationView) o1;
            EducationView educationView2 = (EducationView) o2;

            int flag = ((Integer)educationView1.getId()).compareTo(educationView2.getId());
            return flag;
        }
    }

}
