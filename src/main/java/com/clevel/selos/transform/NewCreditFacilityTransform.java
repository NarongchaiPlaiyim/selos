package com.clevel.selos.transform;

import com.clevel.selos.dao.master.CountryDAO;
import com.clevel.selos.dao.master.CreditRequestTypeDAO;
import com.clevel.selos.dao.working.NewCreditFacilityDAO;
import com.clevel.selos.model.db.master.Country;
import com.clevel.selos.model.db.master.CreditRequestType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCreditFacility;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.CountryView;
import com.clevel.selos.model.view.NewCreditFacilityView;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.util.Date;

public class NewCreditFacilityTransform extends Transform {
    @Inject
    public NewCreditFacilityTransform() {}
    @Inject
    private CreditRequestTypeDAO creditRequestTypeDAO;
    @Inject
    private CountryDAO countryDAO;
    @Inject
    private NewCreditFacilityDAO newCreditFacilityDAO;
    @Inject
    private CountryTransform countryTransform;

    public NewCreditFacility transformToModelDB(NewCreditFacilityView newCreditFacilityView, WorkCase workCase, User user) {

        NewCreditFacility newCreditFacility = new NewCreditFacility();

        newCreditFacility.setWorkCase(workCase);

        if (newCreditFacilityView.getId() != 0) {
            newCreditFacility = newCreditFacilityDAO.findById(newCreditFacilityView.getId());
            newCreditFacility.setModifyDate(newCreditFacilityView.getModifyDate());
            newCreditFacility.setModifyBy(newCreditFacilityView.getModifyBy());
        } else { // id = 0 create new
            newCreditFacility.setCreateDate(new Date());
            newCreditFacility.setCreateBy(user);
        }

        newCreditFacility.setModifyDate(new Date());
        newCreditFacility.setModifyBy(user);
        newCreditFacility.setWcNeed(newCreditFacilityView.getWCNeed());
        newCreditFacility.setTotalWcTmb(newCreditFacilityView.getTotalWcTmb());
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
        newCreditFacility.setMaximumSMELimit(newCreditFacilityView.getMaximumSMELimit());
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
        newCreditFacility.setCreditCustomerType(newCreditFacilityView.getCreditCustomerType());

        if (newCreditFacilityView.getLoanRequestType().getId() != 0) {
            CreditRequestType creditRequestType = creditRequestTypeDAO.findById(newCreditFacilityView.getLoanRequestType().getId());
            newCreditFacility.setLoanRequestType(creditRequestType);
        }

        if (newCreditFacilityView.getInvestedCountry().getId() != 0) {
            Country country = countryDAO.findById(newCreditFacilityView.getInvestedCountry().getId());
            newCreditFacility.setInvestedCountry(country);
        }

        newCreditFacility.setTotalGuaranteeAmount(newCreditFacilityView.getTotalGuaranteeAmount());
        newCreditFacility.setRelatedTMBLending(newCreditFacilityView.getRelatedTMBLending());
        newCreditFacility.setTwentyFivePercentShareRelatedTMBLending(newCreditFacilityView.getTwentyFivePercentShareRelatedTMBLending());
        newCreditFacility.setSingleLendingLimit(newCreditFacilityView.getSingleLendingLimit());
        newCreditFacility.setTotalLoanWCTMB(newCreditFacilityView.getTotalLoanWCTMB());
        newCreditFacility.setIntFeeDOA(newCreditFacilityView.getIntFeeDOA());
        newCreditFacility.setTotalNumberOfCoreAsset(newCreditFacilityView.getTotalNumberOfCoreAsset());
        newCreditFacility.setTotalNumberOfNonCoreAsset(newCreditFacilityView.getTotalNumberOfNonCoreAsset());
        newCreditFacility.setTotalTCGGuaranteeAmount(newCreditFacilityView.getTotalTCGGuaranteeAmount());
        newCreditFacility.setTotalIndvGuaranteeAmount(newCreditFacilityView.getTotalIndvGuaranteeAmount());
        newCreditFacility.setTotalJurisGuaranteeAmount(newCreditFacilityView.getTotalJurisGuaranteeAmount());
        newCreditFacility.setTotalMortgageValue(newCreditFacilityView.getTotalMortgageValue());

        return newCreditFacility;
    }

    public NewCreditFacilityView transformToView(NewCreditFacility newCreditFacility) {
        NewCreditFacilityView newCreditFacilityView = new NewCreditFacilityView();

        newCreditFacilityView.setId(newCreditFacility.getId());
        newCreditFacilityView.setCreateDate(newCreditFacility.getCreateDate());
        newCreditFacilityView.setCreateBy(newCreditFacility.getCreateBy());
        newCreditFacilityView.setModifyDate(newCreditFacility.getModifyDate());
        newCreditFacilityView.setModifyBy(newCreditFacility.getModifyBy());
        newCreditFacilityView.setWCNeed(newCreditFacility.getWcNeed());
        newCreditFacilityView.setTotalWcTmb(newCreditFacility.getTotalWcTmb());
        newCreditFacilityView.setWCNeedDiffer(newCreditFacility.getWCNeedDiffer());
        newCreditFacilityView.setTotalWcDebit(newCreditFacility.getTotalWcDebit());
        newCreditFacilityView.setCase1WcLimit(newCreditFacility.getCase1WcLimit());
        newCreditFacilityView.setCase1WcMinLimit(newCreditFacility.getCase1WcMinLimit());
        newCreditFacilityView.setCase1Wc50CoreWc(newCreditFacility.getCase1Wc50CoreWc());
        newCreditFacilityView.setCase1WcDebitCoreWc(newCreditFacility.getCase1WcDebitCoreWc());
        newCreditFacilityView.setCase2WcLimit(newCreditFacility.getCase2WcLimit());
        newCreditFacilityView.setCase2WcMinLimit(newCreditFacility.getCase2WcMinLimit());
        newCreditFacilityView.setCase2Wc50CoreWc(newCreditFacility.getCase2Wc50CoreWc());
        newCreditFacilityView.setCase2WcDebitCoreWc(newCreditFacility.getCase2WcDebitCoreWc());
        newCreditFacilityView.setCase3WcLimit(newCreditFacility.getCase3WcLimit());
        newCreditFacilityView.setCase3WcMinLimit(newCreditFacility.getCase3WcMinLimit());
        newCreditFacilityView.setCase3Wc50CoreWc(newCreditFacility.getCase3Wc50CoreWc());
        newCreditFacilityView.setCase3WcDebitCoreWc(newCreditFacility.getCase3WcDebitCoreWc());
        newCreditFacilityView.setExistingSMELimit(newCreditFacility.getExistingSMELimit());
        newCreditFacilityView.setMaximumSMELimit(newCreditFacility.getMaximumSMELimit());
        newCreditFacilityView.setTotalPropose(newCreditFacility.getTotalPropose());
        newCreditFacilityView.setTotalProposeLoanDBR(newCreditFacility.getTotalProposeLoanDBR());
        newCreditFacilityView.setTotalProposeNonLoanDBR(newCreditFacility.getTotalProposeNonLoanDBR());
        newCreditFacilityView.setTotalCommercial(newCreditFacility.getTotalCommercial());
        newCreditFacilityView.setTotalCommercialAndOBOD(newCreditFacility.getTotalCommercialAndOBOD());
        newCreditFacilityView.setTotalExposure(newCreditFacility.getTotalExposure());
        newCreditFacilityView.setTotalNumberOfNewOD(newCreditFacility.getTotalNumberOfNewOD());
        newCreditFacilityView.setTotalNumberContingenPropose(newCreditFacility.getTotalNumberContingenPropose());
        newCreditFacilityView.setTotalNumberProposeCreditFac(newCreditFacility.getTotalNumberProposeCreditFac());
        newCreditFacilityView.setContactName(newCreditFacility.getContactName());
        newCreditFacilityView.setContactPhoneNo(newCreditFacility.getContactPhoneNo());
        newCreditFacilityView.setInterService(newCreditFacility.getInterService());
        newCreditFacilityView.setCurrentAddress(newCreditFacility.getCurrentAddress());
        newCreditFacilityView.setRegisteredAddress(newCreditFacility.getRegisteredAddress());
        newCreditFacilityView.setEmailAddress(newCreditFacility.getEmailAddress());
        newCreditFacilityView.setImportMail(newCreditFacility.getImportMail());
        newCreditFacilityView.setExportMail(newCreditFacility.getExportMail());
        newCreditFacilityView.setDepositBranchCode(newCreditFacility.getDepositBranchCode());
        newCreditFacilityView.setOwnerBranchCode(newCreditFacility.getOwnerBranchCode());
        newCreditFacilityView.setFrontendFeeDOA(newCreditFacility.getFrontendFeeDOA());
        newCreditFacilityView.setGuarantorBA(newCreditFacility.getGuarantorBA());
        newCreditFacilityView.setReasonForReduction(newCreditFacility.getReasonForReduction());
        newCreditFacilityView.setCreditCustomerType(newCreditFacility.getCreditCustomerType());

        newCreditFacilityView.setLoanRequestType(newCreditFacility.getLoanRequestType());
        if(newCreditFacilityView.getLoanRequestType() == null){
            newCreditFacilityView.setLoanRequestType(new CreditRequestType());
        }

        Country country = countryDAO.findById(newCreditFacility.getInvestedCountry().getId());
        if(!Util.isNull(country)){
            CountryView countryView = countryTransform.transformToView(country);
            newCreditFacilityView.setInvestedCountry(countryView);
        } else {
            log.debug("-- Country is null while findById {}", newCreditFacility.getInvestedCountry().getId());
            newCreditFacilityView.setInvestedCountry(new CountryView());
        }


        newCreditFacilityView.setTotalGuaranteeAmount(newCreditFacility.getTotalGuaranteeAmount());
        newCreditFacilityView.setRelatedTMBLending(newCreditFacility.getRelatedTMBLending());
        newCreditFacilityView.setTwentyFivePercentShareRelatedTMBLending(newCreditFacility.getTwentyFivePercentShareRelatedTMBLending());
        newCreditFacilityView.setSingleLendingLimit(newCreditFacility.getSingleLendingLimit());
        newCreditFacilityView.setTotalLoanWCTMB(newCreditFacility.getTotalLoanWCTMB());
        newCreditFacilityView.setIntFeeDOA(newCreditFacility.getIntFeeDOA());
        newCreditFacilityView.setTotalNumberOfCoreAsset(newCreditFacility.getTotalNumberOfCoreAsset());
        newCreditFacilityView.setTotalNumberOfNonCoreAsset(newCreditFacility.getTotalNumberOfNonCoreAsset());
        newCreditFacilityView.setTotalTCGGuaranteeAmount(newCreditFacility.getTotalTCGGuaranteeAmount());
        newCreditFacilityView.setTotalIndvGuaranteeAmount(newCreditFacility.getTotalIndvGuaranteeAmount());
        newCreditFacilityView.setTotalJurisGuaranteeAmount(newCreditFacility.getTotalJurisGuaranteeAmount());
        newCreditFacilityView.setTotalMortgageValue(newCreditFacility.getTotalMortgageValue());

        return newCreditFacilityView;
    }

}
