package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.AppraisalCompanyDAO;
import com.clevel.selos.dao.master.HolidayDAO;
import com.clevel.selos.dao.master.ProvinceDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.RequestAppraisalValue;
import com.clevel.selos.model.StatusValue;
import com.clevel.selos.model.db.master.AppraisalCompany;
import com.clevel.selos.model.db.master.Province;
import com.clevel.selos.model.db.master.User;
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
    private ProposeLineDAO newCreditFacilityDAO;
    @Inject
    private ProposeCollateralInfoDAO newCollateralDAO;
    @Inject
    private ProposeCollateralInfoHeadDAO newCollateralHeadDAO;
    @Inject
    private ProposeCollateralInfoSubDAO newCollateralSubDAO;
    @Inject
    private CustomerAcceptanceTransform customerAcceptanceTransform;
    @Inject
    private CustomerAcceptanceDAO customerAcceptanceDAO;
    @Inject
    private ContactRecordDetailTransform contactRecordDetailTransform;
    @Inject
    private ContactRecordDetailDAO contactRecordDetailDAO;
    @Inject
    private AppraisalCompanyDAO appraisalCompanyDAO;
    private Appraisal appraisal;
    private AppraisalView appraisalView;
    private List<AppraisalContactDetail> appraisalContactDetailList;
    private List<ContactRecordDetail> contactRecordDetailList;
    private List<ContactRecordDetailView> contactRecordDetailViewList;
    private List<AppraisalDetailView> appraisalDetailViewList;

    private List<ProposeCollateralInfo> newCollateralList;
    private List<ProposeCollateralInfoHead> newCollateralHeadList;
    private WorkCase workCase;
    private WorkCasePrescreen workCasePrescreen;
    private ProposeLine newCreditFacility;
    private CustomerAcceptance customerAcceptance;
    private ContactRecordDetail contactRecordDetail;
    private User user;
    @Inject
    private ProvinceDAO provinceDAO;
    @Inject
    private HolidayDAO holidayDAO;
    @Inject private UserDAO userDAO;

    @Inject
    public AppraisalAppointmentControl(){

    }

    public List<Province> getProvince(){
        return provinceDAO.findAllASC();
    }

    public List<AppraisalCompany> getCompany(){
        return appraisalCompanyDAO.findAllASC();
    }

    public String getZoneLocation(String bdmId){
        try {
            user = userDAO.findUserByID(bdmId);
            return user.getTeam().getTeam_name();
        } catch (Exception e){
            return "";
        }
    }

    public boolean isHoliday(final Date holiday){
        try {
            return holidayDAO.isHoliday(holiday);
        } catch (Exception e){
            log.debug("-- Exception while get holiday {}", e);
            return false;
        }
    }

	public AppraisalView getAppraisalAppointment(long workCaseId, long workCasePreScreenId, long statusId){
        log.info("-- getAppraisalAppointment WorkCaseId : {}, WorkCasePreScreenId [{}], User.id[{}]", workCaseId, workCasePreScreenId, getCurrentUserID());
        appraisalView = null;
        if(!Util.isZero(workCaseId)){
            appraisal  = appraisalDAO.findByWorkCaseId(workCaseId);
            log.debug("getAppraisalAppointment by workCaseId - appraisal: {}", appraisal);
            newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            log.debug("getAppraisalAppointment by workCaseId - creditFacility : {}", newCreditFacility != null ? newCreditFacility.getId() : null);
            customerAcceptance = customerAcceptanceDAO.findCustomerAcceptanceByWorkCaseId(workCaseId);
            log.debug("getAppraisalAppointment by workCaseId - CustomerAcceptance : {}", customerAcceptance != null ? customerAcceptance.getId() : null);
            contactRecordDetailList = contactRecordDetailDAO.findByWorkCaseId(workCaseId);
            log.debug("getAppraisalAppointment by workCaseId - ContactRecordDetailList.size() : [{}]", contactRecordDetailList != null ? contactRecordDetailList.size() : null);
        }else if(!Util.isZero(workCasePreScreenId)){
            appraisal  = appraisalDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            log.debug("getAppraisalAppointment by workCasePreScreenId : {}", appraisal);
            newCreditFacility = newCreditFacilityDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            log.debug("getAppraisalAppointment by workCasePreScreenId - creditFacility : {}", newCreditFacility != null ? newCreditFacility.getId() : null);
            customerAcceptance = customerAcceptanceDAO.findCustomerAcceptanceByWorkCasePrescreenId(workCasePreScreenId);
            log.debug("getAppraisalAppointment by workCasePreScreenId - CustomerAcceptance : {}", customerAcceptance != null ? customerAcceptance.getId() : null);
            contactRecordDetailList = contactRecordDetailDAO.findByWorkCasePrescreenId(workCasePreScreenId);
            log.debug("getAppraisalAppointment by workCasePreScreenId - ContactRecordDetailList.size()[{}]", contactRecordDetailList != null ? contactRecordDetailList.size() : null);
        }

        if(!Util.isNull(appraisal)){
            appraisalContactDetailList = Util.safetyList(appraisalContactDetailDAO.findByAppraisalId(appraisal.getId()));
            appraisal.setAppraisalContactDetailList(appraisalContactDetailList);
            appraisalView = appraisalTransform.transformToView(appraisal, getCurrentUser());

            if(!Util.isNull(newCreditFacility)){
                newCollateralList = Util.safetyList(newCollateralDAO.findCollateralForAppraisalAppointment(newCreditFacility, ProposeType.A));
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

    public void onSaveAppraisalAppointment(AppraisalView appraisalView, long workCaseId, long workCasePreScreenId, List<ContactRecordDetailView> contactRecordDetailViewList, CustomerAcceptanceView cusAcceptView, long statusId){
        log.debug("-- onSaveAppraisalAppointment");
        if(!Util.isNull(Long.toString(workCaseId)) && workCaseId != 0){
            workCase = workCaseDAO.findById(workCaseId);
            workCasePrescreen = null;
            newCreditFacility = newCreditFacilityDAO.findByWorkCaseId(workCaseId);
            customerAcceptance = customerAcceptanceDAO.findCustomerAcceptanceByWorkCaseId(workCaseId);
        } else if (!Util.isNull(Long.toString(workCasePreScreenId)) && workCasePreScreenId != 0){
            workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
            workCase = null;
            newCreditFacility = newCreditFacilityDAO.findByWorkCasePreScreenId(workCasePreScreenId);
            customerAcceptance = customerAcceptanceDAO.findCustomerAcceptanceByWorkCasePrescreenId(workCasePreScreenId);
        }

        log.debug("onSaveAppraisalAppointment ::: workCase : {}, workCasePrescreen : {}", workCase, workCasePrescreen);
        log.debug("onSaveAppraisalAppointment ::: newCreditFacility : {}", newCreditFacility);

        if(!Util.isNull(workCase) || !Util.isNull(workCasePrescreen)){
            appraisal = appraisalTransform.transformToModel(appraisalView, workCase, workCasePrescreen, getCurrentUser());

            //remove all appraisalContactDetailList
            List<AppraisalContactDetail> appraisalContactDetailDeleteList = Util.safetyList(appraisalContactDetailDAO.findByAppraisalId(appraisal.getId()));
            appraisalContactDetailDAO.delete(appraisalContactDetailDeleteList);
            log.debug("onSaveAppraisalAppointment ::: before persist appraisal : {}", appraisal);
            appraisalDAO.persist(appraisal);
            log.debug("onSaveAppraisalAppointment ::: after persist appraisal : {}", appraisal);

            if(Util.isNull(newCreditFacility)){
                newCreditFacility = new ProposeLine();
                newCreditFacility.setWorkCasePrescreen(workCasePrescreen);
                newCreditFacility.setWorkCase(workCase);
            }
            log.debug("-- NewCreditFacility.id[{}]", newCreditFacility.getId());

            /*//From P'LK CustomerAcceptanceControl
            CustomerAcceptance cusAccept = null;*/

            appraisalDetailViewList = Util.safetyList(appraisalView.getAppraisalDetailViewList());
            log.debug("onSaveAppraisalAppointment ::: appraisalDetailViewList : {}", appraisalDetailViewList);

            //remove all collateral head from list in database
            if(newCreditFacility.getId() != 0){
                newCollateralList = newCollateralDAO.findCollateralForAppraisal(newCreditFacility, ProposeType.A);
                //set flag 0 for all collateral
                log.debug("onSaveAppraisalAppointment ::: newCollateralList from database : {}", newCollateralList);
                for(ProposeCollateralInfo newCollateral : newCollateralList){
                    newCollateralHeadList = newCollateral.getProposeCollateralInfoHeadList();
                    for(ProposeCollateralInfoHead newCollateralHead : newCollateralHeadList){
                        newCollateralHead.setAppraisalRequest(RequestAppraisalValue.NOT_REQUEST.value());
                    }
                    newCollateral.setAppraisalRequest(RequestAppraisalValue.NOT_REQUEST.value());
                    newCollateralDAO.persist(newCollateral);
                }
            }else{
                newCollateralList = new ArrayList<ProposeCollateralInfo>();
            }

            try {
                newCollateralList.clear();
                newCollateralList = Util.safetyList(appraisalDetailTransform.transformToModel(appraisalDetailViewList, newCreditFacility, getCurrentUser(), RequestAppraisalValue.REQUESTED, ProposeType.A));
                log.debug("onSaveAppraisalAppointment ::: before persist newCollateralList : {}", newCollateralList);
                if(!Util.isNull(newCollateralList) && !Util.isZero(newCollateralList.size())){
                    newCollateralDAO.persist(newCollateralList);
                    log.debug("-- NewCollateralList.size()[{}]", newCollateralList.size());
                }
                log.debug("onSaveAppraisalAppointment ::: after persist newCollateralList : {}", newCollateralList);
            } catch (Exception e) {
                log.debug("-- Exception while call NewCollateralDAO ", e);
            }

            if(!Util.isNull(customerAcceptance) && !Util.isZero(customerAcceptance.getId())){
                customerAcceptanceDAO.delete(customerAcceptance);
                log.debug("-- CustomerAcceptance.id[{}] deleted", customerAcceptance.getId());
//                customerAcceptance = new CustomerAcceptance();
                customerAcceptance = customerAcceptanceTransform.transformToModel(cusAcceptView, workCase, workCasePrescreen, getCurrentUser());
                customerAcceptanceDAO.save(customerAcceptance);
                log.debug("-- CustomerAcceptance.id[{}] saved", customerAcceptance.getId());
            } else {
//                customerAcceptance = new CustomerAcceptance();
                customerAcceptance = customerAcceptanceTransform.transformToModel(cusAcceptView, workCase, workCasePrescreen, getCurrentUser());
                customerAcceptanceDAO.save(customerAcceptance);
                log.debug("-- CustomerAcceptance.id[{}] saved", customerAcceptance.getId());
            }

            log.debug("-- CustomerAcceptance.id[{}]", customerAcceptance.getId());
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

            //Delete Collateral which Delete by AAD
            if(!Util.isNull(appraisalView.getRemoveCollListId()) && appraisalView.getRemoveCollListId().size() > 0){
                for(Long colId : appraisalView.getRemoveCollListId()){
                    log.debug("onSaveAppraisalAppointment delete removed Collateral id : {}", colId);
                    if(!Util.isZero(colId)) {
                        ProposeCollateralInfo proposeCollateralInfo = newCollateralDAO.findById(colId);
                        newCollateralDAO.delete(proposeCollateralInfo);
                    }
                }
            }

            log.debug("-- onSaveAppraisalAppointment end");
        }
    }
}