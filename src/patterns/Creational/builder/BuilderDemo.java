package patterns.Creational.builder;

import java.util.function.Consumer;

class MobileBuilder{
    int ram, storage;
    int battery;
    int camera;
    String processor;
    double screenSize;

    public MobileBuilder with(Consumer<MobileBuilder> buildFields) {
        buildFields.accept(this);
        return this;
    }

    public Mobile createMobile() {
        return new Mobile(this);
    }
}

class Mobile {

    final int ram, storage;
    final int battery;
    final int camera;
    final String processor;
    final double screenSize;

    public Mobile(MobileBuilder builder) {
        this.ram = builder.ram;
        this.storage = builder.storage;
        this.battery = builder.battery;
        this.camera = builder.camera;
        this.processor = builder.processor;
        this.screenSize = builder.screenSize;
    }

    public int getRam() {
        return ram;
    }

    public int getStorage() {
        return storage;
    }

    public int getBattery() {
        return battery;
    }

    public int getCamera() {
        return camera;
    }

    public String getProcessor() {
        return processor;
    }

    public double getScreenSize() {
        return screenSize;
    }

    public String toString() {
        return String.format("Specifications- RAM: %d Storage: %d Battery: %d Camera: %dMP Processor: %s Display: %f''", ram, storage, battery, camera, processor, screenSize);
    }
}

public class BuilderDemo {
    public static void main(String[] args) {
        MobileBuilder builder = new MobileBuilder();

        Mobile myMobile = builder.with(myBuilder -> {
            myBuilder.ram = 4;
            myBuilder.battery = 4000;
            myBuilder.processor = "A12 Bionic";
        }).createMobile();

        System.out.println(myMobile);
    }
}
