package com.clevel.selos.transform;

import com.clevel.selos.dao.working.MandateDocFileNameDAO;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.model.db.working.MandateDocFileName;
import com.clevel.selos.model.view.MandateDocFileNameView;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CheckMandateDocFileNameTransform extends Transform {
    @Inject
    @NCB
    private Logger log;
    @Inject
    private MandateDocFileNameDAO mandateDocFileNameDAO;
    private List<MandateDocFileNameView> mandateDocFileNameViewList;
    private List<MandateDocFileName> mandateDocFileNameList;
    @Inject
    public CheckMandateDocFileNameTransform() {

    }

    public List<MandateDocFileNameView> transformToView(final List<MandateDocFileName> mandateDocFileNameList){
        mandateDocFileNameViewList = new ArrayList<MandateDocFileNameView>();
        MandateDocFileNameView view = null;
        for(MandateDocFileName model : mandateDocFileNameList){
            view = new MandateDocFileNameView();
            view.setId(model.getId());
            view.setUrl(model.getUrl());
            view.setFileName(model.getFileName());
            mandateDocFileNameViewList.add(view);
        }
        return mandateDocFileNameViewList;
    }

    public MandateDocFileNameView transformToView(final String name, final String url){
        MandateDocFileNameView view = new MandateDocFileNameView();
        view.setFileName(name);
        view.setUrl(url);
        return view;
    }


    public List<MandateDocFileName> transformToModel(final List<MandateDocFileNameView> mandateDocBRMSViewList){
        mandateDocFileNameList = new ArrayList<MandateDocFileName>();
        MandateDocFileName model = null;
        for(MandateDocFileNameView view : mandateDocBRMSViewList){
            if(!Util.isNull(view.getId())){
                model = mandateDocFileNameDAO.findById(view.getId());
            } else {
                model = new MandateDocFileName();
            }
            model.setUrl(view.getUrl());
            model.setFileName(view.getFileName());
            mandateDocFileNameList.add(model);
        }
        return mandateDocFileNameList;
    }
}
