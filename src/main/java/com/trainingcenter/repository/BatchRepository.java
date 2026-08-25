package com.trainingcenter.repository;

import com.trainingcenter.entity.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    boolean existsByBatchCode(String batchCode);
    List<Batch> findByCenterCenterId(Long centerId);
    long countByCenterCenterId(Long centerId);
    Page<Batch> findByCenterCenterId(Long centerId, Pageable pageable);
}
