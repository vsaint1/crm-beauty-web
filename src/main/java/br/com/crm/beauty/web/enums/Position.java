package br.com.crm.beauty.web.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import br.com.crm.beauty.web.exceptions.NotFoundException;

public enum Position {

    EMPLOYEE("EMPLOYEE"),
    OWNER("OWNER"),
    MANAGER("MANAGER");

    private final String pos;

    Position(String pos) {
        this.pos = pos;
    }

    public String getPos() {
        return pos;
    }

}
