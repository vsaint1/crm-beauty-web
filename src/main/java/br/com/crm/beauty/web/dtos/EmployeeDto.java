package br.com.crm.beauty.web.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.crm.beauty.web.enums.Position;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class EmployeeDto {

    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CompanyDto company;

    private UserDto user;

    private BigDecimal salary = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private Position position = Position.EMPLOYEE;

    private boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public EmployeeDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
