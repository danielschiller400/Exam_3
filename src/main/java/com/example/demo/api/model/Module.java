package com.example.demo.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;


@Entity
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int semester; // 1 for spring, 2 for autumn

    @Min(value = 0, message = "value has to be >= 0")
    private int creditPoints;

    public Module(String name, int semester, int creditPoints) {
        this.name = name;
        this.semester = semester;
        this.creditPoints = creditPoints;
    }

    public Module() {
    }

    @ManyToOne
    @JoinColumn(name = "partner_university_id")
    @JsonIgnore
    private PartnerUniversity partnerUniversity; // Beziehung zu PartnerUniversity

    // Getter und Setter
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

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(int creditPoints) {
        this.creditPoints = creditPoints;
    }

    public PartnerUniversity getPartnerUniversity() {
        return partnerUniversity;
    }

    public void setPartnerUniversity(PartnerUniversity partnerUniversity) {
        this.partnerUniversity = partnerUniversity;
    }
}