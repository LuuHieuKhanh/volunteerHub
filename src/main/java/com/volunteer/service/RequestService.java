package com.volunteer.service;

import com.volunteer.entity.Request;
import com.volunteer.repository.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    @Autowired
    private RequestRepository requestRepository;

    public void createRequest(Request request) {
        logger.info("Create request called: {}", request);
        requestRepository.save(request);
    }

    public void approveRequest(Long requestId) {
        logger.info("Approve request called for id: {}", requestId);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("APPROVED");
        requestRepository.save(request);
    }

    public void rejectRequest(Long requestId, String reason) {
        logger.info("Reject request called for id: {}, reason: {}", requestId, reason);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("REJECTED");
        request.setDenyReason(reason);
        requestRepository.save(request);
    }
} 