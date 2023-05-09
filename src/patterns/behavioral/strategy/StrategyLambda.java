package patterns.behavioral.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class Helper {
    int id;
    String name;
    long webSites;

    public Helper(int id, String name, long webSites) {
        this.id = id;
        this.name = name;
        this.webSites = webSites;
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

    @Override
    public String toString() {
        return "Helper{" + "id=" + id + ", name='" + name + '\'' + ", webSites=" + webSites + '}';
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

public class StrategyLambda {
    public static void main(String[] args) {
        List<Helper> helpers = new ArrayList<>();
        helpers.add(new Helper(1, "liquidweb.com", 80000));
        helpers.add(new Helper(2, "linode.com", 90000));
        helpers.add(new Helper(3, "digitalocean.com", 120000));
        helpers.add(new Helper(4, "aws.amazon.com", 200000));
        helpers.add(new Helper(5, "mkyong.com", 1));
        
        HelperFilter.filter(helpers, helper -> helper.getName().contains("li")).forEach(System.out::println);
        System.out.println("::::::::::::::::::::::::::::::::::::");
        HelperFilter.filter(helpers, helper -> helper.getWebSites()>80000).forEach(System.out::println);
        System.out.println("::::::::::::::::::::::::::::::::::::");
        HelperFilter.filter(helpers, helper -> helper.getId()>3).forEach(System.out::println);
    }
}
