package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.NewCreditFacilityView;

import javax.inject.Inject;
import java.util.Date;

public class NewCreditFacilityTransform extends Transform {
    @Inject
    public NewCreditFacilityTransform() {}

    public NewCreditFacility transformToModelDB(NewCreditFacilityView newCreditFacilityView, WorkCase workCase, User user) {

        NewCreditFacility newCreditFacility = new NewCreditFacility();
        newCreditFacility.setWorkCase(workCase);

        if (newCreditFacilityView.getId() != 0) {
            newCreditFacility.setId(newCreditFacilityView.getId());
            newCreditFacility.setCreateDate(newCreditFacilityView.getCreateDate());
            newCreditFacility.setCreateBy(newCreditFacilityView.getCreateBy());
        } else { // id = 0 create new
            newCreditFacility.setCreateDate(new Date());
            newCreditFacility.setCreateBy(user);
        }

        newCreditFacility.setModifyDate(new Date());
        newCreditFacility.setModifyBy(user);

        newCreditFacility.setWcNeed(newCreditFacilityView.getWCNeed());
        newCreditFacility.setTotalCreditTurnover(newCreditFacilityView.getTotalCreditTurnover());
        newCreditFacility.setWCNeedDiffer(newCreditFacilityView.getWCNeedDiffer());
        newCreditFacility.setTotalWcDebit(newCreditFacilityView.getTotalWcDebit());
        newCreditFacility.setCase1WcLimit(newCreditFacilityView.getCase1WcLimit());
        newCreditFacility.setCase1WcMinLimit(newCreditFacilityView.getCase1WcMinLimit());
        newCreditFacility.setCase1Wc50CoreWc(newCreditFacilityView.getCase1Wc50CoreWc());
        newCreditFacility.setCase1WcDebitCoreWc(newCreditFacilityView.getCase1WcDebitCoreWc());
        newCreditFacility.setCase2WcLimit(newCreditFacilityView.getCase2WcLimit());
        newCreditFacility.setCase2WcMinLimit(newCreditFacilityView.getCase2WcMinLimit());
        newCreditFacility.setCase2Wc50CoreWc(newCreditFacilityView.getCase2Wc50CoreWc());
        newCreditFacility.setCase2WcDebitCoreWc(newCreditFacilityView.getCase2WcDebitCoreWc());
        newCreditFacility.setCase3WcLimit(newCreditFacilityView.getCase3WcLimit());
        newCreditFacility.setCase3WcMinLimit(newCreditFacilityView.getCase3WcMinLimit());
        newCreditFacility.setCase3Wc50CoreWc(newCreditFacilityView.getCase3Wc50CoreWc());
        newCreditFacility.setCase3WcDebitCoreWc(newCreditFacilityView.getCase3WcDebitCoreWc());
        newCreditFacility.setExistingSMELimit(newCreditFacilityView.getExistingSMELimit());
        newCreditFacility.setMaximumExistingSMELimit(newCreditFacilityView.getMaximumExistingSMELimit());
        newCreditFacility.setTotalPropose(newCreditFacilityView.getTotalPropose());
        newCreditFacility.setTotalProposeLoanDBR(newCreditFacilityView.getTotalProposeLoanDBR());
        newCreditFacility.setTotalProposeNonLoanDBR(newCreditFacilityView.getTotalProposeNonLoanDBR());
        newCreditFacility.setTotalCommercial(newCreditFacilityView.getTotalCommercial());
        newCreditFacility.setTotalCommercialAndOBOD(newCreditFacilityView.getTotalCommercialAndOBOD());
        newCreditFacility.setTotalExposure(newCreditFacilityView.getTotalExposure());
        newCreditFacility.setTotalNumberOfNewOD(newCreditFacilityView.getTotalNumberOfNewOD());
        newCreditFacility.setTotalNumberContingenPropose(newCreditFacilityView.getTotalNumberContingenPropose());
        newCreditFacility.setTotalNumberProposeCreditFac(newCreditFacilityView.getTotalNumberProposeCreditFac());
        newCreditFacility.setContactName(newCreditFacilityView.getContactName());
        newCreditFacility.setContactPhoneNo(newCreditFacilityView.getContactPhoneNo());
        newCreditFacility.setInterService(newCreditFacilityView.getInterService());
        newCreditFacility.setCurrentAddress(newCreditFacilityView.getCurrentAddress());
        newCreditFacility.setRegisteredAddress(newCreditFacilityView.getRegisteredAddress());
        newCreditFacility.setEmailAddress(newCreditFacilityView.getEmailAddress());
        newCreditFacility.setImportMail(newCreditFacilityView.getImportMail());
        newCreditFacility.setExportMail(newCreditFacilityView.getExportMail());
        newCreditFacility.setDepositBranchCode(newCreditFacilityView.getDepositBranchCode());
        newCreditFacility.setOwnerBranchCode(newCreditFacilityView.getOwnerBranchCode());
        newCreditFacility.setFrontendFeeDOA(newCreditFacilityView.getFrontendFeeDOA());
        newCreditFacility.setGuarantorBA(newCreditFacilityView.getGuarantorBA());
        newCreditFacility.setReasonForReduction(newCreditFacilityView.getReasonForReduction());
        newCreditFacility.setCreditCustomerType(newCreditFacilityView.getCreditCustomerType().value());
        newCreditFacility.setCreditRequestType(newCreditFacilityView.getCreditRequestType());
        newCreditFacility.setCountry(newCreditFacilityView.getCountry());
        newCreditFacility.setTotalGuaranteeAmount(newCreditFacilityView.getTotalGuaranteeAmount());
        newCreditFacility.setRelatedTMBLending(newCreditFacilityView.getRelatedTMBLending());
        newCreditFacility.setTwentyFivePercentShareRelatedTMBLending(newCreditFacilityView.getTwentyFivePercentShareRelatedTMBLending());
        newCreditFacility.setSingleLendingLimit(newCreditFacilityView.getSingleLendingLimit());


       /* for (NewCollateralHeadDetailView newCollateralHeadDetailView : newCollateralHeadDetailViewList) {
            collateralHeaderDetail = new CollateralHeaderDetail();
            if(newCollateralHeadDetailView.getId()==0){
                collateralHeaderDetail.setCreateBy(newCollateralHeadDetailView.getCreateBy());
                collateralHeaderDetail.setCreateDate(DateTime.now().toDate());
            }

            collateralHeaderDetail.setNo(newCollateralHeadDetailView.getNo());
            collateralHeaderDetail.setCollateralLocation(newCollateralHeadDetailView.getCollateralLocation());
            collateralHeaderDetail.setTitleDeed(newCollateralHeadDetailView.getTitleDeed());
            collateralHeaderDetail.setAppraisalValue(newCollateralHeadDetailView.getAppraisalValue());
            collateralHeaderDetail.setHeadCollType(newCollateralHeadDetailView.getHeadCollType());
            collateralHeaderDetail.setCollateralDetail(collateralDetail);

            collateralHeaderDetail.setModifyBy(newCollateralHeadDetailView.getModifyBy());
            collateralHeaderDetail.setModifyDate(newCollateralHeadDetailView.getModifyDate());

            collateralHeaderDetailList.add(collateralHeaderDetail);
        }*/

        return newCreditFacility;
    }

    public NewCreditFacilityView transformToView(NewCreditFacility newCreditFacility) {
        NewCreditFacilityView newCreditFacilityView = new NewCreditFacilityView();

        return newCreditFacilityView;
    }
}
