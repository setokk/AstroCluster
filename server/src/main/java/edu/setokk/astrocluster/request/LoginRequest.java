package edu.setokk.astrocluster.request;

import edu.setokk.astrocluster.validation.IValidatable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest implements IValidatable {
    @NotNull(message = "[LoginRequest]: username cannot be null")
    @NotEmpty(message = "[LoginRequest]: username cannot be empty")
    private String username;

    @NotNull(message = "[LoginRequest]: password cannot be null")
    @NotEmpty(message = "[LoginRequest]: password cannot be empty")
    private String password;
}
