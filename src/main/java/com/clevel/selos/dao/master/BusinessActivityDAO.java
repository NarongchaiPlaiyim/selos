package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.BusinessActivity;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class BusinessActivityDAO extends GenericDAO<BusinessActivity,Integer> {
    @Inject
    private Logger log;

    @Inject
    public BusinessActivityDAO() {
    }
}
