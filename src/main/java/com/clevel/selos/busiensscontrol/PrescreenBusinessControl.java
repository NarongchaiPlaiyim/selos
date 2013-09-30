package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.master.DocumentTypeDAO;
import com.clevel.selos.dao.working.PrescreenFacilityDAO;
import com.clevel.selos.dao.working.WorkCasePrescreenDAO;
import com.clevel.selos.dao.working.PrescreenDAO;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.master.DocumentType;
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
    DocumentTypeDAO documentTypeDAO;

    @Inject
    RMInterface rmInterface;

    @Inject
    public PrescreenBusinessControl(){

    }

    //** function for integration **//
    public CustomerInfoView getCustomerInfoFromRM(CustomerInfoView customerInfoView, User user) throws Exception{
        CustomerInfoView customerInfoSearch = new CustomerInfoView();
        log.info("getCustomerInfoFromRM ::: customerInfoView : {}", customerInfoView);
        try{
            String userId = Long.toString(user.getId());
            DocumentType masterDocumentType = documentTypeDAO.findById(customerInfoView.getDocumentType().getId());
            String documentTypeCode = masterDocumentType.getDocumentTypeCode();
            log.info("getCustomerInfoFromRM ::: userId : {}", userId);
            log.info("getCustomerInfoFromRM ::: documentType : {}", masterDocumentType);
            log.info("getCustomerInfoFromRM ::: documentTypeCode : {}", documentTypeCode);

            RMInterface.SearchBy searcyBy = RMInterface.SearchBy.CUSTOMER_ID;
            if(customerInfoView.getSearchBy() == 1){
                searcyBy = RMInterface.SearchBy.CUSTOMER_ID;
            }else if(customerInfoView.getSearchBy() == 2){
                searcyBy = RMInterface.SearchBy.TMBCUS_ID;
            }

            RMInterface.DocumentType documentType = RMInterface.DocumentType.CITIZEN_ID;
            if(documentTypeCode.equalsIgnoreCase("CI")){
                documentType = RMInterface.DocumentType.CITIZEN_ID;
            }else if(documentTypeCode.equalsIgnoreCase("PP")){
                documentType = RMInterface.DocumentType.PASSPORT;
            }else if(documentTypeCode.equalsIgnoreCase("SC")){
                documentType = RMInterface.DocumentType.CORPORATE_ID;
            }

            log.info("getCustomerInfoFromRM ::: searchBy : {}", searcyBy);
            log.info("getCustomerInfoFromRM ::: documentType : {}", documentType);

            if(customerInfoView.getCustomerEntity().getId() == 1) {
                customerInfoSearch = rmInterface.getIndividualInfo(userId, customerInfoView.getSearchId(), documentType, searcyBy);
            } else if(customerInfoView.getCustomerEntity().getId() == 2){
                customerInfoSearch = rmInterface.getCorporateInfo(userId, customerInfoView.getSearchId(), documentType, searcyBy);
            }
        }catch (Exception ex){
            log.info("error : {}", ex);
            throw new Exception(ex.getMessage());
        }
        log.info("getCustomerInfoFromRM ::: success!!");
        log.info("getCustomerInfoFromRM ::: customerInfoSearch : {}", customerInfoSearch);
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
