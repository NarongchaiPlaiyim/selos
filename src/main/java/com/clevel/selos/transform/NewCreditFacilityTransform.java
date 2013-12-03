package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.CreditFacilityPropose;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.CreditFacProposeView;

import javax.inject.Inject;
import java.util.Date;

public class NewCreditFacilityTransform extends Transform {
    @Inject
    public NewCreditFacilityTransform() {}

    public CreditFacilityPropose transformToModelDB(CreditFacProposeView creditFacProposeView, WorkCase workCase, User user) {

        CreditFacilityPropose creditFacilityPropose = new CreditFacilityPropose();
        creditFacilityPropose.setWorkCase(workCase);

        if (creditFacProposeView.getId() != 0) {
            creditFacilityPropose.setId(creditFacProposeView.getId());
            creditFacilityPropose.setCreateDate(creditFacProposeView.getCreateDate());
            creditFacilityPropose.setCreateBy(creditFacProposeView.getCreateBy());
        } else { // id = 0 create new
            creditFacilityPropose.setCreateDate(new Date());
            creditFacilityPropose.setCreateBy(user);
        }

        creditFacilityPropose.setModifyDate(new Date());
        creditFacilityPropose.setModifyBy(user);

        creditFacilityPropose.setWcNeed(creditFacProposeView.getWCNeed());
        creditFacilityPropose.setTotalCreditTurnover(creditFacProposeView.getTotalCreditTurnover());
        creditFacilityPropose.setWCNeedDiffer(creditFacProposeView.getWCNeedDiffer());
        creditFacilityPropose.setTotalWcDebit(creditFacProposeView.getTotalWcDebit());
        creditFacilityPropose.setCase1WcLimit(creditFacProposeView.getCase1WcLimit());
        creditFacilityPropose.setCase1WcMinLimit(creditFacProposeView.getCase1WcMinLimit());
        creditFacilityPropose.setCase1Wc50CoreWc(creditFacProposeView.getCase1Wc50CoreWc());
        creditFacilityPropose.setCase1WcDebitCoreWc(creditFacProposeView.getCase1WcDebitCoreWc());
        creditFacilityPropose.setCase2WcLimit(creditFacProposeView.getCase2WcLimit());
        creditFacilityPropose.setCase2WcMinLimit(creditFacProposeView.getCase2WcMinLimit());
        creditFacilityPropose.setCase2Wc50CoreWc(creditFacProposeView.getCase2Wc50CoreWc());
        creditFacilityPropose.setCase2WcDebitCoreWc(creditFacProposeView.getCase2WcDebitCoreWc());
        creditFacilityPropose.setCase3WcLimit(creditFacProposeView.getCase3WcLimit());
        creditFacilityPropose.setCase3WcMinLimit(creditFacProposeView.getCase3WcMinLimit());
        creditFacilityPropose.setCase3Wc50CoreWc(creditFacProposeView.getCase3Wc50CoreWc());
        creditFacilityPropose.setCase3WcDebitCoreWc(creditFacProposeView.getCase3WcDebitCoreWc());
        creditFacilityPropose.setExistingSMELimit(creditFacProposeView.getExistingSMELimit());
        creditFacilityPropose.setMaximumExistingSMELimit(creditFacProposeView.getMaximumExistingSMELimit());
        creditFacilityPropose.setTotalPropose(creditFacProposeView.getTotalPropose());
        creditFacilityPropose.setTotalProposeLoanDBR(creditFacProposeView.getTotalProposeLoanDBR());
        creditFacilityPropose.setTotalProposeNonLoanDBR(creditFacProposeView.getTotalProposeNonLoanDBR());
        creditFacilityPropose.setTotalCommercial(creditFacProposeView.getTotalCommercial());
        creditFacilityPropose.setTotalCommercialAndOBOD(creditFacProposeView.getTotalCommercialAndOBOD());
        creditFacilityPropose.setTotalExposure(creditFacProposeView.getTotalExposure());
        creditFacilityPropose.setTotalNumberOfNewOD(creditFacProposeView.getTotalNumberOfNewOD());
        creditFacilityPropose.setTotalNumberContingenPropose(creditFacProposeView.getTotalNumberContingenPropose());
        creditFacilityPropose.setTotalNumberProposeCreditFac(creditFacProposeView.getTotalNumberProposeCreditFac());
        creditFacilityPropose.setContactName(creditFacProposeView.getContactName());
        creditFacilityPropose.setContactPhoneNo(creditFacProposeView.getContactPhoneNo());
        creditFacilityPropose.setInterService(creditFacProposeView.getInterService());
        creditFacilityPropose.setCurrentAddress(creditFacProposeView.getCurrentAddress());
        creditFacilityPropose.setRegisteredAddress(creditFacProposeView.getRegisteredAddress());
        creditFacilityPropose.setEmailAddress(creditFacProposeView.getEmailAddress());
        creditFacilityPropose.setImportMail(creditFacProposeView.getImportMail());
        creditFacilityPropose.setExportMail(creditFacProposeView.getExportMail());
        creditFacilityPropose.setDepositBranchCode(creditFacProposeView.getDepositBranchCode());
        creditFacilityPropose.setOwnerBranchCode(creditFacProposeView.getOwnerBranchCode());
        creditFacilityPropose.setFrontendFeeDOA(creditFacProposeView.getFrontendFeeDOA());
        creditFacilityPropose.setGuarantorBA(creditFacProposeView.getGuarantorBA());
        creditFacilityPropose.setReasonForReduction(creditFacProposeView.getReasonForReduction());
        creditFacilityPropose.setCreditCustomerType(creditFacProposeView.getCreditCustomerType().value());
        creditFacilityPropose.setCreditRequestType(creditFacProposeView.getCreditRequestType());
        creditFacilityPropose.setCountry(creditFacProposeView.getCountry());
        creditFacilityPropose.setTotalGuaranteeAmount(creditFacProposeView.getTotalGuaranteeAmount());
        creditFacilityPropose.setRelatedTMBLending(creditFacProposeView.getRelatedTMBLending());
        creditFacilityPropose.setTwentyFivePercentShareRelatedTMBLending(creditFacProposeView.getTwentyFivePercentShareRelatedTMBLending());
        creditFacilityPropose.setSingleLendingLimit(creditFacProposeView.getSingleLendingLimit());


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

        return creditFacilityPropose;
    }

    public CreditFacProposeView transformToView(CreditFacilityPropose creditFacilityPropose) {
        CreditFacProposeView creditFacProposeView = new CreditFacProposeView();

        return creditFacProposeView;
    }
}
