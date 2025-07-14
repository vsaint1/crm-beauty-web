package br.com.crm.beauty.web.dtos;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.crm.beauty.web.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;

public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    @Email
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Min(6)
    private String password;

    private Role role;

    private Date createdAt;

    public UserDto() {
    }

    public Collection<String> getRoles() {
        if (role.name().equals(Role.ADMIN.name())) {
            return List.of("ROLE_ADMIN", "ROLE_USER");
        }

        return List.of("ROLE_USER");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
