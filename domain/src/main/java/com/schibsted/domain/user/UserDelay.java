package com.schibsted.domain.user;

import com.schibsted.common.Constants;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


public class UserDelay implements Delayed {

    private User user;
    private long startTime;
    private long delayTime;

    public static UserDelay build(final User user){
        return new UserDelay(user);
    }

    private UserDelay(User user) {
        this.user = user;
        this.startTime = System.currentTimeMillis();
        this.delayTime = System.currentTimeMillis()+ Constants._DELAYTIME;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert( this.delayTime-System.currentTimeMillis(), TimeUnit.MILLISECONDS  );
    }

    @Override
    public int compareTo(Delayed delayedObject) {
        int result = 0;
        long delay = delayedObject.getDelay(TimeUnit.MILLISECONDS);
        if (this.startTime <= delay) {
            result = -1;
        } else {
            result = 1;
        }
        return result;
    }
}
