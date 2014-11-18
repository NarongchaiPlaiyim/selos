package com.clevel.selos.controller;

import com.clevel.selos.businesscontrol.MandatoryFieldsControl;
import com.clevel.selos.businesscontrol.UserAccessControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.Screen;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.AppHeaderView;
import com.clevel.selos.model.view.FieldsControlView;
import com.clevel.selos.model.view.UserAccessView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;
import org.primefaces.context.RequestContext;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class BaseController implements Serializable {
    @Inject
    MandatoryFieldsControl mandatoryFieldsControl;
    @Inject
    UserAccessControl userAccessControl;
    @Inject
    @SELOS
    private org.slf4j.Logger log;

    private final HashMap<String, FieldsControlView> fieldMap = new HashMap<String, FieldsControlView>();
    private final HashMap<String, FieldsControlView> dialogFieldMap = new HashMap<String, FieldsControlView>();
    private final HashMap<String, UserAccessView> userAccessMap = new HashMap<String, UserAccessView>();
    private final HashMap<String, UserAccessView> userAccessMenuMap = new HashMap<String, UserAccessView>();

    private AppHeaderView appHeaderView;

    protected void loadFieldControl(long workCaseId, Screen screenId, String ownerCaseUserId) {
        log.debug("ownerCaseUserId : {}", ownerCaseUserId);
        HttpSession session = FacesUtil.getSession(false);
        long stepId = Util.parseLong(session.getAttribute("stepId"), 0);
        long statusId = Util.parseLong(session.getAttribute("statusId"), 0);
        List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlView(workCaseId, stepId, statusId, screenId, ownerCaseUserId);
        //List<FieldsControlView> dialogFields = mandatoryFieldsControl.getFieldsControlView(workCaseId, stepId, screenId, ownerCaseUserId);
        fieldMap.clear();
        dialogFieldMap.clear();
        for (FieldsControlView field : fields) {
            fieldMap.put(field.getFieldName(), field);
            //log.debug("Field Map ScreenId : [{}], WorkCaseId : [{}], fieldMap : [{}]", screenId, workCaseId, fieldMap);
        }

        /*for (FieldsControlView field : dialogFields) {
            dialogFieldMap.put(field.getFieldName(), field);
        }*/

    }

    protected void loadFieldControlPreScreen(long workCasePreScreenId, Screen screenId, String ownerCaseUserId) {
        log.debug("ownerCaseUserId : {}", ownerCaseUserId);
        HttpSession session = FacesUtil.getSession(false);
        long stepId = Util.parseLong(session.getAttribute("stepId"), 0);
        long statusId = Util.parseLong(session.getAttribute("statusId"), 0);
        List<FieldsControlView> fields = mandatoryFieldsControl.getFieldsControlViewPreScreen(workCasePreScreenId, stepId, statusId, screenId, ownerCaseUserId);
        fieldMap.clear();
        dialogFieldMap.clear();
        for (FieldsControlView field : fields) {
            fieldMap.put(field.getFieldName(), field);
            //log.debug("Field Map ScreenId : [{}], WorkCaseId : [{}], fieldMap : [{}]", screenId, workCaseId, fieldMap);
        }

    }

    public String mandate(String name) {
        FieldsControlView field = fieldMap.get(name);
        if (field == null)
            return "";
        return field.isMandate() ? " *" : "";
    }

    public boolean isDisabled(String name) {
        FieldsControlView field = fieldMap.get(name);
        if (field == null)
            return true;
        return field.isReadOnly();
    }

    public boolean isMandate(String name) {
        FieldsControlView field = fieldMap.get(name);
        if (field == null)
            return true;
        return field.isMandate();
    }

    public void setDisabledValue(String name, boolean disabled){
        FieldsControlView field = fieldMap.get(name);
        if (field == null)
            return;
        field.setReadOnly(disabled);
    }

    public void setMandateValue(String name, boolean madate){
        FieldsControlView field = fieldMap.get(name);
        if (field == null)
            return;
        field.setMandate(madate);
    }

    public boolean isDialogMandate(String name) {
        FieldsControlView field = dialogFieldMap.get(name);
        if (field == null)
            return false;
        return field.isMandate();
    }

    public boolean isDialogDisable(String name) {
        FieldsControlView field = dialogFieldMap.get(name);
        if (field == null)
            return true;
        return field.isReadOnly();
    }

    public boolean checkSession(HttpSession session){
        boolean checkSession = false;
        if(( Util.parseLong(session.getAttribute("workCaseId"), 0) != 0 || Util.parseLong(session.getAttribute("workCasePreScreenId"), 0) != 0 ) &&
                Util.parseLong(session.getAttribute("stepId"), 0) != 0){
            checkSession = true;
        }

        return checkSession;
    }

    protected long getCurrentStep(HttpSession session){
        long stepId = Util.parseLong(session.getAttribute("stepId"), 0);
        return stepId;
    }

    protected long getCurrentStatus(HttpSession session){
        long statusId = Util.parseLong(session.getAttribute("statusId"), 0);
        return statusId;
    }

    protected long getCurrentWorkCaseId(HttpSession session){
        long workCaseId = Util.parseLong(session.getAttribute("workCaseId"), 0);
        return workCaseId;
    }

    //Function for User Access Matrix
    protected void loadUserAccessMatrix(Screen screen){
        HttpSession session = FacesUtil.getSession(false);
        long stepId = Util.parseLong(session.getAttribute("stepId"), 0);
        List<UserAccessView> userAccessViewList = userAccessControl.getUserAccessList(stepId, screen.value());
        userAccessMap.clear();
        for(UserAccessView userAccessView : userAccessViewList){
            userAccessMap.put(Integer.toString(userAccessView.getScreenId()), userAccessView);
        }
    }

    //Function for User Access Matrix
    protected void loadUserAccessMenu(){
        HttpSession session = FacesUtil.getSession(false);
        long stepId = Util.parseLong(session.getAttribute("stepId"), 0);
        List<UserAccessView> userAccessViewList = userAccessControl.getUserAccessList(stepId);
        userAccessMenuMap.clear();
        for(UserAccessView userAccessView : userAccessViewList){
            userAccessMenuMap.put(Integer.toString(userAccessView.getScreenId()), userAccessView);
        }
        log.debug("userAccessMenuMap : {}", userAccessMenuMap);
    }

    /*public boolean isDialogMandate(String name) {
        FieldsControlView field = dialogFieldMap.get(name);
        if (field == null)
            return false;
        return field.isMandate();
    }*/

    public boolean canAccessMenu(int screenId){
        if(userAccessMenuMap.containsKey(Integer.toString(screenId))){
            UserAccessView userAccessView = userAccessMenuMap.get(Integer.toString(screenId));
            if(userAccessView == null)
                return false;

            return userAccessView.isAccessFlag();
        }
        return false;
    }

    public boolean canAccess(Screen screen){
        String screenId = Integer.toString(screen.value());
        if(userAccessMap.containsKey(screenId)){
            UserAccessView userAccessView = userAccessMap.get(screenId);
            if(userAccessView == null)
                return false;

            return userAccessView.isAccessFlag();
        }
        return false;
    }

    public void showMessageRedirect(){
        RequestContext.getCurrentInstance().execute("msgBoxBaseRedirectDlg.show()");
    }

    public void showMessageBox(){
        RequestContext.getCurrentInstance().execute("msgBoxBaseMessageDlg.show()");
    }

    public void showMessageRefresh() {
        RequestContext.getCurrentInstance().execute("msgBoxBaseRefreshDlg.show()");
    }

    public void showMessageMandate(){
        RequestContext.getCurrentInstance().execute("msgBoxMandateMessageDlg.show()");
    }

    public void showMessageNoPermissionBox(){
        RequestContext.getCurrentInstance().execute("msgBoxNoPermissionDlg.show()");
    }

    public void sendCallBackParam(boolean value){
        RequestContext.getCurrentInstance().addCallbackParam("functionComplete", value);
    }

    public AppHeaderView getAppHeaderView() {
        HttpSession session = FacesUtil.getSession(false);
        AppHeaderView appHeaderView = (AppHeaderView) session.getAttribute("appHeaderInfo");
        if(Util.isNull(appHeaderView))
            appHeaderView = new AppHeaderView();
        return appHeaderView;
    }

    public void setAppHeaderView(AppHeaderView appHeaderView) {
        HttpSession session = FacesUtil.getSession(true);
        session.setAttribute("appHeaderInfo", appHeaderView);
        this.appHeaderView = appHeaderView;
    }

    protected User getCurrentUser() {
        HttpSession session = FacesUtil.getSession(false);
        User user = (User) session.getAttribute("user");
        if(Util.isNull(user)) {
            user = new User();
        }
        return user;
    }
}
