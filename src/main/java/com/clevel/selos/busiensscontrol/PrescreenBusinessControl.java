package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.working.PrescreenFacilityDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.dao.working.PrescreenDAO;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.PrescreenFacility;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.db.working.Prescreen;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.FacilityView;
import com.clevel.selos.model.view.PrescreenView;
import com.clevel.selos.transform.PrescreenFacilityTransform;
import com.clevel.selos.transform.PrescreenTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class PrescreenBusinessControl extends BusinessControl {
    @Inject
    Logger log;

    @Inject
    PrescreenTransform prescreenTransform;
    @Inject
    PrescreenFacilityTransform prescreenFacilityTransform;

    @Inject
    PrescreenDAO prescreenDAO;
    @Inject
    PrescreenFacilityDAO prescreenFacilityDAO;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;

    @Inject
    RMInterface rmInterface;

    @Inject
    public PrescreenBusinessControl(){

    }

    //** function for integration **//
    public CustomerInfoView getCustomerInfoFromRM(CustomerInfoView customerInfoView, User user){
        CustomerInfoView customerInfoSearch = new CustomerInfoView();
        if(customerInfoView.getCustomerEntity().getId() == 1) {
            //getIndividualInfo(String customerId,DocumentType documentType,SearchBy searchBy)
            //customerInfoView.getDocumentType().getId()
            //customerInfoSearch = rmInterface.getIndividualInfo(customerInfoView.getSearchId(), );
        } else if(customerInfoView.getCustomerEntity().getId()==2){
            //customerInfoSearch = rmInterface.getCorporateInfo();
        }
        return customerInfoSearch;
    }

    public void getBankStatementFromDWH(){


    }

    public void savePrescreenInitial(PrescreenView prescreenView, List<FacilityView> facilityViewList, long workCasePrescreenId){
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePrescreenId);
        Prescreen prescreen = prescreenTransform.transformToModel(prescreenView, workCasePrescreen);
        prescreenDAO.persist(prescreen);
        List<PrescreenFacility> prescreenFacilityList = prescreenFacilityTransform.transformModel(facilityViewList, prescreen);
        prescreenFacilityDAO.persist(prescreenFacilityList);
    }

    public void save(WorkCasePrescreen workCasePrescreen){
        workCasePrescreenDAO.persist(workCasePrescreen);
    }

    public void delete(WorkCasePrescreen workCasePrescreen){
        workCasePrescreenDAO.delete(workCasePrescreen);
    }

    public WorkCasePrescreen getWorkCase(long caseId) throws Exception{
        WorkCasePrescreen workCasePrescreen = new WorkCasePrescreen();
        try{
            workCasePrescreen = workCasePrescreenDAO.findById(caseId);
            log.info("getWorkCasePrescreen : {}", workCasePrescreen);
            if(workCasePrescreen == null){
                throw new Exception("no data found.");
            }
        } catch (Exception ex){
            log.info("getWorkCasePrescreen ::: error : {}", ex.toString());
            throw new Exception(ex.getMessage());
        }

        return workCasePrescreen;
    }

}
