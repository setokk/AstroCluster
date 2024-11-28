package edu.setokk.astrocluster;

import edu.setokk.astrocluster.model.UserEntity;
import edu.setokk.astrocluster.model.dto.UserDto;
import edu.setokk.astrocluster.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public ApplicationRunner startupRunner(UserRepository userRepository) {
        return args -> {
            /* Save public user if they don't exist in application startup */
            UserDto publicUser = UserDto.publicUser();
            UserEntity user = new UserEntity(publicUser.getId(), publicUser.getUsername(), publicUser.getEmail(), publicUser.getUsername());
            userRepository.save(user);
        };
    }
}
