package com.qa.api.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return properties.getProperty("api.base.url");
    }

    public static String getAuthToken() {
        return properties.getProperty("api.auth.token");
    }

    public static int getTimeout() {
        return Integer.parseInt(properties.getProperty("api.timeout"));
    }

    public static String getExtentReportPath() {
        return properties.getProperty("extent.report.path");
    }

    public static String getExtentReportName() {
        return properties.getProperty("extent.report.name");
    }
}
