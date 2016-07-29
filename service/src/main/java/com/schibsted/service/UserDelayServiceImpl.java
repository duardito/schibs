package com.schibsted.service;

import com.schibsted.common.Constants;
import com.schibsted.domain.user.User;
import com.schibsted.domain.user.UserDelay;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.DelayQueue;

public class UserDelayServiceImpl implements IUserDelayService {

    protected static DelayQueue<UserDelay> userDelayedList = new DelayQueue<UserDelay>();

    static {
        removeUser();
    }

    public static DelayQueue<UserDelay> getUserDelayedList() {
        return userDelayedList;
    }

    //remove loged users from session after 5 minutes
    private static void removeUser() {
        Runnable task2 = () -> {
            while (true) {
                if (getUserDelayedList() != null && !getUserDelayedList().isEmpty()) {
                    final UserDelay peek = getUserDelayedList().peek();
                    if (getUserDelayedList().poll() != null) {
                        System.out.println("User: " + peek.getUser() + " removed from list at: " + new Date());
                    }
                }
            }
        };
        new Thread(task2).start();
    }

    @Override
    public void usersInDelay(Optional<User> loggedUser) {
        if (userDelayedList.isEmpty()) {
            userDelayedList.offer(UserDelay.build(loggedUser.get()));
        } else {
            final Optional<UserDelay> delay = getUserDelay(loggedUser.get().getUsername());
            if (!delay.isPresent()) {
                userDelayedList.offer(UserDelay.build(loggedUser.get()));
            } else {
                resetDelayTime(delay);
            }
        }
    }

    @Override
    public Optional<UserDelay> getUserDelay(final String username) {
        return userDelayedList.stream()
                .filter(userDelay -> userDelay.getUser().getUsername().equals(username)).findAny();
    }

    @Override
    public void resetDelayTime(final Optional<UserDelay> delay) {
        delay.get().setDelayTime(System.currentTimeMillis() + Constants._DELAYTIME);
    }

    @Override
    public void forceDelayTimeToRemoveUser(final Optional<UserDelay> delay) {
        delay.get().setDelayTime(System.currentTimeMillis());
    }

}
