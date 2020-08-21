package com.tirsportif.backend.utils;

public final class NameNormalizer {

    public static String normalize(String name) {
        String temp = name.trim().toLowerCase();
        return temp.substring(0, 1).toUpperCase() + (temp.length() > 1 ? temp.substring(1) : "");
    }

}
