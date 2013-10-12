package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.BusinessDescription;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="wrk_prescreen_business")
public class PrescreenBusiness implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_PRESCRN_BIZ_ID", sequenceName="SEQ_WRK_PRESCRN_BIZ_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_PRESCRN_BIZ_ID")
    private long id;

    @OneToOne
    @JoinColumn(name = "business_desc_id")
    private BusinessDescription businessDescription;

    @ManyToOne
    @JoinColumn(name = "prescreen_id")
    private Prescreen prescreen;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BusinessDescription getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(BusinessDescription businessDescription) {
        this.businessDescription = businessDescription;
    }

    public Prescreen getPrescreen() {
        return prescreen;
    }

    public void setPrescreen(Prescreen prescreen) {
        this.prescreen = prescreen;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("businessDescription", businessDescription)
                .append("prescreen", prescreen)
                .toString();
    }
}
