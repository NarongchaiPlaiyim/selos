package com.clevel.selos.transform;

import com.clevel.selos.dao.master.OpenAccountProductDAO;
import com.clevel.selos.model.db.master.OpenAccountProduct;

import javax.inject.Inject;

public class AccountInfoProductTypeTransform extends Transform {
    @Inject
    private OpenAccountProductDAO productTypeDAO;
    private OpenAccountProduct productType;

    @Inject
    public AccountInfoProductTypeTransform() {

    }

    public OpenAccountProduct transformToModel(final int id){
        productType = new OpenAccountProduct();
        productType = productTypeDAO.findById(id);
        return productType;
    }
}
