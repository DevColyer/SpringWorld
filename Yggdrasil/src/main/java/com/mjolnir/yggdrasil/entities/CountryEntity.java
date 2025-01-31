package com.mjolnir.yggdrasil.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "country")
public class CountryEntity {
    @Id
    @Size(max = 3)
    @ColumnDefault("''")
    @Column(name = "Code", nullable = false, length = 3)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private String code;

    @Size(max = 52)
    @NotNull
    @ColumnDefault("''")
    @Column(name = "Name", nullable = false, length = 52)
    private String name;

    @NotNull
    @ColumnDefault("'Asia'")
    @Lob
    @Column(name = "Continent", nullable = false)
    private String continent;

    @Size(max = 26)
    @NotNull
    @ColumnDefault("''")
    @Column(name = "Region", nullable = false, length = 26)
    private String region;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "SurfaceArea", nullable = false, precision = 10, scale = 2)
    private BigDecimal surfaceArea;

    @Column(name = "IndepYear")
    private Short indepYear;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "Population", nullable = false)
    private Integer population;

    @Column(name = "LifeExpectancy", precision = 3, scale = 1)
    private BigDecimal lifeExpectancy;

    @Column(name = "GNP", precision = 10, scale = 2)
    private BigDecimal gnp;

    @Column(name = "GNPOld", precision = 10, scale = 2)
    private BigDecimal gNPOld;

    @Size(max = 45)
    @NotNull
    @ColumnDefault("''")
    @Column(name = "LocalName", nullable = false, length = 45)
    private String localName;

    @Size(max = 45)
    @NotNull
    @ColumnDefault("''")
    @Column(name = "GovernmentForm", nullable = false, length = 45)
    private String governmentForm;

    @Size(max = 60)
    @Column(name = "HeadOfState", length = 60)
    private String headOfState;

    @Column(name = "Capital")
    private Integer capital;

    @Size(max = 2)
    @NotNull
    @ColumnDefault("''")
    @Column(name = "Code2", nullable = false, length = 2)
    private String code2;

    @JsonManagedReference
    @OneToMany(mappedBy = "countryEntityCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CityEntity> cities = new LinkedHashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "countryEntityCode", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CountryLanguageEntity> countryLanguageEntities = new LinkedHashSet<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public BigDecimal getSurfaceArea() {
        return surfaceArea;
    }

    public void setSurfaceArea(BigDecimal surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    public Short getIndepYear() {
        return indepYear;
    }

    public void setIndepYear(Short indepYear) {
        this.indepYear = indepYear;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public BigDecimal getLifeExpectancy() {
        return lifeExpectancy;
    }

    public void setLifeExpectancy(BigDecimal lifeExpectancy) {
        this.lifeExpectancy = lifeExpectancy;
    }

    public BigDecimal getGnp() {
        return gnp;
    }

    public void setGnp(BigDecimal gnp) {
        this.gnp = gnp;
    }

    public BigDecimal getGNPOld() {
        return gNPOld;
    }

    public void setGNPOld(BigDecimal gNPOld) {
        this.gNPOld = gNPOld;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getGovernmentForm() {
        return governmentForm;
    }

    public void setGovernmentForm(String governmentForm) {
        this.governmentForm = governmentForm;
    }

    public String getHeadOfState() {
        return headOfState;
    }

    public void setHeadOfState(String headOfState) {
        this.headOfState = headOfState;
    }

    public Integer getCapital() {
        return capital;
    }

    public void setCapital(Integer capital) {
        this.capital = capital;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public Set<CityEntity> getCities() {
        return cities;
    }

    @JsonBackReference(value = "country-cities")
    public void setCities(Set<CityEntity> cities) {
        this.cities = cities;
    }

    @JsonBackReference(value = "country-languages")
    public Set<CountryLanguageEntity> getCountrylanguages() {
        return countryLanguageEntities;
    }

    public void setCountrylanguages(Set<CountryLanguageEntity> countryLanguageEntities) {
        this.countryLanguageEntities = countryLanguageEntities;
    }

}