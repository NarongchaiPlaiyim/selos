package com.clevel.selos.model.viewmodel;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: sahawat
 * Date: 16/8/2556
 * Time: 14:31 น.
 * To change this template use File | Settings | File Templates.
 */
public class CardTypeView implements Serializable{

    public CardTypeView(String name, String value){
        this.name=name;
        this.value=value;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String name;
    private String value;
}
