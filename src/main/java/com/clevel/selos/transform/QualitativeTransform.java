package com.clevel.selos.transform;

import com.clevel.selos.dao.working.QualitativeADAO;
import com.clevel.selos.dao.working.QualitativeBDAO;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.QualitativeA;
import com.clevel.selos.model.db.working.QualitativeB;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.QualitativeView;
import com.clevel.selos.util.Util;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.Date;


public class QualitativeTransform extends Transform {

    @Inject
    QualitativeBDAO qualitativeBDAO;
    @Inject
    QualitativeADAO qualitativeADAO;

    public QualitativeA transformQualitativeAToModel(QualitativeView qualitativeAView, WorkCase workCase, User user) {
        log.info("transformQualitativeAToModel ::: ");
        QualitativeA qualitativeA = new QualitativeA();
        Date createDate = DateTime.now().toDate();
        Date modifyDate = DateTime.now().toDate();
        log.info("transformQualitativeAToModel createDate  :: {} ",createDate);
        log.info("transformQualitativeAToModel modifyDate  :: {} ",modifyDate);

        if (qualitativeAView.getId() != 0) {
            qualitativeA = qualitativeADAO.findById(qualitativeAView.getId());
        }

        qualitativeA.setActive(true);
        qualitativeA.setWorkCase(workCase);
        qualitativeA.setCreateBy(user);
        qualitativeA.setModifyBy(user);
        qualitativeA.setCreateDate(createDate);
        qualitativeA.setModifyDate(modifyDate);
        qualitativeA.setProperties_p1(convertToDB(qualitativeAView.isProperties_p1()));
        qualitativeA.setProperties_p2(convertToDB(qualitativeAView.isProperties_p2()));
        qualitativeA.setProperties_p3(convertToDB(qualitativeAView.isProperties_p3()));
        qualitativeA.setProperties_sm1(convertToDB(qualitativeAView.isProperties_sm1()));
        qualitativeA.setProperties_sm2(convertToDB(qualitativeAView.isProperties_sm2()));
        qualitativeA.setProperties_sm3(convertToDB(qualitativeAView.isProperties_sm3()));
        qualitativeA.setProperties_sm4(convertToDB(qualitativeAView.isProperties_sm4()));
        qualitativeA.setProperties_sm5(convertToDB(qualitativeAView.isProperties_sm5()));
        qualitativeA.setProperties_sm6(convertToDB(qualitativeAView.isProperties_sm6()));
        qualitativeA.setProperties_sm7(convertToDB(qualitativeAView.isProperties_sm7()));
        qualitativeA.setProperties_sm8(convertToDB(qualitativeAView.isProperties_sm8()));
        qualitativeA.setProperties_sm9(convertToDB(qualitativeAView.isProperties_sm9()));
        qualitativeA.setProperties_sm10(convertToDB(qualitativeAView.isProperties_sm10()));
        qualitativeA.setProperties_sm11(convertToDB(qualitativeAView.isProperties_sm11()));
        qualitativeA.setProperties_sm12(convertToDB(qualitativeAView.isProperties_sm12()));
        qualitativeA.setProperties_ss1(convertToDB(qualitativeAView.isProperties_ss1()));
        qualitativeA.setProperties_ss2(convertToDB(qualitativeAView.isProperties_ss2()));
        qualitativeA.setProperties_ss3(convertToDB(qualitativeAView.isProperties_ss3()));
        qualitativeA.setProperties_ss4(convertToDB(qualitativeAView.isProperties_ss4()));
        qualitativeA.setProperties_ss5(convertToDB(qualitativeAView.isProperties_ss5()));
        qualitativeA.setProperties_ss6(convertToDB(qualitativeAView.isProperties_ss6()));
        qualitativeA.setProperties_ss7(convertToDB(qualitativeAView.isProperties_ss7()));
        qualitativeA.setProperties_ss8(convertToDB(qualitativeAView.isProperties_ss8()));
        qualitativeA.setProperties_ss9(convertToDB(qualitativeAView.isProperties_ss9()));
        qualitativeA.setProperties_ss10(convertToDB(qualitativeAView.isProperties_ss10()));
        qualitativeA.setProperties_ss11(convertToDB(qualitativeAView.isProperties_ss11()));
        qualitativeA.setProperties_ss12(convertToDB(qualitativeAView.isProperties_ss12()));
        qualitativeA.setProperties_ss13(convertToDB(qualitativeAView.isProperties_ss13()));
        qualitativeA.setProperties_ss14(convertToDB(qualitativeAView.isProperties_ss14()));
        qualitativeA.setProperties_ss15(convertToDB(qualitativeAView.isProperties_ss15()));
        qualitativeA.setProperties_ss16(convertToDB(qualitativeAView.isProperties_ss16()));
        qualitativeA.setProperties_ss17(convertToDB(qualitativeAView.isProperties_ss17()));
        qualitativeA.setProperties_ss18(convertToDB(qualitativeAView.isProperties_ss18()));
        qualitativeA.setProperties_ss19(convertToDB(qualitativeAView.isProperties_ss19()));
        qualitativeA.setProperties_ss20(convertToDB(qualitativeAView.isProperties_ss20()));
        qualitativeA.setProperties_d1(convertToDB(qualitativeAView.isProperties_d1()));
        qualitativeA.setProperties_d2(convertToDB(qualitativeAView.isProperties_d2()));
        qualitativeA.setProperties_d3(convertToDB(qualitativeAView.isProperties_d3()));
        qualitativeA.setProperties_d4(convertToDB(qualitativeAView.isProperties_d4()));
        qualitativeA.setProperties_d5(convertToDB(qualitativeAView.isProperties_d5()));
        qualitativeA.setProperties_d6(convertToDB(qualitativeAView.isProperties_d6()));
        qualitativeA.setProperties_d7(convertToDB(qualitativeAView.isProperties_d7()));
        qualitativeA.setProperties_d8(convertToDB(qualitativeAView.isProperties_d8()));
        qualitativeA.setProperties_d9(convertToDB(qualitativeAView.isProperties_d9()));
        qualitativeA.setProperties_d10(convertToDB(qualitativeAView.isProperties_d10()));
        qualitativeA.setProperties_d11(convertToDB(qualitativeAView.isProperties_d11()));
        qualitativeA.setProperties_d12(convertToDB(qualitativeAView.isProperties_d12()));
        qualitativeA.setProperties_d13(convertToDB(qualitativeAView.isProperties_d13()));
        qualitativeA.setProperties_d14(convertToDB(qualitativeAView.isProperties_d14()));
        qualitativeA.setProperties_d15(convertToDB(qualitativeAView.isProperties_d15()));
        qualitativeA.setProperties_d16(convertToDB(qualitativeAView.isProperties_d16()));
        qualitativeA.setProperties_d17(convertToDB(qualitativeAView.isProperties_d17()));
        qualitativeA.setProperties_d18(convertToDB(qualitativeAView.isProperties_d18()));
        qualitativeA.setProperties_d19(convertToDB(qualitativeAView.isProperties_d19()));
        qualitativeA.setProperties_d20(convertToDB(qualitativeAView.isProperties_d20()));
        qualitativeA.setProperties_dl1(convertToDB(qualitativeAView.isProperties_dl1()));
        qualitativeA.setProperties_dl2(convertToDB(qualitativeAView.isProperties_dl2()));
        qualitativeA.setProperties_dl3(convertToDB(qualitativeAView.isProperties_dl3()));
        qualitativeA.setProperties_dl4(convertToDB(qualitativeAView.isProperties_dl4()));
        qualitativeA.setProperties_dl5(convertToDB(qualitativeAView.isProperties_dl5()));
        qualitativeA.setProperties_dl6(convertToDB(qualitativeAView.isProperties_dl6()));
        qualitativeA.setProperties_dl7(convertToDB(qualitativeAView.isProperties_dl7()));
        qualitativeA.setProperties_dl8(convertToDB(qualitativeAView.isProperties_dl8()));
        qualitativeA.setProperties_dl9(convertToDB(qualitativeAView.isProperties_dl9()));
        qualitativeA.setProperties_dl10(convertToDB(qualitativeAView.isProperties_dl10()));
        qualitativeA.setProperties_dl11(convertToDB(qualitativeAView.isProperties_dl11()));
        qualitativeA.setProperties_dl12(convertToDB(qualitativeAView.isProperties_dl12()));
        qualitativeA.setProperties_dl13(convertToDB(qualitativeAView.isProperties_dl13()));
        qualitativeA.setQualityLevel(qualitativeAView.getQualityLevel());
        qualitativeA.setReason(qualitativeAView.getReason());
        qualitativeA.setQualityResult(qualitativeAView.getQualityResult());

        return qualitativeA;
    }

    public QualitativeView transformQualitativeAToView(QualitativeA qualitativeA) {
        QualitativeView qualitativeView = new QualitativeView();

        qualitativeView.setId(qualitativeA.getId());
        qualitativeView.setCreateBy(qualitativeA.getCreateBy());
        qualitativeView.setCreateDate(qualitativeA.getCreateDate());
        qualitativeView.setModifyDate(qualitativeA.getModifyDate());
        qualitativeView.setModifyBy(qualitativeA.getModifyBy());
        qualitativeView.setProperties_p1(convertToView(qualitativeA.getProperties_p1()));
        qualitativeView.setProperties_p2(convertToView(qualitativeA.getProperties_p2()));
        qualitativeView.setProperties_p3(convertToView(qualitativeA.getProperties_p3()));
        qualitativeView.setProperties_sm1(convertToView(qualitativeA.getProperties_sm1()));
        qualitativeView.setProperties_sm2(convertToView(qualitativeA.getProperties_sm2()));
        qualitativeView.setProperties_sm3(convertToView(qualitativeA.getProperties_sm3()));
        qualitativeView.setProperties_sm4(convertToView(qualitativeA.getProperties_sm4()));
        qualitativeView.setProperties_sm5(convertToView(qualitativeA.getProperties_sm5()));
        qualitativeView.setProperties_sm6(convertToView(qualitativeA.getProperties_sm6()));
        qualitativeView.setProperties_sm7(convertToView(qualitativeA.getProperties_sm7()));
        qualitativeView.setProperties_sm8(convertToView(qualitativeA.getProperties_sm8()));
        qualitativeView.setProperties_sm9(convertToView(qualitativeA.getProperties_sm9()));
        qualitativeView.setProperties_sm10(convertToView(qualitativeA.getProperties_sm10()));
        qualitativeView.setProperties_sm11(convertToView(qualitativeA.getProperties_sm11()));
        qualitativeView.setProperties_sm12(convertToView(qualitativeA.getProperties_sm12()));
        qualitativeView.setProperties_ss1(convertToView(qualitativeA.getProperties_ss1()));
        qualitativeView.setProperties_ss2(convertToView(qualitativeA.getProperties_ss2()));
        qualitativeView.setProperties_ss3(convertToView(qualitativeA.getProperties_ss3()));
        qualitativeView.setProperties_ss4(convertToView(qualitativeA.getProperties_ss4()));
        qualitativeView.setProperties_ss5(convertToView(qualitativeA.getProperties_ss5()));
        qualitativeView.setProperties_ss6(convertToView(qualitativeA.getProperties_ss6()));
        qualitativeView.setProperties_ss7(convertToView(qualitativeA.getProperties_ss7()));
        qualitativeView.setProperties_ss8(convertToView(qualitativeA.getProperties_ss8()));
        qualitativeView.setProperties_ss9(convertToView(qualitativeA.getProperties_ss9()));
        qualitativeView.setProperties_ss10(convertToView(qualitativeA.getProperties_ss10()));
        qualitativeView.setProperties_ss11(convertToView(qualitativeA.getProperties_ss11()));
        qualitativeView.setProperties_ss12(convertToView(qualitativeA.getProperties_ss12()));
        qualitativeView.setProperties_ss13(convertToView(qualitativeA.getProperties_ss13()));
        qualitativeView.setProperties_ss14(convertToView(qualitativeA.getProperties_ss14()));
        qualitativeView.setProperties_ss15(convertToView(qualitativeA.getProperties_ss15()));
        qualitativeView.setProperties_ss16(convertToView(qualitativeA.getProperties_ss16()));
        qualitativeView.setProperties_ss17(convertToView(qualitativeA.getProperties_ss17()));
        qualitativeView.setProperties_ss18(convertToView(qualitativeA.getProperties_ss18()));
        qualitativeView.setProperties_ss19(convertToView(qualitativeA.getProperties_ss19()));
        qualitativeView.setProperties_ss20(convertToView(qualitativeA.getProperties_ss20()));
        qualitativeView.setProperties_d1(convertToView(qualitativeA.getProperties_d1()));
        qualitativeView.setProperties_d2(convertToView(qualitativeA.getProperties_d2()));
        qualitativeView.setProperties_d3(convertToView(qualitativeA.getProperties_d3()));
        qualitativeView.setProperties_d4(convertToView(qualitativeA.getProperties_d4()));
        qualitativeView.setProperties_d5(convertToView(qualitativeA.getProperties_d5()));
        qualitativeView.setProperties_d6(convertToView(qualitativeA.getProperties_d6()));
        qualitativeView.setProperties_d7(convertToView(qualitativeA.getProperties_d7()));
        qualitativeView.setProperties_d8(convertToView(qualitativeA.getProperties_d8()));
        qualitativeView.setProperties_d9(convertToView(qualitativeA.getProperties_d9()));
        qualitativeView.setProperties_d10(convertToView(qualitativeA.getProperties_d10()));
        qualitativeView.setProperties_d11(convertToView(qualitativeA.getProperties_d11()));
        qualitativeView.setProperties_d12(convertToView(qualitativeA.getProperties_d12()));
        qualitativeView.setProperties_d13(convertToView(qualitativeA.getProperties_d13()));
        qualitativeView.setProperties_d14(convertToView(qualitativeA.getProperties_d14()));
        qualitativeView.setProperties_d15(convertToView(qualitativeA.getProperties_d15()));
        qualitativeView.setProperties_d16(convertToView(qualitativeA.getProperties_d16()));
        qualitativeView.setProperties_d17(convertToView(qualitativeA.getProperties_d17()));
        qualitativeView.setProperties_d18(convertToView(qualitativeA.getProperties_d18()));
        qualitativeView.setProperties_d19(convertToView(qualitativeA.getProperties_d19()));
        qualitativeView.setProperties_d20(convertToView(qualitativeA.getProperties_d20()));
        qualitativeView.setProperties_dl1(convertToView(qualitativeA.getProperties_dl1()));
        qualitativeView.setProperties_dl2(convertToView(qualitativeA.getProperties_dl2()));
        qualitativeView.setProperties_dl3(convertToView(qualitativeA.getProperties_dl3()));
        qualitativeView.setProperties_dl4(convertToView(qualitativeA.getProperties_dl4()));
        qualitativeView.setProperties_dl5(convertToView(qualitativeA.getProperties_dl5()));
        qualitativeView.setProperties_dl6(convertToView(qualitativeA.getProperties_dl6()));
        qualitativeView.setProperties_dl7(convertToView(qualitativeA.getProperties_dl7()));
        qualitativeView.setProperties_dl8(convertToView(qualitativeA.getProperties_dl8()));
        qualitativeView.setProperties_dl9(convertToView(qualitativeA.getProperties_dl9()));
        qualitativeView.setProperties_dl10(convertToView(qualitativeA.getProperties_dl10()));
        qualitativeView.setProperties_dl11(convertToView(qualitativeA.getProperties_dl11()));
        qualitativeView.setProperties_dl12(convertToView(qualitativeA.getProperties_dl12()));
        qualitativeView.setProperties_dl13(convertToView(qualitativeA.getProperties_dl13()));
        qualitativeView.setQualityLevel(qualitativeA.getQualityLevel());
        qualitativeView.setReason(qualitativeA.getReason());
        qualitativeView.setQualityResult(qualitativeA.getQualityResult());

        return qualitativeView;
    }

    public QualitativeB transformQualitativeBToModel(QualitativeView qualitativeBView, WorkCase workCase, User user) {
        QualitativeB qualitativeB = new QualitativeB();
        Date createDate = DateTime.now().toDate();
        Date modifyDate = DateTime.now().toDate();
        log.info("transformQualitativeBToModel createDate  :: {} ",createDate);
        log.info("transformQualitativeBToModel modifyDate  :: {} ",modifyDate);

        if (qualitativeBView.getId() != 0) {
            qualitativeB = qualitativeBDAO.findById(qualitativeBView.getId());
        }

        qualitativeB.setActive(true);
        qualitativeB.setWorkCase(workCase);

        qualitativeB.setCreateDate(createDate);
        qualitativeB.setModifyDate(modifyDate);
        qualitativeB.setCreateBy(user);
        qualitativeB.setModifyBy(user);
        qualitativeB.setProperties_p1(convertToDB(qualitativeBView.isProperties_p1()));
        qualitativeB.setProperties_p2(convertToDB(qualitativeBView.isProperties_p2()));
        qualitativeB.setProperties_p3(convertToDB(qualitativeBView.isProperties_p3()));
        qualitativeB.setProperties_sm1(convertToDB(qualitativeBView.isProperties_sm1()));
        qualitativeB.setProperties_sm2(convertToDB(qualitativeBView.isProperties_sm2()));
        qualitativeB.setProperties_sm3(convertToDB(qualitativeBView.isProperties_sm3()));
        qualitativeB.setProperties_sm4(convertToDB(qualitativeBView.isProperties_sm4()));
        qualitativeB.setProperties_sm5(convertToDB(qualitativeBView.isProperties_sm5()));
        qualitativeB.setProperties_sm6(convertToDB(qualitativeBView.isProperties_sm6()));
        qualitativeB.setProperties_sm7(convertToDB(qualitativeBView.isProperties_sm7()));
        qualitativeB.setProperties_sm8(convertToDB(qualitativeBView.isProperties_sm8()));
        qualitativeB.setProperties_sm9(convertToDB(qualitativeBView.isProperties_sm9()));
        qualitativeB.setProperties_sm10(convertToDB(qualitativeBView.isProperties_sm10()));
        qualitativeB.setProperties_sm11(convertToDB(qualitativeBView.isProperties_sm11()));
        qualitativeB.setProperties_sm12(convertToDB(qualitativeBView.isProperties_sm12()));
        qualitativeB.setProperties_ss1(convertToDB(qualitativeBView.isProperties_ss1()));
        qualitativeB.setProperties_ss2(convertToDB(qualitativeBView.isProperties_ss2()));
        qualitativeB.setProperties_ss3(convertToDB(qualitativeBView.isProperties_ss3()));
        qualitativeB.setProperties_ss4(convertToDB(qualitativeBView.isProperties_ss4()));
        qualitativeB.setProperties_ss5(convertToDB(qualitativeBView.isProperties_ss5()));
        qualitativeB.setProperties_ss6(convertToDB(qualitativeBView.isProperties_ss6()));
        qualitativeB.setProperties_ss7(convertToDB(qualitativeBView.isProperties_ss7()));
        qualitativeB.setProperties_ss8(convertToDB(qualitativeBView.isProperties_ss8()));
        qualitativeB.setProperties_ss9(convertToDB(qualitativeBView.isProperties_ss9()));
        qualitativeB.setProperties_ss10(convertToDB(qualitativeBView.isProperties_ss10()));
        qualitativeB.setProperties_ss11(convertToDB(qualitativeBView.isProperties_ss11()));
        qualitativeB.setProperties_ss12(convertToDB(qualitativeBView.isProperties_ss12()));
        qualitativeB.setProperties_ss13(convertToDB(qualitativeBView.isProperties_ss13()));
        qualitativeB.setProperties_ss14(convertToDB(qualitativeBView.isProperties_ss14()));
        qualitativeB.setProperties_ss15(convertToDB(qualitativeBView.isProperties_ss15()));
        qualitativeB.setProperties_ss16(convertToDB(qualitativeBView.isProperties_ss16()));
        qualitativeB.setProperties_ss17(convertToDB(qualitativeBView.isProperties_ss17()));
        qualitativeB.setProperties_ss18(convertToDB(qualitativeBView.isProperties_ss18()));
        qualitativeB.setProperties_ss19(convertToDB(qualitativeBView.isProperties_ss19()));
        qualitativeB.setProperties_ss20(convertToDB(qualitativeBView.isProperties_ss20()));
        qualitativeB.setProperties_d1(convertToDB(qualitativeBView.isProperties_d1()));
        qualitativeB.setProperties_d2(convertToDB(qualitativeBView.isProperties_d2()));
        qualitativeB.setProperties_d3(convertToDB(qualitativeBView.isProperties_d3()));
        qualitativeB.setProperties_d4(convertToDB(qualitativeBView.isProperties_d4()));
        qualitativeB.setProperties_d5(convertToDB(qualitativeBView.isProperties_d5()));
        qualitativeB.setProperties_d6(convertToDB(qualitativeBView.isProperties_d6()));
        qualitativeB.setProperties_d7(convertToDB(qualitativeBView.isProperties_d7()));
        qualitativeB.setProperties_d8(convertToDB(qualitativeBView.isProperties_d8()));
        qualitativeB.setProperties_d9(convertToDB(qualitativeBView.isProperties_d9()));
        qualitativeB.setProperties_d10(convertToDB(qualitativeBView.isProperties_d10()));
        qualitativeB.setProperties_d11(convertToDB(qualitativeBView.isProperties_d11()));
        qualitativeB.setProperties_d12(convertToDB(qualitativeBView.isProperties_d12()));
        qualitativeB.setProperties_d13(convertToDB(qualitativeBView.isProperties_d13()));
        qualitativeB.setProperties_d14(convertToDB(qualitativeBView.isProperties_d14()));
        qualitativeB.setProperties_d15(convertToDB(qualitativeBView.isProperties_d15()));
        qualitativeB.setProperties_d16(convertToDB(qualitativeBView.isProperties_d16()));
        qualitativeB.setProperties_d17(convertToDB(qualitativeBView.isProperties_d17()));
        qualitativeB.setProperties_d18(convertToDB(qualitativeBView.isProperties_d18()));
        qualitativeB.setProperties_d19(convertToDB(qualitativeBView.isProperties_d19()));
        qualitativeB.setProperties_dl1(convertToDB(qualitativeBView.isProperties_dl1()));
        qualitativeB.setProperties_dl2(convertToDB(qualitativeBView.isProperties_dl2()));
        qualitativeB.setProperties_dl3(convertToDB(qualitativeBView.isProperties_dl3()));
        qualitativeB.setProperties_dl4(convertToDB(qualitativeBView.isProperties_dl4()));
        qualitativeB.setProperties_dl5(convertToDB(qualitativeBView.isProperties_dl5()));
        qualitativeB.setProperties_dl6(convertToDB(qualitativeBView.isProperties_dl6()));
        qualitativeB.setProperties_dl7(convertToDB(qualitativeBView.isProperties_dl7()));
        qualitativeB.setProperties_dl8(convertToDB(qualitativeBView.isProperties_dl8()));
        qualitativeB.setProperties_dl9(convertToDB(qualitativeBView.isProperties_dl9()));
        qualitativeB.setProperties_dl10(convertToDB(qualitativeBView.isProperties_dl10()));
        qualitativeB.setProperties_dl11(convertToDB(qualitativeBView.isProperties_dl11()));
        qualitativeB.setProperties_dl12(convertToDB(qualitativeBView.isProperties_dl12()));
        qualitativeB.setProperties_dl13(convertToDB(qualitativeBView.isProperties_dl13()));
        qualitativeB.setQualityLevel(qualitativeBView.getQualityLevel());
        qualitativeB.setReason(qualitativeBView.getReason());
        qualitativeB.setQualityResult(qualitativeBView.getQualityResult());
        return qualitativeB;
    }


    public QualitativeView transformQualitativeBToView(QualitativeB qualitativeB) {
        QualitativeView qualitativeBView = new QualitativeView();

        qualitativeBView.setId(qualitativeB.getId());
        qualitativeBView.setModifyDate(qualitativeB.getModifyDate());
        qualitativeBView.setModifyBy(qualitativeB.getModifyBy());
        qualitativeBView.setCreateBy(qualitativeB.getCreateBy());
        qualitativeBView.setCreateDate(qualitativeB.getCreateDate());
        qualitativeBView.setProperties_p1(convertToView(qualitativeB.getProperties_p1()));
        qualitativeBView.setProperties_p2(convertToView(qualitativeB.getProperties_p2()));
        qualitativeBView.setProperties_p3(convertToView(qualitativeB.getProperties_p3()));
        qualitativeBView.setProperties_sm1(convertToView(qualitativeB.getProperties_sm1()));
        qualitativeBView.setProperties_sm2(convertToView(qualitativeB.getProperties_sm2()));
        qualitativeBView.setProperties_sm3(convertToView(qualitativeB.getProperties_sm3()));
        qualitativeBView.setProperties_sm4(convertToView(qualitativeB.getProperties_sm4()));
        qualitativeBView.setProperties_sm5(convertToView(qualitativeB.getProperties_sm5()));
        qualitativeBView.setProperties_sm6(convertToView(qualitativeB.getProperties_sm6()));
        qualitativeBView.setProperties_sm7(convertToView(qualitativeB.getProperties_sm7()));
        qualitativeBView.setProperties_sm8(convertToView(qualitativeB.getProperties_sm8()));
        qualitativeBView.setProperties_sm9(convertToView(qualitativeB.getProperties_sm9()));
        qualitativeBView.setProperties_sm10(convertToView(qualitativeB.getProperties_sm10()));
        qualitativeBView.setProperties_sm11(convertToView(qualitativeB.getProperties_sm11()));
        qualitativeBView.setProperties_sm12(convertToView(qualitativeB.getProperties_sm12()));
        qualitativeBView.setProperties_ss1(convertToView(qualitativeB.getProperties_ss1()));
        qualitativeBView.setProperties_ss2(convertToView(qualitativeB.getProperties_ss2()));
        qualitativeBView.setProperties_ss3(convertToView(qualitativeB.getProperties_ss3()));
        qualitativeBView.setProperties_ss4(convertToView(qualitativeB.getProperties_ss4()));
        qualitativeBView.setProperties_ss5(convertToView(qualitativeB.getProperties_ss5()));
        qualitativeBView.setProperties_ss6(convertToView(qualitativeB.getProperties_ss6()));
        qualitativeBView.setProperties_ss7(convertToView(qualitativeB.getProperties_ss7()));
        qualitativeBView.setProperties_ss8(convertToView(qualitativeB.getProperties_ss8()));
        qualitativeBView.setProperties_ss9(convertToView(qualitativeB.getProperties_ss9()));
        qualitativeBView.setProperties_ss10(convertToView(qualitativeB.getProperties_ss10()));
        qualitativeBView.setProperties_ss11(convertToView(qualitativeB.getProperties_ss11()));
        qualitativeBView.setProperties_ss12(convertToView(qualitativeB.getProperties_ss12()));
        qualitativeBView.setProperties_ss13(convertToView(qualitativeB.getProperties_ss13()));
        qualitativeBView.setProperties_ss14(convertToView(qualitativeB.getProperties_ss14()));
        qualitativeBView.setProperties_ss15(convertToView(qualitativeB.getProperties_ss15()));
        qualitativeBView.setProperties_ss16(convertToView(qualitativeB.getProperties_ss16()));
        qualitativeBView.setProperties_ss17(convertToView(qualitativeB.getProperties_ss17()));
        qualitativeBView.setProperties_ss18(convertToView(qualitativeB.getProperties_ss18()));
        qualitativeBView.setProperties_ss19(convertToView(qualitativeB.getProperties_ss19()));
        qualitativeBView.setProperties_ss20(convertToView(qualitativeB.getProperties_ss20()));
        qualitativeBView.setProperties_d1(convertToView(qualitativeB.getProperties_d1()));
        qualitativeBView.setProperties_d2(convertToView(qualitativeB.getProperties_d2()));
        qualitativeBView.setProperties_d3(convertToView(qualitativeB.getProperties_d3()));
        qualitativeBView.setProperties_d4(convertToView(qualitativeB.getProperties_d4()));
        qualitativeBView.setProperties_d5(convertToView(qualitativeB.getProperties_d5()));
        qualitativeBView.setProperties_d6(convertToView(qualitativeB.getProperties_d6()));
        qualitativeBView.setProperties_d7(convertToView(qualitativeB.getProperties_d7()));
        qualitativeBView.setProperties_d8(convertToView(qualitativeB.getProperties_d8()));
        qualitativeBView.setProperties_d9(convertToView(qualitativeB.getProperties_d9()));
        qualitativeBView.setProperties_d10(convertToView(qualitativeB.getProperties_d10()));
        qualitativeBView.setProperties_d11(convertToView(qualitativeB.getProperties_d11()));
        qualitativeBView.setProperties_d12(convertToView(qualitativeB.getProperties_d12()));
        qualitativeBView.setProperties_d13(convertToView(qualitativeB.getProperties_d13()));
        qualitativeBView.setProperties_d14(convertToView(qualitativeB.getProperties_d14()));
        qualitativeBView.setProperties_d15(convertToView(qualitativeB.getProperties_d15()));
        qualitativeBView.setProperties_d16(convertToView(qualitativeB.getProperties_d16()));
        qualitativeBView.setProperties_d17(convertToView(qualitativeB.getProperties_d17()));
        qualitativeBView.setProperties_d18(convertToView(qualitativeB.getProperties_d18()));
        qualitativeBView.setProperties_d19(convertToView(qualitativeB.getProperties_d19()));
        qualitativeBView.setProperties_dl1(convertToView(qualitativeB.getProperties_dl1()));
        qualitativeBView.setProperties_dl2(convertToView(qualitativeB.getProperties_dl2()));
        qualitativeBView.setProperties_dl3(convertToView(qualitativeB.getProperties_dl3()));
        qualitativeBView.setProperties_dl4(convertToView(qualitativeB.getProperties_dl4()));
        qualitativeBView.setProperties_dl5(convertToView(qualitativeB.getProperties_dl5()));
        qualitativeBView.setProperties_dl6(convertToView(qualitativeB.getProperties_dl6()));
        qualitativeBView.setProperties_dl7(convertToView(qualitativeB.getProperties_dl7()));
        qualitativeBView.setProperties_dl8(convertToView(qualitativeB.getProperties_dl8()));
        qualitativeBView.setProperties_dl9(convertToView(qualitativeB.getProperties_dl9()));
        qualitativeBView.setProperties_dl10(convertToView(qualitativeB.getProperties_dl10()));
        qualitativeBView.setProperties_dl11(convertToView(qualitativeB.getProperties_dl11()));
        qualitativeBView.setProperties_dl12(convertToView(qualitativeB.getProperties_dl12()));
        qualitativeBView.setProperties_dl13(convertToView(qualitativeB.getProperties_dl13()));
        qualitativeBView.setQualityLevel(qualitativeB.getQualityLevel());
        qualitativeBView.setReason(qualitativeB.getReason());
        qualitativeBView.setQualityResult(qualitativeB.getQualityResult());
        return qualitativeBView;
    }


    public boolean convertToView(int dbObject) {
        return Util.isTrue(dbObject);
    }

    public int convertToDB(boolean viewObject) {
        return Util.returnNumForFlag(viewObject);
    }
}
