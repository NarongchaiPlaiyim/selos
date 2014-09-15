package com.clevel.selos.controller.admin;

import com.clevel.selos.businesscontrol.master.MandateFieldControl;
import com.clevel.selos.integration.ADMIN;
import com.clevel.selos.model.MandateConditionType;
import com.clevel.selos.model.MandateDependType;
import com.clevel.selos.model.view.master.MandateFieldClassView;
import com.clevel.selos.model.view.master.MandateFieldConditionDetailView;
import com.clevel.selos.model.view.master.MandateFieldConditionView;
import com.clevel.selos.model.view.master.MandateFieldView;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ViewScoped
@ManagedBean(name="mandateFieldSum")
public class MandateField implements Serializable {

    @Inject @ADMIN
    private Logger log;

    @Inject
    private MandateFieldControl mandateFieldControl;

    public static final String MANDATE_FIELD_SUM_PAGE = "/admin/mandateFieldSum.jsf";

    private boolean preRenderCheck = false;
    private String message = "";
    private List<MandateFieldClassView> mandateFieldClassViewList;
    private MandateFieldClassView selectedMandateClassView;

    @Inject
    public MandateField(){}

    @PostConstruct
    private void init() {
        log.info("Construct");
        HttpSession session = FacesUtil.getSession(false);
        if (session != null) {
            _loadInitData();
        } else {
            FacesUtil.redirect("/admin/welcome.jsf");
        }
    }

    private void _loadInitData() {
        preRenderCheck = false;

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

        URL scanURL = null;
        try{
            URL url = ClasspathHelper.forWebInfClasses((ServletContext) ec.getContext());
            //scanURL = url;
            scanURL = new URL(url.toString()+"../../../lib/selos-lib.jar");
            log.info("-- URL {}", scanURL.toString());

        }catch (Exception ex){
            log.error("Cannot Build the URL");
        }

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forWebInfClasses((ServletContext) ec.getContext()), scanURL)
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(MandateFieldControl.PACKAGE_NAME))));

        Set<Class<?>> classesSet = reflections.getSubTypesOf(java.lang.Object.class);

        mandateFieldClassViewList = mandateFieldControl.getMandateFieldClass(classesSet);
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

    public String onLinkUpdateMandateFieldCondition(){
        if (selectedMandateClassView == null){
            log.info("selectedMandateClassView : {}", selectedMandateClassView);
            return "";
        }
        FacesUtil.getFlash().put("selectedMandateClassView", selectedMandateClassView);
        return "mandateFieldCondition?faces-redirect=true";
    }

    private void reset(){
        selectedMandateClassView = null;
        mandateFieldClassViewList = null;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
