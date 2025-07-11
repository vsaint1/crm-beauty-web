package br.com.crm.beauty.web.enums;

public enum Position {

    EMPLOYEE(1),
    OWNER(2),
    MANAGER(3);

    private final int code;

    Position(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return name();
    }

    public static Position fromCode(int code) {
        for (Position p : values()) {
            if (p.code == code) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
