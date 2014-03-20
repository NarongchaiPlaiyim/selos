package com.clevel.selos.transform;
import com.clevel.selos.dao.working.NewCollateralSubDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.SubCollateralType;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCollateralHead;
import com.clevel.selos.model.db.working.NewCollateralSub;
import com.clevel.selos.model.view.NewCollateralSubView;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NewCollateralSubTransform extends Transform {
    private List<NewCollateralSub> newCollateralSubList;
    private List<NewCollateralSubView> newCollateralSubViewList;

    @Inject
    public NewCollateralSubTransform() {
    }

    public List<NewCollateralSub> transformToModel(final List<NewCollateralSubView> newCollateralSubViewList, final User user){
        log.debug("------ transformToModel [NewCollateralSubViewList.size[{}]]", newCollateralSubViewList.size());
        newCollateralSubList = new ArrayList<NewCollateralSub>();
        NewCollateralSub model = null;
        for(NewCollateralSubView view : newCollateralSubViewList){
            model = new NewCollateralSub();
            log.debug("------ NewCollateralSub created");
            if(!Util.isZero(view.getId())){
                model.setId(view.getId());
                log.debug("------ NewCollateralSubView.id[{}]", view.getId());
            } else {
                model.setCreateDate(DateTime.now().toDate());
                model.setCreateBy(user);
            }
            model.setCollID(view.getCollID());
            model.setHeadCollID(view.getHeadCollID());
            model.setLineNo(view.getLineNo());
            if(!Util.isNull(view.getSubCollateralType()) && !Util.isZero(view.getSubCollateralType().getId())){
                model.setSubCollateralType(view.getSubCollateralType());
            } else {
                model.setSubCollateralType(null);
            }
            model.setUsage(view.getUsage());
            model.setTypeOfUsage(view.getTypeOfUsage());
            model.setAddress(view.getAddress());
            model.setLandOffice(view.getLandOffice());
            model.setTitleDeed(view.getTitleDeed());
            model.setCollateralOwner(view.getCollateralOwner());
            model.setCollateralOwnerAAD(view.getCollateralOwnerAAD());
            model.setAppraisalValue(view.getAppraisalValue());
            model.setMortgageValue(view.getMortgageValue());
            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());

            newCollateralSubList.add(model);
        }
        log.debug("------[RETURNED] NewCollateralSubList.size[{}]", newCollateralSubList.size());
        return newCollateralSubList;
    }

    public List<NewCollateralSub> transformToNewModel(final List<NewCollateralSubView> newCollateralSubViewList, final User user){
        log.debug("------ transformToNewModel [NewCollateralSubViewList.size[{}]]", newCollateralSubViewList.size());
        newCollateralSubList = new ArrayList<NewCollateralSub>();
        NewCollateralSub model = null;
        for(NewCollateralSubView view : newCollateralSubViewList){
            model = new NewCollateralSub();
            log.debug("------ NewCollateralSub created");
            model.setCreateBy(user);
            model.setCreateDate(DateTime.now().toDate());
            model.setCollID(view.getCollID());
            model.setHeadCollID(view.getHeadCollID());
            model.setLineNo(view.getLineNo());
            if(!Util.isNull(view.getSubCollateralType()) && !Util.isZero(view.getSubCollateralType().getId())){
                model.setSubCollateralType(view.getSubCollateralType());
                log.debug("-------- NewCollateralSub.SubCollateralType.id[{}]", model.getSubCollateralType().getId());
            } else {
                model.setSubCollateralType(null);
            }
            model.setUsage(view.getUsage());
            model.setTypeOfUsage(view.getTypeOfUsage());
            model.setAddress(view.getAddress());
            model.setLandOffice(view.getLandOffice());
            model.setTitleDeed(view.getTitleDeed());
            model.setCollateralOwner(view.getCollateralOwner());
            model.setCollateralOwnerAAD(view.getCollateralOwnerAAD());
            model.setAppraisalValue(view.getAppraisalValue());
            model.setMortgageValue(view.getMortgageValue());
            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());
            newCollateralSubList.add(model);
        }
        log.debug("------[RETURNED] NewCollateralSubList.size[{}]", newCollateralSubList.size());
        return newCollateralSubList;
    }

    public List<NewCollateralSubView> transformToView(final List<NewCollateralSub> newCollateralSubList){
        log.debug("------ transformToView [NewCollateralSubList.size[{}]]", newCollateralSubList.size());
        newCollateralSubViewList = new ArrayList<NewCollateralSubView>();
        NewCollateralSubView view = null;
        for(NewCollateralSub model : newCollateralSubList){
            view = new NewCollateralSubView();
            log.debug("------ NewCollateralSubView created");
            if(!Util.isZero(model.getId())){
                view.setId(model.getId());
                log.debug("------ NewCollateralSubView.id[{}]", view.getId());
            } else {
                view.setCreateDate(model.getCreateDate());
                view.setCreateBy(model.getCreateBy());
            }
            view.setCollID(model.getCollID());
            view.setHeadCollID(model.getHeadCollID());
            view.setLineNo(model.getLineNo());
            if(!Util.isNull(model.getSubCollateralType()) && !Util.isZero(model.getSubCollateralType().getId())){
                view.setSubCollateralType(model.getSubCollateralType());
                log.debug("-------- NewCollateralSubView.SubCollateralType.id[{}]", model.getSubCollateralType().getId());
            } else {
                view.setSubCollateralType(new SubCollateralType());
            }
            view.setUsage(model.getUsage());
            view.setTypeOfUsage(model.getTypeOfUsage());
            view.setAddress(model.getAddress());
            view.setLandOffice(model.getLandOffice());
            view.setTitleDeed(model.getTitleDeed());
            view.setCollateralOwner(model.getCollateralOwner());
            view.setCollateralOwnerAAD(model.getCollateralOwnerAAD());
            view.setAppraisalValue(model.getAppraisalValue());
            view.setMortgageValue(model.getMortgageValue());
            view.setModifyDate(model.getModifyDate());
            view.setModifyBy(model.getModifyBy());
            newCollateralSubViewList.add(view);
            /*view.getCollateralTypeType();
            view.getNewCollateralHead();
            view.getNewCollateralSubMortgageList();
            view.getNewCollateralSubOwnerList();
            view.getNewCollateralSubRelatedList(); */
        }
        log.debug("------[RETURNED] NewCollateralSubViewList.size[{}]", newCollateralSubViewList.size());
        return newCollateralSubViewList;
    }

    public NewCollateralSubView copyToNewView(NewCollateralSubView originalNewCollSubView, boolean isNewId) {
        NewCollateralSubView newCollateralSubView = new NewCollateralSubView();
        if (originalNewCollSubView != null) {
            newCollateralSubView.setId(isNewId ? 0 : originalNewCollSubView.getId());
            newCollateralSubView.setNo(originalNewCollSubView.getNo());
            newCollateralSubView.setCollID(originalNewCollSubView.getCollID());
            newCollateralSubView.setHeadCollID(originalNewCollSubView.getHeadCollID());
            newCollateralSubView.setSubCollateralType(originalNewCollSubView.getSubCollateralType());
            newCollateralSubView.setAddress(originalNewCollSubView.getAddress());
            newCollateralSubView.setLandOffice(originalNewCollSubView.getLandOffice());
            newCollateralSubView.setTitleDeed(originalNewCollSubView.getTitleDeed());
            newCollateralSubView.setCollateralOwner(originalNewCollSubView.getCollateralOwner());
            newCollateralSubView.setCollateralOwnerAAD(originalNewCollSubView.getCollateralOwnerAAD());
            newCollateralSubView.setCollateralOwnerUW(originalNewCollSubView.getCollateralOwnerUW());
            newCollateralSubView.setCollateralOwnerUWList(originalNewCollSubView.getCollateralOwnerUWList());
            newCollateralSubView.setMortgageType(originalNewCollSubView.getMortgageType());
            newCollateralSubView.setMortgageList(originalNewCollSubView.getMortgageList());
            newCollateralSubView.setRelatedWithId(originalNewCollSubView.getRelatedWithId());
            newCollateralSubView.setRelatedWithList(originalNewCollSubView.getRelatedWithList());
            newCollateralSubView.setAppraisalValue(originalNewCollSubView.getAppraisalValue());
            newCollateralSubView.setMortgageValue(originalNewCollSubView.getMortgageValue());
            newCollateralSubView.setCreateDate(originalNewCollSubView.getCreateDate());
            newCollateralSubView.setCreateBy(originalNewCollSubView.getCreateBy());
            newCollateralSubView.setModifyDate(originalNewCollSubView.getModifyDate());
            newCollateralSubView.setModifyBy(originalNewCollSubView.getModifyBy());
            newCollateralSubView.setUsage(originalNewCollSubView.getUsage());
            newCollateralSubView.setTypeOfUsage(originalNewCollSubView.getTypeOfUsage());
            newCollateralSubView.setLineNo(originalNewCollSubView.getLineNo());
        }
        return newCollateralSubView;
    }

    public List<NewCollateralSubView> copyToNewViews(List<NewCollateralSubView> originalNewCollSubViews, boolean isNewId) {
        List<NewCollateralSubView> newCollateralSubViews = new ArrayList<NewCollateralSubView>();
        if (originalNewCollSubViews != null && originalNewCollSubViews.size() > 0) {
            for (NewCollateralSubView originalNewCollSubView : originalNewCollSubViews) {
                newCollateralSubViews.add(copyToNewView(originalNewCollSubView, isNewId));
            }
        }
        return newCollateralSubViews;
    }

}
