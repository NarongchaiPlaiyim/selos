package com.clevel.selos.model.db.working;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_new_sub_col_relate")
public class NewCollateralSubRelate implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_NEW_SUB_COL_REL_ID", sequenceName = "SEQ_WRK_NEW_SUB_COL_REL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_NEW_SUB_COL_REL_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "new_collateral_sub_id")
    private NewCollateralSubDetail newCollateralSubDetail;

    @OneToOne
    @JoinColumn(name = "relate_collateral_sub_id")
    private NewCollateralSubDetail newCollateralSubDetailRel;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public NewCollateralSubDetail getNewCollateralSubDetail() {
        return newCollateralSubDetail;
    }

    public void setNewCollateralSubDetail(NewCollateralSubDetail newCollateralSubDetail) {
        this.newCollateralSubDetail = newCollateralSubDetail;
    }

    public NewCollateralSubDetail getNewCollateralSubDetailRel() {
        return newCollateralSubDetailRel;
    }

    public void setNewCollateralSubDetailRel(NewCollateralSubDetail newCollateralSubDetailRel) {
        this.newCollateralSubDetailRel = newCollateralSubDetailRel;
    }
}
