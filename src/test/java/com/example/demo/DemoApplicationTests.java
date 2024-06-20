package com.example.demo;

import com.example.demo.api.controller.RelTypes;
import com.example.demo.client.Frontend;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
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
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        assertThat(frontend.searchForRelInResponse(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY)).isTrue();
    }

    @Test
    public void test_create_partner_university() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);

        String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
        ResponseEntity<String> response2 = frontend.createPartnerUniversity(url, partnerUniversityJson);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void is_get_all_partner_universities_allowed(){
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        assertThat(frontend.searchForRelInResponse(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES)).isTrue();
    }


    @Test
    public void test_create_5_partner_universities_and_get_all_partner_universities() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void test_is_get_single_partner_university_available() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url);

        HttpHeaders headers2 = response2.getHeaders();

        List<String> linkHeaders2 = headers2.get("Link");

        assertThat(frontend.searchForRelInResponse(linkHeaders2, RelTypes.GET_SINGLE_PARTNER_UNIVERSITY)).isTrue();
    }


    @Test
    public void test_is_get_single_partner_university() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);

        String jsonResponseString = frontend.responseToJsonString(response2);

        String url3 = frontend.findLinkForRelInJson(jsonResponseString, RelTypes.SELF);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);
        assertThat(response3.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void test_update_partner_university_is_available() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }

        //Get all partnerUniversities
        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);

        String jsonResponseString = frontend.responseToJsonString(response2);

        String url3 = frontend.findLinkForRelInJson(jsonResponseString, RelTypes.SELF);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        HttpHeaders headers3 = response3.getHeaders();

        List<String> linkHeaders3 = headers3.get("Link");

        assertThat(frontend.searchForRelInResponse(linkHeaders3, RelTypes.UPDATE_SINGLE_PARTNER_UNIVERSITY)).isTrue();

    }


    @Test
    public void test_update_partner_university() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }

        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);

        String jsonResponseString = frontend.responseToJsonString(response2);

        String url3 = frontend.findLinkForRelInJson(jsonResponseString, RelTypes.SELF);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonResponseString2 = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonResponseString2, RelTypes.SELF);

        String partnerUniversityUpdatedJson = "{ \"name\": \"Test University Update\", \"country\": \"Test Country\"," +
                " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                " \"nextAutumnSemesterStart\": \"2024-09-15\" }";

        ResponseEntity<String> response4 = frontend.updatePartnerUniversity(url4, partnerUniversityUpdatedJson);
        assertThat(response4.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void test_delete_partner_university_allowed() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }

        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);

        String jsonResponseString = frontend.responseToJsonString(response2);

        String url3 = frontend.findLinkForRelInJson(jsonResponseString, RelTypes.SELF);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        HttpHeaders headers3 = response3.getHeaders();

        List<String> linkHeaders3 = headers3.get("Link");

        assertThat(frontend.searchForRelInResponse(linkHeaders3, RelTypes.DELETE_SINGLE_PARTNER_UNIVERSITY)).isTrue();
    }


    @Test
    public void test_delete_partner_university() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }

        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);

        String jsonResponseString = frontend.responseToJsonString(response2);

        String url3 = frontend.findLinkForRelInJson(jsonResponseString, RelTypes.SELF);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonResponseString2 = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonResponseString2, RelTypes.SELF);

        ResponseEntity<Void> response4 = frontend.deletePartnerUniversity(url4);
        assertThat(response4.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


    //-----------------------------------------------------------------------------------------------------------------
    //Module Tests

    @Test
    public void test_create_module_in_partner_university_allowed() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }

        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);

        String jsonResponseString = frontend.responseToJsonString(response2);

        String url3 = frontend.findLinkForRelInJson(jsonResponseString, RelTypes.SELF);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        HttpHeaders headers3 = response3.getHeaders();

        List<String> linkHeaders3 = headers3.get("Link");

        assertThat(frontend.searchForRelInResponse(linkHeaders3, RelTypes.CREATE_MODULE_IN_PARTNER_UNIVERSITY)).isTrue();
    }


    @Test
    public void test_create_module_of_partner_university() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }

        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);

        String jsonResponseString = frontend.responseToJsonString(response2);

        String url3 = frontend.findLinkForRelInJson(jsonResponseString, RelTypes.SELF);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonResponseString2 = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonResponseString2, RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        ResponseEntity<String> response4 = frontend.createModuleInPartnerUniversity(url4, moduleJson);
        assertThat(response4.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }


    @Test
    public void test_get_all_modules_of_partner_university_allowed() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }

        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);

        String jsonResponseString = frontend.responseToJsonString(response2);

        String url3 = frontend.findLinkForRelInJson(jsonResponseString, RelTypes.SELF);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        HttpHeaders headers3 = response3.getHeaders();

        List<String> linkHeaders3 = headers3.get("Link");

        assertThat(frontend.searchForRelInResponse(linkHeaders3, RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY)).isTrue();
    }

    @Test
    public void test_get_all_modules_of_partner_university() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }

        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);

        String jsonResponseString = frontend.responseToJsonString(response2);

        String url3 = frontend.findLinkForRelInJson(jsonResponseString, RelTypes.SELF);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonResponseString2 = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonResponseString2, RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        frontend.createModuleInPartnerUniversity(url4, moduleJson);

        ResponseEntity<String> response5 = frontend.getAllModulesForPartnerUniversity(url4);
        assertThat(response5.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void test_get_single_module_of_partner_university_allowed() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }

        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);

        String jsonResponseString = frontend.responseToJsonString(response2);

        String url3 = frontend.findLinkForRelInJson(jsonResponseString, RelTypes.SELF);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonResponseString2 = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonResponseString2, RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        frontend.createModuleInPartnerUniversity(url4, moduleJson);

        ResponseEntity<String> response5 = frontend.getAllModulesForPartnerUniversity(url4);

        HttpHeaders headers4 = response5.getHeaders();

        List<String> linkHeaders4 = headers4.get("Link");

        assertThat(frontend.searchForRelInResponse(linkHeaders4, RelTypes.GET_SINGLE_MODULE_OF_PARTNER_UNIVERSITY)).isTrue();
    }


    @Test
    public void test_get_single_module_of_partner_university() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }

        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);

        String jsonResponseString = frontend.responseToJsonString(response2);

        String url3 = frontend.findLinkForRelInJson(jsonResponseString, RelTypes.SELF);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonResponseString2 = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonResponseString2, RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        frontend.createModuleInPartnerUniversity(url4, moduleJson);

        ResponseEntity<String> response5 = frontend.getAllModulesForPartnerUniversity(url4);

        String jsonResponseString3 = frontend.responseToJsonString(response5);

        String url6 = frontend.findLinkForRelInJson(jsonResponseString3, RelTypes.SELF);

        ResponseEntity<String> response6 = frontend.getModuleOfPartnerUniversity(url6);
        assertThat(response6.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void test_update_single_module_of_partner_university_allowed() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }

        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);

        String jsonResponseString = frontend.responseToJsonString(response2);

        String url3 = frontend.findLinkForRelInJson(jsonResponseString, RelTypes.SELF);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonResponseString2 = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonResponseString2, RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        frontend.createModuleInPartnerUniversity(url4, moduleJson);

        ResponseEntity<String> response5 = frontend.getAllModulesForPartnerUniversity(url4);

        String jsonResponseString3 = frontend.responseToJsonString(response5);

        String url5 = frontend.findLinkForRelInJson(jsonResponseString3, RelTypes.SELF);

        ResponseEntity<String> response6 = frontend.getModuleOfPartnerUniversity(url5);

        HttpHeaders headers5 = response6.getHeaders();

        List<String> linkHeaders5 = headers5.get("Link");

        assertThat(frontend.searchForRelInResponse(linkHeaders5, RelTypes.UPDATE_MODULE_OF_PARTNER_UNIVERSITY)).isTrue();
    }


    @Test
    public void test_update_single_module_of_partner_university() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }

        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);

        String jsonResponseString = frontend.responseToJsonString(response2);

        String url3 = frontend.findLinkForRelInJson(jsonResponseString, RelTypes.SELF);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonResponseString2 = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonResponseString2, RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        frontend.createModuleInPartnerUniversity(url4, moduleJson);

        ResponseEntity<String> response5 = frontend.getAllModulesForPartnerUniversity(url4);

        String jsonResponseString3 = frontend.responseToJsonString(response5);

        String url5 = frontend.findLinkForRelInJson(jsonResponseString3, RelTypes.SELF);

        ResponseEntity<String> response6 = frontend.getModuleOfPartnerUniversity(url5);

        String jsonResponseString4 = frontend.responseToJsonString(response6);

        String url6 = frontend.findLinkForRelInJson(jsonResponseString4, RelTypes.SELF);

        String updatedModuleJson = "{ \"name\": \"Test module updated\", \"semester\": 3," + " \"creditPoints\": 7}";

        ResponseEntity<String> response7 = frontend.updateModuleOfPartnerUniversity(url6, updatedModuleJson);
        assertThat(response7.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void test_delete_single_module_of_partner_university_allowed() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }

        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);

        String jsonResponseString = frontend.responseToJsonString(response2);

        String url3 = frontend.findLinkForRelInJson(jsonResponseString, RelTypes.SELF);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonResponseString2 = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonResponseString2, RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        frontend.createModuleInPartnerUniversity(url4, moduleJson);

        ResponseEntity<String> response5 = frontend.getAllModulesForPartnerUniversity(url4);

        String jsonResponseString3 = frontend.responseToJsonString(response5);

        String url5 = frontend.findLinkForRelInJson(jsonResponseString3, RelTypes.SELF);

        ResponseEntity<String> response6 = frontend.getModuleOfPartnerUniversity(url5);

        HttpHeaders headers5 = response6.getHeaders();

        List<String> linkHeaders5 = headers5.get("Link");

        assertThat(frontend.searchForRelInResponse(linkHeaders5, RelTypes.DELETE_MODULE_OF_PARTNER_UNIVERSITY)).isTrue();
    }


    @Test
    public void test_delete_single_module_of_partner_university() {
        ResponseEntity<Map<String, String>> response = frontend.getDispatcher(port);
        HttpHeaders headers = response.getHeaders();
        List<String> linkHeaders = headers.get("Link");
        String url = frontend.getLinkFromRel(linkHeaders, RelTypes.CREATE_PARTNER_UNIVERSITY);
        String url2 = frontend.getLinkFromRel(linkHeaders, RelTypes.GET_ALL_PARTNER_UNIVERSITIES);

        for (int i = 0; i < 5; i++) {
            String partnerUniversityJson = "{ \"name\": \"Test University\", \"country\": \"Test Country\"," +
                    " \"department\": \"IT\", \"websiteUrl\": \"Test url\", \"contactPerson\": \"Test person\"," +
                    " \"maxOutgoingStudents\": 100, \"maxIncomingStudents\": 50, \"nextSpringSemesterStart\": \"2024-03-01\"," +
                    " \"nextAutumnSemesterStart\": \"2024-09-15\" }";
            frontend.createPartnerUniversity(url, partnerUniversityJson);
        }

        ResponseEntity<String> response2 = frontend.getAllPartnerUniversities(url2);

        String jsonResponseString = frontend.responseToJsonString(response2);

        String url3 = frontend.findLinkForRelInJson(jsonResponseString, RelTypes.SELF);

        ResponseEntity<String> response3 = frontend.getPartnerUniversityById(url3);

        String jsonResponseString2 = frontend.responseToJsonString(response3);

        String url4 = frontend.findLinkForRelInJson(jsonResponseString2, RelTypes.GET_ALL_MODULES_OF_PARTNER_UNIVERSITY);

        String moduleJson = "{ \"name\": \"Test module\", \"semester\": 2," + " \"creditPoints\": 7}";

        frontend.createModuleInPartnerUniversity(url4, moduleJson);

        ResponseEntity<String> response5 = frontend.getAllModulesForPartnerUniversity(url4);

        String jsonResponseString3 = frontend.responseToJsonString(response5);

        String url5 = frontend.findLinkForRelInJson(jsonResponseString3, RelTypes.SELF);

        ResponseEntity<String> response6 = frontend.getModuleOfPartnerUniversity(url5);

        String jsonResponseString4 = frontend.responseToJsonString(response6);

        String url6 = frontend.findLinkForRelInJson(jsonResponseString4, RelTypes.SELF);

        ResponseEntity<Void> response7 = frontend.deleteModuleOfPartnerUniversity(url6);
        assertThat(response7.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}