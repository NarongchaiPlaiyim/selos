package com.clevel.selos.controller;

import com.clevel.selos.dao.master.BusinessDescriptionDAO;
import com.clevel.selos.dao.master.BusinessGroupDAO;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.model.view.BizInfoFullView;
import com.clevel.selos.model.view.BizProductView;
import com.clevel.selos.model.view.BizInfoFullView;
import com.clevel.selos.model.view.StakeholderView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
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
@ManagedBean(name = "businessInfoSummary")
public class BusinessInfoSummaryMaker implements Serializable {

    @NormalMessage
    @Inject
    Message msg;

    private int rowIndex;
    private String modeForButton;
    private String dlgStakeName;
    private int bizGroupId;


    private List<BizInfoFullView> bizInfoFullViewList;

    @Inject
    Logger log;
    @Inject
    private BusinessGroupDAO businessGroupDAO;
    @Inject
    private BusinessDescriptionDAO businessDescriptionDAO;

    public BusinessInfoSummaryMaker(){

    }

    @PostConstruct
    public void onCreation(){
        bizInfoFullViewList = getBusinessInfoList();

    }

    public List<BizInfoFullView> getBusinessInfoList(){
        bizInfoFullViewList = new ArrayList<BizInfoFullView>();
        BizInfoFullView bizInfoFullView;

        bizInfoFullView = new BizInfoFullView();

        bizInfoFullView.setComment("Comment 1");
        bizInfoFullViewList.add(bizInfoFullView);

        bizInfoFullView = new BizInfoFullView();
        bizInfoFullView.setComment("Comment 2");
        bizInfoFullViewList.add(bizInfoFullView);

        bizInfoFullView = new BizInfoFullView();
        bizInfoFullView.setComment("Comment 3");
        bizInfoFullViewList.add(bizInfoFullView);

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
