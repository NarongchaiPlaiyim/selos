package com.clevel.selos.controller;

import com.clevel.selos.dao.master.BusinessDescriptionDAO;
import com.clevel.selos.dao.master.BusinessGroupDAO;
import com.clevel.selos.dao.working.BizInfoDAO;
import com.clevel.selos.model.db.working.BizInfo;
import com.clevel.selos.model.view.BizInfoView;
import com.clevel.selos.model.view.BizInfoView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
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

    private int rowIndex;
    private String modeForButton;
    private String dlgStakeName;
    private int bizGroupId;


    private List<BizInfoView> bizInfoViewList;
    private List<BizInfo> bizInfoDetailList;

    @Inject
    Logger log;
    @Inject
    private BusinessGroupDAO businessGroupDAO;
    @Inject
    private BusinessDescriptionDAO businessDescriptionDAO;
    @Inject
    private BizInfoDAO bizInfoDAO;

    public BizInfoSummary(){

    }

    @PostConstruct
    public void onCreation(){
        //bizInfoViewList = getBusinessInfoList();
        log.info("onCreation bizInfoSum");
        bizInfoViewList = getBusinessInfoListDB();
        bizInfoDetailList = new ArrayList<BizInfo>();

    }

    public List<BizInfoView> getBusinessInfoList(){
        log.info("getBusinessInfoList bizInfoSum");
        bizInfoViewList = new ArrayList<BizInfoView>();
        BizInfoView bizInfoView;

        bizInfoView = new BizInfoView();

        bizInfoView.setBizComment("Comment 1");
        bizInfoViewList.add(bizInfoView);

        bizInfoView = new BizInfoView();
        bizInfoView.setBizComment("Comment 2");
        bizInfoViewList.add(bizInfoView);

        bizInfoView = new BizInfoView();
        bizInfoView.setBizComment("Comment 3");
        bizInfoViewList.add(bizInfoView);

        return bizInfoViewList;
    }

    public List<BizInfoView> getBusinessInfoListDB(){
        log.info("getBusinessInfoListDB bizInfoSum");
        bizInfoDetailList = bizInfoDAO.findAll();

        bizInfoViewList = onTransformToView(bizInfoDetailList);

        return bizInfoViewList;
    }

    private List<BizInfoView> onTransformToView(List<BizInfo> bizInfoDetailList){
        log.info("onTransformToView bizInfoSum");
        bizInfoViewList = new ArrayList<BizInfoView>();
        BizInfoView bizInfoView;
        BizInfo bizInfo;
        for(int i=0;i<bizInfoDetailList.size();i++){
            bizInfo =  bizInfoDetailList.get(i);
            bizInfoView = new BizInfoView();
            bizInfoView.setBizComment(bizInfo.getBizComment());
            bizInfoViewList.add(bizInfoView);
        }
        return bizInfoViewList;

    }

    public void onViewDetail(){
        log.info(" success !! {}",true);
    }

    public List<BizInfoView> getBizInfoViewList() {
        return bizInfoViewList;
    }

    public void setBizInfoViewList(List<BizInfoView> bizInfoViewList) {
        this.bizInfoViewList = bizInfoViewList;
    }
}
