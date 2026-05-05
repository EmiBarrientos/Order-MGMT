package com.ordermgmt.users.domain.port.out;

import com.ordermgmt.users.domain.model.User;

public interface TokenGeneratorPort {
    String generateToken(User user);
}
