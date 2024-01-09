package mingu.spring.paseto.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mingu.spring.paseto.dto.LoginDTO;
import mingu.spring.paseto.dto.SignupDTO;
import mingu.spring.paseto.dto.SuccessResponse;
import mingu.spring.paseto.dto.UserDTO;
import mingu.spring.paseto.mapper.AppMapper;
import mingu.spring.paseto.security.TokenProvider;
import mingu.spring.paseto.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @GetMapping("/user")
    public ResponseEntity<SuccessResponse> getAllUser() {
        List<UserDTO> users = userService.getUsers().stream().map(AppMapper::copyUserEntityToDto).toList();
        return ResponseEntity.ok(new SuccessResponse(users, MessageFormat.format("{0} result found", users.size())));
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> register(@Valid @RequestBody SignupDTO signupDTO) {
        var user = AppMapper.copyUserDtoToEntity(signupDTO);
        var encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);

        var newUser = userService.save(user);

        return ResponseEntity.ok(new SuccessResponse(AppMapper.copyUserEntityToDto(newUser), "Register Successfully"));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<SuccessResponse> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new SuccessResponse(jwt, "Login Successfully"));
    }
}
