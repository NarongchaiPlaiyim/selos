package com.clevel.selos.businesscontrol.master;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.relation.RelationCustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.relation.RelationCustomer;
import com.clevel.selos.transform.*;
import com.clevel.selos.transform.master.*;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ApplicationCacheLoader implements Serializable{

    @Inject
    @SELOS
    private Logger logger;

    private Map<String, Map> indexHash = null;

    @Inject private CountryDAO countryDAO;
    @Inject private CountryTransform countryTransform;

    @Inject private BankAccountTypeDAO bankAccountTypeDAO;
    @Inject private BankAccountTypeTransform bankAccountTypeTransform;

    @Inject private BaseRateDAO baseRateDAO;
    @Inject private BaseRateTransform baseRateTransform;

    @Inject private BankAccountPurposeDAO bankAccountPurposeDAO;
    @Inject private BankAccountPurposeTransform bankAccountPurposeTransform;

    @Inject private BankAccountProductDAO bankAccountProductDAO;
    @Inject private BankAccountProductTransform bankAccountProductTransform;

    @Inject private ProductGroupDAO productGroupDAO;
    @Inject private ProductTransform productTransform;

    @Inject private SpecialProgramDAO specialProgramDAO;
    @Inject private SpecialProgramTransform specialProgramTransform;

    @Inject private RequestTypeDAO requestTypeDAO;
    @Inject private RequestTypeTransform requestTypeTransform;

    @Inject private RiskTypeDAO riskTypeDAO;
    @Inject private RiskTypeTransform riskTypeTransform;

    @Inject private SBFScoreDAO sbfScoreDAO;
    @Inject private SBFScoreTransform sbfScoreTransform;

    @Inject private BankDAO bankDAO;
    @Inject private BankTransform bankTransform;

    @Inject private BorrowingTypeDAO borrowingTypeDAO;
    @Inject private BorrowingTypeTransform borrowingTypeTransform;

    @Inject private DocumentTypeDAO documentTypeDAO;
    @Inject private DocumentTypeTransform documentTypeTransform;

    @Inject private RelationDAO relationDAO;
    @Inject private RelationTransform relationTransform;

    @Inject private RelationCustomerDAO relationCustomerDAO;
    @Inject private RelationCustomerTransform relationCustomerTransform;

    @Inject private ReferenceDAO referenceDAO;
    @Inject private ReferenceTransform referenceTransform;

    @Inject private TitleDAO titleDAO;
    @Inject private TitleTransform titleTransform;

    @Inject private RaceDAO raceDAO;
    @Inject private RaceTransform raceTransform;

    @Inject private NationalityDAO nationalityDAO;
    @Inject private NationalityTransform nationalityTransform;

    @Inject private EducationDAO educationDAO;
    @Inject private EducationTransform educationTransform;

    @Inject private OccupationDAO occupationDAO;
    @Inject private OccupationTransform occupationTransform;

    @Inject
    public ApplicationCacheLoader() {
    }

    @PostConstruct
    public void onCreation() {
        logger.debug("onCreation.");
        indexHash = new ConcurrentHashMap<String, Map>();
    }

    public void loadCacheDB() {
        logger.debug("loadCacheDB.");
        List<Country> countryList = countryDAO.findAll();
        indexHash.put(Country.class.getName(), countryTransform.transformToCache(countryList));

        List<BankAccountType> bankAccountTypeList = bankAccountTypeDAO.findAll();
        indexHash.put(BankAccountType.class.getName(), bankAccountTypeTransform.transformToCache(bankAccountTypeList));

        List<BaseRate> baseRateList = baseRateDAO.findAll();
        indexHash.put(BaseRate.class.getName(), baseRateTransform.transformToCache(baseRateList));

        List<BankAccountPurpose> bankAccountPurposeList = bankAccountPurposeDAO.findAll();
        indexHash.put(BankAccountPurpose.class.getName(), bankAccountPurposeTransform.transformToCache(bankAccountPurposeList));

        List<BankAccountProduct> bankAccountProductList = bankAccountProductDAO.findAll();
        indexHash.put(BankAccountProduct.class.getName(), bankAccountProductTransform.transformToCache(bankAccountProductList));

        List<ProductGroup> productGroupList = productGroupDAO.findAll();
        indexHash.put(ProductGroup.class.getName(), productTransform.transformToCache(productGroupList));

        List<SpecialProgram> specialProgramList = specialProgramDAO.findAll();
        indexHash.put(SpecialProgram.class.getName(), specialProgramTransform.transformToCache(specialProgramList));

        List<RequestType> requestTypeList = requestTypeDAO.findAll();
        indexHash.put(RequestType.class.getName(), requestTypeTransform.transformToCache(requestTypeList));

        List<RiskType> riskTypeList = riskTypeDAO.findAll();
        indexHash.put(RiskType.class.getName(), riskTypeTransform.transformToCache(riskTypeList));

        List<SBFScore> sbfScoreList = sbfScoreDAO.findAll();
        indexHash.put(SBFScore.class.getName(), sbfScoreTransform.transformToCache(sbfScoreList));

        List<Bank> bankList = bankDAO.findAll();
        indexHash.put(Bank.class.getName(), bankTransform.transformToCache(bankList));

        List<BorrowingType> borrowingTypeList = borrowingTypeDAO.findAll();
        indexHash.put(BorrowingType.class.getName(), borrowingTypeTransform.transformToCache(borrowingTypeList));

        List<DocumentType> documentTypeList = documentTypeDAO.findAll();
        indexHash.put(DocumentType.class.getName(), documentTypeTransform.transformToCache(documentTypeList));

        List<Relation> relationList = relationDAO.findAll();
        indexHash.put(Relation.class.getName(), relationTransform.transformToCache(relationList));

        List<RelationCustomer> relationCustomerList = relationCustomerDAO.findAll();
        indexHash.put(RelationCustomer.class.getName(), relationCustomerTransform.transformToCache(relationCustomerList));

        List<Reference> referenceList = referenceDAO.findAll();
        indexHash.put(Reference.class.getName(), referenceTransform.transformToCache(referenceList));

        List<Title> titleList = titleDAO.findAll();
        indexHash.put(Title.class.getName(), titleTransform.transformToCache(titleList));

        List<Race> raceList = raceDAO.findAll();
        indexHash.put(Race.class.getName(), raceTransform.transformToCache(raceList));

        List<Nationality> nationalityList = nationalityDAO.findAll();
        indexHash.put(Nationality.class.getName(), nationalityTransform.transformToCache(nationalityList));

        List<Education> educationList = educationDAO.findAll();
        indexHash.put(Education.class.getName(), educationTransform.transformToCache(educationList));

        List<Occupation> occupationList = occupationDAO.findAll();
        indexHash.put(Occupation.class.getName(), occupationTransform.transformToCache(occupationList));

        Util.listMap(indexHash);
    }

    public Map getCacheMap(String className){
        return indexHash.get(className);
    }

    public void setCacheMap(String className, Map cacheMap){
        indexHash.put(className, cacheMap);
    }
}
