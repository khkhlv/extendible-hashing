package ru.khkhlv.data;

public record Data(int id, Object data) {
    @Override
    public String toString() {
        return String.format("Data[id=%d]", id);
    }
}
