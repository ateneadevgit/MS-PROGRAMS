package com.fusm.programs.external.impl;

import com.fusm.programs.external.ISafetyMeshService;
import com.fusm.programs.model.external.ColumnPermission;
import com.fusm.programs.model.external.ValidatePermission;
import com.fusm.programs.webclient.WebClientConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SafetyMeshService implements ISafetyMeshService {

    @Autowired
    private WebClientConnector webClientConnector;

    @Value("${ms-safety-mesh.complete-path}")
    private String SAFETY_MESH_ROUTE;

    @Value("${ms-safety-mesh.path}")
    private String SAFETY_MESH_SERVICE;

    @Override
    public List<ColumnPermission> getColumnsByRoleAndModule(ValidatePermission validatePermission) {
        return webClientConnector.connectWebClient(SAFETY_MESH_ROUTE)
                .post()
                .uri(SAFETY_MESH_SERVICE)
                .bodyValue(validatePermission)
                .retrieve()
                .bodyToFlux(ColumnPermission.class)
                .collectList()
                .block();
    }

}
