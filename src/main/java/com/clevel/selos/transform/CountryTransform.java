package com.clevel.selos.transform;

import com.clevel.selos.dao.master.CountryDAO;
import com.clevel.selos.model.db.master.Country;
import com.clevel.selos.model.view.CountryView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CountryTransform extends Transform {
    @Inject
    private CountryDAO countryDAO;

    @Inject
    public CountryTransform() {
    }

    public Country transformToModel(CountryView countryView) {
        if (countryView == null) {
            return new Country();
        }

        Country country;
        if (countryView.getId() != 0) {
            country = countryDAO.findById(countryView.getId());
        } else {
            country = new Country();
        }
        country.setName(countryView.getName());
        country.setCode(countryView.getCode());
        country.setCode2(countryView.getCode2());
        country.setIsoCode(countryView.getIsoCode());
        country.setActive(countryView.getActive());
        return country;
    }

    public List<Country> transformToModel(List<CountryView> countryViewList) {
        List<Country> countryList = new ArrayList<Country>();
        if (countryViewList == null) {
            return countryList;
        }

        for (CountryView countryView : countryViewList) {
            countryList.add(transformToModel(countryView));
        }
        return countryList;
    }

    public CountryView transformToView(Country country) {
        CountryView countryView = new CountryView();
        if (country == null) {
            return countryView;
        }
        countryView.setId(country.getId());
        countryView.setName(country.getName());
        countryView.setCode(country.getCode());
        countryView.setCode2(country.getCode2());
        countryView.setIsoCode(country.getIsoCode());
        countryView.setActive(country.getActive());
        return countryView;
    }

    public List<CountryView> transformToView(List<Country> countryList) {
        List<CountryView> countryViewList = new ArrayList<CountryView>();
        if (countryList == null) {
            return countryViewList;
        }

        for (Country country : countryList) {
            countryViewList.add(transformToView(country));
        }
        return countryViewList;
    }
}
