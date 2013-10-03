package com.clevel.selos.model.db.relation;

import com.clevel.selos.model.db.master.CreditType;
import com.clevel.selos.model.db.master.ProductProgram;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rel_prdprogram_credittype")
public class PrdProgramToCreditType implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @OneToOne
    @JoinColumn(name="productprogram_id")
    private ProductProgram productProgram;
    @OneToOne
    @JoinColumn(name="credittype_id")
    private CreditType creditType;
    @Column(name="active")
    private int active;

    public PrdProgramToCreditType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("productProgram", productProgram).
                append("creditType", creditType).
                append("active", active).
                toString();
    }
}
