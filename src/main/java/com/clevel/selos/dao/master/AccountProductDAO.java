package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.AccountProduct;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class AccountProductDAO extends GenericDAO<AccountProduct, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public AccountProductDAO() {
    }

    @Override
    public List<AccountProduct> findAll() {
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("active", 1));
        List<AccountProduct> list = criteria.list();
        return list;
    }

    public List<AccountProduct> findByBankAccountTypeId(int bankAccountTypeId) {
        log.info("findByAccountTypeId. (accountTypeId: {})", bankAccountTypeId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("bankAccountType.id", bankAccountTypeId));
        criteria.add(Restrictions.eq("active", 1));
        List<AccountProduct> accountProductList = criteria.list();
        log.info("findByAccountTypeId. (result size: {})", accountProductList.size());

        return accountProductList;
    }
}
