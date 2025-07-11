CREATE TABLE tb_employees (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    company_id BINARY(16) NOT NULL,
    user_id BIGINT NOT NULL,
    salary DECIMAL(20,2) DEFAULT 0,
    position VARCHAR(50) NOT NULL DEFAULT 'EMPLOYEE',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_employees_companies FOREIGN KEY (company_id) REFERENCES tb_companies (id),
    CONSTRAINT fk_employees_users FOREIGN KEY (user_id) REFERENCES tb_users (id)
);