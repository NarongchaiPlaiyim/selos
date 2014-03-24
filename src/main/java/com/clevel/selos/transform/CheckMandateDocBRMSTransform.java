package com.clevel.selos.transform;

import com.clevel.selos.dao.working.MandateDocBRMSDAO;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.model.db.working.MandateDocBRMS;
import com.clevel.selos.model.view.MandateDocBRMSView;
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
    private MandateDocBRMS mandateDocBRMS;
    private MandateDocBRMSView mandateDocBRMSView;
    private List<MandateDocBRMSView> mandateDocBRMSViewList;
    private List<MandateDocBRMS> mandateDocBRMSList;
    @Inject
    public CheckMandateDocBRMSTransform() {

    }

    public List<MandateDocBRMSView> transformToView(final List<MandateDocBRMS> mandateDocBRMSList){
        mandateDocBRMSViewList = new ArrayList<MandateDocBRMSView>();

        mandateDocBRMSView = new MandateDocBRMSView();

        return mandateDocBRMSViewList;
    }

    public List<MandateDocBRMS> transformToModel(final List<MandateDocBRMSView> mandateDocBRMSViewList){
        mandateDocBRMSList = new ArrayList<MandateDocBRMS>();
        MandateDocBRMS model = null;
        for(MandateDocBRMSView view : mandateDocBRMSViewList){
            model = new MandateDocBRMS();

            mandateDocBRMSList.add(model);
        }
        return mandateDocBRMSList;
    }
}
