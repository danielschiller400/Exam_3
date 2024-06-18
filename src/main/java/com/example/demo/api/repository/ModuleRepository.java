package com.example.demo.api.repository;

import com.example.demo.api.model.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    Page<Module> findByPartnerUniversityId(Long universityId, Pageable pageable);

    Optional<Module> findByIdAndPartnerUniversityId(Long id, long universityId);
}
