package ru.khkhlv.data;

import java.io.Serializable;

public record Data(int id, Object data) implements Serializable {
    @Override
    public String toString() {
        return String.format("Data[id=%d]", id);
    }
}
