package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BizInfoDetailControl;
import com.clevel.selos.businesscontrol.BizInfoSummaryControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.model.view.BizInfoSummaryView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
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
    private User user;
    private Date currentDate;

    private ReferredExperience referredExperience;
    private String sumIncomeAmountDis;
    private BigDecimal sumIncomeAmount;
    private BigDecimal sumIncomePercent;
    private BigDecimal sumweightIntvIncomeFactor;
    private BigDecimal sumweightAR;
    private BigDecimal sumweightAP;
    private BigDecimal sumweightINV;


    private String messageHeader;
    private String message;
    private String redirect;

    @Inject
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
    private Util util;

    public BizInfoSummary() {

    }

    @PostConstruct
    public void onCreation() {
        log.info("onCreation bizInfoSum");
        onSearchBizInfoSummaryByWorkCase();


        provinceList = provinceDAO.getListOrderByParameter("name");
        countryList = countryDAO.findAll();
        referredExperienceList = referredExperienceDAO.findAll();
        HttpSession session = FacesUtil.getSession(true);
        user = (User)session.getAttribute("user");

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
        }


    }

    public void onSearchBizInfoSummaryByWorkCase() {
        log.info(" Initial session ");
        HttpSession session = FacesUtil.getSession(true);
        log.info(" Initial session is " + session);

        session.setAttribute("workCaseId", 10001);
        long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        log.info(" get FROM session workCaseId is " + workCaseId);
        bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);

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
            double bankstatementAvg = 0;
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

                bankstatementAvg = 12000;
                incomeAmountCal = bankstatementAvg * 12;
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

            log.info("sumIncomeAmountX 1111.00 is " + util.formatNumber(1111.00));
            log.info("sumIncomeAmountX 1111.55 is " + util.formatNumber(1234.55));
            log.info("sumIncomeAmountX 1111.55 is " + util.formatNumber(1234567.55));
            log.info("sumIncomeAmountX 1111.55 is " + util.formatNumber(12345678910.55));

            sumIncomeAmountDis = util.formatNumber(sumIncomeAmountD);

            sumIncomeAmount = new BigDecimal(sumIncomeAmountD).setScale(2);
            sumIncomePercent = new BigDecimal(sumIncomePercentD).setScale(2);
            sumweightAR = new BigDecimal(sumAR).setScale(2);
            sumweightAP = new BigDecimal(sumAP).setScale(2);
            sumweightINV = new BigDecimal(sumINV).setScale(2);
            sumweightIntvIncomeFactor = new BigDecimal(sumAdjust).setScale(2);


            bizInfoSummaryView.setSumIncomeAmount(sumIncomeAmount);
            bizInfoSummaryView.setSumIncomePercent(sumIncomePercent);
            bizInfoSummaryView.setSumWeightAR(sumweightAR);
            bizInfoSummaryView.setSumWeightAP(sumweightAP);
            bizInfoSummaryView.setSumWeightINV(sumweightINV);
            bizInfoSummaryView.setSumWeightInterviewedIncomeFactorPercent(sumweightIntvIncomeFactor);
        }
    }


    public void onSaveBizInfoSummary() {

        try {
            log.info("onSaveBizInfoSummary begin");
            if (bizInfoSummaryView.getId() == 0) {
                bizInfoSummaryView.setCreateBy(user);
                bizInfoSummaryView.setCreateDate(DateTime.now().toDate());
            }
            bizInfoSummaryView.setModifyBy(user);
            HttpSession session = FacesUtil.getSession(true);
            session.setAttribute("workCaseId", 10001);
            session.setAttribute("bizInfoDetailViewId", -1);
            long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
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
                log.info("redirect to new page");
                ec.redirect(url);
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

    public BigDecimal getSumweightAR() {
        return sumweightAR;
    }

    public void setSumweightAR(BigDecimal sumweightAR) {
        this.sumweightAR = sumweightAR;
    }

    public BigDecimal getSumweightIntvIncomeFactor() {
        return sumweightIntvIncomeFactor;
    }

    public void setSumweightIntvIncomeFactor(BigDecimal sumweightIntvIncomeFactor) {
        this.sumweightIntvIncomeFactor = sumweightIntvIncomeFactor;
    }

    public BigDecimal getSumweightAP() {
        return sumweightAP;
    }

    public void setSumweightAP(BigDecimal sumweightAP) {
        this.sumweightAP = sumweightAP;
    }

    public BigDecimal getSumweightINV() {
        return sumweightINV;
    }

    public void setSumweightINV(BigDecimal sumweightINV) {
        this.sumweightINV = sumweightINV;
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
}
