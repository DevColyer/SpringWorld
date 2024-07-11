package com.mjolnir.yggdrasil;

import com.mjolnir.yggdrasil.service.WorldService;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Transactional
class GetCountryWithMostCitiesTest {

    @Autowired
    private WorldService worldService;

    @Test
    @DisplayName("getCountryWithMostCities returns country with most cities")
    void getCountryWithMostCitiesReturnsCountryWithMostCities() {
        Pair<String, Integer> expected = new Pair<>("CHN", 363);
        Pair<String, Integer> actual = worldService.getCountryWithMostCities();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getCountries returns country with most cities sad path")
    void getCountriesReturnsCountryWithMostCitiesSadPath() {
        Pair<String, Integer> fakeMostCities = new Pair<>("USA", 700);
        Pair<String, Integer> actual = worldService.getCountryWithMostCities();
        Assertions.assertNotEquals(fakeMostCities, actual);
    }
}
