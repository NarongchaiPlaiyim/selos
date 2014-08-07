package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.ApprovalAuthority;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ApprovalAuthorityDAO extends GenericDAO<ApprovalAuthority, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ApprovalAuthorityDAO() {
    }

    public List<ApprovalAuthority> getList(){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("active", 1));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }
}
