package functional.reflection;

import java.lang.annotation.*;

enum Role {
    MANAGER, CLERK, SUPPORT_ENGINEER
}

enum OperationType {
    READ, WRITE, DELETE, UPDATE
}
class Annotat {

    @Target(ElementType.TYPE)
    @Repeatable(PermissionsContainer.class)
    public @interface Permissions {
        Role role();

        OperationType[] allowed();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface PermissionsContainer {
        Permissions[] value();
    }
}

public class PermissionChecker {
}
