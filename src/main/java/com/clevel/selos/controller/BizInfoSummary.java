package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.*;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.BankStatementSummaryDAO;
import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.BankStmtSummaryView;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.model.view.BizInfoSummaryView;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "bizInfoSummary")
public class BizInfoSummary implements Serializable {

    @NormalMessage
    @Inject
    Message msg;
    @Inject
    BankStmtControl bankStmtControl;

    private BizInfoSummaryView bizInfoSummaryView;
    private BizInfoDetailView selectBizInfoDetailView;
    private List<BizInfoDetailView> bizInfoDetailViewList;
    private List<BizInfoDetail> bizInfoDetailList;
    private List<Province> provinceList;
    private List<District> districtList;
    private List<Country> countryList;
    private List<SubDistrict> subDistrictList;
    private List<ReferredExperience> referredExperienceList;
    private Province province;
    private District district;
    private SubDistrict subDistrict;
    private Country country;
    private boolean fromDB;
    private boolean readonlyInterview;
    //private User user;
    private Date currentDate;
    private String currentDateDDMMYY;

    private ReferredExperience referredExperience;
    private String sumIncomeAmountDis;
    private String incomeAmountDis;
    private BigDecimal sumIncomeAmount;
    private BigDecimal sumIncomePercent;
    private BigDecimal SumWeightIntvIncomeFactor;
    private BigDecimal SumWeightAR;
    private BigDecimal SumWeightAP;
    private BigDecimal SumWeightINV;
    BigDecimal bankStatementAvg = BigDecimal.ZERO;
    private BankStmtSummaryView bankStmtSummaryView;

    private String messageHeader;
    private String message;
    private String redirect;
    private boolean disableOwnerName;
    private boolean disableExpiryDate;

    @Inject
    @SELOS
    Logger log;
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
    ExSummaryControl exSummaryControl;
    @Inject
    CreditFacProposeControl creditFacProposeControl;

    @Inject
    private Util util;

    private long workCaseId;

    public BizInfoSummary() {

    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation bizInfoSum");
        disableOwnerName = false;
        disableExpiryDate = true;

        HttpSession session = FacesUtil.getSession(true);

        if(!Util.isNull(session.getAttribute("workCaseId"))){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        }else{
            log.info("onCreation ::: workCaseId is null.");
            try{
                FacesUtil.redirect("/site/inbox.jsf");
                return;
            }catch (Exception ex){
                log.info("Exception :: {}",ex);
            }
        }

        log.debug("info WorkCaseId is: {}", workCaseId);

        onSearchBizInfoSummaryByWorkCase();

        provinceList = provinceDAO.getListOrderByParameter("name");
        countryList = countryDAO.findAll();
        referredExperienceList = referredExperienceDAO.findAll();
        bankStatementAvg = BigDecimal.ZERO;
        long stepId = 0;

        bankStmtSummaryView = bankStmtControl.getBankStmtSummaryByWorkCaseId(workCaseId);
        log.debug("bankStmtSummaryView : {}", bankStmtSummaryView);

        if(session.getAttribute("stepId") != null){
            stepId = Long.parseLong(session.getAttribute("stepId").toString());
        }

        log.debug("stepId : {}",stepId);

        BigDecimal Income = BigDecimal.ZERO;
        BigDecimal twenty = BigDecimal.valueOf(12);
        BigDecimal calSumIncomeNet = BigDecimal.ZERO;
        BigDecimal sumIncomeNet = BigDecimal.ZERO;

        if(!Util.isNull(bankStmtSummaryView)){
            if(!Util.isNull(bankStmtSummaryView.getGrdTotalIncomeGross())){
                bankStatementAvg = bankStmtSummaryView.getGrdTotalIncomeGross();

            }else{
                bankStatementAvg = BigDecimal.ZERO;
            }

            log.debug("bankStatementAvg : {} " + bankStatementAvg);

                if(stepId >= StepValue.CREDIT_DECISION_UW1.value()){
                    Income = bankStmtSummaryView.getGrdTotalIncomeNetUW();
                } else {
                    Income = bankStmtSummaryView.getGrdTotalIncomeNetBDM();
                }

            log.debug("Income : {} " + Income);

            if(!Util.isNull(Income)){
                calSumIncomeNet = Util.multiply(Income,twenty);
                sumIncomeNet = calSumIncomeNet.setScale(2,RoundingMode.HALF_UP);
            } else {
                calSumIncomeNet = BigDecimal.ZERO;
                sumIncomeNet = BigDecimal.ZERO;
            }
        } else {
            calSumIncomeNet = BigDecimal.ZERO;
            sumIncomeNet = BigDecimal.ZERO;
        }

        log.debug("sumIncomeNet : {} " + sumIncomeNet);

        if(Util.isNull(bizInfoSummaryView)) {
            fromDB = false;
            log.info("bizInfoSummaryView == null ");

            bizInfoSummaryView = new BizInfoSummaryView();

            province = new Province();
            district = new District();
            subDistrict = new SubDistrict();
            country = new Country();
            country.setId(211);
            referredExperience = new ReferredExperience();

            district.setProvince(province);
            subDistrict.setDistrict(district);
            subDistrict.setProvince(province);
            bizInfoSummaryView.setProvince(province);
            bizInfoSummaryView.setDistrict(district);
            bizInfoSummaryView.setSubDistrict(subDistrict);
            bizInfoSummaryView.setReferredExperience(referredExperience);
            bizInfoSummaryView.setCountry(country);

            bizInfoSummaryView.setSumIncomeAmount(BigDecimal.ZERO);
            bizInfoSummaryView.setSumIncomePercent(BigDecimal.ZERO);
            bizInfoSummaryView.setSumWeightAR(BigDecimal.ZERO);
            bizInfoSummaryView.setSumWeightAP(BigDecimal.ZERO);
            bizInfoSummaryView.setSumWeightINV(BigDecimal.ZERO);
            bizInfoSummaryView.setSumWeightInterviewedIncomeFactorPercent(BigDecimal.ZERO);
            bizInfoSummaryView.setCirculationAmount(bankStatementAvg);
            bizInfoSummaryView.setCirculationPercentage(new BigDecimal(100));
            bizInfoSummaryView.setSumIncomeAmount(sumIncomeNet);

        } else {
            fromDB = true;
            getBusinessInfoListDB();
            onChangeProvince();
            onChangeDistrict();
            onChangeRental();
            bizInfoSummaryView.setCirculationAmount(bankStatementAvg);
            onCalSummaryTable();
            bizInfoSummaryView.setCirculationPercentage(new BigDecimal(100));
            bizInfoSummaryView.setSumIncomeAmount(sumIncomeNet);
            if(Util.isZero(sumIncomeNet)){
                bizInfoSummaryView.setProductionCostsPercentage(BigDecimal.ZERO);
                bizInfoSummaryView.setProfitMarginPercentage(BigDecimal.ZERO);
                bizInfoSummaryView.setEarningsBeforeTaxPercentage(BigDecimal.ZERO);
                bizInfoSummaryView.setNetMarginPercentage(BigDecimal.ZERO);
            }

            log.info("SumIncomeAmount : {}",bizInfoSummaryView.getSumIncomeAmount());
        }
        onCheckInterview();
    }

    public void onSearchBizInfoSummaryByWorkCase() {
        log.info(" get FROM session workCaseId is " + workCaseId);
        try{
            bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);
            bankStmtSummaryView = bizInfoSummaryControl.getBankStmtSummary(workCaseId);
        }catch (Exception e){
            log.error("error onSearchBizInfoSummaryByWorkCase : ", e);
        }
    }

    public void onChangeProvince() {
        Province proSelect = bizInfoSummaryView.getProvince();
        log.info("onChangeProvince :::: Province  : {} ", proSelect);
        districtList = districtDAO.getListByProvince(proSelect);

        if(!fromDB){
            bizInfoSummaryView.setDistrict(new District());
        }
        log.info("onChangeProvince :::: districtList.size ::: {}", districtList.size());
        subDistrictList = new ArrayList<SubDistrict>();
    }

    public void onChangeDistrict() {
        District districtSelect = bizInfoSummaryView.getDistrict();
        log.debug("onChangeDistrict :::: district : {}", districtSelect);
        subDistrictList = subDistrictDAO.getListByDistrict(districtSelect);
//        fromDB = false;

        if(!fromDB){
            bizInfoSummaryView.setSubDistrict(new SubDistrict());
        }
        fromDB = false;

        log.info("onChangeDistrict :::: subDistrictList.size ::: {}", subDistrictList.size());
    }

    public void getBusinessInfoListDB() {

        long bizInfoSummaryViewId;
        bizInfoSummaryViewId = bizInfoSummaryView.getId();
        bizInfoDetailViewList = bizInfoSummaryControl.onGetBizInfoDetailViewByBizInfoSummary(bizInfoSummaryViewId);

        if(bizInfoDetailViewList.size()>0 && bizInfoSummaryView.getCirculationAmount().doubleValue()>0){
            onCalSummaryTable();
        }

    }

    private void onCheckInterview(){
        readonlyInterview = true;
        if(!Util.isNull(bizInfoSummaryView.getCirculationAmount())){
             if( bizInfoSummaryView.getCirculationAmount().doubleValue() > 0){
                 readonlyInterview = false;
            }
        }
        log.info(" readonlyInterview is " + readonlyInterview);

    }

    public void onCalSummaryTable(){
        log.info("onCalSummaryTable begin");
        BigDecimal sumIncomeAmount = BigDecimal.ZERO ;
        BigDecimal productCostAmount = BigDecimal.ZERO ;
        BigDecimal productCostPercent = BigDecimal.ZERO ;
        BigDecimal profitMarginAmount = BigDecimal.ZERO ;
        BigDecimal profitMarginPercent = BigDecimal.ZERO ;
        BigDecimal operatingExpenseAmount = BigDecimal.ZERO ;
        BigDecimal operatingExpensePercent = BigDecimal.ZERO ;
        BigDecimal earningsBeforeTaxAmount = BigDecimal.ZERO ;
        BigDecimal earningsBeforeTaxPercent = BigDecimal.ZERO ;
        BigDecimal reduceInterestAmount = BigDecimal.ZERO ;
        BigDecimal reduceTaxAmount = BigDecimal.ZERO ;
        BigDecimal reduceInterestPercent = BigDecimal.ZERO ;
        BigDecimal reduceTaxPercent = BigDecimal.ZERO ;
        BigDecimal netMarginAmount = BigDecimal.ZERO ;
        BigDecimal netMarginPercent = BigDecimal.ZERO ;
        BigDecimal hundred = new BigDecimal(100);

        if(!Util.isNull(bizInfoSummaryView.getCirculationAmount())){
            sumIncomeAmount = bizInfoSummaryView.getCirculationAmount();
        }

        if(!Util.isNull(bizInfoSummaryView.getProductionCostsPercentage())){
            productCostPercent = bizInfoSummaryView.getProductionCostsPercentage();
        }

        productCostAmount = Util.divide(Util.multiply(sumIncomeAmount,productCostPercent),100);
        bizInfoSummaryView.setProductionCostsAmount(productCostAmount);
        profitMarginPercent = Util.subtract(hundred,productCostPercent);
        profitMarginAmount = Util.divide(Util.multiply(sumIncomeAmount,profitMarginPercent),hundred);
        bizInfoSummaryView.setProfitMarginPercentage(profitMarginPercent);
        bizInfoSummaryView.setProfitMarginAmount(profitMarginAmount);

        if(!Util.isNull(bizInfoSummaryView.getOperatingExpenseAmount())){
            operatingExpenseAmount = bizInfoSummaryView.getOperatingExpenseAmount();
        }

        if(operatingExpenseAmount.compareTo(profitMarginAmount) > 0){
            bizInfoSummaryView.setOperatingExpenseAmount(BigDecimal.ZERO);
            operatingExpenseAmount = BigDecimal.ZERO;
            messageHeader = msg.get("app.bizInfoSummary.message.validate.header.fail");
            message = msg.get("app.bizInfoSummary.message.validate.overOperatingExpense.fail");
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

        operatingExpensePercent = Util.multiply(Util.divide(operatingExpenseAmount,sumIncomeAmount),hundred);
        bizInfoSummaryView.setOperatingExpensePercentage(operatingExpensePercent);

        earningsBeforeTaxAmount = Util.subtract(profitMarginAmount,operatingExpenseAmount);
        earningsBeforeTaxPercent = Util.subtract(profitMarginPercent,operatingExpensePercent);

        bizInfoSummaryView.setEarningsBeforeTaxAmount(earningsBeforeTaxAmount);
        bizInfoSummaryView.setEarningsBeforeTaxPercentage(earningsBeforeTaxPercent);

        if(!Util.isNull(bizInfoSummaryView.getReduceInterestAmount())){
            reduceInterestAmount = bizInfoSummaryView.getReduceInterestAmount();
        }

        if(!Util.isNull(bizInfoSummaryView.getReduceTaxAmount())){
            reduceTaxAmount = bizInfoSummaryView.getReduceTaxAmount();
        }

        if(reduceInterestAmount.compareTo(earningsBeforeTaxAmount) > 0){
            bizInfoSummaryView.setReduceInterestAmount(new BigDecimal(0));
            reduceInterestAmount = BigDecimal.ZERO;
            messageHeader = msg.get("app.bizInfoSummary.message.validate.header.fail");
            message = msg.get("app.bizInfoSummary.message.validate.overInterest.fail");
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

        if( reduceTaxAmount.compareTo(earningsBeforeTaxAmount) > 0){
            bizInfoSummaryView.setReduceTaxAmount(new BigDecimal(0));
            reduceTaxAmount = BigDecimal.ZERO;
            messageHeader = msg.get("app.bizInfoSummary.message.validate.header.fail");
            message = msg.get("app.bizInfoSummary.message.validate.overTax.fail");
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

        if( ((Util.add(reduceInterestAmount,reduceTaxAmount)).compareTo(earningsBeforeTaxAmount) > 0)){
            bizInfoSummaryView.setReduceTaxAmount(new BigDecimal(0));
            bizInfoSummaryView.setReduceInterestAmount(new BigDecimal(0));
            reduceInterestAmount = BigDecimal.ZERO;
            reduceTaxAmount = BigDecimal.ZERO;
            messageHeader = msg.get("app.bizInfoSummary.message.validate.header.fail");
            message = msg.get("app.bizInfoSummary.message.validate.overInterestAndTax.fail");
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }

        reduceInterestAmount = bizInfoSummaryView.getReduceInterestAmount();
        reduceTaxAmount = bizInfoSummaryView.getReduceTaxAmount();

        reduceInterestPercent = Util.multiply(Util.divide(reduceInterestAmount,sumIncomeAmount),hundred);
        reduceTaxPercent = Util.multiply(Util.divide(reduceTaxAmount,sumIncomeAmount),hundred);

        bizInfoSummaryView.setReduceInterestPercentage(reduceInterestPercent);
        bizInfoSummaryView.setReduceTaxPercentage(reduceTaxPercent);

        netMarginAmount = Util.subtract(Util.subtract(earningsBeforeTaxAmount,reduceInterestAmount),reduceTaxAmount);
        netMarginPercent = Util.subtract(Util.subtract(earningsBeforeTaxPercent,reduceInterestPercent),reduceTaxPercent);

        bizInfoSummaryView.setNetMarginAmount(netMarginAmount);
        bizInfoSummaryView.setNetMarginPercentage(netMarginPercent);
        log.info("onCalSummaryTable 333");

        log.info("onCalSummaryTable end");
    }

    public void onCheckSave(){
        log.info("have to redirect is " + redirect );
        if (redirect != null && !redirect.equals("")) {
            RequestContext.getCurrentInstance().execute("confirmAddBizInfoDetailDlg.show()");
        }
    }

    public void onSaveBizInfoSummary() {
        try {
            log.info("onSaveBizInfoSummary begin");
            HttpSession session = FacesUtil.getSession(true);
            session.setAttribute("bizInfoDetailViewId", -1);

            if (!Util.isNull(redirect)&& !redirect.equals("")) {
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }


            bizInfoSummaryControl.onSaveBizSummaryToDB(bizInfoSummaryView, workCaseId);
            exSummaryControl.calForBizInfoSummary(workCaseId);
            if (redirect != null && !redirect.equals("")) {
                if (redirect.equals("viewDetail")) {
                    log.info("view Detail ");
                    onViewDetail();
                }

                log.info("session.getAttribute('bizInfoDetailViewId') " + session.getAttribute("bizInfoDetailViewId"));

                String url = "bizInfoDetail.jsf";
                FacesUtil.redirect("/site/bizInfoDetail.jsf");
                /*FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();

                log.info("redirect to new page url is " + url);
                ec.redirect(ec.getRequestContextPath() + "/site/bizInfoDetail.jsf");*/
                //ec.redirect(url);
                log.info("redirect to new page goooo!! 1");
                return;
            } else {
                log.info("after redirect method");
                log.info("not have to redirect ");
                onCreation();
                log.info("onCreation() after Save");
                messageHeader = msg.get("app.bizInfoSummary.message.header.save.success");
                message = msg.get("app.bizInfoSummary.message.body.save.success");
                log.info("after set message");
                RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            }
        } catch (Exception ex) {
            log.info("onSaveBizInfoSummary Error : ", ex);

            messageHeader = msg.get("app.bizInfoSummary.message.header.save.fail");

            if (ex.getCause() != null) {
                //log.info("ex.getCause().toString() is " + ex.getCause().toString());
                message = msg.get("app.bizInfoSummary.message.body.save.fail") + ex.getCause().toString();
            } else {
                //log.info("ex.getCause().toString() is " + ex.getMessage());
                message = msg.get("app.bizInfoSummary.message.body.save.fail")  + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }
    }

    public void onViewDetail() {
        log.info(" onViewDetail begin !! {}");
        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("bizInfoDetailViewId", selectBizInfoDetailView.getId());
        log.info(" onViewDetail end !! {}");
    }

    public void onDeleteBizInfoToDB() {

        try {
            log.info("onDeleteBizInfoToDB Controller begin ");
            bizInfoDetailControl.onDeleteBizInfoToDB(selectBizInfoDetailView);
            getBusinessInfoListDB();
        } catch (Exception e) {

        } finally {
            log.info("onDeleteBizInfoToDB Controller end ");
        }
    }

    public void onChangeRental(){
        if(bizInfoSummaryView.getRental() == 0 ){
            disableExpiryDate = true;
            disableOwnerName = false;
            bizInfoSummaryView.setExpiryDate(null);
        }else if(bizInfoSummaryView.getRental() == 1 ){
            disableExpiryDate = false;
            disableOwnerName = true;
            bizInfoSummaryView.setOwnerName("");
        }
    }

    public void onCheckAdd(){
        redirect = "addDetail";
        onSaveBizInfoSummary();
    }

    public void onCheckEdit(){
        redirect = "viewDetail";
        onSaveBizInfoSummary();
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

    public boolean isDisableExpiryDate() {
        return disableExpiryDate;
    }

    public void setDisableExpiryDate(boolean disableExpiryDate) {
        this.disableExpiryDate = disableExpiryDate;
    }

    public boolean isDisableOwnerName() {
        return disableOwnerName;
    }

    public void setDisableOwnerName(boolean disableOwnerName) {
        this.disableOwnerName = disableOwnerName;
    }

    public boolean isReadonlyInterview() {
        return readonlyInterview;
    }

    public void setReadonlyInterview(boolean readonlyInterview) {
        this.readonlyInterview = readonlyInterview;
    }
}
