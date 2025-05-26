package org.booklibrary.api;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.booklibrary.entity.PersistedBook;
import org.booklibrary.mapper.PersistedBookMapper;
import org.booklibrary.model.*;
import org.booklibrary.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class BookApiDelegateImpl implements BookApiDelegate {

    private final BookRepository bookRepository;

    private final NativeWebRequest request;

    public BookApiDelegateImpl(BookRepository bookRepository, NativeWebRequest request) {
        this.bookRepository = bookRepository;
        this.request = request;
    }
    private static boolean validateISBN(Book book){
        String isbn = book.getIsbn();
        if (isbn == null || isbn.length() != 13) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checkDigit = (10 - (sum % 10)) % 10;

        return Integer.parseInt(""+isbn.charAt(12)) == checkDigit;
    }
    private static String createIsbn(boolean format){
        Random random = new Random();
        StringBuilder isbn = new StringBuilder("978"); // ISBN-13 prefix

        // Generate 9 random digits
        for (int i = 0; i < 9; i++) {
            isbn.append(random.nextInt(10));
        }

        // Calculate check digit
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checkDigit = (10 - (sum % 10)) % 10;
        isbn.append(checkDigit);

        // Format ISBN with dashes
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setGroupingUsed(false);
        final String sep = format ? "-":"";
        return isbn.substring(0, 3) + sep + isbn.substring(3, 4) + sep +
                isbn.substring(4, 8) + sep + isbn.substring(8, 12) + sep + isbn.charAt(12);
    }
    private static String createIsbn(){
        return createIsbn(false);
    }
    @PostConstruct
    private void initBooks() {
        Category novels = new Category().id(1L).name("Novels");
        Category reference = new Category().id(2L).name("Reference");
        Category magazines = new Category().id(3L).name("Magazines");
        Category romance = new Category().id(4L).name("Romance");

        bookRepository.save(createBook(1, novels, "Novel 1", "author 1", createIsbn(), new String[] {
                "url1", "url2" }, new String[] { "tag1", "tag2" }, Book.StatusEnum.AVAILABLE));
        bookRepository.save(createBook(2, novels, "Novel 2", "author 2", createIsbn(), new String[] {
                "url1", "url2" }, new String[] { "tag2", "tag3" }, Book.StatusEnum.AVAILABLE));
        bookRepository.save(createBook(3, novels, "Novel 3", "author 3", createIsbn(), new String[] {
                "url1", "url2" }, new String[] { "tag3", "tag4" }, Book.StatusEnum.REQUESTED));

        bookRepository.save(createBook(4, reference, "Reference 1", "author 4", createIsbn(),new String[] {
                "url1", "url2" }, new String[] { "tag1", "tag2" }, Book.StatusEnum.AVAILABLE));
        bookRepository.save(createBook(5, reference, "Reference 2", "author 5", createIsbn(),new String[] {
                "url1", "url2" }, new String[] { "tag2", "tag3" }, Book.StatusEnum.OUT));
        bookRepository.save(createBook(6, reference, "Reference 3", "author 6", createIsbn(), new String[] {
                "url1", "url2" }, new String[] { "tag3", "tag4" }, Book.StatusEnum.OUT));

        bookRepository.save(createBook(7, magazines, "Magazine 1", "publisher 1", createIsbn(), new String[] {
                "url1", "url2" }, new String[] { "tag1", "tag2" }, Book.StatusEnum.AVAILABLE));
        bookRepository.save(createBook(8, magazines, "Magazine 2", "publisher 2", createIsbn(), new String[] {
                "url1", "url2" }, new String[] { "tag2", "tag3" }, Book.StatusEnum.AVAILABLE));
        bookRepository.save(createBook(9, magazines, "Magazine 3", "publisher 3",createIsbn(), new String[] {
                "url1", "url2" }, new String[] { "tag3", "tag4" }, Book.StatusEnum.AVAILABLE));

        bookRepository.save(createBook(10, romance, "Romance 1", "author 7", createIsbn(),new String[] {
                "url1", "url2" }, new String[] { "tag3", "tag4" }, Book.StatusEnum.OUT));
    }


    @Override
    public ResponseEntity<Void> addBook(Book book) {
        if(!validateISBN(book)){
            return ResponseEntity.badRequest().build();
        }
        bookRepository.save(PersistedBookMapper.toEntity(book));

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<Book>> findBooksByStatus(List<String> statusList) {
        List<Book.StatusEnum> statusEnums = statusList.stream()
                .map(s -> Optional.ofNullable(Book.StatusEnum.fromValue(s))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status: " + s))
                )
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookRepository.findBooksByStatus(statusEnums).stream().map(PersistedBookMapper::toDto).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<Void> deleteBook(Long bookId, String apiKey) {
        bookRepository.deleteById(bookId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Book> getBookById(Long bookId) {
        ApiUtil.checkApiKey(request);
        return bookRepository.findById(bookId)
                .map(PersistedBookMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Void> updateBook(Book book) {
        return addBook(book);
    }

    @Override
    public ResponseEntity<Void> updateBookWithForm(Long bookId, String name, String status) {
        Book book = bookRepository.findById(bookId).map(PersistedBookMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(!StringUtils.isEmpty(name))
            book.title(name);
        if(!StringUtils.isEmpty(name))
            book.setStatus(Book.StatusEnum.fromValue(status));
        return addBook(book);
    }

    @Override
    public ResponseEntity<ModelApiResponse> uploadFile(Long bookId, String additionalMetadata, MultipartFile file) {
        try {
            String uploadedFileLocation = "./" + file.getName();
            System.out.println("uploading to " + uploadedFileLocation);
            IOUtils.copy(file.getInputStream(), new FileOutputStream(uploadedFileLocation));
            String msg = String.format("additionalMetadata: %s\nFile uploaded to %s, %d bytes", additionalMetadata, uploadedFileLocation, (new File(uploadedFileLocation)).length());
            ModelApiResponse output = new ModelApiResponse().code(200).message(msg);
            return ResponseEntity.ok(output);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't upload file", e);
        }
    }

    private static PersistedBook createBook(long id, Category category, String name, String author, String isbn, String[] urls,
                                            String[] tags, Book.StatusEnum status) {
        Book book = new Book()
                .id(id)
                .category(category)
                .title(name)
                .author(author)
                .isbn(isbn)
                .status(status);

        if (null != urls) {
            //book.setPhotoUrls(Arrays.asList(urls));
        }

        final AtomicLong i = new AtomicLong(0);
        if (null != tags) {
            Arrays.stream(tags)
                    .map(tag -> new Tag().name(tag).id(i.incrementAndGet()))
                    .forEach(book::addTagsItem);
        }
        return PersistedBookMapper.toEntity(book);
    }

}
