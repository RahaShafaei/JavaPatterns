package functional.reflection;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static java.util.Map.entry;

enum Color {
    RED, GREEN, BLUE
}

interface Drawable {
    int getTheNumberOfCorners();
}

class square implements Drawable {

    @Override
    public int getTheNumberOfCorners() {
        return 4;
    }
}

class PopUpTypeInfo {
    private static final List<String> JDK_PACKAGE_PREFIXES = Arrays.asList("com.sun.", "java", "javax", "jdk", "org.w3c", "org.xml");

    private final List<String> inheritedClassNames = new ArrayList<>();
    private Class<?> clazz;
    private boolean isPrimitive;
    private boolean isInterface;
    private boolean isEnum;
    private String name;
    private boolean isJdk;

    public PopUpTypeInfo(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public PopUpTypeInfo isPrimitive() {
        this.setPrimitive(this.clazz.isPrimitive());
        return this;
    }

    public PopUpTypeInfo isInterface() {
        this.setInterface(this.clazz.isInterface());
        return this;
    }

    public PopUpTypeInfo isEnum() {
        this.setEnum(this.clazz.isEnum());
        return this;
    }

    public PopUpTypeInfo getName() {
        this.setName(this.clazz.getSimpleName());
        return this;
    }

    private void setName(String name) {
        this.name = name;
    }

    public PopUpTypeInfo isJdk() {
        this.setJdk(this.isJDKClass());
        return this;
    }

    public PopUpTypeInfo getInheritedClassNames() {
        if (inheritedClassNames != null) {
            this.inheritedClassNames.addAll(Arrays.stream(this.getAllInheritedClassNames()).toList());
        }

        return this;
    }

    private void setPrimitive(boolean primitive) {
        isPrimitive = primitive;
    }

    private void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

    private void setEnum(boolean anEnum) {
        isEnum = anEnum;
    }

    private void setJdk(boolean jdk) {
        isJdk = jdk;
    }

    private boolean isJDKClass() {
        return JDK_PACKAGE_PREFIXES.stream().anyMatch(packagePfx -> this.clazz.getPackage() == null || this.clazz.getPackage().getName().startsWith(packagePfx));
    }

    private String[] getAllInheritedClassNames() {
        String[] inheritedClasses;
        if (this.clazz.isInterface()) {
            inheritedClasses = Arrays.stream(this.clazz.getInterfaces()).map(Class::getSimpleName).toArray(String[]::new);
        } else {
            Class<?> inheritedClass = this.clazz.getSuperclass();
            inheritedClasses = inheritedClass != null ? new String[]{this.clazz.getSuperclass().getSimpleName()} : null;
        }
        return inheritedClasses;
    }

    @Override
    public String toString() {
        return "PopUpTypeInfo{" + ", isPrimitive=" + isPrimitive + ", isInterface=" + isInterface + ", isEnum=" + isEnum + ", name='" + name + '\'' + ", isJdk=" + isJdk + ", inheritedClassNames=" + inheritedClassNames + '}';
    }
}

class Person {
    private final Address address;
    private final String name;
    private final int age;

    public Person() {
        this.name = "anonymous";
        this.age = 0;
        this.address = null;
    }

    public Person(String name) {
        this.name = name;
        this.age = 0;
        this.address = null;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        this.address = null;
    }

    public Person(Address address, String name, int age) {
        this.address = address;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" + "address=" + address + ", name='" + name + '\'' + ", age=" + age + '}';
    }
}

class Address {
    private final String street;
    private final int number;

    public Address(String street, int number) {
        this.street = street;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Address{" + "street='" + street + '\'' + ", number=" + number + '}';
    }
}

class VerifyClassFields {
    public static void getFieldsDetails() throws IllegalAccessException, NoSuchFieldException {
        Movie movie = new Movie("Lord of the Rings",
                2001,
                12.99,
                true,
                Category.ADVENTURE);

        printClassFields(Movie.class, movie);

        Field minPriceStaticField = Movie.class.getDeclaredField("MINIMUM_PRICE");

        System.out.println(String.format("static MINIMUM_PRICE value :%f", minPriceStaticField.get(null)));
    }

    static <T> void printClassFields(Class<T> clazz, T instance) throws IllegalAccessException {
        for (Field field : clazz.getDeclaredFields()) {

            System.out.println(String
                    .format("Field name is: \"%s\", Field type is: \"%s\""
                            , field.getName()
                            , field.getType().getName()
                    ));

            System.out.println(String.format("Is synthetic field : %s", field.isSynthetic()));
            System.out.println(String.format("Field value is : %s", field.get(instance)));

            System.out.println();

        }
    }

    public enum Category {
        ADVENTURE, ACTION, COMEDY
    }

    public static class Movie extends Product {
        public static final double MINIMUM_PRICE = 10.99;

        private final boolean isReleased;
        private final Category category;
        private final double actualPrice;

        public Movie(String name, int year, double price, boolean isReleased, Category category) {
            super(name, year);
            this.isReleased = isReleased;
            this.category = category;
            this.actualPrice = Math.max(price, MINIMUM_PRICE);
        }

        // Nested class
        public class MovieStats {
            private final double timesWatched;

            public MovieStats(double timesWatched) {
                this.timesWatched = timesWatched;
            }

            public double getRevenue() {
                return timesWatched * actualPrice;
            }
        }
    }

    public static class Product {
        protected String name;
        protected int year;
        protected double actualPrice;

        public Product(String name, int year) {
            this.name = name;
            this.year = year;
        }
    }
}

public class TestReflection {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        TestReflection testReflection = new TestReflection();

//        int[] oneDimensionalArray = {1, 2};
//        double[][] towDimensionalArray = {{1.5, 2.5}, {3.5, 4.5}};
//        testReflection.inspectArrayObject(oneDimensionalArray);
//        System.out.println();
//        testReflection.inspectArrayValues(towDimensionalArray);

//        VerifyClassFields.getFieldsDetails();

//        System.out.println(testReflection.createPopupTypeInfoFromClass(Rectangle.class));
//        System.out.println(testReflection.findAllImplementedInterfaces(Rectangle.class));
    }

    private static void printClassInfo(Class<?>... classes) {
        for (Class<?> clazz : classes) {

            System.out.printf("class name : %s, class package name : %s%n", clazz.getSimpleName(), clazz.getPackageName());

            Class<?>[] implementedInfo = clazz.getInterfaces();
            for (Class<?> imp : implementedInfo) {
                System.out.printf("class %s implements : %s%n", clazz.getSimpleName(), imp.getSimpleName());
            }

            System.out.println("Is array : " + clazz.isArray());
            System.out.println("Is primitive : " + clazz.isPrimitive());
            System.out.println("Is enum : " + clazz.isEnum());
            System.out.println("Is interface : " + clazz.isInterface());
            System.out.println("Is anonymous :" + clazz.isAnonymousClass());

            System.out.println();
            System.out.println();

        }
    }

    public static Set<Class<?>> findAllImplementedInterfaces(Class<?> input) {
        Set<Class<?>> allImplementedInterfaces = new HashSet<>();

        Class<?>[] inputInterfaces = input.getInterfaces();
        for (Class<?> currentInterface : inputInterfaces) {
            allImplementedInterfaces.add(currentInterface);
//            allImplementedInterfaces.addAll(findAllImplementedInterfaces(currentInterface));
            findAllImplementedInterfaces(currentInterface);
        }

        return allImplementedInterfaces;
    }

    public static <T> void initConfiguration(Class<T> t) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<T> constructor = t.getDeclaredConstructor(int.class, String.class);

        constructor.setAccessible(true);
        constructor.newInstance(8080, "Good Day!");
    }

    void printConstructorData(Class<?> aClass) {
        Constructor<?>[] aClassConstructors = aClass.getConstructors();

        System.out.printf("Class %s has %d constructors.%n", aClass.getSimpleName(), aClassConstructors.length);

        for (Constructor constructor : aClassConstructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            List<String> parameters = Arrays.stream(parameterTypes).map(type -> type.getSimpleName()).toList();

            System.out.println(parameters);
        }
    }

    <T> T createInstanceWithArgument(Class<T> t, Object... args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Constructor constructor : t.getConstructors()) {
            if (constructor.getParameters().length == args.length) {
                return (T) constructor.newInstance(args);
            }
        }
        System.out.println("An appropriate constructor was not found");
        return null;
    }

    void constructorPractice() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        printConstructorData(Person.class);

        Address address = createInstanceWithArgument(Address.class, "First Street", 10);

        Person person = createInstanceWithArgument(Person.class, address, "John", 20);
        System.out.println(person);
    }

    public PopUpTypeInfo createPopupTypeInfoFromClass(Class<?> inputClass) {
        PopUpTypeInfo popupTypeInfo = new PopUpTypeInfo(inputClass);

        popupTypeInfo.isPrimitive().isInterface().isEnum().getName().isJdk().getInheritedClassNames();

        return popupTypeInfo;
    }

    void reflectionPractice() throws ClassNotFoundException {
        Class<String> stringClass = String.class;

        Map<Integer, String> integerStringMap = new HashMap<>();
        Class<?> hashMapClass = integerStringMap.getClass();

        Class<?> rectangleClass = Class.forName("functional.TestReflection$Rectangle");

        printClassInfo(stringClass, hashMapClass, rectangleClass);

        var circleObject = new Drawable() {
            @Override
            public int getTheNumberOfCorners() {
                return 0;
            }
        };


        printClassInfo(Collection.class, boolean.class, int[][].class, Color.class, circleObject.getClass());
    }

    void inspectArrayObject(Object arrayObj) {
        Class<?> clazz = arrayObj.getClass();

        System.out.printf("Is an array : %s%n", clazz.isArray());

        Class<?> arrayComponentType = clazz.getComponentType();

        System.out.printf("This is an array of \"%s\" type.%n", arrayComponentType.getTypeName());
    }

    void inspectArrayValues(Object arrayObject) {
        int length = Array.getLength(arrayObject);

        System.out.print("[");
        for (int i = 0; i < length; i++) {
            Object element = Array.get(arrayObject, i);

            if (element.getClass().isArray()) {
                inspectArrayValues(element);
            } else {
                System.out.print(element);
            }

            if (i != length - 1) {
                System.out.print(" , ");
            }
        }

        System.out.print("]");
    }

    private long sizeOfString(String inputString) {
        final long HEADER = 12;
        final long OBJECT_REFERENCE = 4;
        int stringBytesSize = inputString.getBytes().length;
        return HEADER + OBJECT_REFERENCE + stringBytesSize;
    }

    long objectSize(Object instance) throws IllegalAccessException {
        final long HEADER = 12;
        final long OBJECT_REFERENCE = 4;
        long size = HEADER + OBJECT_REFERENCE;

        Map<Class<?>, Integer> typeSize = Map.ofEntries(
                entry(int.class, 4),
                entry(byte.class, 1),
                entry(long.class, 8),
                entry(double.class, 8),
                entry(float.class, 4),
                entry(short.class, 2)
        );

        Field[] fields = instance.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if (fields[i].getType().isPrimitive()) {
                size += typeSize.get(fields[i].getType());
            } else {
                size += sizeOfString((String) fields[i].get(instance));
            }
        }
        return size;

    }

    Object arrayTraversInTwoDirection(Object arrayObject, int index) {
        int length = Array.getLength(arrayObject);

        int validIndex = index < 0 ? length + index : index;

        if (validIndex > -1 && validIndex < length) {
            Object element = Array.get(arrayObject, validIndex);
            return element;
        }
        return null;
    }

    public <T> T concat(Class<?> type, Object... arguments) {
        if (arguments.length == 0) {
            return null;
        }

        List<Object> elements = new ArrayList<>();
        for (Object argument : arguments) {
            if (argument.getClass().isArray()) {
                int length = Array.getLength(argument);

                for (int i = 0; i < length; i++) {
                    elements.add(Array.get(argument, i));
                }
            } else {
                elements.add(argument);
            }
        }

        Object flattenedArray = Array.newInstance(type, elements.size());

        for (int i = 0; i < elements.size(); i++) {
            Array.set(flattenedArray, i, elements.get(i));
        }

        return (T) flattenedArray;
    }

    static class Rectangle implements Drawable {
        @Override
        public int getTheNumberOfCorners() {
            return 4;
        }
    }
}
