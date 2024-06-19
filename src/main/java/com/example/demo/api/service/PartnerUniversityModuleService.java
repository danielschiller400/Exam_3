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

            if (pageResult.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<EntityModel<Module>> moduleResources = pageResult.getContent().stream()
                    .map(module -> EntityModel.of(module,
                            linkTo(methodOn(PartnerUniversityModuleController.class).getModuleOfPartnerUniversityById(universityId, module.getId())).withRel(RelTypes.GET_SINGLE_MODULE_OF_PARTNER_UNIVERSITY),
                            linkTo(PartnerUniversityController.class).slash(universityId).withRel(RelTypes.GET_SINGLE_PARTNER_UNIVERSITY)))
                    .collect(Collectors.toList());

            CollectionModel<EntityModel<Module>> collectionModel = CollectionModel.of(moduleResources,
                    linkTo(PartnerUniversityController.class).slash(universityId).slash("modules").withSelfRel());

            HttpHeaders headers = new HttpHeaders();
            addPaginationLinks(headers, universityId, page, size, pageResult);
            addSortingLinks(headers, universityId, sortDir);

            return new ResponseEntity<>(collectionModel, headers, HttpStatus.OK);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<EntityModel<Module>> getModuleOfPartnerUniversityById(long universityId, Long id) {
        if (!partnerUniversityRepository.existsById(universityId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Module> module = moduleRepository.findByIdAndPartnerUniversityId(id, universityId);
        if (!module.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Module moduleModel = module.get();
        return module.map(value -> new ResponseEntity<>(EntityModel.of(value,
                        linkTo(methodOn(PartnerUniversityModuleController.class).getModuleOfPartnerUniversityById(universityId, id)).withSelfRel(),
                        linkTo(PartnerUniversityController.class).slash("modules").withRel(RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY),
                        linkTo(methodOn(PartnerUniversityModuleController.class).updateModuleOfPartnerUniversity(universityId, id, moduleModel)).withRel(RelTypes.UPDATE_MODULE_OF_PARTNER_UNIVERSITY),
                        linkTo(methodOn(PartnerUniversityModuleController.class).deleteModuleOfPartnerUniversity(universityId, id)).withRel(RelTypes.DELETE_MODULE_OF_PARTNER_UNIVERSITY)), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<EntityModel<Module>> createModuleInPartnerUniversity(Long universityId, Module module) {

        Optional<PartnerUniversity> partnerUniversity = partnerUniversityRepository.findById(universityId);
        if (!partnerUniversity.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        module.setPartnerUniversity(partnerUniversity.get());  // Beziehung setzen
        Module savedModule = moduleRepository.save(module);
        EntityModel<Module> moduleResource = EntityModel.of(savedModule,
                linkTo(methodOn(PartnerUniversityModuleController.class).getModuleOfPartnerUniversityById(universityId, savedModule.getId())).withSelfRel(),
                linkTo(PartnerUniversityController.class).slash(universityId).withRel(RelTypes.GET_SINGLE_PARTNER_UNIVERSITY));
        return new ResponseEntity<>(moduleResource, HttpStatus.CREATED);
    }

    public ResponseEntity<EntityModel<Module>> updateModuleOfPartnerUniversity(long universityId, long id, Module moduleDetails) {

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
                linkTo(methodOn(PartnerUniversityModuleController.class).getModuleOfPartnerUniversityById(universityId, updatedModule.getId())).withSelfRel());
        return new ResponseEntity<>(moduleResource, HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> deleteModuleOfPartnerUniversity(Long universityId, Long id) {
        if (!partnerUniversityRepository.existsById(universityId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Module> module = moduleRepository.findByIdAndPartnerUniversityId(id, universityId);
        if (!module.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            moduleRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void addPaginationLinks(HttpHeaders headers, long id, int page, int size, Page<Module> pageResult) {
        Link baseLink = linkTo(PartnerUniversityController.class).slash(id).slash("modules").withRel(RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY);

        String urlWithPagination = "<" + baseLink.getHref() + "?page=" + page + "&size=" + size + ">; rel=\"self\";type=\"application/json\"";

        //String selfLink = "<" + baseUrl + ">; rel=\"self\"";
        headers.add(HttpHeaders.LINK, urlWithPagination);

        if (pageResult.hasNext()) {
            urlWithPagination = "<" + baseLink.getHref() + "?page=" + (page + 1) + "&size=" + size + ">; rel=\"next\";type=\"application/json\"";
            headers.add(HttpHeaders.LINK, urlWithPagination);
        }

        if (pageResult.hasPrevious()) {
            urlWithPagination = "<" + baseLink.getHref() + "?page=" + (page - 1) + "&size=" + size + ">; rel=\"prev\";type=\"application/json\"";
            headers.add(HttpHeaders.LINK, urlWithPagination);
        }
    }

    private void addSortingLinks(HttpHeaders headers, long id, String sortDir) {
        Link baseLink = linkTo(PartnerUniversityController.class).slash(id).slash("modules").withRel(RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY);


        if(sortDir.equalsIgnoreCase("asc")){
            String sortDescLink = "<" + baseLink.getHref() + "?sortDir=desc" + ">; rel=\"sortDesc\";type=\"application/json\"";
            headers.add(HttpHeaders.LINK, sortDescLink);
        }else {
            String sortAscLink = "<" + baseLink.getHref() + "?sortDir=asc" + ">; rel=\"sortAsc\";type=\"application/json\"";
            headers.add(HttpHeaders.LINK, sortAscLink);
        }
    }
}
