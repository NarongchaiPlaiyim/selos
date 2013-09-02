package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.CreditType;
import com.clevel.selos.model.db.master.ProductProgram;
import org.slf4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import java.util.List;

public class CreditTypeDAO extends GenericDAO<CreditType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public CreditTypeDAO() {
    }


}
