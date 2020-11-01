package com.andreas.SelfScannerPOS.main.java.dao;

import com.andreas.SelfScannerPOS.main.java.common.Shared;
import com.andreas.SelfScannerPOS.main.java.model.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductsDB {

    public static Product getProductByBarcode(String barcode) {
        if (barcode.equals("")) return null;
        try {
            URL url = new URL("https://self-scanner-pos.herokuapp.com/products/barcode/" + barcode);//your url i.e fetch data from .
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) {
                try {
                    output = output.replaceAll("\\[", "").replaceAll("\\]","");
                    JSONObject jsonObject = new JSONObject(output);
                    return new Product(jsonObject.get("barcode").toString(), jsonObject.get("item_name_" + Shared.language.toUpperCase()).toString(),
                            Integer.parseInt(jsonObject.get("units").toString()), jsonObject.get("price").toString());
                }catch (JSONException err){
                    System.out.println("Exception when converting String to JSON: " + err);
                }
            }
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
        return null;
    }

    public static List<Product> getProductsByCategory(String category) {
        List <Product> listProducts = new ArrayList<>();
        try {
            URL url = new URL("https://self-scanner-pos.herokuapp.com/products/category/" + category);//your url i.e fetch data from .
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) {
                try {
                    JSONArray jsonArray = new JSONArray(output);
                    for (Object obj: jsonArray) {
                        JSONObject jsonObject = new JSONObject(obj.toString());
                        listProducts.add(new Product(jsonObject.get("barcode").toString(), jsonObject.get("item_name_" + Shared.language.toUpperCase()).toString(),
                                Integer.parseInt(jsonObject.get("units").toString()), jsonObject.get("price").toString(), jsonObject.get("item_photo").toString()));
                    }
                }catch (JSONException err){
                    System.out.println("Exception when converting String to JSON: " + err);
                }
            }
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
        return listProducts;
    }

    public static List<Product> getProductsByName(String name) {
        List <Product> listProducts = new ArrayList<>();
        try {
            URL url = new URL("https://self-scanner-pos.herokuapp.com/products/item_name_" + Shared.language.toUpperCase() + "/" + name);//your url i.e fetch data from .
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;
            while ((output = br.readLine()) != null) {
                try {
                    JSONArray jsonArray = new JSONArray(output);
                    for (Object obj: jsonArray) {
                        JSONObject jsonObject = new JSONObject(obj.toString());
                        listProducts.add(new Product(jsonObject.get("barcode").toString(), jsonObject.get("item_name_" + Shared.language.toUpperCase()).toString(),
                                Integer.parseInt(jsonObject.get("units").toString()), jsonObject.get("price").toString(), jsonObject.get("item_photo").toString()));
                    }
                }catch (JSONException err){
                    System.out.println("Exception when converting String to JSON: " + err);
                }
            }
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
        return listProducts;
    }
}
