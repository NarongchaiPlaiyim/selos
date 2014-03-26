package com.clevel.selos.model.db.master;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "wrk_customer")
public class SearchFirstLastName implements Serializable
{
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name_en")
    private String firstnameenglish;

    @Column(name = "lastname_en")
    private String lastnameenglish;

    @Column(name = "name_th")
    private String firstnametai;

    @Column(name = "lastname_th")
    private String lastnametai;

    @Column(name = "workcase_id")
    private Integer workCaseId;

    @Column(name = "workcase_prescreen_id")
    private Integer wrokCasePreScreenId;

    @Column(name ="relation_id")
    private Integer relationId;

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public Integer getWorkCaseId() {
        return workCaseId;
    }

    public void setWorkCaseId(Integer workCaseId) {
        this.workCaseId = workCaseId;
    }

    public Integer getWrokCasePreScreenId() {
        return wrokCasePreScreenId;
    }

    public void setWrokCasePreScreenId(Integer wrokCasePreScreenId) {
        this.wrokCasePreScreenId = wrokCasePreScreenId;
    }

    public String getLastnameenglish() {
        return lastnameenglish;
    }

    public void setLastnameenglish(String lastnameenglish) {
        this.lastnameenglish = lastnameenglish;
    }

    public String getFirstnametai() {
        return firstnametai;
    }

    public void setFirstnametai(String firstnametai) {
        this.firstnametai = firstnametai;
    }

    public String getLastnametai() {
        return lastnametai;
    }

    public void setLastnametai(String lastnametai) {
        this.lastnametai = lastnametai;
    }

    public String getFirstnameenglish() {
        return firstnameenglish;
    }

    public void setFirstnameenglish(String firstnameenglish) {
        this.firstnameenglish = firstnameenglish;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
