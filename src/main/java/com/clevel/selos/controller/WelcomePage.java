package com.clevel.selos.controller;

import com.clevel.selos.dao.master.BusinessDescriptionDAO;
import com.clevel.selos.dao.master.BusinessGroupDAO;
import com.clevel.selos.dao.system.ConfigDAO;
import com.clevel.selos.integration.Integration;
import com.clevel.selos.model.db.master.BusinessDescription;
import com.clevel.selos.model.db.master.BusinessGroup;
import com.clevel.selos.model.db.system.Config;
import com.clevel.selos.system.MessageProvider;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name="welcomePage")
public class WelcomePage implements Serializable {
    @Inject
    Logger log;
    @Inject
    @Integration(Integration.System.RM)
    Logger rmLog;
    @Inject
    @Integration(Integration.System.NCB)
    Logger ncbLog;
    @Inject
    @Integration(Integration.System.NCBI)
    Logger ncbiLog;
    @Inject
    @Integration(Integration.System.SW_ROSC)
    Logger swLog;
    @Inject
    @Integration(Integration.System.EMAIL)
    Logger emailLog;
    @Inject
    @Integration(Integration.System.DWH)
    Logger dwhLog;
    @Inject
    @Integration(Integration.System.BRMS)
    Logger brmsLog;

    @Inject
    MessageProvider msg;
    @Inject
    ConfigDAO configDAO;
    List<Config> configList;

    private Date now;

    public WelcomePage() {
    }

    @PostConstruct
    public void onCreation() {
        log.debug("onCreation.");
        now = new Date();
        reloadConfig();
        onLoadDescription();
    }

    public void reloadConfig() {
        log.debug("reloadConfig.");
        configList = configDAO.findAll();
    }

    public void onActionRM() {
        rmLog.debug("test RM log. ({})",new Date());
    }

    public void onActionNCB() {
        ncbLog.debug("test NCB log. ({})",new Date());
    }

    public void onActionNCBI() {
        ncbiLog.debug("test NCBI log. ({})",new Date());
    }

    public void onActionSW() {
        swLog.debug("test SW log. ({})",new Date());
    }
    public void onActionEmail() {
        emailLog.debug("test Email log. ({})",new Date());
    }

    public void onActionDWH() {
        dwhLog.debug("test DWH log. ({})",new Date());
    }

    public void onActionBRMS() {
        brmsLog.debug("test BRMS log. ({})",new Date());
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public List<Config> getConfigList() {
        return configList;
    }

    public void setConfigList(List<Config> configList) {
        this.configList = configList;
    }

    @Inject
    BusinessDescriptionDAO businessDescriptionDAO;
    @Inject
    BusinessGroupDAO businessGroupDAO;
    List<BusinessGroup> businessGroups;
    BusinessGroup selectedBusinessGroup;
    List<BusinessDescription> businessDescriptions;
    BusinessDescription selectedBusinessDescription;
    String selectedText;


    public void onLoadDescription() {
        log.debug("onLoadDescription.");
        businessGroups = businessGroupDAO.findAll();
        log.debug("group size: {}",businessGroups.size());
        selectedBusinessGroup = businessGroups.get(0);
        businessDescriptions = businessDescriptionDAO.getListByBusinessGroup(businessGroups.get(0));
        selectedBusinessDescription = businessDescriptions.get(0);
    }

    public void onChangeGroup() {
        log.debug("onChangeGroup. (selected: {})",selectedBusinessGroup);
        BusinessGroup businessGroup = businessGroupDAO.findById(selectedBusinessGroup.getId());
        log.debug("{}",businessGroup);
        businessDescriptions = businessDescriptionDAO.getListByBusinessGroup(businessGroup);
        selectedText = "GROUP: "+businessGroup.getName();
    }

    public void onChangeDescription() {
        log.debug("onChangeDescription.");
        BusinessDescription businessDescription = businessDescriptionDAO.findById(selectedBusinessDescription.getId());
        log.debug("{}",businessDescription);
        selectedText = "DESCRIPTION: "+businessDescription.getName();
    }

    public List<BusinessGroup> getBusinessGroups() {
        return businessGroups;
    }

    public void setBusinessGroups(List<BusinessGroup> businessGroups) {
        this.businessGroups = businessGroups;
    }

    public List<BusinessDescription> getBusinessDescriptions() {
        return businessDescriptions;
    }

    public void setBusinessDescriptions(List<BusinessDescription> businessDescriptions) {
        this.businessDescriptions = businessDescriptions;
    }

    public BusinessGroup getSelectedBusinessGroup() {
        return selectedBusinessGroup;
    }

    public void setSelectedBusinessGroup(BusinessGroup selectedBusinessGroup) {
        this.selectedBusinessGroup = selectedBusinessGroup;
    }

    public BusinessDescription getSelectedBusinessDescription() {
        return selectedBusinessDescription;
    }

    public void setSelectedBusinessDescription(BusinessDescription selectedBusinessDescription) {
        this.selectedBusinessDescription = selectedBusinessDescription;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }
}
