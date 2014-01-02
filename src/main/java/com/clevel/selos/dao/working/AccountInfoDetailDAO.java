package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.AccountInfoDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class AccountInfoDetailDAO extends GenericDAO<AccountInfoDetail, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public AccountInfoDetailDAO() {

    }

    public List<AccountInfoDetail> findByAccountInfoDetailId(long accountInfoDetailId) {
        log.info("findByAccountInfoDetailId : {}", accountInfoDetailId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("accountInfo.id", accountInfoDetailId));
        criteria.addOrder(Order.asc("id"));
        List<AccountInfoDetail> accountInfoDetailList = criteria.list();

        return accountInfoDetailList;
    }
}
