package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.ReasonType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ReasonDAO extends GenericDAO<Reason, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ReasonDAO() {
    }

    @SuppressWarnings("unchecked")
    private List<Reason> getList(ReasonType reasonType) {
        log.debug("getList. (reasonType: {})", reasonType);

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", true));
        criteria.add(Restrictions.eq("reasonType", reasonType));
        criteria.addOrder(Order.asc("id"));

        List<Reason> list = criteria.list();
        log.debug("getList. (result size: {})", list.size());
        return list;
    }

    public List<Reason> getCancelList() {
        log.debug("getCancelList");

        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.add(Restrictions.eq("reasonType.id", 3));
        criteria.addOrder(Order.asc("id"));

        List<Reason> list = criteria.list();
        log.debug("getCancelList. (result size: {})", list.size());
        return list;
    }
}
