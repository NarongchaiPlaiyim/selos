package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.AccountStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class AccountStatusDAO extends GenericDAO<AccountStatus,Integer> {
    @Inject
    private Logger log;

    @Inject
    public AccountStatusDAO() {
    }

    public AccountStatus getIndividualByCode(String code){
        log.debug("getIndividualByCode. (code: {}",code);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("individualCode", code));
        AccountStatus accountStatus = (AccountStatus) criteria.uniqueResult();

        log.debug("getIndividualByCode. (code: {})",code);
        return accountStatus;
    }

    public AccountStatus getJuristicByCode(String code){
        log.debug("getJuristicByCode. (code: {}",code);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("juristicCode", code));
        AccountStatus accountStatus = (AccountStatus) criteria.uniqueResult();

        log.debug("getJuristicByCode. (code: {})",code);
        return accountStatus;
    }
}
