package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.relation.ProductProgramToCreditType;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ProductProgramToCreditTypeDAO extends GenericDAO<ProductProgramToCreditType,Integer> {
    @Inject
    private Logger log;

    @Inject
    public ProductProgramToCreditTypeDAO() {
    }
}
