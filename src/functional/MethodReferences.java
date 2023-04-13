package functional;

interface Callable{
    void call(String msg);
}

class Describe {
    void show(String msg){
        System.out.println(msg);
    }
}

public class MethodReferences {
    static void hello(String name){
        System.out.println("Hello, " + name);
    }
    static class Description{
        String about;

        public Description(String about) {
            this.about = about;
        }

        void help(String msg){
            System.out.println(about + " " + msg);
        }
    }

    static class Helper{
        static void assist(String msg){
            System.out.println(msg);
        }
    }

    public static void main(String[] args) {
        Describe d = new Describe();
        Callable c = d::show;
        c.call("Describe");

        c = new Description("Valuable ")::help;
        c.call("Information");

        c = MethodReferences::hello;
        c.call("Raha");

        c = Helper::assist;
        c.call("Bob");
    }
}
