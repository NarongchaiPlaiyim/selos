package com.clevel.selos.transform;

import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.ProductProgram;
import com.clevel.selos.model.db.relation.PrdGroupToPrdProgram;
import com.clevel.selos.model.view.PrdGroupToPrdProgramView;
import com.clevel.selos.model.view.ProductGroupView;
import com.clevel.selos.model.view.ProductProgramView;

public class ProductTransform extends Transform{

    public PrdGroupToPrdProgramView transformToView(PrdGroupToPrdProgram prdGroupToPrdProgram){
        PrdGroupToPrdProgramView prdGroupToPrdProgramView = new PrdGroupToPrdProgramView();
        prdGroupToPrdProgramView.setProductProgramView(transformToView(prdGroupToPrdProgram.getProductProgram()));
        prdGroupToPrdProgramView.setProductGroupView(transformToView(prdGroupToPrdProgram.getProductGroup()));
        return prdGroupToPrdProgramView;
    }

    public ProductProgramView transformToView(ProductProgram productProgram){
        ProductProgramView productProgramView = new ProductProgramView();
        if(productProgram !=null){
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
        }
        return productGroupView;
    }
}
