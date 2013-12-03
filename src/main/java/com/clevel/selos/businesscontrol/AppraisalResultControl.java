package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.NewCollateralHeadDetailView;
import com.clevel.selos.model.view.CollateralDetailView;
import com.clevel.selos.model.view.AppraisalView;
import com.clevel.selos.model.view.NewSubCollateralDetailView;
import com.clevel.selos.transform.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class AppraisalResultControl extends BusinessControl {
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    AppraisalDAO appraisalDAO;
    @Inject
    CollateralDetailDAO collateralDetailDAO ;
    @Inject
    CollateralHeaderDetailDAO collateralHeaderDetailDAO;
    @Inject
    SubCollateralDetailDAO subCollateralDetailDAO;

    @Inject
    AppraisalTransform appraisalTransform;
    @Inject
    CollateralDetailTransform collateralDetailTransform;
    @Inject
    NewCollHeadDetailTransform newCollHeadDetailTransform;
    @Inject
    NewSubCollDetailTransform newSubCollDetailTransform;


    public AppraisalView getAppraisalResultByWorkCase(long workCaseId){
        log.info("getAppraisalByWorkCase ");

        Appraisal appraisal;
        AppraisalView appraisalView;
        List<CollateralDetail> collateralDetailList;
        List<CollateralDetailView> collateralDetailViewList;
        List<CollateralHeaderDetail> collateralHeaderDetailList;
        List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList;
        List<SubCollateralDetail> subCollateralDetailList;
        List<NewSubCollateralDetailView> newSubCollateralDetailViewList;
        CollateralDetail collateralDetail;
        CollateralHeaderDetail collateralHeaderDetail;

        WorkCase workCase = workCaseDAO.findById(workCaseId);
        log.info("workCase after findById " + workCase );

        appraisal  = appraisalDAO.onSearchByWorkCase(workCase);


        if( appraisal != null){
            log.info("appraisal != null ");
            appraisalView = appraisalTransform.transformToView(appraisal);

            collateralDetailList = collateralDetailDAO.findByAppraisal(appraisal);
            log.info("collateralDetailList.size() is " + collateralDetailList.size());
            if(collateralDetailList.size()>0){
                collateralDetailViewList = collateralDetailTransform.transformToView(collateralDetailList);

                for(int i =0;i<collateralDetailList.size();i++){
                    collateralDetail = collateralDetailList.get(i);
                    log.info("collateralDetail.size()i is " + i +" getId is " + collateralDetail.getId()  );
                    collateralHeaderDetailList = collateralHeaderDetailDAO.findByCollateralDetail(collateralDetail);
                    newCollateralHeadDetailViewList = newCollHeadDetailTransform.transformToView(collateralHeaderDetailList);
                    log.info("collateralDetail.size() i is " + i + "  collateralHeaderDetailList size is "+ collateralHeaderDetailList.size());

                    if(collateralHeaderDetailList.size()>0){
                        for(int j =0;j<collateralHeaderDetailList.size();j++){
                            collateralHeaderDetail = collateralHeaderDetailList.get(j);
                            log.info("collateralHeaderDetailList.size()i is " + j +" getId is " +collateralHeaderDetail.getId()  );
                            subCollateralDetailList = subCollateralDetailDAO.findByCollateralHeaderDetail(collateralHeaderDetail);
                            newSubCollateralDetailViewList = newSubCollDetailTransform.transformToView(subCollateralDetailList);
                            log.info("collateralHeaderDetailList.size() j is " + j + "  newSubCollateralDetailViewList size is "+ newSubCollateralDetailViewList.size());
                            newCollateralHeadDetailViewList.get(j).setNewSubCollateralDetailViewList(newSubCollateralDetailViewList);

                        }
                    }
                    collateralDetailViewList.get(i).setNewCollateralHeadDetailViewList(newCollateralHeadDetailViewList);
                }
                appraisalView.setCollateralDetailViewList(collateralDetailViewList);
            }

        }else{
            appraisalView = null;
        }
        return appraisalView;
    }

    public void onSaveAppraisalResult(AppraisalView appraisalView,long workCaseId){
        log.info("onSaveAppraisalResult begin ");
        Appraisal appraisal;
        List<CollateralDetailView> collateralDetailViewList;
        List<CollateralDetail> collateralDetailList;
        List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList;
        List<CollateralHeaderDetail> collateralHeaderDetailList;
        List<SubCollateralDetail> subCollateralDetailList;
        List<NewSubCollateralDetailView> newSubCollateralDetailViewList;

        WorkCase workCase = workCaseDAO.findById(workCaseId);
        appraisal = appraisalTransform.transformToModel(appraisalView);
        appraisal.setWorkCase(workCase);

        appraisalDAO.persist(appraisal);
        log.info( "appraisalDAO persist end" );

        collateralDetailViewList = appraisalView.getCollateralDetailViewList();

        if(collateralDetailViewList.size()>0){
            List<CollateralDetail>   collateralDetailListDel = collateralDetailDAO.findByAppraisal(appraisal);
            for(int i=0;i<collateralDetailListDel.size();i++){
                if(collateralDetailListDel.size()>0){
                    List<CollateralHeaderDetail>   collateralHeaderDetailListDel = collateralHeaderDetailDAO.findByCollateralDetail(collateralDetailListDel.get(i));
                    if(collateralHeaderDetailListDel.size()>0){
                        for(int j=0;j<collateralHeaderDetailListDel.size();j++){
                            List<SubCollateralDetail> subCollateralDetailListDel = subCollateralDetailDAO.findByCollateralHeaderDetail(collateralHeaderDetailListDel.get(j));
                            subCollateralDetailDAO.delete(subCollateralDetailListDel);
                        }
                    }
                    collateralHeaderDetailDAO.delete(collateralHeaderDetailListDel);
                }
            }
            collateralDetailDAO.delete(collateralDetailListDel);
        }

        collateralDetailList = collateralDetailTransform.transformToModel(collateralDetailViewList, appraisal);
        collateralDetailDAO.persist(collateralDetailList);
        log.info( "collateralDetailDAO persist end" );
        for(int i=0;i<collateralDetailViewList.size();i++){
            newCollateralHeadDetailViewList =  collateralDetailViewList.get(i).getNewCollateralHeadDetailViewList();
            collateralHeaderDetailList = newCollHeadDetailTransform.transformToModel(newCollateralHeadDetailViewList,collateralDetailList.get(i));
            collateralHeaderDetailDAO.persist(collateralHeaderDetailList);
            log.info( "collateralHeaderDetailDAO persist end i is " + i  );
            for(int j=0;j< newCollateralHeadDetailViewList.size();j++){
                newSubCollateralDetailViewList =  newCollateralHeadDetailViewList.get(j).getNewSubCollateralDetailViewList();
                subCollateralDetailList = newSubCollDetailTransform.transformToModel(newSubCollateralDetailViewList,collateralHeaderDetailList.get(j));
                log.info( "subCollateralDetailList size before persist end i is " + i +" and j is " + j  + " size is " + subCollateralDetailList.size());
                subCollateralDetailDAO.persist(subCollateralDetailList);
                log.info( "subCollateralDetailDAO persist end i is " + i +" and j is " + j  );
            }
        }
        log.info("onSaveAppraisalResult end");
    }
}
