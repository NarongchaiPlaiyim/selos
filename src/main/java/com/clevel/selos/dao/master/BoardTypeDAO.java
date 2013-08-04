package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.BoardType;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class BoardTypeDAO extends GenericDAO<BoardType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public BoardTypeDAO() {
    }
}
