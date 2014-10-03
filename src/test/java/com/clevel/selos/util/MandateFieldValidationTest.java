package com.clevel.selos.util;

import com.clevel.selos.businesscontrol.MandateFieldValidationControl;
import com.clevel.selos.model.MandateConDetailType;
import com.clevel.selos.model.MandateDependConType;
import com.clevel.selos.model.MandateDependType;
import com.clevel.selos.model.db.master.Education;
import com.clevel.selos.model.db.master.Occupation;
import com.clevel.selos.model.db.master.Reference;
import com.clevel.selos.model.db.master.Relation;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.Individual;
import com.clevel.selos.model.view.MandateFieldMessageView;
import com.clevel.selos.model.view.MandateFieldValidationResult;
import com.clevel.selos.model.view.master.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MandateFieldValidationTest {

    MandateFieldValidationControl mandateFieldValidationControl = null;

    public MandateFieldValidationTest(){

    }

    public void Test(){

        List<Customer> customerList = new ArrayList<Customer>();
        Customer customer = new Customer();
        customer.setId(2);
        customer.setAge(30);
        customer.setJuristicId(200L);
        //customer.setNameEn("Dummy");
        Reference reference = new Reference();
        reference.setId(1);

        Relation relation = new Relation();
        relation.setId(1);

        customer.setRelation(relation);

        customer.setReference(reference);
        customer.setShares(new BigDecimal(9));

        Individual individual = new Individual();
        individual.setId(1000);
        individual.setCustomer(customer);

        Occupation occupation = new Occupation();
        occupation.setId(1);

        Education education = new Education();
        education.setId(1);

        individual.setOccupation(occupation);
        individual.setEducation(education);

        customer.setIndividual(individual);
        customerList.add(customer);

        mandateFieldValidationControl.validate(customerList, Customer.class.getName());
        MandateFieldValidationResult validationResult = mandateFieldValidationControl.getMandateFieldValidationResult();

        System.out.println("==========================================================================================");
        System.out.println("Validation Result: " + validationResult.getActionResult());
        for(MandateFieldMessageView mandateFieldMessageView : validationResult.getMandateFieldMessageViewList()){
            System.out.println(mandateFieldMessageView);
        }
    }

    public static void main(String[] args){
        MandateFieldValidationTest mandateTest = new MandateFieldValidationTest();
        mandateTest.initialTest();


    }

    public void initialTest(){
        mandateFieldValidationControl = new MandateFieldValidationControl();

        //Set Mandate Class
        MandateFieldClassView customerMandateFieldClassView = new MandateFieldClassView();
        customerMandateFieldClassView.setId(100);
        customerMandateFieldClassView.setActive(true);
        customerMandateFieldClassView.setClassDescription("Customer Info Indv/Juris");
        customerMandateFieldClassView.setClassName(Customer.class.getName());
        customerMandateFieldClassView.setPageName("Customer Info Indv/Juris");

        MandateFieldClassView indvMandateFieldClassView = new MandateFieldClassView();
        indvMandateFieldClassView.setId(101);
        indvMandateFieldClassView.setActive(true);
        indvMandateFieldClassView.setClassDescription("Customer Info Individual");
        indvMandateFieldClassView.setClassName(Individual.class.getName());
        indvMandateFieldClassView.setPageName("Customer Info Individual");

        //Set Mandate Class Step/Action
        Map<String, MandateFieldClassStepActionView> mandateFieldClassStepActionViewMap = new ConcurrentHashMap<String, MandateFieldClassStepActionView>();
        MandateFieldClassStepActionView cusMandateFieldClassStepActionView = new MandateFieldClassStepActionView();
        cusMandateFieldClassStepActionView.setRequired(true);
        cusMandateFieldClassStepActionView.setMandateFieldClassView(customerMandateFieldClassView);

        MandateFieldClassStepActionView indvMandateFieldClassStepActionView = new MandateFieldClassStepActionView();
        indvMandateFieldClassStepActionView.setRequired(true);
        indvMandateFieldClassStepActionView.setMandateFieldClassView(indvMandateFieldClassView);

        mandateFieldClassStepActionViewMap.put(customerMandateFieldClassView.getClassName(), cusMandateFieldClassStepActionView);
        mandateFieldClassStepActionViewMap.put(indvMandateFieldClassView.getClassName(), indvMandateFieldClassStepActionView);

        MandateFieldView ageView = new MandateFieldView();
        ageView.setId(1001);
        ageView.setFieldName("age");
        ageView.setMinValue("1");
        ageView.setMaxValue("60");
        ageView.setNotMatchedValue("20");
        ageView.setMatchedValue("_EMPTY");

        MandateFieldView reference = new MandateFieldView();
        reference.setId(1002);
        reference.setFieldName("reference");
        //reference.setNotMatchedValue("id:1");
        //reference.setMatchedValue("id:1");
        reference.setNotMatchedValue("_EMPTY");

        MandateFieldView relation = new MandateFieldView();
        relation.setId(1003);
        relation.setFieldName("relation");
        //relation.setNotMatchedValue("id:1");
        //relation.setMatchedValue("id:1");
        relation.setNotMatchedValue("_EMPTY");

        MandateFieldView sharesView = new MandateFieldView();
        sharesView.setId(1004);
        sharesView.setFieldName("shares");
        sharesView.setMinValue("1");
        sharesView.setMaxValue("10");
        sharesView.setNotMatchedValue("_EMPTY");
        sharesView.setMatchedValue("_EMPTY");

        MandateFieldView isCommitteeView = new MandateFieldView();
        isCommitteeView.setId(1005);
        isCommitteeView.setFieldName("isCommittee");
        isCommitteeView.setNotMatchedEmpty(1);

        MandateFieldView nameTHView = new MandateFieldView();
        nameTHView.setId(1006);
        nameTHView.setFieldName("nameTh");
        nameTHView.setNotMatchedValue("_EMPTY");

        MandateFieldView nameENView = new MandateFieldView();
        nameENView.setId(1007);
        nameENView.setFieldName("nameEn");
        //nameENView.setNotMatchedEmpty(1);
        nameENView.setNotMatchedValue("Dummy");

        MandateFieldView indv = new MandateFieldView();
        indv.setId(1008);
        indv.setFieldName("individual");
        //reference.setNotMatchedValue("id:1");
        //reference.setMatchedValue("id:1");
        indv.setNotMatchedValue("_EMPTY");
        indv.setCheckFieldDetail(true);

        List<MandateFieldView> mandateFieldViewList = new ArrayList<MandateFieldView>();
        //mandateFieldViewList.add(ageView);
        //mandateFieldViewList.add(reference);
        //mandateFieldViewList.add(sharesView);
        //mandateFieldViewList.add(isCommitteeView);
        //mandateFieldViewList.add(nameTHView);
        mandateFieldViewList.add(nameENView);
        mandateFieldViewList.add(indv);

        List<MandateFieldView> indvMandateFieldList = new ArrayList<MandateFieldView>();
        MandateFieldView occu = new MandateFieldView();
        occu.setId(1009);
        occu.setFieldName("occupation");
        //occu.setMatchedValue("1");
        occu.setNotMatchedValue("_EMPTY");

        MandateFieldView education = new MandateFieldView();
        education.setId(1010);
        education.setFieldName("education");
        //occu.setMatchedValue("1");
        education.setNotMatchedValue("_EMPTY");


        indvMandateFieldList.add(occu);
        indvMandateFieldList.add(education);


        Map<String, List<MandateFieldView>> mandateFieldViewMap = new ConcurrentHashMap<String, List<MandateFieldView>>();
        mandateFieldViewMap.put(Customer.class.getName(), mandateFieldViewList);
        mandateFieldViewMap.put(Individual.class.getName(), indvMandateFieldList);


//Test Relation Condition
        MandateFieldConditionView relationConView = new MandateFieldConditionView();
        relationConView.setId(1);
        relationConView.setDependType(MandateDependType.NO_DEPENDENCY);
        relationConView.setMandateFieldClassView(customerMandateFieldClassView);
        relationConView.setName("RELATION_ID_1");
        relationConView.setConditionDesc("when Relation id = 1");
        relationConView.setMandateConDetailType(MandateConDetailType.BASE);

        MandateFieldConditionDetailView relationConDetailView = new MandateFieldConditionDetailView();
        relationConDetailView.setMandateFieldView(relation);
        relationConDetailView.setBaseValue("id:1");
        relationConDetailView.setId(200);
        List<MandateFieldConditionDetailView> relationConditionDetailViewList = new ArrayList<MandateFieldConditionDetailView>();
        relationConditionDetailViewList.add(relationConDetailView);
        relationConView.setConditionDetailViewList(relationConditionDetailViewList);

//Set Reference Condition
        MandateFieldConditionView referenceConView = new MandateFieldConditionView();
        referenceConView.setId(2);
        referenceConView.setName("REFERENCE_ID_1");
        referenceConView.setDependType(MandateDependType.DEPEND_TRUE);
        referenceConView.setDependConType(MandateDependConType.INTERNAL);
        referenceConView.setDependCondition(relationConView);
        referenceConView.setMandateFieldClassView(customerMandateFieldClassView);
        referenceConView.setMandateConDetailType(MandateConDetailType.BASE);

        MandateFieldConditionDetailView referenceConDetailView = new MandateFieldConditionDetailView();
        referenceConDetailView.setMandateFieldView(reference);
        referenceConDetailView.setBaseValue("id:1");
        referenceConDetailView.setId(201);
        List<MandateFieldConditionDetailView> referenceConditionDetailViewList = new ArrayList<MandateFieldConditionDetailView>();
        referenceConditionDetailViewList.add(referenceConDetailView);
        referenceConView.setConditionDetailViewList(referenceConditionDetailViewList);

//Set Individual

        MandateFieldConditionView individualConView = new MandateFieldConditionView();
        individualConView.setId(3);
        individualConView.setName("CHECK_INDV_MANDATE");
        individualConView.setDependType(MandateDependType.DEPEND_TRUE);
        individualConView.setDependConType(MandateDependConType.EXTERNAL);
        individualConView.setDependCondition(referenceConView);
        individualConView.setMandateFieldClassView(indvMandateFieldClassView);
        individualConView.setMandateConDetailType(MandateConDetailType.AND);

        MandateFieldConditionDetailView occupationDetCon = new MandateFieldConditionDetailView();
        occupationDetCon.setMandateFieldView(occu);
        occupationDetCon.setId(202);
        occupationDetCon.setBaseValue("id:1");

        MandateFieldConditionDetailView eduConDetail = new MandateFieldConditionDetailView();
        eduConDetail.setMandateFieldView(education);
        eduConDetail.setId(203);


        List<MandateFieldConditionDetailView> indvDetailList = new ArrayList<MandateFieldConditionDetailView>();
        indvDetailList.add(occupationDetCon);
        indvDetailList.add(eduConDetail);
        individualConView.setConditionDetailViewList(indvDetailList);


        Map<Long, MandateFieldConditionView> mandateFieldConditionMap = new ConcurrentHashMap<Long, MandateFieldConditionView>();
        mandateFieldConditionMap.put(relationConView.getId(), relationConView);
        mandateFieldConditionMap.put(referenceConView.getId(), referenceConView);
        mandateFieldConditionMap.put(individualConView.getId(), individualConView);

        mandateFieldValidationControl.setMandateFieldClassStepActionViewMap(mandateFieldClassStepActionViewMap);
        mandateFieldValidationControl.setMandateFieldConditionMap(mandateFieldConditionMap);
        mandateFieldValidationControl.setMandateFieldConditionMap(mandateFieldConditionMap);

    }
}
