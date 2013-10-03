package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.ProductGroup;
import com.clevel.selos.model.db.master.Province;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="wrk_prescreen")
public class Prescreen implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_PRESCREEN_ID", sequenceName="SEQ_WRK_PRESCREEN_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_PRESCREEN_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name="workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @OneToOne
    @JoinColumn(name="product_group_id")
    private ProductGroup productGroup;

    @Temporal(TemporalType.DATE)
    @Column(name="expected_submit_date")
    private Date expectedSubmitDate;

    @OneToOne
    @JoinColumn(name="business_location_id")
    private Province businessLocation;

    @Temporal(TemporalType.DATE)
    @Column(name="register_date")
    private Date registerDate;

    @Column(name="tcg")
    private boolean tcg;

    @Column(name="refinance")
    private boolean refinance;

    @OneToOne
    @JoinColumn(name="bdm_checker_id")
    private User bdmChecker;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name="create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name="modify_user_id")
    private User modifyBy;

    public  Prescreen(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCasePrescreen getWorkCasePrescreen() {
        return workCasePrescreen;
    }

    public void setWorkCasePrescreen(WorkCasePrescreen workCasePrescreen) {
        this.workCasePrescreen = workCasePrescreen;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public Date getExpectedSubmitDate() {
        return expectedSubmitDate;
    }

    public void setExpectedSubmitDate(Date expectedSubmitDate) {
        this.expectedSubmitDate = expectedSubmitDate;
    }

    public Province getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(Province businessLocation) {
        this.businessLocation = businessLocation;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public boolean isTcg() {
        return tcg;
    }

    public void setTcg(boolean tcg) {
        this.tcg = tcg;
    }

    public boolean isRefinance() {
        return refinance;
    }

    public void setRefinance(boolean refinance) {
        this.refinance = refinance;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCasePrescreen", workCasePrescreen)
                .append("productGroup", productGroup)
                .append("expectedSubmitDate", expectedSubmitDate)
                .append("businessLocation", businessLocation)
                .append("registerDate", registerDate)
                .append("tcg", tcg)
                .append("refinance", refinance)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
