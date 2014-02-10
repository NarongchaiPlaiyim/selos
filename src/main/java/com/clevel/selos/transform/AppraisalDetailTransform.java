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
    private Logger log;
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
    private List<NewCollateral> newCollateralListForReturn;
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
        newCollateralListForReturn = new ArrayList<NewCollateral>();
        for(AppraisalDetailView view : appraisalDetailViewList){
            newCollateralId = view.getNewCollateralId();
            newCollateralHeadId = view.getNewCollateralHeadId();
            log.debug("-- newCollateralId {}", newCollateralId);
            log.debug("---- newCollateralHeadId {}", newCollateralHeadId);
            if(!Util.isZero(newCollateralId)){
                for(NewCollateral newCollateral : newCollateralList){
                    if(newCollateral.getId() == newCollateralId){
                        log.debug("---- newCollateral.getId()[{}] == newCollateralId[{}] ",newCollateral.getId(), newCollateralId);
                        newCollateralHeadList = newCollateral.getNewCollateralHeadList();
                        for(NewCollateralHead newCollateralHead : newCollateralHeadList){
                            if(newCollateralHead.getId() == newCollateralHeadId){
                                log.debug("------ NewCollateralHead.getId()[{}] == newCollateralHeadId[{}] ",newCollateralHead.getId(), newCollateralHeadId);
                                newCollateralHead.setPurposeNewAppraisal(Util.isTrue(view.isPurposeNewAppraisalB()));
//                                log.debug("------ NewCollateralHead.purposeNewAppraisal[{}]", newCollateralHead.getPurposeNewAppraisal());
                                newCollateralHead.setPurposeReviewAppraisal(Util.isTrue(view.isPurposeReviewAppraisalB()));
//                                log.debug("------ NewCollateralHead.purposeReviewAppraisal[{}]", newCollateralHead.getPurposeReviewAppraisal());
                                newCollateralHead.setPurposeReviewBuilding(Util.isTrue(view.isPurposeReviewBuildingB()));
//                                log.debug("------ NewCollateralHead.purposeReviewBuilding[{}]", newCollateralHead.getPurposeReviewBuilding());
                                newCollateralHead.setTitleDeed(view.getTitleDeed());
//                                log.debug("------ NewCollateralHead.titleDeed[{}]", newCollateralHead.getTitleDeed());
                                newCollateralHead.setCollateralChar(view.getCharacteristic());
//                                log.debug("------ NewCollateralHead.collateralChar[{}]", newCollateralHead.getCollateralChar());
                                newCollateralHead.setNumberOfDocuments(view.getNumberOfDocuments());
//                                log.debug("------ NewCollateralHead.numberOfDocuments[{}]", newCollateralHead.getNumberOfDocuments());
                                newCollateralHead.setModifyBy(user);
//                                log.debug("------ NewCollateralHead.modifyBy[{}]", newCollateralHead.getModifyBy());
                                newCollateralHead.setModifyDate(DateTime.now().toDate());
//                                log.debug("------ NewCollateralHead.modifyDate[{}]", newCollateralHead.getModifyDate());
                                newCollateralHead.setAppraisalRequest(1);
//                                log.debug("------ NewCollateralHead.appraisalRequest[{}]", newCollateralHead.getAppraisalRequest());
                                newCollateralHead.setProposeType("P");
//                                log.debug("------ NewCollateralHead.proposeType[{}]", newCollateralHead.getProposeType());

                                newCollateralListForReturn.add(newCollateral);
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
//                    log.debug("-- NewCollateral.modifyDate[{}]", newCollateral.getModifyDate());
                    newCollateral.setModifyBy(user);
//                    log.debug("-- NewCollateral.modifyBy[{}]", newCollateral.getModifyBy());
                    newCollateral.setCreateDate(DateTime.now().toDate());
//                    log.debug("-- NewCollateral.createDate[{}]", newCollateral.getCreateDate());
                    newCollateral.setCreateBy(user);
//                    log.debug("-- NewCollateral.createBy[{}]", newCollateral.getCreateBy());
                    newCollateral.setNewCreditFacility(newCreditFacility);
//                    log.debug("-- NewCollateral.newCreditFacility[{}]", newCollateral.getNewCreditFacility());
                    newCollateral.setAppraisalRequest(1);
//                    log.debug("-- NewCollateral.appraisalRequest[{}]", newCollateral.getAppraisalRequest());
                    newCollateral.setProposeType("P");
//                    log.debug("-- NewCollateral.proposeType[{}]", newCollateral.getProposeType());
                    createNewCollateralFlag = false;
                    log.debug("-- Change createNewCollateralFlag = false");
                }
                newCollateralHeadListForNewCollateralHead = new ArrayList<NewCollateralHead>();
                newCollateralHeadForNewCollateralHead = new NewCollateralHead();
                newCollateralHeadForNewCollateralHead.setPurposeNewAppraisal(Util.isTrue(view.isPurposeNewAppraisalB()));
//                log.debug("---- NewCollateralHead.purposeNewAppraisal[{}]", newCollateralHeadForNewCollateralHead.getPurposeNewAppraisal());
                newCollateralHeadForNewCollateralHead.setPurposeReviewAppraisal(Util.isTrue(view.isPurposeReviewAppraisalB()));
//                log.debug("---- NewCollateralHead.purposeReviewAppraisal[{}]", newCollateralHeadForNewCollateralHead.getPurposeReviewAppraisal());
                newCollateralHeadForNewCollateralHead.setPurposeReviewBuilding(Util.isTrue(view.isPurposeReviewBuildingB()));
//                log.debug("---- NewCollateralHead.purposeReviewBuilding[{}]", newCollateralHeadForNewCollateralHead.getPurposeReviewBuilding());
                newCollateralHeadForNewCollateralHead.setTitleDeed(view.getTitleDeed());
//                log.debug("---- NewCollateralHead.titleDeed[{}]", newCollateralHeadForNewCollateralHead.getTitleDeed());
                newCollateralHeadForNewCollateralHead.setCollateralChar(view.getCharacteristic());
//                log.debug("---- NewCollateralHead.collateralChar[{}]", newCollateralHeadForNewCollateralHead.getCollateralChar());
                newCollateralHeadForNewCollateralHead.setNumberOfDocuments(view.getNumberOfDocuments());
//                log.debug("---- NewCollateralHead.numberOfDocuments[{}]", newCollateralHeadForNewCollateralHead.getNumberOfDocuments());
                newCollateralHeadForNewCollateralHead.setModifyBy(user);
//                log.debug("---- NewCollateralHead.modifyBy[{}]", newCollateralHeadForNewCollateralHead.getModifyBy());
                newCollateralHeadForNewCollateralHead.setModifyDate(view.getModifyDate());
//                log.debug("---- NewCollateralHead.modifyDate[{}]", newCollateralHeadForNewCollateralHead.getModifyDate());
                newCollateralHeadForNewCollateralHead.setCreateDate(DateTime.now().toDate());
//                log.debug("---- NewCollateralHead.createDate[{}]", newCollateralHeadForNewCollateralHead.getCreateDate());
                newCollateralHeadForNewCollateralHead.setCreateBy(user);
//                log.debug("---- NewCollateralHead.createBy[{}]", newCollateralHeadForNewCollateralHead.getCreateBy());
                newCollateralHeadForNewCollateralHead.setAppraisalRequest(1);
//                log.debug("------ NewCollateralHead.appraisalRequest[{}]", newCollateralHeadForNewCollateralHead.getAppraisalRequest());
                newCollateralHeadForNewCollateralHead.setProposeType("P");
//                log.debug("------ NewCollateralHead.proposeType[{}]", newCollateralHeadForNewCollateralHead.getProposeType());

                newCollateralHeadListForNewCollateralHead.add(newCollateralHeadForNewCollateralHead);
                newCollateral.setNewCollateralHeadList(newCollateralHeadListForNewCollateralHead);
                newCollateralListForReturn.add(newCollateral);
                log.debug("-- NewCollateral {}", newCollateral.toString());
                log.debug("-- NewCollateral added to newCollateralList[Size {}]", newCollateralListForReturn.size());
            }
        }
        return newCollateralListForReturn;
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
        StringBuilder stringBuilder = null;
        for(AppraisalDetailView appraisalDetailView : appraisalDetailViewList){
            stringBuilder = new StringBuilder();
            if(appraisalDetailView.isPurposeReviewAppraisalB()){
                stringBuilder = stringBuilder.append(", ");
                stringBuilder = stringBuilder.append(msg.get("app.appraisal.appraisalDetail.label.purposeNewAppraisal"));
            }
            if(appraisalDetailView.isPurposeNewAppraisalB()){
                stringBuilder = stringBuilder.append(", ");
                stringBuilder = stringBuilder.append(msg.get("app.appraisal.appraisalDetail.label.purposeNewAppraisal"));
            }
            if(appraisalDetailView.isPurposeReviewBuildingB()){
                stringBuilder = stringBuilder.append(", ");
                stringBuilder = stringBuilder.append(msg.get("app.appraisal.appraisalDetail.label.purposeReviewBuilding"));
            }
            String result = stringBuilder.toString();
            if(!Util.isNull(result)){
                appraisalDetailView.setPurposeReviewAppraisalLabel(result.substring(2, result.length()));
            } else {
                appraisalDetailView.setPurposeReviewAppraisalLabel("");
            }

        }
        return appraisalDetailViewList;
    }
}
