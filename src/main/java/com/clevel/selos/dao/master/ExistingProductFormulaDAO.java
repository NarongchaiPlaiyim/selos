package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.ExistingProductFormula;
import com.clevel.selos.model.db.master.ProductFormula;
import com.clevel.selos.model.db.master.SpecialProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ExistingProductFormulaDAO extends GenericDAO<ExistingProductFormula, Integer> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public ExistingProductFormulaDAO() {
    }

    @SuppressWarnings("unchecked")
    public ExistingProductFormula findProductFormula(String productCode, String projectCode, String productTypeCode) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("productCode", productCode));
        criteria.add(Restrictions.eq("projectCode", projectCode));
        criteria.add(Restrictions.eq("productTypeCode", productTypeCode));
        ExistingProductFormula existingProductFormula = (ExistingProductFormula) criteria.uniqueResult();
        return existingProductFormula;
    }

    public List<ExistingProductFormula> findProductFormula(String productCode, String projectCode) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("productCode", productCode));
        criteria.add(Restrictions.eq("projectCode", projectCode));
        List<ExistingProductFormula> existingProductFormula = criteria.list();
        return existingProductFormula;
    }
}
