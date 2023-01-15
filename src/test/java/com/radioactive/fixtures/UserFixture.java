package com.radioactive.fixtures;

import com.radioactive.domain.document.User;

import java.util.List;

public class UserFixture {

    public static User notSaved() {
        return new User(
                null,
                "Leonardo",
                "leonardo@email.com",
                "2ad13fs2ew654"
        );
    }

    public static User one() {
        return new User(
                "1",
                "Leonardo",
                "leonardo@email.com",
                "2ad13fs2ew654"
        );
    }

    public static User two() {
        return new User(
                "2",
                "Joh",
                "joh@email.com",
                "as2d13f5qe5"
        );
    }

    public static List<User> listOfUsers() {
        return List.of(
                UserFixture.one(),
                UserFixture.two()
        );
    }
}
