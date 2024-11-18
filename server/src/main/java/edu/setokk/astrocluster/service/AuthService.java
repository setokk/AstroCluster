package edu.setokk.astrocluster.service;

import edu.setokk.astrocluster.model.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    public Optional<UserDto> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDto))
            return Optional.empty();

        return Optional.ofNullable((UserDto) authentication.getPrincipal());
    }
}
