package com.example.demo.api.repository;

import com.example.demo.api.model.PartnerUniversity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PartnerUniversityRepository extends JpaRepository<PartnerUniversity, Long> {
    Page<PartnerUniversity> findByNameContaining(String name, Pageable pageable);
}
