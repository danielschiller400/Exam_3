package com.example.demo.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="PartnerUniversity")
public class PartnerUniversity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;
    private String department;
    private String websiteUrl;
    private String contactPerson;

    @Min(value = 0, message = "value has to be >= 0")
    private int maxOutgoingStudents;

    @Min(value = 0, message = "value has to be >= 0")
    private int maxIncomingStudents;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date nextSpringSemesterStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date nextAutumnSemesterStart;

    public PartnerUniversity(String name, String country, String department, String websiteUrl, String contactPerson, int maxOutgoingStudents, int maxIncomingStudents, Date nextSpringSemesterStart, Date nextAutumnSemesterStart) {
        this.name = name;
        this.country = country;
        this.department = department;
        this.websiteUrl = websiteUrl;
        this.contactPerson = contactPerson;
        this.maxOutgoingStudents = maxOutgoingStudents;
        this.maxIncomingStudents = maxIncomingStudents;
        this.nextSpringSemesterStart = nextSpringSemesterStart;
        this.nextAutumnSemesterStart = nextAutumnSemesterStart;
    }

    public PartnerUniversity() {
    }

    @OneToMany(mappedBy = "partnerUniversity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Module> modules;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public int getMaxOutgoingStudents() {
        return maxOutgoingStudents;
    }

    public void setMaxOutgoingStudents(int maxOutgoingStudents) {
        this.maxOutgoingStudents = maxOutgoingStudents;
    }

    public int getMaxIncomingStudents() {
        return maxIncomingStudents;
    }

    public void setMaxIncomingStudents(int maxIncomingStudents) {
        this.maxIncomingStudents = maxIncomingStudents;
    }

    public Date getNextSpringSemesterStart() {
        return nextSpringSemesterStart;
    }

    public void setNextSpringSemesterStart(Date nextSpringSemesterStart) {
        this.nextSpringSemesterStart = nextSpringSemesterStart;
    }

    public Date getNextAutumnSemesterStart() {
        return nextAutumnSemesterStart;
    }

    public void setNextAutumnSemesterStart(Date nextAutumnSemesterStart) {
        this.nextAutumnSemesterStart = nextAutumnSemesterStart;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
}