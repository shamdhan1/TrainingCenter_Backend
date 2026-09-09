package com.trainingcenter.service;

import com.trainingcenter.dto.request.BatchRequest;
import com.trainingcenter.dto.response.BatchResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BatchService {
    List<BatchResponse> getBatchesByCenter(Long centerId);
    Page<BatchResponse> getBatchesByCenterPaginated(Long centerId, int page, int size);
    BatchResponse getBatchById(Long id);
    BatchResponse createBatch(BatchRequest request);
    BatchResponse updateBatch(Long id, BatchRequest request);
    void deleteBatch(Long id);
}
