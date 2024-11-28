package edu.setokk.astrocluster.request;

import edu.setokk.astrocluster.validation.IValidatable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class RegisterRequest implements IValidatable {
    @NotNull(message = "[RegisterRequest]: username field is mandatory")
    @NotEmpty(message = "[RegisterRequest]: username field cannot be empty")
    private String username;

    @NotNull(message = "[RegisterRequest]: password field is mandatory")
    @NotEmpty(message = "[RegisterRequest]: password field cannot be empty")
    private String password;

    @NotNull(message = "[RegisterRequest]: email field is mandatory")
    @NotEmpty(message = "[RegisterRequest]: email field cannot be empty")
    @Email(message = "[RegisterRequest]: email field is invalid")
    private String email;
}
