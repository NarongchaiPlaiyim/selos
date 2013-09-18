package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Reference;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ReferenceDAO extends GenericDAO<Reference,Integer> {
    @Inject
    private Logger log;

    @Inject
    public ReferenceDAO(){

    }
}
