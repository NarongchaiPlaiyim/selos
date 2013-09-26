package com.clevel.selos.controller;

import com.clevel.selos.dao.master.BusinessDescriptionDAO;
import com.clevel.selos.dao.master.BusinessGroupDAO;
import com.clevel.selos.dao.working.BizInfoDetailDAO;
import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.view.BizInfoFullView;
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
public class BizInfoSummaryMaker implements Serializable {

    @NormalMessage
    @Inject
    Message msg;

    private int rowIndex;
    private String modeForButton;
    private String dlgStakeName;
    private int bizGroupId;


    private List<BizInfoFullView> bizInfoFullViewList;
    private List<BizInfoDetail> bizInfoDetailList;

    @Inject
    Logger log;
    @Inject
    private BusinessGroupDAO businessGroupDAO;
    @Inject
    private BusinessDescriptionDAO businessDescriptionDAO;
    @Inject
    private BizInfoDetailDAO bizInfoDetailDAO;

    public BizInfoSummaryMaker(){

    }

    @PostConstruct
    public void onCreation(){
        //bizInfoFullViewList = getBusinessInfoList();
        log.info("onCreation bizInfoSum");
        bizInfoFullViewList = getBusinessInfoListDB();
        bizInfoDetailList = new ArrayList<BizInfoDetail>();

    }

    public List<BizInfoFullView> getBusinessInfoList(){
        log.info("getBusinessInfoList bizInfoSum");
        bizInfoFullViewList = new ArrayList<BizInfoFullView>();
        BizInfoFullView bizInfoFullView;

        bizInfoFullView = new BizInfoFullView();

        bizInfoFullView.setBizComment("Comment 1");
        bizInfoFullViewList.add(bizInfoFullView);

        bizInfoFullView = new BizInfoFullView();
        bizInfoFullView.setBizComment("Comment 2");
        bizInfoFullViewList.add(bizInfoFullView);

        bizInfoFullView = new BizInfoFullView();
        bizInfoFullView.setBizComment("Comment 3");
        bizInfoFullViewList.add(bizInfoFullView);

        return bizInfoFullViewList;
    }

    public List<BizInfoFullView> getBusinessInfoListDB(){
        log.info("getBusinessInfoListDB bizInfoSum");
        bizInfoDetailList = bizInfoDetailDAO.findAll();

        bizInfoFullViewList = onTransformToView(bizInfoDetailList);

        return bizInfoFullViewList;
    }

    private List<BizInfoFullView> onTransformToView(List<BizInfoDetail> bizInfoDetailList){
        log.info("onTransformToView bizInfoSum");
        bizInfoFullViewList = new ArrayList<BizInfoFullView>();
        BizInfoFullView bizInfoFullView;
        BizInfoDetail bizInfoDetail;
        for(int i=0;i<bizInfoDetailList.size();i++){
            bizInfoDetail =  bizInfoDetailList.get(i);
            bizInfoFullView = new BizInfoFullView();
            bizInfoFullView.setBizComment(bizInfoDetail.getBizComment());
            bizInfoFullViewList.add(bizInfoFullView);
        }
        return bizInfoFullViewList;

    }

    public void onViewDetail(){
        log.info(" success !! {}",true);
    }

    public List<BizInfoFullView> getBizInfoFullViewList() {
        return bizInfoFullViewList;
    }

    public void setBizInfoFullViewList(List<BizInfoFullView> bizInfoFullViewList) {
        this.bizInfoFullViewList = bizInfoFullViewList;
    }
}
