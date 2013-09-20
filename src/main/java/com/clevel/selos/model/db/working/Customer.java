package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.DocumentType;
import com.clevel.selos.model.db.master.Relation;
import com.clevel.selos.model.db.master.Title;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="wrk_customer")
public class Customer implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_CUSTOMER_ID", sequenceName="SEQ_WRK_CUSTOMER_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_CUSTOMER_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;

    @ManyToOne
    @JoinColumn(name="workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @OneToOne
    @JoinColumn(name="customertype_id")
    private CustomerEntity customerEntity;

    @OneToOne
    @JoinColumn(name="documenttype_id")
    private DocumentType documentType;

    @Column(name="id_number")
    private String idNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="expire_date")
    private Date expireDate;

    @OneToOne
    @JoinColumn(name="title_id")
    private Title title;

    @Column(name="name_en")
    private String nameEn;

    @Column(name="name_th")
    private String nameTh;

    @Column(name="age")
    private int age;

    @OneToOne
    @JoinColumn(name="individual_id")
    private Individual individual;

    @OneToOne
    @JoinColumn(name="juristic_id")
    private Juristic juristic;

    @OneToOne
    @JoinColumn(name="relation_id")
    private Relation relation;

    public Customer() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameTh() {
        return nameTh;
    }

    public void setNameTh(String nameTh) {
        this.nameTh = nameTh;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public Juristic getJuristic() {
        return juristic;
    }

    public void setJuristic(Juristic juristic) {
        this.juristic = juristic;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCase", workCase)
                .append("customerEntity", customerEntity)
                .append("documentType", documentType)
                .append("idNumber", idNumber)
                .append("expireDate", expireDate)
                .append("title", title)
                .append("nameEn", nameEn)
                .append("nameTh", nameTh)
                .append("age", age)
                .append("individual", individual)
                .append("juristic", juristic)
                .append("relation", relation)
                .toString();
    }
}
