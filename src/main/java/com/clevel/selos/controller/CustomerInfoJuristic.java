package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.CalculationControl;
import com.clevel.selos.businesscontrol.CustomerInfoControl;
import com.clevel.selos.businesscontrol.HeaderControl;
import com.clevel.selos.businesscontrol.master.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.model.view.master.DistrictView;
import com.clevel.selos.model.view.master.KYCLevelView;
import com.clevel.selos.model.view.master.ProvinceView;
import com.clevel.selos.system.audit.SLOSAuditor;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@ViewScoped
@ManagedBean(name = "custInfoSumJuris")
public class CustomerInfoJuristic extends BaseController {
    @Inject
    @SELOS
    Logger log;

    @Inject
    @NormalMessage
    Message msg;

    @Inject
    private SLOSAuditor slosAuditor;

    @Inject
    @ValidationMessage
    Message validationMsg;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    @Inject
    private CustomerInfoControl customerInfoControl;
    @Inject
    private CalculationControl calculationControl;
    @Inject
    private DocumentTypeControl documentTypeControl;
    @Inject
    private RelationCustomerControl relationCustomerControl;
    @Inject
    private ReferenceControl referenceControl;
    @Inject
    private TitleControl titleControl;
    @Inject
    private BusinessTypeControl businessTypeControl;
    @Inject
    private ProvinceControl provinceControl;
    @Inject
    private DistrictControl districtControl;
    @Inject
    private SubDistrictControl subDistrictControl;
    @Inject
    private CountryControl countryControl;
    @Inject
    private AddressTypeControl addressTypeControl;
    @Inject
    private KYCLevelControl kycLevelControl;
    @Inject
    private IncomeSourceControl incomeSourceControl;
    @Inject
    private HeaderControl headerControl;

    //*** Drop down List ***//
    private List<SelectItem> documentTypeList;
    private List<SelectItem> relationList;
    private List<SelectItem> referenceList;
    private List<SelectItem> titleThList;
    private List<SelectItem> titleEnList;
    private List<SelectItem> businessTypeList;

    private List<SelectItem> provinceForm1List;
    private List<SelectItem> districtForm1List;
    private List<SelectItem> subDistrictForm1List;
    private List<SelectItem> provinceForm2List;
    private List<SelectItem> districtForm2List;
    private List<SelectItem> subDistrictForm2List;

    private List<SelectItem> countryList;
    private List<SelectItem> addressTypeList;
    private List<SelectItem> kycLevelList;

    private List<String> yearList;

    private List<SelectItem> incomeSourceList;

    //*** View ***//
    private CustomerInfoView customerInfoView;
    private CustomerInfoView customerInfoSearch;

    private String messageHeader;
    private String message;
    private String severity;

    //session
    private long workCaseId;
    private long stepId;

    private int caseBorrowerTypeId;

    // Boolean for search customer //
    private boolean enableDocumentType;
    private boolean enableCitizenId;
    private boolean enableSpouseDocumentType;
    private boolean enableSpouseCitizenId;

    //onEdit
    private CustomerInfoView selectEditIndividual;
    private boolean isEditBorrower;
    private int rowIndex;

    // Mandate boolean for change Reference
    private boolean reqRelation;
    private boolean reqReference;
    private boolean reqDocType;
    private boolean reqRegId;
    private boolean reqTitTh;
    private boolean reqStNameTh;
    private boolean reqTitEn;
    private boolean reqStNameEn;
    private boolean reqBizType;
    private boolean reqRegNo;
    private boolean reqRegPro;
    private boolean reqRegDis;
    private boolean reqRegSub;
    private boolean reqRegPos;
    private boolean reqRegCou;
    private boolean reqRegPhone;
    private boolean reqWorNo;
    private boolean reqWorPro;
    private boolean reqWorDis;
    private boolean reqWorSub;
    private boolean reqWorPos;
    private boolean reqWorCou;
    private boolean reqWorPhone;
    private boolean reqMobNo;
    private boolean reqAddMail;
    private boolean reqKYCLev;

    //param for map
    private long customerId;
    private boolean isFromSummaryParam;
    private boolean isFromIndividualParam;

    private String currentDateDDMMYY;

    private boolean isEditForm;

    private boolean enableAllFieldCus;

    private int relationId;
    private int referenceId;

    private String userId;

    public CustomerInfoJuristic(){
    }

    public void preRender(){
        log.debug("preRender");
        HttpSession session = FacesUtil.getSession(false);

        if(checkSession(session)){
            //TODO Check valid stepId
            log.debug("preRender ::: Check valid stepId");
        }else{
            log.debug("preRender ::: No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation");

        Date date = new Date();

        HttpSession session = FacesUtil.getSession(false);

        User user = getCurrentUser();
        if(!Util.isNull(user)) {
            userId = user.getId();
        } else {
            userId = "Null";
        }

        if(checkSession(session)){
            workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
            stepId = Util.parseLong(session.getAttribute("stepId"), 0);

            initial();

            String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");
            loadFieldControl(workCaseId, Screen.CUSTOMER_INFO_JURISTIC, ownerCaseUserId);

            enableAllFieldCus = false;

            Map<String, Object> cusInfoParams = (Map<String, Object>) session.getAttribute("cusInfoParams");
            if (cusInfoParams != null) {
                isFromSummaryParam = (Boolean) cusInfoParams.get("isFromSummaryParam");
                isFromIndividualParam = (Boolean) cusInfoParams.get("isFromIndividualParam");
                customerId = (Long) cusInfoParams.get("customerId");
                if(isFromIndividualParam){
                    customerInfoView = (CustomerInfoView) cusInfoParams.get("customerInfoView");
                    onEditJuristic();
                }
            }

            if(isFromSummaryParam){                         // go to edit from summary
                if(customerId != 0 && customerId != -1){
                    onEditJuristic();
                }
            }

            slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_CREATION, "", date, ActionResult.SUCCESS, "");
        } else {
            slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_CREATION, "", date, ActionResult.FAILED, "Invalid Session");

            log.debug("No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    public void initial(){
        isEditForm = false;
        customerInfoView = new CustomerInfoView();
        customerInfoView.reset();
        customerInfoView.setIndividualViewList(new ArrayList<CustomerInfoView>());
        customerInfoView.setIndividualViewForShowList(new ArrayList<CustomerInfoView>());
        customerInfoView.setCurrentAddress(null);
        customerInfoView.getRegisterAddress().setAddressTypeFlag(3);
        customerInfoView.getWorkAddress().setAddressTypeFlag(3);

        customerInfoSearch = new CustomerInfoView();
        customerInfoSearch.reset();

        caseBorrowerTypeId = customerInfoControl.getCaseBorrowerTypeIdByWorkCase(workCaseId);

        documentTypeList = documentTypeControl.getDocumentTypeByCustomerEntity(2);
        relationList = relationCustomerControl.getRelationSelectItemWithOutBorrower(BorrowerType.JURISTIC.value(), caseBorrowerTypeId, 0);

        titleEnList = titleControl.getTitleEnSelectItemByCustomerEntity(BorrowerType.JURISTIC.value());
        titleThList = titleControl.getTitleThSelectItemByCustomerEntity(BorrowerType.JURISTIC.value());
        businessTypeList = businessTypeControl.getBusinessTypeSelectItemList();

        provinceForm1List = provinceControl.getProviceSelectItemActiveList();
        provinceForm2List = provinceControl.getProviceSelectItemActiveList();

        countryList = countryControl.getCountrySelectItemActiveList();

        incomeSourceList = incomeSourceControl.getIncomeSourceSelectItemActiveList();

        referenceList = new ArrayList<SelectItem>();

        addressTypeList = addressTypeControl.getAddressTypeSelectItemByCustEntity(BorrowerType.JURISTIC.value());
        kycLevelList = kycLevelControl.getKYCLevelSelectItem();

        yearList = DateTimeUtil.getPreviousFiftyYearTH();

        customerInfoView.setCollateralOwner(1);

        enableDocumentType = true;
        enableCitizenId = true;
        enableSpouseDocumentType = true;
        enableSpouseCitizenId = true;

        customerInfoView.setCapital(BigDecimal.ZERO);
        customerInfoView.setPaidCapital(BigDecimal.ZERO);
        customerInfoView.setSalesFromFinancialStmt(BigDecimal.ZERO);
        customerInfoView.setShareHolderRatio(BigDecimal.ZERO);
        customerInfoView.setTotalShare(BigDecimal.ZERO);

        customerInfoView.getSourceIncome().setId(211);
        customerInfoView.getCountryIncome().setId(211);
        customerInfoView.getRegisterAddress().getCountry().setId(211);
        customerInfoView.getWorkAddress().getCountry().setId(211);

        onChangeReference();
    }

    public void onEditJuristic(){
        if(customerId != 0 && customerId != -1){
            customerInfoView = customerInfoControl.getCustomerJuristicById(customerId);
        }

        if(customerInfoView.getId() != 0 || isFromIndividualParam){
            isEditForm = true;
        } else {
            isEditForm = false;
        }

        enableAllFieldCus = true;

        if(customerInfoView.getRelation() != null){
            relationId = customerInfoView.getRelation().getId();
        } else {
            relationId = 0;
        }

        if(customerInfoView.getReference() != null){
            referenceId = customerInfoView.getReference().getId();
        } else {
            referenceId = 0;
        }

        onChangeRelation();
        onChangeReference();
        onChangeProvinceEditForm1();
        onChangeDistrictEditForm1();
        onChangeProvinceEditForm2();
        onChangeDistrictEditForm2();

        if(customerInfoView.getSearchFromRM() == 1){
            enableDocumentType = false;
            enableCitizenId = false;
        }

        if(relationId == RelationValue.BORROWER.value()){
            isEditBorrower = true;
            relationList = relationCustomerControl.getRelationSelectItem(BorrowerType.JURISTIC.value(), caseBorrowerTypeId, 0);
        }else{
            relationList = relationCustomerControl.getRelationSelectItemWithOutBorrower(BorrowerType.JURISTIC.value(), caseBorrowerTypeId, 0);
        }

        if(customerInfoView.getRemoveIndividualIdList() == null){
            customerInfoView.setRemoveIndividualIdList(new ArrayList<Long>());
        }
    }

    public String onAddIndividual(){
        Date date = new Date();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isFromSummaryParam",false);
        map.put("isFromJuristicParam",true);
        map.put("isEditFromJuristic", false);
        map.put("customerId", -1L);
        map.put("customerInfoView", customerInfoView);


        slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_ADD, "On Add Individual", date, ActionResult.SUCCESS, "");

        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("cusInfoParams", map);

        return "customerInfoIndividual?faces-redirect=true";
    }

    public String onEditIndividual(){
        Date date = new Date();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isFromSummaryParam",false);
        map.put("isFromJuristicParam",true);
        map.put("isEditFromJuristic", true);
        map.put("customerId", -1L);
        map.put("customerInfoView", customerInfoView);
        if(!Util.isNull(customerInfoView.getIndividualViewList())) {
            for(CustomerInfoView cusView : customerInfoView.getIndividualViewList()) {
                if(cusView.getListIndex() == selectEditIndividual.getListIndex()) {
                    if(cusView.getIsSpouse() != 1) {
                        map.put("individualView", cusView);
                    }
                    map.put("listIndex", selectEditIndividual.getListIndex());
                }
            }
        }

        slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_EDIT, "On Edit Individual", date, ActionResult.SUCCESS, "");

        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("cusInfoParams", map);

        return "customerInfoIndividual?faces-redirect=true";
        //listIndex = original row
    }

    public void onChangeRelation(){
        referenceList = referenceControl.getReferenceSelectItemByFlag(BorrowerType.JURISTIC.value(), caseBorrowerTypeId, relationId, 1, 0);
        Relation relation = new Relation();
        relation.setId(relationId);
        customerInfoView.setRelation(relation);
    }

    public void onChangeProvinceForm1() {
        if(customerInfoView.getRegisterAddress() != null && customerInfoView.getRegisterAddress().getProvince().getCode() != 0){
            ProvinceView provinceView = provinceControl.getProvinceViewById(customerInfoView.getRegisterAddress().getProvince().getCode());
            districtForm1List = districtControl.getDistrictSelectItemByProvince(provinceView.getCode());
            customerInfoView.getRegisterAddress().setDistrict(new District());
            subDistrictForm1List = new ArrayList<SelectItem>();
        }else{
            provinceForm1List = provinceControl.getProviceSelectItemActiveList();
            districtForm1List = new ArrayList<SelectItem>();
            subDistrictForm1List = new ArrayList<SelectItem>();
        }
    }

    public void onChangeDistrictForm1() {
        if(customerInfoView.getRegisterAddress() != null && customerInfoView.getRegisterAddress().getDistrict().getId() != 0){
            DistrictView districtView = districtControl.getDistrictById(customerInfoView.getRegisterAddress().getDistrict().getId());
            subDistrictForm1List = subDistrictControl.getSubDistrictSelectItemByDistrict(districtView.getId());
        }else{
            onChangeProvinceForm1();
            subDistrictForm1List = new ArrayList<SelectItem>();
        }
    }

    public void onChangeProvinceForm2() {
        if(customerInfoView.getWorkAddress() != null && customerInfoView.getWorkAddress().getProvince().getCode() != 0){
            ProvinceView provinceView = provinceControl.getProvinceViewById(customerInfoView.getWorkAddress().getProvince().getCode());
            districtForm2List = districtControl.getDistrictSelectItemByProvince(provinceView.getCode());
            customerInfoView.getWorkAddress().setDistrict(new District());
            subDistrictForm2List = new ArrayList<SelectItem>();
        }else{
            provinceForm2List = provinceControl.getProviceSelectItemActiveList();
            districtForm2List = new ArrayList<SelectItem>();
            subDistrictForm2List = new ArrayList<SelectItem>();
        }
    }

    public void onChangeDistrictForm2() {
        if(customerInfoView.getWorkAddress() != null && customerInfoView.getWorkAddress().getDistrict().getId() != 0){
            DistrictView districtView = districtControl.getDistrictById(customerInfoView.getWorkAddress().getDistrict().getId());
            subDistrictForm2List = subDistrictControl.getSubDistrictSelectItemByDistrict(districtView.getId());
        }else{
            onChangeProvinceForm2();
            subDistrictForm2List = new ArrayList<SelectItem>();
        }
    }

    public void onChangeProvinceEditForm1(){
        if(customerInfoView.getRegisterAddress() != null && customerInfoView.getRegisterAddress().getProvince().getCode() != 0){
            ProvinceView provinceView = provinceControl.getProvinceViewById(customerInfoView.getRegisterAddress().getProvince().getCode());
            districtForm1List = districtControl.getDistrictSelectItemByProvince(provinceView.getCode());
        }else{
            provinceForm1List = provinceControl.getProviceSelectItemActiveList();
            districtForm1List = new ArrayList<SelectItem>();
            subDistrictForm1List = new ArrayList<SelectItem>();
        }
    }

    public void onChangeDistrictEditForm1(){
        if(customerInfoView.getRegisterAddress() != null && customerInfoView.getRegisterAddress().getDistrict().getId() != 0){
            DistrictView districtView = districtControl.getDistrictById(customerInfoView.getRegisterAddress().getDistrict().getId());
            subDistrictForm1List = subDistrictControl.getSubDistrictSelectItemByDistrict(districtView.getId());
        }else{
            subDistrictForm1List = new ArrayList<SelectItem>();
        }
    }

    public void onChangeProvinceEditForm2() {
        if(customerInfoView.getWorkAddress() != null && customerInfoView.getWorkAddress().getProvince().getCode() != 0){
            ProvinceView provinceView = provinceControl.getProvinceViewById(customerInfoView.getWorkAddress().getProvince().getCode());
            districtForm2List = districtControl.getDistrictSelectItemByProvince(provinceView.getCode());
        }else{
            provinceForm2List = provinceControl.getProviceSelectItemActiveList();
            districtForm2List = new ArrayList<SelectItem>();
            subDistrictForm2List = new ArrayList<SelectItem>();
        }
    }

    public void onChangeDistrictEditForm2() {
        if(customerInfoView.getWorkAddress() != null && customerInfoView.getWorkAddress().getDistrict().getId() != 0){
            DistrictView districtView = districtControl.getDistrictById(customerInfoView.getWorkAddress().getDistrict().getId());
            subDistrictForm2List = subDistrictControl.getSubDistrictSelectItemByDistrict(districtView.getId());
        }else{
            subDistrictForm2List = new ArrayList<SelectItem>();
        }
    }

    public void onSearchCustomerInfo() {
        Date date = new Date();
        log.debug("onSearchCustomerInfo :::");
        log.debug("onSearchCustomerInfo ::: customerInfoView : {}", customerInfoSearch);
        CustomerInfoResultView customerInfoResultView;
        try{
            customerInfoResultView = customerInfoControl.getCustomerInfoFromRM(customerInfoSearch);
            log.debug("onSearchCustomerInfo ::: customerInfoResultView : {}", customerInfoResultView);
            enableAllFieldCus = true;
            isEditForm = true;
            if(customerInfoResultView.getActionResult().equals(ActionResult.SUCCESS)){
                log.debug("onSearchCustomerInfo ActionResult.SUCCESS");
                if(customerInfoResultView.getCustomerInfoView() != null){
                    log.debug("onSearchCustomerInfo ::: customer found : {}", customerInfoResultView.getCustomerInfoView());
                    customerInfoView = customerInfoResultView.getCustomerInfoView();

                    customerInfoView.getDocumentType().setId(customerInfoSearch.getDocumentType().getId());
                    customerInfoView.setSearchFromRM(1);
                    customerInfoView.setSearchBy(customerInfoSearch.getSearchBy());
                    customerInfoView.setSearchId(customerInfoSearch.getSearchId());
                    customerInfoView.setCollateralOwner(1);

                    //set default country
                    if(customerInfoView.getCitizenCountry() != null){
                        customerInfoView.getCitizenCountry().setId(211);
                    } else {
                        CountryView countryView = new CountryView();
                        countryView.setId(211);
                        customerInfoView.setCitizenCountry(countryView);
                    }

                    //set default source of income country
                    if(customerInfoView.getCountryIncome() != null){
                        customerInfoView.getCountryIncome().setId(211);
                    } else {
                        CountryView countryView = new CountryView();
                        countryView.setId(211);
                        customerInfoView.setCountryIncome(countryView);
                    }

                    if(customerInfoView.getRegisterAddress() != null && customerInfoView.getWorkAddress() != null){
                        if(customerInfoControl.checkAddress(customerInfoView.getRegisterAddress(),customerInfoView.getWorkAddress()) == 1){
                            customerInfoView.getWorkAddress().setAddressTypeFlag(1);
                        } else {
                            customerInfoView.getWorkAddress().setAddressTypeFlag(3);
                        }
                        customerInfoView.getWorkAddress().setAddressTypeFlag(1);
                    }

                    if(customerInfoView.getWorkAddress() == null){
                        customerInfoView.setWorkAddress(new AddressView());
                        customerInfoView.getWorkAddress().setAddressTypeFlag(3);
                    }

                    enableDocumentType = false;
                    enableCitizenId = false;

                    messageHeader = "Information.";
                    message = "Search customer found.";
                    severity = "info";

                    slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_ACTION, "On Search Customer Information - Search ID :: " + customerInfoSearch.getSearchId(), date, ActionResult.SUCCESS, message);
                } else {
                    log.debug("onSearchCustomerInfo ::: customer not found.");
                    enableDocumentType = true;
                    enableCitizenId = true;

                    messageHeader = "Information.";
                    message = "Search customer not found.";
                    severity = "info";

                    slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_ACTION, "On Search Customer Information - Search ID :: " + customerInfoSearch.getSearchId(), date, ActionResult.FAILED, message);
                }
            } else {
                enableDocumentType = true;
                enableCitizenId = true;
                messageHeader = "Information.";
                message = customerInfoResultView.getReason();
                severity = "info";

                slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_ACTION, "On Search Customer Information - Search ID :: " + customerInfoSearch.getSearchId(), date, ActionResult.FAILED, message);
            }

            customerInfoView.getDocumentType().setId(customerInfoSearch.getDocumentType().getId());
            if(customerInfoSearch.getSearchBy() == 1){
                customerInfoView.setRegistrationId(customerInfoSearch.getSearchId());
            }

            onChangeProvinceEditForm1();
            onChangeDistrictEditForm1();
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception ex) {
            enableDocumentType = true;
            enableCitizenId = true;
            customerInfoView.getDocumentType().setId(customerInfoSearch.getDocumentType().getId());
            customerInfoView.setRegistrationId(customerInfoSearch.getSearchId());
            log.error("onSearchCustomerInfo Exception : {}", ex);
            messageHeader = "Error.";
            message = Util.getMessageException(ex);
            severity = "alert";

            slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_ACTION, "On Search Customer Information - Search ID :: " + customerInfoSearch.getSearchId(), date, ActionResult.FAILED, message);

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onRefreshInterfaceInfo(){
        Date date = new Date();

        if(customerInfoView.getSearchFromRM() == 1){
            long cusId = customerInfoView.getId();
            int searchBy = customerInfoView.getSearchBy();
            String searchId = customerInfoView.getSearchId();
            int relId = 0;
            int refId = 0;
            if(relationId == RelationValue.BORROWER.value()){
                relId = relationId;
                refId = referenceId;
            }

            log.debug("refreshInterfaceInfo ::: customerInfoView : {}", customerInfoView);
            CustomerInfoResultView customerInfoResultView;
            try{
                customerInfoResultView = customerInfoControl.getCustomerInfoFromRM(customerInfoView);
                log.debug("refreshInterfaceInfo ::: customerInfoResultView : {}", customerInfoResultView);
                if(customerInfoResultView.getActionResult().equals(ActionResult.SUCCESS)){
                    log.debug("refreshInterfaceInfo ActionResult.SUCCESS");
                    if(customerInfoResultView.getCustomerInfoView() != null){
                        log.debug("refreshInterfaceInfo ::: customer found : {}", customerInfoResultView.getCustomerInfoView());
                        customerInfoView = customerInfoResultView.getCustomerInfoView();
                        customerInfoView.setId(cusId);
                        Relation relation = new Relation();
                        relation.setId(relId);
                        customerInfoView.setRelation(relation);
                        Reference reference = new Reference();
                        reference.setId(refId);
                        customerInfoView.setReference(reference);

                        //set default country
                        if(customerInfoView.getCitizenCountry() != null){
                            customerInfoView.getCitizenCountry().setId(211);
                        } else {
                            CountryView countryView = new CountryView();
                            countryView.setId(211);
                            customerInfoView.setCitizenCountry(countryView);
                        }

                        //set default source of income country
                        if(customerInfoView.getCountryIncome() != null){
                            customerInfoView.getCountryIncome().setId(211);
                        } else {
                            CountryView countryView = new CountryView();
                            countryView.setId(211);
                            customerInfoView.setCountryIncome(countryView);
                        }

                        if(customerInfoView.getRegisterAddress() != null && customerInfoView.getWorkAddress() != null){
                            if(customerInfoControl.checkAddress(customerInfoView.getRegisterAddress(),customerInfoView.getWorkAddress()) == 1){
                                customerInfoView.getWorkAddress().setAddressTypeFlag(1);
                            } else {
                                customerInfoView.getWorkAddress().setAddressTypeFlag(3);
                            }
                        }

                        messageHeader = "Information.";
                        message = "Refresh interface info complete.";
                        severity = "info";

                        slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_ACTION, "On Refresh Interface Information", date, ActionResult.SUCCESS, message);
                    }else{
                        log.debug("refreshInterfaceInfo ::: customer not found.");
                        messageHeader = "Information.";
                        message = "Refresh interface info failed.";
                        severity = "info";

                        slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_ACTION, "On Refresh Interface Information", date, ActionResult.FAILED, message);
                    }
                } else {
                    messageHeader = "Information.";
                    message = customerInfoResultView.getReason();
                    severity = "info";

                    slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_ACTION, "On Refresh Interface Information", date, ActionResult.FAILED, message);
                }
                customerInfoView.setSearchFromRM(1);
                customerInfoView.setSearchBy(searchBy);
                customerInfoView.setSearchId(searchId);
                onChangeProvinceEditForm1();
                onChangeDistrictEditForm1();
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            } catch (Exception ex) {
                log.error("refreshInterfaceInfo Exception : {}", ex);
                messageHeader = "Error.";
                message = Util.getMessageException(ex);
                severity = "alert";
                customerInfoView.setSearchFromRM(1);
                customerInfoView.setSearchBy(searchBy);
                customerInfoView.setSearchId(searchId);

                slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_ACTION, "On Refresh Interface Information", date, ActionResult.FAILED, message);

                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        } else {
            messageHeader = "Information.";
            message = "Cause this customer do not search from RM";
            severity = "info";

            slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_ACTION, "On Refresh Interface Information", date, ActionResult.FAILED, message);

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onChangeReference(){
        reqRelation = true;
        reqReference = true;
        reqDocType = true;
        reqRegId = true;
        reqTitTh = true;
        reqStNameTh = true;
        reqKYCLev = true;
        Reference reference = new Reference();
        reference.setId(referenceId);
        customerInfoView.setReference(reference);
    }

    public void onSave(){
        Date date = new Date();

        log.debug("onSave");
        //check registration
        if(customerInfoControl.isDuplicateCustomerJuris(customerInfoView.getRegistrationId(), customerInfoView.getId() ,workCaseId)){
            messageHeader = "Information.";
            message = "Registration Id is already exist";
            severity = "info";

            slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_SAVE, "", date, ActionResult.FAILED, message);

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        if(customerInfoView.getWorkAddress().getAddressTypeFlag() == 1){ //dup address 1 to address 2 - Address 1 is Regis , Address 2 is Work
            AddressView addressView = new AddressView(customerInfoView.getRegisterAddress(),customerInfoView.getWorkAddress().getId());
            addressView.setAddressTypeFlag(1);
            customerInfoView.setWorkAddress(addressView);
        }

        //check using customer in basic info
        if(customerInfoView.getId() != 0){
            boolean isExist = customerInfoControl.checkExistingAll(customerInfoView.getId());
            if(isExist){
                if(relationId == RelationValue.DIRECTLY_RELATED.value()
                        || relationId == RelationValue.INDIRECTLY_RELATED.value()){
                    messageHeader = "Information.";
                    message = msg.get("app.message.customer.existing.error");
                    severity = "info";

                    slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_SAVE, "", date, ActionResult.FAILED, message);

                    RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
                    return;
                }
            }
        }

        Relation relation = new Relation();
        relation.setId(relationId);
        Reference reference = new Reference();
        reference.setId(referenceId);
        customerInfoView.setRelation(relation);
        customerInfoView.setReference(reference);

        try{
            customerId = customerInfoControl.saveCustomerInfoJuristic(customerInfoView, workCaseId);
            calculationControl.calForCustomerInfo(workCaseId);
            calculationControl.calculateBasicInfo(workCaseId);
            isFromSummaryParam = true;
            initial();
            onEditJuristic();
            messageHeader = "Information.";
            message = "Save Customer Juristic Data Success.";
            severity = "info";

            slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_SAVE, "", date, ActionResult.SUCCESS, message);

            //updateHeaderInfo();
            RequestContext.getCurrentInstance().execute("msgBoxSaveMessageDlg.show()");
        } catch(Exception ex){
            log.error("Exception :: ", ex);
            messageHeader = "Error.";
            message = "Save Juristic Failed. Cause : " + Util.getMessageException(ex);
            severity = "alert";

            slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_SAVE, "", date, ActionResult.FAILED, message);

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    private void updateHeaderInfo(){
        AppHeaderView appHeaderView = getAppHeaderView();
        headerControl.updateCustomerInfo(appHeaderView, 0, workCaseId);
        setAppHeaderView(appHeaderView);
    }

    public void onChangeTitleTh(){
        customerInfoView.setTitleEn(new Title());
        customerInfoView.getTitleEn().setId(customerInfoView.getTitleTh().getId());
    }

    public void onChangeTitleEn(){
        customerInfoView.setTitleTh(new Title());
        customerInfoView.getTitleTh().setId(customerInfoView.getTitleEn().getId());
    }

    public void onDeleteIndividual(){
        Date date = new Date();
        try{
            //check individual using on basic info
            if(selectEditIndividual.getId() != 0){
                boolean isExist = customerInfoControl.checkExistingAll(selectEditIndividual.getId());
                if(isExist){
                    messageHeader = "Information.";
                    message = msg.get("app.message.customer.existing.error2");
                    severity = "info";

                    slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_DELETE, "On Delete Customer Information Individual.", date, ActionResult.FAILED, message);
                } else {
                    List<CustomerInfoView> cusTmp = new ArrayList<CustomerInfoView>();
                    if(selectEditIndividual.getIsSpouse() == 1) {
                        for(CustomerInfoView cusView : customerInfoView.getIndividualViewForShowList()) {
                            if(cusView.getListIndex() != selectEditIndividual.getListIndex()) {
                                cusTmp.add(cusView);
                            } else {
                                if(!cusView.getCitizenId().equalsIgnoreCase(selectEditIndividual.getCitizenId())) {
                                    cusTmp.add(cusView);
                                }
                            }
                        }
                        customerInfoView.setIndividualViewForShowList(cusTmp);
                        CustomerInfoView cusView = new CustomerInfoView();
                        cusView.reset();
                        customerInfoView.getIndividualViewList().get(selectEditIndividual.getListIndex()).setSpouse(cusView);
                    } else {
                        for(CustomerInfoView cusView : customerInfoView.getIndividualViewForShowList()) {
                            if(cusView.getListIndex() != selectEditIndividual.getListIndex()) {
                                cusTmp.add(cusView);
                            }
                        }
                        customerInfoView.setIndividualViewForShowList(cusTmp);
                        customerInfoView.getIndividualViewList().remove(selectEditIndividual.getListIndex());
                        //after remove on original list
                        for(CustomerInfoView cusView : customerInfoView.getIndividualViewForShowList()) {
                            if(cusView.getListIndex() > selectEditIndividual.getListIndex()) {
                                cusView.setListIndex(cusView.getListIndex() - 1);
                            }
                        }
                    }
                    customerInfoView.getRemoveIndividualIdList().add(selectEditIndividual.getId());
                    messageHeader = "Information.";
                    message = "Delete Customer Info Individual Success.";
                    severity = "info";

                    slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_DELETE, "On Delete Customer Information Individual.", date, ActionResult.SUCCESS, message);
                }
            } else {
                List<CustomerInfoView> cusTmp = new ArrayList<CustomerInfoView>();
                if(selectEditIndividual.getIsSpouse() == 1) {
                    for(CustomerInfoView cusView : customerInfoView.getIndividualViewForShowList()) {
                        if(cusView.getListIndex() != selectEditIndividual.getListIndex()) {
                            cusTmp.add(cusView);
                        } else {
                            if(!cusView.getCitizenId().equalsIgnoreCase(selectEditIndividual.getCitizenId())) {
                                cusTmp.add(cusView);
                            }
                        }
                    }
                    customerInfoView.setIndividualViewForShowList(cusTmp);
                    CustomerInfoView cusView = new CustomerInfoView();
                    cusView.reset();
                    customerInfoView.getIndividualViewList().get(selectEditIndividual.getListIndex()).setSpouse(cusView);
                } else {
                    for(CustomerInfoView cusView : customerInfoView.getIndividualViewForShowList()) {
                        if(cusView.getListIndex() != selectEditIndividual.getListIndex()) {
                            cusTmp.add(cusView);
                        }
                    }
                    customerInfoView.setIndividualViewForShowList(cusTmp);
                    customerInfoView.getIndividualViewList().remove(selectEditIndividual.getListIndex());
                    //after remove on original list
                    for(CustomerInfoView cusView : customerInfoView.getIndividualViewForShowList()) {
                        if(cusView.getListIndex() > selectEditIndividual.getListIndex()) {
                            cusView.setListIndex(cusView.getListIndex() - 1);
                        }
                    }
                }
                messageHeader = "Information.";
                message = "Delete Customer Info Individual Success.";
                severity = "info";

                slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_DELETE, "On Delete Customer Information Individual.", date, ActionResult.SUCCESS, message);
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception ex) {
            log.error("Exception :: {}",ex);
            messageHeader = "Error.";
            message = "Delete Customer Info Individual Failed. <br/><br/> Cause : " + Util.getMessageException(ex);
            severity = "alert";

            slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_DELETE, "On Delete Customer Information Individual.", date, ActionResult.FAILED, message);

            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    public String onCancelForm(boolean isRedirect){
        slosAuditor.add(Screen.CUSTOMER_INFO_JURISTIC.value(), userId, ActionAudit.ON_CANCEL, "", new Date(), ActionResult.SUCCESS, "");
        if(isRedirect){
            return "customerInfoSummary?faces-redirect=true";
        } else {
            RequestContext.getCurrentInstance().execute("msgBoxCancelDlg.show()");
            return "";
        }
    }

    public void onChangeCountryIncome() {
        if(customerInfoView.getCountryIncome() != null && customerInfoView.getCountryIncome().getId() != 0){
            CountryView countryView = countryControl.getCountryViewById(customerInfoView.getCountryIncome().getId());
            customerInfoView.setCountryIncome(countryView);
        }
    }

    public void onChangeRegisterAddress() {
        if(customerInfoView.getRegisterAddress() != null && customerInfoView.getRegisterAddress().getCountry() != null && customerInfoView.getRegisterAddress().getCountry().getId() != 0){
            CountryView countryView = countryControl.getCountryViewById(customerInfoView.getRegisterAddress().getCountry().getId());
            customerInfoView.getRegisterAddress().setCountry(countryView);
        }
    }

    public void onChangeWorkAddress() {
        if(customerInfoView.getWorkAddress() != null && customerInfoView.getWorkAddress().getCountry() != null && customerInfoView.getWorkAddress().getCountry().getId() != 0){
            CountryView countryView = countryControl.getCountryViewById(customerInfoView.getWorkAddress().getCountry().getId());
            customerInfoView.getWorkAddress().setCountry(countryView);
        }
    }

    public void onChangeKYCLv() {
        if(customerInfoView.getKycLevel() != null && customerInfoView.getKycLevel().getId() != 0){
            KYCLevelView kycLevelView = kycLevelControl.getKYCLevelViewById(customerInfoView.getKycLevel().getId());
            customerInfoView.setKycLevel(kycLevelView);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////// Get Set ////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public CustomerInfoView getCustomerInfoView() {
        return customerInfoView;
    }

    public void setCustomerInfoView(CustomerInfoView customerInfoView) {
        this.customerInfoView = customerInfoView;
    }

    public List<SelectItem> getDocumentTypeList() {
        return documentTypeList;
    }

    public void setDocumentTypeList(List<SelectItem> documentTypeList) {
        this.documentTypeList = documentTypeList;
    }

    public List<SelectItem> getRelationList() {
        return relationList;
    }

    public void setRelationList(List<SelectItem> relationList) {
        this.relationList = relationList;
    }

    public List<SelectItem> getReferenceList() {
        return referenceList;
    }

    public void setReferenceList(List<SelectItem> referenceList) {
        this.referenceList = referenceList;
    }

    public CustomerInfoView getCustomerInfoSearch() {
        return customerInfoSearch;
    }

    public void setCustomerInfoSearch(CustomerInfoView customerInfoSearch) {
        this.customerInfoSearch = customerInfoSearch;
    }

    public List<SelectItem> getTitleThList() {
        return titleThList;
    }

    public void setTitleThList(List<SelectItem> titleThList) {
        this.titleThList = titleThList;
    }

    public List<SelectItem> getTitleEnList() {
        return titleEnList;
    }

    public void setTitleEnList(List<SelectItem> titleEnList) {
        this.titleEnList = titleEnList;
    }

    public List<SelectItem> getBusinessTypeList() {
        return businessTypeList;
    }

    public void setBusinessTypeList(List<SelectItem> businessTypeList) {
        this.businessTypeList = businessTypeList;
    }

    public List<SelectItem> getProvinceForm1List() {
        return provinceForm1List;
    }

    public void setProvinceForm1List(List<SelectItem> provinceForm1List) {
        this.provinceForm1List = provinceForm1List;
    }

    public List<SelectItem> getDistrictForm1List() {
        return districtForm1List;
    }

    public void setDistrictForm1List(List<SelectItem> districtForm1List) {
        this.districtForm1List = districtForm1List;
    }

    public List<SelectItem> getSubDistrictForm1List() {
        return subDistrictForm1List;
    }

    public void setSubDistrictForm1List(List<SelectItem> subDistrictForm1List) {
        this.subDistrictForm1List = subDistrictForm1List;
    }

    public List<SelectItem> getProvinceForm2List() {
        return provinceForm2List;
    }

    public void setProvinceForm2List(List<SelectItem> provinceForm2List) {
        this.provinceForm2List = provinceForm2List;
    }

    public List<SelectItem> getDistrictForm2List() {
        return districtForm2List;
    }

    public void setDistrictForm2List(List<SelectItem> districtForm2List) {
        this.districtForm2List = districtForm2List;
    }

    public List<SelectItem> getSubDistrictForm2List() {
        return subDistrictForm2List;
    }

    public void setSubDistrictForm2List(List<SelectItem> subDistrictForm2List) {
        this.subDistrictForm2List = subDistrictForm2List;
    }

    public List<SelectItem> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<SelectItem> countryList) {
        this.countryList = countryList;
    }

    public List<SelectItem> getAddressTypeList() {
        return addressTypeList;
    }

    public void setAddressTypeList(List<SelectItem> addressTypeList) {
        this.addressTypeList = addressTypeList;
    }

    public List<SelectItem> getKycLevelList() {
        return kycLevelList;
    }

    public void setKycLevelList(List<SelectItem> kycLevelList) {
        this.kycLevelList = kycLevelList;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isEnableCitizenId() {
        return enableCitizenId;
    }

    public void setEnableCitizenId(boolean enableCitizenId) {
        this.enableCitizenId = enableCitizenId;
    }

    public boolean isEnableDocumentType() {
        return enableDocumentType;
    }

    public void setEnableDocumentType(boolean enableDocumentType) {
        this.enableDocumentType = enableDocumentType;
    }

    public boolean isEnableSpouseCitizenId() {
        return enableSpouseCitizenId;
    }

    public void setEnableSpouseCitizenId(boolean enableSpouseCitizenId) {
        this.enableSpouseCitizenId = enableSpouseCitizenId;
    }

    public boolean isEnableSpouseDocumentType() {
        return enableSpouseDocumentType;
    }

    public void setEnableSpouseDocumentType(boolean enableSpouseDocumentType) {
        this.enableSpouseDocumentType = enableSpouseDocumentType;
    }

    public List<String> getYearList() {
        return yearList;
    }

    public void setYearList(List<String> yearList) {
        this.yearList = yearList;
    }

    public CustomerInfoView getSelectEditIndividual() {
        return selectEditIndividual;
    }

    public void setSelectEditIndividual(CustomerInfoView selectEditIndividual) {
        this.selectEditIndividual = selectEditIndividual;
    }

    public boolean isEditBorrower() {
        return isEditBorrower;
    }

    public void setEditBorrower(boolean editBorrower) {
        isEditBorrower = editBorrower;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getCurrentDateDDMMYY() {
        log.debug("current date : {}", getCurrentDate());
        return  currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(getCurrentDate());
    }

    public void setCurrentDateDDMMYY(String currentDateDDMMYY) {
        this.currentDateDDMMYY = currentDateDDMMYY;
    }

    public boolean isReqRelation() {
        return reqRelation;
    }

    public void setReqRelation(boolean reqRelation) {
        this.reqRelation = reqRelation;
    }

    public boolean isReqReference() {
        return reqReference;
    }

    public void setReqReference(boolean reqReference) {
        this.reqReference = reqReference;
    }

    public boolean isReqDocType() {
        return reqDocType;
    }

    public void setReqDocType(boolean reqDocType) {
        this.reqDocType = reqDocType;
    }

    public boolean isReqRegId() {
        return reqRegId;
    }

    public void setReqRegId(boolean reqRegId) {
        this.reqRegId = reqRegId;
    }

    public boolean isReqTitTh() {
        return reqTitTh;
    }

    public void setReqTitTh(boolean reqTitTh) {
        this.reqTitTh = reqTitTh;
    }

    public boolean isReqStNameTh() {
        return reqStNameTh;
    }

    public void setReqStNameTh(boolean reqStNameTh) {
        this.reqStNameTh = reqStNameTh;
    }

    public boolean isReqTitEn() {
        return reqTitEn;
    }

    public void setReqTitEn(boolean reqTitEn) {
        this.reqTitEn = reqTitEn;
    }

    public boolean isReqStNameEn() {
        return reqStNameEn;
    }

    public void setReqStNameEn(boolean reqStNameEn) {
        this.reqStNameEn = reqStNameEn;
    }

    public boolean isReqBizType() {
        return reqBizType;
    }

    public void setReqBizType(boolean reqBizType) {
        this.reqBizType = reqBizType;
    }

    public boolean isReqRegNo() {
        return reqRegNo;
    }

    public void setReqRegNo(boolean reqRegNo) {
        this.reqRegNo = reqRegNo;
    }

    public boolean isReqRegPro() {
        return reqRegPro;
    }

    public void setReqRegPro(boolean reqRegPro) {
        this.reqRegPro = reqRegPro;
    }

    public boolean isReqRegDis() {
        return reqRegDis;
    }

    public void setReqRegDis(boolean reqRegDis) {
        this.reqRegDis = reqRegDis;
    }

    public boolean isReqRegSub() {
        return reqRegSub;
    }

    public void setReqRegSub(boolean reqRegSub) {
        this.reqRegSub = reqRegSub;
    }

    public boolean isReqRegPos() {
        return reqRegPos;
    }

    public void setReqRegPos(boolean reqRegPos) {
        this.reqRegPos = reqRegPos;
    }

    public boolean isReqRegCou() {
        return reqRegCou;
    }

    public void setReqRegCou(boolean reqRegCou) {
        this.reqRegCou = reqRegCou;
    }

    public boolean isReqRegPhone() {
        return reqRegPhone;
    }

    public void setReqRegPhone(boolean reqRegPhone) {
        this.reqRegPhone = reqRegPhone;
    }

    public boolean isReqWorNo() {
        return reqWorNo;
    }

    public void setReqWorNo(boolean reqWorNo) {
        this.reqWorNo = reqWorNo;
    }

    public boolean isReqWorPro() {
        return reqWorPro;
    }

    public void setReqWorPro(boolean reqWorPro) {
        this.reqWorPro = reqWorPro;
    }

    public boolean isReqWorDis() {
        return reqWorDis;
    }

    public void setReqWorDis(boolean reqWorDis) {
        this.reqWorDis = reqWorDis;
    }

    public boolean isReqWorSub() {
        return reqWorSub;
    }

    public void setReqWorSub(boolean reqWorSub) {
        this.reqWorSub = reqWorSub;
    }

    public boolean isReqWorPos() {
        return reqWorPos;
    }

    public void setReqWorPos(boolean reqWorPos) {
        this.reqWorPos = reqWorPos;
    }

    public boolean isReqWorCou() {
        return reqWorCou;
    }

    public void setReqWorCou(boolean reqWorCou) {
        this.reqWorCou = reqWorCou;
    }

    public boolean isReqWorPhone() {
        return reqWorPhone;
    }

    public void setReqWorPhone(boolean reqWorPhone) {
        this.reqWorPhone = reqWorPhone;
    }

    public boolean isReqMobNo() {
        return reqMobNo;
    }

    public void setReqMobNo(boolean reqMobNo) {
        this.reqMobNo = reqMobNo;
    }

    public boolean isReqAddMail() {
        return reqAddMail;
    }

    public void setReqAddMail(boolean reqAddMail) {
        this.reqAddMail = reqAddMail;
    }

    public boolean isReqKYCLev() {
        return reqKYCLev;
    }

    public void setReqKYCLev(boolean reqKYCLev) {
        this.reqKYCLev = reqKYCLev;
    }

    public boolean isEditForm() {
        return isEditForm;
    }

    public void setEditForm(boolean editForm) {
        isEditForm = editForm;
    }

    public boolean isEnableAllFieldCus() {
        return enableAllFieldCus;
    }

    public void setEnableAllFieldCus(boolean enableAllFieldCus) {
        this.enableAllFieldCus = enableAllFieldCus;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    public List<SelectItem> getIncomeSourceList() {
        return incomeSourceList;
    }

    public void setIncomeSourceList(List<SelectItem> incomeSourceList) {
        this.incomeSourceList = incomeSourceList;
    }
}
