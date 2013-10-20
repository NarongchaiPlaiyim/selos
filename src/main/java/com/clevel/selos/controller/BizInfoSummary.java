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
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
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
    public int provinceID;
    public int districtID;
    public int subDistrictID;

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


    public List<BizInfoDetailView> getBusinessInfoListDB(){
        long bizInfoSummaryViewId;
        bizInfoSummaryViewId = bizInfoSummaryView.getId();
        bizInfoDetailViewList = bizInfoSummaryControl.onGetBizInfoDetailByBizInfoSummary(bizInfoSummaryViewId);
        return bizInfoDetailViewList;
    }


    public void onSaveBizInfoSummary(){
        log.info( "onSaveBizInfoView bizInfoDetailView is " + bizInfoSummaryView);

        log.info( " Initial session ");
        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("workCaseId", 10001);
        session.setAttribute("bizInfoDetailViewId", -1 );

        log.info( " get AT session workCaseId is " + session.getAttribute("workCaseId").toString());
        long workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
        log.info( " get FROM session workCaseId is " + workCaseId);

        bizInfoSummaryControl.onSaveBizSummaryToDB(bizInfoSummaryView, workCaseId);

        session.setAttribute("bizInfoSummaryId",bizInfoSummaryView.getId() );

    }

    public void onViewDetail(){
        log.info(" onViewDetail begin !! {}");
        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("bizInfoDetailViewId",selectBizInfoDetailView.getId() );
        //log.info(" onViewDetail selectBizInfoDetailView onRow !! {}", selectBizInfoDetailView);
        log.info(" onViewDetail end !! {}");


    }


    public void onDeleteBizInfoToDB(){

        bizInfoDetailControl.onDeleteBizInfoToDB(selectBizInfoDetailView);

    }

    public List<BizInfoDetailView> getBizInfoDetailViewList() {
        return bizInfoDetailViewList;
    }

    public void setBizInfoDetailViewList(List<BizInfoDetailView> bizInfoDetailViewList) {
        this.bizInfoDetailViewList = bizInfoDetailViewList;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }

    public int getSubDistrictID() {
        return subDistrictID;
    }

    public void setSubDistrictID(int subDistrictID) {
        this.subDistrictID = subDistrictID;
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
}
