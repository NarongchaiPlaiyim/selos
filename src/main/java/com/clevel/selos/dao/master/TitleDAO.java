package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Title;
import org.slf4j.Logger;

import javax.inject.Inject;

public class TitleDAO extends GenericDAO<Title,Integer> {
    @Inject
    private Logger log;

    @Inject
    public TitleDAO() {
    }
}
