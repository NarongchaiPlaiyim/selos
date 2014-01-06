package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.AccountInfoDetailCreditType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class AccountInfoDetailCreditTypeDAO extends GenericDAO<AccountInfoDetailCreditType, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public AccountInfoDetailCreditTypeDAO() {

    }

    public List<AccountInfoDetailCreditType> findByAccountInfoDetailCreditTypeId(long accountInfoDetailCreditTypeId) {
        log.info("findByAccountInfoDetailCreditType : {}", accountInfoDetailCreditTypeId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("accountInfoDetailCreditType.id", accountInfoDetailCreditTypeId));
        List<AccountInfoDetailCreditType> accountInfoDetailCreditTypeList = criteria.list();

        return accountInfoDetailCreditTypeList;
    }
}
