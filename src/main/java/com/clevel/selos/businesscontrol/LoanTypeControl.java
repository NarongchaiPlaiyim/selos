package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.AccountTypeDAO;
import com.clevel.selos.model.db.master.AccountType;
import com.clevel.selos.model.view.LoanTypeView;
import com.clevel.selos.transform.LoanTypeTransform;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class LoanTypeControl extends BusinessControl {

    @Inject
    LoanTypeTransform loanTypeTransform;

    @Inject
    AccountTypeDAO loanTypeDAO;

    public List<LoanTypeView> getListLoanTypeByCus(int customerEntity) {
        List<AccountType> loanTypes = loanTypeDAO.getListLoanTypeByCusEntity(customerEntity);
        List<LoanTypeView> loanTypeViews = loanTypeTransform.getLoanTypeViews(loanTypes);
        return loanTypeViews;
    }

}
