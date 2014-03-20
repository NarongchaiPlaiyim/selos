package com.clevel.selos.transform;

import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.response.DocumentDetail;
import com.clevel.selos.model.view.MandateDocView;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MandateDocTransform extends Transform {

    @Inject
    @SELOS
    Logger logger;

    @Inject
    private CustomerDAO customerDAO;

    public MandateDocView transformToView(DocumentDetail documentDetail){
        logger.debug("transformToView documentDetail {}", documentDetail);
        if(documentDetail == null)
            return null;

        MandateDocView mandateDocView = new MandateDocView();
        mandateDocView.setEcmDocTypeId(documentDetail.getId());
        mandateDocView.setDocLevel(documentDetail.getDocLevel());
        if(documentDetail.getDocOwner() != null){

        }
        return mandateDocView;
    }

}
