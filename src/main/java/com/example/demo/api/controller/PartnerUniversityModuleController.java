package com.example.demo.api.controller;

import com.example.demo.api.model.Module;
import com.example.demo.api.service.PartnerUniversityModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(BaseURI.BASE_URI + "/partnerUniversities/{universityId}/modules")
public class PartnerUniversityModuleController {

    @Autowired
    private PartnerUniversityModuleService partnerUniversityModuleService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Module>>> getAllModulesOfPartnerUniversities(
            @PathVariable(value = "universityId") Long universityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir){

        return partnerUniversityModuleService.getAllModulesOfPartnerUniversities(universityId, page, size, sortBy, sortDir);

    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Module>> getModuleOfPartnerUniversityById(@PathVariable("universityId") long universityId,
                                                                                @PathVariable("id") Long id) {

        return partnerUniversityModuleService.getModuleOfPartnerUniversityById(universityId, id);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Module>> createModuleInPartnerUniversity(@PathVariable(value = "universityId") Long universityId,
                                                                               @RequestBody Module module) {

        return partnerUniversityModuleService.createModuleInPartnerUniversity(universityId, module);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Module>> updateModuleOfPartnerUniversity(@PathVariable(value = "universityId") long universityId,
                                                                               @PathVariable(value = "id") long id,
                                                                               @RequestBody Module moduleDetails) {

        return partnerUniversityModuleService.updateModuleOfPartnerUniversity(universityId, id, moduleDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteModuleOfPartnerUniversity(@PathVariable(value = "universityId") Long universityId,
                                                                      @PathVariable(value = "id") Long id) {

        return partnerUniversityModuleService.deleteModuleOfPartnerUniversity(universityId, id);
    }

}