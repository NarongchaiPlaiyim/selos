package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.ProductFormulaDAO;
import com.clevel.selos.dao.master.ProductGroupDAO;
import com.clevel.selos.dao.master.ProductProgramDAO;
import com.clevel.selos.dao.relation.PrdGroupToPrdProgramDAO;
import com.clevel.selos.dao.relation.PrdProgramToCreditTypeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.ProductFormula;
import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.ProductProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.view.*;
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
    private ProductTransform productTransform;

    @Inject
    private ProductFormulaDAO productFormulaDAO;
    @Inject
    private PrdGroupToPrdProgramDAO prdGroupToPrdProgramDAO;
    @Inject
    private PrdProgramToCreditTypeDAO prdProgramToCreditTypeDAO;
    @Inject
    private ProductGroupDAO productGroupDAO;
    @Inject
    private ProductProgramDAO productProgramDAO;

    @Inject
    public ProductControl(){}

    public List<PrdGroupToPrdProgramView> getPrdGroupToPrdProgramProposeAll() {
        return productTransform.transformToViewList(prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramProposeAll());
    }

    public List<PrdGroupToPrdProgramView> getPrdGroupToPrdProgramProposeByGroup(ProductGroup productGroup) {
        return productTransform.transformToViewList(prdGroupToPrdProgramDAO.getListPrdGroupToPrdProgramPropose(productGroup));
    }

    public List<ProductProgramView> getProductProgramAll() {
        return productTransform.transformToView(productProgramDAO.findAll());
    }

    public List<PrdGroupToPrdProgramView> getPrdGroupToPrdProgramFromAllPrdProgram() {
        List<PrdGroupToPrdProgramView> prdGroupToPrdProgramViewList = new ArrayList<PrdGroupToPrdProgramView>();
        List<ProductProgram> productProgramList = productProgramDAO.findAll();
        if (productProgramList != null) {
            PrdGroupToPrdProgramView prdGroupToPrdProgramView;
            for (ProductProgram productProgram : productProgramList) {
                prdGroupToPrdProgramView = new PrdGroupToPrdProgramView();
                prdGroupToPrdProgramView.setProductGroupView(new ProductGroupView());
                prdGroupToPrdProgramView.setProductProgramView(productTransform.transformToView(productProgram));
                prdGroupToPrdProgramViewList.add(prdGroupToPrdProgramView);
            }
        }
        return prdGroupToPrdProgramViewList;
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
