package com.clevel.selos.transform;

import com.clevel.selos.dao.working.MandateDocFileNameDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.MandateDoc;
import com.clevel.selos.model.db.working.MandateDocFileName;
import com.clevel.selos.model.view.MandateDocFileNameView;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CheckMandateDocFileNameTransform extends Transform {
    @Inject
    @@SELOS
    private Logger log;
    @Inject
    private MandateDocFileNameDAO mandateDocFileNameDAO;
    private List<MandateDocFileNameView> mandateDocFileNameViewList;
    private List<MandateDocFileName> mandateDocFileNameList;
    @Inject
    public CheckMandateDocFileNameTransform() {

    }

    public List<MandateDocFileNameView> transformToView(final List<MandateDocFileName> mandateDocFileNameList){
        log.debug("--transformToView on MandateDocFileNameView");
        mandateDocFileNameViewList = new ArrayList<MandateDocFileNameView>();
        MandateDocFileNameView view = null;
        log.debug("--mandateDocFileNameList. {}",mandateDocFileNameList);
        for(MandateDocFileName model : mandateDocFileNameList){
            view = new MandateDocFileNameView();
//            view.setId(model.getId());
            view.setUrl(model.getUrl());
            view.setFileName(model.getFileName());
            log.debug("--setFileName. {}",model.getFileName());
            mandateDocFileNameViewList.add(view);
        }
        return mandateDocFileNameViewList;
    }

    public MandateDocFileNameView transformToView(final String name, final String url){
        MandateDocFileNameView view = new MandateDocFileNameView();
        if(!Util.isNull(name) && !Util.isZero(name.length())){
            view.setFileName(name);
        } else {
            view.setFileName("File name");
        }
        view.setUrl(url);
        return view;
    }


    public List<MandateDocFileName> transformToModel(final MandateDoc mandateDoc,final List<MandateDocFileNameView> mandateDocBRMSViewList){
        mandateDocFileNameList = new ArrayList<MandateDocFileName>();
        MandateDocFileName model = null;
        for(MandateDocFileNameView view : mandateDocBRMSViewList){
//            if(!Util.isZero(view.getId())){
//                model = mandateDocFileNameDAO.findById(view.getId());
//                log.debug("-- MandateDocFileName.id[{}]", view.getId());
//            } else {
                model = new MandateDocFileName();
                log.debug("-- [NEW]MandateDocFileName Created");
//            }
            model.setUrl(view.getUrl());
            model.setFileName(view.getFileName());
            model.setMandateDoc(mandateDoc);
            mandateDocFileNameList.add(model);
        }
        return mandateDocFileNameList;
    }
}
