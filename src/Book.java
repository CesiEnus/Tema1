import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Book implements IPublishingArtifact{

    int ID;
    String name;
    String subtitle;
    String ISBN;
    int pageCount;
    String keywords;
    int languageID;
    String createdOn;
    Author []authors;

    public Book(int ID, String name, String subtitle, String ISBN, int pageCount, String keywords, int languageID, String createdOn, Author[] authors) {
        this.ID = ID;
        this.name = name;
        this.subtitle = subtitle;
        this.ISBN = ISBN;
        this.pageCount = pageCount;
        this.keywords = keywords;
        this.languageID = languageID;
        this.createdOn = createdOn;
        this.authors = new Author[authors.length];
        System.arraycopy(authors, 0, this.authors, 0, authors.length);
    }

    @Override
     public String Publish(){

        return "<xml\n" +
               "\t<title>"+name+"</title>\n" +
               "\t<subtitle>"+subtitle+"</subtitle>\n" +
               "\t<isbn>"+ISBN+"</isbn>\n" +
               "\t<pageCount>"+pageCount+"</pageCount>\n" +
               "\t<keywords>"+keywords+"</keywords>\n" +
               "\t<languageID>"+languageID+"</languageID>\n" +
               "\t<createdOn>"+createdOn+"</createdOn>\n" +
               "\t<authors>"+ Arrays.toString(authors)+"</authors>\n" +
             "</xml>\n";
     }
}
