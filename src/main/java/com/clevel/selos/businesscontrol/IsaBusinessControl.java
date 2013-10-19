package com.clevel.selos.businesscontrol;

import com.clevel.selos.model.view.IsaCreateUserView;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class IsaBusinessControl {

    @Inject
    public IsaBusinessControl(){

    }

    @PostConstruct
    public void onCreate(){

    }

    public void createUser(IsaCreateUserView isaCreateUserView){

        System.out.println(isaCreateUserView.toString());
    }

}
