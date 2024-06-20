package com.example.demo.api.controller;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(BaseURI.BASE_URI)
public class DispatcherController {

    @GetMapping
    public ResponseEntity<Map<String, String>> getApiEndpoints() {

        HttpHeaders headers = new HttpHeaders();
        addCrudLink(headers);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    public void addCrudLink(HttpHeaders headers) {
        String self = "<" + linkTo(DispatcherController.class).withRel(RelTypes.SELF).getHref() + ">; rel=\"" + RelTypes.SELF + "\";type=\"*/*\"";
        headers.add(HttpHeaders.LINK, self);

        String getAll = "<" + linkTo(PartnerUniversityController.class).withRel(RelTypes.GET_ALL_PARTNER_UNIVERSITIES).getHref() + ">; rel=\"" + RelTypes.GET_ALL_PARTNER_UNIVERSITIES + "\";type=\"*/*\"";
        headers.add(HttpHeaders.LINK, getAll);

        String create = "<" + linkTo(PartnerUniversityController.class).withRel(RelTypes.CREATE_PARTNER_UNIVERSITY).getHref() + ">; rel=\"" + RelTypes.CREATE_PARTNER_UNIVERSITY + "\";type=\"application/json\"";
        headers.add(HttpHeaders.LINK, create);
    }
}