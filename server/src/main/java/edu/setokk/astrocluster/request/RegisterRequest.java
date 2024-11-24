package edu.setokk.astrocluster.request;

import edu.setokk.astrocluster.validation.IValidatable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequest implements IValidatable {
    @NotNull(message = "[RegisterRequest]: username cannot be null")
    @NotEmpty(message = "[RegisterRequest]: username cannot be empty")
    private String username;

    @NotNull(message = "[RegisterRequest]: password cannot be null")
    @NotEmpty(message = "[RegisterRequest]: password cannot be empty")
    private String password;

    @NotNull(message = "[RegisterRequest]: email cannot be null")
    @NotEmpty(message = "[RegisterRequest]: email cannot be empty")
    @Email(message = "[RegisterRequest]: email is invalid")
    private String email;
}
