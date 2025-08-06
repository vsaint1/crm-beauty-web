package br.com.crm.beauty.web.services;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import br.com.crm.beauty.web.enums.Position;
import br.com.crm.beauty.web.enums.Role;
import br.com.crm.beauty.web.models.User;
import br.com.crm.beauty.web.services.user.UserService;

@Service
public class AuthorizationService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AuthorizationService.class);
    private final EmployeeService employeeService;
    private final UserService userService;

    public AuthorizationService(EmployeeService employeeService, UserService userService) {
        this.employeeService = employeeService;
        this.userService = userService;
    }

    // TODO: tratar error body
    public boolean canManageCompany(Authentication auth) {
        if (!auth.isAuthenticated())
            return false;

        var employee = employeeService.findByEmail(auth.getName());

        if (employee == null) {

            logger.warn("Employee not found for email: {}", auth.getName());
            return false;
        }

        if (!Set.of(Position.OWNER, Position.MANAGER).contains(employee.getPosition())) {
            logger.warn("Access denied for employee: {} with position: {} company_name {}",
                    employee.getId(), employee.getPosition(),employee.getCompany().getName());

            return false;
        }

        return true;
    }

    public boolean isAdminOrSelf(Long resourceOwnerId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long jwtUserId = jwt.getClaim("id");

        if (jwtUserId == null) {
            return false;
        }

        List<String> roles = jwt.getClaim("roles");

        return jwtUserId == resourceOwnerId || roles.contains("ROLE_ADMIN");
    }
}
