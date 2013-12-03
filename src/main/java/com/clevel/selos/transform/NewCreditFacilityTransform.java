package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.CreditFacProposeView;

import javax.inject.Inject;
import java.util.Date;

public class NewCreditFacilityTransform extends Transform {
    @Inject
    public NewCreditFacilityTransform() {}

    public NewCreditFacility transformToModelDB(CreditFacProposeView creditFacProposeView, WorkCase workCase, User user) {

        NewCreditFacility newCreditFacility = new NewCreditFacility();
        newCreditFacility.setWorkCase(workCase);

        if (creditFacProposeView.getId() != 0) {
            newCreditFacility.setId(creditFacProposeView.getId());
            newCreditFacility.setCreateDate(creditFacProposeView.getCreateDate());
            newCreditFacility.setCreateBy(creditFacProposeView.getCreateBy());
        } else { // id = 0 create new
            newCreditFacility.setCreateDate(new Date());
            newCreditFacility.setCreateBy(user);
        }

        newCreditFacility.setModifyDate(new Date());
        newCreditFacility.setModifyBy(user);

        newCreditFacility.setWcNeed(creditFacProposeView.getWCNeed());
        newCreditFacility.setTotalCreditTurnover(creditFacProposeView.getTotalCreditTurnover());
        newCreditFacility.setWCNeedDiffer(creditFacProposeView.getWCNeedDiffer());
        newCreditFacility.setTotalWcDebit(creditFacProposeView.getTotalWcDebit());
        newCreditFacility.setCase1WcLimit(creditFacProposeView.getCase1WcLimit());
        newCreditFacility.setCase1WcMinLimit(creditFacProposeView.getCase1WcMinLimit());
        newCreditFacility.setCase1Wc50CoreWc(creditFacProposeView.getCase1Wc50CoreWc());
        newCreditFacility.setCase1WcDebitCoreWc(creditFacProposeView.getCase1WcDebitCoreWc());
        newCreditFacility.setCase2WcLimit(creditFacProposeView.getCase2WcLimit());
        newCreditFacility.setCase2WcMinLimit(creditFacProposeView.getCase2WcMinLimit());
        newCreditFacility.setCase2Wc50CoreWc(creditFacProposeView.getCase2Wc50CoreWc());
        newCreditFacility.setCase2WcDebitCoreWc(creditFacProposeView.getCase2WcDebitCoreWc());
        newCreditFacility.setCase3WcLimit(creditFacProposeView.getCase3WcLimit());
        newCreditFacility.setCase3WcMinLimit(creditFacProposeView.getCase3WcMinLimit());
        newCreditFacility.setCase3Wc50CoreWc(creditFacProposeView.getCase3Wc50CoreWc());
        newCreditFacility.setCase3WcDebitCoreWc(creditFacProposeView.getCase3WcDebitCoreWc());
        newCreditFacility.setExistingSMELimit(creditFacProposeView.getExistingSMELimit());
        newCreditFacility.setMaximumExistingSMELimit(creditFacProposeView.getMaximumExistingSMELimit());
        newCreditFacility.setTotalPropose(creditFacProposeView.getTotalPropose());
        newCreditFacility.setTotalProposeLoanDBR(creditFacProposeView.getTotalProposeLoanDBR());
        newCreditFacility.setTotalProposeNonLoanDBR(creditFacProposeView.getTotalProposeNonLoanDBR());
        newCreditFacility.setTotalCommercial(creditFacProposeView.getTotalCommercial());
        newCreditFacility.setTotalCommercialAndOBOD(creditFacProposeView.getTotalCommercialAndOBOD());
        newCreditFacility.setTotalExposure(creditFacProposeView.getTotalExposure());
        /*newCreditFacility.setTotalNumberOfNewOD(creditFacProposeView.getTotalNumberOfNewOD());
        newCreditFacility.setTotalNumberContingenPropose(creditFacProposeView.getTotalNumberContingenPropose());
        newCreditFacility.setTotalNumberProposeCreditFac(creditFacProposeView.getTotalNumberProposeCreditFac());
        newCreditFacility.setContactName(creditFacProposeView.getContactName());*/
        newCreditFacility.setContactPhoneNo(creditFacProposeView.getContactPhoneNo());
        newCreditFacility.setInterService(creditFacProposeView.getInterService());
        newCreditFacility.setCurrentAddress(creditFacProposeView.getCurrentAddress());
        newCreditFacility.setRegisteredAddress(creditFacProposeView.getRegisteredAddress());
        newCreditFacility.setEmailAddress(creditFacProposeView.getEmailAddress());
        newCreditFacility.setImportMail(creditFacProposeView.getImportMail());
        newCreditFacility.setExportMail(creditFacProposeView.getExportMail());
        newCreditFacility.setDepositBranchCode(creditFacProposeView.getDepositBranchCode());
        newCreditFacility.setOwnerBranchCode(creditFacProposeView.getOwnerBranchCode());
        newCreditFacility.setFrontendFeeDOA(creditFacProposeView.getFrontendFeeDOA());
        newCreditFacility.setGuarantorBA(creditFacProposeView.getGuarantorBA());
        newCreditFacility.setReasonForReduction(creditFacProposeView.getReasonForReduction());
        newCreditFacility.setCreditCustomerType(creditFacProposeView.getCreditCustomerType().value());
        newCreditFacility.setCreditRequestType(creditFacProposeView.getCreditRequestType());
        newCreditFacility.setCountry(creditFacProposeView.getCountry());
        newCreditFacility.setTotalGuaranteeAmount(creditFacProposeView.getTotalGuaranteeAmount());
        newCreditFacility.setRelatedTMBLending(creditFacProposeView.getRelatedTMBLending());
        newCreditFacility.setTwentyFivePercentShareRelatedTMBLending(creditFacProposeView.getTwentyFivePercentShareRelatedTMBLending());
        newCreditFacility.setSingleLendingLimit(creditFacProposeView.getSingleLendingLimit());


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

    public CreditFacProposeView transformToView(NewCreditFacility newCreditFacility) {
        CreditFacProposeView creditFacProposeView = new CreditFacProposeView();

        return creditFacProposeView;
    }
}
