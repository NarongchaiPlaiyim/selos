package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BorrowingType;
import com.clevel.selos.model.db.master.CustomerEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class BorrowingTypeDAO extends GenericDAO<BorrowingType, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public BorrowingTypeDAO() {
    }

    public List<BorrowingType> findByCustomerEntity(CustomerEntity customerEntity) {
        log.info("findByCustomerEntity : {}", customerEntity);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customerEntity", customerEntity));
        List<BorrowingType> borrowingTypeList = criteria.list();

        return borrowingTypeList;
    }

    public List<BorrowingType> findByCustomerEntityId(int customerEntityId) {
        log.info("findByCustomerEntityId : {}", customerEntityId);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customerEntity.id", customerEntityId));
        List<BorrowingType> borrowingTypeList = criteria.list();

        return borrowingTypeList;
    }
}
