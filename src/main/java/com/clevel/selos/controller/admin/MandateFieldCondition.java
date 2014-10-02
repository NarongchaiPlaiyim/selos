package com.clevel.selos.controller.admin;

import com.clevel.selos.businesscontrol.master.MandateFieldControl;
import com.clevel.selos.integration.ADMIN;
import com.clevel.selos.model.MandateConDetailType;
import com.clevel.selos.model.MandateConditionType;
import com.clevel.selos.model.MandateDependConType;
import com.clevel.selos.model.MandateDependType;
import com.clevel.selos.model.view.master.MandateFieldClassView;
import com.clevel.selos.model.view.master.MandateFieldConditionDetailView;
import com.clevel.selos.model.view.master.MandateFieldConditionView;
import com.clevel.selos.model.view.master.MandateFieldView;
import com.clevel.selos.util.FacesUtil;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name="mandateFieldCondition")
public class MandateFieldCondition implements Serializable {
    @Inject @ADMIN
    private Logger logger;

    @Inject
    private MandateFieldControl mandateFieldControl;

    private static final String MANDATE_FIELD_CONDITION_PAGE = "/admin/mandateFieldCondition.jsf";

    private String message = "";
    private List<MandateFieldConditionView> mandateFieldConViewList;
    private boolean preRenderCheck = false;
    private MandateFieldClassView mandateClassView;
    private int selectedMandateConViewId = -1;
    private int selectedMandateConDetailViewId = -1;
    private int selectedMandateDependCondViewId = -1;
    private int selectedMandateFieldViewId = -1;
    private MandateFieldConditionView wrkMandateConditionView;
    private MandateFieldConditionDetailView wrkMandateConditionDetView;
    private List<MandateFieldView> mandateFieldViewList;
    private List<MandateFieldConditionDetailView> deletedMandateFieldConDetailViewList = null;
    private List<MandateFieldConditionView> deletedMandateFieldConViewList;
    private List<MandateFieldConditionView> externalMandateFieldConViewList;
    private List<SelectItem> dependConditionList;

    @Inject
    public MandateFieldCondition(){}

    @PostConstruct
    private void init() {
        logger.info("PostConstruct");
        HttpSession session = FacesUtil.getSession(false);
        if (session != null) {
            mandateClassView = (MandateFieldClassView)FacesUtil.getFlash().get("selectedMandateClassView");
            if(mandateClassView != null){
                _loadConditionDetail();
            } else {
                FacesUtil.redirect(MandateField.MANDATE_FIELD_SUM_PAGE);
            }
        }
    }

    public void preRender(){
        if(preRenderCheck)
            return;
        preRenderCheck = true;
    }

    private void _loadConditionDetail(){
        preRenderCheck = false;
        try{
            mandateFieldConViewList = mandateFieldControl.getMandateConditionList(mandateClassView);
            mandateFieldViewList = mandateFieldControl.getMandateFieldDB(mandateClassView);
            externalMandateFieldConViewList = mandateFieldControl.getExternalDependCondition(mandateClassView);
        }catch(Exception ex){
            logger.error("Cannot _loadFieldDetail and conditionDetail for {}", mandateClassView);
        }
    }

    public void onChangeDependConType(){
        logger.info("-- on Change Condition Type --");
        _loadDependConditionList(wrkMandateConditionView.getDependConType());
        logger.info("-- end onChange --");
    }

    public void onOpenAddMandateCondition(){
        logger.info("-- begin onOpenAddMandateCondition");
        resetCon();
        resetConDetail();
        wrkMandateConditionView = new MandateFieldConditionView();
        wrkMandateConditionView.setMandateConDetailType(MandateConDetailType.AND);
        wrkMandateConditionView.setMandateConditionType(MandateConditionType.CHECK_RESULT);
        wrkMandateConditionView.setDependType(MandateDependType.NO_DEPENDENCY);
        wrkMandateConditionView.setConditionDetailViewList(new ArrayList<MandateFieldConditionDetailView>());
        selectedMandateDependCondViewId = -1;
        logger.info("-- end onOpenAddMandateCondition");
    }

    /**
     * onOpenUpdateMandateCondition is performed when user click 'pencil' icon from 'Mandate Field - Condition table'
     */
    public void onOpenUpdateMandateCondition(){
        logger.info("-- begin onOpenUpdateMandateCondition");
        MandateFieldConditionView toUpd = null;
        selectedMandateConDetailViewId = -1;
        selectedMandateDependCondViewId = -1;
        if(selectedMandateConViewId >= 0 && selectedMandateConViewId < mandateFieldConViewList.size()){
            toUpd = mandateFieldConViewList.get(selectedMandateConViewId);
        }

        if(toUpd == null){
            return;
        }

        wrkMandateConditionView = new MandateFieldConditionView();
        wrkMandateConditionView.updateValues(toUpd);
        if(wrkMandateConditionView.getDependCondition() != null){
            _loadDependConditionList(wrkMandateConditionView.getDependConType());
        }
        logger.info("-- end onOpenUpdateMandateCondition {}", wrkMandateConditionView);
    }

    /**
     * onOpenAddMandateConditionDetail is performed when user click 'ADD' button from 'Mandate Condition Dialog - Add button before the condition detail screen'
     */
    public void onOpenAddMandateConditionDetail(){
        wrkMandateConditionDetView = new MandateFieldConditionDetailView();
    }

    /**
     * onOpenAddMandateConditionDetail is performed when user click 'ADD' button from 'Mandate Condition Dialog - Add button before the condition detail screen'
     */
    public void onOpenUpdateMandateConditionDetail(){
        wrkMandateConditionDetView = new MandateFieldConditionDetailView();
        logger.info("-- begin onOpenUpdateMandateConditionDetail");
        MandateFieldConditionDetailView toUpd = null;

        if(selectedMandateConDetailViewId >= 0 && selectedMandateConDetailViewId < wrkMandateConditionView.getConditionDetailViewList().size()){
            toUpd = wrkMandateConditionView.getConditionDetailViewList().get(selectedMandateConViewId);
        }

        if(toUpd == null){
            return;
        }

        wrkMandateConditionDetView = new MandateFieldConditionDetailView();
        wrkMandateConditionDetView.updateValues(toUpd);

        logger.info("-- end onOpenUpdateMandateConditionDetail {}", wrkMandateConditionDetView);

    }

    /**
     * onAddMandateCondition is performed when user click 'OK' button to on 'Mandate Condition Dialog'
     */
    public void onAddMandateCondition(){
        logger.info("-- begin onAddMandateCondition");
        if(!_validateCondition()){
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
            return;
        }

        logger.debug("update value {}", wrkMandateConditionView);
        //Set the updated value into Condition List
        MandateFieldConditionView toUpd = new MandateFieldConditionView();

        if(selectedMandateDependCondViewId >= 0) {
            if(wrkMandateConditionView.getDependConType().equals(MandateDependConType.INTERNAL)){
                wrkMandateConditionView.setDependCondition(mandateFieldConViewList.get(selectedMandateDependCondViewId));
            } else if(wrkMandateConditionView.getDependConType().equals(MandateDependConType.EXTERNAL)){
                wrkMandateConditionView.setDependCondition(externalMandateFieldConViewList.get(selectedMandateDependCondViewId));
                logger.debug("onAddMandateCondition: {}", wrkMandateConditionView.getDependCondition());
            }
        }

        toUpd.updateValues(wrkMandateConditionView);
        toUpd.setNeedUpdate(Boolean.TRUE);


        if(mandateFieldConViewList == null)
            mandateFieldConViewList = new ArrayList<MandateFieldConditionView>();

        if(selectedMandateConViewId >= 0) {
            mandateFieldConViewList.remove(selectedMandateConViewId);
            mandateFieldConViewList.add(selectedMandateConViewId, toUpd);
        } else {
            mandateFieldConViewList.add(toUpd);
        }

        logger.debug("reset mandate condition value");
        //Reset Value which initial for update the Condition

        resetCon();
        logger.info("-- end onAddMandateCondition before update function complete");
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);

    }

    /**
     * onAddMandateConditionDetail is performed when user click 'OK' button from 'Mandate Condition Detail Dialog'
     */
    public void onAddMandateConditionDetail(){
        logger.info("-- begin onAddMandateConditionDetail");
        if(selectedMandateFieldViewId < 0){
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
            message = "Please select the fieldName";
        }
        MandateFieldView mandateFieldView = new MandateFieldView();
        for(MandateFieldView _tmpFieldView : mandateFieldViewList){
            if(_tmpFieldView.getId() == selectedMandateFieldViewId){
                mandateFieldView = _tmpFieldView;
            }
        }

        wrkMandateConditionDetView.setMandateFieldView(mandateFieldView);

        if(!_validateConditionDetail()){
            message = "conditiondetail is incorrect";
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
            logger.info("fail validation");
            return;
        }
        logger.info("pass validation");

        MandateFieldConditionDetailView toAdd = new MandateFieldConditionDetailView();
        toAdd.updateValues(wrkMandateConditionDetView);
        toAdd.setNeedUpdate(Boolean.TRUE);

        List<MandateFieldConditionDetailView> detailViewList = wrkMandateConditionView.getConditionDetailViewList();
        if(detailViewList == null){
            detailViewList = new ArrayList<MandateFieldConditionDetailView>();
        }
        if(selectedMandateConDetailViewId >= 0){
            detailViewList.remove(selectedMandateConDetailViewId);
            detailViewList.add(selectedMandateConDetailViewId, toAdd);
        } else {
            detailViewList.add(toAdd);
        }


        wrkMandateConditionView.setNeedUpdate(Boolean.TRUE);
        wrkMandateConditionView.setConditionDetailViewList(detailViewList);
        resetConDetail();
        logger.info("-- end onAddMandateConditionDetail");
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    /**
     * onDeleteMandateCondition is performed when user click 'Delete(trash)' icon within 'Mandate Field screen - condition table'
     */
    public void onDeleteMandateCondition(){
        if(selectedMandateConViewId < 0)
            return;

        MandateFieldConditionView mandateFieldConditionView = mandateFieldConViewList.get(selectedMandateConViewId);
        if(mandateFieldConditionView.getId() > 0){
            if(deletedMandateFieldConViewList == null)
                deletedMandateFieldConViewList = new ArrayList<MandateFieldConditionView>();
            deletedMandateFieldConViewList.add(mandateFieldConditionView);
        }
        mandateFieldConViewList.remove(selectedMandateConViewId);
        selectedMandateConViewId = -1;
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    /************* Starting section for Condition ***************/
    /**
     * onDeleteMandateConditionDetail is performed when user click 'Delete(trash)' icon within 'Mandate Condition Dialog - condition detail table'
     */
    public void onDeleteMandateConditionDetail(){
        logger.debug("-- begin onDeleteMandateConditionDetail");
        if(selectedMandateConDetailViewId < 0)
            return;

        if(wrkMandateConditionView != null){
            List<MandateFieldConditionDetailView> conditionDetailViewList = wrkMandateConditionView.getConditionDetailViewList();
            MandateFieldConditionDetailView mandateFieldConditionDetailView = conditionDetailViewList.get(selectedMandateConDetailViewId);

            if(mandateFieldConditionDetailView.getId() > 0){
                if(deletedMandateFieldConDetailViewList == null)
                    deletedMandateFieldConDetailViewList = new ArrayList<MandateFieldConditionDetailView>();
                deletedMandateFieldConDetailViewList.add((mandateFieldConditionDetailView));
            }
            wrkMandateConditionView.getConditionDetailViewList().remove(selectedMandateConDetailViewId);
        }
        selectedMandateConDetailViewId = -1;
    }

    /**
     * onSaveMandateFieldDetail is for save all the change of Mandate Field Screen.
     */
    public void onSaveMandateFieldCondition(){
        logger.info("onSaveMandateFieldCondition");

        try{
            logger.info("mandateFiledClass: {}", mandateClassView);
            logger.info("mandateFieldCon: {}", mandateFieldConViewList);
            mandateFieldControl.saveMandateCondition(mandateClassView, mandateFieldViewList, mandateFieldConViewList);
            mandateFieldControl.deleteMandateFieldCondition(deletedMandateFieldConViewList, deletedMandateFieldConDetailViewList);
            //Reload New Field Information to get new detail.
            mandateFieldConViewList = mandateFieldControl.getMandateConditionList(mandateClassView);
            _loadConditionDetail();
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
        }catch (Exception ex){
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
        }
    }

    public void onCancelMandateFieldCondition(){
        resetAll();
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try{
            ec.redirect(ec.getRequestContextPath() + MandateField.MANDATE_FIELD_SUM_PAGE);
        }catch(IOException ioe){
            logger.error("Fail to redirect screen to " + MandateField.MANDATE_FIELD_SUM_PAGE,ioe);
        }
    }

    private void _loadDependConditionList(MandateDependConType mandateDependConType){
        dependConditionList = new ArrayList<SelectItem>();
        if(mandateDependConType == MandateDependConType.INTERNAL){
            int countIndex = 0;
            for(MandateFieldConditionView conditionView : mandateFieldConViewList){
                if(!conditionView.getName().equals(wrkMandateConditionView.getName())){
                    SelectItem selectItem = new SelectItem();
                    selectItem.setLabel(conditionView.getName());
                    //selectItem.setValue(mandateFieldConViewList.indexOf(conditionView));
                    selectItem.setValue(countIndex);
                    logger.debug("condition to add: {}", selectItem);
                    dependConditionList.add(selectItem);
                    if(wrkMandateConditionView.getDependCondition() != null && conditionView.getName().equals(wrkMandateConditionView.getDependCondition().getName())){
                        selectedMandateDependCondViewId = countIndex;
                    }
                }
                countIndex++;
            }
        } else if(mandateDependConType == MandateDependConType.EXTERNAL){
            int countIndex = 0;
            for(MandateFieldConditionView conditionView : externalMandateFieldConViewList){
                SelectItem selectItem = new SelectItem();
                selectItem.setLabel(conditionView.getName());
                //selectItem.setValue(externalMandateFieldConViewList.indexOf(conditionView));
                selectItem.setValue(countIndex);
                logger.debug("condition to add: {}", selectItem);
                dependConditionList.add(selectItem);
                if(wrkMandateConditionView.getDependCondition() != null && conditionView.getName().equals(wrkMandateConditionView.getDependCondition().getName())){
                    selectedMandateDependCondViewId = countIndex;
                }
                countIndex++;
            }
        }
    }

    private boolean _validateCondition(){
        if(wrkMandateConditionView == null){
            return false;
        }
        if(wrkMandateConditionView.getName() == null || "".equals(wrkMandateConditionView.getName().trim()))
            return false;
        if(wrkMandateConditionView.getConditionDetailViewList() == null || wrkMandateConditionView.getConditionDetailViewList().size() == 0)
            return false;
        if(wrkMandateConditionView.getMandateConDetailType() == null)
            return false;
        return true;
    }

    private boolean _validateConditionDetail(){
        logger.info("-- begin _validateConditionDetail");
        if(wrkMandateConditionDetView == null || wrkMandateConditionDetView.getMandateFieldView() == null)
            return false;
        return true;
    }

    private void resetAll(){
        externalMandateFieldConViewList = null;
        mandateFieldViewList = null;
        mandateFieldConViewList = null;
        preRenderCheck = false;
        deletedMandateFieldConViewList = null;
        mandateClassView = null;

        resetCon();
        resetConDetail();
    }

    private void resetCon(){
        message = "";
        selectedMandateConViewId = -1;
        wrkMandateConditionView = null;
        dependConditionList = null;

        selectedMandateDependCondViewId = -1;
    }

    private void resetConDetail(){
        deletedMandateFieldConDetailViewList = null;
        selectedMandateConDetailViewId = -1;
        selectedMandateFieldViewId = -1;
        wrkMandateConditionDetView = null;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MandateFieldConditionView> getMandateFieldConViewList() {
        return mandateFieldConViewList;
    }

    public void setMandateFieldConViewList(List<MandateFieldConditionView> mandateFieldConViewList) {
        this.mandateFieldConViewList = mandateFieldConViewList;
    }

    public MandateFieldClassView getMandateClassView() {
        return mandateClassView;
    }

    public void setMandateClassView(MandateFieldClassView mandateClassView) {
        this.mandateClassView = mandateClassView;
    }

    public int getSelectedMandateConViewId() {
        return selectedMandateConViewId;
    }

    public void setSelectedMandateConViewId(int selectedMandateConViewId) {
        this.selectedMandateConViewId = selectedMandateConViewId;
    }

    public int getSelectedMandateConDetailViewId() {
        return selectedMandateConDetailViewId;
    }

    public void setSelectedMandateConDetailViewId(int selectedMandateConDetailViewId) {
        this.selectedMandateConDetailViewId = selectedMandateConDetailViewId;
    }

    public int getSelectedMandateDependCondViewId() {
        return selectedMandateDependCondViewId;
    }

    public void setSelectedMandateDependCondViewId(int selectedMandateDependCondViewId) {
        this.selectedMandateDependCondViewId = selectedMandateDependCondViewId;
    }

    public int getSelectedMandateFieldViewId() {
        return selectedMandateFieldViewId;
    }

    public void setSelectedMandateFieldViewId(int selectedMandateFieldViewId) {
        this.selectedMandateFieldViewId = selectedMandateFieldViewId;
    }

    public MandateFieldConditionView getWrkMandateConditionView() {
        return wrkMandateConditionView;
    }

    public void setWrkMandateConditionView(MandateFieldConditionView wrkMandateConditionView) {
        this.wrkMandateConditionView = wrkMandateConditionView;
    }

    public MandateFieldConditionDetailView getWrkMandateConditionDetView() {
        return wrkMandateConditionDetView;
    }

    public void setWrkMandateConditionDetView(MandateFieldConditionDetailView wrkMandateConditionDetView) {
        this.wrkMandateConditionDetView = wrkMandateConditionDetView;
    }

    public List<MandateFieldView> getMandateFieldViewList() {
        return mandateFieldViewList;
    }

    public void setMandateFieldViewList(List<MandateFieldView> mandateFieldViewList) {
        this.mandateFieldViewList = mandateFieldViewList;
    }

    public List<MandateFieldConditionDetailView> getDeletedMandateFieldConDetailViewList() {
        return deletedMandateFieldConDetailViewList;
    }

    public List<MandateFieldConditionView> getDeletedMandateFieldConViewList() {
        return deletedMandateFieldConViewList;
    }

    public List<SelectItem> getDependConditionList() {
        return dependConditionList;
    }
}
