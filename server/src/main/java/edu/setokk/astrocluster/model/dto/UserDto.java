package edu.setokk.astrocluster.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private String email;

    public static UserDto publicUser() {
        return new UserDto(0, "public_user", "public_user@astrocluster.edu");
    }

    public boolean isPublicUser() {
        return username.equals("public_user");
    }
}
