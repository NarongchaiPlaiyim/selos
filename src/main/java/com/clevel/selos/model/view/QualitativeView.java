package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.QualityLevel;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class QualitativeView implements Serializable {
    private boolean properties_p1;
    private boolean properties_p2;
    private boolean properties_p3;
    private boolean properties_sm1;
    private boolean properties_sm2;
    private boolean properties_sm3;
    private boolean properties_sm4;
    private boolean properties_sm5;
    private boolean properties_sm6;
    private boolean properties_sm7;
    private boolean properties_sm8;
    private boolean properties_sm9;
    private boolean properties_sm10;
    private boolean properties_sm11;
    private boolean properties_sm12;
    private boolean properties_sm13;
    private boolean properties_ss1;
    private boolean properties_ss2;
    private boolean properties_ss3;
    private boolean properties_ss4;
    private boolean properties_ss5;
    private boolean properties_ss6;
    private boolean properties_ss7;
    private boolean properties_ss8;
    private boolean properties_ss9;
    private boolean properties_ss10;
    private boolean properties_ss11;
    private boolean properties_ss12;
    private boolean properties_ss13;
    private boolean properties_ss14;
    private boolean properties_ss15;
    private boolean properties_ss16;
    private boolean properties_ss17;
    private boolean properties_ss18;
    private boolean properties_ss19;
    private boolean properties_ss20;
    private boolean properties_ss21;
    private boolean properties_ss22;
    private boolean properties_d1;
    private boolean properties_d2;
    private boolean properties_d3;
    private boolean properties_d4;
    private boolean properties_d5;
    private boolean properties_d6;
    private boolean properties_d7;
    private boolean properties_d8;
    private boolean properties_d9;
    private boolean properties_d10;
    private boolean properties_d11;
    private boolean properties_d12;
    private boolean properties_d13;
    private boolean properties_d14;
    private boolean properties_d15;
    private boolean properties_d16;
    private boolean properties_d17;
    private boolean properties_d18;
    private boolean properties_d19;
    private boolean properties_d20;
    private boolean properties_dl1;
    private boolean properties_dl2;
    private boolean properties_dl3;
    private boolean properties_dl4;
    private boolean properties_dl5;
    private boolean properties_dl6;
    private boolean properties_dl7;
    private boolean properties_dl8;
    private boolean properties_dl9;
    private boolean properties_dl10;
    private boolean properties_dl11;
    private boolean properties_dl12;
    private boolean properties_dl13;

    private long id;
    private boolean active;
    private String qualityResult;
    private String reason;
    private QualityLevel qualityLevel;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;


    public QualitativeView() {
        qualityLevel = new QualityLevel();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public QualityLevel getQualityLevel() {
        return qualityLevel;
    }

    public void setQualityLevel(QualityLevel qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    public boolean isProperties_p1() {
        return properties_p1;
    }

    public void setProperties_p1(boolean properties_p1) {
        this.properties_p1 = properties_p1;
    }

    public boolean isProperties_p2() {
        return properties_p2;
    }

    public void setProperties_p2(boolean properties_p2) {
        this.properties_p2 = properties_p2;
    }

    public boolean isProperties_p3() {
        return properties_p3;
    }

    public void setProperties_p3(boolean properties_p3) {
        this.properties_p3 = properties_p3;
    }

    public boolean isProperties_sm1() {
        return properties_sm1;
    }

    public void setProperties_sm1(boolean properties_sm1) {
        this.properties_sm1 = properties_sm1;
    }

    public boolean isProperties_sm2() {
        return properties_sm2;
    }

    public void setProperties_sm2(boolean properties_sm2) {
        this.properties_sm2 = properties_sm2;
    }

    public boolean isProperties_sm3() {
        return properties_sm3;
    }

    public void setProperties_sm3(boolean properties_sm3) {
        this.properties_sm3 = properties_sm3;
    }

    public boolean isProperties_sm4() {
        return properties_sm4;
    }

    public void setProperties_sm4(boolean properties_sm4) {
        this.properties_sm4 = properties_sm4;
    }

    public boolean isProperties_sm5() {
        return properties_sm5;
    }

    public void setProperties_sm5(boolean properties_sm5) {
        this.properties_sm5 = properties_sm5;
    }

    public boolean isProperties_sm6() {
        return properties_sm6;
    }

    public void setProperties_sm6(boolean properties_sm6) {
        this.properties_sm6 = properties_sm6;
    }

    public boolean isProperties_sm7() {
        return properties_sm7;
    }

    public void setProperties_sm7(boolean properties_sm7) {
        this.properties_sm7 = properties_sm7;
    }

    public boolean isProperties_sm8() {
        return properties_sm8;
    }

    public void setProperties_sm8(boolean properties_sm8) {
        this.properties_sm8 = properties_sm8;
    }

    public boolean isProperties_sm9() {
        return properties_sm9;
    }

    public void setProperties_sm9(boolean properties_sm9) {
        this.properties_sm9 = properties_sm9;
    }

    public boolean isProperties_sm10() {
        return properties_sm10;
    }

    public void setProperties_sm10(boolean properties_sm10) {
        this.properties_sm10 = properties_sm10;
    }

    public boolean isProperties_sm11() {
        return properties_sm11;
    }

    public void setProperties_sm11(boolean properties_sm11) {
        this.properties_sm11 = properties_sm11;
    }

    public boolean isProperties_sm12() {
        return properties_sm12;
    }

    public void setProperties_sm12(boolean properties_sm12) {
        this.properties_sm12 = properties_sm12;
    }

    public boolean isProperties_sm13() {
        return properties_sm13;
    }

    public void setProperties_sm13(boolean properties_sm13) {
        this.properties_sm13 = properties_sm13;
    }

    public boolean isProperties_ss1() {
        return properties_ss1;
    }

    public void setProperties_ss1(boolean properties_ss1) {
        this.properties_ss1 = properties_ss1;
    }

    public boolean isProperties_ss2() {
        return properties_ss2;
    }

    public void setProperties_ss2(boolean properties_ss2) {
        this.properties_ss2 = properties_ss2;
    }

    public boolean isProperties_ss3() {
        return properties_ss3;
    }

    public void setProperties_ss3(boolean properties_ss3) {
        this.properties_ss3 = properties_ss3;
    }

    public boolean isProperties_ss4() {
        return properties_ss4;
    }

    public void setProperties_ss4(boolean properties_ss4) {
        this.properties_ss4 = properties_ss4;
    }

    public boolean isProperties_ss5() {
        return properties_ss5;
    }

    public void setProperties_ss5(boolean properties_ss5) {
        this.properties_ss5 = properties_ss5;
    }

    public boolean isProperties_ss6() {
        return properties_ss6;
    }

    public void setProperties_ss6(boolean properties_ss6) {
        this.properties_ss6 = properties_ss6;
    }

    public boolean isProperties_ss7() {
        return properties_ss7;
    }

    public void setProperties_ss7(boolean properties_ss7) {
        this.properties_ss7 = properties_ss7;
    }

    public boolean isProperties_ss8() {
        return properties_ss8;
    }

    public void setProperties_ss8(boolean properties_ss8) {
        this.properties_ss8 = properties_ss8;
    }

    public boolean isProperties_ss9() {
        return properties_ss9;
    }

    public void setProperties_ss9(boolean properties_ss9) {
        this.properties_ss9 = properties_ss9;
    }

    public boolean isProperties_ss10() {
        return properties_ss10;
    }

    public void setProperties_ss10(boolean properties_ss10) {
        this.properties_ss10 = properties_ss10;
    }

    public boolean isProperties_ss11() {
        return properties_ss11;
    }

    public void setProperties_ss11(boolean properties_ss11) {
        this.properties_ss11 = properties_ss11;
    }

    public boolean isProperties_ss12() {
        return properties_ss12;
    }

    public void setProperties_ss12(boolean properties_ss12) {
        this.properties_ss12 = properties_ss12;
    }

    public boolean isProperties_ss13() {
        return properties_ss13;
    }

    public void setProperties_ss13(boolean properties_ss13) {
        this.properties_ss13 = properties_ss13;
    }

    public boolean isProperties_ss14() {
        return properties_ss14;
    }

    public void setProperties_ss14(boolean properties_ss14) {
        this.properties_ss14 = properties_ss14;
    }

    public boolean isProperties_ss15() {
        return properties_ss15;
    }

    public void setProperties_ss15(boolean properties_ss15) {
        this.properties_ss15 = properties_ss15;
    }

    public boolean isProperties_ss16() {
        return properties_ss16;
    }

    public void setProperties_ss16(boolean properties_ss16) {
        this.properties_ss16 = properties_ss16;
    }

    public boolean isProperties_ss17() {
        return properties_ss17;
    }

    public void setProperties_ss17(boolean properties_ss17) {
        this.properties_ss17 = properties_ss17;
    }

    public boolean isProperties_ss18() {
        return properties_ss18;
    }

    public void setProperties_ss18(boolean properties_ss18) {
        this.properties_ss18 = properties_ss18;
    }

    public boolean isProperties_ss19() {
        return properties_ss19;
    }

    public void setProperties_ss19(boolean properties_ss19) {
        this.properties_ss19 = properties_ss19;
    }

    public boolean isProperties_ss20() {
        return properties_ss20;
    }

    public void setProperties_ss20(boolean properties_ss20) {
        this.properties_ss20 = properties_ss20;
    }

    public boolean isProperties_ss21() {
        return properties_ss21;
    }

    public void setProperties_ss21(boolean properties_ss21) {
        this.properties_ss21 = properties_ss21;
    }

    public boolean isProperties_ss22() {
        return properties_ss22;
    }

    public void setProperties_ss22(boolean properties_ss22) {
        this.properties_ss22 = properties_ss22;
    }

    public boolean isProperties_d1() {
        return properties_d1;
    }

    public void setProperties_d1(boolean properties_d1) {
        this.properties_d1 = properties_d1;
    }

    public boolean isProperties_d2() {
        return properties_d2;
    }

    public void setProperties_d2(boolean properties_d2) {
        this.properties_d2 = properties_d2;
    }

    public boolean isProperties_d3() {
        return properties_d3;
    }

    public void setProperties_d3(boolean properties_d3) {
        this.properties_d3 = properties_d3;
    }

    public boolean isProperties_d4() {
        return properties_d4;
    }

    public void setProperties_d4(boolean properties_d4) {
        this.properties_d4 = properties_d4;
    }

    public boolean isProperties_d5() {
        return properties_d5;
    }

    public void setProperties_d5(boolean properties_d5) {
        this.properties_d5 = properties_d5;
    }

    public boolean isProperties_d6() {
        return properties_d6;
    }

    public void setProperties_d6(boolean properties_d6) {
        this.properties_d6 = properties_d6;
    }

    public boolean isProperties_d7() {
        return properties_d7;
    }

    public void setProperties_d7(boolean properties_d7) {
        this.properties_d7 = properties_d7;
    }

    public boolean isProperties_d8() {
        return properties_d8;
    }

    public void setProperties_d8(boolean properties_d8) {
        this.properties_d8 = properties_d8;
    }

    public boolean isProperties_d9() {
        return properties_d9;
    }

    public void setProperties_d9(boolean properties_d9) {
        this.properties_d9 = properties_d9;
    }

    public boolean isProperties_d10() {
        return properties_d10;
    }

    public void setProperties_d10(boolean properties_d10) {
        this.properties_d10 = properties_d10;
    }

    public boolean isProperties_d11() {
        return properties_d11;
    }

    public void setProperties_d11(boolean properties_d11) {
        this.properties_d11 = properties_d11;
    }

    public boolean isProperties_d12() {
        return properties_d12;
    }

    public void setProperties_d12(boolean properties_d12) {
        this.properties_d12 = properties_d12;
    }

    public boolean isProperties_d13() {
        return properties_d13;
    }

    public void setProperties_d13(boolean properties_d13) {
        this.properties_d13 = properties_d13;
    }

    public boolean isProperties_d14() {
        return properties_d14;
    }

    public void setProperties_d14(boolean properties_d14) {
        this.properties_d14 = properties_d14;
    }

    public boolean isProperties_d15() {
        return properties_d15;
    }

    public void setProperties_d15(boolean properties_d15) {
        this.properties_d15 = properties_d15;
    }

    public boolean isProperties_d16() {
        return properties_d16;
    }

    public void setProperties_d16(boolean properties_d16) {
        this.properties_d16 = properties_d16;
    }

    public boolean isProperties_d17() {
        return properties_d17;
    }

    public void setProperties_d17(boolean properties_d17) {
        this.properties_d17 = properties_d17;
    }

    public boolean isProperties_d18() {
        return properties_d18;
    }

    public void setProperties_d18(boolean properties_d18) {
        this.properties_d18 = properties_d18;
    }

    public boolean isProperties_d19() {
        return properties_d19;
    }

    public void setProperties_d19(boolean properties_d19) {
        this.properties_d19 = properties_d19;
    }

    public boolean isProperties_d20() {
        return properties_d20;
    }

    public void setProperties_d20(boolean properties_d20) {
        this.properties_d20 = properties_d20;
    }

    public boolean isProperties_dl1() {
        return properties_dl1;
    }

    public void setProperties_dl1(boolean properties_dl1) {
        this.properties_dl1 = properties_dl1;
    }

    public boolean isProperties_dl2() {
        return properties_dl2;
    }

    public void setProperties_dl2(boolean properties_dl2) {
        this.properties_dl2 = properties_dl2;
    }

    public boolean isProperties_dl3() {
        return properties_dl3;
    }

    public void setProperties_dl3(boolean properties_dl3) {
        this.properties_dl3 = properties_dl3;
    }

    public boolean isProperties_dl4() {
        return properties_dl4;
    }

    public void setProperties_dl4(boolean properties_dl4) {
        this.properties_dl4 = properties_dl4;
    }

    public boolean isProperties_dl5() {
        return properties_dl5;
    }

    public void setProperties_dl5(boolean properties_dl5) {
        this.properties_dl5 = properties_dl5;
    }

    public boolean isProperties_dl6() {
        return properties_dl6;
    }

    public void setProperties_dl6(boolean properties_dl6) {
        this.properties_dl6 = properties_dl6;
    }

    public boolean isProperties_dl7() {
        return properties_dl7;
    }

    public void setProperties_dl7(boolean properties_dl7) {
        this.properties_dl7 = properties_dl7;
    }

    public boolean isProperties_dl8() {
        return properties_dl8;
    }

    public void setProperties_dl8(boolean properties_dl8) {
        this.properties_dl8 = properties_dl8;
    }

    public boolean isProperties_dl9() {
        return properties_dl9;
    }

    public void setProperties_dl9(boolean properties_dl9) {
        this.properties_dl9 = properties_dl9;
    }

    public boolean isProperties_dl10() {
        return properties_dl10;
    }

    public void setProperties_dl10(boolean properties_dl10) {
        this.properties_dl10 = properties_dl10;
    }

    public boolean isProperties_dl11() {
        return properties_dl11;
    }

    public void setProperties_dl11(boolean properties_dl11) {
        this.properties_dl11 = properties_dl11;
    }

    public boolean isProperties_dl12() {
        return properties_dl12;
    }

    public void setProperties_dl12(boolean properties_dl12) {
        this.properties_dl12 = properties_dl12;
    }

    public boolean isProperties_dl13() {
        return properties_dl13;
    }

    public void setProperties_dl13(boolean properties_dl13) {
        this.properties_dl13 = properties_dl13;
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

    public String getQualityResult() {
        return qualityResult;
    }

    public void setQualityResult(String qualityResult) {
        this.qualityResult = qualityResult;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("properties_p1", properties_p1)
                .append("properties_p2", properties_p2)
                .append("properties_p3", properties_p3)
                .append("properties_sm1", properties_sm1)
                .append("properties_sm2", properties_sm2)
                .append("properties_sm3", properties_sm3)
                .append("properties_sm4", properties_sm4)
                .append("properties_sm5", properties_sm5)
                .append("properties_sm6", properties_sm6)
                .append("properties_sm7", properties_sm7)
                .append("properties_sm8", properties_sm8)
                .append("properties_sm9", properties_sm9)
                .append("properties_sm10", properties_sm10)
                .append("properties_sm11", properties_sm11)
                .append("properties_sm12", properties_sm12)
                .append("properties_sm13", properties_sm13)
                .append("properties_ss1", properties_ss1)
                .append("properties_ss2", properties_ss2)
                .append("properties_ss3", properties_ss3)
                .append("properties_ss4", properties_ss4)
                .append("properties_ss5", properties_ss5)
                .append("properties_ss6", properties_ss6)
                .append("properties_ss7", properties_ss7)
                .append("properties_ss8", properties_ss8)
                .append("properties_ss9", properties_ss9)
                .append("properties_ss10", properties_ss10)
                .append("properties_ss11", properties_ss11)
                .append("properties_ss12", properties_ss12)
                .append("properties_ss13", properties_ss13)
                .append("properties_ss14", properties_ss14)
                .append("properties_ss15", properties_ss15)
                .append("properties_ss16", properties_ss16)
                .append("properties_ss17", properties_ss17)
                .append("properties_ss18", properties_ss18)
                .append("properties_ss19", properties_ss19)
                .append("properties_ss20", properties_ss20)
                .append("properties_ss21", properties_ss21)
                .append("properties_ss22", properties_ss22)
                .append("properties_d1", properties_d1)
                .append("properties_d2", properties_d2)
                .append("properties_d3", properties_d3)
                .append("properties_d4", properties_d4)
                .append("properties_d5", properties_d5)
                .append("properties_d6", properties_d6)
                .append("properties_d7", properties_d7)
                .append("properties_d8", properties_d8)
                .append("properties_d9", properties_d9)
                .append("properties_d10", properties_d10)
                .append("properties_d11", properties_d11)
                .append("properties_d12", properties_d12)
                .append("properties_d13", properties_d13)
                .append("properties_d14", properties_d14)
                .append("properties_d15", properties_d15)
                .append("properties_d16", properties_d16)
                .append("properties_d17", properties_d17)
                .append("properties_d18", properties_d18)
                .append("properties_d19", properties_d19)
                .append("properties_d20", properties_d20)
                .append("properties_dl1", properties_dl1)
                .append("properties_dl2", properties_dl2)
                .append("properties_dl3", properties_dl3)
                .append("properties_dl4", properties_dl4)
                .append("properties_dl5", properties_dl5)
                .append("properties_dl6", properties_dl6)
                .append("properties_dl7", properties_dl7)
                .append("properties_dl8", properties_dl8)
                .append("properties_dl9", properties_dl9)
                .append("properties_dl10", properties_dl10)
                .append("properties_dl11", properties_dl11)
                .append("properties_dl12", properties_dl12)
                .append("properties_dl13", properties_dl13)
                .append("id", id)
                .append("active", active)
                .append("reason", reason)
                .append("qualityLevel", qualityLevel)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .toString();
    }
}
