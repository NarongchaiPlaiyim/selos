package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Reference;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ReferenceDAO extends GenericDAO<Reference, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ReferenceDAO() {

    }

    public List<Reference> findByCustomerEntityId(int customerEntityId, int borrowerTypeId, int relationId) {
        log.info("findByCustomerEntityId. (customerEntityId: {}, borrowerTypeId : {}, relationId : {})", customerEntityId, borrowerTypeId, relationId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("customerEntity.id", customerEntityId));
        criteria.add(Restrictions.eq("borrowerType.id", borrowerTypeId));
        criteria.add(Restrictions.eq("relation.id", relationId));
        criteria.addOrder(Order.asc("id"));
        List<Reference> referenceList = criteria.list();
        log.info("findByCustomerEntityId. (result size: {})", referenceList.size());

        return referenceList;
    }
}
