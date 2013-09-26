package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.BizStakeholder;
import com.clevel.selos.model.view.BizInfoFullView;
import com.clevel.selos.model.view.StakeholderView;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 25/9/2556
 * Time: 14:59 น.
 * To change this template use File | Settings | File Templates.
 */
public class BizStakeholderTransform extends Transform {

    public StakeholderView transformToView(BizStakeholder bizStakeholder){

        StakeholderView stakeholderView = new StakeholderView();

        stakeholderView.setStakeholderType(bizStakeholder.getStakeholderType());
        stakeholderView.setNo(bizStakeholder.getNo());
        stakeholderView.setName(bizStakeholder.getName());
        stakeholderView.setContactName(bizStakeholder.getContactName());
        stakeholderView.setContactYear(bizStakeholder.getContactYear());
        stakeholderView.setPercentSalesVolume(bizStakeholder.getPercentSalesVolume());
        stakeholderView.setPercentCash(bizStakeholder.getPercentCash());
        stakeholderView.setPercentCredit(bizStakeholder.getPercentCredit());
        stakeholderView.setPhoneNo(bizStakeholder.getPhoneNo());
        stakeholderView.setCreditTerm(bizStakeholder.getCreditTerm());

        return stakeholderView;
    }

    public BizStakeholder transformToModel(StakeholderView stakeholderView,BizInfoDetail bizInfoDetail){

        BizStakeholder bizStakeholder = new BizStakeholder();

        bizStakeholder.setStakeholderType(stakeholderView.getStakeholderType());
        bizStakeholder.setNo(stakeholderView.getNo());
        bizStakeholder.setName(stakeholderView.getName());
        bizStakeholder.setContactName(stakeholderView.getContactName());
        bizStakeholder.setContactYear(stakeholderView.getContactYear());
        bizStakeholder.setPercentSalesVolume(stakeholderView.getPercentSalesVolume());
        bizStakeholder.setPercentCash(stakeholderView.getPercentCash());
        bizStakeholder.setPercentCredit(stakeholderView.getPercentCredit());
        bizStakeholder.setPhoneNo(stakeholderView.getPhoneNo());
        bizStakeholder.setCreditTerm(stakeholderView.getCreditTerm());
        bizStakeholder.setBizInfoDetail(bizInfoDetail);

        return bizStakeholder;
    }

}
