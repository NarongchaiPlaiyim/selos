package com.clevel.selos.transform;

import com.clevel.selos.dao.working.MandateDocCustDAO;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.model.db.working.MandateDocCust;
import com.clevel.selos.model.view.CustomerInfoSimpleView;
import com.clevel.selos.model.view.MandateDocCustView;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CheckMandateDocCustTransform extends Transform {
    @Inject
    @NCB
    private Logger log;
    private MandateDocCustDAO mandateDocCustDAO;
    private List<MandateDocCustView> mandateDocCustViewList;
    private List<MandateDocCust> mandateDocCustList;
    @Inject
    public CheckMandateDocCustTransform() {

    }

    public List<MandateDocCustView> transformToView(final List<MandateDocCust> mandateDocCustList){
        mandateDocCustViewList = new ArrayList<MandateDocCustView>();
        MandateDocCustView view = null;
        for(MandateDocCust model : mandateDocCustList){
            view = new MandateDocCustView();
            view.setId(model.getId());
            view.setCustName(model.getCustName());
            mandateDocCustViewList.add(view);
        }
        return mandateDocCustViewList;
    }

    public List<MandateDocCustView> transformCustomerListToView(List<CustomerInfoSimpleView> customerInfoSimpleViewList){
        mandateDocCustViewList = new ArrayList<MandateDocCustView>();
        MandateDocCustView view = null;
        for(CustomerInfoSimpleView customerInfoSimpleView : customerInfoSimpleViewList){
            view = new MandateDocCustView();
            view.setCustName(customerInfoSimpleView.getCustomerName());
            mandateDocCustViewList.add(view);
        }
        return mandateDocCustViewList;
    }

    public List<MandateDocCust> transformToModel(final List<MandateDocCustView> mandateDocCustViewList){
        mandateDocCustList = new ArrayList<MandateDocCust>();
        MandateDocCust model = null;
        for(MandateDocCustView view : mandateDocCustViewList){
            if(!Util.isNull(view.getId())){
                model = mandateDocCustDAO.findById(view.getId());
            } else {
                model = new MandateDocCust();
            }
            model.setCustName(view.getCustName());
            mandateDocCustList.add(model);
        }
        return mandateDocCustList;
    }

}
