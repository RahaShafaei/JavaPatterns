package functional.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

enum Size {
    SMALL,
    MEDIUM,
    LARGE,
    XLARGE
}

class Addres {
    private String city;
    private String state;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }
}
class Product {
    private double price;
    private String name;
    private long quantity;
    private Date expirationDate;
    private Addres address;

    // Getters
    public double getPrice() {
        return price;
    }

    // Setters
    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Addres getAddress() {
        return address;
    }

    public void setAddress(Addres address) {
        this.address = address;
    }
}

class ClothingProduct extends Product {
    private Size size;
    private String color;

    // Getters
    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    // Setters

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

public class TestGetterSetter {
    public static void main(String[] args) {
        testGetters(ClothingProduct.class);
        testSetters(ClothingProduct.class);
    }

    public static void testSetters(Class<?> dataClass) {
        List<Field> fields = getAllFields(dataClass);

        for (Field field : fields) {
            String setterName = "set" + capitalizeFirstLetter(field.getName());

            Method setterMethod = null;
            try {
                setterMethod = dataClass.getMethod(setterName, field.getType());
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(String.format("Setter : %s not found", setterName));
            }

            if (!setterMethod.getReturnType().equals(void.class)) {
                throw new IllegalStateException(String.format("Setter method : %s has to return void", setterName));
            }
        }
    }

    public static void testGetters(Class<?> clazz){
        List<Field> fields = getAllFields(clazz);
        Map<String,Method> methodNameToMethod = mapMethodNameToMethod(clazz);

        for (Field field : fields) {
            String getterName = "get" + capitalizeFirstLetter(field.getName());

            if (!methodNameToMethod.containsKey(getterName)) {
                throw new IllegalStateException(String.format("Field : %s doesn't have a getter method", field.getName()));
            }

            Method getter = methodNameToMethod.get(getterName);

            if (!getter.getReturnType().equals(field.getType())) {
                throw new IllegalStateException(
                        String.format("Getter method : %s() has return type %s but expected %s",
                                getter.getName(),
                                getter.getReturnType().getTypeName(),
                                field.getType().getTypeName()));
            }

            if (getter.getParameterCount() > 0) {
                throw new IllegalStateException(String.format("Getter : %s has %d arguments", getterName));
            }
        }
    }

    private static List<Field> getAllFields(Class<?> clazz){
        if (clazz == null || clazz.equals(Object.class)) {
            return Collections.emptyList();
        }

        List<Field> allFields = new ArrayList<>();

        List<Field> declaredFields = List.of(clazz.getDeclaredFields());
        List<Field> inheritedFields = getAllFields(clazz.getSuperclass());

        allFields.addAll(declaredFields);
        allFields.addAll(inheritedFields);

        return allFields;
    }

    private static Map<String, Method> mapMethodNameToMethod(Class<?> clazz){
        Map<String,Method> methodMap = new HashMap<>();

        Method[] methods = clazz.getMethods();

        for (Method method:methods) {
            methodMap.put(method.getName(),method);
        }

        return methodMap;
    }

    private static String capitalizeFirstLetter(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase().concat(fieldName.substring(1));
    }
}
