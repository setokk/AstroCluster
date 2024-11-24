package edu.setokk.astrocluster.validation;

import edu.setokk.astrocluster.error.BusinessLogicException;
import edu.setokk.astrocluster.model.dto.UserDto;
import edu.setokk.astrocluster.request.PerformClusteringRequest;
import edu.setokk.astrocluster.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PerformClusteringValidator implements IValidator<PerformClusteringRequest> {
    private final AuthService authService;

    @Autowired
    public PerformClusteringValidator(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void advancedValidate(PerformClusteringRequest requestBody) throws BusinessLogicException {
        UserDto user = authService.getAuthenticatedUser();
        if (user.isPublicUser() && requestBody.getEmail() == null) {
            throw new BusinessLogicException(HttpStatus.BAD_REQUEST, requestBody.errorPrefix() + " email field is required when no user is signed in");
        }
    }
}
