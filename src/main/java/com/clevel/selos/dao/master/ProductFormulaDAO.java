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
import java.util.List;

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

    public ProductFormula findProductFormulaPropose(PrdProgramToCreditType programToCreditType,int creditCusType, SpecialProgram specialProgram,int applyTcg) {
        log.debug("findProductFormulaPropose :: programToCreditType : {}, creditCusType : {}, specialProgram : {}, applyTcg : {}"
                ,programToCreditType, creditCusType, specialProgram, applyTcg);
        String query = "SELECT productFormula FROM ProductFormula productFormula WHERE ( creditCusType = " + creditCusType + " OR creditCusType = 0 ) AND ";
        query = query + "( applyTCG = " + applyTcg + " OR applyTCG = 0 ) AND ( specialProgram = " + specialProgram.getId() + " OR specialProgram = 3 ) AND ";
        query = query + "programToCreditType.id = " + programToCreditType.getId();
        log.debug("findProductFormulaPropose :: query : {}",query);
        List<ProductFormula> productFormulaList = getSession().createQuery(query).list();

        if(productFormulaList != null && productFormulaList.size() > 0){
            return productFormulaList.get(0);
        }
        return null;
    }

    public ProductFormula findProductFormulaPropose(PrdProgramToCreditType programToCreditType,int creditCusType, int specialProgramId,int applyTcg) {
        log.debug("findProductFormulaPropose :: programToCreditType : {}, creditCusType : {}, specialProgramId : {}, applyTcg : {}"
                ,programToCreditType, creditCusType, specialProgramId, applyTcg);
        String query = "SELECT productFormula FROM ProductFormula productFormula WHERE ( creditCusType = " + creditCusType + " OR creditCusType = 0 ) AND ";
        query = query + "( applyTCG = " + applyTcg + " OR applyTCG = 0 ) AND ( specialProgram = " + specialProgramId + " OR specialProgram = 3 ) AND ";
        query = query + "programToCreditType.id = " + programToCreditType.getId();
        log.debug("findProductFormulaPropose :: query : {}",query);
        List<ProductFormula> productFormulaList = getSession().createQuery(query).list();

        if(productFormulaList != null && productFormulaList.size() > 0){
            return productFormulaList.get(0);
        }
        return null;

    }

}
