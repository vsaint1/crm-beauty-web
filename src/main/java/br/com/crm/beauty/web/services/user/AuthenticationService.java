package br.com.crm.beauty.web.services.user;

import java.nio.file.AccessDeniedException;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.expression.AccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.crm.beauty.web.dtos.AuthenticationDto;
import br.com.crm.beauty.web.dtos.JwtTokenDto;
import br.com.crm.beauty.web.enums.Position;
import br.com.crm.beauty.web.models.Employee;
import br.com.crm.beauty.web.services.EmployeeService;
import br.com.crm.beauty.web.services.JwtService;

@Service
public class AuthenticationService {

    private final JwtService jwtService;

    private final UserService userService;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public JwtTokenDto authenticate(AuthenticationDto auth) {
        var user = userService.findByEmail(auth.getEmail());

        logger.info("Authenticating user with email: {}", auth.getEmail());

        if (user.isEmpty())
            throw new IllegalArgumentException("User not found");

        var bcrypt = new BCryptPasswordEncoder();

        if (!bcrypt.matches(auth.getPassword(), user.get().getPassword())) {
            logger.warn("Invalid credentials for user: {}", auth.getEmail());
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwtService.generate(user.get());
    }

    
}
