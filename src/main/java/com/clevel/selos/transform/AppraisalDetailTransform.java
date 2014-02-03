package com.clevel.selos.transform;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Appraisal;
import com.clevel.selos.model.db.working.AppraisalPurpose;
import com.clevel.selos.model.db.working.NewCollateral;
import com.clevel.selos.model.view.AppraisalDetailView;
import com.clevel.selos.model.view.NewCollateralHeadView;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AppraisalDetailTransform extends Transform {
    @Inject
    @SELOS
    Logger log;
    @Inject
    private NewCollateralHeadTransform newCollateralHeadTransform;
    private List<NewCollateral> newCollateralList;
    private List<AppraisalDetailView> appraisalDetailViewList;
    private List<NewCollateralHeadView> newCollateralHeadViewList;
    @Inject
    public AppraisalDetailTransform() {

    }

    public List<AppraisalPurpose> transformToModel(List<AppraisalDetailView> appraisalDetailViewList,Appraisal appraisal){

    List<AppraisalPurpose> appraisalDetailList = new ArrayList<AppraisalPurpose>();
    for(AppraisalDetailView appraisalDetailView : appraisalDetailViewList){


        AppraisalPurpose appraisalDetail = new AppraisalPurpose();

        appraisalDetail.setAppraisal(appraisal);
        appraisalDetail.setNo(appraisalDetailView.getNo());
        appraisalDetail.setTitleDeed(appraisalDetailView.getTitleDeed());
        appraisalDetail.setPurposeReviewAppraisal(appraisalDetailView.getPurposeReviewAppraisal());
        appraisalDetail.setPurposeNewAppraisal(appraisalDetailView.getPurposeNewAppraisal());
        appraisalDetail.setPurposeReviewBuilding(appraisalDetailView.getPurposeReviewBuilding());
        appraisalDetail.setCharacteristic(appraisalDetailView.getCharacteristic());
        appraisalDetail.setNumberOfDocuments(appraisalDetailView.getNumberOfDocuments());
        appraisalDetail.setCreateBy(appraisalDetailView.getCreateBy());
        appraisalDetail.setCreateDate(DateTime.now().toDate());
        appraisalDetail.setModifyBy(appraisalDetailView.getModifyBy());
        appraisalDetail.setModifyDate(DateTime.now().toDate());

        appraisalDetailList.add(appraisalDetail);
    }
    return appraisalDetailList;
}











    public List<AppraisalDetailView> transformToView(final List<NewCollateral> newCollateralList, final User user){
        log.debug("-- transform List<NewCollateral> to List<AppraisalDetailView>(Size of list is {})", ""+newCollateralList.size());
        appraisalDetailViewList = new ArrayList<AppraisalDetailView>();
        AppraisalDetailView view = null;
        for(NewCollateral newCollateral : newCollateralList) {
            newCollateralHeadViewList = newCollateralHeadTransform.transformToView(Util.safetyList(newCollateral.getNewCollateralHeadList()));
            for (NewCollateralHeadView model : newCollateralHeadViewList) {
                view = new AppraisalDetailView();
                view.setId(newCollateral.getId());
                view.setTitleDeed(model.getTitleDeed());
//                view.set
//                view.setCharacteristic(model.get);
//                view.setNumberOfDocuments(model.get);
                view.setModifyBy(user);
                view.setModifyDate(model.getModifyDate());
                appraisalDetailViewList.add(view);
            }
        }
        return appraisalDetailViewList;

    }
}
