package com.sun.librarymanagement.utils;

public final class ApiPaths {
    private ApiPaths() {
    }

    public static final String BASE_API = "/api/v1";
    public static final String BASE_API_ADMIN = BASE_API + "/admin";
    public static final String CATEGORIES = BASE_API + "/categories";
    public static final String CATEGORIES_ADMIN = BASE_API_ADMIN + "/categories";
    public static final String BOOKS = BASE_API + "/books";
    public static final String BOOKS_ADMIN = BASE_API_ADMIN + "/books";
    public static final String AUTHORS = BASE_API + "/authors";
    public static final String AUTHORS_ADMIN = BASE_API_ADMIN + "/authors";
    public static final String PUBLISHERS = BASE_API + "/publishers";
    public static final String PUBLISHERS_ADMIN = BASE_API_ADMIN + "/publishers";
    public static final String USERS = BASE_API + "/users";
    public static final String USERS_ADMIN = BASE_API_ADMIN + "/users";
    public static final String FOLLOWS = BASE_API + "/follows";
    public static final String PROFILES = BASE_API + "/profiles";
}
