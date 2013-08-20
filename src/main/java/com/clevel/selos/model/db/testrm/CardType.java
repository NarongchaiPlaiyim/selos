package com.clevel.selos.model.db.testrm;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: sahawat
 * Date: 16/8/2556
 * Time: 16:45 น.
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "CARDTYPE",schema = "SCOTT")
public class CardType {

    @Id
    @Column(name = "NAME")
    private String name;
    @Column(name = "VALUE")
    private String value;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
