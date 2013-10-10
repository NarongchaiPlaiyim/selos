package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.QualitativeA;
import com.clevel.selos.model.db.working.QualitativeB;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.QualitativeView;

/**
 * Created with IntelliJ IDEA.
 * User: SUKANDA
 * Date: 30/9/2556
 * Time: 11:23 น.
 * To change this template use File | Settings | File Templates.
 */
public class QualitativeTransform extends Transform {

    public QualitativeA transformQualitativeAToModel(QualitativeView qualitativeAView ,WorkCase workCase){
        QualitativeA qualitativeA = new QualitativeA();

        if(qualitativeAView.getId() != 0){
            qualitativeA.setId(qualitativeAView.getId());
        }

        qualitativeA.setActive(true);
        qualitativeA.setWorkCase(workCase);
        qualitativeA.setCreateBy(qualitativeAView.getCreateBy());
        qualitativeA.setCreateDate(qualitativeAView.getCreateDate());
        qualitativeA.setModifyDate(qualitativeAView.getModifyDate());
        qualitativeA.setModifyBy(qualitativeAView.getModifyBy());
        qualitativeA.setProperties_p1(qualitativeAView.isProperties_p1());
        qualitativeA.setProperties_p2(qualitativeAView.isProperties_p2());
        qualitativeA.setProperties_p3(qualitativeAView.isProperties_p3());
        qualitativeA.setProperties_sm1(qualitativeAView.isProperties_sm1());
        qualitativeA.setProperties_sm2(qualitativeAView.isProperties_sm2());
        qualitativeA.setProperties_sm3(qualitativeAView.isProperties_sm3());
        qualitativeA.setProperties_sm4(qualitativeAView.isProperties_sm4());
        qualitativeA.setProperties_sm5(qualitativeAView.isProperties_sm5());
        qualitativeA.setProperties_sm6(qualitativeAView.isProperties_sm6());
        qualitativeA.setProperties_sm7(qualitativeAView.isProperties_sm7());
        qualitativeA.setProperties_sm8(qualitativeAView.isProperties_sm8());
        qualitativeA.setProperties_sm9(qualitativeAView.isProperties_sm9());
        qualitativeA.setProperties_sm10(qualitativeAView.isProperties_sm10());
        qualitativeA.setProperties_sm11(qualitativeAView.isProperties_sm11());
        qualitativeA.setProperties_sm12(qualitativeAView.isProperties_sm12());
        qualitativeA.setProperties_ss1(qualitativeAView.isProperties_ss1());
        qualitativeA.setProperties_ss2(qualitativeAView.isProperties_ss2());
        qualitativeA.setProperties_ss3(qualitativeAView.isProperties_ss3());
        qualitativeA.setProperties_ss4(qualitativeAView.isProperties_ss4());
        qualitativeA.setProperties_ss5(qualitativeAView.isProperties_ss5());
        qualitativeA.setProperties_ss6(qualitativeAView.isProperties_ss6());
        qualitativeA.setProperties_ss7(qualitativeAView.isProperties_ss7());
        qualitativeA.setProperties_ss8(qualitativeAView.isProperties_ss8());
        qualitativeA.setProperties_ss9(qualitativeAView.isProperties_ss9());
        qualitativeA.setProperties_ss10(qualitativeAView.isProperties_ss10());
        qualitativeA.setProperties_ss11(qualitativeAView.isProperties_ss11());
        qualitativeA.setProperties_ss12(qualitativeAView.isProperties_ss12());
        qualitativeA.setProperties_ss13(qualitativeAView.isProperties_ss13());
        qualitativeA.setProperties_ss14(qualitativeAView.isProperties_ss14());
        qualitativeA.setProperties_ss15(qualitativeAView.isProperties_ss15());
        qualitativeA.setProperties_ss16(qualitativeAView.isProperties_ss16());
        qualitativeA.setProperties_ss17(qualitativeAView.isProperties_ss17());
        qualitativeA.setProperties_ss18(qualitativeAView.isProperties_ss18());
        qualitativeA.setProperties_ss19(qualitativeAView.isProperties_ss19());
        qualitativeA.setProperties_ss20(qualitativeAView.isProperties_ss20());
        qualitativeA.setProperties_d1(qualitativeAView.isProperties_d1());
        qualitativeA.setProperties_d2(qualitativeAView.isProperties_d2());
        qualitativeA.setProperties_d3(qualitativeAView.isProperties_d3());
        qualitativeA.setProperties_d4(qualitativeAView.isProperties_d4());
        qualitativeA.setProperties_d5(qualitativeAView.isProperties_d5());
        qualitativeA.setProperties_d6(qualitativeAView.isProperties_d6());
        qualitativeA.setProperties_d7(qualitativeAView.isProperties_d7());
        qualitativeA.setProperties_d8(qualitativeAView.isProperties_d8());
        qualitativeA.setProperties_d9(qualitativeAView.isProperties_d9());
        qualitativeA.setProperties_d10(qualitativeAView.isProperties_d10());
        qualitativeA.setProperties_d11(qualitativeAView.isProperties_d11());
        qualitativeA.setProperties_d12(qualitativeAView.isProperties_d12());
        qualitativeA.setProperties_d13(qualitativeAView.isProperties_d13());
        qualitativeA.setProperties_d14(qualitativeAView.isProperties_d14());
        qualitativeA.setProperties_d15(qualitativeAView.isProperties_d15());
        qualitativeA.setProperties_d16(qualitativeAView.isProperties_d16());
        qualitativeA.setProperties_d17(qualitativeAView.isProperties_d17());
        qualitativeA.setProperties_d18(qualitativeAView.isProperties_d18());
        qualitativeA.setProperties_d19(qualitativeAView.isProperties_d19());
        qualitativeA.setProperties_d20(qualitativeAView.isProperties_d20());
        qualitativeA.setProperties_dl1(qualitativeAView.isProperties_dl1());
        qualitativeA.setProperties_dl2(qualitativeAView.isProperties_dl2());
        qualitativeA.setProperties_dl3(qualitativeAView.isProperties_dl3());
        qualitativeA.setProperties_dl4(qualitativeAView.isProperties_dl4());
        qualitativeA.setProperties_dl5(qualitativeAView.isProperties_dl5());
        qualitativeA.setProperties_dl6(qualitativeAView.isProperties_dl6());
        qualitativeA.setProperties_dl7(qualitativeAView.isProperties_dl7());
        qualitativeA.setProperties_dl8(qualitativeAView.isProperties_dl8());
        qualitativeA.setProperties_dl9(qualitativeAView.isProperties_dl9());
        qualitativeA.setProperties_dl10(qualitativeAView.isProperties_dl10());
        qualitativeA.setProperties_dl11(qualitativeAView.isProperties_dl11());
        qualitativeA.setProperties_dl12(qualitativeAView.isProperties_dl12());
        qualitativeA.setProperties_dl13(qualitativeAView.isProperties_dl13());
        qualitativeA.setQualityLevel(qualitativeAView.getQualityLevel());
        qualitativeA.setReason(qualitativeAView.getReason());
        qualitativeA.setQualityResult(qualitativeAView.getQualityResult());

        return qualitativeA;
    }

    public QualitativeView transformQualitativeAToView(QualitativeA qualitativeA){
        QualitativeView qualitativeView = new QualitativeView();

        qualitativeView.setId(qualitativeA.getId());
        qualitativeView.setCreateBy(qualitativeA.getCreateBy());
        qualitativeView.setCreateDate(qualitativeA.getCreateDate());
        qualitativeView.setModifyDate(qualitativeA.getModifyDate());
        qualitativeView.setModifyBy(qualitativeA.getModifyBy());
        qualitativeView.setProperties_p1(qualitativeA.isProperties_p1());
        qualitativeView.setProperties_p2(qualitativeA.isProperties_p2());
        qualitativeView.setProperties_p3(qualitativeA.isProperties_p3());
        qualitativeView.setProperties_sm1(qualitativeA.isProperties_sm1());
        qualitativeView.setProperties_sm2(qualitativeA.isProperties_sm2());
        qualitativeView.setProperties_sm3(qualitativeA.isProperties_sm3());
        qualitativeView.setProperties_sm4(qualitativeA.isProperties_sm4());
        qualitativeView.setProperties_sm5(qualitativeA.isProperties_sm5());
        qualitativeView.setProperties_sm6(qualitativeA.isProperties_sm6());
        qualitativeView.setProperties_sm7(qualitativeA.isProperties_sm7());
        qualitativeView.setProperties_sm8(qualitativeA.isProperties_sm8());
        qualitativeView.setProperties_sm9(qualitativeA.isProperties_sm9());
        qualitativeView.setProperties_sm10(qualitativeA.isProperties_sm10());
        qualitativeView.setProperties_sm11(qualitativeA.isProperties_sm11());
        qualitativeView.setProperties_sm12(qualitativeA.isProperties_sm12());
        qualitativeView.setProperties_ss1(qualitativeA.isProperties_ss1());
        qualitativeView.setProperties_ss2(qualitativeA.isProperties_ss2());
        qualitativeView.setProperties_ss3(qualitativeA.isProperties_ss3());
        qualitativeView.setProperties_ss4(qualitativeA.isProperties_ss4());
        qualitativeView.setProperties_ss5(qualitativeA.isProperties_ss5());
        qualitativeView.setProperties_ss6(qualitativeA.isProperties_ss6());
        qualitativeView.setProperties_ss7(qualitativeA.isProperties_ss7());
        qualitativeView.setProperties_ss8(qualitativeA.isProperties_ss8());
        qualitativeView.setProperties_ss9(qualitativeA.isProperties_ss9());
        qualitativeView.setProperties_ss10(qualitativeA.isProperties_ss10());
        qualitativeView.setProperties_ss11(qualitativeA.isProperties_ss11());
        qualitativeView.setProperties_ss12(qualitativeA.isProperties_ss12());
        qualitativeView.setProperties_ss13(qualitativeA.isProperties_ss13());
        qualitativeView.setProperties_ss14(qualitativeA.isProperties_ss14());
        qualitativeView.setProperties_ss15(qualitativeA.isProperties_ss15());
        qualitativeView.setProperties_ss16(qualitativeA.isProperties_ss16());
        qualitativeView.setProperties_ss17(qualitativeA.isProperties_ss17());
        qualitativeView.setProperties_ss18(qualitativeA.isProperties_ss18());
        qualitativeView.setProperties_ss19(qualitativeA.isProperties_ss19());
        qualitativeView.setProperties_ss20(qualitativeA.isProperties_ss20());
        qualitativeView.setProperties_d1(qualitativeA.isProperties_d1());
        qualitativeView.setProperties_d2(qualitativeA.isProperties_d2());
        qualitativeView.setProperties_d3(qualitativeA.isProperties_d3());
        qualitativeView.setProperties_d4(qualitativeA.isProperties_d4());
        qualitativeView.setProperties_d5(qualitativeA.isProperties_d5());
        qualitativeView.setProperties_d6(qualitativeA.isProperties_d6());
        qualitativeView.setProperties_d7(qualitativeA.isProperties_d7());
        qualitativeView.setProperties_d8(qualitativeA.isProperties_d8());
        qualitativeView.setProperties_d9(qualitativeA.isProperties_d9());
        qualitativeView.setProperties_d10(qualitativeA.isProperties_d10());
        qualitativeView.setProperties_d11(qualitativeA.isProperties_d11());
        qualitativeView.setProperties_d12(qualitativeA.isProperties_d12());
        qualitativeView.setProperties_d13(qualitativeA.isProperties_d13());
        qualitativeView.setProperties_d14(qualitativeA.isProperties_d14());
        qualitativeView.setProperties_d15(qualitativeA.isProperties_d15());
        qualitativeView.setProperties_d16(qualitativeA.isProperties_d16());
        qualitativeView.setProperties_d17(qualitativeA.isProperties_d17());
        qualitativeView.setProperties_d18(qualitativeA.isProperties_d18());
        qualitativeView.setProperties_d19(qualitativeA.isProperties_d19());
        qualitativeView.setProperties_d20(qualitativeA.isProperties_d20());
        qualitativeView.setProperties_dl1(qualitativeA.isProperties_dl1());
        qualitativeView.setProperties_dl2(qualitativeA.isProperties_dl2());
        qualitativeView.setProperties_dl3(qualitativeA.isProperties_dl3());
        qualitativeView.setProperties_dl4(qualitativeA.isProperties_dl4());
        qualitativeView.setProperties_dl5(qualitativeA.isProperties_dl5());
        qualitativeView.setProperties_dl6(qualitativeA.isProperties_dl6());
        qualitativeView.setProperties_dl7(qualitativeA.isProperties_dl7());
        qualitativeView.setProperties_dl8(qualitativeA.isProperties_dl8());
        qualitativeView.setProperties_dl9(qualitativeA.isProperties_dl9());
        qualitativeView.setProperties_dl10(qualitativeA.isProperties_dl10());
        qualitativeView.setProperties_dl11(qualitativeA.isProperties_dl11());
        qualitativeView.setProperties_dl12(qualitativeA.isProperties_dl12());
        qualitativeView.setProperties_dl13(qualitativeA.isProperties_dl13());
        qualitativeView.setQualityLevel(qualitativeA.getQualityLevel());
        qualitativeView.setReason(qualitativeA.getReason());
        qualitativeView.setQualityResult(qualitativeA.getQualityResult());

        return qualitativeView;
    }

    public QualitativeB transformQualitativeBToModel(QualitativeView qualitativeBView,WorkCase workCase){
        QualitativeB qualitativeB = new QualitativeB();

        if(qualitativeBView.getId() != 0){
            qualitativeB.setId(qualitativeBView.getId());
        }

        qualitativeB.setActive(true);
        qualitativeB.setWorkCase(workCase);
        qualitativeB.setCreateBy(qualitativeBView.getCreateBy());
        qualitativeB.setCreateDate(qualitativeBView.getCreateDate());
        qualitativeB.setModifyDate(qualitativeBView.getModifyDate());
        qualitativeB.setModifyBy(qualitativeBView.getModifyBy());
        qualitativeB.setProperties_p1(qualitativeBView.isProperties_p1());
        qualitativeB.setProperties_p2(qualitativeBView.isProperties_p2());
        qualitativeB.setProperties_p3(qualitativeBView.isProperties_p3());
        qualitativeB.setProperties_sm1(qualitativeBView.isProperties_sm1());
        qualitativeB.setProperties_sm2(qualitativeBView.isProperties_sm2());
        qualitativeB.setProperties_sm3(qualitativeBView.isProperties_sm3());
        qualitativeB.setProperties_sm4(qualitativeBView.isProperties_sm4());
        qualitativeB.setProperties_sm5(qualitativeBView.isProperties_sm5());
        qualitativeB.setProperties_sm6(qualitativeBView.isProperties_sm6());
        qualitativeB.setProperties_sm7(qualitativeBView.isProperties_sm7());
        qualitativeB.setProperties_sm8(qualitativeBView.isProperties_sm8());
        qualitativeB.setProperties_sm9(qualitativeBView.isProperties_sm9());
        qualitativeB.setProperties_sm10(qualitativeBView.isProperties_sm10());
        qualitativeB.setProperties_sm11(qualitativeBView.isProperties_sm11());
        qualitativeB.setProperties_sm12(qualitativeBView.isProperties_sm12());
        qualitativeB.setProperties_ss1(qualitativeBView.isProperties_ss1());
        qualitativeB.setProperties_ss2(qualitativeBView.isProperties_ss2());
        qualitativeB.setProperties_ss3(qualitativeBView.isProperties_ss3());
        qualitativeB.setProperties_ss4(qualitativeBView.isProperties_ss4());
        qualitativeB.setProperties_ss5(qualitativeBView.isProperties_ss5());
        qualitativeB.setProperties_ss6(qualitativeBView.isProperties_ss6());
        qualitativeB.setProperties_ss7(qualitativeBView.isProperties_ss7());
        qualitativeB.setProperties_ss8(qualitativeBView.isProperties_ss8());
        qualitativeB.setProperties_ss9(qualitativeBView.isProperties_ss9());
        qualitativeB.setProperties_ss10(qualitativeBView.isProperties_ss10());
        qualitativeB.setProperties_ss11(qualitativeBView.isProperties_ss11());
        qualitativeB.setProperties_ss12(qualitativeBView.isProperties_ss12());
        qualitativeB.setProperties_ss13(qualitativeBView.isProperties_ss13());
        qualitativeB.setProperties_ss14(qualitativeBView.isProperties_ss14());
        qualitativeB.setProperties_ss15(qualitativeBView.isProperties_ss15());
        qualitativeB.setProperties_ss16(qualitativeBView.isProperties_ss16());
        qualitativeB.setProperties_ss17(qualitativeBView.isProperties_ss17());
        qualitativeB.setProperties_ss18(qualitativeBView.isProperties_ss18());
        qualitativeB.setProperties_ss19(qualitativeBView.isProperties_ss19());
        qualitativeB.setProperties_ss20(qualitativeBView.isProperties_ss20());
        qualitativeB.setProperties_d1(qualitativeBView.isProperties_d1());
        qualitativeB.setProperties_d2(qualitativeBView.isProperties_d2());
        qualitativeB.setProperties_d3(qualitativeBView.isProperties_d3());
        qualitativeB.setProperties_d4(qualitativeBView.isProperties_d4());
        qualitativeB.setProperties_d5(qualitativeBView.isProperties_d5());
        qualitativeB.setProperties_d6(qualitativeBView.isProperties_d6());
        qualitativeB.setProperties_d7(qualitativeBView.isProperties_d7());
        qualitativeB.setProperties_d8(qualitativeBView.isProperties_d8());
        qualitativeB.setProperties_d9(qualitativeBView.isProperties_d9());
        qualitativeB.setProperties_d10(qualitativeBView.isProperties_d10());
        qualitativeB.setProperties_d11(qualitativeBView.isProperties_d11());
        qualitativeB.setProperties_d12(qualitativeBView.isProperties_d12());
        qualitativeB.setProperties_d13(qualitativeBView.isProperties_d13());
        qualitativeB.setProperties_d14(qualitativeBView.isProperties_d14());
        qualitativeB.setProperties_d15(qualitativeBView.isProperties_d15());
        qualitativeB.setProperties_d16(qualitativeBView.isProperties_d16());
        qualitativeB.setProperties_d17(qualitativeBView.isProperties_d17());
        qualitativeB.setProperties_d18(qualitativeBView.isProperties_d18());
        qualitativeB.setProperties_d19(qualitativeBView.isProperties_d19());
        qualitativeB.setProperties_dl1(qualitativeBView.isProperties_dl1());
        qualitativeB.setProperties_dl2(qualitativeBView.isProperties_dl2());
        qualitativeB.setProperties_dl3(qualitativeBView.isProperties_dl3());
        qualitativeB.setProperties_dl4(qualitativeBView.isProperties_dl4());
        qualitativeB.setProperties_dl5(qualitativeBView.isProperties_dl5());
        qualitativeB.setProperties_dl6(qualitativeBView.isProperties_dl6());
        qualitativeB.setProperties_dl7(qualitativeBView.isProperties_dl7());
        qualitativeB.setProperties_dl8(qualitativeBView.isProperties_dl8());
        qualitativeB.setProperties_dl9(qualitativeBView.isProperties_dl9());
        qualitativeB.setProperties_dl10(qualitativeBView.isProperties_dl10());
        qualitativeB.setProperties_dl11(qualitativeBView.isProperties_dl11());
        qualitativeB.setProperties_dl12(qualitativeBView.isProperties_dl12());
        qualitativeB.setProperties_dl13(qualitativeBView.isProperties_dl13());
        qualitativeB.setQualityLevel(qualitativeBView.getQualityLevel());
        qualitativeB.setReason(qualitativeBView.getReason());
        qualitativeB.setQualityResult(qualitativeBView.getQualityResult());
        return qualitativeB;
    }


    public QualitativeView transformQualitativeBToView(QualitativeB qualitativeB){
        QualitativeView qualitativeBView = new QualitativeView();

        qualitativeBView.setId(qualitativeB.getId());
        qualitativeBView.setModifyDate(qualitativeB.getModifyDate());
        qualitativeBView.setModifyBy(qualitativeB.getModifyBy());
        qualitativeBView.setCreateBy(qualitativeB.getCreateBy());
        qualitativeBView.setCreateDate(qualitativeB.getCreateDate());
        qualitativeBView.setProperties_p1(qualitativeB.isProperties_p1());
        qualitativeBView.setProperties_p2(qualitativeB.isProperties_p2());
        qualitativeBView.setProperties_p3(qualitativeB.isProperties_p3());
        qualitativeBView.setProperties_sm1(qualitativeB.isProperties_sm1());
        qualitativeBView.setProperties_sm2(qualitativeB.isProperties_sm2());
        qualitativeBView.setProperties_sm3(qualitativeB.isProperties_sm3());
        qualitativeBView.setProperties_sm4(qualitativeB.isProperties_sm4());
        qualitativeBView.setProperties_sm5(qualitativeB.isProperties_sm5());
        qualitativeBView.setProperties_sm6(qualitativeB.isProperties_sm6());
        qualitativeBView.setProperties_sm7(qualitativeB.isProperties_sm7());
        qualitativeBView.setProperties_sm8(qualitativeB.isProperties_sm8());
        qualitativeBView.setProperties_sm9(qualitativeB.isProperties_sm9());
        qualitativeBView.setProperties_sm10(qualitativeB.isProperties_sm10());
        qualitativeBView.setProperties_sm11(qualitativeB.isProperties_sm11());
        qualitativeBView.setProperties_sm12(qualitativeB.isProperties_sm12());
        qualitativeBView.setProperties_ss1(qualitativeB.isProperties_ss1());
        qualitativeBView.setProperties_ss2(qualitativeB.isProperties_ss2());
        qualitativeBView.setProperties_ss3(qualitativeB.isProperties_ss3());
        qualitativeBView.setProperties_ss4(qualitativeB.isProperties_ss4());
        qualitativeBView.setProperties_ss5(qualitativeB.isProperties_ss5());
        qualitativeBView.setProperties_ss6(qualitativeB.isProperties_ss6());
        qualitativeBView.setProperties_ss7(qualitativeB.isProperties_ss7());
        qualitativeBView.setProperties_ss8(qualitativeB.isProperties_ss8());
        qualitativeBView.setProperties_ss9(qualitativeB.isProperties_ss9());
        qualitativeBView.setProperties_ss10(qualitativeB.isProperties_ss10());
        qualitativeBView.setProperties_ss11(qualitativeB.isProperties_ss11());
        qualitativeBView.setProperties_ss12(qualitativeB.isProperties_ss12());
        qualitativeBView.setProperties_ss13(qualitativeB.isProperties_ss13());
        qualitativeBView.setProperties_ss14(qualitativeB.isProperties_ss14());
        qualitativeBView.setProperties_ss15(qualitativeB.isProperties_ss15());
        qualitativeBView.setProperties_ss16(qualitativeB.isProperties_ss16());
        qualitativeBView.setProperties_ss17(qualitativeB.isProperties_ss17());
        qualitativeBView.setProperties_ss18(qualitativeB.isProperties_ss18());
        qualitativeBView.setProperties_ss19(qualitativeB.isProperties_ss19());
        qualitativeBView.setProperties_ss20(qualitativeB.isProperties_ss20());
        qualitativeBView.setProperties_d1(qualitativeB.isProperties_d1());
        qualitativeBView.setProperties_d2(qualitativeB.isProperties_d2());
        qualitativeBView.setProperties_d3(qualitativeB.isProperties_d3());
        qualitativeBView.setProperties_d4(qualitativeB.isProperties_d4());
        qualitativeBView.setProperties_d5(qualitativeB.isProperties_d5());
        qualitativeBView.setProperties_d6(qualitativeB.isProperties_d6());
        qualitativeBView.setProperties_d7(qualitativeB.isProperties_d7());
        qualitativeBView.setProperties_d8(qualitativeB.isProperties_d8());
        qualitativeBView.setProperties_d9(qualitativeB.isProperties_d9());
        qualitativeBView.setProperties_d10(qualitativeB.isProperties_d10());
        qualitativeBView.setProperties_d11(qualitativeB.isProperties_d11());
        qualitativeBView.setProperties_d12(qualitativeB.isProperties_d12());
        qualitativeBView.setProperties_d13(qualitativeB.isProperties_d13());
        qualitativeBView.setProperties_d14(qualitativeB.isProperties_d14());
        qualitativeBView.setProperties_d15(qualitativeB.isProperties_d15());
        qualitativeBView.setProperties_d16(qualitativeB.isProperties_d16());
        qualitativeBView.setProperties_d17(qualitativeB.isProperties_d17());
        qualitativeBView.setProperties_d18(qualitativeB.isProperties_d18());
        qualitativeBView.setProperties_d19(qualitativeB.isProperties_d19());
        qualitativeBView.setProperties_dl1(qualitativeB.isProperties_dl1());
        qualitativeBView.setProperties_dl2(qualitativeB.isProperties_dl2());
        qualitativeBView.setProperties_dl3(qualitativeB.isProperties_dl3());
        qualitativeBView.setProperties_dl4(qualitativeB.isProperties_dl4());
        qualitativeBView.setProperties_dl5(qualitativeB.isProperties_dl5());
        qualitativeBView.setProperties_dl6(qualitativeB.isProperties_dl6());
        qualitativeBView.setProperties_dl7(qualitativeB.isProperties_dl7());
        qualitativeBView.setProperties_dl8(qualitativeB.isProperties_dl8());
        qualitativeBView.setProperties_dl9(qualitativeB.isProperties_dl9());
        qualitativeBView.setProperties_dl10(qualitativeB.isProperties_dl10());
        qualitativeBView.setProperties_dl11(qualitativeB.isProperties_dl11());
        qualitativeBView.setProperties_dl12(qualitativeB.isProperties_dl12());
        qualitativeBView.setProperties_dl13(qualitativeB.isProperties_dl13());
        qualitativeBView.setQualityLevel(qualitativeB.getQualityLevel());
        qualitativeBView.setReason(qualitativeB.getReason());
        qualitativeBView.setQualityResult(qualitativeB.getQualityResult());
        return qualitativeBView;
    }
}
