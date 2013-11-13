package com.clevel.selos.dao.relation;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.relation.UserToAuthorizationDOA;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UserToAuthorizationDOADAO extends GenericDAO<UserToAuthorizationDOA, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public UserToAuthorizationDOADAO() {
    }
}
