package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.ProductProgram;
import com.clevel.selos.system.Config;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ProductProgramDAO extends GenericDAO<ProductProgram, Integer> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    @Config(name = "app.master.non.productprogram.id")
    String productProgramId;

    @Inject
    public ProductProgramDAO() {
    }

    public ProductProgram getNonProductProgram(){
        try {
            int prdProgramId = Integer.parseInt(productProgramId);
            Criteria criteria = getSession().createCriteria(getEntityClass())
                    .add(Restrictions.eq("id", prdProgramId));
            ProductProgram productProgram = (ProductProgram)criteria.uniqueResult();
            return productProgram;
        } catch (Exception e){

        }
        return null;
    }

    public List<ProductProgram> findExistingProductProgram(){
        try {
            Criteria criteria = getSession().createCriteria(getEntityClass())
                    .add(Restrictions.eq("isExisting", 1));
            List<ProductProgram> productProgramList = criteria.list();
            return productProgramList;
        } catch (Exception e){

        }
        return null;
    }
}
