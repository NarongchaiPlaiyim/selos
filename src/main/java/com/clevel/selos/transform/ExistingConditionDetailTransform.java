package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ExistingConditionDetail;
import com.clevel.selos.model.db.working.ExistingCreditFacility;
import com.clevel.selos.model.view.ExistingConditionDetailView;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExistingConditionDetailTransform extends Transform {
    @SELOS
    @Inject
    private Logger log;

    @Inject
    public ExistingConditionDetailTransform() {
    }

    public List<ExistingConditionDetail> transformsToModel(List<ExistingConditionDetailView> existingConditionDetailViewList,ExistingCreditFacility existingCreditFacility,User user) {
        log.info("transformsToModel ExistingConditionDetail begin ");
        List<ExistingConditionDetail> existingConditionDetailList = new ArrayList<ExistingConditionDetail>();
        ExistingConditionDetail existingConditionDetail;

        for (ExistingConditionDetailView existingConditionDetailView : existingConditionDetailViewList) {
            existingConditionDetail = new ExistingConditionDetail();

            if (existingConditionDetailView.getId() != 0) {
                existingConditionDetail.setId(existingConditionDetailView.getId());
                existingConditionDetail.setCreateDate(existingConditionDetailView.getCreateDate());
                existingConditionDetail.setCreateBy(existingConditionDetailView.getCreateBy());
            } else { // id = 0 create new
                existingConditionDetail.setCreateDate(new Date());
                existingConditionDetail.setCreateBy(user);
            }

            existingConditionDetail.setNo(existingConditionDetailView.getNo());
            existingConditionDetail.setConditionDesc(existingConditionDetailView.getConditionDesc());
            existingConditionDetail.setLoanType(existingConditionDetailView.getLoanType());
            existingConditionDetail.setExistingCreditFacility(existingCreditFacility);
            existingConditionDetail.setModifyDate(new Date());
            existingConditionDetail.setModifyBy(user);
            existingConditionDetailList.add(existingConditionDetail);
        }
        log.info("transformsToModel ExistingConditionDetail end ");

        return existingConditionDetailList;
    }

    public List<ExistingConditionDetailView> transformsToView(List<ExistingConditionDetail> existingConditionDetailList) {

        List<ExistingConditionDetailView> existingConditionDetailViewList = new ArrayList<ExistingConditionDetailView>();
        ExistingConditionDetailView existingConditionDetailView;

        for(ExistingConditionDetail existingConditionDetail : existingConditionDetailList){
            existingConditionDetailView = new ExistingConditionDetailView();
            existingConditionDetailView.setCreateBy(existingConditionDetail.getCreateBy());
            existingConditionDetailView.setCreateDate(existingConditionDetail.getCreateDate());
            existingConditionDetailView.setNo(existingConditionDetail.getNo());
            existingConditionDetailView.setLoanType(existingConditionDetail.getLoanType());
            existingConditionDetailView.setConditionDesc(existingConditionDetail.getConditionDesc());
            existingConditionDetailView.setModifyBy(existingConditionDetail.getModifyBy());
            existingConditionDetailView.setModifyDate(existingConditionDetail.getModifyDate());
            existingConditionDetailViewList.add(existingConditionDetailView);
        }

        return existingConditionDetailViewList;
    }
}
