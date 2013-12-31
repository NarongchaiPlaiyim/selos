package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_new_grt_relation")
public class NewGuarantorRelCredit implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_GRT_CREDIT_ID", sequenceName = "SEQ_WRK_NEW_GRT_CREDIT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_GRT_CREDIT_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "new_guarantor_id")
    private NewGuarantorDetail newGuarantorDetail;

    @OneToOne
    @JoinColumn(name = "new_credit_detail_id")
    private NewCreditDetail newCreditDetail;

/*    @Column(name = "guarantee_amount")
    private BigDecimal guaranteeAmount;*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public NewGuarantorDetail getNewGuarantorDetail() {
        return newGuarantorDetail;
    }

    public void setNewGuarantorDetail(NewGuarantorDetail newGuarantorDetail) {
        this.newGuarantorDetail = newGuarantorDetail;
    }

    public NewCreditDetail getNewCreditDetail() {
        return newCreditDetail;
    }

    public void setNewCreditDetail(NewCreditDetail newCreditDetail) {
        this.newCreditDetail = newCreditDetail;
    }
}
