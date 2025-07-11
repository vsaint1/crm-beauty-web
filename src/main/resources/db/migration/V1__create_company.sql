CREATE TABLE tb_companies (
    id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255),
    logo_url VARCHAR(512),
    primary_color VARCHAR(50),
    secondary_color VARCHAR(50),
    description TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    cnpj VARCHAR(20) UNIQUE
);