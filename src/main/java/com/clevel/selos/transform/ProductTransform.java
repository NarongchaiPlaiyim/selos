package com.clevel.selos.transform;

import com.clevel.selos.dao.master.CreditTypeDAO;
import com.clevel.selos.dao.master.ProductGroupDAO;
import com.clevel.selos.dao.master.ProductProgramDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.db.relation.PrdProgramToCreditType;
import com.clevel.selos.model.view.*;
import com.clevel.selos.model.view.master.ProductGroupView;
import com.clevel.selos.transform.master.SpecialProgramTransform;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProductTransform extends Transform{
    @SELOS
    @Inject
    private Logger log;
    @Inject
    CreditTypeDAO creditTypeDAO;
    @Inject
    ProductProgramDAO productProgramDAO;
    @Inject
    ProductGroupDAO productGroupDAO;
    @Inject
    SpecialProgramTransform specialProgramTransform;

    public PrdGroupToPrdProgramView transformToView(PrdGroupToPrdProgram prdGroupToPrdProgram){
        PrdGroupToPrdProgramView prdGroupToPrdProgramView = new PrdGroupToPrdProgramView();
        prdGroupToPrdProgramView.setProductProgramView(transformToView(prdGroupToPrdProgram.getProductProgram()));
        prdGroupToPrdProgramView.setProductGroupView(transformToView(prdGroupToPrdProgram.getProductGroup()));
        return prdGroupToPrdProgramView;
    }

    public List<PrdGroupToPrdProgramView> transformToViewList(List<PrdGroupToPrdProgram> prdGroupToPrdProgramList) {
        List<PrdGroupToPrdProgramView> prdGroupToPrdProgramViewList = new ArrayList<PrdGroupToPrdProgramView>();
        if (prdGroupToPrdProgramList != null && !prdGroupToPrdProgramList.isEmpty()) {
            for (PrdGroupToPrdProgram prdGroupToPrdProgram : prdGroupToPrdProgramList) {
                prdGroupToPrdProgramViewList.add(transformToView(prdGroupToPrdProgram));
            }
        }
        return prdGroupToPrdProgramViewList;
    }

    public ProductProgramView transformToView(ProductProgram productProgram){
        ProductProgramView productProgramView = new ProductProgramView();
        if(productProgram != null){
            productProgramView.setId(productProgram.getId());
            productProgramView.setName(productProgram.getName());
            productProgramView.setDescription(productProgram.getDescription());
            productProgramView.setActive(productProgram.getActive());
            productProgramView.setBrmsCode(productProgram.getBrmsCode());
        }
        return productProgramView;
    }

    public ProductGroupView transformToView(ProductGroup productGroup){
        ProductGroupView productGroupView = new ProductGroupView();
        if(productGroup != null){
            productGroupView.setId(productGroup.getId());
            productGroupView.setName(productGroup.getName());
            productGroupView.setDescription(productGroup.getDescription());
            productGroupView.setBrmsCode(productGroup.getBrmsCode());
            productGroupView.setActive(productGroup.getActive());
            productGroupView.setAddProposeCredit(productGroup.getAddProposeCredit());
            productGroupView.setSpecialLTV(productGroup.getSpecialLTV());
        }
        return productGroupView;
    }

    public ProductFormulaView transformToView(ProductFormula productFormula){
        ProductFormulaView productFormulaView = new ProductFormulaView();
        if(productFormula != null){
            productFormulaView.setId(productFormula.getId());
            productFormulaView.setApplyTCG(productFormula.getApplyTCG());
            productFormulaView.setActive(productFormula.getActive());
            productFormulaView.setCreditCusType(productFormula.getCreditCusType());
            productFormulaView.setDbrCalculate(productFormula.getDbrCalculate());
            productFormulaView.setDbrMethod(productFormula.getDbrMethod());
            productFormulaView.setDbrSpread(productFormula.getDbrSpread());
            productFormulaView.setExposureMethod(productFormula.getExposureMethod());
            productFormulaView.setProductCode(productFormula.getProductCode());
            productFormulaView.setProjectCode(productFormula.getProjectCode());
            productFormulaView.setProgramToCreditTypeView(transformToView(productFormula.getProgramToCreditType()));
            productFormulaView.setReduceFrontEndFee(productFormula.getReduceFrontEndFee());
            productFormulaView.setReducePricing(productFormula.getReducePricing());
            productFormulaView.setSpecialProgramView(specialProgramTransform.transformToView(productFormula.getSpecialProgram()));
            productFormulaView.setWcCalculate(productFormula.getWcCalculate());
        }
        return productFormulaView;
    }

    public PrdProgramToCreditTypeView transformToView(PrdProgramToCreditType prdProgramToCreditType){
        PrdProgramToCreditTypeView prdProgramToCreditTypeView = new PrdProgramToCreditTypeView();
        if(prdProgramToCreditType != null){
            prdProgramToCreditTypeView.setId(prdProgramToCreditType.getId());
            prdProgramToCreditTypeView.setAddExistingCredit(prdProgramToCreditType.getAddExistingCredit());
            prdProgramToCreditTypeView.setAddProposeCredit(prdProgramToCreditType.getAddProposeCredit());
            prdProgramToCreditTypeView.setCreditTypeView(transformToView(prdProgramToCreditType.getCreditType()));
            prdProgramToCreditTypeView.setProductProgramView(transformToView(prdProgramToCreditType.getProductProgram()));
        }
        return prdProgramToCreditTypeView;
    }

    public CreditTypeView transformToView(CreditType creditType){
        CreditTypeView creditTypeView = new CreditTypeView();
        if(creditType != null){
            creditTypeView.setId(creditType.getId());
            creditTypeView.setActive(creditType.getActive());
            creditTypeView.setName(creditType.getName());
            creditTypeView.setDescription(creditType.getDescription());
            creditTypeView.setBrmsCode(creditType.getBrmsCode());
            creditTypeView.setComsIntType(creditType.getComsIntType());
            creditTypeView.setCanSplit(creditType.getCanSplit());
            creditTypeView.setCalLimitType(creditType.getCalLimitType());
            creditTypeView.setCreditGroup(creditType.getCreditGroup());
            creditTypeView.setContingentFlag(creditType.isContingentFlag());
        }
        return creditTypeView;
    }

    public CreditType transformToModel(CreditTypeView creditTypeView){
        log.debug("begin transformToModel(CreditTypeView {})", creditTypeView);
        if(creditTypeView!=null && creditTypeView.getId() != 0){
            try{
                CreditType creditType = creditTypeDAO.findById(creditTypeView.getId());
                return creditType;
            }catch (Exception ex){
                log.info("cannot find CreditType for {}", creditTypeView);
            }
        }
        return null;
    }

    public ProductProgram transformToModel(ProductProgramView productProgramView){
        log.debug("begin transformToModel(productProgramView {})", productProgramView);
        if(productProgramView!=null && productProgramView.getId() != 0){
            try{
                ProductProgram productProgram = productProgramDAO.findById(productProgramView.getId());
                return productProgram;
            } catch (Exception ex){
                log.info("cannot find ProductProgram for {}", productProgramView);
            }
        }
        return null;
    }

    public List<ProductProgramView> transformToView(List<ProductProgram> productProgramList) {
        try {
            if(productProgramList!=null && productProgramList.size()>0) {
                log.debug("begin transformToView(productProgramList size : {})", productProgramList.size());
                List<ProductProgramView> productProgramViewList = new ArrayList<ProductProgramView>();
                for(ProductProgram productProgram : productProgramList) {
                    ProductProgramView productProgramView = transformToView(productProgram);
                    productProgramViewList.add(productProgramView);
                }
                return productProgramViewList;
            }
        } catch (Exception ex) {
            log.error("transformToView: {}", ex);
        }
        return null;
    }

    public Map<Integer, ProductGroupView> transformToCache(List<ProductGroup> productGroupList){
        if(productGroupList == null || productGroupList.size() == 0)
            return null;
        Map<Integer, ProductGroupView> _tmpMap = new ConcurrentHashMap<Integer, ProductGroupView>();
        for(ProductGroup productGroup : productGroupList){
            ProductGroupView productGroupView = transformToView(productGroup);
            _tmpMap.put(productGroupView.getId(), productGroupView);
        }
        return _tmpMap;
    }

}
