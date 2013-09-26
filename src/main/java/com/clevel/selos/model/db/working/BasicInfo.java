package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="wrk_basicinfo")
public class BasicInfo implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_BASIC_INFO_ID", sequenceName="SEQ_WRK_BASIC_INFO_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_BASIC_INFO_ID")
    private long id;

    @OneToOne
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;
}
