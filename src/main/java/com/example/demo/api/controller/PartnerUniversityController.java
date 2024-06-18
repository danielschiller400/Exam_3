package com.example.demo.api.controller;

import com.example.demo.api.model.PartnerUniversity;
import com.example.demo.api.service.PartnerUniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(BaseURI.BASE_URI + "/partnerUniversities")
public class PartnerUniversityController {

    @Autowired
    private PartnerUniversityService partnerUniversityService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PartnerUniversity>>> getAllPartnerUniversities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return partnerUniversityService.getAllPartnerUniversities(page, size, filter, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PartnerUniversity>> getPartnerUniversityById(@PathVariable("id") long id) {
        return partnerUniversityService.getPartnerUniversityById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<PartnerUniversity>> createPartnerUniversity(@RequestBody PartnerUniversity partnerUniversity) {
        return partnerUniversityService.createPartnerUniversity(partnerUniversity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PartnerUniversity>> updatePartnerUniversity(@PathVariable("id") long id, @RequestBody PartnerUniversity partnerUniversity) {
        return partnerUniversityService.updatePartnerUniversity(id, partnerUniversity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePartnerUniversity(@PathVariable("id") long id) {
        return partnerUniversityService.deletePartnerUniversity(id);
    }

}