package com.clevel.selos.model.view;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.AgreementSingView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;

@ViewScoped
@ManagedBean(name = "agreementSign")
public class AgreementSign implements Serializable {
    @Inject
    @SELOS
    Logger log;

    @Inject
    @NormalMessage
    Message msg;

    //*** View ***//
    private AgreementSingView agreementSingView;

    @Inject
    public AgreementSign() {

    }

    @PostConstruct
    public void onCreation(){
        init();
    }

    private void init(){
        agreementSingView = new AgreementSingView();
    }

    public AgreementSingView getAgreementSingView() {
        return agreementSingView;
    }

    public void setAgreementSingView(AgreementSingView agreementSingView) {
        this.agreementSingView = agreementSingView;
    }
}
