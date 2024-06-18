package com.example.demo.api.controller;

public interface RelTypes {

    String SELF = "self";

    //Partner University
    String CREATE_PARTNER_UNIVERSITY = "createPartnerUniversity";
    String GET_ALL_PARTNER_UNIVERSITIES = "getAllPartnerUniversities";
    String UPDATE_SINGLE_PARTNER_UNIVERSITY = "updatePartnerUniversity";
    String DELETE_SINGLE_PARTNER_UNIVERSITY = "deletePartnerUniversity";
    String GET_SINGLE_PARTNER_UNIVERSITY = "getSinglePartnerUniversity";

    //Module
    String CREATE_MODULE_IN_PARTNER_UNIVERSITY = "createModuleInUniversity";
    String GET_ALL_MODULES_OF_PARTNER_UNIVERSITY = "getAllModulesOfUniversity";
    String UPDATE_MODULE_OF_PARTNER_UNIVERSITY = "updateModuleInUniversity";
    String DELETE_MODULE_OF_PARTNER_UNIVERSITY = "deleteModuleInUniversity";
    String GET_SINGLE_MODULE_OF_PARTNER_UNIVERSITY = "getSingleModuleOfUniversity";
}
