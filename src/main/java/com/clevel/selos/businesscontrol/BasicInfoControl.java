package com.clevel.selos.businesscontrol;

import com.clevel.selos.model.db.working.BasicInfo;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.transform.BasicInfoTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class BasicInfoControl extends BusinessControl {
    @Inject
    Logger log;

    @Inject
    BasicInfoTransform basicInfoTransform;

//    public BasicInfoView getBasicInfo(long workCaseId){
//        log.info("getBasicInfo ::: workCaseId : {}", workCaseId);
//        BasicInfoView basicInfoView = null;
//        BasicInfo basicInfo = prescreenDAO.findByWorkCasePrescreenId(workCaseId);
//
//        if(basicInfo != null){
//            log.info("getBasicInfo ::: basicInfo : {}", basicInfo);
//            basicInfoView = basicInfoTransform.transformToView(prescreen);
//        }
//        log.info("getPreScreen ::: preScreenView : {}", prescreenView);
//        return prescreenView;
//    }
}
