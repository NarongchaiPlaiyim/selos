package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.model.db.master.BusinessType;
import com.clevel.selos.model.db.working.BizInfo;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.BizInfoView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 25/9/2556
 * Time: 14:59 น.
 * To change this template use File | Settings | File Templates.
 */
public class BizInfoTransform extends Transform {

    public BizInfoView transformToView(BizInfo bizInfo){

        BizInfoView bizInfoView = new BizInfoView();
        bizInfoView.setBizInfoText(bizInfo.getBizInfoText());
        bizInfoView.setBizType(bizInfo.getBusinessType());
        bizInfoView.setBizGroup(bizInfo.getBusinessGroup());
        bizInfoView.setBizDesc(bizInfo.getBusinessDescription());
        bizInfoView.setBizCode(bizInfo.getBizCode());
        bizInfoView.setBizComment(bizInfo.getBizComment());
        bizInfoView.setIncomeFactor(bizInfo.getIncomeFactor());
        bizInfoView.setAdjustedIncomeFactor(bizInfo.getAdjustedIncomeFactor());
        bizInfoView.setPercentBiz(bizInfo.getPercentBiz());

        return bizInfoView;
    }

    public List<BizInfoView> transformToPreScreenView(List<BizInfo> bizInfos){
        List<BizInfoView> bizInfoViewList = new ArrayList<BizInfoView>();
        for(BizInfo item : bizInfos){
            BizInfoView bizInfoView = new BizInfoView();
            bizInfoView.setId(item.getId());
            bizInfoView.setBizInfoText(item.getBizInfoText());
            bizInfoView.setBizType(item.getBusinessType());
            bizInfoView.setBizGroup(item.getBusinessGroup());
            bizInfoView.setBizDesc(item.getBusinessDescription());
            bizInfoView.setBizCode(item.getBizCode());
            bizInfoView.setIncomeFactor(item.getIncomeFactor());
            bizInfoView.setAdjustedIncomeFactor(item.getAdjustedIncomeFactor());
            bizInfoView.setPercentBiz(item.getPercentBiz());
            bizInfoView.setBizPermission(item.getBizPermission());
            bizInfoView.setBizComment(item.getBizComment());

            bizInfoViewList.add(bizInfoView);
        }

        return bizInfoViewList;
    }

    public List<BizInfo> transformPrescreenToModel(List<BizInfoView> bizInfoViews, WorkCasePrescreen workCasePrescreen){
        List<BizInfo> bizInfoList = new ArrayList<BizInfo>();
        for(BizInfoView item : bizInfoViews){
            BizInfo bizInfo = new BizInfo();
            bizInfo.setId(item.getId());
            bizInfo.setWorkCasePrescreen(workCasePrescreen);
            bizInfo.setBizInfoText(item.getBizInfoText());
            bizInfo.setBusinessType(item.getBizType());
            bizInfo.setBusinessGroup(item.getBizGroup());
            bizInfo.setBusinessDescription(item.getBizDesc());
            bizInfo.setBizCode(item.getBizCode());
            bizInfo.setIncomeFactor(item.getIncomeFactor());
            bizInfo.setAdjustedIncomeFactor(item.getAdjustedIncomeFactor());
            bizInfo.setPercentBiz(item.getPercentBiz());
            bizInfo.setBizPermission(item.getBizPermission());
            bizInfo.setBizComment(item.getBizComment());

            bizInfoList.add(bizInfo);
        }

        return bizInfoList;
    }

    public BizInfo transformToModel(BizInfoView bizInfoFullApp){

        BizInfo bizInfo = new BizInfo();
        bizInfo.setBizInfoText(bizInfoFullApp.getBizInfoText());
        bizInfo.setBusinessType(bizInfoFullApp.getBizType());
        bizInfo.setBusinessGroup(bizInfoFullApp.getBizGroup());
        bizInfo.setBusinessDescription(bizInfoFullApp.getBizDesc());
        bizInfo.setBizCode(bizInfoFullApp.getBizCode());
        bizInfo.setBizComment(bizInfoFullApp.getBizComment());
        bizInfo.setIncomeFactor(bizInfoFullApp.getIncomeFactor());
        bizInfo.setAdjustedIncomeFactor(bizInfoFullApp.getAdjustedIncomeFactor());
        bizInfo.setPercentBiz(bizInfoFullApp.getPercentBiz());

        return bizInfo;
    }

}
