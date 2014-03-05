package com.clevel.selos.controller;

/**
 * Created with IntelliJ IDEA.
 * User: Sreenu
 * Date: 2/21/14
 * Time: 10:25 AM
 * To change this template use File | Settings | File Templates.
 */
import com.clevel.selos.businesscontrol.PEDBExecute;
import com.clevel.selos.dao.master.ActionDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Action;
import com.clevel.selos.model.db.master.Relretunactions;
import com.clevel.selos.model.view.PERoster;
import org.slf4j.Logger;

import javax.faces.bean.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;

@ManagedBean(name = "trackBean")
@RequestScoped
public class TrackcaBean implements Serializable
{

    @Inject
    @SELOS
    Logger log;

    @Inject
    PEDBExecute pedbExecute;

    @Inject
    ActionDAO actionDAO;

    private ArrayList<PERoster> rosterViewList ;

    private PERoster rosterViewSelectItem;

    private String columnName;

    private  String sortOrder;

    private String statusType;

    Action action = null;

    ArrayList<Action> descriptionList = null;

    Relretunactions relretunactions = null;

    ArrayList<Relretunactions> relretunactionses = null;

    String decriptionList = null;

    ArrayList<Action> discriptionList = null;

    public PERoster getRosterViewSelectItem() {
        return rosterViewSelectItem;
    }

    public void setRosterViewSelectItem(PERoster rosterViewSelectItem) {
        this.rosterViewSelectItem = rosterViewSelectItem;
    }

    public ArrayList<PERoster> getRosterViewList() {
        return rosterViewList;
    }

    public void setRosterViewList(ArrayList<PERoster> rosterViewList) {
        this.rosterViewList = rosterViewList;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public ArrayList<PERoster> peRosterQueryForTrackCa()
    {

        action = new Action();

        descriptionList = new ArrayList<Action>();

        relretunactions = new Relretunactions();

        log.info("before call the method::::");

        relretunactionses = new ArrayList<Relretunactions>();

        discriptionList = new ArrayList<Action>();

        decriptionList = actionDAO.getDescripationFromAction(relretunactions,action );

        log.info("stringDiscriptionList in Trackbean :::::::"+decriptionList);

        try
        {
            rosterViewList =  pedbExecute.getRosterQuery(statusType,decriptionList);
        }
        catch(Exception e)
        {
            log.error("Error im track Ca *** ",e);
        }
        finally
        {

            rosterViewSelectItem = null;
            columnName = null;
            sortOrder = null;
            statusType = null;
            action = null;
            descriptionList = null;
            relretunactions = null;
            relretunactionses = null;
            decriptionList = null;

        }

        log.info("rosterViewList is : {}",rosterViewList.size());

        return  rosterViewList;

    }




}
