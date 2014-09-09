package com.clevel.selos.transform.master;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Education;
import com.clevel.selos.model.view.master.EducationView;
import com.clevel.selos.transform.Transform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EducationTransform extends Transform {

    @Inject
    @SELOS
    private Logger logger;

    @Inject
    public EducationTransform(){}

    public EducationView transformToView(Education education){
        EducationView educationView = new EducationView();
        if(education != null){
            educationView.setId(education.getId());
            educationView.setCode(education.getCode());
            educationView.setNameEn(education.getNameEn());
            educationView.setNameTh(education.getNameTh());
            educationView.setActive(education.getActive());
        }
        return educationView;
    }

    public Map<Integer, EducationView> transformToCache(List<Education> educationList){
        if(educationList == null || educationList.size() == 0)
            return null;

        Map<Integer, EducationView> _tmpMap = new ConcurrentHashMap<Integer, EducationView>();
        for(Education education : educationList){
            EducationView educationView = transformToView(education);
            _tmpMap.put(educationView.getId(), educationView);

        }
        return _tmpMap;
    }

}
