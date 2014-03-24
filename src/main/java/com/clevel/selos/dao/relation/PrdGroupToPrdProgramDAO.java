package com.clevel.selos.dao.relation;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class PrdGroupToPrdProgramDAO extends GenericDAO<PrdGroupToPrdProgram, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public PrdGroupToPrdProgramDAO() {
    }

    @SuppressWarnings("unchecked")
    public List<PrdGroupToPrdProgram> getListPrdProByPrdGroup(ProductGroup productGroup) {
        log.info("getListPrdProByPrdGroup. (ProductGroup: {})", productGroup);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("productGroup", productGroup));
        criteria.addOrder(Order.asc("productProgram.id"));

        List<PrdGroupToPrdProgram> list = criteria.list();

        log.info("getList. (result size: {})", list.size());

        return list;

    }
    public List<PrdGroupToPrdProgram> getListPrdProByPrdGroupForExistCredit(ProductGroup productGroup) {

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("addExistingCredit", 1));
        criteria.addOrder(Order.asc("productProgram.id"));

        List<PrdGroupToPrdProgram> list = criteria.list();

        log.info("getList. (result size: {})", list.size());

        return list;

    }


    @SuppressWarnings("unchecked")
    public List<PrdGroupToPrdProgram> getListPrdGroupToPrdProgramPropose(ProductGroup productGroup) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("productGroup", productGroup));
        criteria.add(Restrictions.eq("addProposeCredit", 1));
        criteria.addOrder(Order.asc("productProgram.id"));
        List<PrdGroupToPrdProgram> list = criteria.list();

        log.info("getList. (result size: {})", list.size());

        return list;

    }

    @SuppressWarnings("unchecked")
    public List<PrdGroupToPrdProgram> getListPrdGroupToPrdProgramProposeAll() {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("addProposeCredit", 1));
        criteria.addOrder(Order.asc("productProgram.id"));
        List<PrdGroupToPrdProgram> list = criteria.list();

        log.info("getList. (result size: {})", list.size());

        return list;

    }
}
