package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Title;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="wrk_children")
public class Children implements Serializable{
    @Id
    @SequenceGenerator(name="SEQ_WRK_CHILDREN_ID", sequenceName="SEQ_WRK_CHILDREN_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_CHILDREN_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customerId;

    @OneToOne
    @JoinColumn(name="title_id")
    private Title title;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    public Children(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
