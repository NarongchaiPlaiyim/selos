package com.clevel.selos.transform.master;

import com.clevel.selos.dao.master.CountryDAO;
import com.clevel.selos.model.db.master.Country;
import com.clevel.selos.model.view.CountryView;
import com.clevel.selos.transform.Transform;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CountryTransform extends Transform {
    @Inject
    private CountryDAO countryDAO;

    @Inject
    public CountryTransform() {
    }

    public Country transformToModel(CountryView countryView) {
        Country country = new Country();
        if(countryView != null && countryView.getId() != 0){
            country = countryDAO.findById(countryView.getId());
        }
        country.setName(countryView.getName());
        country.setCode(countryView.getCode());
        country.setCode2(countryView.getCode2());
        country.setIsoCode(countryView.getIsoCode());
        country.setActive(countryView.getActive());

        return country;
    }

    public CountryView transformToView(Country country) {
        CountryView countryView = new CountryView();
        if(country != null){
            countryView.setId(country.getId());
            countryView.setName(country.getName());
            countryView.setCode(country.getCode());
            countryView.setCode2(country.getCode2());
            countryView.setIsoCode(country.getIsoCode());
            countryView.setActive(country.getActive());
        }
        return countryView;
    }

    public List<Country> transformToModelList(List<CountryView> countryViewList) {
        List<Country> countryList = new ArrayList<Country>();
        if (countryViewList != null) {
            for (CountryView cv : countryViewList) {
                Country country = transformToModel(cv);
                countryList.add(country);
            }
        }
        return countryList;
    }

    public List<CountryView> transformToViewList(List<Country> countryList) {
        List<CountryView> countryViewList = new ArrayList<CountryView>();
        if (countryList != null) {
            for (Country c : countryList) {
                CountryView countryView = transformToView(c);
                countryViewList.add(countryView);
            }
        }
        return countryViewList;
    }

    public Country transformSelectToModel(CountryView countryView) {
        Country country = new Country();
        if(countryView != null && countryView.getId() != 0){
            country = countryDAO.findById(countryView.getId());
        }
        return country;
    }

    public Map<Integer, CountryView> transformToCache(List<Country> countryList){
        if(countryList == null || countryList.size() == 0)
            return null;
        Map<Integer, CountryView> countryViewMap = new ConcurrentHashMap<Integer, CountryView>();
        for(Country country :  countryList){
                CountryView countryView = transformToView(country);
                countryViewMap.put(countryView.getId(), countryView);
        }
        return countryViewMap;
    }
}
