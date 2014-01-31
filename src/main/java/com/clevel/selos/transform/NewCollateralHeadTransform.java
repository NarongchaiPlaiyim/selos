package com.clevel.selos.transform;
import com.clevel.selos.dao.working.NewCollateralHeadDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.NewCollateral;
import com.clevel.selos.model.db.working.NewCollateralHead;
import com.clevel.selos.model.view.NewCollateralHeadView;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class NewCollateralHeadTransform extends Transform {
    @Inject
    @SELOS
    Logger log;
    @Inject
    private NewCollateralSubTransform newCollateralSubTransform;
    @Inject
    private NewCollateralHeadDAO newCollateralHeadDAO;
    private List<NewCollateralHead> newCollateralHeadList;
    private List<NewCollateralHeadView> newCollateralHeadViewList;
    @Inject
    public NewCollateralHeadTransform() {

    }

    public List<NewCollateralHead> transformToModel(final List<NewCollateralHeadView> newCollateralHeadViewList, final User user){
        log.debug("-- transformToModel(Size of list is {})", ""+newCollateralHeadViewList.size());
        newCollateralHeadList = new ArrayList<NewCollateralHead>();
        NewCollateralHead model = null;
        long id = 0;
        for(NewCollateralHeadView view : newCollateralHeadViewList){
            id = view.getId();
            if(id != 0){
                model = newCollateralHeadDAO.findById(id);
                model.setModifyBy(user);
                model.setModifyDate(DateTime.now().toDate());
            } else {
                model = new NewCollateralHead();
                model.setCreateBy(user);
                model.setCreateDate(DateTime.now().toDate());
                model.setModifyBy(user);
                model.setModifyDate(DateTime.now().toDate());
                model.setNewCollateral(new NewCollateral());
            }
            model.setTitleDeed(view.getTitleDeed());
            model.setCollateralLocation(view.getCollateralLocation());
            model.setAppraisalValue(view.getAppraisalValue());
            model.setNewCollateralSubList(newCollateralSubTransform.transformToModel(view.getNewCollateralSubViewList(), user));
            newCollateralHeadList.add(model);
        }
        return newCollateralHeadList;
    }

    public List<NewCollateralHeadView> transformToView(final List<NewCollateralHead> newCollateralHeadList){
        log.debug("-- transform List<NewCollateralHead> to List<NewCollateralHeadView>(Size of list is {})", ""+newCollateralHeadList.size());
        newCollateralHeadViewList = new ArrayList<NewCollateralHeadView>();
        NewCollateralHeadView view = null;
        for(NewCollateralHead model : newCollateralHeadList){
            view = new NewCollateralHeadView();
            view.setId(model.getId());
            view.setTitleDeed(model.getTitleDeed());
            view.setNo(0);
            view.setCollateralLocation(model.getCollateralLocation());
            view.setAppraisalValue(model.getAppraisalValue());
            try {
                view.getHeadCollType().setDescription(model.getHeadCollType().getDescription());
            } catch (NullPointerException e) {
                view.getHeadCollType().setDescription("");
            }
            view.setNewCollateralSubViewList(newCollateralSubTransform.transformToView(model.getNewCollateralSubList()));
            newCollateralHeadViewList.add(view);
        }
        return newCollateralHeadViewList;
    }
}
