package com.clevel.selos.transform;

import com.clevel.selos.model.CreditTypeOfStep;
import com.clevel.selos.model.view.ExistingCreditDetailView;
import com.clevel.selos.model.view.NewCreditDetailView;
import com.clevel.selos.model.view.ProposeCreditDetailView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ProposeCreditDetailTransform extends Transform {

    @Inject
    public ProposeCreditDetailTransform() {
    }

    public ProposeCreditDetailView convertNewCreditToProposeCredit(NewCreditDetailView newCreditDetailView, int seqNumber) {
        ProposeCreditDetailView proposeCreditDetailView = new ProposeCreditDetailView();
        if (newCreditDetailView != null) {
            proposeCreditDetailView = new ProposeCreditDetailView();
            proposeCreditDetailView.setSeq(seqNumber);
            proposeCreditDetailView.setId(newCreditDetailView.getId());
            proposeCreditDetailView.setTypeOfStep(CreditTypeOfStep.NEW.type());
            proposeCreditDetailView.setAccountName(newCreditDetailView.getAccountName());
            proposeCreditDetailView.setAccountNumber(newCreditDetailView.getAccountNumber());
            proposeCreditDetailView.setAccountSuf(newCreditDetailView.getAccountSuf());
            proposeCreditDetailView.setRequestType(newCreditDetailView.getRequestType());
            proposeCreditDetailView.setProductProgramView(newCreditDetailView.getProductProgramView());
            proposeCreditDetailView.setCreditFacilityView(newCreditDetailView.getCreditTypeView());
            proposeCreditDetailView.setLimit(newCreditDetailView.getLimit());
            proposeCreditDetailView.setGuaranteeAmount(newCreditDetailView.getGuaranteeAmount());
            proposeCreditDetailView.setUseCount(newCreditDetailView.getUseCount());
            proposeCreditDetailView.setNoFlag(newCreditDetailView.isNoFlag());
        }
        return proposeCreditDetailView;
    }

    public ProposeCreditDetailView convertNewCreditToProposeCredit(NewCreditDetailView newCreditDetailView) {
        ProposeCreditDetailView proposeCreditDetailView = new ProposeCreditDetailView();
        if (newCreditDetailView != null) {
            proposeCreditDetailView = new ProposeCreditDetailView();
            proposeCreditDetailView.setSeq(newCreditDetailView.getSeq());
            proposeCreditDetailView.setId(newCreditDetailView.getId());
            proposeCreditDetailView.setTypeOfStep(CreditTypeOfStep.NEW.type());
            proposeCreditDetailView.setAccountName(newCreditDetailView.getAccountName());
            proposeCreditDetailView.setAccountNumber(newCreditDetailView.getAccountNumber());
            proposeCreditDetailView.setAccountSuf(newCreditDetailView.getAccountSuf());
            proposeCreditDetailView.setRequestType(newCreditDetailView.getRequestType());
            proposeCreditDetailView.setProductProgramView(newCreditDetailView.getProductProgramView());
            proposeCreditDetailView.setCreditFacilityView(newCreditDetailView.getCreditTypeView());
            proposeCreditDetailView.setLimit(newCreditDetailView.getLimit());
            proposeCreditDetailView.setGuaranteeAmount(newCreditDetailView.getGuaranteeAmount());
            proposeCreditDetailView.setUseCount(newCreditDetailView.getUseCount());
            proposeCreditDetailView.setNoFlag(newCreditDetailView.isNoFlag());
        }
        return proposeCreditDetailView;
    }

    public ProposeCreditDetailView convertExistingCreditToProposeCredit(ExistingCreditDetailView existingCreditDetailView, int seqNumber) {
        ProposeCreditDetailView proposeCreditDetailView = new ProposeCreditDetailView();
        if (existingCreditDetailView != null) {
            proposeCreditDetailView = new ProposeCreditDetailView();
            proposeCreditDetailView.setSeq(seqNumber);
            proposeCreditDetailView.setId(existingCreditDetailView.getId());
            proposeCreditDetailView.setTypeOfStep(CreditTypeOfStep.EXISTING.type());
            proposeCreditDetailView.setAccountName(existingCreditDetailView.getAccountName());
            proposeCreditDetailView.setAccountNumber(existingCreditDetailView.getAccountNumber());
            proposeCreditDetailView.setAccountSuf(existingCreditDetailView.getAccountSuf());
            proposeCreditDetailView.setProductProgramView(existingCreditDetailView.getExistProductProgramView());
            proposeCreditDetailView.setCreditFacilityView(existingCreditDetailView.getExistCreditTypeView());
            proposeCreditDetailView.setLimit(existingCreditDetailView.getLimit());
        }
        return proposeCreditDetailView;
    }

    public ProposeCreditDetailView convertExistingCreditToProposeCredit(ExistingCreditDetailView existingCreditDetailView) {
        ProposeCreditDetailView proposeCreditDetailView = new ProposeCreditDetailView();
        if (existingCreditDetailView != null) {
            proposeCreditDetailView = new ProposeCreditDetailView();
            proposeCreditDetailView.setSeq(existingCreditDetailView.getNo()); //todo:
            proposeCreditDetailView.setId(existingCreditDetailView.getId());
            proposeCreditDetailView.setTypeOfStep(CreditTypeOfStep.EXISTING.type());
            proposeCreditDetailView.setAccountName(existingCreditDetailView.getAccountName());
            proposeCreditDetailView.setAccountNumber(existingCreditDetailView.getAccountNumber());
            proposeCreditDetailView.setAccountSuf(existingCreditDetailView.getAccountSuf());
            proposeCreditDetailView.setProductProgramView(existingCreditDetailView.getExistProductProgramView());
            proposeCreditDetailView.setCreditFacilityView(existingCreditDetailView.getExistCreditTypeView());
            proposeCreditDetailView.setLimit(existingCreditDetailView.getLimit());
        }
        return proposeCreditDetailView;
    }

    public ProposeCreditDetailView copyToNewView(ProposeCreditDetailView originalProposeCreditDetailView, boolean isNewId) {
        ProposeCreditDetailView proposeCreditDetailView = new ProposeCreditDetailView();
        if (originalProposeCreditDetailView != null) {
            proposeCreditDetailView.setId(isNewId ? 0 : originalProposeCreditDetailView.getId());
            proposeCreditDetailView.setSeq(originalProposeCreditDetailView.getSeq());
            proposeCreditDetailView.setNoFlag(originalProposeCreditDetailView.isNoFlag());
            proposeCreditDetailView.setTypeOfStep(originalProposeCreditDetailView.getTypeOfStep());
            proposeCreditDetailView.setAccountName(originalProposeCreditDetailView.getAccountName());
            proposeCreditDetailView.setAccountNumber(originalProposeCreditDetailView.getAccountNumber());
            proposeCreditDetailView.setAccountSuf(originalProposeCreditDetailView.getAccountSuf());
            proposeCreditDetailView.setRequestType(originalProposeCreditDetailView.getRequestType());
            proposeCreditDetailView.setProductProgramView(originalProposeCreditDetailView.getProductProgramView());
            proposeCreditDetailView.setCreditFacilityView(originalProposeCreditDetailView.getCreditFacilityView());
            proposeCreditDetailView.setLimit(originalProposeCreditDetailView.getLimit());
            proposeCreditDetailView.setGuaranteeAmount(originalProposeCreditDetailView.getGuaranteeAmount());
            proposeCreditDetailView.setUseCount(originalProposeCreditDetailView.getUseCount());
        }
        return proposeCreditDetailView;
    }

    public List<ProposeCreditDetailView> copyToNewViews(List<ProposeCreditDetailView> originalProposeCreditDetailViews, boolean isNewId) {
        List<ProposeCreditDetailView> proposeCreditDetailViewList = new ArrayList<ProposeCreditDetailView>();
        if (originalProposeCreditDetailViews != null && originalProposeCreditDetailViews.size() > 0) {
            for (ProposeCreditDetailView proposeCreditDetailView : originalProposeCreditDetailViews) {
                proposeCreditDetailViewList.add(copyToNewView(proposeCreditDetailView, isNewId));
            }
        }
        return proposeCreditDetailViewList;
    }

}
