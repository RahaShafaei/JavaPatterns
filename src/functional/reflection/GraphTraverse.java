package functional.reflection;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

class Annotations {
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Input {
        String value();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Operation {
        String value();
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DependsOn {
        String value();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface FinalResult {
    }
}

class SqlQueryBuilder {

    @Annotations.Input("ids")
    private List<String> ids;

    @Annotations.Input("limit")
    private Integer limit;

    @Annotations.Input("table")
    private String tableName;

    @Annotations.Input("columns")
    private List<String> columnNames;

    public SqlQueryBuilder(List<String> ids, Integer limit, String tableName, List<String> columnNames) {
        this.ids = ids;
        this.limit = limit;
        this.tableName = tableName;
        this.columnNames = columnNames;
    }

    @Annotations.Operation("SelectBuilder")
    public String selectStatementBuilder(@Annotations.Input("table") String tableName, @Annotations.Input("columns") List<String> columnNames) {
        String columnsString = columnNames.isEmpty() ? "*" : String.join(",", columnNames);

        return String.format("SELECT %s FROM %s", columnsString, tableName);
    }

    @Annotations.FinalResult
    public String addWhereClause(@Annotations.DependsOn("SelectBuilder") String query, @Annotations.Input("ids") List<String> ids) {
        if (ids.isEmpty()) {
            return query;
        }

        return String.format("%s WHERE id IN (%s)", query, String.join(",", ids));
    }

    public String addLimit(@Annotations.DependsOn("SelectBuilder") String query, @Annotations.Input("limit") Integer limit) {
        if (limit == null || limit == 0) {
            return query;
        }

        if (limit < 0) {
            throw new RuntimeException("limit cannot be negative");
        }

        return String.format("%s LIMIT %d", query, limit.intValue());
    }
}

public class GraphTraverse {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        SqlQueryBuilder sqlQueryBuilder = new SqlQueryBuilder(Arrays.asList("1", "2", "3"),
                10,
                "Movies",
                Arrays.asList("Id", "Name"));

        String sqlQuery = execute(sqlQueryBuilder);
        System.out.println(sqlQuery);
    }

    public static <T> T execute(Object instance) throws InvocationTargetException, IllegalAccessException {
        Class<?> clazz = instance.getClass();

        Map<String, Method> operationToMethod = getOperationToMethod(clazz);
        Map<String, Field> inputToField = getInputToField(clazz);

        Method finalResultMethod = findFinalResultMethod(clazz);

        return (T) executeWithDependencies(instance, finalResultMethod, operationToMethod, inputToField);
    }

    private static Object executeWithDependencies(Object instance,
                                                  Method currentMethod,
                                                  Map<String, Method> operationToMethod,
                                                  Map<String, Field> inputToField) throws InvocationTargetException, IllegalAccessException {
        List<Object> parameterValues = new ArrayList<>(currentMethod.getParameterCount());

        for (Parameter parameter : currentMethod.getParameters()) {
            Object value = null;
            if (parameter.isAnnotationPresent(Annotations.DependsOn.class)) {
                String dependencyOperationName = parameter.getAnnotation(Annotations.DependsOn.class).value();
                Method dependencyMethod = operationToMethod.get(dependencyOperationName);

                value = executeWithDependencies(instance, dependencyMethod, operationToMethod, inputToField);
            } else if (parameter.isAnnotationPresent(Annotations.Input.class)) {
                String inputName = parameter.getAnnotation(Annotations.Input.class).value();

                Field field = inputToField.get(inputName);
                field.setAccessible(true);

                value = field.get(instance);
            }

            parameterValues.add(value);
        }

        return currentMethod.invoke(instance, parameterValues.toArray());
    }

    private static Map<String, Field> getInputToField(Class<?> clazz) {
        Map<String, Field> inputNameToField = new HashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Annotations.Input.class)) {
                continue;
            }

            Annotations.Input input = field.getAnnotation(Annotations.Input.class);
            inputNameToField.put(input.value(), field);
        }

        return inputNameToField;
    }

    private static Map<String, Method> getOperationToMethod(Class<?> clazz) {
        Map<String, Method> operationNameToMethod = new HashMap<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Annotations.Operation.class)) {
                continue;
            }

            Annotations.Operation operation = method.getAnnotation(Annotations.Operation.class);

            operationNameToMethod.put(operation.value(), method);
        }
        return operationNameToMethod;
    }

    private static Method findFinalResultMethod(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Annotations.FinalResult.class)) {
                return method;
            }
        }

        throw new RuntimeException("No method found with FinalResult annotation");
    }

}
