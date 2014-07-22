package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.*;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

@Stateless
public class DecisionControl extends BusinessControl {
    @Inject
    @SELOS
    private Logger log;

    //DAO
    @Inject
    private WorkCaseDAO workCaseDAO;
    @Inject
    private ProposeLineDAO newCreditFacilityDAO;
    @Inject
    private ProposeCreditInfoDAO newCreditDetailDAO;
    @Inject
    private ProposeCollateralInfoDAO newCollateralDAO;
    @Inject
    private ProposeGuarantorInfoDAO newGuarantorDetailDAO;
    @Inject
    private DecisionFollowConditionDAO decisionFollowConditionDAO;
    @Inject
    private ApprovalHistoryDAO approvalHistoryDAO;
    @Inject
    private StepDAO stepDAO;
    @Inject
    private DecisionDAO decisionDAO;
    @Inject
    private ProposeGuarantorInfoRelationDAO newGuarantorRelationDAO;
    @Inject
    private ProposeCollateralSubMortgageDAO newSubCollMortgageDAO;
    @Inject
    private ProposeCollateralSubOwnerDAO newCollateralSubOwnerDAO;
    @Inject
    private ProposeCollateralSubRelatedDAO newCollateralSubRelatedDAO;
    @Inject
    private ProposeCollateralInfoRelationDAO newCollateralRelationDAO;
    @Inject
    private ProductProgramDAO productProgramDAO;
    @Inject
    private CreditTypeDAO creditTypeDAO;
    @Inject
    private PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;
    @Inject
    private ProductFormulaDAO productFormulaDAO;
    @Inject
    private ProposeCollateralInfoSubDAO newCollateralSubDAO;

    //Transform
    @Inject
    private ProposeLineTransform proposeLineTransform;
    @Inject
    private DecisionFollowConditionTransform decisionFollowConditionTransform;
    @Inject
    private ApprovalHistoryTransform approvalHistoryTransform;
    @Inject
    private CreditRequestTypeTransform creditRequestTypeTransform;
    @Inject
    private CountryTransform countryTransform;
    @Inject
    private StepTransform stepTransform;
    @Inject
    private RoleTransform roleTransform;
    @Inject
    private UserTransform userTransform;

    //Other Business Control
    @Inject
    private CreditFacExistingControl creditFacExistingControl;
    @Inject
    private ProposeLineControl proposeLineControl;

    @Inject
    private ProposeFeeDetailDAO proposeFeeDetailDAO;
    @Inject
    private ProposeCreditInfoTierDetailDAO proposeCreditInfoTierDetailDAO;
    @Inject
    private ProposeCreditInfoDAO proposeCreditInfoDAO;
    @Inject
    private ProposeGuarantorInfoDAO proposeGuarantorInfoDAO;
    @Inject
    private ProposeGuarantorInfoRelationDAO proposeGuarantorInfoRelationDAO;
    @Inject
    private ExistingCreditDetailDAO existingCreditDetailDAO;
    @Inject
    private SpecialProgramDAO specialProgramDAO;
    @Inject
    private ProposeLineDAO proposeLineDAO;
    @Inject
    private ProposeCollateralInfoRelationDAO proposeCollateralInfoRelationDAO;
    @Inject
    private ProposeCollateralInfoHeadDAO proposeCollateralInfoHeadDAO;
    @Inject
    private ProposeCollateralInfoSubDAO proposeCollateralInfoSubDAO;
    @Inject
    private ProposeCollateralSubOwnerDAO proposeCollateralSubOwnerDAO;
    @Inject
    private ProposeCollateralSubMortgageDAO proposeCollateralSubMortgageDAO;
    @Inject
    private ProposeCollateralSubRelatedDAO proposeCollateralSubRelatedDAO;
    @Inject
    private ProposeCollateralInfoDAO proposeCollateralInfoDAO;

    @Inject
    private BasicInfoControl basicInfoControl;
    @Inject
    private TCGInfoControl tcgInfoControl;

    @Inject
    public DecisionControl() {

    }

    public DecisionView findDecisionViewByWorkCaseId(long workCaseId) {
        log.debug("findDecisionViewByWorkCaseId :: workCaseId :: {}", workCaseId);

        ExistingCreditFacilityView existingCreditFacilityView = creditFacExistingControl.onFindExistingCreditFacility(workCaseId);
        ProposeLineView proposeLineView = proposeLineControl.findProposeLineViewByWorkCaseId(workCaseId);
        Decision decision = decisionDAO.findByWorkCaseId(workCaseId);

        DecisionView decisionView = proposeLineTransform.transformToDecisionView(existingCreditFacilityView, proposeLineView, decision, workCaseId);

        return decisionView;
    }

    /*public DecisionView saveApproveAndConditionData(DecisionView decisionView, WorkCase workCase) {
        log.debug("saveApproveAndConditionData ::: decisionView :: {}, workCase: {}", decisionView, workCase != null ? workCase.getId() : "0");

        Cloner cloner = new Cloner();
        DecisionView decisionViewReturn = cloner.deepClone(decisionView);

        if (workCase != null) {
            User currentUser = getCurrentUser();

            // Decision Follow up Condition
            if (decisionView.getDecisionFollowConditionViewList() != null && decisionView.getDecisionFollowConditionViewList().size() > 0) {
                List<DecisionFollowCondition> decFollowConList = decisionFollowConditionTransform.transformToModel(decisionView.getDecisionFollowConditionViewList(), workCase);
                decisionFollowConditionDAO.persist(decFollowConList);
                log.debug("decFollowConList :: {}",decFollowConList);
                decisionViewReturn.setDecisionFollowConditionViewList(decisionFollowConditionTransform.transformToView(decFollowConList));
            }

            ProposeLine proposeLine = newCreditFacilityDAO.findByWorkCaseId(workCase.getId());
            log.debug("find proposeLine: {}", proposeLine);

            // Approve Credit Detail
            if (decisionView.getApproveCreditList() != null && decisionView.getApproveCreditList().size() > 0) {
                List<ProposeCreditInfo> newCreditDetailList = proposeLineTransform.transformProposeCreditToModelList(proposeLine, decisionView.getApproveCreditList(), workCase, currentUser, ProposeType.A);
                newCreditDetailDAO.persist(newCreditDetailList);
                log.debug("decFollowConList :: {}",newCreditDetailList);
                decisionViewReturn.setApproveCreditList(proposeLineTransform.transformProposeCreditToViewList(newCreditDetailList));
            }

            // Approve Guarantor Detail
            if (decisionView.getApproveGuarantorList() != null && decisionView.getApproveGuarantorList().size() > 0) {
                List<ProposeGuarantorInfoRelation> relationDeleteList = newGuarantorRelationDAO.getListByNewCreditFacilityId(proposeLine.getId(), ProposeType.A);
                if(relationDeleteList != null && relationDeleteList.size() > 0) {
                    log.info("Guarantor Relation - deleteList size: {}",relationDeleteList.size());
                    newGuarantorRelationDAO.delete(relationDeleteList);
                }
                List<ProposeGuarantorInfo> newGuarantorDetailList = new ArrayList<ProposeGuarantorInfo>();
                for(ProposeGuarantorInfoView proposeGuarantorInfoView : decisionView.getApproveGuarantorList()) {
                    ProposeGuarantorInfo proposeGuarantorInfo = proposeLineTransform.transformProposeGuarantorToModel(proposeLine, proposeGuarantorInfoView, currentUser, ProposeType.A);
                    newGuarantorDetailList.add(proposeGuarantorInfo);
                }
                if(newGuarantorDetailList.size() != 0){
                    newGuarantorDetailDAO.persist(newGuarantorDetailList);
                }
                log.debug("decFollowConList :: {}",newGuarantorDetailList);
                decisionViewReturn.setApproveGuarantorList(proposeLineTransform.transformProposeGuarantorToViewList(newGuarantorDetailList, ProposeType.A));
            }

            // Approve Collateral
            if (decisionView.getApproveCollateralList() != null && decisionView.getApproveCollateralList().size() > 0) {
                List<ProposeCollateralInfo> tmpNewCollateralList = newCollateralDAO.findNewCollateralByTypeA(proposeLine);
                if (tmpNewCollateralList != null && tmpNewCollateralList.size() > 0) {
                    for (ProposeCollateralInfo newCollateral : tmpNewCollateralList) {
                        // delete old collateral relation
                        if (newCollateral.getProposeCollateralInfoRelationList() != null) {
                            newCollateralRelationDAO.delete(newCollateral.getProposeCollateralInfoRelationList());
                            newCollateral.setProposeCollateralInfoRelationList(Collections.<ProposeCollateralInfoRelation>emptyList());
                        }

                        List<ProposeCollateralInfoHead> newCollateralHeadList = newCollateral.getProposeCollateralInfoHeadList();
                        for (ProposeCollateralInfoHead newCollateralHead : newCollateralHeadList) {
                            List<ProposeCollateralInfoSub> newCollateralSubList = newCollateralHead.getProposeCollateralInfoSubList();
                            for (ProposeCollateralInfoSub newCollateralSub : newCollateralSubList) {
                                newSubCollMortgageDAO.delete(newCollateralSub.getProposeCollateralSubMortgageList());
                                newCollateralSubOwnerDAO.delete(newCollateralSub.getProposeCollateralSubOwnerList());
                                newCollateralSub.setProposeCollateralSubMortgageList(Collections.<ProposeCollateralSubMortgage>emptyList());
                                newCollateralSub.setProposeCollateralSubOwnerList(Collections.<ProposeCollateralSubOwner>emptyList());
                            }
                        }
                        newCollateralDAO.persist(newCollateral);
                    }
                }

                List<ProposeCollateralInfo> newCollateralList = new ArrayList<ProposeCollateralInfo>();
                for(ProposeCollateralInfoView proposeCollateralInfoView : decisionView.getApproveCollateralList()) {
                    ProposeCollateralInfo proposeCollateralInfo = proposeLineTransform.transformProposeCollateralToModel(workCase, proposeLine, proposeCollateralInfoView, currentUser, ProposeType.A);
                    newCollateralList.add(proposeCollateralInfo);
                }
                if(newCollateralList.size() != 0){
                    newCollateralDAO.persist(newCollateralList);
                }
                log.debug("decFollowConList :: {}",newCollateralList);
                decisionViewReturn.setApproveCollateralList(proposeLineTransform.transformProposeCollateralToViewList(newCollateralList, ProposeType.A));
            }
        }
        log.debug("decisionViewReturn :: {}", decisionViewReturn);
        return decisionViewReturn;
    }*/

    public void saveApproveAndCondition(DecisionView decisionView, long workCaseId, Hashtable hashSeqCredit) {
        log.debug("saveApproveAndCondition :: workCaseId :: {}, decisionView :: {}",workCaseId, decisionView);
        ProposeType proposeType = ProposeType.A;
        User currentUser = getCurrentUser();
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        BasicInfoView basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
        int specialProgramId = 0;
        int applyTCG = 0;
        TCGView tcgView = tcgInfoControl.getTCGView(workCaseId);
        if (!Util.isNull(tcgView)) {
            applyTCG = tcgView.getTCG();
        }

        if(!Util.isNull(decisionView)) {
            //Remove All Fee Detail
            List<ProposeFeeDetail> proposeFeeDetailList = proposeFeeDetailDAO.findByWorkCaseId(workCaseId, proposeType);
            if(!Util.isNull(proposeFeeDetailList) && !Util.isZero(proposeFeeDetailList.size())) {
                proposeFeeDetailDAO.delete(proposeFeeDetailList);
            }

            //Remove Tier On Credit Info Detail
            if(!Util.isNull(decisionView.getApproveCreditList()) && !Util.isZero(decisionView.getApproveCreditList().size())) {
                for(ProposeCreditInfoDetailView proCreditDetailView : decisionView.getApproveCreditList()) {
                    if(!Util.isNull(proCreditDetailView)) {
                        if(!Util.isNull(proCreditDetailView.getDeleteCreditTierIdList()) && !Util.isZero(proCreditDetailView.getDeleteCreditTierIdList().size())) {
                            for(Long deleteId : proCreditDetailView.getDeleteCreditTierIdList()) {
                                if(!Util.isZero(deleteId)) {
                                    ProposeCreditInfoTierDetail proposeCreditInfoTierDetail = proposeCreditInfoTierDetailDAO.findById(deleteId);
                                    if(!Util.isNull(proposeCreditInfoTierDetail)) {
                                        proposeCreditInfoTierDetailDAO.delete(proposeCreditInfoTierDetail);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //Remove Credit Detail On Propose Line
            if(!Util.isNull(decisionView.getDeleteCreditIdList()) && !Util.isZero(decisionView.getDeleteCreditIdList().size())) {
                for(Long deleteId : decisionView.getDeleteCreditIdList()) {
                    if(!Util.isZero(deleteId)) {
                        ProposeCreditInfo proposeCreditInfo = proposeCreditInfoDAO.findById(deleteId);
                        if(!Util.isNull(proposeCreditInfo.getProposeCreditInfoTierDetailList()) && !Util.isZero(proposeCreditInfo.getProposeCreditInfoTierDetailList().size())) {
                            proposeCreditInfoTierDetailDAO.delete(proposeCreditInfo.getProposeCreditInfoTierDetailList());
                        }
                        if(!Util.isNull(proposeCreditInfo)) {
                            proposeCreditInfoDAO.delete(proposeCreditInfo);
                        }
                    }
                }
            }

            //Remove Condition Info On Propose Line
            if(!Util.isNull(decisionView.getDeleteFollowConditionIdList()) && !Util.isZero(decisionView.getDeleteFollowConditionIdList().size())) {
                for(Long deleteId : decisionView.getDeleteFollowConditionIdList()) {
                    if(!Util.isZero(deleteId)) {
                        DecisionFollowCondition decisionFollowCondition = decisionFollowConditionDAO.findById(deleteId);
                        if(!Util.isNull(decisionFollowCondition)) {
                            decisionFollowConditionDAO.delete(decisionFollowCondition);
                        }
                    }
                }
            }
        }


        if (basicInfoView != null) {
            if (basicInfoView.getSpProgram() == RadioValue.YES.value()) {
                if(!Util.isNull(basicInfoView.getSpecialProgram()) && !Util.isZero(basicInfoView.getSpecialProgram().getId())){
                    specialProgramId = basicInfoView.getSpecialProgram().getId();
                }
            } else {
                SpecialProgram specialProgram = specialProgramDAO.findById(3);
                if(!Util.isNull(specialProgram) && !Util.isZero(specialProgram.getId())) {
                    specialProgramId = specialProgram.getId();
                }
            }
        }

        //Cal Installment
        if(!Util.isNull(decisionView.getApproveCreditList()) && !Util.isZero(decisionView.getApproveCreditList().size())) {
            List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList = new ArrayList<ProposeCreditInfoDetailView>();
            for (ProposeCreditInfoDetailView proCreInfDetView : decisionView.getApproveCreditList()) {
                proCreInfDetView = onCalInstallment(decisionView, proCreInfDetView, specialProgramId, applyTCG);
                proposeCreditInfoDetailViewList.add(proCreInfDetView);
            }
            decisionView.setApproveCreditList(proposeCreditInfoDetailViewList);
        }

        //Set Use Count for Propose Credit
        if(!Util.isNull(decisionView.getApproveCreditList()) && !Util.isZero(decisionView.getApproveCreditList().size())) {
            for (ProposeCreditInfoDetailView proCreInfDetView : decisionView.getApproveCreditList()) {
                if(hashSeqCredit.containsKey(proCreInfDetView.getSeq())) {
                    int seq = (Integer)hashSeqCredit.get(proCreInfDetView.getSeq());
                    proCreInfDetView.setUseCount(seq);
                }
            }
        }

        ProposeLine decision = proposeLineTransform.transformDecisionToModel(decisionView, workCase, currentUser, proposeType);

        if(!Util.isNull(decision)) {
            proposeLineDAO.persist(decision);
            if(!Util.isNull(decision.getProposeCreditInfoList()) && !Util.isZero(decision.getProposeCreditInfoList().size())) {
                proposeCreditInfoDAO.persist(decision.getProposeCreditInfoList());
                for(ProposeCreditInfo proposeCreditInfo : decision.getProposeCreditInfoList()) {
                    if(!Util.isNull(proposeCreditInfo.getProposeCreditInfoTierDetailList()) && !Util.isZero(proposeCreditInfo.getProposeCreditInfoTierDetailList().size())) {
                        proposeCreditInfoTierDetailDAO.persist(proposeCreditInfo.getProposeCreditInfoTierDetailList());
                    }
                }
            }

            // Decision Follow up Condition
            if (decisionView.getDecisionFollowConditionViewList() != null && decisionView.getDecisionFollowConditionViewList().size() > 0) {
                List<DecisionFollowCondition> decFollowConList = decisionFollowConditionTransform.transformToModel(decisionView.getDecisionFollowConditionViewList(), workCase);
                decisionFollowConditionDAO.persist(decFollowConList);
            }

            // ------------------------------------------ Guarantor ------------------------------------------- //
            //Remove All Guarantor Relation By Propose Line
            if(!Util.isNull(decisionView) && !Util.isZero(decisionView.getId())){
                List<ProposeGuarantorInfoRelation> proposeGuarantorInfoRelationList = proposeGuarantorInfoRelationDAO.findByProposeLine(decisionView.getId(), proposeType);
                if(!Util.isNull(proposeGuarantorInfoRelationList) && !Util.isZero(proposeGuarantorInfoRelationList.size())) {
                    proposeGuarantorInfoRelationDAO.delete(proposeGuarantorInfoRelationList);
                }
            }

            //Remove Guarantor Info On Propose Line
            if(!Util.isNull(decisionView.getDeleteGuarantorIdList()) && !Util.isZero(decisionView.getDeleteGuarantorIdList().size())) {
                for(Long deleteId : decisionView.getDeleteGuarantorIdList()) {
                    if(!Util.isZero(deleteId)) {
                        ProposeGuarantorInfo proposeGuarantorInfo = proposeGuarantorInfoDAO.findById(deleteId);
                        if(!Util.isNull(proposeGuarantorInfo)) {
                            proposeGuarantorInfoDAO.delete(proposeGuarantorInfo);
                        }
                    }
                }
            }

            BigDecimal sumGuaranteeAmount = BigDecimal.ZERO;
            if(!Util.isNull(decisionView.getApproveGuarantorList()) && !Util.isZero(decisionView.getApproveGuarantorList().size())) {
                for(ProposeGuarantorInfoView proposeGuarantorInfoView : decisionView.getApproveGuarantorList()) {
                    ProposeGuarantorInfo proposeGuarantorInfo = proposeLineTransform.transformProposeGuarantorToModel(decision, proposeGuarantorInfoView, currentUser, proposeType);
                    proposeGuarantorInfoDAO.persist(proposeGuarantorInfo);

                    if(!Util.isNull(proposeGuarantorInfoView.getProposeCreditInfoDetailViewList()) && !Util.isZero(proposeGuarantorInfoView.getProposeCreditInfoDetailViewList().size())) {
                        for(ProposeCreditInfoDetailView proposeCreditInfoDetailView : proposeGuarantorInfoView.getProposeCreditInfoDetailViewList()) {
                            if(proposeCreditInfoDetailView.isNoFlag()) {
                                ExistingCreditDetail existingCreditDetail = null;
                                ProposeCreditInfo proposeCreditInfo = null;
                                if(proposeCreditInfoDetailView.isExistingCredit()) {
                                    existingCreditDetail = existingCreditDetailDAO.findById(proposeCreditInfoDetailView.getId());
                                } else { // can't find by id coz propose credit id is zero
                                    proposeCreditInfo = proposeCreditInfoDAO.findBySeqAndPropose(proposeCreditInfoDetailView.getSeq(), decision, proposeType);
                                }
                                ProposeGuarantorInfoRelation proposeGuarantorInfoRelation = proposeLineTransform.transformProposeGuarantorRelationToModel(decision, proposeGuarantorInfo, proposeCreditInfo, existingCreditDetail , proposeType, currentUser, proposeCreditInfoDetailView.getGuaranteeAmount());

                                proposeGuarantorInfoRelationDAO.persist(proposeGuarantorInfoRelation);
                            }
                        }
                    }
                    sumGuaranteeAmount = Util.add(sumGuaranteeAmount, proposeGuarantorInfo.getTotalLimitGuaranteeAmount());
                }
            }

            decision.setTotalGuaranteeAmount(sumGuaranteeAmount);
            proposeLineDAO.persist(decision);

            //Save Propose Fee Detail
            List<ProposeFeeDetail> proposeFeeDetailList = proposeLineTransform.transformProposeFeeToModelList(decision, decisionView.getApproveFeeDetailViewOriginalList(), proposeType);
            if(!Util.isNull(proposeFeeDetailList) && !Util.isZero(proposeFeeDetailList.size())) {
                proposeFeeDetailDAO.persist(proposeFeeDetailList);
            }

            // ------------------------------------------ Collateral ------------------------------------------- //
            //Remove All Collateral Relation By Propose Line
            if(!Util.isNull(decisionView) && !Util.isZero(decisionView.getId())){
                List<ProposeCollateralInfoRelation> proposeCollateralInfoRelationList = proposeCollateralInfoRelationDAO.findByProposeLine(decisionView.getId(), proposeType);
                if(!Util.isNull(proposeCollateralInfoRelationList) && !Util.isZero(proposeCollateralInfoRelationList.size())) {
                    proposeCollateralInfoRelationDAO.delete(proposeCollateralInfoRelationList);
                }
            }

            //Remove All List On Sub Collateral
            List<ProposeCollateralSubOwner> proposeCollateralSubOwnerList = proposeCollateralSubOwnerDAO.findByWorkCaseId(workCaseId, proposeType);
            if(!Util.isNull(proposeCollateralSubOwnerList) && !Util.isZero(proposeCollateralSubOwnerList.size())) {
                proposeCollateralSubOwnerDAO.delete(proposeCollateralSubOwnerList);
            }
            List<ProposeCollateralSubMortgage> proposeCollateralSubMortgageList = proposeCollateralSubMortgageDAO.findByWorkCaseId(workCaseId, proposeType);
            if(!Util.isNull(proposeCollateralSubMortgageList) && !Util.isZero(proposeCollateralSubMortgageList.size())) {
                proposeCollateralSubMortgageDAO.delete(proposeCollateralSubMortgageList);
            }
            List<ProposeCollateralSubRelated> proposeCollateralSubRelatedList = proposeCollateralSubRelatedDAO.findByWorkCaseId(workCaseId, proposeType);
            if(!Util.isNull(proposeCollateralSubRelatedList) && !Util.isZero(proposeCollateralSubRelatedList.size())) {
                proposeCollateralSubRelatedDAO.delete(proposeCollateralSubRelatedList);
            }

            //Remove Collateral Sub
            if(!Util.isNull(decisionView.getApproveCollateralList()) && !Util.isZero(decisionView.getApproveCollateralList().size())) {
                for(ProposeCollateralInfoView proCollView : decisionView.getApproveCollateralList()) {
                    if(!Util.isNull(proCollView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proCollView.getProposeCollateralInfoHeadViewList().size())) {
                        for(ProposeCollateralInfoHeadView proCollHeadView : proCollView.getProposeCollateralInfoHeadViewList()) {
                            if(!Util.isNull(proCollHeadView.getDeleteSubColHeadIdList()) && !Util.isZero(proCollHeadView.getDeleteSubColHeadIdList().size())) {
                                for(Long deleteId : proCollHeadView.getDeleteSubColHeadIdList()) {
                                    ProposeCollateralInfoSub proCollSub = proposeCollateralInfoSubDAO.findById(deleteId);
                                    if(!Util.isNull(proCollSub)) {
                                        proposeCollateralInfoSubDAO.delete(proCollSub);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //Remove Collateral Info On Propose Line
            if(!Util.isNull(decisionView.getDeleteCollateralIdList()) && !Util.isZero(decisionView.getDeleteCollateralIdList().size())) {
                for(Long deleteId : decisionView.getDeleteCollateralIdList()) {
                    if(!Util.isZero(deleteId)) {
                        ProposeCollateralInfo proposeCollateralInfo = proposeCollateralInfoDAO.findById(deleteId);
                        if(!Util.isNull(proposeCollateralInfo)) {
                            if(!Util.isNull(proposeCollateralInfo.getProposeCollateralInfoHeadList()) && !Util.isZero(proposeCollateralInfo.getProposeCollateralInfoHeadList().size())){
                                for(ProposeCollateralInfoHead proposeCollateralInfoHead : proposeCollateralInfo.getProposeCollateralInfoHeadList()) {
                                    if(!Util.isNull(proposeCollateralInfoHead) && !Util.isNull(proposeCollateralInfoHead.getProposeCollateralInfoSubList()) && !Util.isZero(proposeCollateralInfoHead.getProposeCollateralInfoSubList().size())) {
                                        proposeCollateralInfoSubDAO.delete(proposeCollateralInfoHead.getProposeCollateralInfoSubList());
                                    }
                                }
                                proposeCollateralInfoHeadDAO.delete(proposeCollateralInfo.getProposeCollateralInfoHeadList());
                            }
                            proposeCollateralInfoDAO.delete(proposeCollateralInfo);
                        }
                    }
                }
            }

            //Save Collateral
            if(!Util.isNull(decisionView.getApproveCollateralList()) && !Util.isZero(decisionView.getApproveCollateralList().size())) {
                for(ProposeCollateralInfoView proposeCollateralInfoView : decisionView.getApproveCollateralList()) {
                    ProposeCollateralInfo proposeCollateralInfo = proposeLineTransform.transformProposeCollateralToModel(workCase, decision, proposeCollateralInfoView, currentUser, proposeType);
                    proposeCollateralInfoDAO.persist(proposeCollateralInfo);
                    if(!Util.isNull(proposeCollateralInfo) && !Util.isNull(proposeCollateralInfo.getProposeCollateralInfoHeadList()) && !Util.isZero(proposeCollateralInfo.getProposeCollateralInfoHeadList().size())) {
                        for(ProposeCollateralInfoHead proposeCollateralInfoHead : proposeCollateralInfo.getProposeCollateralInfoHeadList()) {
                            proposeCollateralInfoHeadDAO.persist(proposeCollateralInfoHead);
                            if(!Util.isNull(proposeCollateralInfoHead) && !Util.isNull(proposeCollateralInfoHead.getProposeCollateralInfoSubList()) && !Util.isZero(proposeCollateralInfoHead.getProposeCollateralInfoSubList().size())) {
                                for(ProposeCollateralInfoSub proposeCollateralInfoSub : proposeCollateralInfoHead.getProposeCollateralInfoSubList()) {
                                    proposeCollateralInfoSubDAO.persist(proposeCollateralInfoSub);
                                    if(!Util.isNull(proposeCollateralInfoSub) && !Util.isNull(proposeCollateralInfoSub.getProposeCollateralSubOwnerList()) && !Util.isZero(proposeCollateralInfoSub.getProposeCollateralSubOwnerList().size())) {
                                        proposeCollateralSubOwnerDAO.persist(proposeCollateralInfoSub.getProposeCollateralSubOwnerList());
                                    }
                                    if(!Util.isNull(proposeCollateralInfoSub) && !Util.isNull(proposeCollateralInfoSub.getProposeCollateralSubMortgageList()) && !Util.isZero(proposeCollateralInfoSub.getProposeCollateralSubMortgageList().size())) {
                                        proposeCollateralSubMortgageDAO.persist(proposeCollateralInfoSub.getProposeCollateralSubMortgageList());
                                    }
                                }
                            }
                        }
                    }
                    //after persist all collateral sub
                    if (!Util.isNull(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCollateralInfoHeadViewList().size())) {
                        for (ProposeCollateralInfoHeadView proposeCollateralInfoHeadView : proposeCollateralInfoView.getProposeCollateralInfoHeadViewList()) {
                            if (!Util.isNull(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList()) && !Util.isZero(proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList().size())) {
                                for (ProposeCollateralInfoSubView proposeCollateralInfoSubView : proposeCollateralInfoHeadView.getProposeCollateralInfoSubViewList()) {
                                    ProposeCollateralInfoSub mainCollSub = proposeCollateralInfoSubDAO.findBySubId(proposeCollateralInfoSubView.getSubId());
                                    if (!Util.isNull(proposeCollateralInfoSubView.getRelatedWithList()) && !Util.isZero(proposeCollateralInfoSubView.getRelatedWithList().size())) {
                                        for (ProposeCollateralInfoSubView relatedCollSubView : proposeCollateralInfoSubView.getRelatedWithList()) {
                                            ProposeCollateralInfoSub relatedCollSub = proposeCollateralInfoSubDAO.findBySubId(relatedCollSubView.getSubId());
                                            ProposeCollateralSubRelated proposeCollateralSubRelated = proposeLineTransform.transformProposeCollateralSubRelatedToModel(workCase, mainCollSub, relatedCollSub, proposeType);
                                            proposeCollateralSubRelatedDAO.persist(proposeCollateralSubRelated);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if(!Util.isNull(proposeCollateralInfoView.getProposeCreditInfoDetailViewList()) && !Util.isZero(proposeCollateralInfoView.getProposeCreditInfoDetailViewList().size())) {
                        for(ProposeCreditInfoDetailView proposeCreditInfoDetailView : proposeCollateralInfoView.getProposeCreditInfoDetailViewList()) {
                            if(proposeCreditInfoDetailView.isNoFlag()) {
                                ExistingCreditDetail existingCreditDetail = null;
                                ProposeCreditInfo proposeCreditInfo = null;
                                if(proposeCreditInfoDetailView.isExistingCredit()) {
                                    existingCreditDetail = existingCreditDetailDAO.findById(proposeCreditInfoDetailView.getId());
                                } else { // can't find by id coz propose credit id is zero
                                    proposeCreditInfo = proposeCreditInfoDAO.findBySeqAndPropose(proposeCreditInfoDetailView.getSeq(), decision, proposeType);
                                }
                                ProposeCollateralInfoRelation proposeCollateralInfoRelation = proposeLineTransform.transformProposeCollateralRelationToModel(decision, proposeCollateralInfo, proposeCreditInfo, existingCreditDetail , proposeType, currentUser);

                                proposeCollateralInfoRelationDAO.persist(proposeCollateralInfoRelation);
                            }
                        }
                    }
                }
            }
        }
    }

    public ProposeCreditInfoDetailView onCalInstallment(DecisionView decisionView, ProposeCreditInfoDetailView proposeCreditInfoDetailView, int specialProgramId, int applyTCG){
        if(!Util.isNull(proposeCreditInfoDetailView) && !Util.isNull(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList()) && !Util.isZero(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().size())) {
            if(proposeCreditInfoDetailView.getLimit().compareTo(BigDecimal.ZERO) < 0) { // limit < 0
                return proposeCreditInfoDetailView;
            }
            for(ProposeCreditInfoTierDetailView proCreInfTieDetView : proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList()) {
                if(proCreInfTieDetView.getBrmsFlag() == 1){ // cal only brms
                    if(proCreInfTieDetView.getTenor() < 0){ // if tenor < 0
                        return proposeCreditInfoDetailView;
                    }

                    if(proposeCreditInfoDetailView.isReduceFrontEndFee()){ // on save front end fee to original only , cal save on front end fee only
                        proposeCreditInfoDetailView.setFrontEndFee(Util.subtract(proposeCreditInfoDetailView.getFrontEndFeeOriginal(),decisionView.getFrontendFeeDOA()));
                    } else {
                        proposeCreditInfoDetailView.setFrontEndFee(proposeCreditInfoDetailView.getFrontEndFeeOriginal());
                    }

                    BigDecimal standard = BigDecimal.ZERO;
                    BigDecimal suggest = BigDecimal.ZERO;
                    if(!Util.isNull(proposeCreditInfoDetailView.getStandardBaseRate())){
                        standard  = Util.add(proposeCreditInfoDetailView.getStandardBaseRate().getValue(), proposeCreditInfoDetailView.getStandardInterest());
                    }
                    if(!Util.isNull(proposeCreditInfoDetailView.getSuggestBaseRate())){
                        suggest = Util.add(proposeCreditInfoDetailView.getSuggestBaseRate().getValue(), proposeCreditInfoDetailView.getSuggestInterest());
                    }

                    if(standard.compareTo(suggest) > 0){
                        proCreInfTieDetView.setFinalBasePrice(proposeCreditInfoDetailView.getStandardBaseRate());
                        proCreInfTieDetView.setFinalInterest(proposeCreditInfoDetailView.getStandardInterest());
                        proCreInfTieDetView.setFinalInterestOriginal(proposeCreditInfoDetailView.getStandardInterest());
                    } else {
                        proCreInfTieDetView.setFinalBasePrice(proposeCreditInfoDetailView.getSuggestBaseRate());
                        proCreInfTieDetView.setFinalInterest(proposeCreditInfoDetailView.getSuggestInterest());
                        proCreInfTieDetView.setFinalInterestOriginal(proposeCreditInfoDetailView.getSuggestInterest());
                    }

                    if(proposeCreditInfoDetailView.isReducePriceFlag()){
                        proCreInfTieDetView.setFinalInterest(Util.subtract(proCreInfTieDetView.getFinalInterestOriginal(),decisionView.getIntFeeDOA())); // reduce interest
                    }

                    BigDecimal twelve = new BigDecimal(12);
                    BigDecimal oneHundred = new BigDecimal(100);
                    BigDecimal baseRate = BigDecimal.ZERO;
                    BigDecimal interest = BigDecimal.ZERO;
                    BigDecimal interestOriginal = BigDecimal.ZERO;
                    BigDecimal spread = BigDecimal.ZERO;

                    if((!Util.isNull(proposeCreditInfoDetailView.getProductProgramView()) && !Util.isZero(proposeCreditInfoDetailView.getProductProgramView().getId())) &&
                            (!Util.isNull(proposeCreditInfoDetailView.getCreditTypeView()) && !Util.isZero(proposeCreditInfoDetailView.getCreditTypeView().getId()))){

                    }

                    PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(proposeCreditInfoDetailView.getCreditTypeView().getId(), proposeCreditInfoDetailView.getProductProgramView().getId());

                    if(!Util.isNull(prdProgramToCreditType)){
                        ProductFormula productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType, decisionView.getCreditCustomerType().value(), specialProgramId, applyTCG);
                        if(!Util.isNull(productFormula)){
                            spread = productFormula.getDbrSpread();
                        }
                    }

                    if (proCreInfTieDetView.getFinalBasePrice() != null) {
                        baseRate = proCreInfTieDetView.getFinalBasePrice().getValue();
                    }
                    if (proCreInfTieDetView.getFinalInterest() != null) {
                        interest = proCreInfTieDetView.getFinalInterest();
                        interestOriginal = proCreInfTieDetView.getFinalInterestOriginal();
                    }

                    BigDecimal interestPerMonth = Util.divide(Util.divide(Util.add(Util.add(spread,baseRate),interest),oneHundred,100),twelve,100);
                    BigDecimal interestPerMonthOriginal = Util.divide(Util.divide(Util.add(Util.add(spread,baseRate),interestOriginal),oneHundred,100),twelve,100);
                    BigDecimal limit = BigDecimal.ZERO;
                    int tenor = proCreInfTieDetView.getTenor();
                    BigDecimal installment;
                    BigDecimal installmentOriginal;

                    if (proposeCreditInfoDetailView.getLimit() != null) {
                        limit = proposeCreditInfoDetailView.getLimit();
                    }

                    installment = Util.divide(Util.multiply(Util.multiply(interestPerMonth,limit),Util.add(BigDecimal.ONE,interestPerMonth).pow(tenor)) ,
                            Util.subtract(Util.add(BigDecimal.ONE,interestPerMonth).pow(tenor),BigDecimal.ONE));

                    installmentOriginal = Util.divide(Util.multiply(Util.multiply(interestPerMonthOriginal,limit),Util.add(BigDecimal.ONE,interestPerMonthOriginal).pow(tenor)) ,
                            Util.subtract(Util.add(BigDecimal.ONE,interestPerMonthOriginal).pow(tenor),BigDecimal.ONE));

                    if (installment != null) {
                        installment.setScale(2, RoundingMode.HALF_UP);
                    }

                    if (installmentOriginal != null) {
                        installmentOriginal.setScale(2, RoundingMode.HALF_UP);
                    }

                    proCreInfTieDetView.setInstallment(installment);
                    proCreInfTieDetView.setInstallmentOriginal(installmentOriginal);
                } else {
                    proCreInfTieDetView.setInstallmentOriginal(proCreInfTieDetView.getInstallment());
                }
            }
        }

        if (!Util.isNull(proposeCreditInfoDetailView)) {
            BigDecimal sumOfPCE = Util.multiply(proposeCreditInfoDetailView.getLimit(), proposeCreditInfoDetailView.getPCEPercent());
            BigDecimal sum = Util.divide(sumOfPCE, BigDecimal.valueOf(100));

            if (!Util.isNull(sum)){
                sum.setScale(2, RoundingMode.HALF_UP);
            }
            proposeCreditInfoDetailView.setPCEAmount(sum);
        }

        if(!Util.isNull(proposeCreditInfoDetailView) && !Util.isNull(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList()) && !Util.isZero(proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList().size())) {
            BigDecimal mostInstallment = BigDecimal.ZERO;
            for(ProposeCreditInfoTierDetailView proposeCreditInfoTierDetailView : proposeCreditInfoDetailView.getProposeCreditInfoTierDetailViewList()) {
                if(mostInstallment.compareTo(proposeCreditInfoTierDetailView.getInstallmentOriginal()) < 0){
                    mostInstallment = proposeCreditInfoTierDetailView.getInstallmentOriginal();
                }
            }
            proposeCreditInfoDetailView.setInstallment(mostInstallment);
        }

        return proposeCreditInfoDetailView;
    }

    /*public void saveRelatedSubColl(WorkCase workCase, List<ProposeCollateralInfoView> approveCollateralList) {

        if (workCase != null && approveCollateralList != null && approveCollateralList.size() > 0) {
            //remove all relate in this work case
            List<NewCollateralSubRelated> newCollateralSubRelatedList = newCollateralSubRelatedDAO.getListByWorkCase(workCase, ProposeType.A);
            if (newCollateralSubRelatedList != null) {
                newCollateralSubRelatedDAO.delete(newCollateralSubRelatedList);
            }

            //save related sub coll
            for (NewCollateralView newCollateralView : approveCollateralList) {
                if (newCollateralView.getNewCollateralHeadViewList() != null && newCollateralView.getNewCollateralHeadViewList().size() > 0) {

                    for (NewCollateralHeadView newCollateralHeadView : newCollateralView.getNewCollateralHeadViewList()) {
                        if (newCollateralHeadView.getNewCollateralSubViewList() != null && newCollateralHeadView.getNewCollateralSubViewList().size() > 0) {

                            for (NewCollateralSubView newCollateralSubView : newCollateralHeadView.getNewCollateralSubViewList()) {
                                NewCollateralSub mainNewCollSub = newCollateralSubDAO.findBySubId(newCollateralSubView.getSubId());
                                if (newCollateralSubView.getRelatedWithList() != null && newCollateralSubView.getRelatedWithList().size() > 0) {

                                    for (NewCollateralSubView related : newCollateralSubView.getRelatedWithList()) {
                                        NewCollateralSub relatedNewCollSub = newCollateralSubDAO.findBySubId(related.getSubId());
                                        NewCollateralSubRelated newCollateralSubRelated = new NewCollateralSubRelated();
                                        newCollateralSubRelated.setWorkCase(workCase);
                                        newCollateralSubRelated.setNewCollateralSub(mainNewCollSub);
                                        newCollateralSubRelated.setNewCollateralSubRelated(relatedNewCollSub);
                                        newCollateralSubRelated.setProposeType(ProposeType.A);
                                        newCollateralSubRelatedDAO.persist(newCollateralSubRelated);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }*/

    public ApprovalHistoryView saveApprovalHistory(ApprovalHistoryView approvalHistoryView, WorkCase workCase) {
        log.debug("saveApprovalHistory() workCase: {}", workCase);
        // Set current time for submit
        //approvalHistoryView.setSubmitDate(DateTime.now().toDate());
        approvalHistoryView.setApprovalType(ApprovalType.CA_APPROVAL.value());
        ApprovalHistory returnApprovalHistory = approvalHistoryDAO.persist(approvalHistoryTransform.transformToModel(approvalHistoryView, workCase));
        return approvalHistoryTransform.transformToView(returnApprovalHistory);
    }

    public ApprovalHistoryView saveApprovalHistoryPricing(ApprovalHistoryView approvalPricingHistoryView, WorkCase workCase) {
        log.debug("saveApprovalHistoryPricing() workCase: {}", workCase);
        // Set current time for submit
        //approvalHistoryView.setSubmitDate(DateTime.now().toDate());
        approvalPricingHistoryView.setApprovalType(ApprovalType.PRICING_APPROVAL.value());
        ApprovalHistory returnApprovalHistory = approvalHistoryDAO.persist(approvalHistoryTransform.transformToModel(approvalPricingHistoryView, workCase));
        return approvalHistoryTransform.transformToView(returnApprovalHistory);
    }

    public ApprovalHistoryView getCurrentApprovalHistory(long workCaseId, int approvalType, long stepId){
        ApprovalHistoryView approvalHistoryView = new ApprovalHistoryView();
        ApprovalHistory approvalHistory = approvalHistoryDAO.findByWorkCaseAndUserForSubmit(workCaseId, getCurrentUserID(), approvalType);
        if(!Util.isNull(approvalHistory)){
            approvalHistoryView = approvalHistoryTransform.transformToView(approvalHistory);
        }else{
            User user = getCurrentUser();
            Step step = stepDAO.findById(stepId);
            Role role = user.getRole();
            approvalHistoryView.setUserView(userTransform.transformToView(user));
            approvalHistoryView.setStepView(stepTransform.transformToView(step));
            approvalHistoryView.setRoleView(roleTransform.transformRoleToView(role));
            approvalHistoryView.setSubmitDate(null);
        }

        return approvalHistoryView;
    }

    /*public void deleteAllApproveByIdList(List<Long> deleteCreditIdList, List<Long> deleteCollIdList, List<Long> deleteGuarantorIdList, List<Long> deleteConditionIdList) {
        log.debug("deleteAllApproveByIdList()");
        log.debug("deleteCreditIdList: {}", deleteCreditIdList);
        log.debug("deleteCollIdList: {}", deleteCollIdList);
        log.debug("deleteGuarantorIdList: {}", deleteGuarantorIdList);
        log.debug("deleteConditionIdList: {}", deleteConditionIdList);

        if (deleteCreditIdList != null && deleteCreditIdList.size() > 0) {
            List<ProposeCreditInfo> deleteCreditDetailList = new ArrayList<ProposeCreditInfo>();
            for (Long id : deleteCreditIdList) {
                deleteCreditDetailList.add(newCreditDetailDAO.findById(id));
            }
            newCreditDetailDAO.delete(deleteCreditDetailList);
        }

        if (deleteCollIdList != null && deleteCollIdList.size() > 0) {
            List<ProposeCollateralInfo> deleteCollateralList = new ArrayList<ProposeCollateralInfo>();
            for (Long id : deleteCollIdList) {
                deleteCollateralList.add(newCollateralDAO.findById(id));
            }
            newCollateralDAO.delete(deleteCollateralList);
        }

        if (deleteGuarantorIdList != null && deleteGuarantorIdList.size() > 0) {
            List<ProposeGuarantorInfo> deleteGuarantorList = new ArrayList<ProposeGuarantorInfo>();
            for (Long id : deleteGuarantorIdList) {
                deleteGuarantorList.add(newGuarantorDetailDAO.findById(id));
            }
            newGuarantorDetailDAO.delete(deleteGuarantorList);
        }

        if (deleteConditionIdList != null && deleteConditionIdList.size() > 0) {
            List<DecisionFollowCondition> deleteConditionList = new ArrayList<DecisionFollowCondition>();
            for (Long id : deleteConditionIdList) {
                deleteConditionList.add(decisionFollowConditionDAO.findById(id));
            }
            decisionFollowConditionDAO.delete(deleteConditionList);
        }
    }*/

    public ApprovalHistoryView getApprovalHistoryView(long stepId) {
        ApprovalHistoryView approvalHistoryView = new ApprovalHistoryView();
        StepView stepView;
        if (stepId != 0) {
            stepView = stepTransform.transformToView(stepDAO.findById(stepId));
        } else {
            stepView = new StepView();
        }

        User user = getCurrentUser();
        UserView userView = new UserView();
        RoleView roleView = new RoleView();
        if (user != null) {
            userView.setId(user.getId());
            userView.setUserName(user.getUserName());
            userView.setTitleName(user.getTitle() != null ? user.getTitle().getName() : "");
            userView.setRoleDescription(user.getRole() != null ? user.getRole().getDescription() : "");

            if (user.getRole() != null) {
                roleView = roleTransform.transformRoleToView(user.getRole());
            }
        }

        approvalHistoryView.setStepView(stepView);
        approvalHistoryView.setUserView(userView);
        approvalHistoryView.setRoleView(roleView);
        return approvalHistoryView;
    }

    public int getUserRoleId() {
        User user = getCurrentUser();
        if (user != null && user.getRole() != null) {
            return user.getRole().getId();
        }
        return 0;
    }

    public void saveDecision(DecisionView decisionView, WorkCase workCase) {
        log.debug("saveDecision() workCase: {}", workCase);
        Decision decision = decisionDAO.findByWorkCase(workCase);
        log.debug("decision: {}", decision);
        User currentUser = getCurrentUser();
        if (decision == null) {
            decision = new Decision();
            decision.setCreateBy(currentUser);
            decision.setCreateDate(new Date());
            decision.setWorkCase(workCase);
        }
        decision.setModifyBy(currentUser);
        decision.setModifyDate(new Date());

        decision.setTotalApproveCredit(decisionView.getApproveTotalCreditLimit());
        decision.setTotalApproveCommercial(decisionView.getApproveBrwTotalCommercial());
        decision.setTotalApproveComAndOBOD(decisionView.getApproveBrwTotalComAndOBOD());
        decision.setTotalApproveExposure(decisionView.getApproveTotalExposure());
        decision.setTotalApprovedODLimit(decisionView.getApproveTotalODLimit());
        decision.setTotalApproveNumOfNewOD(decisionView.getApproveTotalNumOfNewOD());
        decision.setTotalApproveNumProposeCreditFac(decisionView.getApproveTotalNumProposeCreditFac());
        decision.setTotalApproveNumContingentPropose(decisionView.getApproveTotalNumContingentPropose());
        decision.setTotalApproveNumOfCoreAsset(decisionView.getGrandTotalNumOfCoreAsset());
        decision.setTotalApproveNumOfNonCoreAsset(decisionView.getGrandTotalNumOfNonCoreAsset());
        decision.setTotalApproveGuaranteeAmt(decisionView.getApproveTotalGuaranteeAmt());
        decision.setTotalApproveTCGGuaranteeAmt(decisionView.getApproveTotalTCGGuaranteeAmt());
        decision.setTotalApproveIndiGuaranteeAmt(decisionView.getApproveTotalIndvGuaranteeAmt());
        decision.setTotalApproveJuriGuaranteeAmt(decisionView.getApproveTotalJurisGuaranteeAmt());

        decisionDAO.persist(decision);
    }

    public void calculateTotalApprove(DecisionView decisionView) {
        log.debug("calculateTotalApprove()");
        if (decisionView != null) {

            BigDecimal totalApproveCredit = BigDecimal.ZERO;
            BigDecimal totalODLimit = BigDecimal.ZERO;
            BigDecimal totalNumOfNewOD = BigDecimal.ZERO;
            BigDecimal totalNumProposeCreditFac = BigDecimal.ZERO;
            BigDecimal totalNumContingentPropose = BigDecimal.ZERO;
            BigDecimal totalNumOfCoreAsset = BigDecimal.ZERO;
            BigDecimal totalNumOfNonCoreAsset = BigDecimal.ZERO;
            BigDecimal totalMortgageValue = BigDecimal.ZERO;
            BigDecimal totalApproveGuaranteeAmt = BigDecimal.ZERO;
            BigDecimal totalTCGGuaranteeAmt = BigDecimal.ZERO;
            BigDecimal totalIndiGuaranteeAmt = BigDecimal.ZERO;
            BigDecimal totalJuriGuaranteeAmt = BigDecimal.ZERO;

            // Credit Detail
            List<ProposeCreditInfoDetailView> approveCreditList = decisionView.getApproveCreditList();
            if (approveCreditList != null && approveCreditList.size() > 0) {
                for (ProposeCreditInfoDetailView approveCredit : approveCreditList) {
                    // Sum total approve credit limit amount
                    totalApproveCredit = Util.add(totalApproveCredit, approveCredit.getLimit());
                    // Count All 'New' propose credit
                    totalNumProposeCreditFac = Util.add(totalNumProposeCreditFac, BigDecimal.ONE);

                    CreditTypeView creditTypeView = approveCredit.getCreditTypeView();
                    if (creditTypeView != null) {
                        // Count propose credit which credit facility = 'OD'
                        if (CreditTypeGroup.OD.value() == creditTypeView.getCreditGroup()) {
                            totalNumOfNewOD = Util.add(totalNumOfNewOD, BigDecimal.ONE);
                            totalODLimit = Util.add(totalODLimit, approveCredit.getLimit());
                        }
                        // Count the 'New' propose credit which has Contingent Flag 'Y'
                        if (creditTypeView.isContingentFlag()) {
                            totalNumContingentPropose = Util.add(totalNumContingentPropose, BigDecimal.ONE);
                        }
                    }
                }
            }

            BigDecimal totalApproveCommercial = Util.add(decisionView.getExtBorrowerTotalCommercial(), totalApproveCredit);
            BigDecimal totalApproveComAndOBOD = Util.add(decisionView.getExtBorrowerTotalComAndOBOD(), totalApproveCredit);
            BigDecimal totalApproveExposure = Util.add(decisionView.getExtBorrowerTotalExposure(), totalApproveCredit);

            List<ProposeCollateralInfoView> approveCollateralList = decisionView.getApproveCollateralList();
            if (approveCollateralList != null && approveCollateralList.size() > 0) {
                for (ProposeCollateralInfoView collateralView : approveCollateralList) {
                    List<ProposeCollateralInfoHeadView> collHeadViewList = collateralView.getProposeCollateralInfoHeadViewList();
                    if (collHeadViewList != null && collHeadViewList.size() > 0) {
                        for (ProposeCollateralInfoHeadView collHeadView : collHeadViewList) {
                            PotentialCollateral potentialCollateral = collHeadView.getPotentialCollateral();

                            // Count core asset and none core asset
                            if (PotentialCollateralValue.CORE_ASSET.id() == potentialCollateral.getId()) {
                                totalNumOfCoreAsset = Util.add(totalNumOfCoreAsset, BigDecimal.ONE);
                            }
                            else if (PotentialCollateralValue.NONE_CORE_ASSET.id() == potentialCollateral.getId()) {
                                totalNumOfNonCoreAsset = Util.add(totalNumOfNonCoreAsset, BigDecimal.ONE);
                            }

                            // Sum total mortgage value
                            List<ProposeCollateralInfoSubView> collSubViewList = collHeadView.getProposeCollateralInfoSubViewList();
                            if (collSubViewList != null && collSubViewList.size() > 0) {
                                for (ProposeCollateralInfoSubView collSubView : collSubViewList) {
                                    totalMortgageValue = Util.add(totalMortgageValue, collSubView.getMortgageValue());
                                }
                            }
                        }
                    }
                }
            }

            // Guarantor Detail
            List<ProposeGuarantorInfoView> approveGuarantorList = decisionView.getApproveGuarantorList();
            if (approveGuarantorList != null && approveGuarantorList.size() > 0) {
                for (ProposeGuarantorInfoView approveGuarantor : approveGuarantorList) {
                    // Sum total approve guarantee amount
                    totalApproveGuaranteeAmt = Util.add(totalApproveGuaranteeAmt, approveGuarantor.getGuaranteeAmount());
                    // Sum total guarantee amount (TCG, Individual, Juristic)
                    CustomerInfoView customerInfoView = approveGuarantor.getGuarantorName();
                    if (customerInfoView != null) {
                        CustomerEntity customerEntity = customerInfoView.getCustomerEntity();
                        if (customerEntity != null) {
                            if (GuarantorCategory.INDIVIDUAL.value() == customerEntity.getId()) {
                                totalIndiGuaranteeAmt = Util.add(totalIndiGuaranteeAmt, approveGuarantor.getGuaranteeAmount());
                            }
                            else if (GuarantorCategory.JURISTIC.value() == customerEntity.getId()) {
                                totalJuriGuaranteeAmt = Util.add(totalJuriGuaranteeAmt, approveGuarantor.getGuaranteeAmount());
                            }
                            else if (GuarantorCategory.TCG.value() == customerEntity.getId()) {
                                totalTCGGuaranteeAmt = Util.add(totalTCGGuaranteeAmt, approveGuarantor.getGuaranteeAmount());
                            }
                        }
                    }
                }
            }

            decisionView.setApproveTotalCreditLimit(totalApproveCredit);
            decisionView.setApproveBrwTotalCommercial(totalApproveCommercial);
            decisionView.setApproveBrwTotalComAndOBOD(totalApproveComAndOBOD);
            decisionView.setApproveTotalExposure(totalApproveExposure);
            decisionView.setApproveTotalODLimit(totalODLimit);
            decisionView.setApproveTotalNumOfNewOD(totalNumOfNewOD);
            decisionView.setApproveTotalNumProposeCreditFac(totalNumProposeCreditFac);
            decisionView.setApproveTotalNumContingentPropose(totalNumContingentPropose);
            decisionView.setGrandTotalNumOfCoreAsset(totalNumOfCoreAsset);
            decisionView.setGrandTotalNumOfNonCoreAsset(totalNumOfNonCoreAsset);
            decisionView.setApproveTotalGuaranteeAmt(totalApproveGuaranteeAmt);
            decisionView.setApproveTotalTCGGuaranteeAmt(totalTCGGuaranteeAmt);
            decisionView.setApproveTotalIndvGuaranteeAmt(totalIndiGuaranteeAmt);
            decisionView.setApproveTotalJurisGuaranteeAmt(totalJuriGuaranteeAmt);
        }
    }

    //Get value pass to PDFOfferLetter by Bird
    public BigDecimal getMRRValue(){
        return Util.convertNullToZERO(super.getMRRValue());
    }

    public BigDecimal getMLRValue(){
        return Util.convertNullToZERO(super.getMLRValue());
    }

    public BigDecimal getMORValue(){
        return Util.convertNullToZERO(super.getMORValue());
    }

    public String getCurrentUserID(){
        return Util.checkNullString(super.getCurrentUserID());
    }
}
