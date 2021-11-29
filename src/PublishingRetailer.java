import java.util.Scanner;

public class PublishingRetailer {

    int ID;
    String name;
    IPublishingArtifact[] publishingArtifacts;
    Countries[] countries;

    public PublishingRetailer(int ID, String name, IPublishingArtifact[] publishingArtifacts, Countries[] countries) {
        this.ID = ID;
        this.name = name;
        this.publishingArtifacts = new IPublishingArtifact[publishingArtifacts.length];
        System.arraycopy(publishingArtifacts, 0, this.publishingArtifacts, 0, publishingArtifacts.length);
        this.countries = new Countries[countries.length];
        System.arraycopy(countries, 0, this.countries, 0, countries.length);
    }

    public static void main(String[] args) {



    }

 }
