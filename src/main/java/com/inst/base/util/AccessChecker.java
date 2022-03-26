package com.inst.base.util;

import com.inst.base.entity.user.User;
import org.springframework.http.HttpStatus;

public final class AccessChecker {
    public static boolean followedOrEquals(User user1, User user2) {
        if(user1 == null || user2 == null)
            return false;

        if(user1.getId().equals(user2.getId()))
            return true;

        return user1.getFollowers().contains(user2);
    }

    public static boolean followed(User user1, User user2) {
        if(user1 == null || user2 == null)
            return false;

        return user1.getFollowers().contains(user2);
    }

    public static boolean equals(User user1, User user2) {
        if(user1 == null || user2 == null)
            return false;

        return user1.getId().equals(user2.getId());
    }
}
