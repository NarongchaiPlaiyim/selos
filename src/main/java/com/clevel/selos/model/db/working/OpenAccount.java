package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.OpenAccountProduct;
import com.clevel.selos.model.db.master.OpenAccountType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="wrk_open_account")
public class OpenAccount implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_OPEN_ACC_ID", sequenceName="SEQ_WRK_OPEN_ACC_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_OPEN_ACC_ID")
    private long id;

    @OneToOne
    @JoinColumn(name="basic_info_id")
    private BasicInfo basicInfo;

    @Column(name="account_name")
    private String accountName;

    @OneToOne
    @JoinColumn(name="open_account_type_id")
    private OpenAccountType accountType;

    @OneToOne
    @JoinColumn(name="open_account_product_id")
    private OpenAccountProduct accountProduct;
}
