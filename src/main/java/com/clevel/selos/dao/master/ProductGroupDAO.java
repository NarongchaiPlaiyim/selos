package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.ProductGroup;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ProductGroupDAO extends GenericDAO<ProductGroup,Integer> {
    @Inject
    private Logger log;

    @Inject
    public ProductGroupDAO() {
    }
}
