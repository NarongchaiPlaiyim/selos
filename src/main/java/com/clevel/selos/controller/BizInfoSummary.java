package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.BizInfoDetailControl;
import com.clevel.selos.businesscontrol.BizInfoSummaryControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.model.db.master.District;
import com.clevel.selos.model.db.master.Province;
import com.clevel.selos.model.db.master.ReferredExperience;
import com.clevel.selos.model.db.master.SubDistrict;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.model.view.BizInfoSummaryView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.transform.BizInfoDetailTransform;
import com.clevel.selos.util.FacesUtil;
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
    private List<SubDistrict> subDistrictList;
    private List<ReferredExperience> referredExperienceList;
    private Province province;
    private District district;
    private SubDistrict subDistrict;
    private ReferredExperience  referredExperience;
    private BigDecimal sumIncomeAmount;
    private BigDecimal sumIncomePercent;
    private BigDecimal sumweightAdjustIncomeFactor;
    private long sumweightAR;
    private long sumweightAP;
    private long sumweightINV;


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
    private ProvinceDAO provinceDAO;    // find credit type
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


    public BizInfoSummary(){

    }

    @PostConstruct
    public void onCreation(){
        log.info("onCreation bizInfoSum");
        onSearchBizInfoSummaryByWorkCase();


        provinceList = provinceDAO.getListOrderByParameter("name");
        referredExperienceList = referredExperienceDAO.findAll();

        if(bizInfoSummaryView == null){

            log.info("bizInfoSummaryView == null " );

            bizInfoSummaryView =  new BizInfoSummaryView();

            province = new Province();
            district = new District();
            subDistrict = new SubDistrict();
            referredExperience = new ReferredExperience();

            district.setProvince(province);
            subDistrict.setDistrict(district);
            subDistrict.setProvince(province);
            bizInfoSummaryView.setSubDistrict(subDistrict);
            bizInfoSummaryView.setReferredExperience(referredExperience);

        }else{
            getBusinessInfoListDB();
            onChangeProvince();
            onChangeDistrict();
        }


    }

    public void onSearchBizInfoSummaryByWorkCase(){
        log.info( " Initial session ");
        HttpSession session = FacesUtil.getSession(true);
        log.info(" Initial session is " + session);

        session.setAttribute("workCaseId", 10001);
        long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        log.info( " get FROM session workCaseId is " + workCaseId);
        bizInfoSummaryView = bizInfoSummaryControl.onGetBizInfoSummaryByWorkCase(workCaseId);

    }
    public void onChangeProvince() {
        Province proSelect = bizInfoSummaryView.getSubDistrict().getDistrict().getProvince();
        districtList = districtDAO.getListByProvince(proSelect);
        log.info("onChangeProvince :::: districtList.size ::: ", districtList.size());
    }

    public void onChangeDistrict() {
        District districtSelect= bizInfoSummaryView.getSubDistrict().getDistrict();
        subDistrictList = subDistrictDAO.getListByDistrict(districtSelect);
        log.info("onChangeDistrict :::: subDistrictList.size ::: ", subDistrictList.size());
    }


    public void getBusinessInfoListDB(){

        long bizInfoSummaryViewId;
        bizInfoSummaryViewId = bizInfoSummaryView.getId();
        bizInfoDetailViewList = bizInfoSummaryControl.onGetBizInfoDetailByBizInfoSummary(bizInfoSummaryViewId);
        if(bizInfoDetailViewList.size()==0){
            bizInfoDetailViewList = new ArrayList<BizInfoDetailView>();
        }else{
            sumIncomeAmount = new BigDecimal(1000).setScale(2);
            sumIncomePercent = new BigDecimal(1000).setScale(2);
            double sumAdjust = -1;
            for(int i=0;i<bizInfoDetailViewList.size();i++){
                BizInfoDetailView temp = bizInfoDetailViewList.get(i);
                sumAdjust =+ temp.getAdjustedIncomeFactor().doubleValue();
                sumweightAR =+ temp.getBizDesc().getAr();
                sumweightAP =+ temp.getBizDesc().getAp() ;
                sumweightINV =+ temp.getBizDesc().getInv()  ;
            }
            sumweightAdjustIncomeFactor = new BigDecimal(sumAdjust).setScale(2);
        }
    }


    public void onSaveBizInfoSummary(){

        try{
            log.info("onSaveBizInfoSummary begin");

            HttpSession session = FacesUtil.getSession(true);
            session.setAttribute("workCaseId", 10001);
            session.setAttribute("bizInfoDetailViewId", -1 );
            long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            bizInfoSummaryControl.onSaveBizSummaryToDB(bizInfoSummaryView, workCaseId);
            log.info("bizInfoSummaryControl end");


            if(redirect!=null&&!redirect.equals("")){
                log.info("have to redirect ");
                if(redirect.equals("viewDetail")){
                    log.info("view Detail ");
                    onViewDetail();
                }

                String url = "bizInfoDetail.jsf";
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                log.info("redirect to new page");
                ec.redirect(url);
            }else{
                log.info("not have to redirect ");
            }

            log.info("after redirect method");
            messageHeader = "Save BizInfoSummary Success.";
            message = "Save BizInfoSummary data success.";
            log.info("after set message");
            onCreation();
            log.info("onCreation() after Save");
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        } catch(Exception ex){
            log.info("onSaveBizInfoSummary Error");
            messageHeader = "Save BizInfoSummary Failed.";
            if(ex.getCause() != null){
                log.info("ex.getCause().toString() is " + ex.getCause().toString());
                message = "Save BizInfoSummary data failed. Cause : " + ex.getCause().toString();
            } else {
                log.info("ex.getCause().toString() is " + ex.getMessage());
                message = "Save BizInfoSummary data failed. Cause : " + ex.getMessage();
            }
            RequestContext.getCurrentInstance().execute("msgBoxSystemMessageDlg.show()");
        }finally {
            log.info("onSaveBizInfoSummary end");
        }
    }

    public void onViewDetail(){
        log.info(" onViewDetail begin !! {}");
        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("bizInfoDetailViewId",selectBizInfoDetailView.getId() );
        log.info(" onViewDetail end !! {}");
    }


    public void onDeleteBizInfoToDB(){

        try{
            log.info("onDeleteBizInfoToDB Controller begin " );
            bizInfoDetailControl.onDeleteBizInfoToDB(selectBizInfoDetailView);
            getBusinessInfoListDB();
        }catch (Exception e){

        }finally {
            log.info("onDeleteBizInfoToDB Controller end " );
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

    public BigDecimal getSumweightAdjustIncomeFactor() {
        return sumweightAdjustIncomeFactor;
    }

    public void setSumweightAdjustIncomeFactor(BigDecimal sumweightAdjustIncomeFactor) {
        this.sumweightAdjustIncomeFactor = sumweightAdjustIncomeFactor;
    }

    public long getSumweightAR() {
        return sumweightAR;
    }

    public void setSumweightAR(long sumweightAR) {
        this.sumweightAR = sumweightAR;
    }

    public long getSumweightAP() {
        return sumweightAP;
    }

    public void setSumweightAP(long sumweightAP) {
        this.sumweightAP = sumweightAP;
    }

    public long getSumweightINV() {
        return sumweightINV;
    }

    public void setSumweightINV(long sumweightINV) {
        this.sumweightINV = sumweightINV;
    }
}
