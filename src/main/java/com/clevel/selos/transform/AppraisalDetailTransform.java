package com.clevel.selos.transform;

import com.clevel.selos.dao.working.NewCollateralDAO;
import com.clevel.selos.dao.working.NewCollateralHeadDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.SubCollateralType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.AppraisalDetailView;
import com.clevel.selos.model.view.NewCollateralHeadView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AppraisalDetailTransform extends Transform {
    @Inject
    @SELOS
    Logger log;
    @Inject
    @NormalMessage
    private Message msg;
    @Inject
    private NewCollateralHeadTransform newCollateralHeadTransform;
    @Inject
    private NewCollateralDAO newCollateralDAO;
    @Inject
    private NewCollateralHeadDAO newCollateralHeadDAO;
    private List<NewCollateral> newCollateralList;
    private List<NewCollateralHead> newCollateralHeadList;

    private NewCollateral newCollateral;
    private List<AppraisalDetailView> appraisalDetailViewList;
    @Inject
    public AppraisalDetailTransform() {

    }

    public List<NewCollateral> transformToModel(final List<AppraisalDetailView> appraisalDetailViewList, final NewCreditFacility newCreditFacility, final User user){
        log.debug("-- transform List<NewCollateral> to List<AppraisalDetailView>(Size of list is {})", ""+appraisalDetailViewList.size());
        long newCollateralId = 0;
        long newCollateralHeadId = 0;
        boolean createNewCollateralFlag = true;
        newCollateralList = Util.safetyList(newCollateralDAO.findNewCollateralByNewCreditFacility(newCreditFacility));

        for(AppraisalDetailView view : appraisalDetailViewList){
            newCollateralId = view.getNewCollateralId();
            newCollateralHeadId = view.getNewCollateralHeadId();
            log.debug("-- newCollateralId {}", newCollateralId);
            log.debug("-- newCollateralHeadId {}", newCollateralHeadId);
            if(newCollateralId != 0){
                for(NewCollateral newCollateral : newCollateralList){
                    if(newCollateral.getId() == newCollateralId){
                        log.debug("-- newCollateral.getId()[{}] == newCollateralId[{}] ",newCollateral.getId(), newCollateralId);
                        newCollateralHeadList = newCollateral.getNewCollateralHeadList();
                        for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                            if(newCollateralHead.getId() == newCollateralHeadId){
                                log.debug("-- newCollateral.getId()[{}] == newCollateralId[{}] ",newCollateral.getId(), newCollateralId);
                                log.debug("-- [BEFORE] NewCollateralHead Model {}", newCollateralHead.toString());
                                newCollateralHead.setPurposeNewAppraisal(Util.isTrue(view.isPurposeNewAppraisalB()));
                                newCollateralHead.setPurposeReviewAppraisal(Util.isTrue(view.isPurposeReviewAppraisalB()));
                                newCollateralHead.setPurposeReviewBuilding(Util.isTrue(view.isPurposeReviewBuildingB()));
                                newCollateralHead.setTitleDeed(view.getTitleDeed());
                                newCollateralHead.setCollateralChar(view.getCharacteristic());
                                newCollateralHead.setNumberOfDocuments(view.getNumberOfDocuments());
                                newCollateralHead.setModifyBy(view.getModifyBy());
                                newCollateralHead.setModifyDate(view.getModifyDate());
                                log.debug("-- [AFTER]  NewCollateralHead Model {}", newCollateralHead.toString());
                                continue;
                            }
                        }
                        continue;
                    }
                }
            } else {
                List<NewCollateralHead> newCollateralHeadListForNewCollateralHead = null;
                NewCollateralHead newCollateralHeadForNewCollateralHead = null;
                if(createNewCollateralFlag){
                    newCollateral = new NewCollateral();
                    log.debug("-- NewCollateral[NEW] created. ");
                    newCollateral.setModifyDate(DateTime.now().toDate());
                    newCollateral.setModifyBy(user);
                    newCollateral.setCreateDate(DateTime.now().toDate());
                    newCollateral.setCreateBy(user);
                    newCollateral.setNewCreditFacility(newCreditFacility);
                    createNewCollateralFlag = false;
                    log.debug("-- Change createNewCollateralFlag = false");
                }
                newCollateralHeadListForNewCollateralHead = new ArrayList<NewCollateralHead>();
                newCollateralHeadForNewCollateralHead = new NewCollateralHead();
                newCollateralHeadForNewCollateralHead.setPurposeNewAppraisal(Util.isTrue(view.isPurposeNewAppraisalB()));
                newCollateralHeadForNewCollateralHead.setPurposeReviewAppraisal(Util.isTrue(view.isPurposeReviewAppraisalB()));
                newCollateralHeadForNewCollateralHead.setPurposeReviewBuilding(Util.isTrue(view.isPurposeReviewBuildingB()));
                newCollateralHeadForNewCollateralHead.setTitleDeed(view.getTitleDeed());
                newCollateralHeadForNewCollateralHead.setCollateralChar(view.getCharacteristic());
                newCollateralHeadForNewCollateralHead.setNumberOfDocuments(view.getNumberOfDocuments());
                newCollateralHeadForNewCollateralHead.setModifyBy(view.getModifyBy());
                newCollateralHeadForNewCollateralHead.setModifyDate(view.getModifyDate());
                newCollateralHeadForNewCollateralHead.setCreateDate(DateTime.now().toDate());
                newCollateralHeadForNewCollateralHead.setCreateBy(user);

                newCollateralHeadListForNewCollateralHead.add(newCollateralHeadForNewCollateralHead);
                newCollateral.setNewCollateralHeadList(newCollateralHeadListForNewCollateralHead);
                newCollateralList.add(newCollateral);
                log.debug("-- NewCollateral {}", newCollateral.toString());
                log.debug("-- NewCollateral added to newCollateralList[Size {}]", newCollateralList.size());
            }
        }

        return newCollateralList;
    }

    public List<AppraisalDetailView> transformToView(final List<NewCollateral> newCollateralList){
        log.debug("-- transform List<NewCollateral> to List<AppraisalDetailView>(Size of list is {})", ""+newCollateralList.size());
        appraisalDetailViewList = new ArrayList<AppraisalDetailView>();
        AppraisalDetailView view = null;
        List<NewCollateralHead> newCollateralHeadList = null;
        int id = 0;
        for(NewCollateral newCollateral : newCollateralList) {
            newCollateralHeadList = Util.safetyList(newCollateral.getNewCollateralHeadList());
            for (NewCollateralHead model  : newCollateralHeadList) {
                view = new AppraisalDetailView();
                view.setNewCollateralId(newCollateral.getId());
                view.setNewCollateralHeadId(model.getId());
                view.setTitleDeed(model.getTitleDeed());
                id= model.getPurposeNewAppraisal();
                view.setPurposeNewAppraisal(id);
                view.setPurposeNewAppraisalB(Util.isTrue(id));
                id= model.getPurposeReviewAppraisal();
                view.setPurposeReviewAppraisal(model.getPurposeReviewAppraisal());
                view.setPurposeReviewAppraisalB(Util.isTrue(id));
                id= model.getPurposeReviewBuilding();
                view.setPurposeReviewBuilding(id);
                view.setPurposeReviewBuildingB(Util.isTrue(id));
                view.setCharacteristic(model.getCollateralChar());
                view.setNumberOfDocuments(model.getNumberOfDocuments());
                view.setModifyBy(model.getModifyBy());
                view.setModifyDate(model.getModifyDate());
                appraisalDetailViewList.add(view);
            }
        }
        return appraisalDetailViewList;
    }

    public List<AppraisalDetailView> updateLabel(List<AppraisalDetailView> appraisalDetailViewList){
        for(AppraisalDetailView appraisalDetailView : appraisalDetailViewList){
            if(appraisalDetailView.isPurposeReviewBuildingB()){
                appraisalDetailView.setPurposeReviewBuildingLabel(msg.get("app.appraisal.appraisalDetail.label.purposeReviewBuilding")+" ");
            }
            if(appraisalDetailView.isPurposeReviewAppraisalB()){
                appraisalDetailView.setPurposeReviewAppraisalLabel(msg.get("app.appraisal.appraisalDetail.label.purposeNewAppraisal")+" ");
            }
            if(appraisalDetailView.isPurposeNewAppraisalB()){
                appraisalDetailView.setPurposeNewAppraisalLabel(msg.get("app.appraisal.appraisalDetail.label.purposeNewAppraisal"));
            }
        }
        return appraisalDetailViewList;
    }
}
