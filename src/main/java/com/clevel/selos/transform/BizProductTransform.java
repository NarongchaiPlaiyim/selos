package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.BizProduct;
import com.clevel.selos.model.view.BizInfoFullView;
import com.clevel.selos.model.view.BizProductView;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 25/9/2556
 * Time: 14:59 น.
 * To change this template use File | Settings | File Templates.
 */
public class BizProductTransform extends Transform {

    public BizProductView transformToView(BizProduct bizProduct){

        BizProductView bizProductView = new BizProductView();
        bizProductView.setNo(bizProduct.getNo());
        bizProductView.setProductType(bizProduct.getProductType());
        bizProductView.setPercentEBIT(bizProduct.getPercentEBIT());
        bizProductView.setPercentSalesVolume(bizProduct.getPercentSalesVolume());
        bizProductView.setProductDetail(bizProduct.getProductDetail());

        return bizProductView;
    }

    public BizProduct transformToModel(BizProductView bizProductView,BizInfoDetail bizInfoDetail){

        BizProduct bizProduct = new BizProduct();
        bizProduct.setNo(bizProductView.getNo());
        bizProduct.setProductType(bizProductView.getProductType());
        bizProduct.setPercentEBIT(bizProductView.getPercentEBIT());
        bizProduct.setPercentSalesVolume(bizProductView.getPercentSalesVolume());
        bizProduct.setProductDetail(bizProductView.getProductDetail());
        bizProduct.setBizInfoDetail(bizInfoDetail);

        return bizProduct;
    }

}
