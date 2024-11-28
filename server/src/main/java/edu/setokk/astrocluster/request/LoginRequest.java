package edu.setokk.astrocluster.request;

import edu.setokk.astrocluster.validation.IValidatable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class LoginRequest implements IValidatable {
    @NotNull(message = "[LoginRequest]: username field is mandatory")
    @NotEmpty(message = "[LoginRequest]: username field cannot be empty")
    private String username;

    @NotNull(message = "[LoginRequest]: password field is mandatory")
    @NotEmpty(message = "[LoginRequest]: password field cannot be empty")
    private String password;
}
