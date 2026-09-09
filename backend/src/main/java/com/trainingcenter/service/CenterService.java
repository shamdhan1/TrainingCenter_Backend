package com.trainingcenter.service;

import com.trainingcenter.dto.request.CenterRequest;
import com.trainingcenter.dto.response.CenterResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CenterService {
    List<CenterResponse> getAllCenters();
    Page<CenterResponse> getAllCentersPaginated(int page, int size);
    CenterResponse getCenterById(Long centerId);
    CenterResponse createCenter(CenterRequest centerRequest);
    CenterResponse updateCenter(Long centerId, CenterRequest centerRequest);
    void deleteCenter(Long centerId);
}
