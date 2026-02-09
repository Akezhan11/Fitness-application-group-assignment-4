package utils;

@FunctionalInterface
public interface Filter<T> {
    boolean test(T item);
    default Filter<T> and(Filter<T> other) {
        return item -> this.test(item) && other.test(item);
    }
    default Filter<T> or(Filter<T> other) {
        return item -> this.test(item) || other.test(item);
    }
    default Filter<T> not() {
        return item -> !this.test(item);
    }
}
