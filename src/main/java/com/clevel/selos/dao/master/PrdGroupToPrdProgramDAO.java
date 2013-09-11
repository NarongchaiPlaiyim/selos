package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.ProductProgram;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import java.util.List;

public class PrdGroupToPrdProgramDAO extends GenericDAO<PrdGroupToPrdProgram,Integer> {
    @Inject
    private Logger log;

    @Inject
    public PrdGroupToPrdProgramDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<PrdGroupToPrdProgram> getListPrdProByPrdGroup(ProductGroup productGroup ){
        log.info("getListPrdProByPrdGroup. (ProductGroup: {})", productGroup);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("productGroup", productGroup));
        criteria.addOrder(Order.asc("productProgram.id"));

        List<PrdGroupToPrdProgram> list = criteria.list();

        log.info("getList. (result size: {})", list.size());

        return list;

    }
}