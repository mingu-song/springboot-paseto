package mingu.spring.paseto.service;

import lombok.RequiredArgsConstructor;
import mingu.spring.paseto.entity.User;
import mingu.spring.paseto.exception.DuplicateException;
import mingu.spring.paseto.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service(value = "userService")
@RequiredArgsConstructor
@Transactional
public class UserDetailServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateException("Username is already exist");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateException("Email is already exist");
        }
        return userRepository.save(user);
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = getUserByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        detailsChecker.check(user.get());
        return  user.get();
    }

    private Optional<User> getUserByUsername(String usernameValue) {
        var username = StringUtils.trimToNull(usernameValue);
        if (StringUtils.isEmpty(username)) {
            return Optional.empty();
        }
        return username.contains("@") ? userRepository.findActiveByEmail(username) : userRepository.findActiveByUsername(username);
    }
}
