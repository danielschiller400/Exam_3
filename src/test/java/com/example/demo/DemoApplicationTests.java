package com.example.demo;

import com.example.demo.api.controller.RelTypes;
import com.example.demo.client.Frontend;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {


    @LocalServerPort
    private int port;

    @Autowired
    private Frontend frontend;


    @Test
    public void is_dispatcher_available() {
        // Direkt die Dispatcher-URL aufrufen und die Links prüfen
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void test_is_create_partner_university_allowed() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        String jsonStringGetDispatcher = frontend.responseToJsonString(response);

        assertThat(frontend.jsonStringContainsLink(jsonStringGetDispatcher, RelTypes.CREATE_PARTNER_UNIVERSITY)).isTrue();
    }

    @Test
    public void test_create_partner_university() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        String jsonStringGetDispatcher = frontend.responseToJsonString(response);
        String url = frontend.findLinkForRelInJson(jsonStringGetDispatcher, RelTypes.CREATE_PARTNER_UNIVERSITY);

        String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                " \"nextAutumnSemesterStart\": \"test date 2\" }";
        ResponseEntity<String> response2 = frontend.createPartnerUniversity(port, partnerUniversityJson);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void is_get_all_partner_universities_allowed(){
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        String jsonStringGetDispatcher = frontend.responseToJsonString(response);

        assertThat(frontend.jsonStringContainsLink(jsonStringGetDispatcher, RelTypes.GET_ALL_PARTNER_UNIVERSITIES)).isTrue();
    }


    @Test
    public void test_create_5_partner_universities_and_get_all_partner_universities() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void test_is_get_single_partner_university_available() {
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetAllPU = frontend.responseToJsonString(response2);

        assertThat(frontend.jsonStringContainsLink(jsonStringGetAllPU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY)).isTrue();
    }


    @Test
    public void test_is_get_single_partner_university() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetSinglePU = frontend.responseToJsonString(response2);

        assertThat(frontend.jsonStringContainsLink(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY)).isTrue();

        //Check for single partnerUniversity Url
        String url3 = frontend.findLinkForRelInJson(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void test_update_partner_university_is_available() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetSinglePU = frontend.responseToJsonString(response2);

        //Check for single partnerUniversity Url
        String url3 = frontend.findLinkForRelInJson(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);

        String jsonStringUpdatePU = frontend.responseToJsonString(response3);

        assertThat(frontend.jsonStringContainsLink(jsonStringUpdatePU, RelTypes.UPDATE_SINGLE_PARTNER_UNIVERSITY)).isTrue();
    }


    @Test
    public void test_update_partner_university() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetSinglePU = frontend.responseToJsonString(response2);

        //Check for single partnerUniversity Url
        String url3 = frontend.findLinkForRelInJson(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonStringUpdatePU = frontend.responseToJsonString(response3);

        assertThat(frontend.jsonStringContainsLink(jsonStringUpdatePU, RelTypes.UPDATE_SINGLE_PARTNER_UNIVERSITY)).isTrue();

        String url4 = frontend.findLinkForRelInJson(jsonStringUpdatePU, RelTypes.UPDATE_SINGLE_PARTNER_UNIVERSITY);

        String partnerUniversityUpdatedJson = "{ \"name\": \"Test University Updated\", \"country\": \"Test Country\"," +
                " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                " \"nextAutumnSemesterStart\": \"test date 2\" }";

        ResponseEntity<String> response4 = frontend.updatePartnerUniversity(url4, partnerUniversityUpdatedJson);
        assertThat(response4.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void test_delete_partner_university_available() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetSinglePU = frontend.responseToJsonString(response2);

        //Check for single partnerUniversity Url
        String url3 = frontend.findLinkForRelInJson(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);

        String jsonStringUpdatePU = frontend.responseToJsonString(response3);

        assertThat(frontend.jsonStringContainsLink(jsonStringUpdatePU, RelTypes.DELETE_SINGLE_PARTNER_UNIVERSITY)).isTrue();
    }


    @Test
    public void test_delete_partner_university() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetSinglePU = frontend.responseToJsonString(response2);

        assertThat(frontend.jsonStringContainsLink(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY)).isTrue();

        //Check for single partnerUniversity Url
        String url3 = frontend.findLinkForRelInJson(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY);

        ResponseEntity<Void> response3 = frontend.deletePartnerUniversity(url3);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


    //-----------------------------------------------------------------------------------------------------------------
    //Module Tests

    @Test
    public void test_create_module_in_partner_university_allowed() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetSinglePU = frontend.responseToJsonString(response2);

        assertThat(frontend.jsonStringContainsLink(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY)).isTrue();

        //Check for single partnerUniversity Url
        String url3 = frontend.findLinkForRelInJson(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);

        String jsonStringCreateModule = frontend.responseToJsonString(response3);

        assertThat(frontend.jsonStringContainsLink(jsonStringCreateModule, RelTypes.CREATE_MODULE_IN_PARTNER_UNIVERSITY)).isTrue();

    }


    @Test
    public void test_create_module_of_partner_university() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetSinglePU = frontend.responseToJsonString(response2);

        assertThat(frontend.jsonStringContainsLink(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY)).isTrue();

        //Check for single partnerUniversity Url
        String url3 = frontend.findLinkForRelInJson(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonStringCreateModule = frontend.responseToJsonString(response3);
        String url4 = frontend.findLinkForRelInJson(jsonStringCreateModule, RelTypes.CREATE_MODULE_IN_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        ResponseEntity<String> response4 = frontend.createModuleInPartnerUniversity(url4, moduleJson);
        assertThat(response4.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }


    @Test
    public void test_get_all_modules_of_partner_university_allowed() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetSinglePU = frontend.responseToJsonString(response2);

        //Check for single partnerUniversity Url
        String url3 = frontend.findLinkForRelInJson(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);

        String jsonStringGetAllPModules = frontend.responseToJsonString(response3);
        assertThat(frontend.jsonStringContainsLink(jsonStringGetAllPModules, RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY)).isTrue();
    }

    @Test
    public void test_get_all_modules_of_partner_university() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetSinglePU = frontend.responseToJsonString(response2);

        //Check for single partnerUniversity Url
        String url3 = frontend.findLinkForRelInJson(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonStringGetAllPModules = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonStringGetAllPModules, RelTypes.CREATE_MODULE_IN_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        frontend.createModuleInPartnerUniversity(url4, moduleJson);

        ResponseEntity<String> response5 = frontend.getAllModulesForPartnerUniversity(url4);
        assertThat(response5.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void test_get_single_module_of_partner_university_allowed() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetSinglePU = frontend.responseToJsonString(response2);

        //Check for single partnerUniversity Url
        String url3 = frontend.findLinkForRelInJson(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonStringGetAllPModules = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonStringGetAllPModules, RelTypes.CREATE_MODULE_IN_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        frontend.createModuleInPartnerUniversity(url4, moduleJson);

        ResponseEntity<String> response5 = frontend.getAllModulesForPartnerUniversity(url4);

        String jsonStringGetSingleModule = frontend.responseToJsonString(response5);

        assertThat(frontend.jsonStringContainsLink(jsonStringGetSingleModule, RelTypes.GET_SINGLE_MODULE_OF_PARTNER_UNIVERSITY)).isTrue();
    }


    @Test
    public void test_get_single_module_of_partner_university() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetSinglePU = frontend.responseToJsonString(response2);

        //Check for single partnerUniversity Url
        String url3 = frontend.findLinkForRelInJson(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonStringGetAllPModules = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonStringGetAllPModules, RelTypes.CREATE_MODULE_IN_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        frontend.createModuleInPartnerUniversity(url4, moduleJson);

        ResponseEntity<String> response5 = frontend.getAllModulesForPartnerUniversity(url4);

        String jsonStringGetSingleModule = frontend.responseToJsonString(response5);

        String url5 = frontend.findLinkForRelInJson(jsonStringGetSingleModule, RelTypes.GET_SINGLE_MODULE_OF_PARTNER_UNIVERSITY);
        ResponseEntity<String> response6 = frontend.getModuleOfPartnerUniversity(url5);
        assertThat(response6.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void test_update_single_module_of_partner_university_allowed() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetSinglePU = frontend.responseToJsonString(response2);

        //Check for single partnerUniversity Url
        String url3 = frontend.findLinkForRelInJson(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonStringGetAllPModules = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonStringGetAllPModules, RelTypes.CREATE_MODULE_IN_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        frontend.createModuleInPartnerUniversity(url4, moduleJson);

        ResponseEntity<String> response5 = frontend.getAllModulesForPartnerUniversity(url4);

        String jsonStringGetSingleModule = frontend.responseToJsonString(response5);

        String url5 = frontend.findLinkForRelInJson(jsonStringGetSingleModule, RelTypes.GET_SINGLE_MODULE_OF_PARTNER_UNIVERSITY);
        ResponseEntity<String> response6 = frontend.getModuleOfPartnerUniversity(url5);

        String jsonStringUpdateModule = frontend.responseToJsonString(response6);

        assertThat(frontend.jsonStringContainsLink(jsonStringUpdateModule, RelTypes.UPDATE_MODULE_OF_PARTNER_UNIVERSITY)).isTrue();
    }


    @Test
    public void test_update_single_module_of_partner_university() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetSinglePU = frontend.responseToJsonString(response2);

        //Check for single partnerUniversity Url
        String url3 = frontend.findLinkForRelInJson(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonStringGetAllPModules = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonStringGetAllPModules, RelTypes.CREATE_MODULE_IN_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        frontend.createModuleInPartnerUniversity(url4, moduleJson);

        ResponseEntity<String> response5 = frontend.getAllModulesForPartnerUniversity(url4);

        String jsonStringGetSingleModule = frontend.responseToJsonString(response5);

        String url5 = frontend.findLinkForRelInJson(jsonStringGetSingleModule, RelTypes.GET_SINGLE_MODULE_OF_PARTNER_UNIVERSITY);
        ResponseEntity<String> response6 = frontend.getModuleOfPartnerUniversity(url5);

        String jsonStringUpdateModule = frontend.responseToJsonString(response6);

        String url6 = frontend.findLinkForRelInJson(jsonStringUpdateModule, RelTypes.UPDATE_MODULE_OF_PARTNER_UNIVERSITY);

        String updatedModuleJson = "{ \"name\": \"Test module updated\", \"semester\": 3," + " \"creditPoints\": 7}";

        ResponseEntity<String> response7 = frontend.updateModuleOfPartnerUniversity(url6, updatedModuleJson);
        assertThat(response7.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void test_delete_single_module_of_partner_university_allowed() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetSinglePU = frontend.responseToJsonString(response2);

        //Check for single partnerUniversity Url
        String url3 = frontend.findLinkForRelInJson(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonStringGetAllPModules = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonStringGetAllPModules, RelTypes.CREATE_MODULE_IN_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        frontend.createModuleInPartnerUniversity(url4, moduleJson);

        ResponseEntity<String> response5 = frontend.getAllModulesForPartnerUniversity(url4);

        String jsonStringGetSingleModule = frontend.responseToJsonString(response5);

        String url5 = frontend.findLinkForRelInJson(jsonStringGetSingleModule, RelTypes.GET_SINGLE_MODULE_OF_PARTNER_UNIVERSITY);
        ResponseEntity<String> response6 = frontend.getModuleOfPartnerUniversity(url5);

        String jsonStringDeleteModule = frontend.responseToJsonString(response6);
        assertThat(frontend.jsonStringContainsLink(jsonStringDeleteModule, RelTypes.DELETE_MODULE_OF_PARTNER_UNIVERSITY)).isTrue();
    }


    @Test
    public void test_delete_single_module_of_partner_university() {

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"test date\"," +
                    " \"nextAutumnSemesterStart\": \"test date 2\" }";
            frontend.createPartnerUniversity(port, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(port);

        String jsonStringGetSinglePU = frontend.responseToJsonString(response2);

        //Check for single partnerUniversity Url
        String url3 = frontend.findLinkForRelInJson(jsonStringGetSinglePU, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonStringGetAllPModules = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonStringGetAllPModules, RelTypes.CREATE_MODULE_IN_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        frontend.createModuleInPartnerUniversity(url4, moduleJson);

        ResponseEntity<String> response5 = frontend.getAllModulesForPartnerUniversity(url4);

        String jsonStringGetSingleModule = frontend.responseToJsonString(response5);

        String url5 = frontend.findLinkForRelInJson(jsonStringGetSingleModule, RelTypes.GET_SINGLE_MODULE_OF_PARTNER_UNIVERSITY);
        ResponseEntity<String> response6 = frontend.getModuleOfPartnerUniversity(url5);

        String jsonStringDeleteModule = frontend.responseToJsonString(response6);

        String url6 = frontend.findLinkForRelInJson(jsonStringDeleteModule, RelTypes.DELETE_MODULE_OF_PARTNER_UNIVERSITY);
        ResponseEntity<Void> response7 = frontend.deleteModuleOfPartnerUniversity(url6);
        assertThat(response7.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}