package com.schibsted.service;

import com.schibsted.domain.user.User;
import com.schibsted.domain.user.UserDelay;

import java.util.Optional;

public interface IUserDelayService {

    void usersInDelay(Optional<User> loggedUser);

    Optional<UserDelay> getUserDelay(String username);

    void resetDelayTime(Optional<UserDelay> delay);

    void forceDelayTimeToRemoveUser(Optional<UserDelay> delay);
}
