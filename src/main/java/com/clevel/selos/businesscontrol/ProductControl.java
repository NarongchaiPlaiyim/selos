package com.clevel.selos.businesscontrol;

import com.clevel.selos.model.view.CreditTypeView;
import com.clevel.selos.model.view.PrdProgramToCreditTypeView;
import com.clevel.selos.model.view.ProductProgramView;

import javax.ejb.Stateless;

@Stateless
public class ProductControl {

    public PrdProgramToCreditTypeView getPrdProgramToCreditTypeView(CreditTypeView creditTypeView, ProductProgramView productProgramView){
        PrdProgramToCreditTypeView prdProgramToCreditTypeView = new PrdProgramToCreditTypeView();
        return null;
    }



}
