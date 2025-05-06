package com.sun.librarymanagement.utils;

public class Constant {

    public static final int USERNAME_MAX_LENGTH = 100;
    public static final int EMAIL_MAX_LENGTH = 100;
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String PAGE_SIZE_PARAM = "page_size";
    public static final String PAGE_NUMBER_PARAM = "page_number";
    public static final String BOOK_CSV_FILE_NAME = "books.csv";
    public static final String[] BOOK_EXPORT_HEADERS = {
        "ID",
        "Title",
        "Author",
        "Publisher",
        "Category",
        "Quantity",
        "Available Quantity"
    };
}
