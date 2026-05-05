package com.ordermgmt.users.domain.port.in;

import com.ordermgmt.users.domain.model.User;

public interface RegisterUserUseCase {
    User register(User user);
}
