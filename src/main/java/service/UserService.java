package service;

import domain.user.User;

public interface UserService {
    int MIN_LOGIN_FOR_SILVER = 50;
    int MIN_RECOMMEND_FOR_GOLD = 30;

    void add(User user);
    void upgradeLevels();
}
