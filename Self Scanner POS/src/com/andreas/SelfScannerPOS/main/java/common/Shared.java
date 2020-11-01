package com.andreas.SelfScannerPOS.main.java.common;

import javafx.beans.property.BooleanProperty;
import java.util.List;
import java.util.Map;

public class Shared {
    public static String appName;
    public static String appVersion;
    public static List<String> availableLangs;
    public static String language;
    public static ScreenController.Screen screen;
    public static final String bagBarcode = "0000000001";
    public static BooleanProperty updateCart;
    public static int TVA;
    public static String currency;
    public static Map<String, Integer> discountCodes;
}
