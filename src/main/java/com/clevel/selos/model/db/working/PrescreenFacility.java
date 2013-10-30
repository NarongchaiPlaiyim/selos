package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.CreditType;
import com.clevel.selos.model.db.master.ProductProgram;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "wrk_prescreen_facility")
public class PrescreenFacility implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_PRESCREEN_FACILITY_ID", sequenceName = "SEQ_WRK_PRESCREEN_FACILITY_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_PRESCREEN_FACILITY_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "prescreen_id")
    private Prescreen prescreen;

    @OneToOne
    @JoinColumn(name = "product_program_id")
    private ProductProgram productProgram;

    @OneToOne
    @JoinColumn(name = "credit_type_id")
    private CreditType creditType;

    @Column(name = "request_amount")
    private BigDecimal requestAmount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Prescreen getPrescreen() {
        return prescreen;
    }

    public void setPrescreen(Prescreen prescreen) {
        this.prescreen = prescreen;
    }

    public ProductProgram getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(ProductProgram productProgram) {
        this.productProgram = productProgram;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    public BigDecimal getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(BigDecimal requestAmount) {
        this.requestAmount = requestAmount;
    }
}
