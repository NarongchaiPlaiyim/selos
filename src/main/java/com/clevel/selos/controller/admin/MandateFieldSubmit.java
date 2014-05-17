package com.clevel.selos.controller.admin;

import com.clevel.selos.businesscontrol.admin.MandateFieldControl;
import com.clevel.selos.integration.ADMIN;
import com.clevel.selos.model.MandateConditionType;
import com.clevel.selos.model.MandateDependType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.UserSysParameterKey;
import com.clevel.selos.model.db.master.MandateFieldConditionDetail;
import com.clevel.selos.model.view.MandateFieldClassView;
import com.clevel.selos.model.view.MandateFieldConditionDetailView;
import com.clevel.selos.model.view.MandateFieldConditionView;
import com.clevel.selos.model.view.MandateFieldView;
import com.clevel.selos.transform.MandateFieldTransform;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import sun.reflect.Reflection;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ViewScoped
@ManagedBean(name="mandateFieldSubmit")
public class MandateFieldSubmit implements Serializable {

    @Inject @ADMIN
    private Logger log;

    @Inject
    private MandateFieldControl mandateFieldControl;

    private static final String MANDATE_FIELD_SUM_PAGE = "/admin/mandateFieldSum.jsf";

    private boolean preRenderCheck = false;
    private String message = "";
    private List<MandateFieldClassView> mandateFieldClassViewList;
    private MandateFieldClassView selectedMandateClassView;
    private List<MandateFieldView> mandateFieldViewList;
    private List<MandateFieldConditionView> mandateFieldConViewList;
    private List<MandateFieldConditionView> deletedMandateFieldConViewList;
    private List<MandateFieldConditionDetailView> deletedMandateFieldConDetailViewList;
    private int selectedMandateFieldViewId = -1;
    private int selectedMandateConViewId = -1;
    private int selectedMandateConDetailViewId = -1;
    private int selectedMandateDependCondViewId = -1;
    private MandateFieldView wrkMandateFieldView;
    private MandateFieldConditionView wrkMandateConditionView;
    private MandateFieldConditionDetailView wrkMandateConditionDetView;

    @Inject
    public MandateFieldSubmit(){}

    @PostConstruct
    private void init() {
        log.info("Construct");
        HttpSession session = FacesUtil.getSession(false);
        if (session != null) {
            selectedMandateClassView = (MandateFieldClassView)FacesUtil.getFlash().get("selectedMandateClassView");
            if(selectedMandateClassView != null){
                _loadFieldDetail();
            } else {
                _loadInitData();
            }
        }
    }

    private void _loadInitData() {
        preRenderCheck = false;
        String packageName = "com.clevel.selos.model.db.working";

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();


        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                //.setUrls(ClasspathHelper.forPackage("" + packageName))
                .setUrls(ClasspathHelper.forWebInfClasses((ServletContext) ec.getContext()))
                //.setUrls(ClasspathHelper.forPackage(ec.getRequestContextPath() + packageName))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));

        Set<Class<?>> classesSet = reflections.getSubTypesOf(java.lang.Object.class);

        mandateFieldClassViewList = mandateFieldControl.getMandateFieldClass(classesSet);
    }

    private void _loadFieldDetail(){
        preRenderCheck = false;
        try{
            mandateFieldViewList = mandateFieldControl.getMandateField(selectedMandateClassView);
            mandateFieldConViewList = mandateFieldControl.getMandateConditionList(selectedMandateClassView);
        }catch(Exception ex){
            log.error("Cannot _loadFieldDetail and conditionDetail for {}", selectedMandateClassView);
        }
    }

    public void preRender(){
        if(preRenderCheck)
            return;
        preRenderCheck = true;
    }

    public String onLinkEditMandateFieldDetail(){
        if (selectedMandateClassView == null){
            log.info("selectedMandateClassView : {}", selectedMandateClassView);
            return "";
        }
        FacesUtil.getFlash().put("selectedMandateClassView", selectedMandateClassView);
        return "mandateFieldDetail?faces-redirect=true";
    }

    public void onOpenUpdateMandateFieldDetail(){
        MandateFieldView toUpd = null;
        if(selectedMandateFieldViewId >= 0 && selectedMandateFieldViewId < mandateFieldViewList.size())
            toUpd = mandateFieldViewList.get(selectedMandateFieldViewId);

        if(toUpd == null){
            return;
        }

        wrkMandateFieldView = new MandateFieldView();
        wrkMandateFieldView.updateValues(toUpd);
        if(Util.isEmpty(wrkMandateFieldView.getPage()))
            wrkMandateFieldView.setPage(selectedMandateClassView.getPageName());
    }

    public void onOpenAddMandateCondition(){
        log.info("-- begin onOpenAddMandateCondition");
        wrkMandateConditionView = new MandateFieldConditionView();
        wrkMandateConditionView.setMandateConditionType(MandateConditionType.BASE);
        wrkMandateConditionView.setDependType(MandateDependType.NO_DEPENDENCY);
        wrkMandateConditionView.setConditionDetailViewList(new ArrayList<MandateFieldConditionDetailView>());
        log.info("-- end onOpenAddMandateCondition");
    }


    /**
     * onOpenUpdateMandateCondition is performed when user click 'pencil' icon from 'Mandate Field - Condition table'
     */
    public void onOpenUpdateMandateCondition(){
        log.info("-- begin onOpenUpdateMandateCondition");
        MandateFieldConditionView toUpd = null;

        if(selectedMandateConViewId >= 0 && selectedMandateConViewId < mandateFieldConViewList.size()){
            toUpd = mandateFieldConViewList.get(selectedMandateConViewId);
        }

        if(toUpd == null){
            return;
        }

        wrkMandateConditionView = new MandateFieldConditionView();
        wrkMandateConditionView.updateValues(toUpd);
        if(wrkMandateConditionView.getDependCondition() != null){
            int count = 0;
            for(MandateFieldConditionView mandateFieldConditionView : mandateFieldConViewList){
                if(mandateFieldConditionView.getName().equals(wrkMandateConditionView.getDependCondition().getName())){
                    selectedMandateDependCondViewId = count;
                }
                count++;
            }
        }
        log.info("-- end onOpenUpdateMandateCondition {}", wrkMandateConditionView);
    }


    /**
     * onAddMandateCondition is performed when user click 'OK' button to on 'Mandate Condition Dialog'
     */
    public void onAddMandateCondition(){
        log.info("-- begin onAddMandateCondition");
        if(!_validateCondition()){
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
            return;
        }

        log.debug("update value {}", wrkMandateConditionView);
        //Set the updated value into Condition List
        MandateFieldConditionView toUpd = new MandateFieldConditionView();

        if(selectedMandateDependCondViewId >= 0)
            wrkMandateConditionView.setDependCondition(mandateFieldConViewList.get(selectedMandateDependCondViewId));

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

        log.debug("reset mandate condition value");
        //Reset Value which initial for update the Condition
        wrkMandateConditionView = null;
        wrkMandateConditionDetView = null;
        selectedMandateConDetailViewId = -1;
        selectedMandateConViewId = -1;
        selectedMandateDependCondViewId = -1;

        log.info("-- end onAddMandateCondition before update function complete");
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
     * onOpenAddMandateConditionDetail is performed when user click 'ADD' button from 'Mandate Condition Dialog - Add button before the condition detail screen'
     */
    public void onOpenAddMandateConditionDetail(){
        wrkMandateConditionDetView = new MandateFieldConditionDetailView();
    }

    /**
     * onOpenUpdateMandateConditionDetail is performed when user click 'Edit/Pencil' icon from 'Mandate Condition Dialog - condition detail table'
     */
    public void onOpenUpdateMandateConditionDetail(){
        if(selectedMandateConDetailViewId < 0)
            return;

        wrkMandateConditionDetView = new MandateFieldConditionDetailView();
        if(wrkMandateConditionView.getConditionDetailViewList().size() > 0){
            MandateFieldConditionDetailView toUpd = wrkMandateConditionView.getConditionDetailViewList().get(selectedMandateConDetailViewId);
            wrkMandateConditionDetView.updateValues(toUpd);
        }
    }

    /**
     * onAddMandateConditionDetail is performed when user click 'OK' button from 'Mandate Condition Detail Dialog'
     */
    public void onAddMandateConditionDetail(){
        log.info("-- begin onAddMandateConditionDetail");
        if(selectedMandateFieldViewId < 0){
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
            message = "Please select the fieldName";
        }

        MandateFieldView mandateFieldView = mandateFieldViewList.get(selectedMandateFieldViewId);
        wrkMandateConditionDetView.setMandateFieldView(mandateFieldView);

        if(!_validateConditionDetail()){
            message = "conditiondetail is incorrect";
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
            log.info("fail validation");
            return;
        }
        log.info("pass validation");

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
        wrkMandateConditionDetView = null;
        selectedMandateConDetailViewId = -1;
        log.info("-- end onAddMandateConditionDetail");
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    /**
     * onUpdateMandateFieldDetail is performed when user click 'OK' button from 'Mandate Field Dialog'
     */
    public void onUpdateMandateFieldDetail(){
        if(!_validateFieldDetail()){
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
            return;
        }

        wrkMandateFieldView.setNeedUpdate(Boolean.TRUE);
        MandateFieldView upd = mandateFieldViewList.get(selectedMandateFieldViewId);
        upd.updateValues(wrkMandateFieldView);
        wrkMandateFieldView = null;
        selectedMandateFieldViewId = -1;
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
    }

    /**
     * onSaveAllMandateField is for save all the change of Mandate Field Screen.
     */
    public void onSaveAllMandateField(){
        if(!_validateFieldClass()){
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
            return;
        }
        try{
            log.info("mandateFiledClass: {}", selectedMandateClassView);
            mandateFieldControl.deleteAllMandateField(deletedMandateFieldConViewList, deletedMandateFieldConDetailViewList);
            mandateFieldControl.saveAllMandateField(selectedMandateClassView, mandateFieldViewList, mandateFieldConViewList);
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
        }catch (Exception ex){
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
        }
    }

    public void onCancelMandateField(){
        reset();
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try{
            ec.redirect(ec.getRequestContextPath() + MANDATE_FIELD_SUM_PAGE);
        }catch(IOException ioe){
            log.error("Fail to redirect screen to " + MANDATE_FIELD_SUM_PAGE,ioe);
        }
    }

    private void reset(){
        selectedMandateClassView = null;
        selectedMandateFieldViewId = 0;
        mandateFieldClassViewList = null;
        mandateFieldViewList = null;
        wrkMandateFieldView = null;
    }

    private boolean _validateFieldDetail(){
        if (wrkMandateFieldView == null)
            return false;
        return true;
    }

    private boolean _validateFieldClass(){
        if(selectedMandateClassView == null)
            return false;
        return true;
    }

    private boolean _validateCondition(){
        if(wrkMandateConditionView == null){
            return false;
        }
        return true;
    }

    private boolean _validateConditionDetail(){
        log.info("-- begin _validateConditionDetail");
        if(wrkMandateConditionDetView == null)
            return false;
        if(wrkMandateConditionDetView.getMandateFieldView() == null)
            return false;
        return true;
    }

    public List<SelectItem> getMandateFieldList(){
        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        int count = 0;
        for(MandateFieldView mandateFieldView : mandateFieldViewList){
            if(mandateFieldView.getId() > 0 || mandateFieldView.isNeedUpdate()){
                SelectItem selectItem = new SelectItem();
                selectItem.setValue(count);
                selectItem.setLabel(mandateFieldView.getFieldName());
                selectItem.setDescription(mandateFieldView.getFieldDesc());
                selectItemList.add(selectItem);
            }
            count++;
        }
        return selectItemList;
    }

    public List<SelectItem> getConditionList(){
        List<SelectItem> selectItemList = new ArrayList<SelectItem>();
        int count = 0;
        for(MandateFieldConditionView conditionView : mandateFieldConViewList){
            if(!conditionView.getName().equals(wrkMandateConditionView.getName())){
                SelectItem selectItem = new SelectItem();
                selectItem.setValue(count);
                selectItem.setLabel(conditionView.getName());
                selectItem.setDescription(conditionView.getConditionDesc());
                selectItemList.add(selectItem);
            }
            count++;
        }
        return selectItemList;
    }

    public List<MandateFieldClassView> getMandateFieldClassViewList() {
        return mandateFieldClassViewList;
    }

    public void setMandateFieldClassViewList(List<MandateFieldClassView> mandateFieldClassViewList) {
        this.mandateFieldClassViewList = mandateFieldClassViewList;
    }

    public MandateFieldClassView getSelectedMandateClassView() {
        return selectedMandateClassView;
    }

    public void setSelectedMandateClassView(MandateFieldClassView selectedMandateClassView) {
        this.selectedMandateClassView = selectedMandateClassView;
    }

    public int getSelectedMandateFieldViewId() {
        return selectedMandateFieldViewId;
    }

    public void setSelectedMandateFieldViewId(int selectedMandateFieldViewId) {
        this.selectedMandateFieldViewId = selectedMandateFieldViewId;
    }

    public List<MandateFieldView> getMandateFieldViewList() {
        return mandateFieldViewList;
    }

    public void setMandateFieldViewList(List<MandateFieldView> mandateFieldViewList) {
        this.mandateFieldViewList = mandateFieldViewList;
    }

    public MandateFieldView getWrkMandateFieldView() {
        return wrkMandateFieldView;
    }

    public void setWrkMandateFieldView(MandateFieldView wrkMandateFieldView) {
        this.wrkMandateFieldView = wrkMandateFieldView;
    }

    public List<MandateFieldConditionView> getMandateFieldConViewList() {
        return mandateFieldConViewList;
    }

    public void setMandateFieldConViewList(List<MandateFieldConditionView> mandateFieldConViewList) {
        this.mandateFieldConViewList = mandateFieldConViewList;
    }

    public int getSelectedMandateConViewId() {
        return selectedMandateConViewId;
    }

    public void setSelectedMandateConViewId(int selectedMandateConViewId) {
        this.selectedMandateConViewId = selectedMandateConViewId;
    }

    public MandateFieldConditionView getWrkMandateConditionView() {
        return wrkMandateConditionView;
    }

    public void setWrkMandateConditionView(MandateFieldConditionView wrkMandateConditionView) {
        this.wrkMandateConditionView = wrkMandateConditionView;
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

    public MandateFieldConditionDetailView getWrkMandateConditionDetView() {
        return wrkMandateConditionDetView;
    }

    public void setWrkMandateConditionDetView(MandateFieldConditionDetailView wrkMandateConditionDetView) {
        this.wrkMandateConditionDetView = wrkMandateConditionDetView;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
