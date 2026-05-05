package com.cts.connectease.constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConstants {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = AppConstants.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in != null) props.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Cannot load config.properties: " + e.getMessage());
        }
    }

    public static final String BASE_URL           = props.getProperty("base.url", "http://localhost:4200");
    public static final String CUSTOMER_EMAIL     = props.getProperty("customer.email");
    public static final String CUSTOMER_PASSWORD  = props.getProperty("customer.password");
    public static final String VENDOR_EMAIL       = props.getProperty("vendor.email");
    public static final String VENDOR_PASSWORD    = props.getProperty("vendor.password");
}
