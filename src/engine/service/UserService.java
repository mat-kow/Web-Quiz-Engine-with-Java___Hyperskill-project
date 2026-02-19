package engine.service;

import engine.model.entities.User;

public interface UserService {
    boolean register(User user);
    User getCurrent();
}
