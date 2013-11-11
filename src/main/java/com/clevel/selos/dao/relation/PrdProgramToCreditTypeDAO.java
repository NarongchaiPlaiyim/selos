package com.clevel.selos.dao.relation;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.ProductProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class PrdProgramToCreditTypeDAO extends GenericDAO<PrdProgramToCreditType, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public PrdProgramToCreditTypeDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<PrdProgramToCreditType> getListPrdProByPrdprogram(ProductProgram productProgram) {
        log.info("getListPrdProByPrdprogram. (ProductProgram: {})", productProgram);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("productProgram", productProgram));
        criteria.addOrder(Order.asc("creditType.id"));
        List<PrdProgramToCreditType> list = criteria.list();

        log.info("getList. (result size: {})", list.size());

        return list;

    }
}
