package com.fusm.programs.external;

import com.fusm.programs.model.external.TokenModel;
import com.fusm.programs.model.external.UserModel;
import org.springframework.stereotype.Service;

@Service
public interface IAuthorizerService {

    UserModel getTokenSubject(TokenModel tokenModel);

}
