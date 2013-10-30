package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.BizInfoDetail;
import com.clevel.selos.model.db.working.BizProductDetail;
import com.clevel.selos.model.view.BizProductDetailView;
import org.joda.time.DateTime;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 25/9/2556
 * Time: 14:59 น.
 * To change this template use File | Settings | File Templates.
 */
public class BizProductDetailTransform extends Transform {

    public BizProductDetailView transformToView(BizProductDetail bizProductDetail) {

        BizProductDetailView bizProductDetailView = new BizProductDetailView();
        bizProductDetailView.setNo(bizProductDetail.getNo());
        bizProductDetailView.setProductType(bizProductDetail.getProductType());
        bizProductDetailView.setPercentEBIT(bizProductDetail.getPercentEBIT());
        bizProductDetailView.setPercentSalesVolume(bizProductDetail.getPercentSalesVolume());

        bizProductDetailView.setCreateBy(bizProductDetail.getCreateBy());
        bizProductDetailView.setCreateDate(bizProductDetail.getCreateDate());
        bizProductDetailView.setModifyBy(bizProductDetail.getModifyBy());
        bizProductDetailView.setModifyDate(bizProductDetail.getModifyDate());


        bizProductDetailView.setProductDetail(bizProductDetail.getProductDetail());

        return bizProductDetailView;
    }

    public BizProductDetail transformToModel(BizProductDetailView bizProductDetailView, BizInfoDetail bizInfoDetail) {

        BizProductDetail bizProductDetail = new BizProductDetail();
        bizProductDetail.setNo(bizProductDetailView.getNo());
        bizProductDetail.setProductType(bizProductDetailView.getProductType());
        bizProductDetail.setPercentEBIT(bizProductDetailView.getPercentEBIT());
        bizProductDetail.setPercentSalesVolume(bizProductDetailView.getPercentSalesVolume());
        bizProductDetail.setProductDetail(bizProductDetailView.getProductDetail());

        bizProductDetail.setCreateBy(bizInfoDetail.getCreateBy());
        bizProductDetail.setCreateDate(bizInfoDetail.getCreateDate());
        bizProductDetail.setModifyBy(bizInfoDetail.getModifyBy());
        bizProductDetail.setModifyDate(DateTime.now().toDate());

        bizProductDetail.setBizInfoDetail(bizInfoDetail);


        return bizProductDetail;
    }

}
