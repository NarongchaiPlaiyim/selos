package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "wrk_mandate_doc")
public class MandateDoc implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_MANDATE_ID", sequenceName = "SEQ_WRK_MANDATE_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_MANDATE_ID")
    private long id;

    @OneToOne
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;

    @Column(name = "role")
    private String role;

    @Column(name = "is_completed", columnDefinition = "int default 0")
    private int isCompleted;

    @Column(name = "remark")
    private String remark;

    @OneToMany(mappedBy = "mandateDoc", cascade = CascadeType.ALL)
    private List<MandateDocBRMS> mandateDocBRMSList;

    @OneToMany(mappedBy = "mandateDoc", cascade = CascadeType.ALL)
    private List<MandateDocReason> mandateDocReasonList;

    @OneToMany(mappedBy = "mandateDoc", cascade = CascadeType.ALL)
    private List<MandateDocCust> mandateDocCustList;


}
