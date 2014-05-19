package com.clevel.selos.controller.admin;

import com.clevel.selos.businesscontrol.StepStatusControl;
import com.clevel.selos.businesscontrol.admin.MandateFieldControl;
import com.clevel.selos.integration.ADMIN;
import com.clevel.selos.model.view.*;
import com.clevel.selos.util.FacesUtil;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name="mandateFieldSubmitStep")
public class MandateFieldSubmitStep implements Serializable {
    @Inject
    @ADMIN
    private Logger log;

    @Inject
    private MandateFieldControl mandateFieldControl;

    @Inject
    private StepStatusControl stepStatusControl;

    private boolean preRenderCheck = false;

    private List<SelectItem> stepItemList = null;
    private List<SelectItem> actionItemList = null;
    private SelectItem selectedStepItem = null;
    private SelectItem selectedActionItem = null;

    private MandateFieldStepActionView mandateFieldStepActionView = null;
    private List<MandateFieldClassView> mandateFieldClassViewList = null;
    private List<MandateFieldView> mandateFieldViewList = null;
    private List<MandateFieldConditionView> mandateFieldConditionViewList = null;
    private MandateFieldClassSAAdminView wrkMandateFieldClassSAAdminView = null;

    private long selectedStepActionViewId = -1;
    private long selectedFieldViewId = -1;
    private long selectedClassViewId = -1;
    private long selectedConditionId = -1;
    private long selectedMandateFieldSAAdminId = -1;

    private long selectedStepId = -1;
    private long selectedActionId = -1;
    private boolean updatedMode = false;

    @Inject
    public MandateFieldSubmitStep(){}

    @PostConstruct
    private void init() {
        log.info("Construct");
        HttpSession session = FacesUtil.getSession(false);
        if (session != null) {
            _loadStepActionView();
        }
    }

    public void preRender(){
        if(preRenderCheck)
            return;
        preRenderCheck = true;
    }


    private void _loadStepActionView(){
        actionItemList = stepStatusControl.getActionSelectItemList();
        stepItemList = stepStatusControl.getStepSelectItemList();
        if(selectedStepItem != null && selectedActionItem != null){
            mandateFieldStepActionView = mandateFieldControl.getMandateFieldStepAction(selectedStepItem, selectedActionItem);
        }
    }

    public void onSearchStepAction(){
        log.info("-- begin onSearchStepAction step: {}, action:{}", selectedStepId, selectedActionId);
        if(selectedStepId == -1 || selectedActionId == -1){
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
            return;
        }

        for(SelectItem selectItem : stepItemList){
            if((Long)selectItem.getValue() == selectedStepId){
                selectedStepItem = selectItem;
                break;
            }
        }

        for(SelectItem selectItem : actionItemList){
            if((Long)selectItem.getValue() == selectedActionId){
                selectedActionItem = selectItem;
                break;
            }
        }
        log.info("step: {}, action: {}", selectedStepItem, selectedActionItem);

        mandateFieldStepActionView = mandateFieldControl.getMandateFieldStepAction(selectedStepItem, selectedActionItem);
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
        log.info("-- end onSearchStepAction");
    }

    public void onOpenAddStepActionField(){
        log.info("-- begin onOpenAddStepActionField mandateFieldStepActionView:{}", mandateFieldStepActionView);
        if(mandateFieldStepActionView.getClassSAAdminViewList() == null){
            mandateFieldStepActionView.setClassSAAdminViewList(new ArrayList<MandateFieldClassSAAdminView>());
        }
        wrkMandateFieldClassSAAdminView = new MandateFieldClassSAAdminView();
        updatedMode = false;
        selectedMandateFieldSAAdminId = -1;
    }

    public void onOpenUpdateStepActionField(){
        log.info("-- begin onOpenUpdateStepActionField: {}", selectedMandateFieldSAAdminId);
        if(selectedMandateFieldSAAdminId >= 0){
            if(mandateFieldStepActionView.getClassSAAdminViewList() != null){
                wrkMandateFieldClassSAAdminView = mandateFieldStepActionView.getClassSAAdminViewList().get((int)selectedMandateFieldSAAdminId);
                selectedClassViewId = wrkMandateFieldClassSAAdminView.getId();
                updatedMode = true;
            }
        }

    }

    public void onDeleteStepActionField(){
        log.info("-- begin onDeleteStepActionField: {}", selectedMandateFieldSAAdminId);
        if(selectedMandateFieldSAAdminId >= 0){
            if(mandateFieldStepActionView.getClassSAAdminViewList() != null){
                wrkMandateFieldClassSAAdminView = mandateFieldStepActionView.getClassSAAdminViewList().get((int)selectedMandateFieldSAAdminId);
                mandateFieldControl.deleteMandateFieldClassSAAdmin(wrkMandateFieldClassSAAdminView, mandateFieldStepActionView.getStepView(), mandateFieldStepActionView.getActionView());
                mandateFieldStepActionView.getClassSAAdminViewList().remove((int)selectedMandateFieldSAAdminId);
            }
        }
    }

    public void onChangeClassView(){
        log.info("-- begin onChangeClassView {}", selectedClassViewId);
        if(selectedClassViewId > 0){
            for(MandateFieldClassView mandateFieldClassView : mandateFieldClassViewList){
                if(mandateFieldClassView.getId() == selectedClassViewId){
                    wrkMandateFieldClassSAAdminView = new MandateFieldClassSAAdminView();
                    wrkMandateFieldClassSAAdminView.setId(mandateFieldClassView.getId());
                    wrkMandateFieldClassSAAdminView.setActive(mandateFieldClassView.isActive());
                    wrkMandateFieldClassSAAdminView.setClassName(mandateFieldClassView.getClassName());
                    wrkMandateFieldClassSAAdminView.setPageName(mandateFieldClassView.getPageName());
                    break;
                }
            }
            _initFieldConditionDropdown();
        }
    }

    private void _initFieldConditionDropdown(){
        //Query Field list from MST and then filter the selected one out.
        mandateFieldViewList = mandateFieldControl.getMandateFieldDB(wrkMandateFieldClassSAAdminView);
        if(wrkMandateFieldClassSAAdminView.getMandateFieldViewList() != null){
            for(MandateFieldView selectedMandateFieldView : wrkMandateFieldClassSAAdminView.getMandateFieldViewList()){
                for(MandateFieldView mstMandateFieldView : mandateFieldViewList){
                    if(selectedMandateFieldView.getId() == mstMandateFieldView.getId()){
                        mandateFieldViewList.remove(mstMandateFieldView);
                        break;
                    }
                }
            }
        }

        //Query Condition list from MST and then filter the selected one out.
        mandateFieldConditionViewList = mandateFieldControl.getMandateConditionList(wrkMandateFieldClassSAAdminView);
        if(wrkMandateFieldClassSAAdminView.getMandateFieldConditionViewList() != null){
            for(MandateFieldConditionView selectedConditionView : wrkMandateFieldClassSAAdminView.getMandateFieldConditionViewList()){
                for(MandateFieldConditionView mstConditionView : mandateFieldConditionViewList){
                    if(selectedConditionView.getId() == mstConditionView.getId()){
                        mandateFieldConditionViewList.remove(mstConditionView);
                    }
                }
            }
        }
    }

    public void onChangeFieldView(){
        log.info("-- begin onChangeClassView {}", selectedFieldViewId);
    }

    public void onChangeCondition(){
        log.info("-- begin onChangeClassView {}", selectedConditionId);
    }

    public void onAddStepActionField(){
        log.info("-- begin onAddStepActionField");
        MandateFieldClassSAAdminView _toUpd = new MandateFieldClassSAAdminView();
        _toUpd.updateValues(wrkMandateFieldClassSAAdminView);
        List<MandateFieldClassSAAdminView> classStepActionViewList = mandateFieldStepActionView.getClassSAAdminViewList();
        for(MandateFieldClassSAAdminView classSAAdminView : classStepActionViewList){
            if(classSAAdminView.getId() == _toUpd.getId()){
                classStepActionViewList.remove(classSAAdminView);
                _toUpd.setNeedUpdate(Boolean.TRUE);
                break;
            }
        }

        mandateFieldControl.deleteMandateFieldStepAction(mandateFieldViewList, mandateFieldStepActionView.getStepView(), mandateFieldStepActionView.getActionView());
        mandateFieldControl.deleteMandateFieldConStepAction(mandateFieldConditionViewList, mandateFieldStepActionView.getStepView(), mandateFieldStepActionView.getActionView());
        mandateFieldControl.saveMandateFieldClassSAAdmin(wrkMandateFieldClassSAAdminView, mandateFieldStepActionView.getStepView(), mandateFieldStepActionView.getActionView());
        classStepActionViewList.add(_toUpd);
        mandateFieldStepActionView.setClassSAAdminViewList(classStepActionViewList);

        updatedMode = false;
        selectedClassViewId = -1;
        selectedStepActionViewId = -1;
        selectedFieldViewId = -1;
        selectedConditionId = -1;
        selectedMandateFieldSAAdminId = -1;
        selectedStepId = -1;
        selectedActionId = -1;
        wrkMandateFieldClassSAAdminView = null;
        log.info("-- end onAddMandateCondition before update function complete");
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public boolean isDisableClassChange(){
        if(updatedMode)
            return updatedMode;
        if(wrkMandateFieldClassSAAdminView != null && wrkMandateFieldClassSAAdminView.getMandateFieldViewList() != null && wrkMandateFieldClassSAAdminView.getMandateFieldViewList().size() > 0){
            if(wrkMandateFieldClassSAAdminView.getMandateFieldConditionViewList() != null && wrkMandateFieldClassSAAdminView.getMandateFieldConditionViewList().size() > 0){
                return true;
            }
        }
        return false;
    }

    public void onSelectCondition(){
        log.info("-- begin onSelectCondition", selectedConditionId);
        if(selectedConditionId > 0){
            List<MandateFieldConditionView> selectedConditionViewList = wrkMandateFieldClassSAAdminView.getMandateFieldConditionViewList();
            if(selectedConditionViewList == null)
                selectedConditionViewList = new ArrayList<MandateFieldConditionView>();

            for(MandateFieldConditionView mstConditionView : mandateFieldConditionViewList){
                if(selectedConditionId == mstConditionView.getId()){
                    selectedConditionViewList.add(mstConditionView);
                    mandateFieldConditionViewList.remove(mstConditionView);
                    break;
                }
            }
            wrkMandateFieldClassSAAdminView.setMandateFieldConditionViewList(selectedConditionViewList);
        }
        selectedConditionId = -1;
    }

    public void onSelectFieldName(){
        log.info("-- onSelectFieldName {}", selectedFieldViewId);
        if(selectedFieldViewId > 0){
            List<MandateFieldView> selectedFieldViewList = wrkMandateFieldClassSAAdminView.getMandateFieldViewList();
            if(selectedFieldViewList == null)
                selectedFieldViewList = new ArrayList<MandateFieldView>();

            for(MandateFieldView mandateFieldView : mandateFieldViewList){
                if(selectedFieldViewId == mandateFieldView.getId()){
                    selectedFieldViewList.add(mandateFieldView);
                    mandateFieldViewList.remove(mandateFieldView);
                    break;
                }
            }
            wrkMandateFieldClassSAAdminView.setMandateFieldViewList(selectedFieldViewList);
        }
        selectedFieldViewId = -1;
    }

    public void onDeleteFieldName(){
        log.info("-- onDeleteFileName {}", selectedFieldViewId);
        if(selectedFieldViewId >= 0){
            List<MandateFieldView> selectedFieldViewList = wrkMandateFieldClassSAAdminView.getMandateFieldViewList();
            MandateFieldView selectedMandateFieldView = selectedFieldViewList.get((int)selectedFieldViewId);
            selectedFieldViewList.remove(selectedMandateFieldView);
            mandateFieldViewList.add(selectedMandateFieldView);
            selectedFieldViewId = -1;
        }
    }

    public void onDeleteCondition(){
        log.info("-- onDeleteCondition {}", selectedConditionId);
        if(selectedConditionId >= 0){
            List<MandateFieldConditionView> selectedConditionViewList = wrkMandateFieldClassSAAdminView.getMandateFieldConditionViewList();
            MandateFieldConditionView selectedMandateFieldView = selectedConditionViewList.get((int)selectedConditionId);
            selectedConditionViewList.remove(selectedMandateFieldView);
            mandateFieldConditionViewList.add(selectedMandateFieldView);
            selectedConditionId = -1;
        }
    }

    public void onReset(){
        selectedActionItem = null;
        selectedStepItem = null;
        mandateFieldStepActionView = null;
        updatedMode = false;
        selectedClassViewId = -1;
        selectedStepActionViewId = -1;
        selectedFieldViewId = -1;
        selectedConditionId = -1;
        selectedMandateFieldSAAdminId = -1;
        selectedStepId = -1;
        selectedActionId = -1;
        wrkMandateFieldClassSAAdminView = null;
    }

    public MandateFieldStepActionView getMandateFieldStepActionView() {
        return mandateFieldStepActionView;
    }

    public void setMandateFieldStepActionView(MandateFieldStepActionView mandateFieldStepActionView) {
        this.mandateFieldStepActionView = mandateFieldStepActionView;
    }

    public List<SelectItem> getStepItemList() {
        return stepItemList;
    }

    public void setStepItemList(List<SelectItem> stepItemList) {
        this.stepItemList = stepItemList;
    }

    public List<SelectItem> getActionItemList() {
        return actionItemList;
    }

    public void setActionItemList(List<SelectItem> actionItemList) {
        this.actionItemList = actionItemList;
    }

    public SelectItem getSelectedStepItem() {
        return selectedStepItem;
    }

    public void setSelectedStepItem(SelectItem selectedStepItem) {
        this.selectedStepItem = selectedStepItem;
    }

    public SelectItem getSelectedActionItem() {
        return selectedActionItem;
    }

    public void setSelectedActionItem(SelectItem selectedActionItem) {
        this.selectedActionItem = selectedActionItem;
    }

    public long getSelectedStepActionViewId() {
        return selectedStepActionViewId;
    }

    public void setSelectedStepActionViewId(long selectedStepActionViewId) {
        this.selectedStepActionViewId = selectedStepActionViewId;
    }

    public long getSelectedFieldViewId() {
        return selectedFieldViewId;
    }

    public void setSelectedFieldViewId(long selectedFieldViewId) {
        this.selectedFieldViewId = selectedFieldViewId;
    }

    public long getSelectedClassViewId() {
        return selectedClassViewId;
    }

    public void setSelectedClassViewId(long selectedClassViewId) {
        this.selectedClassViewId = selectedClassViewId;
    }

    public void setSelectedConditionId(long selectedConditionId) {
        this.selectedConditionId = selectedConditionId;
    }

    public long getSelectedMandateFieldSAAdminId() {
        return selectedMandateFieldSAAdminId;
    }

    public void setSelectedMandateFieldSAAdminId(long selectedMandateFieldSAAdminId) {
        this.selectedMandateFieldSAAdminId = selectedMandateFieldSAAdminId;
    }

    public long getSelectedStepId() {
        return selectedStepId;
    }

    public void setSelectedStepId(long selectedStepId) {
        this.selectedStepId = selectedStepId;
    }

    public long getSelectedActionId() {
        return selectedActionId;
    }

    public void setSelectedActionId(long selectedActionId) {
        this.selectedActionId = selectedActionId;
    }

    public void setSelectedActionId(int selectedActionId) {
        this.selectedActionId = selectedActionId;
    }

    public List<MandateFieldClassView> getMandateFieldClassViewList() {
        if(mandateFieldClassViewList == null){
            mandateFieldClassViewList = mandateFieldControl.getMandateFieldClass();
        }
        if(!updatedMode && mandateFieldStepActionView.getClassSAAdminViewList() != null){
            List<MandateFieldClassView> _temp = new ArrayList<MandateFieldClassView>();
            for(MandateFieldClassView mandateFieldClassView : mandateFieldClassViewList){
                boolean skip = false;
                for(MandateFieldClassSAAdminView mandateFieldClassSAAdminView : mandateFieldStepActionView.getClassSAAdminViewList()){
                    if(mandateFieldClassView.getId() == mandateFieldClassSAAdminView.getId()){
                        skip = true;
                        break;
                    }
                }
                if(!skip)
                    _temp.add(mandateFieldClassView);
            }
            return _temp;
        }
        return mandateFieldClassViewList;
    }

    public void setMandateFieldClassViewList(List<MandateFieldClassView> mandateFieldClassViewList) {
        this.mandateFieldClassViewList = mandateFieldClassViewList;
    }

    public MandateFieldClassSAAdminView getWrkMandateFieldClassSAAdminView() {
        return wrkMandateFieldClassSAAdminView;
    }

    public void setWrkMandateFieldClassSAAdminView(MandateFieldClassSAAdminView wrkMandateFieldClassSAAdminView) {
        this.wrkMandateFieldClassSAAdminView = wrkMandateFieldClassSAAdminView;
    }

    public List<MandateFieldView> getMandateFieldViewList() {
        return mandateFieldViewList;
    }

    public void setMandateFieldViewList(List<MandateFieldView> mandateFieldViewList) {
        this.mandateFieldViewList = mandateFieldViewList;
    }

    public List<MandateFieldConditionView> getMandateFieldConditionViewList() {
        return mandateFieldConditionViewList;
    }

    public void setMandateFieldConditionViewList(List<MandateFieldConditionView> mandateFieldConditionViewList) {
        this.mandateFieldConditionViewList = mandateFieldConditionViewList;
    }


    public long getSelectedConditionId() {
        return selectedConditionId;
    }
}
