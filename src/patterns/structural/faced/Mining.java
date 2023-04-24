package patterns.structural.faced;

class Site {
    private String name;

    public Site(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class MineSites {
    Site site;

    public MineSites(Site site) {
        this.site = site;
    }

    void checkingMineSite() {
        System.out.println("Checking " + this.site.getName() + " site.");
    }

    void reportingHazards() {
        System.out.println("Reporting hazards of " + this.site.getName() + " site.");
    }
}

class Resource {
    private String staffs;

    public Resource(String staffs) {
        this.staffs = staffs;
    }

    public String getStaffs() {
        return staffs;
    }
}

class ResourceRemover {
    Resource resource;

    public ResourceRemover(Resource resource) {
        this.resource = resource;
    }

    void excavator() {
        System.out.println("Excavator " + resource.getStaffs());
    }

    void drill() {
        System.out.println("Drill " + resource.getStaffs());
    }

    void blasting() {
        System.out.println("Blasting " + resource.getStaffs());
    }
}

class Load{

    void loadOntoTruck(){
        System.out.println("Load GOLDEN onto Truck");
    }
}

class MiningFaced{
    void makeMineSites(String site){
        MineSites mineSites = new MineSites(new Site(site));
        mineSites.checkingMineSite();
        mineSites.reportingHazards();
    }
    void makeResourceRemover(String resource){
        ResourceRemover resourceRemover = new ResourceRemover(new Resource(resource));
        resourceRemover.blasting();
        resourceRemover.drill();
        resourceRemover.excavator();
    }
    void makeLoad(){
        Load load = new Load();
        load.loadOntoTruck();
    }
}
public class Mining {
    public static void main(String[] args) {
        MiningFaced miningFaced = new MiningFaced();
        miningFaced.makeMineSites("Main SITE");
        miningFaced.makeResourceRemover("SUBSTANCES");
        miningFaced.makeLoad();
    }
}
