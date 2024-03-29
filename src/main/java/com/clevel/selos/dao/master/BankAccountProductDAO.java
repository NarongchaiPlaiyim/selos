package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountProduct;
import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.SubCollateralType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BankAccountProductDAO extends GenericDAO<BankAccountProduct, Integer> {
    @Inject
    @SELOS
    Logger log;
    
    public BankAccountProductDAO() {
    }

    @Override
    public List<BankAccountProduct> findAll() {
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("active", 1));
        List<BankAccountProduct> list = criteria.list();
        return list;
    }

    public List<BankAccountProduct> findByBankAccountTypeId(int bankAccountTypeId) {
        log.info("findByAccountTypeId. (accountTypeId: {})", bankAccountTypeId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("bankAccountType.id", bankAccountTypeId));
        criteria.add(Restrictions.eq("active", 1));
        List<BankAccountProduct> accountProductList = criteria.list();
        log.info("findByAccountTypeId. (result size: {})", accountProductList.size());

        return accountProductList;
    }
    
    public BankAccountProduct findByCollateral(CollateralType collateralType,SubCollateralType subCollateralType) {
    	if (collateralType == null || subCollateralType == null)
    		return null;
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("collateralType", collateralType));
		criteria.add(Restrictions.eq("subCollateralType", subCollateralType));
		criteria.add(Restrictions.eq("active", 1));
		try {
			return (BankAccountProduct) criteria.uniqueResult();
		} catch (Throwable e) {}
		return null;
    }
}
