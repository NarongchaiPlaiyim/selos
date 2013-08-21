package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.UserTitle;
import org.slf4j.Logger;

import javax.inject.Inject;

public class UserTitleDAO extends GenericDAO<UserTitle,Integer> {
    @Inject
    private Logger log;

    @Inject
    public UserTitleDAO() {
    }
}
