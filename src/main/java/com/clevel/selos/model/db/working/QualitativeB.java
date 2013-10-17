package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.QualityLevel;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="wrk_qualitative_b")
public class QualitativeB implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_QUALI_B_ID", sequenceName="SEQ_WRK_QUALI_B_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_QUALI_B_ID")
    private long id;

    @OneToOne
    @JoinColumn(name="workcase_id")
    private WorkCase workCase;

    @Column(name="active")
    private boolean active;

    @Column(name="properties_p1")
    private int properties_p1;

    @Column(name="properties_p2")
    private int properties_p2;

    @Column(name="properties_p3")
    private int properties_p3;

    @Column(name="properties_sm1")
    private int properties_sm1;

    @Column(name="properties_sm2")
    private int properties_sm2;

    @Column(name="properties_sm3")
    private int properties_sm3;

    @Column(name="properties_sm4")
    private int properties_sm4;

    @Column(name="properties_sm5")
    private int properties_sm5;

    @Column(name="properties_sm6")
    private int properties_sm6;

    @Column(name="properties_sm7")
    private int properties_sm7;

    @Column(name="properties_sm8")
    private int properties_sm8;

    @Column(name="properties_sm9")
    private int properties_sm9;

    @Column(name="properties_sm10")
    private int properties_sm10;

    @Column(name="properties_sm11")
    private int properties_sm11;

    @Column(name="properties_sm12")
    private int properties_sm12;

    @Column(name="properties_sm13")
    private int properties_sm13;

    @Column(name="properties_ss1")
    private int properties_ss1;

    @Column(name="properties_ss2")
    private int properties_ss2;

    @Column(name="properties_ss3")
    private int properties_ss3;

    @Column(name="properties_ss4")
    private int properties_ss4;

    @Column(name="properties_ss5")
    private int properties_ss5;

    @Column(name="properties_ss6")
    private int properties_ss6;

    @Column(name="properties_ss7")
    private int properties_ss7;

    @Column(name="properties_ss8")
    private int properties_ss8;

    @Column(name="properties_ss9")
    private int properties_ss9;

    @Column(name="properties_ss10")
    private int properties_ss10;

    @Column(name="properties_ss11")
    private int properties_ss11;

    @Column(name="properties_ss12")
    private int properties_ss12;

    @Column(name="properties_ss13")
    private int properties_ss13;

    @Column(name="properties_ss14")
    private int properties_ss14;

    @Column(name="properties_ss15")
    private int properties_ss15;

    @Column(name="properties_ss16")
    private int properties_ss16;

    @Column(name="properties_ss17")
    private int properties_ss17;

    @Column(name="properties_ss18")
    private int properties_ss18;

    @Column(name="properties_ss19")
    private int properties_ss19;

    @Column(name="properties_ss20")
    private int properties_ss20;

    @Column(name="properties_ss21")
    private int properties_ss21;

    @Column(name="properties_ss22")
    private int properties_ss22;

    @Column(name="properties_d1")
    private int properties_d1;

    @Column(name="properties_d2")
    private int properties_d2;

    @Column(name="properties_d3")
    private int properties_d3;

    @Column(name="properties_d4")
    private int properties_d4;

    @Column(name="properties_d5")
    private int properties_d5;

    @Column(name="properties_d6")
    private int properties_d6;

    @Column(name="properties_d7")
    private int properties_d7;

    @Column(name="properties_d8")
    private int properties_d8;

    @Column(name="properties_d9")
    private int properties_d9;

    @Column(name="properties_d10")
    private int properties_d10;

    @Column(name="properties_d11")
    private int properties_d11;

    @Column(name="properties_d12")
    private int properties_d12;

    @Column(name="properties_d13")
    private int properties_d13;

    @Column(name="properties_d14")
    private int properties_d14;

    @Column(name="properties_d15")
    private int properties_d15;

    @Column(name="properties_d16")
    private int properties_d16;

    @Column(name="properties_d17")
    private int properties_d17;

    @Column(name="properties_d18")
    private int properties_d18;

    @Column(name="properties_d19")
    private int properties_d19;

    @Column(name="properties_dl1")
    private int properties_dl1;

    @Column(name="properties_dl2")
    private int properties_dl2;

    @Column(name="properties_dl3")
    private int properties_dl3;

    @Column(name="properties_dl4")
    private int properties_dl4;

    @Column(name="properties_dl5")
    private int properties_dl5;

    @Column(name="properties_dl6")
    private int properties_dl6;

    @Column(name="properties_dl7")
    private int properties_dl7;

    @Column(name="properties_dl8")
    private int properties_dl8;

    @Column(name="properties_dl9")
    private int properties_dl9;

    @Column(name="properties_dl10")
    private int properties_dl10;

    @Column(name="properties_dl11")
    private int properties_dl11;

    @Column(name="properties_dl12")
    private int properties_dl12;

    @Column(name="properties_dl13")
    private int properties_dl13;

    @OneToOne
    @JoinColumn(name="quality_level_id")
    private QualityLevel qualityLevel;

    @Column(name="quality_result")
    private String qualityResult;

    @Column(name="reason")
    private String reason;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name="create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name="modify_user_id")
    private User modifyBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getProperties_p1() {
        return properties_p1;
    }

    public void setProperties_p1(int properties_p1) {
        this.properties_p1 = properties_p1;
    }

    public int getProperties_p2() {
        return properties_p2;
    }

    public void setProperties_p2(int properties_p2) {
        this.properties_p2 = properties_p2;
    }

    public int getProperties_p3() {
        return properties_p3;
    }

    public void setProperties_p3(int properties_p3) {
        this.properties_p3 = properties_p3;
    }

    public int getProperties_sm1() {
        return properties_sm1;
    }

    public void setProperties_sm1(int properties_sm1) {
        this.properties_sm1 = properties_sm1;
    }

    public int getProperties_sm2() {
        return properties_sm2;
    }

    public void setProperties_sm2(int properties_sm2) {
        this.properties_sm2 = properties_sm2;
    }

    public int getProperties_sm3() {
        return properties_sm3;
    }

    public void setProperties_sm3(int properties_sm3) {
        this.properties_sm3 = properties_sm3;
    }

    public int getProperties_sm4() {
        return properties_sm4;
    }

    public void setProperties_sm4(int properties_sm4) {
        this.properties_sm4 = properties_sm4;
    }

    public int getProperties_sm5() {
        return properties_sm5;
    }

    public void setProperties_sm5(int properties_sm5) {
        this.properties_sm5 = properties_sm5;
    }

    public int getProperties_sm6() {
        return properties_sm6;
    }

    public void setProperties_sm6(int properties_sm6) {
        this.properties_sm6 = properties_sm6;
    }

    public int getProperties_sm7() {
        return properties_sm7;
    }

    public void setProperties_sm7(int properties_sm7) {
        this.properties_sm7 = properties_sm7;
    }

    public int getProperties_sm8() {
        return properties_sm8;
    }

    public void setProperties_sm8(int properties_sm8) {
        this.properties_sm8 = properties_sm8;
    }

    public int getProperties_sm9() {
        return properties_sm9;
    }

    public void setProperties_sm9(int properties_sm9) {
        this.properties_sm9 = properties_sm9;
    }

    public int getProperties_sm10() {
        return properties_sm10;
    }

    public void setProperties_sm10(int properties_sm10) {
        this.properties_sm10 = properties_sm10;
    }

    public int getProperties_sm11() {
        return properties_sm11;
    }

    public void setProperties_sm11(int properties_sm11) {
        this.properties_sm11 = properties_sm11;
    }

    public int getProperties_sm12() {
        return properties_sm12;
    }

    public void setProperties_sm12(int properties_sm12) {
        this.properties_sm12 = properties_sm12;
    }

    public int getProperties_sm13() {
        return properties_sm13;
    }

    public void setProperties_sm13(int properties_sm13) {
        this.properties_sm13 = properties_sm13;
    }

    public int getProperties_ss1() {
        return properties_ss1;
    }

    public void setProperties_ss1(int properties_ss1) {
        this.properties_ss1 = properties_ss1;
    }

    public int getProperties_ss2() {
        return properties_ss2;
    }

    public void setProperties_ss2(int properties_ss2) {
        this.properties_ss2 = properties_ss2;
    }

    public int getProperties_ss3() {
        return properties_ss3;
    }

    public void setProperties_ss3(int properties_ss3) {
        this.properties_ss3 = properties_ss3;
    }

    public int getProperties_ss4() {
        return properties_ss4;
    }

    public void setProperties_ss4(int properties_ss4) {
        this.properties_ss4 = properties_ss4;
    }

    public int getProperties_ss5() {
        return properties_ss5;
    }

    public void setProperties_ss5(int properties_ss5) {
        this.properties_ss5 = properties_ss5;
    }

    public int getProperties_ss6() {
        return properties_ss6;
    }

    public void setProperties_ss6(int properties_ss6) {
        this.properties_ss6 = properties_ss6;
    }

    public int getProperties_ss7() {
        return properties_ss7;
    }

    public void setProperties_ss7(int properties_ss7) {
        this.properties_ss7 = properties_ss7;
    }

    public int getProperties_ss8() {
        return properties_ss8;
    }

    public void setProperties_ss8(int properties_ss8) {
        this.properties_ss8 = properties_ss8;
    }

    public int getProperties_ss9() {
        return properties_ss9;
    }

    public void setProperties_ss9(int properties_ss9) {
        this.properties_ss9 = properties_ss9;
    }

    public int getProperties_ss10() {
        return properties_ss10;
    }

    public void setProperties_ss10(int properties_ss10) {
        this.properties_ss10 = properties_ss10;
    }

    public int getProperties_ss11() {
        return properties_ss11;
    }

    public void setProperties_ss11(int properties_ss11) {
        this.properties_ss11 = properties_ss11;
    }

    public int getProperties_ss12() {
        return properties_ss12;
    }

    public void setProperties_ss12(int properties_ss12) {
        this.properties_ss12 = properties_ss12;
    }

    public int getProperties_ss13() {
        return properties_ss13;
    }

    public void setProperties_ss13(int properties_ss13) {
        this.properties_ss13 = properties_ss13;
    }

    public int getProperties_ss14() {
        return properties_ss14;
    }

    public void setProperties_ss14(int properties_ss14) {
        this.properties_ss14 = properties_ss14;
    }

    public int getProperties_ss15() {
        return properties_ss15;
    }

    public void setProperties_ss15(int properties_ss15) {
        this.properties_ss15 = properties_ss15;
    }

    public int getProperties_ss16() {
        return properties_ss16;
    }

    public void setProperties_ss16(int properties_ss16) {
        this.properties_ss16 = properties_ss16;
    }

    public int getProperties_ss17() {
        return properties_ss17;
    }

    public void setProperties_ss17(int properties_ss17) {
        this.properties_ss17 = properties_ss17;
    }

    public int getProperties_ss18() {
        return properties_ss18;
    }

    public void setProperties_ss18(int properties_ss18) {
        this.properties_ss18 = properties_ss18;
    }

    public int getProperties_ss19() {
        return properties_ss19;
    }

    public void setProperties_ss19(int properties_ss19) {
        this.properties_ss19 = properties_ss19;
    }

    public int getProperties_ss20() {
        return properties_ss20;
    }

    public void setProperties_ss20(int properties_ss20) {
        this.properties_ss20 = properties_ss20;
    }

    public int getProperties_ss21() {
        return properties_ss21;
    }

    public void setProperties_ss21(int properties_ss21) {
        this.properties_ss21 = properties_ss21;
    }

    public int getProperties_ss22() {
        return properties_ss22;
    }

    public void setProperties_ss22(int properties_ss22) {
        this.properties_ss22 = properties_ss22;
    }

    public int getProperties_d1() {
        return properties_d1;
    }

    public void setProperties_d1(int properties_d1) {
        this.properties_d1 = properties_d1;
    }

    public int getProperties_d2() {
        return properties_d2;
    }

    public void setProperties_d2(int properties_d2) {
        this.properties_d2 = properties_d2;
    }

    public int getProperties_d3() {
        return properties_d3;
    }

    public void setProperties_d3(int properties_d3) {
        this.properties_d3 = properties_d3;
    }

    public int getProperties_d4() {
        return properties_d4;
    }

    public void setProperties_d4(int properties_d4) {
        this.properties_d4 = properties_d4;
    }

    public int getProperties_d5() {
        return properties_d5;
    }

    public void setProperties_d5(int properties_d5) {
        this.properties_d5 = properties_d5;
    }

    public int getProperties_d6() {
        return properties_d6;
    }

    public void setProperties_d6(int properties_d6) {
        this.properties_d6 = properties_d6;
    }

    public int getProperties_d7() {
        return properties_d7;
    }

    public void setProperties_d7(int properties_d7) {
        this.properties_d7 = properties_d7;
    }

    public int getProperties_d8() {
        return properties_d8;
    }

    public void setProperties_d8(int properties_d8) {
        this.properties_d8 = properties_d8;
    }

    public int getProperties_d9() {
        return properties_d9;
    }

    public void setProperties_d9(int properties_d9) {
        this.properties_d9 = properties_d9;
    }

    public int getProperties_d10() {
        return properties_d10;
    }

    public void setProperties_d10(int properties_d10) {
        this.properties_d10 = properties_d10;
    }

    public int getProperties_d11() {
        return properties_d11;
    }

    public void setProperties_d11(int properties_d11) {
        this.properties_d11 = properties_d11;
    }

    public int getProperties_d12() {
        return properties_d12;
    }

    public void setProperties_d12(int properties_d12) {
        this.properties_d12 = properties_d12;
    }

    public int getProperties_d13() {
        return properties_d13;
    }

    public void setProperties_d13(int properties_d13) {
        this.properties_d13 = properties_d13;
    }

    public int getProperties_d14() {
        return properties_d14;
    }

    public void setProperties_d14(int properties_d14) {
        this.properties_d14 = properties_d14;
    }

    public int getProperties_d15() {
        return properties_d15;
    }

    public void setProperties_d15(int properties_d15) {
        this.properties_d15 = properties_d15;
    }

    public int getProperties_d16() {
        return properties_d16;
    }

    public void setProperties_d16(int properties_d16) {
        this.properties_d16 = properties_d16;
    }

    public int getProperties_d17() {
        return properties_d17;
    }

    public void setProperties_d17(int properties_d17) {
        this.properties_d17 = properties_d17;
    }

    public int getProperties_d18() {
        return properties_d18;
    }

    public void setProperties_d18(int properties_d18) {
        this.properties_d18 = properties_d18;
    }

    public int getProperties_d19() {
        return properties_d19;
    }

    public void setProperties_d19(int properties_d19) {
        this.properties_d19 = properties_d19;
    }

    public int getProperties_dl1() {
        return properties_dl1;
    }

    public void setProperties_dl1(int properties_dl1) {
        this.properties_dl1 = properties_dl1;
    }

    public int getProperties_dl2() {
        return properties_dl2;
    }

    public void setProperties_dl2(int properties_dl2) {
        this.properties_dl2 = properties_dl2;
    }

    public int getProperties_dl3() {
        return properties_dl3;
    }

    public void setProperties_dl3(int properties_dl3) {
        this.properties_dl3 = properties_dl3;
    }

    public int getProperties_dl4() {
        return properties_dl4;
    }

    public void setProperties_dl4(int properties_dl4) {
        this.properties_dl4 = properties_dl4;
    }

    public int getProperties_dl5() {
        return properties_dl5;
    }

    public void setProperties_dl5(int properties_dl5) {
        this.properties_dl5 = properties_dl5;
    }

    public int getProperties_dl6() {
        return properties_dl6;
    }

    public void setProperties_dl6(int properties_dl6) {
        this.properties_dl6 = properties_dl6;
    }

    public int getProperties_dl7() {
        return properties_dl7;
    }

    public void setProperties_dl7(int properties_dl7) {
        this.properties_dl7 = properties_dl7;
    }

    public int getProperties_dl8() {
        return properties_dl8;
    }

    public void setProperties_dl8(int properties_dl8) {
        this.properties_dl8 = properties_dl8;
    }

    public int getProperties_dl9() {
        return properties_dl9;
    }

    public void setProperties_dl9(int properties_dl9) {
        this.properties_dl9 = properties_dl9;
    }

    public int getProperties_dl10() {
        return properties_dl10;
    }

    public void setProperties_dl10(int properties_dl10) {
        this.properties_dl10 = properties_dl10;
    }

    public int getProperties_dl11() {
        return properties_dl11;
    }

    public void setProperties_dl11(int properties_dl11) {
        this.properties_dl11 = properties_dl11;
    }

    public int getProperties_dl12() {
        return properties_dl12;
    }

    public void setProperties_dl12(int properties_dl12) {
        this.properties_dl12 = properties_dl12;
    }

    public int getProperties_dl13() {
        return properties_dl13;
    }

    public void setProperties_dl13(int properties_dl13) {
        this.properties_dl13 = properties_dl13;
    }

    public QualityLevel getQualityLevel() {
        return qualityLevel;
    }

    public void setQualityLevel(QualityLevel qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    public String getQualityResult() {
        return qualityResult;
    }

    public void setQualityResult(String qualityResult) {
        this.qualityResult = qualityResult;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
}