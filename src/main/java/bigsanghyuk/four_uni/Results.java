package bigsanghyuk.four_uni;

import lombok.Getter;

@Getter
public class Results<T> {

    private T data;
    private int count;

    public Results(T data, int count) {
        this.data = data;
        this.count = count;
    }
}
