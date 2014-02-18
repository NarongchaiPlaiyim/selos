package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ExistingCreditDetail;
import com.clevel.selos.model.db.working.ExistingSplitLineDetail;
import com.clevel.selos.model.view.ExistingSplitLineDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExistingSplitLineDetailTransform extends Transform {

    @Inject
    public ExistingSplitLineDetailTransform() {
    }

    public List<ExistingSplitLineDetail> transformsToModel(List<ExistingSplitLineDetailView> existingSplitLineDetailViewList,ExistingCreditDetail existingCreditDetail,User user) {

        List<ExistingSplitLineDetail> existingSplitLineDetails = new ArrayList<ExistingSplitLineDetail>();
        ExistingSplitLineDetail existingSplitLineDetail;

        if(existingSplitLineDetailViewList!=null){
            for (ExistingSplitLineDetailView existingSplitLineDetailView : existingSplitLineDetailViewList) {
                existingSplitLineDetail = new ExistingSplitLineDetail();

                if (existingSplitLineDetailView.getId() != 0) {
                    existingSplitLineDetail.setId(existingSplitLineDetailView.getId());
                    existingSplitLineDetail.setCreateDate(existingSplitLineDetailView.getCreateDate());
                    existingSplitLineDetail.setCreateBy(existingSplitLineDetailView.getCreateBy());
                } else { // id = 0 create new
                    existingSplitLineDetail.setCreateDate(new Date());
                    existingSplitLineDetail.setCreateBy(user);
                }

                existingSplitLineDetail.setNo(existingSplitLineDetailView.getNo());
                existingSplitLineDetail.setProductProgram(existingSplitLineDetailView.getProductProgram());
                existingSplitLineDetail.setLimit(existingSplitLineDetailView.getLimit());
                existingSplitLineDetail.setExistingCreditDetail(existingCreditDetail);
                existingSplitLineDetail.setModifyDate(new Date());
                existingSplitLineDetail.setModifyBy(user);
                existingSplitLineDetails.add(existingSplitLineDetail);
            }
        }

        return existingSplitLineDetails;
    }

    public List<ExistingSplitLineDetailView> transformsToView(List<ExistingSplitLineDetail> existingSplitLineDetailList) {

        List<ExistingSplitLineDetailView> existingSplitLineDetailViewList = new ArrayList<ExistingSplitLineDetailView>();
        ExistingSplitLineDetailView existingSplitLineDetailView;

        for(ExistingSplitLineDetail existingSplitLineDetail : existingSplitLineDetailList){
            existingSplitLineDetailView = new ExistingSplitLineDetailView();
            existingSplitLineDetailView.setCreateBy(existingSplitLineDetail.getCreateBy());
            existingSplitLineDetailView.setCreateDate(existingSplitLineDetail.getCreateDate());
            existingSplitLineDetailView.setNo(existingSplitLineDetail.getNo());
            existingSplitLineDetailView.setProductProgram(existingSplitLineDetail.getProductProgram());
            existingSplitLineDetailView.setLimit(existingSplitLineDetail.getLimit());
            existingSplitLineDetailView.setModifyBy(existingSplitLineDetail.getModifyBy());
            existingSplitLineDetailView.setModifyDate(existingSplitLineDetail.getModifyDate());
            existingSplitLineDetailViewList.add(existingSplitLineDetailView);
        }

        return existingSplitLineDetailViewList;
    }
}
