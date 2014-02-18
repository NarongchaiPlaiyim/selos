package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.DisbursementTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.DisbursementType;
import com.clevel.selos.model.view.DisbursementTypeView;
import com.clevel.selos.transform.DisbursementTypeTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DisbursementTypeControl extends BusinessControl{

    @Inject
    @SELOS
    Logger logger;

    @Inject
    DisbursementTypeDAO disbursementTypeDAO;

    @Inject
    DisbursementTypeTransform disbursementTypeTransform;

    public List<DisbursementTypeView> getDisbursementTypeViewList(){
        List<DisbursementTypeView> disbursementTypeViewList = new ArrayList<DisbursementTypeView>();
        List<DisbursementType> disbursementTypeList = disbursementTypeDAO.findActiveAll();

        for(DisbursementType disbursementType : disbursementTypeList){
            disbursementTypeViewList.add(disbursementTypeTransform.transformToView(disbursementType));
        }
        return disbursementTypeViewList;

    }
}
