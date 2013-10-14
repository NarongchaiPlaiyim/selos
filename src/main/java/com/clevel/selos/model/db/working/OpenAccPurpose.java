package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.OpenAccountProduct;
import com.clevel.selos.model.db.master.OpenAccountType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="wrk_open_account_purpose")
public class OpenAccPurpose implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_OPEN_ACC_PUR_ID", sequenceName="SEQ_WRK_OPEN_ACC_PUR_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_OPEN_ACC_PUR_ID")
    private long id;

    @OneToOne
    @JoinColumn(name="open_account_id")
    private OpenAccount openAccount;

    @Column(name="purpose_name")
    private String purposeName;
}
