package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.BizStakeHolderDetail;
import com.clevel.selos.model.view.BizStakeHolderDetailView;
import org.joda.time.DateTime;

import java.security.Timestamp;

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


        stakeHolderView.setCreateBy(bizStakeHolderDetail.getCreateBy());
        stakeHolderView.setCreateDate(bizStakeHolderDetail.getCreateDate());
        stakeHolderView.setModifyBy(bizStakeHolderDetail.getModifyBy());
        stakeHolderView.setModifyDate(bizStakeHolderDetail.getModifyDate());


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


        bizStakeHolderDetail.setCreateBy(bizInfoDetail.getCreateBy());
        bizStakeHolderDetail.setCreateDate(bizInfoDetail.getCreateDate());
        bizStakeHolderDetail.setModifyBy(bizInfoDetail.getModifyBy());
        bizStakeHolderDetail.setModifyDate(DateTime.now().toDate());


        return bizStakeHolderDetail;
    }

}
