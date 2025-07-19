package br.com.crm.beauty.web.controllers;

import java.net.URI;
import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.crm.beauty.web.dtos.EmployeeDto;
import br.com.crm.beauty.web.dtos.PasswordUpdateDto;
import br.com.crm.beauty.web.dtos.UserDto;
import br.com.crm.beauty.web.services.user.UserService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> listAllPaged(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        var users = userService.findAll(pageable);

        return ResponseEntity.ok().body(users);
    }

    @PreAuthorize("@authorizationService.isAdminOrSelf(#id, authentication)")
    @GetMapping("{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {

        var user = userService.findById(id);

        return ResponseEntity.ok().body(user);

    }

    @PostMapping("create")
    public ResponseEntity<UserDto> register(@RequestBody @Validated UserDto user) {

        var created = userService.add(user);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uri).body(created);

    }

    @PutMapping("edit/{id}")
    public ResponseEntity<UserDto> updateData(@PathVariable Long id, @RequestBody UserDto user) {

        var updated = userService.update(id, user);

        return ResponseEntity.ok().body(updated);
    }

    @PutMapping("update-password/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id,
            @RequestBody @Validated PasswordUpdateDto password) {

        userService.updatePassword(id, password);

        return ResponseEntity.noContent().build();
    }

}
