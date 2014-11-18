package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.businesscontrol.util.stp.STPExecutor;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.RelationCustomerDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.RLOSInterface;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.corebanking.model.corporateInfo.CorporateResult;
import com.clevel.selos.integration.corebanking.model.individualInfo.IndividualResult;
import com.clevel.selos.integration.ncb.NCBInterfaceImpl;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSOutputModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.RegistType;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.*;
import com.clevel.selos.integration.rlos.csi.model.CSIData;
import com.clevel.selos.integration.rlos.csi.model.CSIInputData;
import com.clevel.selos.integration.rlos.csi.model.CSIResult;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.master.DocumentType;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.*;
import com.clevel.selos.transform.business.CustomerBizTransform;
import com.clevel.selos.transform.business.NCBBizTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

@Stateless
public class PrescreenBusinessControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    @Inject
    PrescreenTransform prescreenTransform;
    @Inject
    PrescreenFacilityTransform prescreenFacilityTransform;
    @Inject
    BizInfoDetailTransform bizInfoTransform;
    @Inject
    CustomerTransform customerTransform;
    @Inject
    PrescreenBusinessTransform prescreenBusinessTransform;
    @Inject
    CustomerBizTransform customerBizTransform;
    @Inject
    NCBBizTransform ncbBizTransform;
    @Inject
    PrescreenCollateralTransform prescreenCollateralTransform;
    @Inject
    NCBTransform ncbTransform;
    @Inject
    NCBDetailTransform ncbDetailTransform;

    @Inject
    private UserDAO userDAO;
    @Inject
    PrescreenDAO prescreenDAO;
    @Inject
    PrescreenFacilityDAO prescreenFacilityDAO;
    @Inject
    BizInfoDetailDAO bizInfoDAO;
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
    CustomerDAO customerDAO;
    @Inject
    CustomerOblInfoDAO customerOblInfoDAO;
    @Inject
    IndividualDAO individualDAO;
    @Inject
    JuristicDAO juristicDAO;
    @Inject
    ActionDAO actionDAO;
    @Inject
    AddressDAO addressDAO;
    @Inject
    WarningCodeDAO warningCodeDAO;
    @Inject
    CustomerCSIDAO customerCSIDAO;
    @Inject
    PrescreenBusinessDAO prescreenBusinessDAO;
    @Inject
    PrescreenCollateralDAO prescreenCollateralDAO;
    @Inject
    RelationDAO relationDAO;
    @Inject
    NCBDetailDAO ncbDetailDAO;
    @Inject
    NCBDAO ncbDAO;
    @Inject
    CustomerEntityDAO customerEntityDAO;
    @Inject
    CustomerAccountDAO customerAccountDAO;
    @Inject
    CustomerAccountNameDAO customerAccountNameDAO;
    @Inject
    RelationCustomerDAO relationCustomerDAO;
    @Inject
    ProductGroupDAO productGroupDAO;
    @Inject
    CustomerOblAccountInfoDAO customerOblAccountInfoDAO;
    @Inject
    WorkCaseOwnerDAO workCaseOwnerDAO;
    @Inject
    ReasonDAO reasonDAO;
    @Inject
    MandateDocSummaryDAO mandateDocSummaryDAO;

    @Inject
    RMInterface rmInterface;
    @Inject
    BPMInterface bpmInterface;
    @Inject
    RLOSInterface rlosInterface;
    @Inject
    private NCBInterfaceImpl ncbInterface;

    @Inject
    STPExecutor stpExecutor;
    @Inject
    BPMExecutor bpmExecutor;

    @Inject
    private ExistingCreditControl existingCreditControl;
    @Inject
    private BankStmtControl bankStmtControl;
    @Inject
    private UWRuleResultControl uwRuleResultControl;
    @Inject
    private CustomerInfoControl customerInfoControl;
    @Inject
    private MandateFieldValidationControl mandateFieldValidationControl;

    @Inject
    public PrescreenBusinessControl(){

    }

    //** function for integration **//

    // *** Function for RM *** //
    public CustomerInfoResultView getCustomerInfoFromRM(CustomerInfoView customerInfoView, User user){
        CustomerInfoResultView customerInfoResultSearch = new CustomerInfoResultView();
        log.info("getCustomerInfoFromRM ::: SearchBy : {}", customerInfoView.getSearchBy());
        log.info("getCustomerInfoFromRM ::: SearchId : {}", customerInfoView.getSearchId());

        DocumentType masterDocumentType;

        RMInterface.SearchBy searchBy = RMInterface.SearchBy.CUSTOMER_ID;
        if(customerInfoView.getSearchBy() == 1){
            searchBy = RMInterface.SearchBy.CUSTOMER_ID;
        }else if(customerInfoView.getSearchBy() == 2){
            searchBy = RMInterface.SearchBy.TMBCUS_ID;
        }

        masterDocumentType = documentTypeDAO.findById(customerInfoView.getDocumentType().getId());

        String userId = user.getId();
        String documentTypeCode = masterDocumentType.getDocumentTypeCode();
        log.info("getCustomerInfoFromRM ::: userId : {}", userId);
        log.info("getCustomerInfoFromRM ::: documentType : {}", masterDocumentType);
        log.info("getCustomerInfoFromRM ::: documentTypeCode : {}", documentTypeCode);

        RMInterface.DocumentType documentType = RMInterface.DocumentType.CITIZEN_ID;

        if(documentTypeCode.equalsIgnoreCase("CI")){
            documentType = RMInterface.DocumentType.CITIZEN_ID;
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(1);
            customerInfoView.setCustomerEntity(customerEntity);
        }else if(documentTypeCode.equalsIgnoreCase("PP")){
            documentType = RMInterface.DocumentType.PASSPORT;
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(1);
            customerInfoView.setCustomerEntity(customerEntity);
        }else if(documentTypeCode.equalsIgnoreCase("SC")){
            documentType = RMInterface.DocumentType.CORPORATE_ID;
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(2);
            customerInfoView.setCustomerEntity(customerEntity);
        }

        if(customerInfoView.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()) {
            IndividualResult individualResult = rmInterface.getIndividualInfo(userId, customerInfoView.getSearchId(), documentType, searchBy);
            log.info("getCustomerInfoFromRM ::: individualResult : {}", individualResult);
            customerInfoResultSearch = customerBizTransform.tranformIndividual(individualResult);
        } else if(customerInfoView.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
            CorporateResult corporateResult = rmInterface.getCorporateInfo(userId, customerInfoView.getSearchId(), documentType, searchBy);
            log.info("getCustomerInfoFromRM ::: corporateResult : {}", corporateResult);
            customerInfoResultSearch = customerBizTransform.tranformJuristic(corporateResult);
        }

        log.info("getCustomerInfoFromRM ::: success!!");
        log.info("getCustomerInfoFromRM ::: customerInfoSearch : {}", customerInfoResultSearch);
        return customerInfoResultSearch;
    }

    /**
     * To Retreive the following Interface Information in Prescreen
     * <ul>
     *     <li>DWH Obligation - Existing Credit</li>
     *     <li>DWH BankStatement</li>
     * </ul>
     * @param customerInfoViewList
     * @param prescreenResultView
     * @return
     */
    public PrescreenResultView getInterfaceInfo(List<CustomerInfoView> customerInfoViewList, PrescreenResultView prescreenResultView, long workCasePreScreenId) throws Exception{
        log.info("retreive interface for customer list: {}", customerInfoViewList);

        ExistingCreditFacilityView existingCreditFacilityView = existingCreditControl.refreshExistingCredit(customerInfoViewList);

        BankStmtSummaryView bankStmtSummaryView = bankStmtControl.retrieveBankStmtInterface(customerInfoViewList, prescreenResultView.getExpectedSubmitDate());
        //BankStmtSummaryView bankStmtSummaryView = new BankStmtSummaryView();

        //Set Existing Credit for PreScreen
        List<ExistingCreditDetailView> borrowerComExistingCredit = existingCreditFacilityView.getBorrowerComExistingCredit();
        List<ExistingCreditDetailView> borrowerRetailExistingCredit = existingCreditFacilityView.getBorrowerRetailExistingCredit();
        List<ExistingCreditDetailView> relatedComExistingCredit = existingCreditFacilityView.getRelatedComExistingCredit();
        List<ExistingCreditDetailView> relatedRetailExistingCredit = existingCreditFacilityView.getRelatedRetailExistingCredit();

        BigDecimal totalBorrowerComLimit = existingCreditFacilityView.getTotalBorrowerComLimit();
        BigDecimal totalBorrowerRetailLimit = existingCreditFacilityView.getTotalBorrowerRetailLimit();
        BigDecimal totalRelatedComLimit = existingCreditFacilityView.getTotalRelatedComLimit();
        BigDecimal totalRelatedRetailLimit = existingCreditFacilityView.getTotalRelatedRetailLimit();

        List<ExistingCreditDetailView> borrowerExistingCreditPreScreen = new ArrayList<ExistingCreditDetailView>();
        List<ExistingCreditDetailView> relateExistingCreditPresScreen = new ArrayList<ExistingCreditDetailView>();
        BigDecimal totalBorrowerLimitPreScreen = BigDecimal.ZERO;
        BigDecimal totalRelatedLimitPreScreen = BigDecimal.ZERO;

        if(borrowerComExistingCredit!=null && borrowerComExistingCredit.size()>0){
            for(ExistingCreditDetailView existingCreditDetailView : borrowerComExistingCredit) {
                borrowerExistingCreditPreScreen.add(existingCreditDetailView);
            }
        }
        if(borrowerRetailExistingCredit!=null && borrowerRetailExistingCredit.size()>0){
            for(ExistingCreditDetailView existingCreditDetailView : borrowerRetailExistingCredit) {
                borrowerExistingCreditPreScreen.add(existingCreditDetailView);
            }
        }

        if(relatedComExistingCredit!=null && relatedComExistingCredit.size()>0){
            for(ExistingCreditDetailView existingCreditDetailView : relatedComExistingCredit) {
                relateExistingCreditPresScreen.add(existingCreditDetailView);
            }
        }
        if(relatedRetailExistingCredit!=null && relatedRetailExistingCredit.size()>0){
            for(ExistingCreditDetailView existingCreditDetailView : relatedRetailExistingCredit) {
                relateExistingCreditPresScreen.add(existingCreditDetailView);
            }
        }

        //add total
        if(totalBorrowerComLimit!=null && totalBorrowerComLimit.compareTo(BigDecimal.ZERO)>0){
            totalBorrowerLimitPreScreen = totalBorrowerLimitPreScreen.add(totalBorrowerComLimit);
        }

        if(totalBorrowerRetailLimit!=null && totalBorrowerRetailLimit.compareTo(BigDecimal.ZERO)>0){
            totalBorrowerLimitPreScreen = totalBorrowerLimitPreScreen.add(totalBorrowerRetailLimit);
        }

        if(totalRelatedComLimit!=null && totalRelatedComLimit.compareTo(BigDecimal.ZERO)>0){
            totalRelatedLimitPreScreen = totalRelatedLimitPreScreen.add(totalRelatedComLimit);
        }

        if(totalRelatedRetailLimit!=null && totalRelatedRetailLimit.compareTo(BigDecimal.ZERO)>0){
            totalRelatedLimitPreScreen = totalRelatedRetailLimit.add(totalRelatedRetailLimit);
        }

        existingCreditFacilityView.setBorrowerExistingCreditPreScreen(borrowerExistingCreditPreScreen);
        existingCreditFacilityView.setRelateExistingCreditPresScreen(relateExistingCreditPresScreen);
        existingCreditFacilityView.setTotalBorrowerLimitPreScreen(totalBorrowerLimitPreScreen);
        existingCreditFacilityView.setTotalRelatedLimitPreScreen(totalRelatedLimitPreScreen);

        if(bankStmtSummaryView != null){
            if(Util.safetyList(bankStmtSummaryView.getActionStatusViewList()).size() >= 1){
                ActionStatusView actionStatusView = bankStmtSummaryView.getActionStatusViewList().get(0);
                log.debug("getInterfaceInfo : actionStatusView : {}", actionStatusView);
                if(actionStatusView != null && actionStatusView.getStatusCode() == ActionResult.FAILED){
                    throw new Exception(actionStatusView.getStatusDesc());
                }
            }
        } else {
            prescreenResultView.setExistingCreditFacilityView(existingCreditFacilityView);
            //Calculate for Group Income
            BigDecimal groupIncome = new BigDecimal(0);
            for(CustomerInfoView customerInfoView : customerInfoViewList){
                if(Util.isTrue(customerInfoView.getReference().getGroupIncome())){
                    if(customerInfoView.getApproxIncome() != null)
                        groupIncome = groupIncome.add(customerInfoView.getApproxIncome());
                }
            }
            prescreenResultView.setGroupIncome(groupIncome);
        }

        prescreenResultView.setExistingCreditFacilityView(existingCreditFacilityView);
        prescreenResultView.setBankStmtSummaryView(bankStmtSummaryView);
        //Calculate for Group Income
        BigDecimal groupIncome = new BigDecimal(0);
        for(CustomerInfoView customerInfoView : customerInfoViewList){
            if(Util.isTrue(customerInfoView.getReference().getGroupIncome())){
                if(customerInfoView.getApproxIncome() != null)
                    groupIncome = groupIncome.add(customerInfoView.getApproxIncome());
            }
        }
        prescreenResultView.setGroupIncome(groupIncome);

        //Calculate for Group Exposure
        BigDecimal groupExposure = new BigDecimal(0);
        if(existingCreditFacilityView.getTotalBorrowerComLimit() != null)
            groupExposure = groupExposure.add(existingCreditFacilityView.getTotalBorrowerComLimit());
        if(existingCreditFacilityView.getTotalRelatedAppInRLOSLimit() != null)
            groupExposure = groupExposure.add(existingCreditFacilityView.getTotalBorrowerAppInRLOSLimit());
        if(existingCreditFacilityView.getTotalRelatedComLimit() != null)
            groupExposure = groupExposure.add(existingCreditFacilityView.getTotalRelatedComLimit());
        if(existingCreditFacilityView.getTotalRelatedAppInRLOSLimit() != null)
            groupExposure = groupExposure.add(existingCreditFacilityView.getTotalRelatedAppInRLOSLimit());
        if(existingCreditFacilityView.getTotalBorrowerRetailLimit() != null)
            groupExposure = groupExposure.add(existingCreditFacilityView.getTotalBorrowerRetailLimit());
        if(existingCreditFacilityView.getTotalRelatedRetailLimit() != null)
            groupExposure = groupExposure.add(existingCreditFacilityView.getTotalRelatedRetailLimit());

        prescreenResultView.setGroupExposure(groupExposure);

        //--TO Refresh Customer Obligation Info
        //--Get all customer in case
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        for(CustomerInfoView customerInfoView : customerInfoViewList){
            if(!Util.isEmpty(customerInfoView.getTmbCustomerId())) {
                //Remove all CustomerOblAccountInfo
                List<CustomerOblAccountInfo> customerOblAccountInfoList = customerOblAccountInfoDAO.findByCustomerId(customerInfoView.getId());
                customerOblAccountInfoDAO.delete(customerOblAccountInfoList);
                customerInfoView.setCustomerOblAccountInfoViewList(null);
                CustomerInfoView tmpCustomerInfoView = customerInfoControl.getCustomerCreditInfo(customerInfoView);
                Customer customer = new Customer();
                customer = customerTransform.transformToModel(tmpCustomerInfoView, workCasePrescreen, null, getCurrentUser());
                if (customer.getCustomerOblInfo() != null) {
                    customerOblInfoDAO.persist(customer.getCustomerOblInfo());
                }
                customerDAO.persist(customer);
            }
        }

        return prescreenResultView;
    }

    public PrescreenResultView getPrescreenResult(long workCasePreScreenId){
        Prescreen prescreen = prescreenDAO.findByWorkCasePrescreenId(workCasePreScreenId);
        PrescreenResultView prescreenResultView = prescreenTransform.getPrescreenResultView(prescreen);
        prescreenResultView.setExistingCreditFacilityView(existingCreditControl.getExistingCredit(workCasePreScreenId));

        //set Prescreen Result//
        UWRuleResultSummaryView uwRuleResultSummaryView = uwRuleResultControl.getUWRuleResultByWorkCasePrescreenId(workCasePreScreenId);
        if(uwRuleResultSummaryView != null){

            if(uwRuleResultSummaryView.getUwRuleResultDetailViewMap() != null){

                Map<RelationValue, Integer> _numberOfCusRelationMap = new TreeMap<RelationValue, Integer>();
                Map<String, CustomerInfoSimpleView> _customerInfoSimpleMap = new TreeMap<String, CustomerInfoSimpleView>();
                List<Customer> customerList = customerDAO.findByWorkCasePreScreenId(workCasePreScreenId);
                for(Customer customer : customerList){
                    if((customer.getRelation() != null) && (customer.getRelation().getId() != RelationValue.INDIRECTLY_RELATED.value())){
                        CustomerInfoSimpleView _customerInfoSimpleView = customerTransform.transformToSimpleView(customer);
                        RelationValue _relationValue = RelationValue.lookup(customer.getRelation().getId());

                        //Set Number of Customer Per Relation Type
                        Integer _numberOfCustomer = _numberOfCusRelationMap.get(_relationValue);
                        if(_numberOfCustomer == null)
                            _numberOfCustomer = 0;
                        _numberOfCusRelationMap.put(_relationValue, _numberOfCustomer+1);

                        //Set Customer with Map to Relation Type;
                        _customerInfoSimpleMap.put(_relationValue.value() + _customerInfoSimpleView.getCitizenId(), _customerInfoSimpleView);
                    }
                }


                Map<String, UWRuleResultDetailView> uwRuleResultDetailViewMap = uwRuleResultSummaryView.getUwRuleResultDetailViewMap();
                Map<Integer, UWRuleResultDetailView> _groupUWResultDetailMap = new TreeMap<Integer, UWRuleResultDetailView>();
                Map<Integer, String> checkCountRuleNameMap = new HashMap<Integer, String>();

                Integer lastOrder = Integer.MAX_VALUE;
                Map<Integer, PrescreenCusRulesGroupView> _prescreenCusRulesGroupViewMap = new TreeMap<Integer, PrescreenCusRulesGroupView>();
                for(UWRuleResultDetailView uwRuleResultDetailView : uwRuleResultDetailViewMap.values()){
                    if(UWRuleType.GROUP_LEVEL.equals(uwRuleResultDetailView.getUwRuleType())){
                        if(uwRuleResultDetailView.getRuleOrder() == 0)
                            _groupUWResultDetailMap.put(lastOrder--, uwRuleResultDetailView);
                        else
                            _groupUWResultDetailMap.put(uwRuleResultDetailView.getRuleOrder(), uwRuleResultDetailView);
                    } else {
                        UWRuleGroupView _uwRuleGroupView = uwRuleResultDetailView.getUwRuleNameView().getUwRuleGroupView();
                        PrescreenCusRulesGroupView _prescreenCusRulesGroupView = _prescreenCusRulesGroupViewMap.get(_uwRuleGroupView.getId());
                        if(_prescreenCusRulesGroupView == null){
                            _prescreenCusRulesGroupView = new PrescreenCusRulesGroupView();
                        }
                        _prescreenCusRulesGroupView.setUwRuleGroupView(_uwRuleGroupView);

                        if(!checkCountRuleNameMap.containsKey(uwRuleResultDetailView.getUwRuleNameView().getId())){
                            checkCountRuleNameMap.put(uwRuleResultDetailView.getUwRuleNameView().getId(), uwRuleResultDetailView.getCustomerInfoSimpleView().getCitizenId());
                            _prescreenCusRulesGroupView.setNumberOfRuleName(_prescreenCusRulesGroupView.getNumberOfRuleName() + 1);
                        }

                        Map<Integer, PrescreenCusRuleNameView> _prescreenCusRuleNameViewMap = _prescreenCusRulesGroupView.getPrescreenCusRuleNameViewMap();
                        if(_prescreenCusRuleNameViewMap == null)
                            _prescreenCusRuleNameViewMap = new TreeMap<Integer, PrescreenCusRuleNameView>();

                        PrescreenCusRuleNameView _prescreenCusRuleNameView = _prescreenCusRuleNameViewMap.get(uwRuleResultDetailView.getUwRuleNameView().getId());
                        if(_prescreenCusRuleNameView == null)
                            _prescreenCusRuleNameView = new PrescreenCusRuleNameView();
                        _prescreenCusRuleNameView.setUwRuleNameView(uwRuleResultDetailView.getUwRuleNameView());
                        Map<String, UWRuleResultDetailView> _uwRuleResultDetailViewMap = _prescreenCusRuleNameView.getUwRuleResultDetailViewMap();
                        if(_uwRuleResultDetailViewMap == null)
                            _uwRuleResultDetailViewMap = new TreeMap<String, UWRuleResultDetailView>();
                        _uwRuleResultDetailViewMap.put(uwRuleResultDetailView.getCustomerInfoSimpleView().getCitizenId(), uwRuleResultDetailView);
                        _prescreenCusRuleNameView.setUwRuleResultDetailViewMap(_uwRuleResultDetailViewMap);
                        _prescreenCusRuleNameViewMap.put( uwRuleResultDetailView.getUwRuleNameView().getId(), _prescreenCusRuleNameView);

                        _prescreenCusRulesGroupView.setPrescreenCusRuleNameViewMap(_prescreenCusRuleNameViewMap);
                        _prescreenCusRulesGroupViewMap.put(_uwRuleGroupView.getId(), _prescreenCusRulesGroupView);

                        //_customerUWResultDetailMap.put()
                    }
                }
                prescreenResultView.setPrescreenCusRulesGroupViewMap(_prescreenCusRulesGroupViewMap);
                prescreenResultView.setUwRuleResultSummaryView(uwRuleResultSummaryView);
                log.debug("uwRuleResultSummaryView {}", prescreenResultView.getUwRuleResultSummaryView());
                prescreenResultView.setGroupRuleResults(_groupUWResultDetailMap);
                log.debug("groupRuleResult {}", prescreenResultView.getGroupRuleResults());


                prescreenResultView.setNumberOfCusRelation(_numberOfCusRelationMap);
                prescreenResultView.setCustomerInfoSimpleViewMap(_customerInfoSimpleMap);
            }
        }

        return prescreenResultView;
    }

    public void savePrescreenResult(PrescreenResultView prescreenResultView, long workCasePrescreenId, User user){
        Prescreen prescreen = prescreenTransform.getPrescreen(prescreenResultView, getCurrentUser());
        prescreen.setModifyFlag(0);
        prescreenDAO.persist(prescreen);

        try{
            existingCreditControl.saveExistingCredit(prescreenResultView.getExistingCreditFacilityView(), getWorkCase(workCasePrescreenId));
            bankStmtControl.saveBankStmtSummary(prescreenResultView.getBankStmtSummaryView(), 0, workCasePrescreenId);

        } catch(Exception ex){
            log.error("cannot get workcase prescreen id", ex);
        }
    }

    // *** Function for NCB *** //
    public NCBOutputView getNCBFromNCB(List<CustomerInfoView> customerInfoViewList, long workCasePreScreenId) throws Exception{
        //List<NcbView> ncbViewList = new ArrayList<NcbView>();
        NCBOutputView ncbOutputView = new NCBOutputView();

        NCRSInputModel ncrsInputModel = null;
        ArrayList<NCRSModel> ncrsModelList = new ArrayList<NCRSModel>();

        NCCRSInputModel nccrsInputModel = null;
        ArrayList<NCCRSModel> nccrsModelList = new ArrayList<NCCRSModel>();

        //User user = userDAO.findById(userId);
        WorkCaseOwner workCaseOwner = workCaseOwnerDAO.getWorkCaseOwnerByStep(workCasePreScreenId, 0, RoleValue.BDM.id(), StepValue.PRESCREEN_INITIAL.value());
        User requestedUser = workCaseOwner != null ? workCaseOwner.getUser() : getCurrentUser();
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);

        for(CustomerInfoView customerItem : customerInfoViewList){
            log.info("customerItem : {}", customerItem);
            if(customerItem.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()
                    && customerItem.getNcbFlag() == 0){
                log.info("customerItem ::: NcbFlag : {}", customerItem.getNcbFlag());
                NCRSModel ncrsModel = new NCRSModel();

                /*if(customerItem.getGender() == Gender.MALE.value()){
                    ncrsModel.setTitleNameCode(TitleName.Mr);
                } else if (customerItem.getGender() == Gender.FEMALE.value()){
                    if(customerItem.getMaritalStatus() != null && customerItem.getMaritalStatus().getSpouseFlag() == 1){
                        ncrsModel.setTitleNameCode(TitleName.Mrs);
                    } else {
                        ncrsModel.setTitleNameCode(TitleName.Miss);
                    }
                } else {
                    //TODO Return with Error
                    ncrsModel.setTitleNameCode(TitleName.Mr);
                }*/


                if(customerItem.getTitleTh() != null){
                    if(customerItem.getTitleTh().getCode().equals("1")){
                        ncrsModel.setTitleNameCode(TitleName.Mr);
                    } else if(customerItem.getTitleTh().getCode().equals("2")){
                        ncrsModel.setTitleNameCode(TitleName.Mrs);
                    } else if(customerItem.getTitleTh().getCode().equals("3")){
                        ncrsModel.setTitleNameCode(TitleName.Miss);
                    } else {
                        //send other
                        if(Gender.MALE.value() == customerItem.getGender()){
                            ncrsModel.setTitleNameCode(TitleName.Mr);
                        } else if(Gender.FEMALE.value() == customerItem.getGender()){
                            if(customerItem.getMaritalStatus().getSpouseFlag() == 0){ //single
                                ncrsModel.setTitleNameCode(TitleName.Miss);
                            } else if(customerItem.getMaritalStatus().getSpouseFlag() == 1){ //married
                                ncrsModel.setTitleNameCode(TitleName.Mrs);
                            } else {
                                log.debug("Spouse != 0 or 1");
                                ncrsModel.setTitleNameCode(TitleName.Miss);
                            }
                        } else {
                            log.debug("Gender != 0 or 1");
                        }
                    }
                }




                ncrsModel.setFirstName(customerItem.getFirstNameTh());
                ncrsModel.setLastName(customerItem.getLastNameTh());
                ncrsModel.setCitizenId(customerItem.getCitizenId());

                if(customerItem.getDocumentType() != null){
                    if(customerItem.getDocumentType().getId() == 1){
                        ncrsModel.setIdType(IdType.CITIZEN);
                    } else if(customerItem.getDocumentType().getId() == 2){
                        ncrsModel.setIdType(IdType.PASSPORT);
                    }
                }

                /*if(customerItem.getCitizenCountry() != null){
                    ncrsModel.setCountryCode(customerItem.getCitizenCountry().getCode2());
                }*/
                ncrsModel.setCountryCode("TH");
                log.debug("getNCBFromNCB ::: ncrsModel : {}", ncrsModel);
                ncrsModelList.add(ncrsModel);
            } else if(customerItem.getCustomerEntity().getId() == 2
                    && customerItem.getNcbFlag() == 0) {
                NCCRSModel nccrsModel = new NCCRSModel();
                if(customerItem.getDocumentType() != null){
                    if(customerItem.getTitleTh() != null){
                        if(customerItem.getTitleTh().getCode().equals("1")){
                            nccrsModel.setRegistType(RegistType.CompanyLimited);
                        } else if(customerItem.getTitleTh().getCode().equals("2")){
                            nccrsModel.setRegistType(RegistType.PublicCompanyLimited);
                        } else if(customerItem.getTitleTh().getCode().equals("3")){
                            nccrsModel.setRegistType(RegistType.LimitedPartnership);
                        } else if(customerItem.getTitleTh().getCode().equals("4")){
                            nccrsModel.setRegistType(RegistType.RegisteredOrdinaryPartnership);
                        } else {
                            nccrsModel.setRegistType(RegistType.ForeignRegistrationIdOrOthers);
                        }
                    }
                }
                nccrsModel.setRegistId(customerItem.getRegistrationId());
                nccrsModel.setCompanyName(customerItem.getFirstNameTh());
                log.debug("getNCBFromNCB ::: nccrsModel : {}", nccrsModel);
                nccrsModelList.add(nccrsModel);
            }
        }

        log.debug("getNCBFromNCB ::: userId : {}, appNumber : {}, caNumber : {}, phoneNumber : {}", requestedUser.getId(), workCasePrescreen.getAppNumber(), workCasePrescreen.getCaNumber(), requestedUser.getPhoneNumber());
        if(ncrsModelList.size() > 0){
            ncrsInputModel = new NCRSInputModel(requestedUser.getId(), workCasePrescreen.getAppNumber(), workCasePrescreen.getCaNumber(), requestedUser.getPhoneNumber(), ncrsModelList);
        }

        if(nccrsModelList.size() > 0){
            nccrsInputModel = new NCCRSInputModel(requestedUser.getId(), workCasePrescreen.getAppNumber(), workCasePrescreen.getCaNumber(), requestedUser.getPhoneNumber(), nccrsModelList);
        }

        //*** TRY TO CHECK NCB ***
        try{
            boolean checkNCBComplete = false;
            String exceptionMessage = "";

            if(ncrsInputModel != null){
                log.info("getNCBFromNCB ::: ncrsInputModel : {}", ncrsInputModel);
                List<NCRSOutputModel> ncrsOutputModelList = ncbInterface.request(ncrsInputModel);
                log.info("getNCBFromNCB ::: ncrsOutputModel : {}", ncrsOutputModelList);
                ncbOutputView.setNcrsOutputModelList(ncrsOutputModelList);
                //**** END FUNCTION ****//
                //*** Return ncbOutputView to Controller ***//
            } else if(nccrsInputModel != null){
                log.info("getNCBFromNCB ::: nccrsInputModel : {}", nccrsInputModel);
                List<NCCRSOutputModel> nccrsOutputModelList = ncbInterface.request(nccrsInputModel);
                log.info("getNCBFromNCB ::: nccrsOutputModel : {}", nccrsOutputModelList);
                ncbOutputView.setNccrsOutputModelList(nccrsOutputModelList);
                //**** End function ****//
                //*** Return ncbOutputView to Controller
            }

        } catch (Exception ex){
            log.error("Exception : ", ex);
            throw ex;
        }

        return ncbOutputView;
    }

    public List<NcbView> getNCBData(NCBOutputView ncbOutputView, long workCasePreScreenId) throws Exception{
        List<NcbView> ncbViewList = new ArrayList<NcbView>();
        log.debug("getNCBData ::: ncbOutputView : {}", ncbOutputView);
        try{
            if(ncbOutputView.getNcrsOutputModelList() != null && ncbOutputView.getNcrsOutputModelList().size() > 0){
                log.info("getNCBData ::: ncrsOutputModelList {}", ncbOutputView.getNcrsOutputModelList());
                List<NcbView> ncbIndividualViewList = ncbBizTransform.transformIndividual(ncbOutputView.getNcrsOutputModelList());

                log.info("getNCBData ::: ncbIndividualViewList : {}", ncbIndividualViewList);
                if(ncbIndividualViewList != null){
                    for(NcbView item : ncbIndividualViewList){
                        ncbViewList.add(item);
                    }
                }

                //*** Save NCB Data to Database *** //
                for(NcbView ncbView : ncbIndividualViewList){
                    if(ncbView.getResult() == ActionResult.SUCCESS){
                        Customer customer = individualDAO.findByCitizenId(ncbView.getIdNumber(), workCasePreScreenId);
                        log.info("saving ncb (individual) data ... findByCitizenId customer : {}", customer);
                        if(customer == null ){
                            customer = new Customer();
                        }

                        log.debug("saving ncb (individual) data ... ncbView before transform : {}", ncbView);
                        NCBInfoView ncbInfoView = ncbView.getNcbInfoView();
                        List<NCBDetailView> ncbDetailViewList = ncbView.getNCBDetailViews();

                        //*** Save NCB ,, Transform NCB ***//
                        if(ncbInfoView != null){
                            ncbInfoView.setCheckingDate(new Date());
                            NCB ncb = ncbTransform.transformToModel(ncbInfoView);
                            log.debug("saving ncb (individual) data ... ncbView after transform : {}", ncb);
                            ncb.setCustomer(customer);
                            ncbDAO.persist(ncb);

                            if(ncbDetailViewList!=null && ncbDetailViewList.size()>0){
                                List<NCBDetail> ncbDetailList = ncbDetailTransform.transformToModel(ncbDetailViewList,ncb);
                                ncbDetailDAO.persist(ncbDetailList);
                            }
                        }
                    }
                }
            } else if(ncbOutputView.getNccrsOutputModelList() != null && ncbOutputView.getNccrsOutputModelList().size() > 0){
                log.info("getNCBData ::: nccrsOutputModelList : {}", ncbOutputView.getNccrsOutputModelList());
                List<NcbView> ncbJuristicViewList = ncbBizTransform.transformJuristic(ncbOutputView.getNccrsOutputModelList());
                log.info("getNCBData ::: ncbJuristicViewList : {}", ncbJuristicViewList);
                if(ncbJuristicViewList != null){
                    for(NcbView item : ncbJuristicViewList){
                        ncbViewList.add(item);
                    }
                }

                //*** Save NCB Data to Database ***//
                for(NcbView ncbView : ncbJuristicViewList){
                    Customer customer = juristicDAO.findByRegistrationId(ncbView.getIdNumber(), workCasePreScreenId);
                    log.info("saving ncb (juristic) data ... findByCitizenId customer : {}", customer);
                    if(customer == null ){
                        customer = new Customer();
                    }

                    log.debug("saving ncb (juristic) data ... ncbView before transform : {}", ncbView);
                    NCBInfoView ncbInfoView = ncbView.getNcbInfoView();
                    List<NCBDetailView> ncbDetailViewList = ncbView.getNCBDetailViews();

                    //save NCB,, transform NCB
                    if(ncbInfoView != null){
                        ncbInfoView.setCheckingDate(new Date());
                        NCB ncb = ncbTransform.transformToModel(ncbInfoView);
                        log.debug("saving ncb (juristic) data ... ncbView after transform : {}", ncb);
                        ncb.setCustomer(customer);
                        ncbDAO.persist(ncb);

                        //transform NCBDetail list
                        if(ncbDetailViewList != null && ncbDetailViewList.size() > 0){
                            List<NCBDetail> ncbDetailList = ncbDetailTransform.transformToModel(ncbDetailViewList,ncb);
                            ncbDetailDAO.persist(ncbDetailList);
                        }
                    }
                }
            }
        } catch (Exception ex){
            throw ex;
        }

        return ncbViewList;
    }

    public List<CSIResult> getCSIData(List<NcbView> ncbViewList, int customerEntityId, String userId, long workCasePreScreenId){
        //TODO Check CSI
        List<CSIResult> csiResultList = new ArrayList<CSIResult>();
        for(NcbView ncbView : ncbViewList){
            log.info("getCSI ::: accountInfoIdList : {}", ncbView.getAccountInfoIdList());
            log.info("getCSI ::: accountInfoNameList : {}", ncbView.getAccountInfoNameList());

            //need to save ncb if check NCB Success
            if(ncbView.getResult() == ActionResult.SUCCESS){
                Customer customer = new Customer();
                if(customerEntityId == 1){
                    customer = individualDAO.findByCitizenId(ncbView.getIdNumber(), workCasePreScreenId);
                }else if(customerEntityId == 2){
                    customer = juristicDAO.findByRegistrationId(ncbView.getIdNumber(), workCasePreScreenId);
                }
                log.info("findByCitizenId customer : {}", customer);
                if(customer == null ){
                    customer = new Customer();
                }

                //Add default account name to check CSI
                AccountInfoName accountInfoName = new AccountInfoName();
                accountInfoName.setNameTh(customer.getNameTh());
                accountInfoName.setNameEn(customer.getNameEn());
                accountInfoName.setSurnameTh(customer.getLastNameTh());
                accountInfoName.setSurnameEn(customer.getLastNameEn());

                if(ncbView.getAccountInfoNameList() != null) {
                    List<AccountInfoName> accountInfoNameList = ncbView.getAccountInfoNameList();
                    accountInfoNameList.add(accountInfoName);
                    ncbView.setAccountInfoNameList(accountInfoNameList);
                }else{
                    List<AccountInfoName> accountInfoNameList = new ArrayList<AccountInfoName>();
                    accountInfoNameList.add(accountInfoName);
                    ncbView.setAccountInfoNameList(accountInfoNameList);
                }

                CSIInputData csiInputData = new CSIInputData();
                csiInputData.setIdModelList(ncbView.getAccountInfoIdList());
                csiInputData.setNameModelList(ncbView.getAccountInfoNameList());

                log.info("getCSI ::: csiInputData : {}", csiInputData);
                CSIResult csiResult = new CSIResult();
                try{
                    csiResult = rlosInterface.getCSIData(userId, csiInputData);
                    csiResult.setIdNumber(ncbView.getIdNumber());
                    csiResult.setActionResult(ActionResult.SUCCESS);
                    csiResult.setResultReason("SUCCESS");
                    csiResultList.add(csiResult);
                } catch (Exception ex){
                    log.debug("getCSI ::: fail to get CSI");
                    csiResult = new CSIResult();
                    csiResult.setIdNumber(ncbView.getIdNumber());
                    csiResult.setActionResult(ActionResult.FAILED);
                    csiResult.setResultReason(ex.getMessage());
                    csiResultList.add(csiResult);
                }

                //Customer customer = individualDAO.findByCitizenId(ncbView.getIdNumber(), workCasePreScreenId);
                //Customer customer = customerDAO.findById(new Long(151));
                log.debug("getCSIData ::: csiResult : {}", csiResult);
                log.info("findByCitizenId customer : {}", customer);

                if(customer == null ){
                    customer = new Customer();
                }

                List<CustomerCSI> customerCSIs = customerCSIDAO.getCustomerCSIByCustomer(customer);
                customerCSIDAO.delete(customerCSIs);

                customer.setCustomerCSIList(Collections.<CustomerCSI>emptyList());

                List<CustomerCSI> customerCSIList = new ArrayList<CustomerCSI>();

                if(csiResult != null && csiResult.getWarningCodeFullMatched() != null && csiResult.getWarningCodeFullMatched().size() > 0){
                    for(CSIData csiData : csiResult.getWarningCodeFullMatched()){
                        log.info("getCSI ::: csiResult.getWarningCodeFullMatched : {}", csiData);
                        CustomerCSI customerCSI = new CustomerCSI();
                        customerCSI.setCustomer(customer);
                        customerCSI.setWarningCode(warningCodeDAO.findByCode(csiData.getWarningCode()));
                        customerCSI.setWarningDate(csiData.getDateWarningCode());
                        customerCSI.setMatchedType(CSIMatchedType.F.name());
                        customerCSIList.add(customerCSI);
                    }
                }

                if(csiResult != null && csiResult.getWarningCodePartialMatched() != null && csiResult.getWarningCodePartialMatched().size() > 0){
                    for(CSIData csiData : csiResult.getWarningCodePartialMatched()){
                        log.info("getCSI ::: csiResult.getWarningCodePartialMatched : {}", csiData);
                        CustomerCSI customerCSI = new CustomerCSI();
                        customerCSI.setCustomer(customer);
                        customerCSI.setWarningCode(warningCodeDAO.findByCode(csiData.getWarningCode()));
                        customerCSI.setWarningDate(csiData.getDateWarningCode());
                        customerCSI.setMatchedType(CSIMatchedType.P.name());
                        customerCSIList.add(customerCSI);
                    }
                }

                log.info("getCSI ::: customerCSIList : {}", customerCSIList);
                if(customerCSIList != null && customerCSIList.size() > 0){
                    log.info("getCSI ::: persist item");
                    customerCSIDAO.persist(customerCSIList);
                }
                log.info("getCSI ::: end...");

            }
        }

        return csiResultList;
    }

    public List<CSIResult> getCSIDataWithOutNCB(List<CustomerInfoView> customerInfoViewList, int customerEntityId, String userId, long workCasePreScreenId){
        List<CSIResult> csiResultList = new ArrayList<CSIResult>();
        long customerId = 0;
        for(CustomerInfoView customerInfoView : customerInfoViewList){
            int csiFlag = 0;
            customerId = customerInfoView.getId();
            csiFlag = customerInfoView.getCsiFlag();
            log.debug("getCSIDataWithOutNCB ::: customerId : {}", customerId);
            log.debug("getCSIDataWithOutNCB ::: csiFlag : {}", csiFlag);
            if(customerId != 0 && csiFlag == 0){
                //TODO Find AccountID to Search CSI
                List<CustomerAccount> customerAccountList = customerAccountDAO.getCustomerAccountByCustomerId(customerId);
                log.debug("getCSIDataWithOutNCB ::: customerAccountList : {}", customerAccountList);
                //TODO Find AccountName to Search CSI
                List<CustomerAccountName> customerAccountNameList = customerAccountNameDAO.getCustomerAccountNameByCustomerId(customerId);
                log.debug("getCSIDataWithOutNCB ::: customerAccountNameList : {}", customerAccountNameList);

                List<AccountInfoId> accountInfoIdList = new ArrayList<AccountInfoId>();
                for(CustomerAccount customerAccount : customerAccountList){
                    AccountInfoId accountInfoId = new AccountInfoId();
                    accountInfoId.setIdNumber(customerAccount.getIdNumber());
                    if(customerAccount.getDocumentType() != null && customerAccount.getDocumentType().getId() == 1){
                        accountInfoId.setDocumentType(com.clevel.selos.model.DocumentType.CITIZEN_ID);
                    }else if(customerAccount.getDocumentType() != null && customerAccount.getDocumentType().getId() == 2){
                        accountInfoId.setDocumentType(com.clevel.selos.model.DocumentType.PASSPORT);
                    }else if(customerAccount.getDocumentType() != null && customerAccount.getDocumentType().getId() == 3){
                        accountInfoId.setDocumentType(com.clevel.selos.model.DocumentType.CORPORATE_ID);
                    }
                    accountInfoIdList.add(accountInfoId);
                }

                List<AccountInfoName> accountInfoNameList = new ArrayList<AccountInfoName>();
                for(CustomerAccountName customerAccountName : customerAccountNameList){
                    AccountInfoName accountInfoName = new AccountInfoName();

                    accountInfoName.setNameTh(customerAccountName.getNameTh());
                    accountInfoName.setNameEn(customerAccountName.getNameEn());
                    accountInfoName.setSurnameTh(customerAccountName.getSurnameTh());
                    accountInfoName.setSurnameEn(customerAccountName.getSurnameEn());

                    accountInfoNameList.add(accountInfoName);
                }

                log.debug("getCSIDataWithOutNCB ::: accountInfoIdList : {}", accountInfoIdList);
                log.debug("getCSIDataWithOutNCB ::: accountInfoNameList : {}", accountInfoNameList);

                CSIInputData csiInputData = new CSIInputData();
                csiInputData.setIdModelList(accountInfoIdList);
                csiInputData.setNameModelList(accountInfoNameList);

                log.info("getCSI ::: csiInputData : {}", csiInputData);
                CSIResult csiResult = new CSIResult();
                String idNumber = "";
                Customer customer = new Customer();
                if(customerEntityId == 1){
                    idNumber = customerInfoView.getCitizenId();
                    customer = individualDAO.findByCitizenId(idNumber, workCasePreScreenId);
                } else if (customerEntityId == 2){
                    idNumber = customerInfoView.getRegistrationId();
                    customer = juristicDAO.findByRegistrationId(idNumber, workCasePreScreenId);
                }
                try{
                    csiResult = rlosInterface.getCSIData(userId, csiInputData);

                    csiResult.setIdNumber(idNumber);
                    csiResult.setActionResult(ActionResult.SUCCESS);
                    csiResult.setResultReason("SUCCESS");
                    csiResultList.add(csiResult);

                    List<CustomerCSI> customerCSIList = new ArrayList<CustomerCSI>();

                    if(csiResult != null && csiResult.getWarningCodeFullMatched() != null && csiResult.getWarningCodeFullMatched().size() > 0){
                        for(CSIData csiData : csiResult.getWarningCodeFullMatched()){
                            log.info("getCSI ::: csiResult.getWarningCodeFullMatched : {}", csiData);
                            CustomerCSI customerCSI = new CustomerCSI();
                            customerCSI.setCustomer(customer);
                            customerCSI.setWarningCode(warningCodeDAO.findByCode(csiData.getWarningCode()));
                            customerCSI.setWarningDate(csiData.getDateWarningCode());
                            customerCSI.setMatchedType(CSIMatchedType.F.name());
                            customerCSIList.add(customerCSI);
                        }
                    }

                    if(csiResult != null && csiResult.getWarningCodePartialMatched() != null && csiResult.getWarningCodePartialMatched().size() > 0){
                        for(CSIData csiData : csiResult.getWarningCodePartialMatched()){
                            log.info("getCSI ::: csiResult.getWarningCodePartialMatched : {}", csiData);
                            CustomerCSI customerCSI = new CustomerCSI();
                            customerCSI.setCustomer(customer);
                            customerCSI.setWarningCode(warningCodeDAO.findByCode(csiData.getWarningCode()));
                            customerCSI.setWarningDate(csiData.getDateWarningCode());
                            customerCSI.setMatchedType(CSIMatchedType.P.name());
                            customerCSIList.add(customerCSI);
                        }
                    }

                    log.info("getCSI ::: customerCSIList : {}", customerCSIList);
                    if(customerCSIList != null && customerCSIList.size() > 0){
                        log.info("getCSI ::: persist item");
                        customerCSIDAO.persist(customerCSIList);
                    }
                    log.info("getCSI ::: end...");

                } catch (Exception ex){
                    log.error("getCSI ::: error ", ex);
                    csiResult = new CSIResult();
                    csiResult.setIdNumber(idNumber);
                    csiResult.setActionResult(ActionResult.FAILED);
                    csiResult.setResultReason(ex.getMessage());
                    csiResultList.add(csiResult);
                }

            }
        }

        return csiResultList;
    }

    // *** Function for PreScreen *** //
    public int getCaseBorrowerTypeId(long workCasePreScreenId){
        int caseBorrowerTypeId = 0;
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        if(workCasePrescreen != null){
            if(workCasePrescreen.getBorrowerType() != null){
                caseBorrowerTypeId = workCasePrescreen.getBorrowerType().getId();
            }
        }
        return caseBorrowerTypeId;
    }

    // *** Function for PreScreen Initial *** //
    public PrescreenView getPreScreen(long workCasePreScreenId){
        log.info("getPreScreen ::: workCasePreScreenId : {}", workCasePreScreenId);
        PrescreenView prescreenView = null;
        Prescreen prescreen = prescreenDAO.findByWorkCasePrescreenId(workCasePreScreenId);

        if(prescreen != null){
            log.info("getPreScreen ::: prescreen : {}", prescreen);
            prescreenView = prescreenTransform.transformToView(prescreen);
        }
        log.info("getPreScreen ::: preScreenView : {}", prescreenView);
        return prescreenView;
    }

    public List<FacilityView> getPreScreenFacility(PrescreenView prescreenView){
        log.info("getPreScreenFacility ::: prescreenView : {}", prescreenView);
        List<FacilityView> facilityViewList = null;
        List<PrescreenFacility> prescreenFacilityList = prescreenFacilityDAO.findByPreScreenId(prescreenView.getId());

        if(prescreenFacilityList != null){
            facilityViewList = prescreenFacilityTransform.transformToView(prescreenFacilityList);
        }
        log.info("getPreScreenFacility ::: prescreenFacilityList : {}", prescreenFacilityList.size());
        return facilityViewList;
    }

    public List<BizInfoDetailView> getPreScreenBusinessInfo(long prescreenId){
        List<BizInfoDetailView> bizInfoDetailViewList = new ArrayList<BizInfoDetailView>();
        List<PrescreenBusiness> prescreenBusinessList = prescreenBusinessDAO.findByPreScreenId(prescreenId);
        if(prescreenBusinessList != null){
            bizInfoDetailViewList = prescreenBusinessTransform.transformToViewList(prescreenBusinessList);
        }

        return bizInfoDetailViewList;
    }

    public List<PrescreenCollateralView> getPreScreenCollateral(long prescreenId){
        List<PrescreenCollateralView> prescreenCollateralViewList = new ArrayList<PrescreenCollateralView>();
        List<PrescreenCollateral> prescreenCollateralList = prescreenCollateralDAO.findByPreScreenId(prescreenId);

        if(prescreenCollateralList != null){
            prescreenCollateralViewList = prescreenCollateralTransform.transformToViewList(prescreenCollateralList);
        }

        return prescreenCollateralViewList;
    }

    public void savePreScreenData(PrescreenView prescreenView, List<FacilityView> facilityViewList, List<BizInfoDetailView> bizInfoViewList, List<PrescreenCollateralView> prescreenCollateralViewList, WorkCasePrescreen workCasePrescreen){
        log.debug("save PreScreen ::: preScreenView : {}", prescreenView);
        Prescreen prescreen = prescreenTransform.transformToModel(prescreenView, workCasePrescreen, getCurrentUser());
        prescreenDAO.persist(prescreen);

        if(facilityViewList != null){
            //Remove all Facility before add new
            List<PrescreenFacility> prescreenFacilitieListDelete = prescreenFacilityDAO.findByPreScreen(prescreen);
            if(prescreenFacilitieListDelete != null){
                log.debug("save PreScreen data ::: delete PreScreenFacility size : {}", prescreenFacilitieListDelete.size());
                prescreenFacilityDAO.delete(prescreenFacilitieListDelete);
            }

            log.debug("save PreScreen data ::: facilityViewList : {}", facilityViewList);
            List<PrescreenFacility> prescreenFacilityList = prescreenFacilityTransform.transformToModel(facilityViewList, prescreen);
            prescreenFacilityDAO.persist(prescreenFacilityList);
        }

        if(bizInfoViewList != null){
            //Remove all Business before add new
            List<PrescreenBusiness> prescreenBusinessDelete = prescreenBusinessDAO.findByPreScreen(prescreen);
            if(prescreenBusinessDelete != null){
                log.debug("save PreScreen data ::: delete prescreenBusinessDelete size : {}", prescreenBusinessDelete.size());
                prescreenBusinessDAO.delete(prescreenBusinessDelete);
            }

            log.debug("save PreScreen data ::: bizInfoViewList size : {}", bizInfoViewList.size());
            List<PrescreenBusiness> prescreenBusinessList = prescreenBusinessTransform.transformToModelList(bizInfoViewList, prescreen);
            prescreenBusinessDAO.persist(prescreenBusinessList);
        }

        if(prescreenCollateralViewList != null){
            //Remove all Collateral before add new
            List<PrescreenCollateral> prescreenCollateralDelete = prescreenCollateralDAO.findByPreScreen(prescreen);
            if(prescreenCollateralDelete != null){
                log.debug("save PreScreen data ::: delete prescreenCollateralDelete size : {}", prescreenCollateralDelete.size());
                prescreenCollateralDAO.delete(prescreenCollateralDelete);
            }

            log.debug("save PreScreen data ::: prescreenCollateralViewList size : {}", prescreenCollateralViewList.size());
            List<PrescreenCollateral> prescreenCollateralList = prescreenCollateralTransform.transformToModelList(prescreenCollateralViewList, prescreen);
            prescreenCollateralDAO.persist(prescreenCollateralList);
        }
    }

    public void saveCustomerData(List<CustomerInfoView> customerInfoDeleteList, List<CustomerInfoView> customerInfoViewList, WorkCasePrescreen workCasePrescreen){
        //Remove all Customer before add new
        List<Customer> customerDeleteList = customerTransform.transformToModelDeleteList(customerInfoDeleteList, workCasePrescreen, null, getCurrentUser());
        /*log.info("saveCustomer ::: customerDeleteList size : {}", customerDeleteList.size());
        for(Customer customer : customerDeleteList){
            addressDAO.delete(customer.getAddressesList());

            List<CustomerAccount> customerAccountList = customerAccountDAO.getCustomerAccountByCustomer(customer);
            if(customerAccountList != null){
                customerAccountDAO.delete(customerAccountList);
            }

            List<CustomerAccountName> customerAccountNameList = customerAccountNameDAO.getCustomerAccountNameByCustomer(customer);
            if(customerAccountNameList != null){
                customerAccountNameDAO.delete(customerAccountNameList);
            }

            if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == 1) {
                //Individual
                individualDAO.delete(customer.getIndividual());
            } else {
                juristicDAO.delete(customer.getJuristic());
            }

            customerDAO.delete(customer);
            if(customer.getCustomerOblInfo() != null){
                customerOblInfoDAO.delete(customer.getCustomerOblInfo());
            }
        }*/
        customerDAO.delete(customerDeleteList);

        //Add all Customer from customer list
        for(CustomerInfoView customerInfoView : customerInfoViewList){
            Customer customer = new Customer();
            customer = customerTransform.transformToModel(customerInfoView, workCasePrescreen, null, getCurrentUser());
            customer.setIsSpouse(0);
            customer.setSpouseId(0);
            if(customer.getCustomerOblInfo() != null){
                customerOblInfoDAO.persist(customer.getCustomerOblInfo());
            }
            customerDAO.persist(customer);
            if(customer.getAddressesList() != null){
                addressDAO.persist(customer.getAddressesList());
            }
            if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == 1) {
                //Individual
                Individual individual = customer.getIndividual();
                individualDAO.persist(individual);
            } else if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == 2) {
                //Juristic
                Juristic juristic = customer.getJuristic();
                juristicDAO.persist(juristic);
            }

            if(customer.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                if(customer.getIndividual().getMaritalStatus() != null && customer.getIndividual().getMaritalStatus().getSpouseFlag() == 1){
                    Customer spouse;
                    spouse = customerTransform.transformToModel(customerInfoView.getSpouse(), workCasePrescreen, null, getCurrentUser());
                    spouse.setIsSpouse(1);
                    spouse.setSpouseId(0);
                    if(spouse.getCustomerOblInfo() != null){
                        customerOblInfoDAO.persist(spouse.getCustomerOblInfo());
                    }
                    customerDAO.persist(spouse);
                    customer.setSpouseId(spouse.getId());
                    customerDAO.persist(customer);

                    if(spouse.getAddressesList() != null){
                        addressDAO.persist(spouse.getAddressesList());
                    }

                    Individual individual = spouse.getIndividual();
                    individualDAO.persist(individual);

                }
            }
        }
    }

    public void savePreScreenInitial(PrescreenView prescreenView, List<FacilityView> facilityViewList, List<CustomerInfoView> customerInfoViewList, List<CustomerInfoView> customerInfoDeleteList, long workCasePreScreenId, int caseBorrowerTypeId, User user){
        log.debug("savePreScreenInitial ::: Starting ... ");
        log.debug("savePreScreenInitial ::: caseBorrowerType : {}", caseBorrowerTypeId);
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        CustomerEntity customerEntity = null;
        if(caseBorrowerTypeId != 0){
            customerEntity = customerEntityDAO.findById(caseBorrowerTypeId);
            log.debug("savePreScreenInitial ::: caseBorrowerEntity : {}", customerEntity);
        }
        workCasePrescreen.setBorrowerType(customerEntity);
        workCasePrescreen.setProductGroup(productGroupDAO.findById(prescreenView.getProductGroup().getId()));
        workCasePrescreenDAO.persist(workCasePrescreen);

        log.debug("savePreScreenInitial ::: saving prescreen data...");
        if(customerEntity != null && customerEntity.getId() == BorrowerType.JURISTIC.value()) {
            Date tmpDateOfRegister = calculateDateOfRegister(customerInfoViewList);
            prescreenView.setRegisterDate(tmpDateOfRegister);
        }
        savePreScreenData(prescreenView, facilityViewList, null, null, workCasePrescreen);
        log.debug("savePreScreenInitial ::: saving prescreen data success...");

        log.debug("savePreScreenInitial ::: saving customer data...");
        saveCustomerData(customerInfoDeleteList, customerInfoViewList, workCasePrescreen);
        log.debug("savePreScreenInitial ::: saving customer data success...");
    }

    private Date calculateDateOfRegister(List<CustomerInfoView> customerInfoViewList){
        log.debug("calculateDateOfRegister");
        Date dateOfRegister = null;

        for(CustomerInfoView customerInfoView : customerInfoViewList){
            if(customerInfoView.getRelation().getId() == RelationValue.BORROWER.value()
                    && customerInfoView.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()){
                log.debug("dateOfRegister : {}", dateOfRegister);
                if(dateOfRegister == null){
                    dateOfRegister = customerInfoView.getDateOfRegister();
                } else {
                    log.debug("customerInfoView.getDateOfRegister : {} ,,, dateOfRegister : {}", customerInfoView.getDateOfRegister(), dateOfRegister);
                    if(DateTimeUtil.compareDate(dateOfRegister, customerInfoView.getDateOfRegister()) < 0){
                        dateOfRegister = customerInfoView.getDateOfRegister();
                    }
                    log.debug("After compare dateOfRegister : {}", dateOfRegister);
                }
            }
        }

        return dateOfRegister;
    }

    public int getModifyValue(long workCasePreScreenId){
        Prescreen tmpPrescreen = prescreenDAO.findByWorkCasePrescreenId(workCasePreScreenId);
        int modifyValue = tmpPrescreen != null ? tmpPrescreen.getModifyFlag() : 2;

        return modifyValue;
    }

    public int checkModifyValue(PrescreenView currentPrescreenView, long workCasePrescreenId){
        int modifyCount = 0;

        Prescreen tmpPrescreen = prescreenDAO.findByWorkCasePrescreenId(workCasePrescreenId);
        if(tmpPrescreen != null){
            PrescreenView tmpPrescreenView = prescreenTransform.transformToView(tmpPrescreen);
            tmpPrescreen = null;
            if(tmpPrescreenView.getProductGroup().getId() != currentPrescreenView.getProductGroup().getId()){
                modifyCount = modifyCount + 1;
            }
            if(tmpPrescreenView.getBusinessLocation() != null && currentPrescreenView.getBusinessLocation() != null){
                if(tmpPrescreenView.getBusinessLocation().getCode() != currentPrescreenView.getBusinessLocation().getCode()){
                    modifyCount = modifyCount + 1;
                }
            }
            if(DateTimeUtil.compareDate(tmpPrescreenView.getRegisterDate(),currentPrescreenView.getRegisterDate()) != 0 ){
                modifyCount = modifyCount + 1;
            }
            if(DateTimeUtil.compareDate(tmpPrescreenView.getReferDate(),currentPrescreenView.getReferDate()) != 0){
                modifyCount = modifyCount + 1;
            }
            if(tmpPrescreenView.getReferredExperience() != null && currentPrescreenView.getReferredExperience() != null){
                if(tmpPrescreenView.getReferredExperience().getId() != currentPrescreenView.getReferredExperience().getId()){
                    modifyCount = modifyCount + 1;
                }
            }
            if(tmpPrescreenView.getTcg() != currentPrescreenView.getTcg()){
                modifyCount = modifyCount + 1;
            }
            if(tmpPrescreenView.getRefinanceIn() != currentPrescreenView.getRefinanceIn()){
                modifyCount = modifyCount + 1;
            }
            if(tmpPrescreenView.getRefinanceInBank() != null && currentPrescreenView.getRefinanceInBank() != null){
                if(tmpPrescreenView.getRefinanceInBank().getCode() != currentPrescreenView.getRefinanceInBank().getCode()){
                    modifyCount = modifyCount + 1;
                }
            }
            if(tmpPrescreenView.getRefinanceOut() != currentPrescreenView.getRefinanceOut()){
                modifyCount = modifyCount + 1;
            }
            if(tmpPrescreenView.getRefinanceOutBank() != null && currentPrescreenView.getRefinanceOutBank() != null){
                if(tmpPrescreenView.getRefinanceOutBank().getCode() != currentPrescreenView.getRefinanceOutBank().getCode()){
                    modifyCount = modifyCount + 1;
                }
            }
            if(tmpPrescreenView.getBorrowingType() != null && currentPrescreenView.getBorrowingType() != null){
                if(tmpPrescreenView.getBorrowingType().getId() != currentPrescreenView.getBorrowingType().getId()){
                    modifyCount = modifyCount + 1;
                }
            }
        }

        return modifyCount;
    }

    public boolean savePreScreen(PrescreenView prescreenView, List<FacilityView> facilityViewList, List<CustomerInfoView> customerInfoViewList, List<CustomerInfoView> customerInfoDeleteList, List<BizInfoDetailView> bizInfoViewList, List<PrescreenCollateralView> prescreenCollateralViewList, long workCasePreScreenId, int customerModifyFlag, User user){
        boolean modifyFlag = false;

        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        if(customerModifyFlag > 0){
            prescreenView.setModifyFlag(1);
            modifyFlag = true;
        }else{
            prescreenView.setModifyFlag(0);
            modifyFlag = false;
        }
        //Remove PreScreen Result Data
        log.debug("savePreScreen ::: remove UWResultSummary data...");
        uwRuleResultControl.deleteUWRuleResult(workCasePreScreenId, 0);

        log.debug("savePreScreen ::: saving prescreen data...");
        savePreScreenData(prescreenView, facilityViewList, bizInfoViewList, prescreenCollateralViewList, workCasePrescreen);
        log.debug("savePreScreen ::: saving prescreen data success...");

        log.debug("savePreScreen ::: saving customer data...");
        saveCustomerData(customerInfoDeleteList, customerInfoViewList, workCasePrescreen);
        log.debug("savePreScreen ::: saving customer data success...");

        return modifyFlag;
    }

    public void savePreScreenChecker(List<CustomerInfoView> customerInfoViews, List<NcbView> ncbViewList, int customerEntityId, long workCasePreScreenId){
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        List<Customer> customerList = customerTransform.transformToModelList(customerInfoViews, workCasePrescreen, null, getCurrentUser());

        log.info("saveCustomer ::: customerList size : {}", customerList.size());
        for(Customer customer : customerList){
            customerDAO.persist(customer);
            if(customer.getAddressesList() != null){
                //addressDAO.persist(customer.getAddressesList());
            }
            if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()) {
                //Individual
                Individual individual = customer.getIndividual();
                individualDAO.persist(individual);
            } else if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == BorrowerType.JURISTIC.value()) {
                //Juristic
                Juristic juristic = customer.getJuristic();
                juristicDAO.persist(juristic);
            }
        }

        log.debug("savePreScreenChecker ::: ncbViewList : {}", ncbViewList);
        for(NcbView ncbView : ncbViewList){
            if(ncbView.getResult() == ActionResult.SUCCESS){
                Customer customer = null;

                if(customerEntityId == 1){
                    customer = individualDAO.findByCitizenId(ncbView.getIdNumber(), workCasePreScreenId);
                    log.debug("savePreScreenChecker ::: findCustomerIndividual : {}", customer);
                }else if(customerEntityId == 2){
                    customer = juristicDAO.findByRegistrationId(ncbView.getIdNumber(), workCasePreScreenId);
                    log.debug("savePreScreenChecker ::: findCustomerJuristic : {}", customer);
                }

                if(customer != null){
                    log.debug("savePreScreenChecker ::: ncbView.getAccountInfoIdList() : {}", ncbView.getAccountInfoIdList());

                    List<CustomerAccount> customerAccountList = customerAccountDAO.getCustomerAccountByCustomer(customer);
                    customerAccountDAO.delete(customerAccountList);

                    customer.setCustomerAccountList(Collections.<CustomerAccount>emptyList());

                    if(ncbView.getAccountInfoIdList() != null && ncbView.getAccountInfoIdList().size() > 0){
                        for(AccountInfoId accountInfoId : ncbView.getAccountInfoIdList()){
                            CustomerAccount customerAccount = new CustomerAccount();
                            DocumentType documentType = null;
                            if(accountInfoId.getDocumentType() == com.clevel.selos.model.DocumentType.CITIZEN_ID){
                                documentType = documentTypeDAO.findById(1);
                            } else if(accountInfoId.getDocumentType() == com.clevel.selos.model.DocumentType.PASSPORT){
                                documentType = documentTypeDAO.findById(2);
                            } else if(accountInfoId.getDocumentType() == com.clevel.selos.model.DocumentType.CORPORATE_ID){
                                documentType = documentTypeDAO.findById(3);
                            }
                            customerAccount.setCustomer(customer);
                            customerAccount.setDocumentType(documentType);
                            customerAccount.setIdNumber(accountInfoId.getIdNumber());
                            customerAccountDAO.persist(customerAccount);
                        }
                    }

                    log.debug("savePreScreenChecker ::: ncbView.getAccountInfoNameList() : {}", ncbView.getAccountInfoNameList());

                    List<CustomerAccountName> customerAccountNameList = customerAccountNameDAO.getCustomerAccountNameByCustomer(customer);
                    customerAccountNameDAO.delete(customerAccountNameList);

                    customer.setCustomerAccountNameList(Collections.<CustomerAccountName>emptyList());

                    CustomerAccountName customerAccountName = new CustomerAccountName();
                    customerAccountName.setCustomer(customer);
                    customerAccountName.setNameTh(customer.getNameTh());
                    customerAccountName.setSurnameTh(customer.getLastNameTh());
                    customerAccountName.setNameEn(customer.getNameEn());
                    customerAccountName.setSurnameEn(customer.getLastNameEn());
                    customerAccountNameDAO.persist(customerAccountName);

                    if(ncbView.getAccountInfoNameList() != null && ncbView.getAccountInfoNameList().size() > 0){
                        for(AccountInfoName accountInfoName : ncbView.getAccountInfoNameList()){
                            customerAccountName = new CustomerAccountName();
                            customerAccountName.setCustomer(customer);
                            customerAccountName.setNameTh(accountInfoName.getNameTh());
                            customerAccountName.setSurnameTh(accountInfoName.getSurnameTh());
                            customerAccountName.setNameEn(accountInfoName.getNameEn());
                            customerAccountName.setSurnameEn(accountInfoName.getSurnameEn());
                            customerAccountNameDAO.persist(customerAccountName);
                        }
                    }

                }
            }
        }
    }

    public void savePreScreenCheckerOnlyCSI(List<CustomerInfoView> customerInfoViews, int customerEntityId, long workCasePreScreenId){
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        List<Customer> customerList = customerTransform.transformToModelList(customerInfoViews, workCasePrescreen, null, getCurrentUser());

        log.info("saveCustomer ::: customerList : {}", customerList);
        for(Customer customer : customerList){
            customerDAO.persist(customer);
            if(customer.getAddressesList() != null){
                //addressDAO.persist(customer.getAddressesList());
            }
            if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == 1) {
                //Individual
                Individual individual = customer.getIndividual();
                individualDAO.persist(individual);
            } else if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == 2) {
                //Juristic
                Juristic juristic = customer.getJuristic();
                juristicDAO.persist(juristic);
            }
        }
    }

    public void duplicateData(String queueName, String wobNumber, long actionCode, long workCasePreScreenId, int reasonId, String remark) throws Exception{
        stpExecutor.duplicateData(workCasePreScreenId);
        closeSale(queueName, wobNumber, actionCode, workCasePreScreenId, getReasonDescription(reasonId), remark);
    }

    // *** Function for BPM *** //
    public void assignChecker(String queueName, String wobNumber, long actionCode, long workCasePreScreenId, String checkerId, String remark) throws Exception {
        Prescreen prescreen = prescreenDAO.findByWorkCasePrescreenId(workCasePreScreenId);
        if(!Util.isNull(prescreen)){
            if(!Util.isNull(prescreen.getProductGroup())) {
                bpmExecutor.assignChecker(queueName, wobNumber, actionCode, prescreen.getProductGroup().getName(), checkerId, remark);
            }else{
                throw new Exception("An exception occurred, Please save 'PreScreen screen' before assign to checker.");
            }
        }else{
            throw new Exception("An exception occurred, Please save 'PreScreen screen' before assign to checker.");
        }
    }

    public void cancelCase(String queueName, String wobNumber, long actionCode, String reason, String remark) throws Exception {
        bpmExecutor.cancelCase(queueName, wobNumber, actionCode, reason, remark);
    }

    public void closeSale(String queueName, String wobNumber, long actionCode, long workCasePreScreenId, String reason, String remark) throws Exception {
        bpmExecutor.closeSales(workCasePreScreenId, queueName, wobNumber, actionCode);
    }

    public void returnBDM(long workCasePreScreenId, String queueName, long actionCode) throws Exception {
        bpmExecutor.returnMaker(workCasePreScreenId, queueName, actionCode);
    }

    public void submitBDM(long workCasePreScreenId, String queueName, long actionCode) throws Exception {
        bpmExecutor.submitMaker(workCasePreScreenId, queueName, actionCode);
    }

    public String getReasonDescription(int reasonId){
        String reasonDescription = "";
        if(!Util.isZero(reasonId)){
            try {
                Reason reason = reasonDAO.findById(reasonId);
                if (!Util.isNull(reason)) {
                    reasonDescription = reason.getCode() != null ? reason.getCode() : "";
                    reasonDescription = reason.getDescription() != null ? reasonDescription.concat(" - ").concat(reason.getDescription()) : reasonDescription;
                }
            } catch (Exception ex) {
                log.error("Exception while get reason description : ", ex);
            }
        }

        return reasonDescription;
    }

    //*** Function for PreScreen Checker ***//
    public String getBDMMakerName(long workCasePreScreenId){
        String bdmMakerName = "";
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        if(workCasePrescreen != null){
            bdmMakerName = ((User)workCasePrescreen.getCreateBy()).getUserName();
        }

        return bdmMakerName;
    }

    public List<CustomerInfoView> getCustomerListByWorkCasePreScreenId(long workCasePreScreenId){
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
        List<Customer> customerList = customerDAO.findCustomerByWorkCasePreScreenId(workCasePreScreenId);

        CustomerInfoView customerInfoView;
        CustomerInfoView spouseInfoView;
        for(Customer customer : customerList){
            customerInfoView = customerTransform.transformToView(customer);
            customerInfoView.setListIndex(customerInfoViewList.size());
            if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                if(Long.toString(customer.getSpouseId()) != null && customer.getSpouseId() != 0 ){
                    Customer spouse = new Customer();
                    spouse = customerDAO.findSpouseById(customer.getSpouseId());
                    if(spouse != null){
                        spouseInfoView = new CustomerInfoView();
                        spouseInfoView = customerTransform.transformToView(spouse);
                        spouseInfoView.setListIndex(customerInfoViewList.size());
                        spouseInfoView.setIsSpouse(1);
                        customerInfoView.setSpouse(spouseInfoView);
                    }
                }
            }

            customerInfoViewList.add(customerInfoView);
        }

        log.info("getBorrowerListByWorkCaseId ::: customerList : {}", customerList);
        //customerInfoViewList = customerTransform.transformToViewList(customerList);
        log.info("getBorrowerListByWorkCaseId ::: customerInfoViewList : {}", customerInfoViewList);

        return customerInfoViewList;
    }

    public List<CustomerInfoView> getBorrowerViewListByCustomerViewList(List<CustomerInfoView> customerInfoViews){
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
        customerInfoViewList = customerTransform.transformToBorrowerViewList(customerInfoViews);
        return customerInfoViewList;
    }

    public List<CustomerInfoView> getGuarantorViewListByCustomerViewList(List<CustomerInfoView> customerInfoViews){
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
        customerInfoViewList = customerTransform.transformToGuarantorViewList(customerInfoViews);
        return customerInfoViewList;
    }

    public List<CustomerInfoView> getRelatedViewListByCustomerViewList(List<CustomerInfoView> customerInfoViews){
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
        customerInfoViewList = customerTransform.transformToRelatedViewList(customerInfoViews);
        return customerInfoViewList;
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


    // *** Function for Drop Down *** //
    public List<Relation> getRelationByStepId(long stepId, int customerEntityId, int borrowerTypeId, int spouse){
        log.debug("getRelationByStepId : stepId : {}, customerEntityId : {}, borrowerTypeId : {}, spouse : {}", stepId, customerEntityId, borrowerTypeId, spouse);
        List<Relation> relationList = new ArrayList<Relation>();

        if(stepId == StepValue.PRESCREEN_INITIAL.value()){
            relationList = relationDAO.getRelationOnlyBorrower();
        } else if(stepId == StepValue.PRESCREEN_MAKER.value()){
            relationList = relationCustomerDAO.getListRelationWithOutBorrower(customerEntityId, borrowerTypeId, spouse);
        }

        return relationList;
    }

    public List<Relation> getRelationByStepAndBorrowerRelationId(long stepId, int customerEntityId, int borrowerTypeId, int borrowerPiority){
        log.debug("getRelationByStepId : stepId : {}, customerEntityId : {}, borrowerTypeId : {}, borrowerPiority : {}", stepId, customerEntityId, borrowerTypeId, borrowerPiority);
        List<Relation> relationList = new ArrayList<Relation>();

        if(stepId == StepValue.PRESCREEN_INITIAL.value()){
            relationList = relationDAO.getRelationOnlyBorrower();
        } else if(stepId == StepValue.PRESCREEN_MAKER.value()){
            List<Relation> tmpRelationList = relationCustomerDAO.getListRelationForSpouse(customerEntityId, borrowerTypeId, borrowerPiority);
            //Remove Borrower for Spouse
            for(Relation relation : tmpRelationList){
                if(relation.getId() != RelationValue.BORROWER.value()){
                    relationList.add(relation);
                }
            }
        }

        return relationList;
    }

    public void updateBorrowerProductGroupForBPM(List<CustomerInfoView> borrowerInfoViewList, String productGroupName, String queueName, long workCasePreScreenId) throws Exception{
        String borrowerName = "";
        if(borrowerInfoViewList != null && borrowerInfoViewList.size() > 0){
            CustomerInfoView customerInfoView = borrowerInfoViewList.get(0);
            borrowerName = customerInfoView.getFirstNameTh();
            if(!Util.isNull(customerInfoView.getLastNameTh()) && !Util.isEmpty(customerInfoView.getLastNameTh())){
                borrowerName = borrowerName + " " + customerInfoView.getLastNameTh();
            }
        }

        WorkCasePrescreen workCasePreScreen = workCasePrescreenDAO.getWorkCasePreScreenById(workCasePreScreenId);
        if(workCasePreScreen != null){
            bpmExecutor.updateBorrowerProductGroup(borrowerName, productGroupName, queueName, workCasePreScreen.getWobNumber());
        }else{
            throw new Exception("Work item data could not found.");
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void updateCSIData(long workCasePreScreenId) throws Exception{
        List<Customer> customerList = customerDAO.findByWorkCasePreScreenId(workCasePreScreenId);
        //List<CustomerInfoView> customerInfoViewList = customerTransform.transformToViewList(customers);
        List<CSIResult> csiResultList = new ArrayList<CSIResult>();
        long customerId = 0;
        //for(CustomerInfoView customerInfoView : customerInfoViewList){
        for(Customer customer : customerList){
            customerId = customer.getId();
            log.debug("updateCSIDataFullApp ::: customerId : {}", customerId);
            if(customerId != 0){
                List<CustomerAccount> customerAccountList = customerAccountDAO.getCustomerAccountByCustomerId(customerId);
                log.debug("updateCSIDataFullApp ::: customerAccountList : {}", customerAccountList);
                List<CustomerAccountName> customerAccountNameList = customerAccountNameDAO.getCustomerAccountNameByCustomerId(customerId);
                log.debug("updateCSIDataFullApp ::: customerAccountNameList : {}", customerAccountNameList);

                List<AccountInfoId> accountInfoIdList = new ArrayList<AccountInfoId>();
                //-- if customer never check NCB ( eg. guarantor and related person ) manual add customer account name and customer account--//
                if(customerAccountList != null && customerAccountList.size() > 0) {
                    for (CustomerAccount customerAccount : customerAccountList) {
                        AccountInfoId accountInfoId = new AccountInfoId();
                        accountInfoId.setIdNumber(customerAccount.getIdNumber());
                        if (customerAccount.getDocumentType() != null && customerAccount.getDocumentType().getId() == 1) {
                            accountInfoId.setDocumentType(com.clevel.selos.model.DocumentType.CITIZEN_ID);
                        } else if (customerAccount.getDocumentType() != null && customerAccount.getDocumentType().getId() == 2) {
                            accountInfoId.setDocumentType(com.clevel.selos.model.DocumentType.PASSPORT);
                        } else if (customerAccount.getDocumentType() != null && customerAccount.getDocumentType().getId() == 3) {
                            accountInfoId.setDocumentType(com.clevel.selos.model.DocumentType.CORPORATE_ID);
                        }
                        accountInfoIdList.add(accountInfoId);
                    }
                } else {
                    AccountInfoId accountInfoId = new AccountInfoId();
                    String idNumber = "";

                    if(customer.getCustomerEntity().getId() == 1)
                        idNumber = customer.getIndividual().getCitizenId();
                    else if(customer.getCustomerEntity().getId() == 2)
                        idNumber = customer.getJuristic().getRegistrationId();
                    accountInfoId.setIdNumber(idNumber);

                    if(customer.getDocumentType() != null && customer.getDocumentType().getId() == 1)
                        accountInfoId.setDocumentType(com.clevel.selos.model.DocumentType.CITIZEN_ID);
                    else if(customer.getDocumentType() != null && customer.getDocumentType().getId() == 2)
                        accountInfoId.setDocumentType(com.clevel.selos.model.DocumentType.PASSPORT);
                    else if (customer.getDocumentType() != null && customer.getDocumentType().getId() == 3)
                        accountInfoId.setDocumentType(com.clevel.selos.model.DocumentType.CORPORATE_ID);

                    accountInfoIdList.add(accountInfoId);

                }

                List<AccountInfoName> accountInfoNameList = new ArrayList<AccountInfoName>();
                //-- if customer never check NCB ( eg. guarantor and related person ) manual add customer account name and customer account--//
                //Add default account name to check CSI
                AccountInfoName accountInfoName = new AccountInfoName();
                accountInfoName.setNameTh(customer.getNameTh());
                accountInfoName.setNameEn(customer.getNameEn());
                accountInfoName.setSurnameTh(customer.getLastNameTh());
                accountInfoName.setSurnameEn(customer.getLastNameEn());
                accountInfoNameList.add(accountInfoName);

                if(customerAccountNameList != null && customerAccountNameList.size() > 0){
                    for(CustomerAccountName customerAccountName : customerAccountNameList){
                        accountInfoName = new AccountInfoName();

                        accountInfoName.setNameTh(customerAccountName.getNameTh());
                        accountInfoName.setNameEn(customerAccountName.getNameEn());
                        accountInfoName.setSurnameTh(customerAccountName.getSurnameTh());
                        accountInfoName.setSurnameEn(customerAccountName.getSurnameEn());

                        accountInfoNameList.add(accountInfoName);
                    }
                }


                log.debug("updateCSIDataFullApp ::: accountInfoIdList : {}", accountInfoIdList);
                log.debug("updateCSIDataFullApp ::: accountInfoNameList : {}", accountInfoNameList);

                CSIInputData csiInputData = new CSIInputData();
                csiInputData.setIdModelList(accountInfoIdList);
                csiInputData.setNameModelList(accountInfoNameList);

                log.info("getCSI ::: csiInputData : {}", csiInputData);
                CSIResult csiResult = new CSIResult();
                String idNumber = "";
                if(customer.getCustomerEntity().getId() == 1){
                    idNumber = customer.getIndividual().getCitizenId();
                }else if(customer.getCustomerEntity().getId() == 2){
                    idNumber = customer.getJuristic().getRegistrationId();
                }
                /*Customer customer = new Customer();
                if(customerInfoView.getCustomerEntity().getId() == 1){
                    idNumber = customerInfoView.getCitizenId();
                    customer = individualDAO.findByCitizenId(idNumber, workCasePreScreenId);
                } else if (customerInfoView.getCustomerEntity().getId() == 2){
                    idNumber = customerInfoView.getRegistrationId();
                    customer = juristicDAO.findByRegistrationId(idNumber, workCasePreScreenId);
                }*/
                try{
                    User user = getCurrentUser();
                    csiResult = rlosInterface.getCSIData(user.getId(), csiInputData);

                    csiResult.setIdNumber(idNumber);
                    csiResult.setActionResult(ActionResult.SUCCESS);
                    csiResult.setResultReason("SUCCESS");
                    csiResultList.add(csiResult);

                    List<CustomerCSI> customerCSIList = new ArrayList<CustomerCSI>();
                    List<CustomerCSI> customerCSIListDel = customerCSIDAO.findCustomerCSIByCustomerId(customerId);
                    customerCSIDAO.delete(customerCSIListDel);

                    customer.setCustomerCSIList(Collections.<CustomerCSI>emptyList());

                    if(csiResult != null && csiResult.getWarningCodeFullMatched() != null && csiResult.getWarningCodeFullMatched().size() > 0){
                        for(CSIData csiData : csiResult.getWarningCodeFullMatched()){
                            log.info("getCSI ::: csiResult.getWarningCodeFullMatched : {}", csiData);
                            CustomerCSI customerCSI = new CustomerCSI();
                            customerCSI.setCustomer(customer);
                            customerCSI.setWarningCode(warningCodeDAO.findByCode(csiData.getWarningCode()));
                            customerCSI.setWarningDate(csiData.getDateWarningCode());
                            customerCSI.setMatchedType(CSIMatchedType.F.name());
                            customerCSIList.add(customerCSI);
                        }
                    }

                    if(csiResult != null && csiResult.getWarningCodePartialMatched() != null && csiResult.getWarningCodePartialMatched().size() > 0){
                        for(CSIData csiData : csiResult.getWarningCodePartialMatched()){
                            log.info("getCSI ::: csiResult.getWarningCodePartialMatched : {}", csiData);
                            CustomerCSI customerCSI = new CustomerCSI();
                            customerCSI.setCustomer(customer);
                            customerCSI.setWarningCode(warningCodeDAO.findByCode(csiData.getWarningCode()));
                            customerCSI.setWarningDate(csiData.getDateWarningCode());
                            customerCSI.setMatchedType(CSIMatchedType.P.name());
                            customerCSIList.add(customerCSI);
                        }
                    }

                    log.info("getCSI ::: customerCSIList : {}", customerCSIList);
                    if(customerCSIList != null && customerCSIList.size() > 0){
                        log.info("getCSI ::: persist item");
                        customerCSIDAO.persist(customerCSIList);
                    }
                    log.info("getCSI ::: end...");

                } catch (Exception ex){
                    log.error("getCSI ::: error ", ex);
                    throw ex;
                }
            }
        }
    }

    public void updateTimeOfCheckPreScreen(long workCasePreScreenId, long stepId){
        try{
            WorkCaseOwner workCaseOwner = workCaseOwnerDAO.getWorkCaseOwnerPreScreen(workCasePreScreenId, getCurrentUser().getRole().getId(), getCurrentUserID(), stepId);
            log.debug("Update time of criteria checked [workCaseOwner] : {}", workCaseOwner);
            if(!Util.isNull(workCaseOwner)) {
                int timesOfCriteriaChecked = workCaseOwner.getTimesOfCriteriaChecked();
                timesOfCriteriaChecked = timesOfCriteriaChecked + 1;
                workCaseOwner.setTimesOfCriteriaChecked(timesOfCriteriaChecked);
                log.debug("Update time of criteria checked [timeOfCriteriaCheck] : {}", timesOfCriteriaChecked);
                workCaseOwnerDAO.persist(workCaseOwner);
            }
        }catch(Exception ex){
            log.error("Exception while update time of check criteria.", ex);
        }
    }

    public int getTimesOfPreScreenCheck(long workCasePreScreenId, long stepId){
        int timesOfPreScreenCheck = 0;
        try{
            WorkCaseOwner workCaseOwner = workCaseOwnerDAO.getWorkCaseOwnerPreScreen(workCasePreScreenId, getCurrentUser().getRole().getId(), getCurrentUserID(), stepId);
            if(!Util.isNull(workCaseOwner)){
                log.debug("getTimesOfPreScreenCheck ::: workCaseOwner : {}", workCaseOwner);
                timesOfPreScreenCheck = workCaseOwner.getTimesOfCriteriaChecked();
            }
            log.debug("getTimesOfPreScreenCheck ::: timesOfCriteriaCheck : {}", timesOfPreScreenCheck);
        }catch(Exception ex){
            log.error("Exception while get time of check criteria : ", ex);
        }

        return timesOfPreScreenCheck;
    }
}
