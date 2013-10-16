package com.clevel.selos.controller;


import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.NCBSummaryView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.util.FacesUtil;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ViewScoped
@ManagedBean(name = "ncbSummary")
public class NCBSummary implements Serializable {
    @Inject
    Logger log;

    @Inject
    @NormalMessage
    Message msg;

    @Inject
    @ValidationMessage
    Message validationMsg;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    @Inject
    private CustomerDAO customerDAO;

    private List<NCBSummaryView> ncbSumList;
    private NCBSummaryView ncbSum;
    private long workCaseId;


    //test show customer
    private List<Customer> customerViewTest;
    private Customer customerItem;

    public NCBSummary(){}

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.");

        HttpSession session = FacesUtil.getSession(true);

        session.setAttribute("workCaseId", new Long(1)) ;    // ไว้เทส set workCaseId ส่งมาจาก NCB Summary

        if(session.getAttribute("workCaseId") != null){
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.info("workCaseId :: {} ",workCaseId);
        }

        try{
            customerViewTest = customerDAO.findByWorkCaseId(workCaseId);
        }catch (Exception e){
            log.error( "customerDAO.findAll  error ::: {}" , e.getMessage());
        }

        if(customerViewTest == null){
            customerViewTest = new ArrayList<Customer>();
        }


        if(customerItem == null){
            customerItem = new Customer();
        }

        if(ncbSumList == null){
            ncbSumList = new ArrayList<NCBSummaryView>();
        }


    }

    public void onEditNCBInfo(){
        log.info("openNCBInfo ::: ");
        if(customerItem != null){
            log.info("customerItem.id {} ",customerItem.getId());
            try{
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/NCBInfo.jsf");
                HttpSession session = FacesUtil.getSession(true);
                session.setAttribute("customerId", customerItem.getId()) ;    // set customerId to NCB information
                return;
            }catch (Exception ex){
                log.info("Exception :: {}",ex);
            }
        }
    }

    // onclick edit button
    public void onEdit(){

    }

    public NCBSummaryView getNcbSum() {
        return ncbSum;
    }

    public void setNcbSum(NCBSummaryView ncbSum) {
        this.ncbSum = ncbSum;
    }

    public List<NCBSummaryView> getNcbSumList() {
        return ncbSumList;
    }

    public void setNcbSumList(List<NCBSummaryView> ncbSumList) {
        this.ncbSumList = ncbSumList;
    }


    //test
    public List<Customer> getCustomerViewTest() {
        return customerViewTest;
    }

    public void setCustomerViewTest(List<Customer> customerViewTest) {
        this.customerViewTest = customerViewTest;
    }

    public Customer getCustomerItem() {
        return customerItem;
    }

    public void setCustomerItem(Customer customerItem) {
        this.customerItem = customerItem;
    }
}
