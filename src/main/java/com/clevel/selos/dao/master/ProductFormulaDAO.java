package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.ProductFormula;
import com.clevel.selos.model.db.master.SpecialProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ProductFormulaDAO extends GenericDAO<ProductFormula, Integer> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public ProductFormulaDAO() {
    }

    @SuppressWarnings("unchecked")
    public ProductFormula findProductFormula(PrdProgramToCreditType programToCreditType) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("programToCreditType", programToCreditType));
        ProductFormula productFormula = (ProductFormula) criteria.uniqueResult();
        return productFormula;
    }

    @SuppressWarnings("unchecked")
    public ProductFormula findProductFormulaForPropose(PrdProgramToCreditType programToCreditType,int CreditCusType,SpecialProgram specialProgram,int applyTcg) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("programToCreditType", programToCreditType));
        criteria.add(Restrictions.eq("creditCusType", CreditCusType));
        criteria.add(Restrictions.eq("specialProgram", specialProgram));
        criteria.add(Restrictions.eq("applyTCG", applyTcg));
        ProductFormula productFormula = (ProductFormula) criteria.uniqueResult();
        return productFormula;
    }
}
