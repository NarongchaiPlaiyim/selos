package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.IDDocumentType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class IDDocumentTypeDAO extends GenericDAO<IDDocumentType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public IDDocumentTypeDAO() {
    }
}
