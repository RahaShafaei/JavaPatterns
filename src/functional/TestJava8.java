package functional;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

interface IProducer<T> {
    T produce();
}

interface IConfigurator<T, R> {
    R configure(T t);
}

interface IFactory<T> {
    T create();
}

interface IConsumer<T> {
    T accept(T t);

    default IConsumer<T> andThen(IConsumer<T> next) {
        Objects.requireNonNull(next);
        return (t) -> {
//            this.accept(t);
            return next.accept(this.accept(t));
        };

    }
}

class Helper2 {

    private Helper2() {
        throw new IllegalStateException("Helper Utility class");
    }
    public static <T, R, Y> Map<R, Y> convertListToMap(List<T> lst,
                                                       Function<T, R> func1,
                                                       Function<T, Y> func2
    ) {
        Map<R, Y> temp = new HashMap<>();

        lst.stream().forEach(t -> temp.put(func1.apply(t), func2.apply(t)));

        return temp;
    }

    public static <T, R, Y, Z> Map<R, Z> convertListToMapAccordingToMapper(List<T> lst,
                                                                           Function<T, R> func1,
                                                                           Function<T, Y> func2,
                                                                           Function<Y, Z> func3
    ) {
        Map<R, Z> temp = new HashMap<>();

        lst.stream().forEach(t -> temp.put(func1.apply(t), func2.andThen(func3).apply(t)));

        return temp;
    }

    static <T, Y, R> R playTwoToOne(T t1,
                                    T t2,
                                    BiFunction<T, T, Y> twoFunc,
                                    Function<Y, R> oneFunc
    ) {
        return twoFunc.andThen(oneFunc).apply(t1, t2);
    }
}

class Helper implements Comparable<Helper> {
    int id;
    String name;
    long webSites;
    SiteType siteType;

    public Helper(int id, String name, long webSites, SiteType siteType) {
        this.id = id;
        this.name = name;
        this.webSites = webSites;
        this.siteType = siteType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getWebSites() {
        return webSites;
    }

    public void setWebSites(long webSites) {
        this.webSites = webSites;
    }

    public SiteType getSiteType() {
        return siteType;
    }

    public void setSiteType(SiteType siteType) {
        this.siteType = siteType;
    }

    @Override
    public String toString() {
        return "Helper{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", webSites=" + webSites +
                ", siteType=" + siteType +
                '}';
    }

    @Override
    public int compareTo(@NotNull Helper o) {
        if (this.id < o.id)
            return -1;
        else if (this.id > o.id)
            return 1;
        else
            return 0;
    }

    enum SiteType {
        EDUCATIONAL,
        COMMERCIAL
    }
}

class HelperSplitter implements Spliterator<Helper> {
    int id;
    String name;
    long webSites;
    Helper.SiteType siteType;
    Spliterator<String> baseSpliterator;

    public HelperSplitter(Spliterator<String> baseSpliterator) {
        this.baseSpliterator = baseSpliterator;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Helper> action) {
        if (
                this.baseSpliterator.tryAdvance(s -> this.id = Integer.parseInt(s))
             && this.baseSpliterator.tryAdvance(s -> this.name = s)
             && this.baseSpliterator.tryAdvance(s -> this.webSites = Long.parseLong(s))
             && this.baseSpliterator.tryAdvance(s -> this.siteType = Helper.SiteType.valueOf(s))
        ) {
            action.accept(new Helper(this.id, this.name, this.webSites, this.siteType));
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<Helper> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return baseSpliterator.estimateSize() / 4;
    }

    @Override
    public int characteristics() {
        return baseSpliterator.characteristics();
    }
}

class ForEachConsumer {
    public static Consumer<String> printTextInHexConsumer = (String x) -> {
        StringBuilder sb = new StringBuilder();
        for (char c : x.toCharArray()) {
            String hex = Integer.toHexString(c);
            sb.append(hex);
        }
        System.out.printf("%n%-10s:%s%n", x, sb);
    };
}

class IntegerUtils {
    public static String join(Integer i1, Integer i2) {
        return i1 + " | " + i2;
    }
}

class ComparatorProvider {
    int compareByName(Helper h1, Helper h2) {
        return h1.getName().compareTo(h2.getName());
    }

    int compareBySite(Helper h1, Helper h2) {
        return (int) (h1.getWebSites() - h2.getWebSites());
    }
}

class HelperFilter {
    public static List<Helper> filter(List<Helper> helpers, Predicate<Helper> ph) {
        List<Helper> filteredHelpers = new ArrayList<>();

        helpers.stream()
                .filter(helper -> ph.test(helper))
                .forEach(helper -> filteredHelpers.add(helper));

        return filteredHelpers;
    }
}

public class TestJava8 {
    List<Helper> helpers = new ArrayList<>();

    public TestJava8() {
        helpers.add(new Helper(1, "liquidweb.com", 80000, Helper.SiteType.EDUCATIONAL));
        helpers.add(new Helper(2, "linode.net", 90000, Helper.SiteType.COMMERCIAL));
        helpers.add(new Helper(3, "digitalocean.net", 120000, Helper.SiteType.COMMERCIAL));
        helpers.add(new Helper(4, "aws.amazon.com", 200000, Helper.SiteType.EDUCATIONAL));
        helpers.add(new Helper(5, "mkyong.com", 1, Helper.SiteType.EDUCATIONAL));
    }

    static <R, T> R playTwoArg(T o1, T o2, @NotNull BiFunction<T, T, R> func) {
        return func.apply(o1, o2);
    }

    static <R, T> R playOneArg(T o1, @NotNull Function<T, R> func) {
        return func.apply(o1);
    }

    static <T, R> Map<T, R> convertToMap(List<T> lst, Function<T, R> func) {
        Map<T, R> temp = new HashMap<>();

        lst.stream().forEach(t -> temp.put(t, func.apply(t)));

        return temp;
    }

    static <T, Y, R> R playTwoToOne(T t1, T t2, BiFunction<T, T, Y> twoFunc, Function<Y, R> oneFunc) {
        return twoFunc.andThen(oneFunc).apply(t1, t2);
    }

    static <R> R find(List<R> list, BinaryOperator<R> func) {
        R result = null;
        for (R t : list) {
            if (result == null) {
                result = t;
            } else {
                result = func.apply(result, t);
            }
        }
        return result;
    }

    public static <T, U, V, R> R compute(T t, U u, V v, ThreeFunction<T, U, V, R> function) {
        return function.apply(t, u, v);
    }

    public static void main(String[] args) {
        TestJava8 testJava8 = new TestJava8();
        testJava8.collectorSplitterPractice();
    }

    public static Number optionalSum(Optional<Integer> t1, Optional<Integer> t2) {
        return t1.filter(v -> v.equals(10)).map(v -> v * 2).orElse(t1.orElse(0)) + t2.filter(v -> v.equals(10)).map(v -> v * 3).orElse(t2.orElse(0));
    }

    public static <T, R> IFactory<R> createFactory(IProducer<T> iProducer, IConfigurator<T, R> iConfigurator) {
        return () -> {
            T produce = iProducer.produce();
            R configure = iConfigurator.configure(produce);
            return configure;
        };
    }

    void mapPractice() {
//        https://mkyong.com/java8/java-8-foreach-examples/
//        https://mkyong.com/java8/java-8-convert-list-to-map/

        Map<String, Integer> tm = new HashMap<>();
        tm.put("one", 1);
        tm.put(null, 2);
        tm.put("three", 3);
        tm.put("four", null);
        tm.put(null, 5);
//        for (Map.Entry<String,Integer > t:tm.entrySet()) {
//            System.out.println(t.getKey() +" _ "+ t.getValue());
//        }

        //lambada
//        tm.forEach((k,v)-> System.out.println("Key: " + k + " ___ Value: " + v));
        /*______________________________________________________________*/

        List<String> lst = new ArrayList<>();
        lst.add("one");
        lst.add("two");
        lst.add(null);
        lst.add("three");
        lst.add("four");
////      Lambada
//        lst.forEach((v) -> System.out.println("Value : " + v));
////      MethodReference
//        lst.forEach(System.out::println);
//      MethodReference Filter
//        lst.stream()
//                .filter(Objects::nonNull)
//                .forEach(System.out::println);
        /*__Consumer____________________________________________________________*/
//        lst.stream()
//                .filter(Objects::nonNull)
//                .forEach(ForEachConsumer.printTextInHexConsumer);
        /*__ConvertListToMap____________________________________________________________*/
        Map<Integer, String> ts1 = helpers.stream().collect(Collectors.toMap(Helper::getId, Helper::getName));
        System.out.println("Result1: " + ts1);

        Map<Integer, Long> ts2 = helpers.stream().collect(Collectors.toMap(x -> x.getId(), x -> x.getWebSites()));
        System.out.println("Result2: " + ts2);

        helpers.add(new Helper(5, "mkyong.com", 2, Helper.SiteType.EDUCATIONAL));
        Map<String, Long> ts3 = helpers.stream().collect(Collectors.toMap(Helper::getName, Helper::getWebSites, (oldValue, newValue) -> newValue));
        System.out.println("Result3: " + ts3);

        // returns a LinkedHashMap, keep order***********************
        Map result1 = helpers.stream().sorted(java.util.Comparator.comparingLong(Helper::getWebSites).reversed()).collect(Collectors.toMap(Helper::getName, Helper::getWebSites, // key = name, value = websites
                (oldValue, newValue) -> oldValue,       // if same key, take the old key
                LinkedHashMap::new                      // returns a LinkedHashMap, keep order
        ));
        System.out.println(result1);
    }

    void comparatorPractice() {
//        https://mkyong.com/java8/java-8-lambda-comparator-example/

//        Classic Sort =================================
//        Collections.sort(helpers, new Comparator<Helper>() {
//            @Override
//            public int compare(Helper o1, Helper o2) {
//                return (int) (o1.getWebSites() - o2.getWebSites());
//            }
//        });
//        helpers.forEach(v -> System.out.println(" Name: " + v.getName() + " || Site: " + v.getWebSites()));
//        System.out.println("::::::::::::::::::::::::::::::::");
//        Collections.sort(helpers, new Comparator<Helper>() {
//            @Override
//            public int compare(Helper o1, Helper o2) {
//                return o1.getName().compareTo(o2.getName());
//            }
//        });
//        helpers.forEach(v -> System.out.println(" Name: " + v.getName() + " || Site: " + v.getWebSites()));
//        Lambada Sort =================================
//        helpers.sort(( h1, h2)-> (int) (h1.getWebSites() - h2.getWebSites()));
//        helpers.forEach(System.out::println);

        java.util.Comparator<Helper> helperComparator = (h1, h2) -> h1.getName().compareTo(h2.getName());
        helpers.sort(helperComparator);
        helpers.forEach(System.out::println);

    }

    void methodReferencePractice() {
        List<String> list = Arrays.asList("node", "java", "python", "ruby");
//        list.forEach(new Consumer<String>() {
//            @Override
//            public void accept(String s) {
//                System.out.println(s);
//            }
//        });
//        System.out.println(":::::::::::::::::::::::::::::::::::::::::::");
//        list.forEach(System.out::println);

        /*
        There are four kinds of method references:
        Reference to a static method ClassName::staticMethodName
        Reference to an instance method of a particular object Object::instanceMethodName
        Reference to an instance method of an arbitrary object of a particular type ContainingType::methodName–
        Reference to a constructor ClassName::new
        */

//        ClassName::staticMethodName =====================================================
//        list.forEach(ForEachConsumer.printTextInHexConsumer::accept);

//        ClassName::staticMethodName =======Function<>====================================
//        List<String> lst = Arrays.asList("1", "2", "3");
//        List<Integer> collect1 = lst.stream().map(new Function<String,Integer>(){
//            @Override
//            public Integer apply(String s) {
//                return Integer.parseInt(s);
//            }
//        }).collect(Collectors.toList());
//        collect1.forEach(System.out::println);
//
//        collect1 = lst.stream().map(Integer::parseInt).collect(Collectors.toList());

//        ClassName::staticMethodName =======BiFunction<>==================================
//        String str = playTwoArg(1, 2, new BiFunction<Integer, Integer, String>() {
//            @Override
//            public String apply(Integer integer1, Integer integer2) {
//                return IntegerUtils.join(integer1, integer2);
//            }
//        });
//        str = playTwoArg(1,2,(a,b)->IntegerUtils.join(a,b));
//        str = playTwoArg(1,2,IntegerUtils::join);
//        System.out.println(str);

//        Object::instanceMethodName =====================================================
//        ComparatorProvider provider = new ComparatorProvider();
//        helpers.sort(provider::compareByName);
//        helpers.forEach(System.out::println);

//        ContainingType::methodName– =====================================================
//        int result1 = playOneArg("mkYoung", x -> x.length());
//        result1 = playOneArg("mkYoung", String::length);
//
//        Boolean result2 = playTwoArg("mkYong", "y", (x, y) -> x.contains(y));
//        result2 = playTwoArg("mkYong", "y", String::contains);
//
//        System.out.println(result1);
//        System.out.println(result2);

//        ClassName::new =====================================================
//        https://mkyong.com/java8/java-8-method-references-double-colon-operator/
    }

    void streamFilter() {
//        helpers.stream()
//                .filter(h -> !playTwoArg(h.getName(), "mkyong", String::contains))
//                .collect(Collectors.toList())
//                .forEach(System.out::println);


//        Helper hl = helpers.stream()
//                .filter(h -> playTwoArg(h.getName(), "mkyong.com", String::equals))
//                .findAny()
//                .orElse(new Helper(0, "Unknown", 0l));
//        System.out.println(hl);

//        convert stream to String -----------------------------------------
        String nm = helpers.stream().filter(h -> playTwoArg(h.getName(), "mkyong.comm", String::equals))
                .map(Helper::getName)
                .findAny()
                .orElse("Unknown!");
        System.out.println(nm);

//        get all names -----------------------------------------
        List<String> names = helpers.stream().map(Helper::getName).collect(Collectors.toList());
        System.out.println(names);
    }

    void streamMap() {
        helpers.stream()
                .map(h -> playOneArg(h.getName(), String::toUpperCase))
                .collect(Collectors.toList())
                .forEach(System.out::println);


    }

    void functionExample() {
        Function<String, Integer> func = x -> x.length();
        System.out.println(func.apply("mkyong"));

//        Chain Function<T, R> ============================================
        Function<Integer, Integer> func2 = x -> x * 2;
        System.out.println(func.andThen(func2).apply("mkyong"));

//        Chain List -> Map ============================================
        List<String> helperNames = helpers.stream().map(Helper::getName).collect(Collectors.toList());
        Map<String, Integer> namesLength = convertToMap(helperNames, x -> x.length());
        System.out.println(namesLength);
    }

    void biFunctionExample() {
        String str = playTwoToOne(2, 4, (a1, a2) -> (int) Math.pow(a1, a2), (r) -> "Result : " + r);
        System.out.println(str);
    }

    void binaryOperatorExamples() {
//        https://mkyong.com/java8/java-8-binaryoperator-examples/
        /*In this example, the BiFunction<Integer, Integer, Integer>
        which accepts and returns the same type,
        can be replaced with BinaryOperator<Integer>.*/

//        BiFunction ==========================
        BiFunction<Integer, Integer, Integer> func = (x1, x2) -> x1 + x2;
        Integer result = func.apply(2, 3);
        System.out.println(result); // 5

//        BinaryOperator ==========================
        BinaryOperator<Integer> func2 = (x1, x2) -> x1 + x2;
        Integer result2 = func.apply(2, 3);
        System.out.println(result2); // 5

//        BinaryOperator.maxBy() and BinaryOperator.minBy() ==========================
        Comparator<Helper> helperComparator = Comparator.comparing(Helper::getWebSites);
        BinaryOperator<Helper> operator = BinaryOperator.maxBy(helperComparator);
        System.out.println(find(helpers, operator));

//        one line
//        find helper with the lowest WebSites
        Helper helper = find(helpers, BinaryOperator.minBy(Comparator.comparing(Helper::getWebSites)));
        System.out.println(helper);
    }

    void flatMapExample() {
        String[][] array = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};
        Stream.of(array).flatMap(Stream::of).filter(x -> !x.equals("a")).collect(Collectors.toList()).forEach(System.out::println);
    }

    void parallelStreamExample() {
//        IntStream iStr = IntStream.rangeClosed(1,10);
//        System.out.println("Normal . . . .");
//        iStr.forEach(System.out::println);
//
//        IntStream iStr2 = IntStream.rangeClosed(1,10);
//        System.out.println("Parallel . . . .");
//        iStr2.parallel().forEach(System.out::println);

//        Collection.parallelStream()
        helpers.parallelStream().forEach(x -> {
            System.out.println("Thread : " + Thread.currentThread().getName() + ", value: " + x);
        });

    }

    void consReference() {
//        Function<Runnable, Thread> threadGenerator = r -> new Thread(r);
//        OR
        Function<Runnable, Thread> threadGenerator = Thread::new;
//      method Reference
        Runnable task1 = () -> System.out.println("Task1 . . . . ");
        Runnable task2 = () -> System.out.println("Task2 . . . . ");

        Thread thread1 = threadGenerator.apply(task1);
        Thread thread2 = threadGenerator.apply(task2);

        thread1.start();
        thread2.start();
//      lambda
        threadGenerator.apply(() -> System.out.println("Task3 . . . . ")).start();
    }

    void optionalMethod() {
        String val = null;
//        If the specified value is null, then this method returns an empty instance of the Optional class.
        Optional<String> opStr = Optional.ofNullable(val);
        System.out.println(opStr);
//        =========================================
        System.out.println(optionalSum(Optional.ofNullable(null), Optional.ofNullable(10)));
        System.out.println(optionalSum(Optional.ofNullable(1), Optional.ofNullable(10)));
    }

    void higherOrderFunctions() {
        IFactory<Integer> createIFactory = createFactory(() -> Math.random() * 100, r -> r.intValue());
        System.out.println(createIFactory.create());
    }

    void chainingExample() {
        IConsumer<Integer> c1 = x -> x + x;
        IConsumer<Integer> c2 = c1.andThen(y -> y * 2);
        System.out.println(c2.accept(10));
//        =============================================
        Function<Integer, Integer> f1 = s -> s + 2;
        Function<Integer, Integer> f2 = s -> s * 2;

        Function<Integer, Integer> f3 = f1.andThen(f2);
        System.out.println(f3.apply(10));
    }

    void curryingExample() {
        Function<Integer, Function<Integer, Function<Integer, Integer>>> f1 = x -> y -> z -> x + y + z;
        Function<Integer, Function<Integer, Integer>> f2 = f1.apply(1);
        Function<Integer, Integer> f3 = f2.apply(2);
        System.out.println(f3.apply(3));
        System.out.println("::: OR :::::::::::::::::::::::::::");
        System.out.println(f1.apply(1).apply(1).apply(1));
    }

    void strategyPatternLambda() {
        HelperFilter.filter(helpers, helper -> helper.getName().contains("li")).forEach(System.out::println);
        System.out.println("::::::::::::::::::::::::::::::::::::");
        HelperFilter.filter(helpers, helper -> helper.getWebSites() > 80000).forEach(System.out::println);
        System.out.println("::::::::::::::::::::::::::::::::::::");
        HelperFilter.filter(helpers, helper -> helper.getId() > 3).forEach(System.out::println);
    }

    void collectorSplitterPractice() {
        Path path = Paths.get("D:\\Migrate\\JAVA\\JavaPatterns\\helperContainer.txt");
        try (Stream<String> lines = Files.lines(path);) {
            Spliterator<String> baseSpliterator = lines
                    .flatMap(line -> Arrays.stream(line.split(",")))
                    .spliterator();
            Spliterator<Helper> helperSpliterator = new HelperSplitter(baseSpliterator);

            Stream<Helper> helperStream = StreamSupport.stream(helperSpliterator, false);

            System.out.println("-----X-------LIST---------X---");
            List<Helper> helpers = helperStream.collect(Collectors.toList());
            helpers.forEach(System.out::println);

            System.out.println("-----X-------SET---------X---");
            Set<Helper.SiteType> siteTypes = helpers.stream()
                    .map(Helper::getSiteType)
                    .collect(Collectors.toSet());
            siteTypes.forEach(System.out::println);

            System.out.println("-----X-------Collection(TreeSet)---------X---");
            TreeSet<Helper> helpersSorted = helpers.stream()
                    .collect(Collectors.toCollection(TreeSet::new));
            helpersSorted.forEach(System.out::println);

            System.out.println("-----X-------Map---------X---");
//            Map<Helper.SiteType,List<Helper>> partitionedHelpers = helpers.stream()
//                    .collect(Collectors.partitioningBy(e->e.getSiteType()));

            Map<Helper.SiteType, List<Helper>> groupedHelpers = helpers.stream()
                    .collect(Collectors.groupingBy(e -> e.getSiteType()));
            groupedHelpers.forEach((k, v) -> System.out.println(k + " :: " + v));

            System.out.println("=======X==========COUNTING=============X========");
            Map<Helper.SiteType, Long> countSiteTypes = helpers.stream()
                    .collect(Collectors.groupingBy(
                            e -> e.getSiteType(),
                            Collectors.counting()
                    ));
            countSiteTypes.forEach((k, v) -> System.out.println(k + " :: " + v));

            System.out.println("======x============SUMMING===========x=========");
            countSiteTypes = helpers.stream()
                    .collect(Collectors.groupingBy(
                            e -> e.getSiteType(),
                            Collectors.summingLong(e -> e.getWebSites())
                    ));
            countSiteTypes.forEach((k, v) -> System.out.println(k + " :: " + v));

            System.out.println("======x============MAX===========x=========");
            Map<Helper.SiteType, Optional<Helper>> maxSiteTypes = helpers.stream()
                    .collect(Collectors.groupingBy(
                            e -> e.getSiteType(),
                            Collectors.maxBy(Comparator.comparing(e -> e.getWebSites())
                            )));
            maxSiteTypes.forEach((k, v) -> System.out.println(k + " :: " + v));

//            System.out.println("======x============MAX===========x=========");
//            Map<String, Optional<Long>> masSites =
//                    helpers.stream()
//                            .collect(
//                                    Collectors.groupingBy(
//                                            e -> e.getSiteType().toString(),
//                                            Collectors.mapping(
//                                                    e -> e.getWebSites(),
//                                                    Collectors.maxBy(Comparator.comparing(Function.identity()))
//                                            )
//                                    )
//                            );
//
//            System.out.println(masSites);

        } catch (IOException e) {
            System.out.println(e);
        }


    }

    public void cumputeThree(){
        ThreeFunction<Integer, Integer, Integer, Integer> addThreeNumbers = (a, b, c) -> a + b + c;
        ThreeFunction<Integer, Integer, Integer, Integer> multiplyThreeNumbers = (a, b, c) -> a * b * c;

        int result1 = compute(3, 5, 2, addThreeNumbers);
        int result2 = compute(2, 4, 3, multiplyThreeNumbers);

        System.out.println("Result of addition: " + result1); // Output: 10
        System.out.println("Result of multiplication: " + result2); // Output: 24

    }
}
