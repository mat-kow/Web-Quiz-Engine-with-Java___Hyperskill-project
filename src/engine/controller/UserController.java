package engine.controller;

import engine.model.entities.User;
import engine.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/register")
    public ResponseEntity<Object> register(@RequestBody @Valid User user) {
        LOGGER.debug("register {}", user);
        boolean registered = userService.register(user);
        if (!registered)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok().build();
    }

}
