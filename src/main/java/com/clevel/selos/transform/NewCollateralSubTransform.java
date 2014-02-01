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
    @Inject
    @SELOS
    Logger log;
    @Inject
    private NewCollateralSubDAO newCollateralSubDAO;
    private List<NewCollateralSub> newCollateralSubList;
    private List<NewCollateralSubView> newCollateralSubViewList;
    @Inject
    public NewCollateralSubTransform() {

    }

    public List<NewCollateralSub> transformToModel(final List<NewCollateralSubView> newCollateralSubViewList, final User user){
        log.debug("-- transform List<NewCollateralSubView> to List<NewCollateralSub>(Size of list is {})", ""+newCollateralSubViewList.size());
        newCollateralSubList = new ArrayList<NewCollateralSub>();
        NewCollateralSub model = null;
        long id = 0;
        for(NewCollateralSubView view : newCollateralSubViewList){
            id = view.getId();
            if(id != 0){
                model = newCollateralSubDAO.findById(id);
            } else {
                model = new NewCollateralSub();
                log.debug("-- NewCollateralSub created");
                model.setCreateBy(user);
                model.setCreateDate(DateTime.now().toDate());
            }

            if(checkNullObject(model.getSubCollateralType()) && checkId0(model.getSubCollateralType().getId())){
                try{
                    model.getSubCollateralType().setDescription(view.getSubCollateralType().getDescription());
                } catch (NullPointerException e){
                    model.getSubCollateralType().setDescription("");
                }
            } else {
                model.setSubCollateralType(null);
            }

            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());
            model.setAddress(view.getAddress());
            model.setLandOffice(view.getLandOffice());
            model.setCollateralOwner(view.getCollateralOwner());
            model.setAppraisalValue(view.getAppraisalValue());
            newCollateralSubList.add(model);
        }
        return newCollateralSubList;
    }

    public List<NewCollateralSub> transformToNewModel(final List<NewCollateralSubView> newCollateralSubViewList, final User user){
        log.debug("-- transform List<NewCollateralSubView> to new List<NewCollateralSub>(Size of list is {})", ""+newCollateralSubViewList.size());
        newCollateralSubList = new ArrayList<NewCollateralSub>();
        NewCollateralSub model = null;
        for(NewCollateralSubView view : newCollateralSubViewList){
            model = new NewCollateralSub();
            log.debug("-- NewCollateralSub created");
            model.setCreateBy(user);
            model.setCreateDate(DateTime.now().toDate());
            if(checkNullObject(model.getSubCollateralType()) && checkId0(model.getSubCollateralType().getId())){
                try{
                    model.getSubCollateralType().setDescription(view.getSubCollateralType().getDescription());
                } catch (NullPointerException e){
                    model.getSubCollateralType().setDescription("");
                }
            } else {
                model.setSubCollateralType(null);
            }
            model.setModifyBy(user);
            model.setModifyDate(DateTime.now().toDate());
            model.setAddress(view.getAddress());
            model.setLandOffice(view.getLandOffice());
            model.setCollateralOwner(view.getCollateralOwner());
            model.setAppraisalValue(view.getAppraisalValue());
            newCollateralSubList.add(model);
        }
        return newCollateralSubList;
    }

    public List<NewCollateralSubView> transformToView(final List<NewCollateralSub> newCollateralSubList){
        log.debug("-- transform List<NewCollateralSub> to List<NewCollateralSubView>(Size of list is {})", ""+newCollateralSubList.size());
        newCollateralSubViewList = new ArrayList<NewCollateralSubView>();
        NewCollateralSubView view = null;
        for(NewCollateralSub model : newCollateralSubList){
            view = new NewCollateralSubView();
            view.setId(model.getId());
            view.setNo(0);//todo : No. ?

            if(checkNullObject(view.getSubCollateralType()) && checkId0(view.getSubCollateralType().getId())){
                try{
                    view.getSubCollateralType().setDescription(model.getSubCollateralType().getDescription());
                } catch (NullPointerException e){
                    view.getSubCollateralType().setDescription("");
                }
            } else {
                view.setSubCollateralType(new SubCollateralType());
            }
            view.setCreateBy(model.getCreateBy());
            view.setCreateDate(model.getCreateDate());
            view.setAddress(model.getAddress());
            view.setLandOffice(model.getLandOffice());
            view.setCollateralOwner(model.getCollateralOwner());
            view.setAppraisalValue(model.getAppraisalValue());
            newCollateralSubViewList.add(view);
        }
        return newCollateralSubViewList;
    }

    private<T> boolean checkNullObject(T object){
        return !Util.isNull(object);
    }

    private boolean checkId0(int id){
        return !Util.isZero(id);
    }
}
