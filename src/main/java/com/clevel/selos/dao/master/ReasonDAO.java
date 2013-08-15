package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.ReasonType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class ReasonDAO extends GenericDAO<Reason, Integer> {
    @Inject
    private Logger log;

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

}
