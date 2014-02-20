package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.ProductFormulaDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.ProductFormula;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.view.CreditTypeView;
import com.clevel.selos.model.view.PrdProgramToCreditTypeView;
import com.clevel.selos.model.view.ProductFormulaView;
import com.clevel.selos.model.view.ProductProgramView;
import com.clevel.selos.transform.ProductTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProductControl extends BusinessControl{

    @Inject
    @SELOS
    Logger logger;

    @Inject
    ProductTransform productTransform;

    @Inject
    ProductFormulaDAO productFormulaDAO;

    @Inject
    PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;

    @Inject
    public ProductControl(){}

    public PrdProgramToCreditTypeView getPrdProgramToCreditTypeView(CreditTypeView creditTypeView, ProductProgramView productProgramView){
        PrdProgramToCreditTypeView prdProgramToCreditTypeView = new PrdProgramToCreditTypeView();
        return null;
    }

    public List<PrdProgramToCreditTypeView> getPrdProgramToCreditTypeViewList(ProductProgramView productProgramView){
        logger.info("begin getPrdProgramToCreditTypeViewList {}", productProgramView);
        List<PrdProgramToCreditTypeView> programToCreditTypeViewList = new ArrayList<PrdProgramToCreditTypeView>();
        if(productProgramView != null && productProgramView.getId() != 0){
            List<PrdProgramToCreditType> prdProgramToCreditTypeList = prdProgramToCreditTypeDAO.getListCreditProposeByPrdprogram(productProgramView.getId());
            for(PrdProgramToCreditType prdProgramToCreditType : prdProgramToCreditTypeList){
                programToCreditTypeViewList.add(productTransform.transformToView(prdProgramToCreditType));
            }
        }
        logger.info("return getPrdProgramToCreditTypeViewList size {}", programToCreditTypeViewList.size());
        return programToCreditTypeViewList;
    }

    public ProductFormulaView getProductFormulaView(int creditTypeId, int productProgramId, int creditCustomerTypeId, int specialProgramId, int applyTCG){
        logger.info("find product formula getProductFormulaView creditTypeId{}, productProgramId{}, creditCustomerTypeId{}, specialProgramId{}, applyTCG{}", creditTypeId, productProgramId, creditCustomerTypeId, specialProgramId, applyTCG);
        PrdProgramToCreditType prdProgramToCreditType = prdProgramToCreditTypeDAO.getPrdProgramToCreditType(creditTypeId, productProgramId);
        return getProductFormulaView(prdProgramToCreditType, creditCustomerTypeId, specialProgramId, applyTCG);
    }

    public ProductFormulaView getProductFormulaView(PrdProgramToCreditType prdProgramToCreditType, int creditCustomerTypeId, int specialProgramId, int applyTCG){
        logger.info("find product formula getProductFormulaView PrdProgramToCreditType{}, creditCustomerTypeId{}, specialProgramId{}, applyTCG{}", prdProgramToCreditType, creditCustomerTypeId, specialProgramId, applyTCG);
        try{
            ProductFormula productFormula = productFormulaDAO.findProductFormulaPropose(prdProgramToCreditType, creditCustomerTypeId, specialProgramId, applyTCG);
            return productTransform.transformToView(productFormula);
        }catch(Exception ex){
            logger.error("Cannot find productFormula with ");
        }
        return new ProductFormulaView();
    }

}
