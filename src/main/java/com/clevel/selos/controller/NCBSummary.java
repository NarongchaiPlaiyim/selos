package com.clevel.selos.controller;


import com.clevel.selos.businesscontrol.NCBInfoControl;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.NCBInfoView;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.transform.NCBTransform;
import com.clevel.selos.util.FacesUtil;
import org.joda.time.DateTime;
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
import java.util.Date;
import java.util.List;


@ViewScoped
@ManagedBean(name = "ncbSummary")
public class NCBSummary implements Serializable {
    @Inject
    @SELOS
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
    @Inject
    private NCBTransform ncbTransform;

    @Inject
    private NCBInfoControl ncbInfoControl;

    private long workCaseId;
    private List<NCBInfoView> ncbSumViewList;
    private NCBInfoView ncbView;
    private List<Customer> customerView;
    private NCBInfoView ncbSummaryViewItem;
    private User user;
    private Date date;

    public NCBSummary() {
    }

    @PostConstruct
    public void onCreation() {

        log.info("onCreation.");

        HttpSession session = FacesUtil.getSession(true);

        //session.setAttribute("workCaseId", new Long(1));    // ไว้เทส set workCaseId ส่งมาจาก inbox

        user = (User)session.getAttribute("user");
        date = DateTime.now().toDate();

        if (session.getAttribute("workCaseId") != null) {
            workCaseId = Long.parseLong(session.getAttribute("workCaseId").toString());
            log.info("workCaseId :: {} ", workCaseId);
        }

        //Change to use business control - AS
        /*try {
            customerView = customerDAO.findByWorkCaseId(workCaseId);
        } catch (Exception e) {
            log.error("customerDAO.findAll  error ::: {}", e.getMessage());
        }

        if (customerView != null) {
            log.error("customerView.size :: {}", customerView.size());
            for (int i = 0; i < customerView.size(); i++) {

                if (customerView.get(i).getNcb() != null) {
                    ncbView = ncbTransform.transformToView(customerView.get(i).getNcb());
                    log.info("ncbView :: {} ", ncbView.toString());

                    if (ncbSumViewList == null) {
                        ncbSumViewList = new ArrayList<NCBInfoView>();
                    }

                    ncbSumViewList.add(i, ncbView);

                }
            }

            log.info("ncbSumViewList :: {}", ncbSumViewList.size());
        }*/
        ncbSumViewList = ncbInfoControl.getNCBInfoViewByWorkCaseId(workCaseId);

        if (customerView == null) {
            customerView = new ArrayList<Customer>();
        }

        if (ncbSumViewList == null) {
            ncbSumViewList = new ArrayList<NCBInfoView>();
        }


    }

    public void onOpenNCBInfo() {
        log.info("openNCBInfo ::: ");

        if (ncbSummaryViewItem != null) {
            log.info("ncbSummaryViewItem.id {} ", ncbSummaryViewItem.getId());
            try {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/site/NCBInfo.jsf");
                HttpSession session = FacesUtil.getSession(true);
                session.setAttribute("customerId", ncbSummaryViewItem.getCustomer().getId());    // set customerId to NCB information
                return;
            } catch (Exception ex) {
                log.info("Exception :: {}", ex);
            }
        }
    }


    public NCBInfoView getNcbView() {
        return ncbView;
    }

    public void setNcbView(NCBInfoView ncbView) {
        this.ncbView = ncbView;
    }

    public List<NCBInfoView> getNcbSumViewList() {
        return ncbSumViewList;
    }

    public void setNcbSumViewList(List<NCBInfoView> ncbSumViewList) {
        this.ncbSumViewList = ncbSumViewList;
    }

    public List<Customer> getCustomerView() {
        return customerView;
    }

    public void setCustomerView(List<Customer> customerView) {
        this.customerView = customerView;
    }

    public NCBInfoView getNcbSummaryViewItem() {
        return ncbSummaryViewItem;
    }

    public void setNcbSummaryViewItem(NCBInfoView ncbSummaryViewItem) {
        this.ncbSummaryViewItem = ncbSummaryViewItem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
