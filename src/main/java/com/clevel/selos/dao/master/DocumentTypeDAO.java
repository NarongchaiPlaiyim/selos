package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.DocumentType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class DocumentTypeDAO extends GenericDAO<DocumentType, Integer> {
    @Inject
    private Logger log;

    @Inject
    public DocumentTypeDAO(){

    }
}
