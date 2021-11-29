import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Administration {
    static public ArrayList<Countries> countries = new ArrayList<>();
    static public ArrayList<Language> languages = new ArrayList<>();
    static public ArrayList<Book> books = new ArrayList<>();
    static public ArrayList<Author> authors = new ArrayList<>();
    static public ArrayList<EditorialGroup> editorialGroups = new ArrayList<>();
    static public ArrayList<PublishingBrand> publishingBrands = new ArrayList<>();
    static public ArrayList<PublishingRetailer> publishingRetailers = new ArrayList<>();

    static public HashMap<String, ArrayList<String>> bookAuthors = new HashMap<>();
    static public HashMap<String, ArrayList<String>> editorialBooks = new HashMap<>();
    static public HashMap<String, ArrayList<String>> publishingBrandBooks = new HashMap<>();
    static public HashMap<String, ArrayList<String>> publishingRetailerCountries = new HashMap<>();
    static public HashMap<String, ArrayList<String>> publishingRetailerBooks = new HashMap<>();
    static public HashMap<String, ArrayList<String>> publishingRetailerEditorialGroups = new HashMap<>();
    static public HashMap<String, ArrayList<String>> publishingRetailerPublishingBrands = new HashMap<>();

    static public ArrayList<Book> getBooksForPublishingRetailerID(int publishingRetailerID) {
        ArrayList<Book> bookRet = new ArrayList<>();
        HashMap<Integer, Boolean> booksIDs = new HashMap<>();
        for(PublishingRetailer publishingRetailer : publishingRetailers) {
            if(publishingRetailer.ID == publishingRetailerID) {
                for(IPublishingArtifact publishingArtifact : publishingRetailer.publishingArtifacts) {
                    if(publishingArtifact instanceof Book && !booksIDs.containsKey(((Book)publishingArtifact).ID)) {
                            bookRet.add((Book)publishingArtifact);
                            booksIDs.put(((Book)publishingArtifact).ID, true);
                    }
                    if(publishingArtifact instanceof EditorialGroup) {
                        for(Book book: ((EditorialGroup)publishingArtifact).books) {
                            if(!booksIDs.containsKey(book.ID)) {
                                bookRet.add(book);
                                booksIDs.put(book.ID, true);
                            }
                        }
                    }
                    if(publishingArtifact instanceof PublishingBrand) {
                        for(Book book: ((PublishingBrand)publishingArtifact).books) {
                            if(!booksIDs.containsKey(book.ID)) {
                                bookRet.add(book);
                                booksIDs.put(book.ID, true);
                            }
                        }
                    }
                }
                break;
            }
        }
        return bookRet;
    }

    static public ArrayList<Language>getLanguagesForPublishingRetailerID(int publishingReatilerID) {
        ArrayList<Book> retailerBooks = getBooksForPublishingRetailerID(publishingReatilerID);
        ArrayList<Language> languagesRet = new ArrayList<>();
        HashMap<Integer, Boolean> languagesIDs = new HashMap<>();
        for(Book book : retailerBooks) {
            for(Language language : languages) {
                if(book.languageID == language.ID && !languagesIDs.containsKey(language.ID)) {
                    languagesRet.add(language);
                    languagesIDs.put(language.ID, true);
                }
            }
        }
        return languagesRet;
    }

    static public ArrayList<Countries> getCountriesForBookID(int bookID) {
        ArrayList<Countries> countriesForBook = new ArrayList<>();
        int stop = 0;
        for(PublishingRetailer publishingRetailer: publishingRetailers) {
            ArrayList<Book> publishingRetailerBooks = getBooksForPublishingRetailerID(publishingRetailer.ID);
            for(Book book : publishingRetailerBooks) {
                if(book.ID == bookID) {
                    countriesForBook.addAll(Arrays.asList(publishingRetailer.countries));
                    stop = 1;
                    break;
                }
            }
            if(stop == 1)
                break;
        }
        return countriesForBook;
    }

    static public ArrayList<Book> getCommonBooksForRetailerIDs(int retailerID1, int retailerID2) {
        ArrayList<Book> booksForRetailer1 = getBooksForPublishingRetailerID(retailerID1);
        ArrayList<Book> booksForRetailer2 = getBooksForPublishingRetailerID(retailerID2);
        ArrayList<Book> booksRet = getBooksForPublishingRetailerID(retailerID2);
        HashMap<Integer, Integer> frequencyForBookID = new HashMap<>();
        for(Book book: booksForRetailer1) {
            frequencyForBookID.putIfAbsent(book.ID, 0);
            frequencyForBookID.put(book.ID, frequencyForBookID.get(book.ID) + 1);
        }
        for(Book book: booksForRetailer2) {
            frequencyForBookID.putIfAbsent(book.ID, 0);
            frequencyForBookID.put(book.ID, frequencyForBookID.get(book.ID) + 1);
        }
        for(Book book : booksForRetailer1) {
            if(frequencyForBookID.get(book.ID) >= 2) {
                booksRet.add(book);
            }
        }
        return booksRet;
    }

    static public ArrayList<Book> getAllBooksForRetailerIDs (int retailerID1, int retailerID2) {
        ArrayList<Book> booksForRetailer1 = getBooksForPublishingRetailerID(retailerID1);
        ArrayList<Book> booksForRetailer2 = getBooksForPublishingRetailerID(retailerID2);
        ArrayList<Book> booksRet = getBooksForPublishingRetailerID(retailerID2);
        HashMap<Integer, Integer> frequencyForBookID = new HashMap<>();
        for(Book book: booksForRetailer1) {
            frequencyForBookID.putIfAbsent(book.ID, 0);
            frequencyForBookID.put(book.ID, frequencyForBookID.get(book.ID) + 1);
        }
        for(Book book: booksForRetailer2) {
            frequencyForBookID.putIfAbsent(book.ID, 0);
            frequencyForBookID.put(book.ID, frequencyForBookID.get(book.ID) + 1);
        }
        booksRet.addAll(booksForRetailer1);
        for(Book book: booksForRetailer2) {
            if(frequencyForBookID.get(book.ID) == 1) {
                booksRet.add(book);
            }
        }
        return booksRet;
    }

    static public ArrayList<String> readFromFile(String file) throws IOException {
        ArrayList<String> a = new ArrayList<>();
        int firstLine = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(firstLine == 1) {
                    firstLine = 0;
                    continue;
                }
                a.add(line);
            }
        }
        return a;
    }

    public static void main(String[] args) throws IOException {
        ArrayList<String> countriesData = readFromFile("init/countries.in");
        for (String line : countriesData) {
            String[] words = line.split("###");
            countries.add(new Countries(Integer.parseInt(words[0]), words[1]));
        }

        ArrayList<String> languagesData = readFromFile("init/languages.in");
        for (String line : languagesData) {
            String[] words = line.split("###");
            languages.add(new Language(Integer.parseInt(words[0]), words[1], words[2]));
        }

        ArrayList<String> bookAuthorsData = readFromFile("init/books-authors.in");
        for(String line : bookAuthorsData) {
            String[] words = line.split("###");
            bookAuthors.putIfAbsent(words[0], new ArrayList<String>());
            bookAuthors.get(words[0]).add(words[1]);
        }

        ArrayList<String> booksData = readFromFile("init/books.in");
        for (String line : booksData) {
            String[] words = line.split("###");
            ArrayList<Author> authorsTemp = new ArrayList<>();
            if(bookAuthors.get(words[0]) != null) {
                for(String authorID: bookAuthors.get(words[0])) {
                    for(Author author: authors) {
                        if(author.ID == Integer.parseInt(authorID))
                            authorsTemp.add(author);
                    }
                }
            }

            Author[] authorsTempArr = new Author[authorsTemp.size()];
            for(int i = 0; i < authorsTempArr.length; i++)
                authorsTempArr[i] = authorsTemp.get(i);
            books.add(new Book(
                    Integer.parseInt(words[0]),
                    words[1], words[2], words[3],
                    Integer.parseInt(words[4]),
                    words[5], Integer.parseInt(words[6]), words[7],
                    authorsTempArr
            ));
        }

        ArrayList<String> authorsData = readFromFile("init/authors.in");
        for (String line : authorsData) {
            String[] words = line.split("###");
            authors.add(new Author(Integer.parseInt(words[0]), words[1], words[2]));
        }

        ArrayList<String> editorialBooksData = readFromFile("init/editorial-groups-books.in");
        for(String line : editorialBooksData) {
            String[] words = line.split("###");
            editorialBooks.putIfAbsent(words[0], new ArrayList<String>());
            editorialBooks.get(words[0]).add(words[1]);
        }
        ArrayList<String> editorialGroupsData = readFromFile("init/editorial-groups.in");
        for(String line : editorialGroupsData) {
            String[] words = line.split("###");
            ArrayList<Book> booksTemp = new ArrayList<>();
            if(editorialBooks.get(words[0]) != null) {
                for(String bookID: editorialBooks.get(words[0])) {
                    long ID = Integer.parseInt(bookID);

                    for(Book book: books) {
                        if(book.ID == ID)
                            booksTemp.add(book);
                    }
                }
            }

            Book[] booksTempArr = new Book[booksTemp.size()];
            for(int i = 0; i < booksTempArr.length; i++)
                booksTempArr[i] = booksTemp.get(i);
            editorialGroups.add(new EditorialGroup(Integer.parseInt(words[0]), words[1], booksTempArr));
        }

        ArrayList<String> publishingBrandBooksData = readFromFile("init/publishing-brands-books.in");
        for(String line : publishingBrandBooksData) {
            String[] words = line.split("###");
            publishingBrandBooks.putIfAbsent(words[0], new ArrayList<String>());
            publishingBrandBooks.get(words[0]).add(words[1]);
        }

        ArrayList<String> publishingBrandsData = readFromFile("init/publishing-brands.in");
        for(String line : publishingBrandsData) {
            String[] words = line.split("###");
            ArrayList<Book> booksTemp = new ArrayList<>();
            if(publishingBrandBooks.get(words[0]) != null) {
                for(String bookID: publishingBrandBooks.get(words[0])) {
                    long ID = Integer.parseInt(bookID);

                    for(Book book: books) {
                        if(book.ID == ID)
                            booksTemp.add(book);
                    }
                }
            }
            Book[] booksTempArr = new Book[booksTemp.size()];
            for(int i = 0; i < booksTempArr.length; i++)
                booksTempArr[i] = booksTemp.get(i);
            publishingBrands.add(new PublishingBrand(Integer.parseInt(words[0]), words[1], booksTempArr));
        }

        ArrayList<String> publishingRetailerCountriesData = readFromFile("init/publishing-retailers-countries.in");
        for(String line : publishingRetailerCountriesData) {
            String[] words = line.split("###");
            publishingRetailerCountries.putIfAbsent(words[0], new ArrayList<String>());
            publishingRetailerCountries.get(words[0]).add(words[1]);
        }

        ArrayList<String> publishingRetailerBooksData = readFromFile("init/publishing-retailers-books.in");
        for(String line : publishingRetailerBooksData) {
            String[] words = line.split("###");
            publishingRetailerBooks.putIfAbsent(words[0], new ArrayList<String>());
            publishingRetailerBooks.get(words[0]).add(words[1]);
        }

        ArrayList<String> publishingRetailerEditorialGroupsData = readFromFile("init/publishing-retailers-editorial-groups.in");
        for(String line : publishingRetailerEditorialGroupsData) {
            String[] words = line.split("###");
            publishingRetailerEditorialGroups.putIfAbsent(words[0], new ArrayList<String>());
            publishingRetailerEditorialGroups.get(words[0]).add(words[1]);
        }

        ArrayList<String> publishingRetailerPublishingBrandsData = readFromFile("init/publishing-retailers-publishing-brands.in");
        for(String line : publishingRetailerPublishingBrandsData) {
            String[] words = line.split("###");
            publishingRetailerPublishingBrands.putIfAbsent(words[0], new ArrayList<String>());
            publishingRetailerPublishingBrands.get(words[0]).add(words[1]);
        }

        ArrayList<String> publishingRetailersData = readFromFile("init/publishing-retailers.in");
        for(String line : publishingRetailersData) {
            String[] words = line.split("###");
            ArrayList<Countries> countriesTemp = new ArrayList<>();
            ArrayList<IPublishingArtifact> publishingArtifactsTemp = new ArrayList<>();
            if(publishingRetailerCountries.get(words[0]) != null) {
                for(String countryID : publishingRetailerCountries.get(words[0])) {
                    for(Countries country : countries) {
                        if(country.ID == Integer.parseInt(countryID))
                            countriesTemp.add(country);
                    }
                }
            }
            if(publishingRetailerBooks.get(words[0]) != null) {
                for(String bookID : publishingRetailerBooks.get(words[0])) {
                    for(Book book : books) {
                        if(book.ID == Integer.parseInt(bookID))
                            publishingArtifactsTemp.add(book);
                    }
                }
            }
            if(publishingRetailerEditorialGroups.get(words[0]) != null) {
                for(String bookID : publishingRetailerEditorialGroups.get(words[0])) {
                    for(Book book : books) {
                        if(book.ID == Integer.parseInt(bookID))
                            publishingArtifactsTemp.add(book);
                    }
                }
            }
            if(publishingRetailerPublishingBrands.get(words[0]) != null) {
                for(String bookID : publishingRetailerPublishingBrands.get(words[0])) {
                    for(Book book : books) {
                        if(book.ID == Integer.parseInt(bookID))
                            publishingArtifactsTemp.add(book);
                    }
                }
            }

            Countries[] countriesTempArr = new Countries[countriesTemp.size()];
            for(int i = 0; i < countriesTempArr.length; i++)
                countriesTempArr[i] = countriesTemp.get(i);
            IPublishingArtifact[] publishingArtifactsTempArr = new IPublishingArtifact[publishingArtifactsTemp.size()];
            for(int i = 0; i < publishingArtifactsTempArr.length; i++)
                publishingArtifactsTempArr[i] = publishingArtifactsTemp.get(i);
            publishingRetailers.add(new PublishingRetailer(
                    Integer.parseInt(words[0]),
                    words[1],
                    publishingArtifactsTempArr,
                    countriesTempArr
            ));
        }
        System.out.println(getBooksForPublishingRetailerID(1));
        System.out.println(getBooksForPublishingRetailerID(2));
        System.out.println(getBooksForPublishingRetailerID(3));
        System.out.println(getBooksForPublishingRetailerID(4));
        System.out.println(getBooksForPublishingRetailerID(5));

        System.out.println(getLanguagesForPublishingRetailerID(1));
        System.out.println(getLanguagesForPublishingRetailerID(2));
        System.out.println(getLanguagesForPublishingRetailerID(3));
        System.out.println(getLanguagesForPublishingRetailerID(4));
        System.out.println(getLanguagesForPublishingRetailerID(5));

        System.out.println(getCountriesForBookID(204));
        System.out.println(getCountriesForBookID(224));
        System.out.println(getCountriesForBookID(262));
        System.out.println(getCountriesForBookID(275));
        System.out.println(getCountriesForBookID(291));

        System.out.println(getCommonBooksForRetailerIDs(1, 11));
        System.out.println(getCommonBooksForRetailerIDs(2, 12));
        System.out.println(getCommonBooksForRetailerIDs(3, 13));
        System.out.println(getCommonBooksForRetailerIDs(4, 14));
        System.out.println(getCommonBooksForRetailerIDs(6, 16));

        System.out.println(getAllBooksForRetailerIDs(1, 11));
        System.out.println(getAllBooksForRetailerIDs(2, 12));
        System.out.println(getAllBooksForRetailerIDs(3, 13));
        System.out.println(getAllBooksForRetailerIDs(4, 14));
        System.out.println(getAllBooksForRetailerIDs(6, 16));
    }
}
