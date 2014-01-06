package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.AccountInfoDetailAccountName;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class AccountInfoDetailAccountNameDAO extends GenericDAO<AccountInfoDetailAccountName, Long>{
    @Inject
    @SELOS
    Logger log;
    @Inject
    public AccountInfoDetailAccountNameDAO() {

    }

    public List<AccountInfoDetailAccountName> findByAccountInfoDetailAccountNameId(long accountInfoDetailAccountNameId) {
        log.info("findByAccountInfoDetailAccountNameId : {}", accountInfoDetailAccountNameId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("accountInfoDetailAccountName.id", accountInfoDetailAccountNameId));
        List<AccountInfoDetailAccountName> accountInfoDetailAccountNameList = criteria.list();

        return accountInfoDetailAccountNameList;
    }
}
