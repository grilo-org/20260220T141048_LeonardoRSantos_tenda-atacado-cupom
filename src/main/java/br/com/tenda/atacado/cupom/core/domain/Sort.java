package br.com.tenda.atacado.cupom.core.domain;

public class Sort {

    private String field;
    private Direction direction;

    public Sort(String field, Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public Direction getDirection() {
        return direction;
    }

    public enum Direction {
        ASC, DESC
    }
}
