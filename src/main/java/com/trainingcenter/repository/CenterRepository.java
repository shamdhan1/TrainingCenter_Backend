package com.trainingcenter.repository;

import com.trainingcenter.entity.Center;
import com.trainingcenter.enums.CenterStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CenterRepository extends JpaRepository<Center, Long> {
    boolean existsByCenterCode(String centerCode);
    List<Center> findByStatus(CenterStatus status);
    Page<Center> findAll(Pageable pageable);
}
