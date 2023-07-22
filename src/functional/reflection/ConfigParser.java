package functional.reflection;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

class GameConfig {
    private int releaseYear;
    private String gameName;
    private double price;
    private String[] characterNames;

    public int getReleaseYear() {
        return this.releaseYear;
    }

    public String getGameName() {
        return this.gameName;
    }

    public double getPrice() {
        return this.price;
    }

    public String[] getCharacterNames() {
        return characterNames;
    }

    @Override
    public String toString() {
        return "GameConfig{" + "releaseYear=" + releaseYear + ", gameName='" + gameName + '\'' + ", price=" + price + ", characterName=" + Arrays.toString(characterNames) + '}';
    }
}

class UserInterfaceConfig {
    private String titleColor;
    private String titleText;
    private String footerText;
    private short titleFontSize;
    private short footerFontSize;
    private String[] titleFonts;
    private short[] titleTextSizes;

    public String getTitleColor() {
        return titleColor;
    }

    public String getTitleText() {
        return titleText;
    }

    public String getFooterText() {
        return footerText;
    }

    public short getTitleFontSize() {
        return titleFontSize;
    }

    public short getFooterFontSize() {
        return footerFontSize;
    }

    public String[] getTitleFonts() {
        return titleFonts;
    }

    public short[] getTitleTextSizes() {
        return titleTextSizes;
    }

    @Override
    public String toString() {
        return "UserInterfaceConfig{" + "titleColor='" + titleColor + '\'' + ", titleText='" + titleText + '\'' + ", footerText='" + footerText + '\'' + ", titleFontSize=" + titleFontSize + ", footerFontSize=" + footerFontSize + ", titleFonts=" + Arrays.toString(titleFonts) + ", titleTextSizes=" + Arrays.toString(titleTextSizes) + '}';
    }
}

public class ConfigParser {
    private static final Path GAME_CONFIG_PATH = Path.of("resources/game-properties.cfg");
    private static final Path UI_CONFIG_PATH = Path.of("resources/user-interface.cfg");

    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        GameConfig gameConfig = createConfigObject(GameConfig.class, GAME_CONFIG_PATH);
        System.out.println(gameConfig);

        UserInterfaceConfig userInterfaceConfig = createConfigObject(UserInterfaceConfig.class, UI_CONFIG_PATH);
        System.out.println(userInterfaceConfig);
    }

    private static Object parseValue(Class<?> type, String value) {
        if (type.equals(int.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(short.class)) {
            return Short.parseShort(value);
        } else if (type.equals(long.class)) {
            return Long.parseLong(value);
        } else if (type.equals(double.class)) {
            return Double.parseDouble(value);
        } else if (type.equals(float.class)) {
            return Float.parseFloat(value);
        } else if (type.equals(String.class)) {
            return value;
        }
        throw new RuntimeException(String.format("Type : %s unsupported", type.getTypeName()));
    }

    static <T> T createConfigObject(Class<T> clazz, Path path) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Scanner scanner = new Scanner(path);

        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        T configInstance = constructor.newInstance();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] nameValuePair = line.split("=");

            if (nameValuePair.length != 2) {
                continue;
            }

            String propertyName = nameValuePair[0];
            String propertyValue = nameValuePair[1];

            Field field;
            try {
                field = clazz.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                System.out.printf("Property name : %s is unsupported%n", propertyName);
                continue;
            }

            field.setAccessible(true);
            Object parsedValue;

            if (field.getType().isArray()) {
                parsedValue = parseArray(field.getType().componentType(), propertyValue);
            } else {
                parsedValue = parseValue(field.getType(), propertyValue);
            }


            field.set(configInstance, parsedValue);

        }

        return configInstance;
    }

    private static Object parseArray(Class<?> type, String value) {
        String[] elements = value.split(",");

        Object arrayObject = Array.newInstance(type, elements.length);

        for (int i = 0; i < elements.length; i++) {
            Array.set(arrayObject, i, parseValue(type, elements[i]));
        }
        return arrayObject;
    }

}
