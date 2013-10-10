package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.BizStakeHolderDetail;
import com.clevel.selos.model.view.BizStakeHolderDetailView;

public class BizStakeHolderDetailTransform extends Transform {

    public BizStakeHolderDetailView transformToView(BizStakeHolderDetail bizStakeHolderDetail){

        BizStakeHolderDetailView stakeHolderView = new BizStakeHolderDetailView();

        stakeHolderView.setStakeHolderType(bizStakeHolderDetail.getStakeHolderType());
        stakeHolderView.setNo(bizStakeHolderDetail.getNo());
        stakeHolderView.setName(bizStakeHolderDetail.getName());
        stakeHolderView.setContactName(bizStakeHolderDetail.getContactName());
        stakeHolderView.setContactYear(bizStakeHolderDetail.getContactYear());
        stakeHolderView.setPercentSalesVolume(bizStakeHolderDetail.getPercentSalesVolume());
        stakeHolderView.setPercentCash(bizStakeHolderDetail.getPercentCash());
        stakeHolderView.setPercentCredit(bizStakeHolderDetail.getPercentCredit());
        stakeHolderView.setPhoneNo(bizStakeHolderDetail.getPhoneNo());
        stakeHolderView.setCreditTerm(bizStakeHolderDetail.getCreditTerm());

        return stakeHolderView;
    }

    public BizStakeHolderDetail transformToModel(BizStakeHolderDetailView stakeHolderView,BizInfoDetail bizInfoDetail){

        BizStakeHolderDetail bizStakeHolderDetail = new BizStakeHolderDetail();

        bizStakeHolderDetail.setStakeHolderType(stakeHolderView.getStakeHolderType());
        bizStakeHolderDetail.setNo(stakeHolderView.getNo());
        bizStakeHolderDetail.setName(stakeHolderView.getName());
        bizStakeHolderDetail.setContactName(stakeHolderView.getContactName());
        bizStakeHolderDetail.setContactYear(stakeHolderView.getContactYear());
        bizStakeHolderDetail.setPercentSalesVolume(stakeHolderView.getPercentSalesVolume());
        bizStakeHolderDetail.setPercentCash(stakeHolderView.getPercentCash());
        bizStakeHolderDetail.setPercentCredit(stakeHolderView.getPercentCredit());
        bizStakeHolderDetail.setPhoneNo(stakeHolderView.getPhoneNo());
        bizStakeHolderDetail.setCreditTerm(stakeHolderView.getCreditTerm());
        bizStakeHolderDetail.setBizInfoDetail(bizInfoDetail);

        return bizStakeHolderDetail;
    }

}
