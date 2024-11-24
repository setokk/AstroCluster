package edu.setokk.astrocluster.service;

import edu.setokk.astrocluster.error.BusinessLogicException;
import edu.setokk.astrocluster.model.UserJpo;
import edu.setokk.astrocluster.model.dto.UserDto;
import edu.setokk.astrocluster.repository.UserRepository;
import edu.setokk.astrocluster.request.LoginRequest;
import edu.setokk.astrocluster.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto loginUser(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        UserJpo user = userRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new BusinessLogicException(HttpStatus.NOT_FOUND, "User with username '" + username + "' not found."));

        String actualHashedPassword = user.getPassword();
        boolean isValidCredentials = passwordEncoder.matches(password, actualHashedPassword);
        if (!isValidCredentials)
            throw new BusinessLogicException(HttpStatus.UNAUTHORIZED, "Invalid password");

        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }

    public UserDto registerUser(RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String email = registerRequest.getEmail();

        boolean usernameExists = userRepository.findUserByUsername(username).isPresent();
        if (usernameExists)
            throw new BusinessLogicException(HttpStatus.CONFLICT, "User with username: " + username + " exists");

        boolean emailExists = userRepository.findUserByEmail(email).isPresent();
        if (emailExists)
            throw new BusinessLogicException(HttpStatus.CONFLICT, "Email is taken.");

        UserJpo savedUser = userRepository.save(new UserJpo(username, passwordEncoder.encode(password), email));
        return new UserDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }
}
