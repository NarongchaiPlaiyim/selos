package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.TCGDetail;
import org.slf4j.Logger;

import javax.inject.Inject;

public class TCGDetailDAO extends GenericDAO<TCGDetail, Integer> {
    @Inject
    private Logger log;

    @Inject
    public TCGDetailDAO(){

    }
}
