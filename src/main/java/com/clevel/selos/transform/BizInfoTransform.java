package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.BizInfo;
import com.clevel.selos.model.view.BizInfoView;

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
