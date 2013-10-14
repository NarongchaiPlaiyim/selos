package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.BPMInterface;
import com.clevel.selos.integration.BRMSInterface;
import com.clevel.selos.integration.RLOSInterface;
import com.clevel.selos.integration.RMInterface;
import com.clevel.selos.integration.brms.model.request.PreScreenRequest;
import com.clevel.selos.integration.brms.model.response.PreScreenResponse;
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
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.DocumentType;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.*;
import com.clevel.selos.transform.business.CustomerBizTransform;
import com.clevel.selos.transform.business.NCBBizTransform;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    IndividualDAO individualDAO;
    @Inject
    JuristicDAO juristicDAO;
    @Inject
    UserDAO userDAO;
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
    RMInterface rmInterface;
    @Inject
    BRMSInterface brmsInterface;
    @Inject
    BPMInterface bpmInterface;
    @Inject
    RLOSInterface rlosInterface;

    /*@Inject
    NCBInterface ncbInterface;  */

    @Inject
    NCBInterfaceImpl ncbInterface;

    @Inject
    public PrescreenBusinessControl(){

    }

    //** function for integration **//

    // *** Function for RM *** //
    public CustomerInfoResultView getCustomerInfoFromRM(CustomerInfoView customerInfoView, User user){
        CustomerInfoResultView customerInfoResultSearch = new CustomerInfoResultView();
        log.info("getCustomerInfoFromRM ::: customerInfoView : {}", customerInfoView);

        String userId = user.getId();
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
            IndividualResult individualResult = rmInterface.getIndividualInfo(userId, customerInfoView.getSearchId(), documentType, searcyBy);
            log.info("getCustomerInfoFromRM ::: individualResult : {}", individualResult);
            customerInfoResultSearch = customerBizTransform.tranformIndividual(individualResult);
        } else if(customerInfoView.getCustomerEntity().getId() == 2){
            CorporateResult corporateResult = rmInterface.getCorporateInfo(userId, customerInfoView.getSearchId(), documentType, searcyBy);
            log.info("getCustomerInfoFromRM ::: corporateResult : {}", corporateResult);
            customerInfoResultSearch = customerBizTransform.tranformJuristic(corporateResult);
        }

        log.info("getCustomerInfoFromRM ::: success!!");
        log.info("getCustomerInfoFromRM ::: customerInfoSearch : {}", customerInfoResultSearch);
        return customerInfoResultSearch;
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

    // *** Function for NCB *** //
    public List<NcbView> getNCBFromNCB(List<CustomerInfoView> customerInfoViewList, String userId, long workCasePreScreenId) throws Exception{
        List<NcbView> ncbViewList = new ArrayList<NcbView>();
        NCRSInputModel ncrsInputModel = null;
        ArrayList<NCRSModel> ncrsModelList = new ArrayList<NCRSModel>();
        NCCRSInputModel nccrsInputModel = null;
        ArrayList<NCCRSModel> nccrsModelList = new ArrayList<NCCRSModel>();

        User user = userDAO.findById(userId);
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);

        for(CustomerInfoView customerItem : customerInfoViewList){
            log.info("customerItem : {}", customerItem);
            if(customerItem.getCustomerEntity().getId() == 1 && !customerItem.isNcbFlag()){
                log.info("customerItem ::: NcbFlag : {}", customerItem.isNcbFlag());
                NCRSModel ncrsModel = new NCRSModel();

                if(customerItem.getTitleTh() != null){
                    if(customerItem.getTitleTh().getCode().equals("1")){
                        ncrsModel.setTitleNameCode(TitleName.Mr);
                    } else if(customerItem.getTitleTh().getCode().equals("2")){
                        ncrsModel.setTitleNameCode(TitleName.Mrs);
                    } else if(customerItem.getTitleTh().getCode().equals("3")){
                        ncrsModel.setTitleNameCode(TitleName.Miss);
                    } else {
                        //send other
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
            } else if(customerItem.getCustomerEntity().getId() == 2 && !customerItem.isNcbFlag()) {
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
                        }
                    }
                }
                nccrsModel.setRegistId(customerItem.getRegistrationId());
                nccrsModel.setCompanyName(customerItem.getFirstNameTh());
                log.debug("getNCBFromNCB ::: nccrsModel : {}", nccrsModel);
                nccrsModelList.add(nccrsModel);
            }
        }
        log.debug("getNCBFromNCB ::: userId : {}, appNumber : {}, caNumber : {}, phoneNumber : {}", user.getId(), workCasePrescreen.getAppNumber(), workCasePrescreen.getCaNumber(), user.getPhoneNumber());
        if(ncrsModelList.size() > 0){
            ncrsInputModel = new NCRSInputModel(user.getId(), workCasePrescreen.getAppNumber(), workCasePrescreen.getCaNumber(), user.getPhoneNumber(), ncrsModelList);
        }

        if(nccrsModelList.size() > 0){
            nccrsInputModel = new NCCRSInputModel(user.getId(), workCasePrescreen.getAppNumber(), workCasePrescreen.getCaNumber(), user.getPhoneNumber(), nccrsModelList);
        }

        //Get NCB for Individual
            //ncbInterface.request();
        //Get NCB for Juristic
        try{
            boolean checkNCBComplete = false;
            String exceptionMessage = "";
            if(ncrsInputModel != null){
                log.info("getNCBFromNCB ::: ncrsInputModel : {}", ncrsInputModel);
                List<NCRSOutputModel> ncrsOutputModelList = ncbInterface.request(ncrsInputModel);
                log.info("getNCBFromNCB ::: ncrsOutputModelList {}", ncrsOutputModelList);
                List<NcbView> ncbIndividualViewList = ncbBizTransform.transformIndividual(ncrsOutputModelList);
                if(ncbIndividualViewList != null){
                    for(NcbView item : ncbIndividualViewList){
                        ncbViewList.add(item);
                    }
                }

                //TODO Check CSI
                for(NcbView ncbView : ncbIndividualViewList){
                    log.info("getCSI ::: accountInfoIdList : {}", ncbView.getAccountInfoIdList());
                    log.info("getCSI ::: accountInfoNameList : {}", ncbView.getAccountInfoNameList());

                    if(ncbView.getResult() == ActionResult.SUCCEED){
                        CSIInputData csiInputData = new CSIInputData();
                        csiInputData.setIdModelList(ncbView.getAccountInfoIdList());
                        csiInputData.setNameModelList(ncbView.getAccountInfoNameList());

                        log.info("getCSI ::: csiInputData : {}", csiInputData);
                        CSIResult csiResult = rlosInterface.getCSIData(userId, csiInputData);
                        log.info("getCSI ::: csiResult.FullMatched : {}", csiResult.getWarningCodeFullMatched());
                        log.info("getCSI ::: csiResult.PartialMatched : {}", csiResult.getWarningCodePartialMatched());

                        Individual individual = individualDAO.findByCitizenId(ncbView.getIdNumber());
                        Customer customer = individual.getCustomer();

                        List<CustomerCSI> customerCSIList = new ArrayList<CustomerCSI>();

                        for(CSIData csiData : csiResult.getWarningCodeFullMatched()){
                            CustomerCSI customerCSI = new CustomerCSI();
                            customerCSI.setCustomer(customer);
                            customerCSI.setWarningCode(warningCodeDAO.findByCode(csiData.getWarningCode()));
                            customerCSI.setWarningDate(csiData.getDateWarningCode());
                            customerCSI.setMatchedType("F");
                            customerCSIList.add(customerCSI);
                        }

                        for(CSIData csiData : csiResult.getWarningCodePartialMatched()){
                            CustomerCSI customerCSI = new CustomerCSI();
                            customerCSI.setCustomer(customer);
                            customerCSI.setWarningCode(warningCodeDAO.findByCode(csiData.getWarningCode()));
                            customerCSI.setWarningDate(csiData.getDateWarningCode());
                            customerCSI.setMatchedType("P");
                            customerCSIList.add(customerCSI);
                        }
                        customerCSIDAO.persist(customerCSIList);
                    }
                }
            }

            if(nccrsInputModel != null){
                log.info("getNCBFromNCB ::: nccrsInputModel : {}", nccrsInputModel);
                List<NCCRSOutputModel> nccrsOutputModelList = ncbInterface.request(nccrsInputModel);
                log.info("getNCBFromNCB ::: nccrsOutputModelList {}", nccrsOutputModelList);
                List<NcbView> ncbJuristicViewList = ncbBizTransform.transformJuristic(nccrsOutputModelList);
                if(ncbJuristicViewList != null){
                    for(NcbView item : ncbJuristicViewList){
                        ncbViewList.add(item);
                    }
                }

                //TODO Check CSI
                for(NcbView ncbView : ncbJuristicViewList){
                    log.info("getCSI ::: accountInfoIdList : {}", ncbView.getAccountInfoIdList());
                    log.info("getCSI ::: accountInfoNameList : {}", ncbView.getAccountInfoNameList());

                    if(ncbView.getResult() == ActionResult.SUCCEED){
                        CSIInputData csiInputData = new CSIInputData();
                        csiInputData.setIdModelList(ncbView.getAccountInfoIdList());
                        csiInputData.setNameModelList(ncbView.getAccountInfoNameList());

                        log.info("getCSI ::: csiInputData : {}", csiInputData);
                        CSIResult csiResult = rlosInterface.getCSIData(userId, csiInputData);
                        log.info("getCSI ::: csiResult.FullMatched : {}", csiResult.getWarningCodeFullMatched());
                        log.info("getCSI ::: csiResult.PartialMatched : {}", csiResult.getWarningCodePartialMatched());

                        Juristic juristic = juristicDAO.findByRegisterId(ncbView.getIdNumber());
                        Customer customer = juristic.getCustomer();
                        List<CustomerCSI> customerCSIList = new ArrayList<CustomerCSI>();

                        for(CSIData csiData : csiResult.getWarningCodeFullMatched()){
                            CustomerCSI customerCSI = new CustomerCSI();
                            customerCSI.setCustomer(customer);
                            customerCSI.setWarningCode(warningCodeDAO.findByCode(csiData.getWarningCode()));
                            customerCSI.setWarningDate(csiData.getDateWarningCode());
                            customerCSI.setMatchedType("F");
                            customerCSIList.add(customerCSI);
                        }

                        for(CSIData csiData : csiResult.getWarningCodePartialMatched()){
                            CustomerCSI customerCSI = new CustomerCSI();
                            customerCSI.setCustomer(customer);
                            customerCSI.setWarningCode(warningCodeDAO.findByCode(csiData.getWarningCode()));
                            customerCSI.setWarningDate(csiData.getDateWarningCode());
                            customerCSI.setMatchedType("P");
                            customerCSIList.add(customerCSI);
                        }
                        customerCSIDAO.persist(customerCSIList);
                    }
                }
            }
        } catch (Exception ex){
            throw ex;
        }

        return ncbViewList;
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

    public List<BizInfoDetailView> getBusinessInfo(long workCasePreScreenId){
        List<BizInfoDetailView> bizInfoViewList = null;
        List<BizInfoDetail> bizInfoList = bizInfoDAO.findByWorkCasePreScreenId(workCasePreScreenId);

        if(bizInfoList != null){
            bizInfoViewList = bizInfoTransform.transformToPreScreenView(bizInfoList);
        }

        return bizInfoViewList;
    }

    public void savePreScreenInitial(PrescreenView prescreenView, List<FacilityView> facilityViewList, List<CustomerInfoView> customerInfoViewList, long workCasePreScreenId, User user){
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);

        Prescreen prescreen = prescreenTransform.transformToModel(prescreenView, workCasePrescreen, user);
        prescreenDAO.persist(prescreen);

        //Remove all Facility before add new
        List<PrescreenFacility> prescreenFacilitieListDelete = prescreenFacilityDAO.findByPreScreen(prescreen);
        if(prescreenFacilitieListDelete != null){
            prescreenFacilityDAO.delete(prescreenFacilitieListDelete);
        }

        List<PrescreenFacility> prescreenFacilityList = prescreenFacilityTransform.transformToModel(facilityViewList, prescreen);
        prescreenFacilityDAO.persist(prescreenFacilityList);

        //Remove all Customer before add new
        /*List<Customer> customerListDelete = customerDAO.findByWorkCasePreScreenId(workCasePreScreenId);
        for(Customer customer : customerListDelete){
            if(customer.getAddressesList() != null){
                List<Address> addressList = customer.getAddressesList();
                addressDAO.delete(addressList);
            }
            if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == 1){
                Individual individual = customer.getIndividual();
                individualDAO.delete(individual);
            } else if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == 2){
                Juristic juristic = customer.getJuristic();
                juristicDAO.delete(juristic);
            }
            customerDAO.delete(customer);
        }*/
        //customerDAO.delete(customerListDelete);

        List<Customer> customerList = customerTransform.transformToModelList(customerInfoViewList, workCasePrescreen, null);
        log.info("savePreScreenInitial ::: customerList : {}", customerList);
        for(Customer customer : customerList){
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
        }
        //customerDAO.persist(customerList);

        /*//Remove all Business before add new
        List<BizInfoDetail> bizInfoListDelete = bizInfoDAO.findByWorkCasePreScreen(workCasePrescreen);
        if(bizInfoListDelete != null){
            bizInfoDAO.delete(bizInfoListDelete);
        }

        List<BizInfoDetail> bizInfoList = bizInfoTransform.transformPrescreenToModel(bizInfoViewList, workCasePrescreen);
        bizInfoDAO.persist(bizInfoList);*/
    }

    public void savePreScreenChecker(List<CustomerInfoView> customerInfoViews, long workCasePreScreenId){
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        List<Customer> customerList = customerTransform.transformToModelList(customerInfoViews, workCasePrescreen, null);

        log.info("saveCustomer ::: customerList : {}", customerList);
        for(Customer customer : customerList){
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
        }
    }

    public void savePreScreenMaker(PrescreenView prescreenView, List<FacilityView> facilityViewList, List<CustomerInfoView> customerInfoViewList, List<BizInfoDetailView> bizInfoDetailViewList, List<CollateralView> collateralViewList, long workCasePreScreenId, User user){
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);

        Prescreen prescreen = prescreenTransform.transformToModel(prescreenView, workCasePrescreen, user);
        prescreenDAO.persist(prescreen);

        //Remove all Facility before add new
        List<PrescreenFacility> prescreenFacilitieListDelete = prescreenFacilityDAO.findByPreScreen(prescreen);
        if(prescreenFacilitieListDelete != null){
            prescreenFacilityDAO.delete(prescreenFacilitieListDelete);
        }

        List<PrescreenFacility> prescreenFacilityList = prescreenFacilityTransform.transformToModel(facilityViewList, prescreen);
        prescreenFacilityDAO.persist(prescreenFacilityList);

        //Remove all Customer before add new
        List<Customer> customerListDelete = customerDAO.findByWorkCasePreScreenId(workCasePreScreenId);
        for(Customer customer : customerListDelete){
            if(customer.getAddressesList() != null){
                List<Address> addressList = customer.getAddressesList();
                addressDAO.delete(addressList);
            }
            if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == 1){
                Individual individual = customer.getIndividual();
                individualDAO.delete(individual);
            } else if(customer.getCustomerEntity() != null && customer.getCustomerEntity().getId() == 2){
                Juristic juristic = customer.getJuristic();
                juristicDAO.delete(juristic);
            }
            customerDAO.delete(customer);
        }

        //Save all Customer after Remove
        List<Customer> customerList = customerTransform.transformToModelList(customerInfoViewList, workCasePrescreen, null);
        log.info("savePreScreenInitial ::: customerList : {}", customerList);
        for(Customer customer : customerList){
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
        }

        //Save PreScreen Business
        List<PrescreenBusiness> prescreenBusinessDelete = prescreenBusinessDAO.findByPreScreen(prescreen);
        if(prescreenBusinessDelete != null){
            prescreenBusinessDAO.delete(prescreenBusinessDelete);
        }

        List<PrescreenBusiness> prescreenBusinessList = prescreenBusinessTransform.transformToModelList(bizInfoDetailViewList, prescreen);
        prescreenBusinessDAO.persist(prescreenBusinessList);

        //Save PreScreen Collateral
        List<PrescreenCollateral> prescreenCollateralDelete = prescreenCollateralDAO.findByPreScreen(prescreen);
        if(prescreenCollateralDelete != null){
            prescreenCollateralDAO.delete(prescreenCollateralDelete);
        }

        List<PrescreenCollateral> prescreenCollateralList = prescreenCollateralTransform.transformToModelList(collateralViewList, prescreen);
        prescreenCollateralDAO.persist(prescreenCollateralList);

    }

    public void assignChecker(long workCasePreScreenId, String queueName, String checkerId, String actionCode){
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        Action action = actionDAO.findById(Long.parseLong(actionCode));

        HashMap<String,String> fields = new HashMap<String, String>();
        fields.put("Action_Code", Long.toString(action.getId()));
        fields.put("Action_Name", action.getName());
        fields.put("BDMCheckerUserName", checkerId);

        log.info("assignChecker ::: workCasePreScreenid : {}", workCasePreScreenId);
        log.info("assignChecker ::: queueName : {}", queueName);
        log.info("assignChecker ::: Action_Code : {}", action.getId());
        log.info("assignChecker ::: Action_Name : {}", action.getName());
        log.info("assignChecker ::: BDMCheckerUserName : {}", checkerId);

        bpmInterface.dispatchCase(queueName, workCasePrescreen.getWobNumber(), fields);
    }

    public void nextStepPreScreen(long workCasePreScreenId, String queueName, String actionCode){
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        Action action = actionDAO.findById(Long.parseLong(actionCode));

        HashMap<String,String> fields = new HashMap<String, String>();
        fields.put("Action_Code", Long.toString(action.getId()));
        fields.put("Action_Name", action.getName());

        log.info("nextStepPreScreen ::: workCasePreScreenid : {}", workCasePreScreenId);
        log.info("nextStepPreScreen ::: wobNumber : {}", workCasePrescreen.getWobNumber());
        log.info("nextStepPreScreen ::: queueName : {}", queueName);
        log.info("nextStepPreScreen ::: Action_Code : {}", action.getId());
        log.info("nextStepPreScreen ::: Action_Name : {}", action.getName());

        bpmInterface.dispatchCase(queueName, workCasePrescreen.getWobNumber(), fields);
    }

    //*** Function for PreScreen Checker ***//
    public String getBDMMakerName(long workCasePreScreenId){
        String bdmMakerName = "";
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);
        if(workCasePrescreen != null){
            bdmMakerName = workCasePrescreen.getCreateBy().getUserName();
        }

        return bdmMakerName;
    }

    public List<CustomerInfoView> getCustomerListByWorkCasePreScreenId(long workCasePreScreenId){
        List<CustomerInfoView> customerInfoViewList = new ArrayList<CustomerInfoView>();
        List<Customer> customerList = customerDAO.findByWorkCasePreScreenId(workCasePreScreenId);
        log.info("getBorrowerListByWorkCaseId ::: customerList : {}", customerList);
        customerInfoViewList = customerTransform.transformToViewList(customerList);
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

    public void savePreScreen(PrescreenView prescreenView, List<FacilityView> facilityViewList, List<BizInfoDetailView> bizInfoViewList, long workCasePreScreenId){
        WorkCasePrescreen workCasePrescreen = workCasePrescreenDAO.findById(workCasePreScreenId);

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

}
