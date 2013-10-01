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

        return qualitativeA;
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
        return qualitativeB;
    }
}
