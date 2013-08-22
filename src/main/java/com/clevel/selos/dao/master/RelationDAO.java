package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Relation;
import org.slf4j.Logger;

import javax.inject.Inject;

public class RelationDAO extends GenericDAO<Relation,Integer> {
    @Inject
    private Logger log;

    @Inject
    public RelationDAO() {
    }
}
