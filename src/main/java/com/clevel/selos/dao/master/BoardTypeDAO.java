package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.BoardType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class BoardTypeDAO extends GenericDAO<BoardType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public BoardTypeDAO() {
    }
}
