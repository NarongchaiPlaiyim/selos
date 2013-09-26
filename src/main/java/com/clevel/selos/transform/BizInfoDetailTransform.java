package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.view.BizInfoFullView;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 25/9/2556
 * Time: 14:59 น.
 * To change this template use File | Settings | File Templates.
 */
public class BizInfoDetailTransform extends Transform {

    public BizInfoFullView transformToView(BizInfoDetail bizInfoDetail){

        BizInfoFullView bizInfoFullView = new BizInfoFullView();
        bizInfoFullView.setBizInfoText(bizInfoDetail.getBizInfoText());
        bizInfoFullView.setBizType(bizInfoDetail.getBusinessType());
        bizInfoFullView.setBizGroup(bizInfoDetail.getBusinessGroup());
        bizInfoFullView.setBizDesc(bizInfoDetail.getBusinessDescription());
        bizInfoFullView.setBizCode(bizInfoDetail.getBizCode());
        bizInfoFullView.setBizComment(bizInfoDetail.getBizComment());
        bizInfoFullView.setIncomeFactor(bizInfoDetail.getIncomeFactor());
        bizInfoDetail.setAdjustedIncomeFactor(bizInfoDetail.getAdjustedIncomeFactor());
        bizInfoFullView.setPercentBiz(bizInfoDetail.getPercentBiz());

        return bizInfoFullView;
    }

    public BizInfoDetail transformToModel(BizInfoFullView bizInfoFullApp){

        BizInfoDetail bizInfoDetail = new BizInfoDetail();
        bizInfoDetail.setBizInfoText(bizInfoFullApp.getBizInfoText());
        bizInfoDetail.setBusinessType(bizInfoFullApp.getBizType());
        bizInfoDetail.setBusinessGroup(bizInfoFullApp.getBizGroup());
        bizInfoDetail.setBusinessDescription(bizInfoFullApp.getBizDesc());
        bizInfoDetail.setBizCode(bizInfoFullApp.getBizCode());
        bizInfoDetail.setBizComment(bizInfoFullApp.getBizComment());
        bizInfoDetail.setIncomeFactor(bizInfoFullApp.getIncomeFactor());
        bizInfoDetail.setAdjustedIncomeFactor(bizInfoFullApp.getAdjustedIncomeFactor());
        bizInfoDetail.setPercentBiz(bizInfoFullApp.getPercentBiz());

        return bizInfoDetail;
    }

}
