package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.AccountStatus;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class AccountStatusDAO extends GenericDAO<AccountStatus, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public AccountStatusDAO() {
    }

    public AccountStatus getIndividualByCode(String code) {
        log.debug("getIndividualByCode. (code: {}", code);
        AccountStatus accountStatus = new AccountStatus();
        if (!Util.isEmpty(code)) {
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq("ncbCodeInd", code.trim()));
            accountStatus = (AccountStatus) criteria.uniqueResult();

            log.debug("getIndividualByCode. (accountStatus: {})", accountStatus);
        }
        return accountStatus;
    }

    public AccountStatus getJuristicByCode(String code) {
        log.debug("getJuristicByCode. (code: {}", code);
        AccountStatus accountStatus = new AccountStatus();
        if (!Util.isEmpty(code)) {
            Criteria criteria = createCriteria();
            criteria.add(Restrictions.eq("ncbCodeJur", code.trim()));
            accountStatus = (AccountStatus) criteria.uniqueResult();

            log.debug("getJuristicByCode. (accountStatus: {})", accountStatus);
        }
        return accountStatus;
    }
}
