package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.master.DocumentTypeDAO;
import com.clevel.selos.dao.master.StepDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.brms.model.request.PreScreenRequest;
import com.clevel.selos.integration.brms.model.response.PreScreenResponse;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.master.DocumentType;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.FacilityView;
import com.clevel.selos.model.view.PreScreenResponseView;
import com.clevel.selos.model.view.PrescreenView;
import com.clevel.selos.transform.PreScreenResultTransform;
import com.clevel.selos.transform.PrescreenFacilityTransform;
import com.clevel.selos.transform.PrescreenTransform;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
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
    PreScreenResultTransform preScreenResultTransform;

    @Inject
    PrescreenDAO prescreenDAO;
    @Inject
    PrescreenFacilityDAO prescreenFacilityDAO;
    @Inject
    WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    StepDAO stepDAO;
    @Inject
    DocumentTypeDAO documentTypeDAO;
    @Inject
    BRMSResultDAO brmsResultDAO;

    @Inject
    RMInterface rmInterface;
    @Inject
    BRMSInterface brmsInterface;

    @Inject
    public PrescreenBusinessControl(){

    }

    //** function for integration **//

    // *** Function for RM *** //
    public CustomerInfoView getCustomerInfoFromRM(CustomerInfoView customerInfoView, User user) throws Exception{
        CustomerInfoView customerInfoSearch = new CustomerInfoView();
        log.info("getCustomerInfoFromRM ::: customerInfoView : {}", customerInfoView);
        try {
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

            /*if(customerInfoView.getCustomerEntity().getId() == 1) {
                customerInfoSearch = rmInterface.getIndividualInfo(userId, customerInfoView.getSearchId(), documentType, searcyBy);
            } else if(customerInfoView.getCustomerEntity().getId() == 2){
                customerInfoSearch = rmInterface.getCorporateInfo(userId, customerInfoView.getSearchId(), documentType, searcyBy);
            }*/
        } catch(Exception ex) {
            log.info("error : {}", ex);
            throw new Exception(ex.getMessage());
        }
        log.info("getCustomerInfoFromRM ::: success!!");
        log.info("getCustomerInfoFromRM ::: customerInfoSearch : {}", customerInfoSearch);
        return customerInfoSearch;
    }

    // *** Function for DWH (BankStatement) *** //
    public void getBankStatementFromDWH(PrescreenView prescreenView, User user) throws Exception{
        Date expectedSubmitDate = prescreenView.getExpectedSubmitDate();
        //TODO if expected submit date less than 15 get current month -2 if more than 15 get current month -1
        expectedSubmitDate = DateTime.now().toDate();
        int months = Util.getMonthOfDate(expectedSubmitDate);
        int days = Util.getDayOfDate(expectedSubmitDate);
        log.info("getBankStatementFromDWH ::: months : {}", months);
        log.info("getBankStatementFromDWH ::: days : {}", days);

        if(days < 15){
            // *** Get data from database *** //

        }else {
            // *** Get data from database *** //

        }

    }

    // *** Function for BRMS (PreScreenRules) ***//
    public List<PreScreenResponseView> getPreScreenResultFromBRMS(List<CustomerInfoView> customerInfoViewList){
        //TODO Transform view model to prescreenRequest
        PreScreenRequest preScreenRequest = preScreenResultTransform.transformToRequest(customerInfoViewList);
        List<PreScreenResponse> preScreenResponseList;
        preScreenResponseList = brmsInterface.checkPreScreenRule(preScreenRequest);

        List<PreScreenResponseView> preScreenResponseViewList = preScreenResultTransform.transformResponseToView(preScreenResponseList);

        return preScreenResponseViewList;
    }

    public List<PreScreenResponseView> getPreScreenCustomerResult(List<PreScreenResponseView> prescreenResponseViews){
        List<PreScreenResponseView> preScreenResponseViewList = new ArrayList<PreScreenResponseView>();
        preScreenResponseViewList = preScreenResultTransform.tranformToCustomerResponse(prescreenResponseViews);

        return preScreenResponseViewList;
    }

    public List<PreScreenResponseView> getPreScreenGroupResult(List<PreScreenResponseView> prescreenResponseViews){
        List<PreScreenResponseView> preScreenResponseViewList = new ArrayList<PreScreenResponseView>();
        preScreenResponseViewList = preScreenResultTransform.tranformToGroupResponse(prescreenResponseViews);

        return preScreenResponseViewList;
    }

    public void savePreScreenResult(List<PreScreenResponseView> preScreenResponseViews, long workCasePreScreenId, long workCaseId, long stepId, User user){
        WorkCasePrescreen workCasePrescreen = null;
        WorkCase workCase = null;
        Step step = null;

        if(workCasePreScreenId != 0){ workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId); }
        if(workCaseId != 0){ workCase = workCaseDAO.findById(workCaseId); }
        if(stepId != 0){ step = stepDAO.findById(stepId); }

        List<BRMSResult> brmsResultList = preScreenResultTransform.transformResultToModel(preScreenResponseViews, workCasePrescreen, workCase, step, user);

        brmsResultDAO.persist(brmsResultList);
    }

    // *** Function for PreScreen Initial *** //
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
