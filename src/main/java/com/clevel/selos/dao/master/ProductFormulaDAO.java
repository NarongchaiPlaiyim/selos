package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.CreditCustomerType;
import com.clevel.selos.model.RadioValue;
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


    public ProductFormula findProductFormulaForPropose(PrdProgramToCreditType programToCreditType,int creditCusType,SpecialProgram specialProgram,int applyTcg) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("programToCreditType", programToCreditType));
        criteria.add(Restrictions.or(Restrictions.eq("creditCusType", creditCusType),Restrictions.eq("creditCusType", CreditCustomerType.NOT_SELECTED.value())));
        criteria.add(Restrictions.or(Restrictions.eq("specialProgram", specialProgram),Restrictions.eq("specialProgram.id", 3)));
        criteria.add(Restrictions.or(Restrictions.eq("applyTCG", applyTcg),Restrictions.eq("applyTCG", RadioValue.NOT_SELECTED.value())));
        ProductFormula productFormula = (ProductFormula)criteria.uniqueResult();
        return productFormula;
    }

}
