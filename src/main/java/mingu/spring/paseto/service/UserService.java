package mingu.spring.paseto.service;

import mingu.spring.paseto.entity.User;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService, UserDetailsPasswordService {
    List<User> getUsers();

    User save(User user);
}
