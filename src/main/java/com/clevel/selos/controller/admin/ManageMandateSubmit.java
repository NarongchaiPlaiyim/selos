package com.clevel.selos.controller.admin;

import com.clevel.selos.businesscontrol.admin.MandateFieldControl;
import com.clevel.selos.integration.ADMIN;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.UserSysParameterKey;
import com.clevel.selos.model.view.MandateFieldClassView;
import com.clevel.selos.model.view.MandateFieldView;
import com.clevel.selos.transform.MandateFieldTransform;
import com.clevel.selos.util.FacesUtil;
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
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@ViewScoped
@ManagedBean(name="manageMandateSubmit")
public class ManageMandateSubmit implements Serializable {

    @Inject @ADMIN
    private Logger log;

    @Inject
    private MandateFieldControl mandateFieldControl;

    private boolean preRenderCheck = false;
    private List<MandateFieldClassView> mandateFieldClassViewList;
    private MandateFieldClassView selectedMandateClassView;
    private List<MandateFieldView> mandateFieldViewList;
    private int selectedMandateFieldViewId;
    private MandateFieldView wrkMandateFieldView;



    @Inject
    public ManageMandateSubmit(){}

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

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forPackage(packageName))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));

        Set<Class<?>> classesSet = reflections.getSubTypesOf(java.lang.Object.class);

        mandateFieldClassViewList = mandateFieldControl.getMandateFieldClass(classesSet);
    }

    private void _loadFieldDetail(){
        preRenderCheck = false;
        try{
            mandateFieldViewList = mandateFieldControl.getMandateField(selectedMandateClassView);
        }catch(Exception ex){

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
        wrkMandateFieldView.setPage(selectedMandateClassView.getPageName());
    }



    public void onUpdateMatchedValue(){

    }



    public String onSaveMandateFieldDetail(){


        return "";
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
}
