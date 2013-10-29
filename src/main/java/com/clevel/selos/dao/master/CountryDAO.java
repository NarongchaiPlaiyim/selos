package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.Country;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CountryDAO extends GenericDAO<Country, Integer> {
    @Inject
    private Logger log;

    @Inject
    public CountryDAO() {
    }
}
