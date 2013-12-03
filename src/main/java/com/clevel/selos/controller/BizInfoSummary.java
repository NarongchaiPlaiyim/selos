package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BizInfoDetailControl;
import com.clevel.selos.businesscontrol.BizInfoSummaryControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.BankStatementSummaryDAO;
import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RoleUser;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.BankStmtSummaryView;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.model.view.BizInfoSummaryView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.transform.BankStmtTransform;
import com.clevel.selos.transform.BizInfoDetailTransform;
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
    //private User user;
    private Date currentDate;

    private ReferredExperience referredExperience;
    private String sumIncomeAmountDis;
    private BigDecimal sumIncomeAmount;
    private BigDecimal sumIncomePercent;
    private BigDecimal SumWeightIntvIncomeFactor;
    private BigDecimal SumWeightAR;
    private BigDecimal SumWeightAP;
    private BigDecimal SumWeightINV;
    double bankStatementAvg = 0;
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
    private Util util;

    private long workCaseId;

    public BizInfoSummary() {

    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation bizInfoSum");
        HttpSession session = FacesUtil.getSession(true);
        log.info("info WorkCase : {}", session.getAttribute("workCaseId"));
        disableOwnerName = false;
        disableExpiryDate = true;
        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        }
        log.debug("info WorkCaseId is: {}" + workCaseId);
        onSearchBizInfoSummaryByWorkCase();


        provinceList = provinceDAO.getListOrderByParameter("name");
        countryList = countryDAO.findAll();
        referredExperienceList = referredExperienceDAO.findAll();

        //user = (User)session.getAttribute("user");

        //log.info("onCreation bizInfoSum " + user.getRole().toString());

        //user.getRole().setId(102);

        bankStatementAvg = 0;
        if(bankStmtSummaryView != null ){

            /*if(RoleUser.ABDM.getValue() == user.getRole().getId()){
                if(bankStmtSummaryView.getGrdTotalIncomeNetBDM()!= null ){
                    bankStatementAvg = bankStmtSummaryView.getGrdTotalIncomeNetBDM().doubleValue();
                }
            }else if(RoleUser.BDM.getValue() == user.getRole().getId()){
                if(bankStmtSummaryView.getGrdTotalIncomeNetUW()!= null ){
                    bankStatementAvg = bankStmtSummaryView.getGrdTotalIncomeNetUW().doubleValue();
                }
            }*/

            if(bankStmtSummaryView.getGrdTotalIncomeNetUW()!= null ){
                bankStatementAvg = bankStmtSummaryView.getGrdTotalIncomeNetUW().doubleValue();
            }else{
                if(bankStmtSummaryView.getGrdTotalIncomeNetBDM()!= null ){
                    bankStatementAvg = bankStmtSummaryView.getGrdTotalIncomeNetBDM().doubleValue();
                }
            }
        }else{
           log.info("bankStmtSummaryView is null ");
        }

        log.info("bankStatementAvg is " + bankStatementAvg);

        if (bizInfoSummaryView == null) {

            log.info("bizInfoSummaryView == null ");

            bizInfoSummaryView = new BizInfoSummaryView();

            province = new Province();
            district = new District();
            subDistrict = new SubDistrict();
            country = new Country();
            referredExperience = new ReferredExperience();

            district.setProvince(province);
            subDistrict.setDistrict(district);
            subDistrict.setProvince(province);
            bizInfoSummaryView.setSubDistrict(subDistrict);
            bizInfoSummaryView.setReferredExperience(referredExperience);
            bizInfoSummaryView.setCountry(country);

            bizInfoSummaryView.setSumIncomeAmount(new BigDecimal(0));
            bizInfoSummaryView.setSumIncomePercent(new BigDecimal(0));
            bizInfoSummaryView.setSumWeightAR(new BigDecimal(0));
            bizInfoSummaryView.setSumWeightAP(new BigDecimal(0));
            bizInfoSummaryView.setSumWeightINV(new BigDecimal(0));
            bizInfoSummaryView.setSumWeightInterviewedIncomeFactorPercent(new BigDecimal(0));

        } else {
            getBusinessInfoListDB();
            onChangeProvince();
            onChangeDistrict();
            onChangeRental();
        }
    }

    public void onSearchBizInfoSummaryByWorkCase() {
        log.info(" Initial session ");
        HttpSession session = FacesUtil.getSession(true);
        log.info(" Initial session is " + session);

        //session.setAttribute("workCaseId", 10001);

        log.info(" get FROM session workCaseId is " + workCaseId);
        try{
            bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);
            bankStmtSummaryView = bizInfoSummaryControl.getBankStmtSummary(workCaseId);
        }catch (Exception e){
            log.info("error");

        }

        log.info(" get FROM session setGrdTotalIncomeNetBDM is " + bankStmtSummaryView.getGrdTotalIncomeNetBDM());
        log.info(" get FROM session setGrdTotalIncomeNetUW is  " + bankStmtSummaryView.getGrdTotalIncomeNetUW());

    }

    public void onChangeProvince() {
        Province proSelect = bizInfoSummaryView.getSubDistrict().getDistrict().getProvince();
        districtList = districtDAO.getListByProvince(proSelect);
        log.info("onChangeProvince :::: districtList.size ::: ", districtList.size());
    }

    public void onChangeDistrict() {
        District districtSelect = bizInfoSummaryView.getSubDistrict().getDistrict();
        subDistrictList = subDistrictDAO.getListByDistrict(districtSelect);
        log.info("onChangeDistrict :::: subDistrictList.size ::: ", subDistrictList.size());
    }


    public void getBusinessInfoListDB() {

        long bizInfoSummaryViewId;
        bizInfoSummaryViewId = bizInfoSummaryView.getId();
        bizInfoDetailViewList = bizInfoSummaryControl.onGetBizInfoDetailByBizInfoSummary(bizInfoSummaryViewId);


        if (bizInfoDetailViewList.size() == 0) {
            bizInfoDetailViewList = new ArrayList<BizInfoDetailView>();
        } else {

            double incomeAmountCal = 0;
            double sumIncomeAmountD = 0;

            double sumIncomePercentD = 0;
            double incomePercentD = 0;

            double adjustIncome = 0;
            double adjustIncomeCal = 0;
            double sumAdjust = 0;

            long ar = 0;
            double arCal = 0;
            double sumAR = 0;

            long ap = 0;
            double apCal = 0;
            double sumAP = 0;

            long inv = 0;
            double invCal = 0;
            double sumINV = 0;

            for (int i = 0; i < bizInfoDetailViewList.size(); i++) {

                BizInfoDetailView temp = bizInfoDetailViewList.get(i);

                incomePercentD = temp.getPercentBiz().doubleValue();
                sumIncomePercentD += incomePercentD;
                incomeAmountCal = bankStatementAvg * 12;
                sumIncomeAmountD += incomeAmountCal;


                adjustIncome = temp.getAdjustedIncomeFactor().doubleValue();
                adjustIncomeCal = (adjustIncome * incomePercentD) / 100;
                sumAdjust += adjustIncomeCal;

                ar = temp.getBizDesc().getAr();
                arCal = (ar * incomePercentD) / 100;
                sumAR += arCal;

                ap = temp.getBizDesc().getAp();
                apCal = (ap * incomePercentD) / 100;
                sumAP += apCal;

                inv = temp.getBizDesc().getInv();

                invCal = (inv * incomePercentD) / 100;
                sumINV += invCal;

            }

            sumIncomeAmountDis = util.formatNumber(sumIncomeAmountD);

            sumIncomeAmount = new BigDecimal(sumIncomeAmountD).setScale(2, RoundingMode.HALF_UP);
            sumIncomePercent = new BigDecimal(sumIncomePercentD).setScale(2,RoundingMode.HALF_UP);
            SumWeightAR = new BigDecimal(sumAR).setScale(2,RoundingMode.HALF_UP);
            SumWeightAP = new BigDecimal(sumAP).setScale(2,RoundingMode.HALF_UP);
            SumWeightINV = new BigDecimal(sumINV).setScale(2,RoundingMode.HALF_UP);
            SumWeightIntvIncomeFactor = new BigDecimal(sumAdjust).setScale(2,RoundingMode.HALF_UP);

            bizInfoSummaryView.setCirculationAmount(sumIncomeAmount);
            bizInfoSummaryView.setCirculationPercentage(new BigDecimal(100));
            bizInfoSummaryView.setSumIncomeAmount(sumIncomeAmount);
            bizInfoSummaryView.setSumIncomePercent(sumIncomePercent);
            bizInfoSummaryView.setSumWeightAR(SumWeightAR);
            bizInfoSummaryView.setSumWeightAP(SumWeightAP);
            bizInfoSummaryView.setSumWeightINV(SumWeightINV);
            bizInfoSummaryView.setSumWeightInterviewedIncomeFactorPercent(SumWeightIntvIncomeFactor);
            if(bizInfoDetailViewList.size()>0 && bizInfoSummaryView.getCirculationAmount().doubleValue()>0){
                onCalSummaryTable();
            }
        }
    }

    public void onCalSummaryTable(){
        log.info("onCalSummaryTable begin");
        if( bizInfoSummaryView.getCirculationAmount().doubleValue() <= 0){
            messageHeader = "error";
            message = "sumIncomeAmount is zero ";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }
        double sumIncomeAmount = 0;
        double productCostAmount = 0;
        double productCostPercent = 0;
        double profitMarginAmount = 0;
        double profitMarginPercent = 0;
        double operatingExpenseAmount = 0;
        double operatingExpensePercent = 0;
        double earningsBeforeTaxAmount = 0;
        double earningsBeforeTaxPercent = 0;
        double reduceInterestAmount = 0;
        double reduceTaxAmount = 0;
        double reduceInterestPercent = 0;
        double reduceTaxPercent = 0;
        double netMarginAmount = 0;
        double netMarginPercent = 0;

        if(bizInfoSummaryView.getCirculationAmount()!= null){
            sumIncomeAmount = bizInfoSummaryView.getCirculationAmount().doubleValue();
        }

        if(bizInfoSummaryView.getProductionCostsPercentage()!= null){
            productCostPercent = bizInfoSummaryView.getProductionCostsPercentage().doubleValue();
        }

        if( productCostPercent > 100.01){
            bizInfoSummaryView.setProductionCostsPercentage(new BigDecimal(0));
            messageHeader = "error";
            message = "error 100";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }
        
        productCostAmount = (sumIncomeAmount * productCostPercent)/100;
        bizInfoSummaryView.setProductionCostsAmount(new BigDecimal(productCostAmount));
        profitMarginPercent = 100 - productCostPercent;
        profitMarginAmount = (sumIncomeAmount * profitMarginPercent)/100;
        bizInfoSummaryView.setProfitMarginPercentage(new BigDecimal(profitMarginPercent));
        bizInfoSummaryView.setProfitMarginAmount(new BigDecimal(profitMarginAmount));

        log.info("onCalSummaryTable 111");

        if(bizInfoSummaryView.getOperatingExpenseAmount()!= null){
            operatingExpenseAmount = bizInfoSummaryView.getOperatingExpenseAmount().doubleValue();
        }

        if( operatingExpenseAmount > profitMarginAmount){
            bizInfoSummaryView.setProductionCostsPercentage(new BigDecimal(0));
            messageHeader = "error";
            message = "operatingExpenseAmount > profitMarginPercent";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        operatingExpensePercent = (operatingExpenseAmount/sumIncomeAmount)*100;
        bizInfoSummaryView.setOperatingExpensePercentage(new BigDecimal(operatingExpensePercent));

        earningsBeforeTaxAmount = profitMarginAmount- operatingExpenseAmount;
        earningsBeforeTaxPercent = profitMarginPercent - operatingExpensePercent;

        bizInfoSummaryView.setEarningsBeforeTaxAmount(new BigDecimal(earningsBeforeTaxAmount));
        bizInfoSummaryView.setEarningsBeforeTaxPercentage(new BigDecimal(earningsBeforeTaxPercent));

        log.info("onCalSummaryTable 222");
        if(bizInfoSummaryView.getReduceInterestAmount()!= null){
            reduceInterestAmount = bizInfoSummaryView.getReduceInterestAmount().doubleValue();
        }

        if(bizInfoSummaryView.getReduceTaxAmount()!= null){
            reduceTaxAmount = bizInfoSummaryView.getReduceTaxAmount().doubleValue();
        }

        if( reduceInterestAmount > earningsBeforeTaxAmount){
            bizInfoSummaryView.setReduceInterestAmount(new BigDecimal(0));
            messageHeader = "error";
            message = "error Interest > earningsBeforeTaxAmount";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        if( reduceTaxAmount > earningsBeforeTaxAmount){
            bizInfoSummaryView.setReduceTaxAmount(new BigDecimal(0));
            messageHeader = "error";
            message = "error tax > earningsBeforeTaxAmount";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        if( (reduceInterestAmount + reduceTaxAmount) > earningsBeforeTaxAmount){
            bizInfoSummaryView.setReduceTaxAmount(new BigDecimal(0));
            bizInfoSummaryView.setReduceInterestAmount(new BigDecimal(0));
            messageHeader = "error";
            message = "error  interest and tax > earningsBeforeTaxAmount";
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
            return;
        }

        reduceInterestAmount = bizInfoSummaryView.getReduceInterestAmount().doubleValue();
        reduceTaxAmount = bizInfoSummaryView.getReduceTaxAmount().doubleValue();

        reduceInterestPercent = (reduceInterestAmount/sumIncomeAmount)*100;
        reduceTaxPercent = (reduceTaxAmount/sumIncomeAmount)*100;

        bizInfoSummaryView.setReduceInterestPercentage(new BigDecimal(reduceInterestPercent));
        bizInfoSummaryView.setReduceTaxPercentage(new BigDecimal(reduceTaxPercent));

        netMarginAmount = earningsBeforeTaxAmount- reduceInterestAmount - reduceTaxAmount ;
        netMarginPercent = earningsBeforeTaxPercent - reduceInterestPercent - reduceTaxPercent;

        bizInfoSummaryView.setNetMarginAmount(new BigDecimal(netMarginAmount));
        bizInfoSummaryView.setNetMarginPercentage(new BigDecimal(netMarginPercent));
        log.info("onCalSummaryTable 333");

        log.info("onCalSummaryTable end");
    }

    public void onSaveBizInfoSummary() {

        try {
            log.info("onSaveBizInfoSummary begin");
            HttpSession session = FacesUtil.getSession(true);
            session.setAttribute("bizInfoDetailViewId", -1);
            log.info("bizInfoSummaryControl workCase  1 ---- " + workCaseId);

            log.info("Controller getAddressMoo is ---- " + bizInfoSummaryView.getAddressMoo());
            log.info("Controller getAddressNo is ---- " + bizInfoSummaryView.getAddressNo());
            bizInfoSummaryControl.onSaveBizSummaryToDB(bizInfoSummaryView, workCaseId);
            log.info("bizInfoSummaryControl end");

            if (redirect != null && !redirect.equals("")) {
                log.info("have to redirect ");
                if (redirect.equals("viewDetail")) {
                    log.info("view Detail ");
                    onViewDetail();
                }

                String url = "bizInfoDetail.jsf";
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();

                log.info("redirect to new page url is " + url);
                ec.redirect(ec.getRequestContextPath() + "/site/bizInfoDetail.jsf");
                //ec.redirect(url);
                log.info("redirect to new page goooo!! 1");
            } else {
                log.info("not have to redirect ");
            }

            log.info("after redirect method");
            messageHeader = "Save BizInfoSummary Success.";
            message = "Save BizInfoSummary data success.";
            log.info("after set message");
            onCreation();
            log.info("onCreation() after Save");
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch (Exception ex) {
            log.info("onSaveBizInfoSummary Error");
            messageHeader = "Save BizInfoSummary Failed.";
            if (ex.getCause() != null) {
                log.info("ex.getCause().toString() is " + ex.getCause().toString());
                message = "Save BizInfoSummary data failed. Cause : " + ex.getCause().toString();
            } else {
                log.info("ex.getCause().toString() is " + ex.getMessage());
                message = "Save BizInfoSummary data failed. Cause : " + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } finally {
            log.info("onSaveBizInfoSummary end");
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

    public Date getCurrentDate() {
        return DateTime.now().toDate();
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
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
}
