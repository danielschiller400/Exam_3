package com.example.demo.api.service;

import com.example.demo.api.repository.PartnerUniversityRepository;
import com.example.demo.api.controller.PartnerUniversityController;
import com.example.demo.api.model.PartnerUniversity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.example.demo.api.controller.RelTypes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PartnerUniversityService {

    @Autowired
    private PartnerUniversityRepository partnerUniversityRepository;

    public ResponseEntity<CollectionModel<EntityModel<PartnerUniversity>>> getAllPartnerUniversities(int page, int size, String filter, String sortBy, String sortDir) {
        try {

            Pageable paging = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
            Page<PartnerUniversity> pageResult;

            if (filter != null) {
                pageResult = partnerUniversityRepository.findByNameContaining(filter, paging);
            } else {
                pageResult = partnerUniversityRepository.findAll(paging);
            }

            HttpHeaders headers = new HttpHeaders();
            addPaginationLinks(headers, page, size, pageResult);
            addSortingLinks(headers, sortDir);
            addFilteringLinks(headers);
            addCrudLinksCollectionNoContent(headers);

            if (pageResult.isEmpty()) {
                return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
            }

            List<EntityModel<PartnerUniversity>> partnerUniversityModels = pageResult.getContent().stream()
                    .map(university -> EntityModel.of(university,
                            linkTo(PartnerUniversityController.class).slash(university.getId()).withRel(RelTypes.SELF).withType("*/*"),
                            linkTo(PartnerUniversityController.class).slash(university.getId()).withRel(RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY).withType("*/*")))
                    .collect(Collectors.toList());

            CollectionModel<EntityModel<PartnerUniversity>> collectionModel = CollectionModel.of(partnerUniversityModels,
                    linkTo(PartnerUniversityController.class).withRel(RelTypes.SELF).withType("*/*"));

            addCrudLinksGetCollection(headers);

            return new ResponseEntity<>(collectionModel, headers, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<EntityModel<PartnerUniversity>> getPartnerUniversityById(long id) {
        try {
            Optional<PartnerUniversity> partnerUniversityData = partnerUniversityRepository.findById(id);

            if (partnerUniversityData.isPresent()) {
                PartnerUniversity university = partnerUniversityData.get();
                EntityModel<PartnerUniversity> partnerUniversityModel = EntityModel.of(university,
                        linkTo(methodOn(PartnerUniversityController.class).getPartnerUniversityById(id)).withSelfRel().withType("*/*"),
                        linkTo(PartnerUniversityController.class).slash(id).slash("modules").withRel(RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY).withType("*/*"));

                HttpHeaders headers = new HttpHeaders();
                addCrudLinksGetSingle(headers, id);

                return new ResponseEntity<>(partnerUniversityModel, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<EntityModel<PartnerUniversity>> createPartnerUniversity(PartnerUniversity partnerUniversity) {
        try {
            PartnerUniversity savedUniversity = partnerUniversityRepository.save(new PartnerUniversity(
                    partnerUniversity.getName(),
                    partnerUniversity.getCountry(),
                    partnerUniversity.getDepartment(),
                    partnerUniversity.getWebsiteUrl(),
                    partnerUniversity.getContactPerson(),
                    partnerUniversity.getMaxOutgoingStudents(),
                    partnerUniversity.getMaxIncomingStudents(),
                    partnerUniversity.getNextSpringSemesterStart(),
                    partnerUniversity.getNextAutumnSemesterStart()));
            EntityModel<PartnerUniversity> partnerUniversityModel = EntityModel.of(savedUniversity,
                    linkTo(methodOn(PartnerUniversityController.class).getPartnerUniversityById(savedUniversity.getId())).withSelfRel().withType("application/json"));

            HttpHeaders headers = new HttpHeaders();
            addCrudLinksPostPut(headers, savedUniversity.getId());

            return new ResponseEntity<>(partnerUniversityModel, headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<EntityModel<PartnerUniversity>> updatePartnerUniversity(long id, PartnerUniversity partnerUniversity) {
        try {
            Optional<PartnerUniversity> partnerUniversityData = partnerUniversityRepository.findById(id);

            if (partnerUniversityData.isPresent()) {
                PartnerUniversity existingUniversity = partnerUniversityData.get();
                existingUniversity.setName(partnerUniversity.getName());
                existingUniversity.setCountry(partnerUniversity.getCountry());
                existingUniversity.setDepartment(partnerUniversity.getDepartment());
                existingUniversity.setWebsiteUrl(partnerUniversity.getWebsiteUrl());
                existingUniversity.setContactPerson(partnerUniversity.getContactPerson());
                existingUniversity.setMaxOutgoingStudents(partnerUniversity.getMaxOutgoingStudents());
                existingUniversity.setMaxIncomingStudents(partnerUniversity.getMaxIncomingStudents());
                existingUniversity.setNextSpringSemesterStart(partnerUniversity.getNextSpringSemesterStart());
                existingUniversity.setNextAutumnSemesterStart(partnerUniversity.getNextAutumnSemesterStart());
                PartnerUniversity updatedUniversity = partnerUniversityRepository.save(existingUniversity);
                EntityModel<PartnerUniversity> partnerUniversityModel = EntityModel.of(updatedUniversity,
                        linkTo(methodOn(PartnerUniversityController.class).getPartnerUniversityById(updatedUniversity.getId())).withSelfRel().withType("*/*"));

                HttpHeaders headers = new HttpHeaders();
                addCrudLinksPostPut(headers, updatedUniversity.getId());

                return new ResponseEntity<>(partnerUniversityModel, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<HttpStatus> deletePartnerUniversity(long id) {
        try {
            partnerUniversityRepository.deleteById(id);

            HttpHeaders headers = new HttpHeaders();
            addCrudLinksDelete(headers);

            return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private void addCrudLinksGetCollection(HttpHeaders header){
        String getSingle = "<" + linkTo(PartnerUniversityController.class).withRel(RelTypes.GET_SINGLE_PARTNER_UNIVERSITY).getHref() + "/{id}" + ">; rel=\"" + RelTypes.GET_SINGLE_PARTNER_UNIVERSITY + "\";type=\"*/*\"";
        header.add(HttpHeaders.LINK, getSingle);
    }

    private void addCrudLinksCollectionNoContent(HttpHeaders header){
        String create = "<" + linkTo(PartnerUniversityController.class).withRel(RelTypes.CREATE_PARTNER_UNIVERSITY).getHref() + ">; rel=\"" + RelTypes.CREATE_PARTNER_UNIVERSITY + "\";type=\"application/json\"";
        header.add(HttpHeaders.LINK, create);

    }


    private void addCrudLinksGetSingle(HttpHeaders header, long id){
        String createModule = "<" + linkTo(PartnerUniversityController.class).slash("modules").withRel(RelTypes.CREATE_MODULE_IN_PARTNER_UNIVERSITY).getHref() + ">; rel=\"" + RelTypes.CREATE_MODULE_IN_PARTNER_UNIVERSITY + "\";type=\"application/json\"";
        header.add(HttpHeaders.LINK, createModule);

        String getAllModules = "<" + linkTo(PartnerUniversityController.class).slash("modules").withRel(RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY).getHref() + ">; rel=\"" + RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY + "\";type=\"*/*\"";
        header.add(HttpHeaders.LINK, getAllModules);

        String getAll = "<" + linkTo(PartnerUniversityController.class).withRel(RelTypes.GET_ALL_PARTNER_UNIVERSITIES).getHref() + ">; rel=\"" + RelTypes.GET_ALL_PARTNER_UNIVERSITIES + "\";type=\"*/*\"";
        header.add(HttpHeaders.LINK, getAll);

        String update = "<" + linkTo(PartnerUniversityController.class).slash(id).withRel(RelTypes.UPDATE_SINGLE_PARTNER_UNIVERSITY).getHref() + ">; rel=\"" + RelTypes.UPDATE_SINGLE_PARTNER_UNIVERSITY + "\";type=\"application/json\"";
        header.add(HttpHeaders.LINK, update);

        String delete = "<" + linkTo(PartnerUniversityController.class).slash(id).withRel(RelTypes.DELETE_SINGLE_PARTNER_UNIVERSITY).getHref() + ">; rel=\"" + RelTypes.DELETE_SINGLE_PARTNER_UNIVERSITY + "\";type=\"*/*\"";
        header.add(HttpHeaders.LINK, delete);
    }

    private void addCrudLinksPostPut(HttpHeaders header, long id){
        String getSingle = "<" + linkTo(PartnerUniversityController.class).slash(id).withRel(RelTypes.GET_SINGLE_PARTNER_UNIVERSITY).getHref() + ">; rel=\"" + RelTypes.GET_SINGLE_PARTNER_UNIVERSITY + "\";type=\"*/*\"";
        header.add(HttpHeaders.LINK, getSingle);
    }


    private void addCrudLinksDelete(HttpHeaders header){
        String getAll = "<" + linkTo(PartnerUniversityController.class).withRel(RelTypes.GET_ALL_PARTNER_UNIVERSITIES).getHref() + ">; rel=\"" + RelTypes.GET_ALL_PARTNER_UNIVERSITIES + "\";type=\"*/*\"";
        header.add(HttpHeaders.LINK, getAll);

    }


    private void addPaginationLinks(HttpHeaders headers, int page, int size, Page<PartnerUniversity> pageResult) {
        Link baseLink = linkTo(PartnerUniversityController.class).withRel(RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        String urlWithPagination = "<" + baseLink.getHref() + "?page=" + page + "&size=" + size + ">; rel=\"self\";type=\"*/*\"";

        //String selfLink = "<" + baseUrl + ">; rel=\"self\"";
        headers.add(HttpHeaders.LINK, urlWithPagination);

        if (pageResult.hasNext()) {
            urlWithPagination = "<" + baseLink.getHref() + "?page=" + (page + 1) + "&size=" + size + ">; rel=\"next\";type=\"*/*\"";
            headers.add(HttpHeaders.LINK, urlWithPagination);
        }

        if (pageResult.hasPrevious()) {
            urlWithPagination = "<" + baseLink.getHref() + "?page=" + (page - 1) + "&size=" + size + ">; rel=\"prev\";type=\"*/*\"";
            headers.add(HttpHeaders.LINK, urlWithPagination);
        }
    }

    private void addSortingLinks(HttpHeaders headers, String sortDir) {
        Link baseLink = linkTo(PartnerUniversityController.class).withRel(RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        if(sortDir.equalsIgnoreCase("asc")) {
            String sortDescLink = "<" + baseLink.getHref() + "?sortDir=desc" + ">; rel=\"sortDesc\";type=\"*/*\"";
            headers.add(HttpHeaders.LINK, sortDescLink);
        }else {
            String sortAscLink = "<" + baseLink.getHref() + "?sortDir=asc" + ">; rel=\"sortAsc\";type=\"*/*\"";
            headers.add(HttpHeaders.LINK, sortAscLink);
        }
    }

    private void addFilteringLinks(HttpHeaders headers){
        Link baseLink = linkTo(PartnerUniversityController.class).withRel(RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        String sortFilterLink = "<" + baseLink.getHref() + "?filter={partnerUniversityName}" + ">; rel=\"sortFilter\";type=\"*/*\"";
        headers.add(HttpHeaders.LINK, sortFilterLink);
    }
}
