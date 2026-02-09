package utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    private ListUtils() {}

    public static <T> List<T> filter(List<T> items, Filter<T> filter) {
        List<T> result = new ArrayList<>();
        if (items == null || filter == null) return result;

        for (T item : items) {
            if (filter.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
}
