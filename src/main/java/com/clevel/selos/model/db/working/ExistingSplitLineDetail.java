package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.ProductProgram;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "wrk_exist_split_line_detail")
public class ExistingSplitLineDetail  implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_EXT_SPLIT_LINE_DET_ID", sequenceName = "SEQ_WRK_EXT_SPLIT_LINE_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_EXT_SPLIT_LINE_DET_ID")
    private long id;

    @Column(name = "no")
    private int no;

    @OneToOne
    @JoinColumn(name = "product_program_id")

    private ProductProgram productProgram;
    @Column(name = "limit")
    private BigDecimal limit;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;


    @OneToOne
    @JoinColumn(name = "create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name = "modify_user_id")
    private User modifyBy;

    @ManyToOne
    @JoinColumn(name = "existing_credit_detail_id")
    private ExistingCreditDetail existingCreditDetail;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public ProductProgram getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(ProductProgram productProgram) {
        this.productProgram = productProgram;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
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

    public ExistingCreditDetail getExistingCreditDetail() {
        return existingCreditDetail;
    }

    public void setExistingCreditDetail(ExistingCreditDetail existingCreditDetail) {
        this.existingCreditDetail = existingCreditDetail;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("no", no)
                .append("productProgram", productProgram)
                .append("limit", limit)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("existingCreditDetail",existingCreditDetail)
                .toString();
    }
}
