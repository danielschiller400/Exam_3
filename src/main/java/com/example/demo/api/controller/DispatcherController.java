package com.example.demo.api.controller;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(BaseURI.BASE_URI)
public class DispatcherController {

    @GetMapping
    public ResponseEntity<Map<String, String>> getApiEndpoints() {

        Map<String, String> links = new HashMap<>();
        links.put("getAllPartnerUniversities", createLinkForAllPartnerUniversities().getHref());
        links.put("createPartnerUniversity", createLinkForCreatePartnerUniversity().getHref());

        return new ResponseEntity<>(links, HttpStatus.OK);
    }

    private Link createLinkForAllPartnerUniversities() {
        return WebMvcLinkBuilder.linkTo(PartnerUniversityController.class).withRel(RelTypes.GET_ALL_PARTNER_UNIVERSITIES);
    }

    private Link createLinkForCreatePartnerUniversity() {
        return WebMvcLinkBuilder.linkTo(PartnerUniversityController.class).withRel(RelTypes.CREATE_PARTNER_UNIVERSITY);
    }
}