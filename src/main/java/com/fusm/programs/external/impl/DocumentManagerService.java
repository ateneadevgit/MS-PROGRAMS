package com.fusm.programs.external.impl;

import com.fusm.programs.external.IDocumentManagerService;
import com.fusm.programs.model.external.DocumentRequest;
import com.fusm.programs.webclient.WebClientConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentManagerService implements IDocumentManagerService {

    @Autowired
    private WebClientConnector webClientConnector;

    @Value("${ms-document.complete-path}")
    private String DOCUMENT_MANAGER_ROUTE;

    @Value("${ms-document.path}")
    private String DOCUMENT_MANAGER_SERVICE;

    @Override
    public String saveFile(DocumentRequest documentRequest) {
        return webClientConnector.connectWebClient(DOCUMENT_MANAGER_ROUTE)
                .post()
                .uri(DOCUMENT_MANAGER_SERVICE)
                .bodyValue(documentRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public void deleteDocumentMassive(List<String> fileName) {
        webClientConnector.connectWebClient(DOCUMENT_MANAGER_ROUTE)
                .post()
                .uri(DOCUMENT_MANAGER_SERVICE + "/delete")
                .bodyValue(fileName)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
