package com.ordermgmt.users.domain.port.in;

import com.ordermgmt.users.domain.model.AuthResult;

public interface LoginUserUseCase {
    AuthResult login(String username, String password);
}
