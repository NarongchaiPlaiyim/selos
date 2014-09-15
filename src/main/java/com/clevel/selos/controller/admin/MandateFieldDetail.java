package com.clevel.selos.controller.admin;

import com.clevel.selos.businesscontrol.master.MandateFieldControl;
import com.clevel.selos.integration.ADMIN;
import com.clevel.selos.model.view.master.MandateFieldClassView;
import com.clevel.selos.model.view.master.MandateFieldView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@ManagedBean(name="mandateFieldDetail")
public class MandateFieldDetail implements Serializable {

    @Inject
    @ADMIN
    private Logger logger;
    @Inject
    private MandateFieldControl mandateFieldControl;

    private MandateFieldClassView mandateClassView;
    private boolean preRenderCheck = false;
    private List<MandateFieldView> mandateFieldViewList;
    private int selectedMandateFieldViewId = -1;
    private MandateFieldView wrkMandateFieldView;

    @Inject
    public MandateFieldDetail(){}

    @PostConstruct
    private void init() {
        logger.info("PostConstruct");
        HttpSession session = FacesUtil.getSession(false);
        if (session != null) {
            mandateClassView = (MandateFieldClassView)FacesUtil.getFlash().get("selectedMandateClassView");
            if(mandateClassView != null){
                _loadFieldDetail();
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

    private void _loadFieldDetail(){
        preRenderCheck = false;
        try{
            mandateFieldViewList = mandateFieldControl.getMandateField(mandateClassView);
        }catch(Exception ex){
            logger.error("Cannot _loadFieldDetail and conditionDetail for {}", mandateClassView);
        }
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
            wrkMandateFieldView.setPage(mandateClassView.getPageName());
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
     * onSaveMandateFieldDetail is for save all the change of Mandate Field Screen.
     */
    public void onSaveMandateFieldDetail(){
        if(!_validateFieldClass()){
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
            return;
        }
        try{
            logger.info("mandateFiledClass: {}", mandateClassView);
            mandateFieldControl.saveMandateField(mandateClassView, mandateFieldViewList);
            //Reload New Field Information to get new detail.

            mandateClassView = mandateFieldControl.getMandateFieldClassView(mandateClassView.getClassName());
            _loadFieldDetail();
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
        }catch (Exception ex){
            RequestContext.getCurrentInstance().addCallbackParam("functionComplete", false);
        }
    }

    public void onCancelMandateFieldDetail(){
        reset();
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try{
            ec.redirect(ec.getRequestContextPath() + MandateField.MANDATE_FIELD_SUM_PAGE);
        }catch(IOException ioe){
            logger.error("Fail to redirect screen to " + MandateField.MANDATE_FIELD_SUM_PAGE,ioe);
        }
    }

    private boolean _validateFieldClass(){
        if(mandateClassView == null)
            return false;
        return true;
    }

    private boolean _validateFieldDetail(){
        if (wrkMandateFieldView == null)
            return false;
        return true;
    }

    private void reset(){
        mandateClassView = null;
        selectedMandateFieldViewId = 0;
        mandateFieldViewList = null;
        wrkMandateFieldView = null;
    }

    public MandateFieldClassView getMandateClassView() {
        return mandateClassView;
    }

    public void setMandateClassView(MandateFieldClassView mandateClassView) {
        this.mandateClassView = mandateClassView;
    }

    public List<MandateFieldView> getMandateFieldViewList() {
        return mandateFieldViewList;
    }

    public void setMandateFieldViewList(List<MandateFieldView> mandateFieldViewList) {
        this.mandateFieldViewList = mandateFieldViewList;
    }

    public int getSelectedMandateFieldViewId() {
        return selectedMandateFieldViewId;
    }

    public void setSelectedMandateFieldViewId(int selectedMandateFieldViewId) {
        this.selectedMandateFieldViewId = selectedMandateFieldViewId;
    }

    public MandateFieldView getWrkMandateFieldView() {
        return wrkMandateFieldView;
    }

    public void setWrkMandateFieldView(MandateFieldView wrkMandateFieldView) {
        this.wrkMandateFieldView = wrkMandateFieldView;
    }
}
