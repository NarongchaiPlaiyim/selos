package com.clevel.selos.transform;

import com.clevel.selos.dao.working.MandateDocBRMSDAO;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.db.working.MandateDocBRMS;
import com.clevel.selos.model.view.MandateDocBRMSView;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CheckMandateDocBRMSTransform extends Transform {
    @Inject
    @NCB
    private Logger log;
    @Inject
    private MandateDocBRMSDAO mandateDocBRMSDAO;
    private List<MandateDocBRMSView> mandateDocBRMSViewList;
    private List<MandateDocBRMS> mandateDocBRMSList;
    @Inject
    public CheckMandateDocBRMSTransform() {

    }

    public List<MandateDocBRMSView> transformToView(final List<MandateDocBRMS> mandateDocBRMSList){
        log.debug("--transformToView on MandateDocBRMSView");
        mandateDocBRMSViewList = new ArrayList<MandateDocBRMSView>();
        MandateDocBRMSView view = null;
        log.debug("--mandateDocBRMSList. {}",mandateDocBRMSList);
        for(MandateDocBRMS model : mandateDocBRMSList){
            view = new MandateDocBRMSView();
//            view.setId(model.getId());
            view.setBRMSDocType(model.getBRMSDocType());
            log.debug("--getBRMSDocType. {}",model.getBRMSDocType());
            mandateDocBRMSViewList.add(view);
        }
        return mandateDocBRMSViewList;
    }

    public List<MandateDocBRMSView> transformStringListToView(final List<String> stringList){
        mandateDocBRMSViewList = new ArrayList<MandateDocBRMSView>();
        MandateDocBRMSView view = null;
        for(String string : stringList){
            view = new MandateDocBRMSView();
            view.setBRMSDocType(string);
            mandateDocBRMSViewList.add(view);
        }
        return mandateDocBRMSViewList;
    }

    public List<MandateDocBRMS> transformToModel(final MandateDoc mandateDoc, final List<MandateDocBRMSView> mandateDocBRMSViewList){
        mandateDocBRMSList = new ArrayList<MandateDocBRMS>();
        MandateDocBRMS model = null;
        for(MandateDocBRMSView view : mandateDocBRMSViewList){
//            if(!Util.isZero(view.getId())){
//                model = mandateDocBRMSDAO.findById(view.getId());
//                log.debug("-- MandateDocBRMS.id[{}]", view.getId());
//            } else {
                model = new MandateDocBRMS();
                log.debug("-- [NEW]MandateDocBRMS Created");
//            }
            model.setBRMSDocType(view.getBRMSDocType());
            model.setMandateDoc(mandateDoc);
            mandateDocBRMSList.add(model);
        }
        return mandateDocBRMSList;
    }
}
