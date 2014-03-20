package com.clevel.selos.transform;

import com.clevel.selos.dao.master.ServiceSegmentDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.ServiceSegment;
import com.clevel.selos.model.view.ServiceSegmentView;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ServiceSegmentTransform  extends Transform{
    @SELOS
    @Inject
    private Logger log;

    @Inject
    ServiceSegmentDAO serviceSegmentDAO;

    @Inject
    public ServiceSegmentTransform(){

    }

    public ServiceSegmentView transformToView(ServiceSegment serviceSegment){
        log.debug("transformToView({})", serviceSegment);
        ServiceSegmentView serviceSegmentView = new ServiceSegmentView();
        if(serviceSegment != null){
            serviceSegmentView.setId(serviceSegment.getId());
            serviceSegmentView.setActive(serviceSegment.getActive());
            serviceSegmentView.setCode(serviceSegment.getCode());
            serviceSegmentView.setExistingSME(Util.isTrue(serviceSegment.getExistingSME()));
            serviceSegmentView.setNonExistingSME(Util.isTrue(serviceSegment.getNonExistingSME()));
        }
        log.debug("return transformToView {}", serviceSegmentView);
        return serviceSegmentView;
    }

    public ServiceSegment transformToModel(ServiceSegmentView serviceSegmentView){
        log.debug("transformToModel({})", serviceSegmentView);
        ServiceSegment serviceSegment = null;
        try{
            if(serviceSegmentView != null && serviceSegmentView.getId() !=0){
                serviceSegment = serviceSegmentDAO.findById(serviceSegmentView.getId());
            }
        }catch (Exception ex){
            log.debug("Cannot Find Service Segment ID {}", serviceSegmentView.getId());
        }
        log.debug("return transformToModel {}", serviceSegment);
        return serviceSegment;
    }

    public ServiceSegmentView transformToView(int code){
        log.info("transformToView({})", code);
        ServiceSegmentView serviceSegmentView = new ServiceSegmentView();
        try{
            ServiceSegment serviceSegment = serviceSegmentDAO.findByCode(code);
            serviceSegmentView = transformToView(serviceSegment);
        }catch (Exception ex){
            log.info("Cannot Find Service Segment Code {}", code);
        }
        log.info("return transformToView {}", serviceSegmentView);
        return serviceSegmentView;
    }
}
