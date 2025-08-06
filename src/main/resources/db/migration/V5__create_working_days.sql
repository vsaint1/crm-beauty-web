CREATE TABLE tb_working_days
(
    id          BIGINT     NOT NULL PRIMARY KEY,
    day_of_week TINYINT    NOT NULL DEFAULT 1, # MONDAY = 1 ... SUNDAY = 7
    employee_id  BIGINT NOT NULL,
    CONSTRAINT fk_emp_working_days FOREIGN KEY (employee_id) REFERENCES tb_employees (id)
);
