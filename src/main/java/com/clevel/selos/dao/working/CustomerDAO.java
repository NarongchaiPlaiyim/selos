package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.testrm.CardType;
import com.clevel.selos.model.db.working.Customer;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class CustomerDAO extends GenericDAO<Customer,String> {
    @Inject
    private Logger log;

    @Inject
    public CustomerDAO() {
    }



}
