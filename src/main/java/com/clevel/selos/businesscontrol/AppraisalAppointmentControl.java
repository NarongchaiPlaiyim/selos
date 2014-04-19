package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.RequestAppraisalValue;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppraisalDetailView;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.model.view.ContactRecordDetailView;
import com.clevel.selos.model.view.CustomerAcceptanceView;
import com.clevel.selos.transform.*;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class AppraisalAppointmentControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private WorkCasePrescreenDAO workCasePrescreenDAO;
    @Inject
    private AppraisalDAO appraisalDAO;
    @Inject
    private AppraisalDetailDAO appraisalDetailDAO;
    @Inject
    private AppraisalContactDetailDAO appraisalContactDetailDAO;
    @Inject
    private AppraisalTransform appraisalTransform;
    @Inject
    private AppraisalDetailTransform appraisalDetailTransform;
    @Inject
    private AppraisalContactDetailTransform appraisalContactDetailTransform;
    @Inject
    private NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    private NewCollateralDAO newCollateralDAO;
    @Inject
    private NewCollateralHeadDAO newCollateralHeadDAO;
    @Inject
    private NewCollateralSubDAO newCollateralSubDAO;
    @Inject
    private CustomerAcceptanceTransform customerAcceptanceTransform;
    @Inject
    private CustomerAcceptanceDAO customerAcceptanceDAO;
    @Inject
    private ContactRecordDetailTransform contactRecordDetailTransform;
    @Inject
    private ContactRecordDetailDAO contactRecordDetailDAO;

    private Appraisal appraisal;
    private AppraisalView appraisalView;
    private List<AppraisalContactDetail> appraisalContactDetailList;
    private List<ContactRecordDetail> contactRecordDetailList;
    private List<ContactRecordDetailView> contactRecordDetailViewList;
    private List<AppraisalDetailView> appraisalDetailViewList;

    private List<NewCollateral> newCollateralList;
    private List<NewCollateralHead> newCollateralHeadList;
    private WorkCase workCase;
    private WorkCasePrescreen workCasePrescreen;
    private NewCreditFacility newCreditFacility;
    private CustomerAcceptance customerAcceptance;
    private ContactRecordDetail contactRecordDetail;

    @Inject
    public AppraisalAppointmentControl(){

    }
	
	public AppraisalView getAppraisalAppointment(final long workCaseId, final long workCasePreScreenId){
        log.info("-- getAppraisalAppointment WorkCaseId : {}, WorkCasePreScreenId [{}], User.id[{}]", workCaseId, workCasePreScreenId, getCurrentUserID());
        if(!Util.isNull(Long.toString(workCaseId)) && workCaseId != 0){
            appraisal  = appraisalDAO.findByWorkCaseId(workCaseId);
            log.debug("getAppraisalAppointment by workCaseId - appraisal: {}", appraisal);
            newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            log.debug("getAppraisalAppointment by workCaseId - creditFacility : {}", newCreditFacility);
            customerAcceptance = customerAcceptanceDAO.findCustomerAcceptanceByWorkCase(workCaseId);
            if(!Util.isNull(customerAcceptance)){
                log.debug("getAppraisalAppointment by workCaseId - CustomerAcceptance : {}", customerAcceptance);
                log.debug("-- CustomerAcceptance.id[{}]", customerAcceptance.getId());
            }
            contactRecordDetailList = contactRecordDetailDAO.findByWorkCaseId(workCaseId);
            if(!Util.isNull(contactRecordDetailList)){
                log.debug("getAppraisalAppointment by workCaseId - ContactRecordDetailList.size()[{}]", contactRecordDetailList.size());
            }
        }else if(!Util.isNull(Long.toString(workCasePreScreenId)) && workCasePreScreenId != 0){
            appraisal  = appraisalDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            log.debug("getAppraisalAppointment by workCasePreScreenId : {}", appraisal);
            newCreditFacility = newCreditFacilityDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            log.debug("getAppraisalAppointment by workCasePreScreenId - creditFacility : {}", newCreditFacility);
            customerAcceptance = customerAcceptanceDAO.findCustomerAcceptanceByWorkCasePrescreen(workCasePreScreenId);
            if(!Util.isNull(customerAcceptance)){
                log.debug("getAppraisalAppointment by workCasePreScreenId - CustomerAcceptance : {}", customerAcceptance);
                log.debug("-- CustomerAcceptance.id[{}]", customerAcceptance.getId());
            }
            contactRecordDetailList = contactRecordDetailDAO.findByWorkCasePrescreenId(workCasePreScreenId);
            if(!Util.isNull(contactRecordDetailList)){
                log.debug("getAppraisalAppointment by workCasePreScreenId - ContactRecordDetailList.size()[{}]", contactRecordDetailList.size());
            }
        }

        if(!Util.isNull(appraisal)){
            appraisalContactDetailList = Util.safetyList(appraisalContactDetailDAO.findByAppraisalId(appraisal.getId()));
            appraisal.setAppraisalContactDetailList(appraisalContactDetailList);
            appraisalView = appraisalTransform.transformToView(appraisal, getCurrentUser());

            if(!Util.isNull(newCreditFacility)){
                newCollateralList = Util.safetyList(newCollateralDAO.findNewCollateralByNewCreditFacility(newCreditFacility));
                for(NewCollateral newCollateral : newCollateralList){
                    //newCollateral.setNewCollateralHeadList(newCollateralHeadDAO.findByNewCollateralIdAndPurpose(newCollateral.getId()));
                    newCollateral.setNewCollateralHeadList(newCollateralHeadDAO.findByCollateralProposeTypeRequestAppraisalType(newCollateral.getId(), ProposeType.P, RequestAppraisalValue.NOT_REQUEST));
                }
                appraisalDetailViewList = appraisalDetailTransform.transformToView(newCollateralList);
                appraisalView.setAppraisalDetailViewList(appraisalDetailViewList);

                if(!Util.isNull(contactRecordDetailList) && !Util.isZero(contactRecordDetailList.size())){
                    contactRecordDetailViewList = contactRecordDetailTransform.transformToView(contactRecordDetailList);
                    if(!Util.isNull(contactRecordDetailViewList) && !Util.isZero(contactRecordDetailViewList.size())){
                        appraisalView.setContactRecordDetailViewList(contactRecordDetailViewList);
                        log.debug("-- AppraisalView.ContactRecordDetailViewList.size()[{}]", appraisalView.getContactRecordDetailViewList().size());
                    }
                } else {
                    contactRecordDetailViewList = new ArrayList<ContactRecordDetailView>();
                    log.debug("-- [NEW]ContactRecordDetailViewList created");
                    appraisalView.setContactRecordDetailViewList(contactRecordDetailViewList);
                }

                log.info("-- getAppraisalRequest ::: AppraisalView : {}", appraisalView.toString());
                return appraisalView;
            } else {
                log.debug("-- NewCreditFacility = null");
                return appraisalView;
            }
        } else {
            log.debug("-- Find by work case id = {} appraisal is null. ", workCaseId);
            return appraisalView;
        }
    }

    public void onSaveAppraisalAppointment(final AppraisalView appraisalView,final long workCaseId, final long workCasePreScreenId, final List<ContactRecordDetailView> contactRecordDetailViewList, final CustomerAcceptanceView cusAcceptView){
        log.info("-- onSaveAppraisalAppointment");
        if(!Util.isNull(Long.toString(workCaseId)) && workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            workCasePrescreen = null;
            newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
        } else if (!Util.isNull(Long.toString(workCasePreScreenId)) && workCasePreScreenId != 0){
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            workCase = null;
            newCreditFacility = newCreditFacilityDAO.findByWorkCasePreScreenId(workCasePreScreenId);
        }

        if(!Util.isNull(workCase) || !Util.isNull(workCasePrescreen)){

            appraisal = appraisalTransform.transformToModel(appraisalView, workCase, workCasePrescreen, getCurrentUser());

            //remove all appraisalContactDetailList
            List<AppraisalContactDetail> appraisalContactDetailDeleteList = Util.safetyList(appraisalContactDetailDAO.findByAppraisalId(appraisal.getId()));
            appraisalContactDetailDAO.delete(appraisalContactDetailDeleteList);

            log.debug("onSaveAppraisalAppointment ::: before persist appraisal : {}", appraisal);
            appraisalDAO.persist(appraisal);
            log.debug("onSaveAppraisalAppointment ::: after persist appraisal : {}", appraisal);



            if(Util.isNull(newCreditFacility)){
                newCreditFacility = new NewCreditFacility();
                newCreditFacility.setWorkCasePrescreen(workCasePrescreen);
                newCreditFacility.setWorkCase(workCase);
            }
            log.debug("-- NewCreditFacility.id[{}]", newCreditFacility.getId());

            //From P'LK CustomerAcceptanceControl
            CustomerAcceptance cusAccept = null;
            if (cusAcceptView.getId() <= 0) { //new
                cusAccept = customerAcceptanceTransform.transformToNewModel(cusAcceptView, workCase, workCasePrescreen, getCurrentUser());
                customerAcceptanceDAO.save(cusAccept);
                customerAcceptance = cusAccept;
            } else {
                cusAccept = customerAcceptanceDAO.findById(cusAcceptView.getId());
                cusAccept.setModifyBy(getCurrentUser());
                cusAccept.setModifyDate(new Date());
                customerAcceptanceDAO.persist(cusAccept);
                customerAcceptance = cusAccept;
            }

            //Add and update first
            for (ContactRecordDetailView view : contactRecordDetailViewList) {
                if (view.isNew()) {
                    ContactRecordDetail model = contactRecordDetailTransform.transformToNewModel(view, getCurrentUser(), cusAccept);
                    contactRecordDetailDAO.save(model);
                } else if (view.isNeedUpdate()) {
                    //get from db
                    ContactRecordDetail model = contactRecordDetailDAO.findById(view.getId());
                    contactRecordDetailTransform.updateModelFromView(model, view, getCurrentUser());
                    contactRecordDetailDAO.persist(model);
                }
            }

            //Delete
            for (ContactRecordDetailView view : contactRecordDetailViewList) {
                if (view.isNew())
                    continue;
                contactRecordDetailDAO.deleteById(view.getId());
            }



            appraisalDetailViewList = Util.safetyList(appraisalView.getAppraisalDetailViewList());
            log.debug("onSaveAppraisalAppointment ::: appraisalDetailViewList : {}", appraisalDetailViewList);

            //remove all collateral head from list in database
            if(newCreditFacility.getId() != 0){
                newCollateralList = newCollateralDAO.findNewCollateralByNewCreditFacility(newCreditFacility);
            }else{
                newCollateralList = new ArrayList<NewCollateral>();
            }

            //set flag 0 for all collateral
            log.debug("onSaveAppraisalAppointment ::: newCollateralList from database : {}", newCollateralList);
            for(NewCollateral newCollateral : newCollateralList){
                newCollateralHeadList = Util.safetyList(newCollateralHeadDAO.findByNewCollateralId(newCollateral.getId()));
                for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                    newCollateralHead.setAppraisalRequest(RequestAppraisalValue.NOT_REQUEST.value());
                }
                newCollateralHeadDAO.persist(newCollateralHeadList);
            }

            //TODO
            if(!Util.isNull(Long.toString(workCaseId)) && workCaseId != 0){
                //remove all contactRecordDetailViewList
                contactRecordDetailList = Util.safetyList(contactRecordDetailDAO.findByWorkCaseId(workCaseId));
                contactRecordDetailDAO.delete(contactRecordDetailList);
                log.debug("-- ContactRecordDetailList.size()[{}] deleted", contactRecordDetailList.size());

                if(!Util.isNull(contactRecordDetailViewList) && !Util.isZero(contactRecordDetailViewList.size())){
                    contactRecordDetailList = contactRecordDetailTransform.transformToModel(contactRecordDetailViewList, workCase, getCurrentUser(), workCasePrescreen, workCase.getStep(), customerAcceptance);
                    contactRecordDetailDAO.persist(contactRecordDetailList);
                    log.debug("-- ContactRecordDetailList.size()[{}] saved", contactRecordDetailList.size());
                }
            }else if(!Util.isNull(Long.toString(workCasePreScreenId)) && workCasePreScreenId != 0){
                contactRecordDetailList = Util.safetyList(contactRecordDetailDAO.findByWorkCasePrescreenId(workCasePreScreenId));
                contactRecordDetailDAO.delete(contactRecordDetailList);
                log.debug("-- ContactRecordDetailList.size()[{}] deleted", contactRecordDetailList.size());

                if(!Util.isNull(contactRecordDetailViewList) && !Util.isZero(contactRecordDetailViewList.size())){
                    contactRecordDetailList = contactRecordDetailTransform.transformToModel(contactRecordDetailViewList, workCase, getCurrentUser(), workCasePrescreen, workCasePrescreen.getStep(), customerAcceptance);
                    contactRecordDetailDAO.persist(contactRecordDetailList);
                    log.debug("-- ContactRecordDetailList.size()[{}] saved", contactRecordDetailList.size());
                }
            }


            //transform collateral head from view
            newCollateralList.clear();
            newCollateralList = Util.safetyList(appraisalDetailTransform.transformToModel(appraisalDetailViewList, newCreditFacility, getCurrentUser()));
            log.debug("onSaveAppraisalAppointment ::: before persist newCreditfacility : {}", newCreditFacility);
            newCreditFacilityDAO.persist(newCreditFacility);
            log.debug("onSaveAppraisalAppointment ::: after persist newCreditfacility : {}", newCreditFacility);

            log.debug("onSaveAppraisalAppointment ::: before persist newCollateralList : {}", newCollateralList);
            newCollateralDAO.persist(newCollateralList);
            log.debug("onSaveAppraisalAppointment ::: after persist newCollateralList : {}", newCollateralList);
            log.debug("-- onSaveAppraisalAppointment end");

        }
    }
}