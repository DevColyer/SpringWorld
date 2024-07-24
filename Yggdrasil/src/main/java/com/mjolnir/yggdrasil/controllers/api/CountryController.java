package com.mjolnir.yggdrasil.controllers.api;

import com.mjolnir.yggdrasil.entities.CountryEntity;
import com.mjolnir.yggdrasil.exceptions.ResourceNotFoundException;
import com.mjolnir.yggdrasil.repositories.CountryRepository;
import com.mjolnir.yggdrasil.service.MjolnirApiService;
import com.mjolnir.yggdrasil.service.WorldService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/Yggdrasil/countries")
public class CountryController {

    private final MjolnirApiService mjolnirApiService;
    private final WorldService worldService;
    private final CountryRepository countryRepository;

    @Autowired
    public CountryController(MjolnirApiService mjolnirApiService, WorldService worldService, CountryRepository countryRepository) {
        this.mjolnirApiService = mjolnirApiService;
        this.worldService = worldService;
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public List<CountryEntity> getCountries() {
        List<CountryEntity> countries = worldService.getAllCountries();
        return countries;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CountryEntity>> getCountry(@PathVariable String id) {
        Optional<CountryEntity> country = worldService.getCountryByCode(id);
        if (!country.isPresent()) {
            throw new ResourceNotFoundException("Country with id " + id + " not found");
        }

        EntityModel<CountryEntity> resource = EntityModel.of(country.get());
        resource.add(linkTo(methodOn(CountryController.class).getCountry(id)).withSelfRel());
        resource.add(linkTo(methodOn(CountryController.class).getCountries()).withRel("all-countries"));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EntityModel<CountryEntity>> createCountry(@RequestBody CountryEntity country, @RequestHeader(name = "MJOLNIR-API-KEY") String apiKey, HttpServletRequest request) {
        String requestRole = mjolnirApiService.getRoleFromApiKey(apiKey);
        if (requestRole == null || !requestRole.equals("FULL_ACCESS"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized user.");

        CountryEntity countryEntity = null;
        try {
            worldService.createNewCountry(
                    country.getCode(),
                    country.getName(),
                    country.getContinent(),
                    country.getRegion(),
                    country.getSurfaceArea(),
                    country.getIndepYear(),
                    country.getPopulation(),
                    country.getLifeExpectancy(),
                    country.getGnp(),
                    country.getGNPOld(),
                    country.getLocalName(),
                    country.getGovernmentForm(),
                    country.getHeadOfState(),
                    country.getCode2(),
                    true
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntityModel<CountryEntity> resource = EntityModel.of(worldService.getCountryByCode(country.getCode()).get());
        resource.add(linkTo(methodOn(CountryController.class).getCountry(country.getCode())).withSelfRel());
        resource.add(linkTo(methodOn(CountryController.class).getCountries()).withRel("all-countries"));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<EntityModel<CountryEntity>> updateCountry(@RequestBody CountryEntity country, @RequestHeader(name = "MJOLNIR-API-KEY") String apiKey, HttpServletRequest request) {
        String requestRole = mjolnirApiService.getRoleFromApiKey(apiKey);
        if (requestRole == null || !requestRole.equals("FULL_ACCESS"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized user.");

        CountryEntity countryEntity = countryRepository.save(country);
        EntityModel<CountryEntity> resource = EntityModel.of(worldService.getCountryByCode(country.getCode()).get());
        resource.add(linkTo(methodOn(CountryController.class).getCountry(country.getCode())).withSelfRel());
        resource.add(linkTo(methodOn(CountryController.class).getCountries()).withRel("all-countries"));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCountry(@PathVariable String id, @RequestHeader(name = "MJOLNIR-API-KEY") String apiKey, HttpServletRequest request) {
        String requestRole = mjolnirApiService.getRoleFromApiKey(apiKey);
        if (requestRole == null || !requestRole.equals("FULL_ACCESS"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized user.");

        boolean deleted = worldService.deleteCountryByCode(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Country with id " + id + " not found");
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<CountryEntity>> partialUpdateCountry(@PathVariable String id, @RequestBody Map<String, Object> updates, @RequestHeader(name = "MJOLNIR-API-KEY") String apiKey, HttpServletRequest request) {
        String requestRole = mjolnirApiService.getRoleFromApiKey(apiKey);
        if (requestRole == null || !requestRole.equals("FULL_ACCESS"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized user.");
        Optional<CountryEntity> optionalCountry = worldService.getCountryByCode(id);
        if (!optionalCountry.isPresent()) {
            throw new ResourceNotFoundException("Country with id " + id + " not found");
        }

        CountryEntity country = optionalCountry.get();

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            switch (key) {
                case "name" -> country.setName((String) value);
                case "continent" -> country.setContinent((String) value);
                case "region" -> country.setRegion((String) value);
                case "surfaceArea" -> country.setSurfaceArea(new BigDecimal(value.toString()));
                case "indepYear" -> country.setIndepYear(Short.parseShort(value.toString()));
                case "population" -> country.setPopulation(Integer.parseInt(value.toString()));
                case "lifeExpectancy" -> country.setLifeExpectancy(new BigDecimal(value.toString()));
                case "gnp" -> country.setGnp(new BigDecimal(value.toString()));
                case "localName" -> country.setLocalName((String) value);
                case "governmentForm" -> country.setGovernmentForm((String) value);
                case "headOfState" -> country.setHeadOfState((String) value);
                case "capital" -> country.setCapital(Integer.parseInt(value.toString()));
                case "code2" -> country.setCode2((String) value);
                case "gnpold" -> country.setGNPOld(new BigDecimal(value.toString()));
                default -> throw new ResourceNotFoundException("The field '" + key + "' is not valid.");
            }
        }
        
        CountryEntity updatedCountry = countryRepository.save(country);
        EntityModel<CountryEntity> resource = EntityModel.of(worldService.getCountryByCode(country.getCode()).get());
        resource.add(linkTo(methodOn(CountryController.class).getCountry(country.getCode())).withSelfRel());
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
