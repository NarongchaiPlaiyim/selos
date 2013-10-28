package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.AddressType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class AddressTypeDAO extends GenericDAO<AddressType, Integer> {
    @Inject
    private Logger log;

    @Inject
    public AddressTypeDAO() {
    }
}
