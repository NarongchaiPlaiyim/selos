package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BankStmtControl;
import com.clevel.selos.businesscontrol.BizInfoDetailControl;
import com.clevel.selos.businesscontrol.BizInfoSummaryControl;
import com.clevel.selos.businesscontrol.CalculationControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.BankStatementSummaryDAO;
import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ActionAudit;
import com.clevel.selos.model.ActionResult;
import com.clevel.selos.model.MessageDialogSeverity;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.model.view.BizInfoSummaryView;
import com.clevel.selos.system.audit.SLOSAuditor;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.transform.BankStmtTransform;
import com.clevel.selos.transform.BizInfoDetailTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "bizInfoSummary")
public class BizInfoSummary extends BaseController {

    @NormalMessage
    @Inject
    Message msg;

    @Inject
    @SELOS
    Logger log;

    @Inject
    private SLOSAuditor slosAuditor;

    @Inject
    private BusinessGroupDAO businessGroupDAO;
    @Inject
    private BusinessDescriptionDAO businessDescriptionDAO;
    @Inject
    private BizInfoDetailDAO bizInfoDetailDAO;
    @Inject
    private ProvinceDAO provinceDAO;
    @Inject
    private DistrictDAO districtDAO;
    @Inject
    private SubDistrictDAO subDistrictDAO;
    @Inject
    private ReferredExperienceDAO referredExperienceDAO;
    @Inject
    private BizInfoDetailControl bizInfoDetailControl;
    @Inject
    private BizInfoDetailTransform bizProductDetailTransform;
    @Inject
    private BizInfoSummaryControl bizInfoSummaryControl;
    @Inject
    private CountryDAO countryDAO;
    @Inject
    BankStmtTransform bankStmtTransform;
    @Inject
    BankStatementSummaryDAO bankStmtSummaryDAO;
    @Inject
    CalculationControl calculationControl;
    @Inject
    BankStmtControl bankStmtControl;

    private BizInfoSummaryView bizInfoSummaryView;
    private BizInfoDetailView selectBizInfoDetailView;
    private List<BizInfoDetailView> bizInfoDetailViewList;
    private List<BizInfoDetail> bizInfoDetailList;
    private List<Province> provinceList;
    private List<District> districtList;
    private List<SubDistrict> subDistrictList;
    private List<Country> countryList;
    private List<ReferredExperience> referredExperienceList;
    private boolean readonlyInterview;
    //private User user;
    private Date currentDate;
    private String currentDateDDMMYY;

    private String sumIncomeAmountDis;
    private String incomeAmountDis;
    private BigDecimal sumIncomeAmount;
    private BigDecimal sumIncomePercent;
    private BigDecimal SumWeightIntvIncomeFactor;
    private BigDecimal SumWeightAR;
    private BigDecimal SumWeightAP;
    private BigDecimal SumWeightINV;

    private String messageHeader;
    private String message;
    private String severity;

    private String redirect;

    private long workCaseId;
    private long stepId;

    private String userId;

    public BizInfoSummary() {
    }

    public boolean checkSession(HttpSession session){
        boolean checkSession = false;
        if( (Long)session.getAttribute("workCaseId") != 0){
            checkSession = true;
        }

        return checkSession;
    }


    public void preRender(){
        log.debug("preRender");
        HttpSession session = FacesUtil.getSession(false);

        if(checkSession(session)){
            //TODO Check valid step
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
            workCaseId = (Long)session.getAttribute("workCaseId");
            stepId = Util.parseLong(session.getAttribute("stepId"), 0);
            String ownerCaseUserId = Util.parseString(session.getAttribute("caseOwner"), "");

            loadFieldControl(workCaseId, Screen.BUSINESS_INFO_SUMMARY, ownerCaseUserId);

            setDisabledValue("ownerName",false);
            setDisabledValue("expiryDate",true);

            setMandateValue("registrationDate",true);
            setMandateValue("establishDate",true);

            bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);

            provinceList = provinceDAO.getListOrderByParameter("name");
            districtList = new ArrayList<District>();
            subDistrictList = new ArrayList<SubDistrict>();

            countryList = countryDAO.findAll();
            referredExperienceList = referredExperienceDAO.findAll();

            if(Util.isNull(bizInfoSummaryView)) {
                log.info("bizInfoSummaryView == null ");
                bizInfoSummaryView = new BizInfoSummaryView();

                Country country = new Country();

                country.setId(211);
                bizInfoSummaryView.setCountry(country);
                bizInfoSummaryView.setProvince(new Province());
                bizInfoSummaryView.setDistrict(new District());
                bizInfoSummaryView.setSubDistrict(new SubDistrict());
                bizInfoSummaryView.setReferredExperience(new ReferredExperience());
                bizInfoSummaryView.setSumIncomeAmount(BigDecimal.ZERO);
                bizInfoSummaryView.setSumIncomePercent(BigDecimal.ZERO);
                bizInfoSummaryView.setSumWeightAR(BigDecimal.ZERO);
                bizInfoSummaryView.setSumWeightAP(BigDecimal.ZERO);
                bizInfoSummaryView.setSumWeightINV(BigDecimal.ZERO);
                bizInfoSummaryView.setSumWeightInterviewedIncomeFactorPercent(BigDecimal.ZERO);
                bizInfoSummaryView.setCirculationAmount(BigDecimal.ZERO);
                bizInfoSummaryView.setCirculationPercentage(new BigDecimal(100));
                bizInfoSummaryView.setWeightIncomeFactor(BigDecimal.ZERO);
            } else {
                log.info("bizInfoSummaryView != null ");
                getBusinessInfoListDB();
                onChangeProvinceEdit();
                onChangeDistrictEdit();
                onChangeRental();
                onCalSummaryTable();
                bizInfoSummaryView.setCirculationPercentage(new BigDecimal(100));
            }
            onCheckInterview();
            onChangeEstablishDate();

            slosAuditor.add(Screen.BUSINESS_INFO_SUMMARY.value(), userId, ActionAudit.ON_CREATION, "", date, ActionResult.SUCCESS, "");
        } else {
            slosAuditor.add(Screen.BUSINESS_INFO_SUMMARY.value(), userId, ActionAudit.ON_CREATION, "", date, ActionResult.FAILED, "Invalid Session");

            log.debug("No session for case found. Redirect to Inbox");
            FacesUtil.redirect("/site/inbox.jsf");
        }
    }

    public void onChangeProvince() {
        log.info("onChangeProvince :::: Province  : {} ", bizInfoSummaryView.getProvince());
        if(bizInfoSummaryView.getProvince() != null && bizInfoSummaryView.getProvince().getCode() != 0){
            Province province = provinceDAO.findById(bizInfoSummaryView.getProvince().getCode());
            districtList = districtDAO.getListByProvince(province);
            bizInfoSummaryView.setDistrict(new District());
            subDistrictList = new ArrayList<SubDistrict>();
            bizInfoSummaryView.setSubDistrict(new SubDistrict());
        }else{
            districtList = new ArrayList<District>();
            subDistrictList = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrict() {
        log.debug("onChangeDistrict :::: District : {}", bizInfoSummaryView.getDistrict());
        if(bizInfoSummaryView.getDistrict() != null && bizInfoSummaryView.getDistrict().getId() != 0){
            District district = districtDAO.findById(bizInfoSummaryView.getDistrict().getId());
            subDistrictList = subDistrictDAO.getListByDistrict(district);
            bizInfoSummaryView.setSubDistrict(new SubDistrict());
        }else{
            onChangeProvince();
            subDistrictList = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeProvinceEdit(){
        log.info("onChangeProvinceEdit :::: Province  : {} ", bizInfoSummaryView.getProvince());
        if(bizInfoSummaryView.getProvince() != null && bizInfoSummaryView.getProvince().getCode() != 0){
            Province province = provinceDAO.findById(bizInfoSummaryView.getProvince().getCode());
            districtList = districtDAO.getListByProvince(province);
        }else{
            districtList = new ArrayList<District>();
            subDistrictList = new ArrayList<SubDistrict>();
        }
    }

    public void onChangeDistrictEdit(){
        log.debug("onChangeDistrictEdit :::: District : {}", bizInfoSummaryView.getDistrict());
        if(bizInfoSummaryView.getDistrict() != null && bizInfoSummaryView.getDistrict().getId() != 0){
            District district = districtDAO.findById(bizInfoSummaryView.getDistrict().getId());
            subDistrictList = subDistrictDAO.getListByDistrict(district);
        }else{
            subDistrictList = new ArrayList<SubDistrict>();
        }
    }

    public void getBusinessInfoListDB() {
        long bizInfoSummaryViewId;
        bizInfoSummaryViewId = bizInfoSummaryView.getId();
        bizInfoDetailViewList = bizInfoSummaryControl.onGetBizInfoDetailViewByBizInfoSummary(bizInfoSummaryViewId);
        log.debug("getBusinessInfoListDB ::: bizInfoDetailViewList : {}", bizInfoDetailViewList);

        if(Util.isSafetyList(bizInfoDetailViewList)){
            if(!Util.isNull( bizInfoSummaryView.getCirculationAmount())){
                if (bizInfoSummaryView.getCirculationAmount().compareTo(BigDecimal.ZERO) > 0){
                    onCalSummaryTable();
                }
            }
        }

    }

    private void onCheckInterview(){
        readonlyInterview = true;
        if(!Util.isNull(bizInfoSummaryView.getCirculationAmount())){
            if (bizInfoSummaryView.getCirculationAmount().compareTo(BigDecimal.ZERO) > 0){
                 readonlyInterview = false;
            }
        }
        log.info(" readonlyInterview is " + readonlyInterview);

    }

    public void onCalSummaryTable(){
        log.info("onCalSummaryTable begin");
        bizInfoSummaryView = bizInfoSummaryControl.calSummaryTable(bizInfoSummaryView);

        BigDecimal operatingExpenseAmount = BigDecimal.ZERO;
        BigDecimal profitMarginAmount = BigDecimal.ZERO;
        BigDecimal reduceInterestAmount = BigDecimal.ZERO;
        BigDecimal earningsBeforeTaxAmount;
        BigDecimal reduceTaxAmount = BigDecimal.ZERO;

        if(!Util.isNull(bizInfoSummaryView.getOperatingExpenseAmount())){
            operatingExpenseAmount = bizInfoSummaryView.getOperatingExpenseAmount();
        }

        if(!Util.isNull(bizInfoSummaryView.getProfitMarginAmount())){
            profitMarginAmount = bizInfoSummaryView.getProfitMarginAmount();
        }

        if(!Util.isNull(bizInfoSummaryView.getReduceInterestAmount())){
            reduceInterestAmount = bizInfoSummaryView.getReduceInterestAmount();
        }

        if(!Util.isNull(bizInfoSummaryView.getReduceTaxAmount())){
            reduceTaxAmount = bizInfoSummaryView.getReduceTaxAmount();
        }

        earningsBeforeTaxAmount = Util.subtract(profitMarginAmount,operatingExpenseAmount);

        if(operatingExpenseAmount.compareTo(profitMarginAmount) > 0){
            if(profitMarginAmount.compareTo(BigDecimal.ZERO) > 0) {
                bizInfoSummaryView.setOperatingExpenseAmount(BigDecimal.ZERO);
                onCalSummaryTable();
                messageHeader = msg.get("app.bizInfoSummary.message.validate.header.fail");
                message = msg.get("app.bizInfoSummary.message.validate.overOperatingExpense.fail");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            } else {
                bizInfoSummaryView.setOperatingExpenseAmount(profitMarginAmount);
                onCalSummaryTable();
                messageHeader = msg.get("app.bizInfoSummary.message.validate.header.fail");
                message = msg.get("app.bizInfoSummary.message.validate.overOperatingExpense.fail");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        }

        if(reduceInterestAmount.compareTo(earningsBeforeTaxAmount) > 0){
            if(earningsBeforeTaxAmount.compareTo(BigDecimal.ZERO) > 0) {
                bizInfoSummaryView.setReduceInterestAmount(BigDecimal.ZERO);
                onCalSummaryTable();
                messageHeader = msg.get("app.bizInfoSummary.message.validate.header.fail");
                message = msg.get("app.bizInfoSummary.message.validate.overInterest.fail");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            } else {
                bizInfoSummaryView.setReduceInterestAmount(earningsBeforeTaxAmount);
                onCalSummaryTable();
                messageHeader = msg.get("app.bizInfoSummary.message.validate.header.fail");
                message = msg.get("app.bizInfoSummary.message.validate.overInterest.fail");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        }

        if(reduceTaxAmount.compareTo(earningsBeforeTaxAmount) > 0){
            if(earningsBeforeTaxAmount.compareTo(BigDecimal.ZERO) > 0) {
                bizInfoSummaryView.setReduceTaxAmount(BigDecimal.ZERO);
                onCalSummaryTable();
                messageHeader = msg.get("app.bizInfoSummary.message.validate.header.fail");
                message = msg.get("app.bizInfoSummary.message.validate.overTax.fail");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            } else {
                bizInfoSummaryView.setReduceTaxAmount(earningsBeforeTaxAmount);
                onCalSummaryTable();
                messageHeader = msg.get("app.bizInfoSummary.message.validate.header.fail");
                message = msg.get("app.bizInfoSummary.message.validate.overTax.fail");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        }

        if((Util.add(reduceInterestAmount,reduceTaxAmount)).compareTo(earningsBeforeTaxAmount) > 0){
            bizInfoSummaryView.setReduceTaxAmount(BigDecimal.ZERO);
            bizInfoSummaryView.setReduceInterestAmount(BigDecimal.ZERO);
            onCalSummaryTable();
            messageHeader = msg.get("app.bizInfoSummary.message.validate.header.fail");
            message = msg.get("app.bizInfoSummary.message.validate.overInterestAndTax.fail");
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
        log.info("onCalSummaryTable end");
    }

    private void onAddBizInfoDetail(){
        try {
            HttpSession session = FacesUtil.getSession(true);
            session.setAttribute("bizInfoDetailViewId", -1);
            FacesUtil.redirect("/site/bizInfoDetail.jsf");

            slosAuditor.add(Screen.BUSINESS_INFO_SUMMARY.value(), userId, ActionAudit.ON_ADD, "On Add Business Info Detail", new Date(), ActionResult.SUCCESS, "");
        } catch (Exception ex) {
            log.error("onAddBizInfoDetail :: Exception : {}", Util.getMessageException(ex));
            slosAuditor.add(Screen.BUSINESS_INFO_SUMMARY.value(), userId, ActionAudit.ON_ADD, "On Add Business Info Detail", new Date(), ActionResult.FAILED, Util.getMessageException(ex));
        }
    }

    public void onEditBizInfoDetail() {
        try {
            HttpSession session = FacesUtil.getSession(true);
            session.setAttribute("bizInfoDetailViewId", selectBizInfoDetailView.getId());
            FacesUtil.redirect("/site/bizInfoDetail.jsf");

            slosAuditor.add(Screen.BUSINESS_INFO_SUMMARY.value(), userId, ActionAudit.ON_EDIT, "On Edit Business Info Detail", new Date(), ActionResult.SUCCESS, "");
        } catch (Exception ex) {
            log.error("onEditBizInfoDetail :: Exception : {}", Util.getMessageException(ex));
            slosAuditor.add(Screen.BUSINESS_INFO_SUMMARY.value(), userId, ActionAudit.ON_EDIT, "On Edit Business Info Detail", new Date(), ActionResult.FAILED, Util.getMessageException(ex));
        }
    }

    public void onSaveBizInfoSummary() {
        Date date = new Date();
        try {
            log.info("onSaveBizInfoSummary begin");

            bizInfoSummaryControl.onSaveBizSummaryToDB(bizInfoSummaryView, workCaseId);
            calculationControl.calForBizInfoSummary(workCaseId);

            slosAuditor.add(Screen.BUSINESS_INFO_SUMMARY.value(), userId, ActionAudit.ON_SAVE, "", date, ActionResult.SUCCESS, "");

            onCreation();

            messageHeader = msg.get("app.messageHeader.info");
            message = msg.get("app.tcg.response.save.success");
            severity = MessageDialogSeverity.INFO.severity();
        } catch(Exception ex) {
            log.error("Business Info Summary :: Exception : {}", Util.getMessageException(ex));
            slosAuditor.add(Screen.BUSINESS_INFO_SUMMARY.value(), userId, ActionAudit.ON_SAVE, "", date, ActionResult.FAILED, Util.getMessageException(ex));

            messageHeader = msg.get("app.messageHeader.error");
            message = "Save business info summary data failed. Cause : " + Util.getMessageException(ex);
            severity = MessageDialogSeverity.ALERT.severity();
        }
        RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
    }

    public void onCancelBizInfoSummary() {
        slosAuditor.add(Screen.BUSINESS_INFO_SUMMARY.value(), userId, ActionAudit.ON_CANCEL, "", new Date(), ActionResult.SUCCESS, "");

        onCreation();
    }

    public void onDeleteBizInfoToDB() {
        Date date = new Date();
        try {
            bizInfoDetailControl.onDeleteBizInfoToDB(selectBizInfoDetailView);
            bizInfoDetailControl.onSaveSumOnSummary(bizInfoSummaryView.getId(),workCaseId,stepId);

            slosAuditor.add(Screen.BUSINESS_INFO_SUMMARY.value(), userId, ActionAudit.ON_DELETE, "On Delete Business Info Detail :: Business Info Detail ID :: " + selectBizInfoDetailView.getId(), date, ActionResult.SUCCESS, "");

            onCreation();
        } catch (Exception ex) {
            log.error("onDeleteBizInfoToDB :: Exception : {}", Util.getMessageException(ex));
            slosAuditor.add(Screen.BUSINESS_INFO_SUMMARY.value(), userId, ActionAudit.ON_DELETE, "On Delete Business Info Detail", date, ActionResult.FAILED, Util.getMessageException(ex));
        }
    }

    public void onChangeRental(){
        if(bizInfoSummaryView.getRental() == 0 ){
            setDisabledValue("ownerName", false);
            setMandateValue("ownerName", true);
            setDisabledValue("expiryDate", true);
            setMandateValue("expiryDate", false);
            bizInfoSummaryView.setExpiryDate(null);
        }else if(bizInfoSummaryView.getRental() == 1 ){
            setDisabledValue("ownerName", true);
            setMandateValue("ownerName", false);
            setDisabledValue("expiryDate", false);
            setMandateValue("expiryDate", true);
            bizInfoSummaryView.setOwnerName("");
        }
    }

    public void onChangeEstablishDate(){
        if(bizInfoSummaryView.getEstablishDate() != null ) {
            setMandateValue("establishFrom",true);
            referredExperienceList = referredExperienceDAO.findAll();
        } else {
            setMandateValue("establishFrom",false);
            referredExperienceList = new ArrayList<ReferredExperience>();
            referredExperienceList = referredExperienceDAO.findAll();
        }
    }

    public List<BizInfoDetailView> getBizInfoDetailViewList() {
        return bizInfoDetailViewList;
    }

    public void setBizInfoDetailViewList(List<BizInfoDetailView> bizInfoDetailViewList) {
        this.bizInfoDetailViewList = bizInfoDetailViewList;
    }

    public List<Province> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    public List<District> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<District> districtList) {
        this.districtList = districtList;
    }

    public List<SubDistrict> getSubDistrictList() {
        return subDistrictList;
    }

    public void setSubDistrictList(List<SubDistrict> subDistrictList) {
        this.subDistrictList = subDistrictList;
    }

    public List<BizInfoDetail> getBizInfoDetailList() {
        return bizInfoDetailList;
    }

    public void setBizInfoDetailList(List<BizInfoDetail> bizInfoDetailList) {
        this.bizInfoDetailList = bizInfoDetailList;
    }

    public BizInfoSummaryView getBizInfoSummaryView() {
        return bizInfoSummaryView;
    }

    public void setBizInfoSummaryView(BizInfoSummaryView bizInfoSummaryView) {
        this.bizInfoSummaryView = bizInfoSummaryView;
    }

    public BizInfoDetailView getSelectBizInfoDetailView() {
        return selectBizInfoDetailView;
    }

    public void setSelectBizInfoDetailView(BizInfoDetailView selectBizInfoDetailView) {
        this.selectBizInfoDetailView = selectBizInfoDetailView;
    }

    public List<ReferredExperience> getReferredExperienceList() {
        return referredExperienceList;
    }

    public void setReferredExperienceList(List<ReferredExperience> referredExperienceList) {
        this.referredExperienceList = referredExperienceList;
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

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public BigDecimal getSumIncomeAmount() {
        return sumIncomeAmount;
    }

    public void setSumIncomeAmount(BigDecimal sumIncomeAmount) {
        this.sumIncomeAmount = sumIncomeAmount;
    }

    public BigDecimal getSumIncomePercent() {
        return sumIncomePercent;
    }

    public void setSumIncomePercent(BigDecimal sumIncomePercent) {
        this.sumIncomePercent = sumIncomePercent;
    }

    public BigDecimal getSumWeightAR() {
        return SumWeightAR;
    }

    public void setSumWeightAR(BigDecimal SumWeightAR) {
        this.SumWeightAR = SumWeightAR;
    }

    public BigDecimal getSumWeightIntvIncomeFactor() {
        return SumWeightIntvIncomeFactor;
    }

    public void setSumWeightIntvIncomeFactor(BigDecimal SumWeightIntvIncomeFactor) {
        this.SumWeightIntvIncomeFactor = SumWeightIntvIncomeFactor;
    }

    public BigDecimal getSumWeightAP() {
        return SumWeightAP;
    }

    public void setSumWeightAP(BigDecimal SumWeightAP) {
        this.SumWeightAP = SumWeightAP;
    }

    public BigDecimal getSumWeightINV() {
        return SumWeightINV;
    }

    public void setSumWeightINV(BigDecimal SumWeightINV) {
        this.SumWeightINV = SumWeightINV;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public String getSumIncomeAmountDis() {
        return sumIncomeAmountDis;
    }

    public void setSumIncomeAmountDis(String sumIncomeAmountDis) {
        this.sumIncomeAmountDis = sumIncomeAmountDis;
    }

    public String getIncomeAmountDis() {
        return incomeAmountDis;
    }

    public void setIncomeAmountDis(String incomeAmountDis) {
        this.incomeAmountDis = incomeAmountDis;
    }

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentDateDDMMYY() {
        log.debug("current date : {}", getCurrentDate());
        return  currentDateDDMMYY = DateTimeUtil.convertToStringDDMMYYYY(getCurrentDate());
    }

    public void setCurrentDateDDMMYY(String currentDateDDMMYY) {
        this.currentDateDDMMYY = currentDateDDMMYY;
    }

    public boolean isReadonlyInterview() {
        return readonlyInterview;
    }

    public void setReadonlyInterview(boolean readonlyInterview) {
        this.readonlyInterview = readonlyInterview;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
