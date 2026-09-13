package com.trainingcenter.admin.repository;

import com.trainingcenter.admin.entity.Center;
import com.trainingcenter.admin.enums.CenterStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CenterRepository extends JpaRepository<Center, Long> {
    boolean existsByCenterCode(String centerCode);
    Optional<Center> findByCenterCode(String centerCode);
    Page<Center> findByStatus(CenterStatus status, Pageable pageable);
}
