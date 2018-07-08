package ax.commons.reflect;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Reflect {

    public static <T> ArrayList<Field> getFieldsOfType(Class<?> c, Class<T> type) {
        ArrayList<Field> fields = new ArrayList<>();

        Field[] declared = c.getDeclaredFields();

        for (Field field : declared) {
            if (field.getType().equals(type)) {
                fields.add(field);
            }
        }

        return fields;
    }

    public static <T> ArrayList<FieldDef<T>> getFieldDefsOfType(Class<?> c, Class<T> type) {
        ArrayList<Field> fields = getFieldsOfType(c, type);
        ArrayList<FieldDef<T>> fieldDefs = new ArrayList<>();

        for (Field field : fields) {
            String name = field.getName();
            Object value = null;
            try {
                value = field.get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            FieldDef<T> fieldDef = new FieldDef<>(name, (T)value);
            fieldDefs.add(fieldDef);
        }

        return fieldDefs;
    }

}
