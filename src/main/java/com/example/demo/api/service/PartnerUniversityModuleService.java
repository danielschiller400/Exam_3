package com.example.demo.api.service;

import com.example.demo.api.controller.PartnerUniversityController;
import com.example.demo.api.controller.PartnerUniversityModuleController;
import com.example.demo.api.controller.RelTypes;
import com.example.demo.api.model.Module;
import com.example.demo.api.model.PartnerUniversity;
import com.example.demo.api.repository.ModuleRepository;
import com.example.demo.api.repository.PartnerUniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.*;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PartnerUniversityModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private PartnerUniversityRepository partnerUniversityRepository;

    public ResponseEntity<CollectionModel<EntityModel<Module>>> getAllModulesOfPartnerUniversities(Long universityId, int page, int size, String sortBy, String sortDir) {
        try {
            if (!partnerUniversityRepository.existsById(universityId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Pageable paging = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy);
            Page<Module> pageResult = moduleRepository.findByPartnerUniversityId(universityId, paging);

            HttpHeaders headers = new HttpHeaders();
            addPaginationLinks(headers, universityId, page, size, pageResult);
            addSortingLinks(headers, universityId, sortDir);
            addCrudLinksCollectionNoContent(headers, universityId);

            if (pageResult.isEmpty()) {
                return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
            }

            List<EntityModel<Module>> moduleResources = pageResult.getContent().stream()
                    .map(module -> EntityModel.of(module,
                            linkTo(methodOn(PartnerUniversityModuleController.class).getModuleOfPartnerUniversityById(universityId, module.getId())).withRel(RelTypes.SELF).withType("*/*")))
                    .collect(Collectors.toList());

            CollectionModel<EntityModel<Module>> collectionModel = CollectionModel.of(moduleResources,
                    linkTo(PartnerUniversityController.class).slash(universityId).slash("modules").withSelfRel().withType("*/*"));

            addCrudLinksGetCollection(headers, universityId);

            return new ResponseEntity<>(collectionModel, headers, HttpStatus.OK);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<EntityModel<Module>> getModuleOfPartnerUniversityById(long universityId, Long id) {
        try {

            HttpHeaders headers = new HttpHeaders();
            addCrudLinksGetSingleNoContent(headers, universityId);

            if (!partnerUniversityRepository.existsById(universityId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Optional<Module> module = moduleRepository.findByIdAndPartnerUniversityId(id, universityId);
            if (!module.isPresent()) {
                return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
            }

            addCrudLinksGetSingle(headers, universityId, module.get().getId());

            Module moduleModel = module.get();
            return module.map(value -> new ResponseEntity<>(EntityModel.of(value,
                            linkTo(methodOn(PartnerUniversityModuleController.class).getModuleOfPartnerUniversityById(universityId, id)).withSelfRel().withType("*/*")), headers, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<EntityModel<Module>> createModuleInPartnerUniversity(Long universityId, Module module) {
        try {
            Optional<PartnerUniversity> partnerUniversity = partnerUniversityRepository.findById(universityId);
            if (!partnerUniversity.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            module.setPartnerUniversity(partnerUniversity.get());
            Module savedModule = moduleRepository.save(module);
            EntityModel<Module> moduleResource = EntityModel.of(savedModule,
                    linkTo(methodOn(PartnerUniversityModuleController.class).getModuleOfPartnerUniversityById(universityId, savedModule.getId())).withSelfRel().withType("*/*"),
                    linkTo(PartnerUniversityController.class).slash(universityId).withRel(RelTypes.GET_SINGLE_PARTNER_UNIVERSITY).withType("*/*"));

            HttpHeaders headers = new HttpHeaders();
            addCrudLinksPostPut(headers, universityId, module.getId());

            return new ResponseEntity<>(moduleResource, headers, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<EntityModel<Module>> updateModuleOfPartnerUniversity(long universityId, long id, Module moduleDetails) {
        try{
            if (!partnerUniversityRepository.existsById(universityId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Optional<Module> module = moduleRepository.findByIdAndPartnerUniversityId(id, universityId);
            if (!module.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Module existingModule = module.get();
            existingModule.setName(moduleDetails.getName());
            existingModule.setSemester(moduleDetails.getSemester());
            existingModule.setCreditPoints(moduleDetails.getCreditPoints());
            Module updatedModule = moduleRepository.save(existingModule);
            EntityModel<Module> moduleResource = EntityModel.of(updatedModule,
                    linkTo(methodOn(PartnerUniversityModuleController.class).getModuleOfPartnerUniversityById(universityId, updatedModule.getId())).withSelfRel().withType("*/*"));

            HttpHeaders headers = new HttpHeaders();
            addCrudLinksPostPut(headers, universityId, updatedModule.getId());

            return new ResponseEntity<>(moduleResource, headers, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<HttpStatus> deleteModuleOfPartnerUniversity(Long universityId, Long id) {
        if (!partnerUniversityRepository.existsById(universityId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        addCrudLinksDelete(headers, universityId);

        Optional<Module> module = moduleRepository.findByIdAndPartnerUniversityId(id, universityId);
        if (!module.isPresent()) {
            return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
        }

        try {
            moduleRepository.deleteById(id);
            return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private void addCrudLinksGetCollection(HttpHeaders header, long id){
        String getSingle = "<" + linkTo(PartnerUniversityController.class).slash(id).withRel(RelTypes.GET_SINGLE_MODULE_OF_PARTNER_UNIVERSITY).getHref()+ "/modules" + "/{id}" + ">; rel=\"" + RelTypes.GET_SINGLE_MODULE_OF_PARTNER_UNIVERSITY + "\";type=\"*/*\"";
        header.add(HttpHeaders.LINK, getSingle);
    }

    private void addCrudLinksCollectionNoContent(HttpHeaders header, long id){
        String create = "<" + linkTo(PartnerUniversityController.class).slash(id).withRel(RelTypes.CREATE_MODULE_IN_PARTNER_UNIVERSITY).getHref() + "/modules" + ">; rel=\"" + RelTypes.CREATE_MODULE_IN_PARTNER_UNIVERSITY + "\";type=\"application/json\"";
        header.add(HttpHeaders.LINK, create);

        String getSingle = "<" + linkTo(PartnerUniversityController.class).slash(id).withRel(RelTypes.GET_SINGLE_PARTNER_UNIVERSITY).getHref() + ">; rel=\"" + RelTypes.GET_SINGLE_PARTNER_UNIVERSITY + "\";type=\"*/*\"";
        header.add(HttpHeaders.LINK, getSingle);
    }


    private void addCrudLinksGetSingle(HttpHeaders header, long id, long moduleId){

        String update = "<" + linkTo(PartnerUniversityController.class).slash(id).withRel(RelTypes.UPDATE_MODULE_OF_PARTNER_UNIVERSITY).getHref() + "/modules" + "/" + moduleId + ">; rel=\"" + RelTypes.UPDATE_MODULE_OF_PARTNER_UNIVERSITY + "\";type=\"application/json\"";
        header.add(HttpHeaders.LINK, update);

        String delete = "<" + linkTo(PartnerUniversityController.class).slash(id).withRel(RelTypes.DELETE_MODULE_OF_PARTNER_UNIVERSITY).getHref() + "/modules" + "/" + moduleId + ">; rel=\"" + RelTypes.DELETE_MODULE_OF_PARTNER_UNIVERSITY + "\";type=\"*/*\"";
        header.add(HttpHeaders.LINK, delete);
    }

    private void addCrudLinksGetSingleNoContent(HttpHeaders header, long id){
        String getAllModules = "<" + linkTo(PartnerUniversityController.class).slash(id).withRel(RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY).getHref() + "/modules" + ">; rel=\"" + RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY + "\";type=\"*/*\"";
        header.add(HttpHeaders.LINK, getAllModules);
    }


    private void addCrudLinksPostPut(HttpHeaders header, long id, long moduleId){
        String getSingle = "<" + linkTo(PartnerUniversityController.class).slash(id).withRel(RelTypes.GET_SINGLE_MODULE_OF_PARTNER_UNIVERSITY).getHref() + "/modules" + "/" + moduleId + ">; rel=\"" + RelTypes.GET_SINGLE_MODULE_OF_PARTNER_UNIVERSITY + "\";type=\"*/*\"";
        header.add(HttpHeaders.LINK, getSingle);
    }

    private void addCrudLinksDelete(HttpHeaders header, long id){
        String getAll = "<" + linkTo(PartnerUniversityController.class).slash(id).withRel(RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY).getHref() + "/modules" + ">; rel=\"" + RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY + "\";type=\"*/*\"";
        header.add(HttpHeaders.LINK, getAll);

    }

    private void addPaginationLinks(HttpHeaders headers, long id, int page, int size, Page<Module> pageResult) {
        Link baseLink = linkTo(PartnerUniversityController.class).slash(id).slash("modules").withRel(RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY);

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

    private void addSortingLinks(HttpHeaders headers, long id, String sortDir) {
        Link baseLink = linkTo(PartnerUniversityController.class).slash(id).slash("modules").withRel(RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY);


        if(sortDir.equalsIgnoreCase("asc")){
            String sortDescLink = "<" + baseLink.getHref() + "?sortDir=desc" + ">; rel=\"sortDesc\";type=\"*/*\"";
            headers.add(HttpHeaders.LINK, sortDescLink);
        }else {
            String sortAscLink = "<" + baseLink.getHref() + "?sortDir=asc" + ">; rel=\"sortAsc\";type=\"*/*\"";
            headers.add(HttpHeaders.LINK, sortAscLink);
        }
    }
}
