package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.BizInfo;
import com.clevel.selos.model.db.working.BizProductDetail;
import com.clevel.selos.model.view.BizProductDetailView;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 25/9/2556
 * Time: 14:59 น.
 * To change this template use File | Settings | File Templates.
 */
public class BizProductDetailTransform extends Transform {

    public BizProductDetailView transformToView(BizProductDetail bizProductDetail){

        BizProductDetailView bizProductDetailView = new BizProductDetailView();
        bizProductDetailView.setNo(bizProductDetail.getNo());
        bizProductDetailView.setProductType(bizProductDetail.getProductType());
        bizProductDetailView.setPercentEBIT(bizProductDetail.getPercentEBIT());
        bizProductDetailView.setPercentSalesVolume(bizProductDetail.getPercentSalesVolume());
        bizProductDetailView.setProductDetail(bizProductDetail.getProductDetail());

        return bizProductDetailView;
    }

    public BizProductDetail transformToModel(BizProductDetailView bizProductDetailView,BizInfo bizInfo){

        BizProductDetail bizProductDetail = new BizProductDetail();
        bizProductDetail.setNo(bizProductDetailView.getNo());
        bizProductDetail.setProductType(bizProductDetailView.getProductType());
        bizProductDetail.setPercentEBIT(bizProductDetailView.getPercentEBIT());
        bizProductDetail.setPercentSalesVolume(bizProductDetailView.getPercentSalesVolume());
        bizProductDetail.setProductDetail(bizProductDetailView.getProductDetail());
        bizProductDetail.setBizInfo(bizInfo);

        return bizProductDetail;
    }

}
