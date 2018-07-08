package ax.commons.reflect;

public class FieldDef<T> {

    String name;
    T value;

    FieldDef(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public T value() {
        return value;
    }

}
