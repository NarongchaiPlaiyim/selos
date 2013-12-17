package com.clevel.selos.model;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.PreDisbursementView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;

@ViewScoped
@ManagedBean(name = "preDisbursement")
public class PreDisbursement implements Serializable {
    @Inject
    @SELOS
    Logger log;

    @Inject
    @NormalMessage
    Message msg;

    private PreDisbursementView preDisbursementView;

    @Inject
    public PreDisbursement() {

    }

    @PostConstruct
    public void onCreation(){
        init();
    }

    private void init(){
        preDisbursementView = new PreDisbursementView();
    }

    public PreDisbursementView getPreDisbursementView() {
        return preDisbursementView;
    }

    public void setPreDisbursementView(PreDisbursementView preDisbursementView) {
        this.preDisbursementView = preDisbursementView;
    }
}
