package com.fusm.programs.external.impl;

import com.fusm.programs.external.IAuthorizerService;
import com.fusm.programs.model.external.TokenModel;
import com.fusm.programs.model.external.UserModel;
import com.fusm.programs.webclient.WebClientConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthorizerService implements IAuthorizerService {

    @Autowired
    private WebClientConnector webClientConnector;

    @Value("${ms-authorizer.complete-path}")
    private String AUTHORIZER_ROUTE;

    @Value("${ms-authorizer.path}")
    private String AUTHORIZER_SERVICE;

    @Override
    public UserModel getTokenSubject(TokenModel tokenModel) {
        return webClientConnector.connectWebClient(AUTHORIZER_ROUTE)
                .post()
                .uri(AUTHORIZER_SERVICE + "/decrypt")
                .bodyValue(tokenModel)
                .retrieve()
                .bodyToMono(UserModel.class)
                .block();
    }

}
