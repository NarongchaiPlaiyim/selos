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
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name="mandateFieldSubmitStep")
public class MandateFieldSubmitStep {
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
    private MandateFieldClassStepActionView wrkMandateFieldClassStepActionView = null;

    private long selectedStepActionViewId = -1;
    private long selectedFieldViewId = -1;
    private long selectedClassViewId = -1;
    private long selectedConditionId = -1;
    private long selectedMandateFieldClassStepActionId = -1;

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
        if(mandateFieldStepActionView.getClassStepActionViewList() == null){
            mandateFieldStepActionView.setClassStepActionViewList(new ArrayList<MandateFieldClassStepActionView>());
        }
        wrkMandateFieldClassStepActionView = new MandateFieldClassStepActionView();
        updatedMode = false;
        selectedMandateFieldClassStepActionId = -1;
    }

    public void onOpenUpdateStepActionField(){
        log.info("-- begin onOpenUpdateStepActionField: {}", selectedMandateFieldClassStepActionId);
        if(selectedMandateFieldClassStepActionId >= 0){
            if(mandateFieldStepActionView.getClassStepActionViewList() != null){
                wrkMandateFieldClassStepActionView = mandateFieldStepActionView.getClassStepActionViewList().get((int)selectedMandateFieldClassStepActionId);
                selectedClassViewId = wrkMandateFieldClassStepActionView.getId();
                updatedMode = true;
            }
        }

    }

    public void onDeleteStepActionField(){
        log.info("-- begin onDeleteStepActionField: {}", selectedMandateFieldClassStepActionId);
        if(selectedMandateFieldClassStepActionId >= 0){
            if(mandateFieldStepActionView.getClassStepActionViewList() != null){
                wrkMandateFieldClassStepActionView = mandateFieldStepActionView.getClassStepActionViewList().get((int)selectedMandateFieldClassStepActionId);
                mandateFieldControl.deleteMandateFieldClassStepActionView(wrkMandateFieldClassStepActionView, mandateFieldStepActionView.getStepView(), mandateFieldStepActionView.getActionView());
                mandateFieldStepActionView.getClassStepActionViewList().remove((int)selectedMandateFieldClassStepActionId);
            }
        }
    }

    public void onChangeClassView(){
        log.info("-- begin onChangeClassView {}", selectedClassViewId);
        if(selectedClassViewId > 0){
            for(MandateFieldClassView mandateFieldClassView : mandateFieldClassViewList){
                if(mandateFieldClassView.getId() == selectedClassViewId){
                    wrkMandateFieldClassStepActionView = new MandateFieldClassStepActionView();
                    wrkMandateFieldClassStepActionView.setId(mandateFieldClassView.getId());
                    wrkMandateFieldClassStepActionView.setActive(mandateFieldClassView.isActive());
                    wrkMandateFieldClassStepActionView.setClassName(mandateFieldClassView.getClassName());
                    wrkMandateFieldClassStepActionView.setPageName(mandateFieldClassView.getPageName());
                    break;
                }
            }
            _initFieldConditionDropdown();
        }
    }

    private void _initFieldConditionDropdown(){
        //Query Field list from MST and then filter the selected one out.
        mandateFieldViewList = mandateFieldControl.getMandateFieldDB(wrkMandateFieldClassStepActionView);
        if(wrkMandateFieldClassStepActionView.getMandateFieldViewList() != null){
            for(MandateFieldView selectedMandateFieldView : wrkMandateFieldClassStepActionView.getMandateFieldViewList()){
                for(MandateFieldView mstMandateFieldView : mandateFieldViewList){
                    if(selectedMandateFieldView.getId() == mstMandateFieldView.getId()){
                        mandateFieldViewList.remove(mstMandateFieldView);
                        break;
                    }
                }
            }
        }

        //Query Condition list from MST and then filter the selected one out.
        mandateFieldConditionViewList = mandateFieldControl.getMandateConditionList(wrkMandateFieldClassStepActionView);
        if(wrkMandateFieldClassStepActionView.getMandateFieldConditionViewList() != null){
            for(MandateFieldConditionView selectedConditionView : wrkMandateFieldClassStepActionView.getMandateFieldConditionViewList()){
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
        MandateFieldClassStepActionView _toUpd = new MandateFieldClassStepActionView();
        _toUpd.updateValues(wrkMandateFieldClassStepActionView);
        List<MandateFieldClassStepActionView> classStepActionViewList = mandateFieldStepActionView.getClassStepActionViewList();
        for(MandateFieldClassStepActionView classStepActionView : classStepActionViewList){
            if(classStepActionView.getId() == _toUpd.getId()){
                classStepActionViewList.remove(classStepActionView);
                _toUpd.setNeedUpdate(Boolean.TRUE);
                break;
            }
        }

        mandateFieldControl.deleteMandateFieldStepAction(mandateFieldViewList, mandateFieldStepActionView.getStepView(), mandateFieldStepActionView.getActionView());
        mandateFieldControl.deleteMandateFieldConStepAction(mandateFieldConditionViewList, mandateFieldStepActionView.getStepView(), mandateFieldStepActionView.getActionView());
        mandateFieldControl.saveMandateFieldClassStepAction(wrkMandateFieldClassStepActionView, mandateFieldStepActionView.getStepView(), mandateFieldStepActionView.getActionView());
        classStepActionViewList.add(_toUpd);
        mandateFieldStepActionView.setClassStepActionViewList(classStepActionViewList);

        updatedMode = false;
        selectedClassViewId = -1;
        selectedStepActionViewId = -1;
        selectedFieldViewId = -1;
        selectedConditionId = -1;
        selectedMandateFieldClassStepActionId = -1;
        selectedStepId = -1;
        selectedActionId = -1;
        wrkMandateFieldClassStepActionView = null;
        log.info("-- end onAddMandateCondition before update function complete");
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    public boolean isDisableClassChange(){
        if(updatedMode)
            return updatedMode;
        if(wrkMandateFieldClassStepActionView != null && wrkMandateFieldClassStepActionView.getMandateFieldViewList() != null && wrkMandateFieldClassStepActionView.getMandateFieldViewList().size() > 0){
            if(wrkMandateFieldClassStepActionView.getMandateFieldConditionViewList() != null && wrkMandateFieldClassStepActionView.getMandateFieldConditionViewList().size() > 0){
                return true;
            }
        }
        return false;
    }

    public void onSelectCondition(){
        log.info("-- begin onSelectCondition", selectedConditionId);
        if(selectedConditionId > 0){
            List<MandateFieldConditionView> selectedConditionViewList = wrkMandateFieldClassStepActionView.getMandateFieldConditionViewList();
            if(selectedConditionViewList == null)
                selectedConditionViewList = new ArrayList<MandateFieldConditionView>();

            for(MandateFieldConditionView mstConditionView : mandateFieldConditionViewList){
                if(selectedConditionId == mstConditionView.getId()){
                    selectedConditionViewList.add(mstConditionView);
                    mandateFieldConditionViewList.remove(mstConditionView);
                    break;
                }
            }
            wrkMandateFieldClassStepActionView.setMandateFieldConditionViewList(selectedConditionViewList);
        }
        selectedConditionId = -1;
    }

    public void onSelectFieldName(){
        log.info("-- onSelectFieldName {}", selectedFieldViewId);
        if(selectedFieldViewId > 0){
            List<MandateFieldView> selectedFieldViewList = wrkMandateFieldClassStepActionView.getMandateFieldViewList();
            if(selectedFieldViewList == null)
                selectedFieldViewList = new ArrayList<MandateFieldView>();

            for(MandateFieldView mandateFieldView : mandateFieldViewList){
                if(selectedFieldViewId == mandateFieldView.getId()){
                    selectedFieldViewList.add(mandateFieldView);
                    mandateFieldViewList.remove(mandateFieldView);
                    break;
                }
            }
            wrkMandateFieldClassStepActionView.setMandateFieldViewList(selectedFieldViewList);
        }
        selectedFieldViewId = -1;
    }

    public void onDeleteFieldName(){
        log.info("-- onDeleteFileName {}", selectedFieldViewId);
        if(selectedFieldViewId >= 0){
            List<MandateFieldView> selectedFieldViewList = wrkMandateFieldClassStepActionView.getMandateFieldViewList();
            MandateFieldView selectedMandateFieldView = selectedFieldViewList.get((int)selectedFieldViewId);
            selectedFieldViewList.remove(selectedMandateFieldView);
            mandateFieldViewList.add(selectedMandateFieldView);
            selectedFieldViewId = -1;
        }
    }

    public void onDeleteCondition(){
        log.info("-- onDeleteCondition {}", selectedConditionId);
        if(selectedConditionId >= 0){
            List<MandateFieldConditionView> selectedConditionViewList = wrkMandateFieldClassStepActionView.getMandateFieldConditionViewList();
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
        selectedMandateFieldClassStepActionId = -1;
        selectedStepId = -1;
        selectedActionId = -1;
        wrkMandateFieldClassStepActionView = null;
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

    public long getSelectedMandateFieldClassStepActionId() {
        return selectedMandateFieldClassStepActionId;
    }

    public void setSelectedMandateFieldClassStepActionId(long selectedMandateFieldClassStepActionId) {
        this.selectedMandateFieldClassStepActionId = selectedMandateFieldClassStepActionId;
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
        if(!updatedMode && mandateFieldStepActionView.getClassStepActionViewList() != null){
            List<MandateFieldClassView> _temp = new ArrayList<MandateFieldClassView>();
            for(MandateFieldClassView mandateFieldClassView : mandateFieldClassViewList){
                boolean skip = false;
                for(MandateFieldClassStepActionView mandateFieldClassStepActionView : mandateFieldStepActionView.getClassStepActionViewList()){
                    if(mandateFieldClassView.getId() == mandateFieldClassStepActionView.getId()){
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

    public MandateFieldClassStepActionView getWrkMandateFieldClassStepActionView() {
        return wrkMandateFieldClassStepActionView;
    }

    public void setWrkMandateFieldClassStepActionView(MandateFieldClassStepActionView wrkMandateFieldClassStepActionView) {
        this.wrkMandateFieldClassStepActionView = wrkMandateFieldClassStepActionView;
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
