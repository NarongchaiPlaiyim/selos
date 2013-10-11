package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.TCG;
import com.clevel.selos.model.db.working.TCGDetail;
import com.clevel.selos.model.view.TCGDetailView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 1/10/2556
 * Time: 14:27 น.
 * To change this template use File | Settings | File Templates.
 */
public class TCGDetailTransform extends Transform {

    public List<TCGDetail> transformTCGDetailViewToModel(List<TCGDetailView> tcgDetailViewList , TCG tcg) {

        List<TCGDetail> tcgDetailList  = new ArrayList<TCGDetail>();

        for(TCGDetailView tcgDetailView : tcgDetailViewList){
            TCGDetail tcgDetail = new TCGDetail();
            tcgDetail.setId(tcgDetailView.getId());
            tcgDetail.setTcg(tcg);
            tcgDetail.setLtvValue(tcgDetailView.getLtvValue());
            tcgDetail.setAppraisalAmount(tcgDetailView.getAppraisalAmount());
            tcgDetail.setPotentialCollateral(tcgDetailView.getPotentialCollateral());
            tcgDetail.setProposeInThisRequest(tcgDetailView.isProposeInThisRequest());
            tcgDetail.setTcgCollateralType(tcgDetailView.getTcgCollateralType());

            tcgDetailList.add(tcgDetail);
        }

        return tcgDetailList;
    }

    public List<TCGDetailView> transformTCGDetailModelToView(List<TCGDetail> tcgDetailList) {

        List<TCGDetailView> tcgDetailViewList  = new ArrayList<TCGDetailView>();

        for(TCGDetail tcgDetail : tcgDetailList){
            TCGDetailView tcgDetailView = new TCGDetailView();
            tcgDetailView.setId(tcgDetail.getId());
            tcgDetailView.setLtvValue(tcgDetail.getLtvValue());
            tcgDetailView.setAppraisalAmount(tcgDetail.getAppraisalAmount());
            tcgDetailView.setPotentialCollateral(tcgDetail.getPotentialCollateral());
            tcgDetailView.setProposeInThisRequest(tcgDetail.isProposeInThisRequest());
            tcgDetailView.setTcgCollateralType(tcgDetail.getTcgCollateralType());

            tcgDetailViewList.add(tcgDetailView);
        }

        return tcgDetailViewList;
    }
}
