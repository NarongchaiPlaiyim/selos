package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.ProductFormula;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
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
    public ProductFormula findProductFormulaPropose(PrdProgramToCreditType programToCreditType,int creditCusType, int specialProgramId,int applyTcg) {
        log.debug("findProductFormulaPropose :: programToCreditType : {}, creditCusType : {}, specialProgramId : {}, applyTcg : {}"
                ,programToCreditType, creditCusType, specialProgramId, applyTcg);
        String query = "SELECT productFormula FROM ProductFormula productFormula WHERE ( creditCusType = " + creditCusType + " OR creditCusType = 0 ) AND ";
        query = query + "( applyTCG = " + applyTcg + " OR applyTCG = 0 ) AND ( specialProgram = " + specialProgramId + " OR specialProgram = 3 ) AND ";
        query = query + "programToCreditType.id = " + programToCreditType.getId();
        log.debug("findProductFormulaPropose :: query : {}",query);
        List<ProductFormula> productFormulaList = getSession().createQuery(query).list();

        if(productFormulaList != null && productFormulaList.size() > 0){
            log.debug("Return Product Formula :: {}", productFormulaList.get(0).getId());
            return productFormulaList.get(0);
        }
        log.debug("Return Null");
        return null;
    }

    public ProductFormula findProductFormulaPropose(PrdProgramToCreditType programToCreditType,int creditCusType, int specialProgramId,int applyTcg,int dbrMarketableFlag) {
        log.debug("findProductFormulaPropose :: programToCreditType : {}, creditCusType : {}, specialProgramId : {}, applyTcg : {}"
                ,programToCreditType, creditCusType, specialProgramId, applyTcg);
        String query = "SELECT productFormula FROM ProductFormula productFormula WHERE ( creditCusType = " + creditCusType + " OR creditCusType = 0 ) AND ";
        query = query + "( applyTCG = " + applyTcg + " OR applyTCG = 0 ) AND ( specialProgram = " + specialProgramId + " OR specialProgram = 3 ) AND ";
        query = query + "programToCreditType.id = " + programToCreditType.getId() + " AND ";
        query = query + "marketableFlag = " + dbrMarketableFlag;
        log.debug("findProductFormulaPropose :: query : {}",query);
        List<ProductFormula> productFormulaList = getSession().createQuery(query).list();

        if(productFormulaList != null && productFormulaList.size() > 0){
            log.debug("Return Product Formula :: {}", productFormulaList.get(0).getId());
            return productFormulaList.get(0);
        }
        log.debug("Return Null");
        return null;
    }
}
