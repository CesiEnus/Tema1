import java.util.Arrays;

public class PublishingBrand implements IPublishingArtifact {

    int ID;
    String name;
    Book[] books;

    public PublishingBrand(int ID, String name, Book[] books) {
        this.ID = ID;
        this.name = name;
        this.books = new Book[books.length];
        System.arraycopy(books, 0, this.books, 0, books.length);
    }

    public String Publish(){
        String Pub = "<xml>\n" +
                     "\t<publishingBrand>\n" +
                     "\t\t<ID>" + ID + "</ID>\n" +
                     "\t\t<Name>" + name + "</Name>\n" +
                     "\t</publishingBrand>\n" +
                     "\t<books>\n";
        StringBuilder Pub1 = new StringBuilder();
        for(Book book: books) {
            Pub1.append("\t\t<book>" +
                    "\t\t\t<title>" + book.name + "</title>\n" +
                    "\t\t\t<subtitle>" + book.subtitle + "</subtitle>\n" +
                    "\t\t\t<isbn>" + book.ISBN + "</isbn>\n" +
                    "\t\t\t<pageCount>" + book.pageCount + "</pageCount>\n" +
                    "\t\t\t<keywords>" + book.keywords + "</keywords>\n" +
                    "\t\t\t<LanguageID>" + book.languageID + "</languageID>\n" +
                    "\t\t\t<createdOn>" + book.createdOn + "</createdOn>\n" +
                    "\t\t\t<authors>" + Arrays.toString(book.authors) + "</authors>" +
                    "\t\t</book>"
            );
        }
        return Pub + Pub1 + "\t</books>\n</xml>";
    }
}
