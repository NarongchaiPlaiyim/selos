package com.clevel.selos.controller;

import com.clevel.selos.busiensscontrol.BizInfoDetailControl;
import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.model.db.master.District;
import com.clevel.selos.model.db.master.Province;
import com.clevel.selos.model.db.master.SubDistrict;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.view.BizInfoDetailView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.transform.BizInfoDetailTransform;
import com.clevel.selos.transform.BizProductDetailTransform;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 5/9/2556
 * Time: 16:26 น.
 * To change this template use File | Settings | File Templates.
 */
@ViewScoped
@ManagedBean(name = "bizInfoSummary")
public class BizInfoSummary implements Serializable {

    @NormalMessage
    @Inject
    Message msg;

    private BizInfoSummary bizInfoSummary;
    private List<BizInfoDetailView> bizInfoDetailViewList;
    private List<BizInfoDetail> bizInfoDetailDetailList;
    private List<Province> provinceList;
    private List<District> districtList;
    private List<SubDistrict> subDistrictList;
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
    private BizInfoDetailControl bizInfoDetailControl;
    @Inject
    private BizInfoDetailTransform bizProductDetailTransform;

    public BizInfoSummary(){

    }

    @PostConstruct
    public void onCreation(){
        //bizInfoDetailViewList = getBusinessInfoList();
        log.info("onCreation bizInfoSum");
        //bizInfoDetailViewList = getBusinessInfoListDB();
        bizInfoSummary =  new BizInfoSummary();
        provinceList = provinceDAO.getListOrderByParameter("name");
        bizInfoDetailViewList= getBusinessInfoListDB();
        //bizInfoDetailDetailList = new ArrayList<BizInfoDetail>();

    }

    public void onChangeProvince() {

        log.info("onChangeProvince :::: provinceID :::  ", provinceID);
        Province province = provinceDAO.findById(provinceID);
        log.info("onChangeProvince :::: province ::: ", province);

        districtList = districtDAO.getListByProvince(province);
        log.info("onChangeProvince :::: districtList.size ::: ", districtList.size());
    }

    public void onChangeDistrict() {

        log.info("onChangeDistrict :::: districtID  :::  ", districtID);
        District district = districtDAO.findById(districtID);
        log.info("onChangeDistrict :::: district ::: ", district);

        subDistrictList = subDistrictDAO.getListByDistrict(district);
        log.info("onChangeDistrict :::: subDistrictList.size ::: ", subDistrictList.size());
    }


    public List<BizInfoDetailView> getBusinessInfoList(){
        log.info("getBusinessInfoList bizInfoSum");
        bizInfoDetailViewList = new ArrayList<BizInfoDetailView>();
        BizInfoDetailView bizInfoDetailView;

        bizInfoDetailView = new BizInfoDetailView();

        bizInfoDetailView.setBizComment("Comment 1");
        bizInfoDetailViewList.add(bizInfoDetailView);

        bizInfoDetailView = new BizInfoDetailView();
        bizInfoDetailView.setBizComment("Comment 2");
        bizInfoDetailViewList.add(bizInfoDetailView);

        bizInfoDetailView = new BizInfoDetailView();
        bizInfoDetailView.setBizComment("Comment 3");
        bizInfoDetailViewList.add(bizInfoDetailView);

        return bizInfoDetailViewList;
    }

    public List<BizInfoDetailView> getBusinessInfoListDB(){
        log.info("getBusinessInfoListDB bizInfoSum");
        bizInfoDetailDetailList = bizInfoDetailDAO.findAll();

        bizInfoDetailViewList = onTransformToView(bizInfoDetailDetailList);

        return bizInfoDetailViewList;
    }

    private List<BizInfoDetailView> onTransformToView(List<BizInfoDetail> bizInfoDetailDetailList){
        log.info("onTransformToView bizInfoSum");
        bizInfoDetailViewList = new ArrayList<BizInfoDetailView>();
        BizInfoDetailView bizInfoDetailView;
        BizInfoDetail bizInfoDetail;
        for(int i=0;i< bizInfoDetailDetailList.size();i++){
            bizInfoDetail =  bizInfoDetailDetailList.get(i);
            bizInfoDetailView =  bizProductDetailTransform.transformToView(bizInfoDetail)  ;
            bizInfoDetailViewList.add(bizInfoDetailView);
        }
        return bizInfoDetailViewList;

    }

    public void onViewDetail(){
        log.info(" success !! {}",true);
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

    public List<BizInfoDetail> getBizInfoDetailDetailList() {
        return bizInfoDetailDetailList;
    }

    public void setBizInfoDetailDetailList(List<BizInfoDetail> bizInfoDetailDetailList) {
        this.bizInfoDetailDetailList = bizInfoDetailDetailList;
    }
}
