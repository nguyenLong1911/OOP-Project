/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thuThapDuLieu;

import java.util.*;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileWriter;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.FileReader;


/**
 *
 * @author ACER
 */

//aggregates InputStrings class
public class InputProcessor extends InputStrings{
//    private Gson json = new Gson();
    
//    public void parseObjectToJson(InputStrings inputStrings) {
//        inputStrings.getInputStrings();
//        json.toJson(inputStrings);
//    }
//    
//    public void initializeJsonArray() {
//        // Tạo một đối tượng JSON chứa mảng rỗng
//        jsonObject.put("News", jsonArray);
//
//        // Ghi JSON vào tệp
//        try (FileWriter file = new FileWriter("project.json")) {
//            file.write(jsonObject.toString());
//            System.out.println("Successfully wrote JSON to file.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    
    public void addJsonObjectToArray() {
        this.getInputStrings();
        // Đọc nội dung tệp JSON đã có
        JsonObject jsonObject = null;
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader("D:\\Long\\Test\\src\\main\\java\\thuThapDuLieu\\project.json"));
            jsonObject = jsonElement.getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Chuyển đổi đối tượng Java thành JSON
        Gson gson = new Gson();
        JsonElement personJson = gson.toJsonTree(this);

        // Thêm đối tượng JSON vào mảng JSON đã có
        JsonArray jsonArray;
        if (jsonObject.has("News")) {
            jsonArray = jsonObject.getAsJsonArray("News");
        } else {
            jsonArray = new JsonArray();
        }
        jsonArray.add(personJson);
        jsonObject.add("News", jsonArray);

        // Lưu nội dung mới vào tệp JSON
        try (FileWriter writer = new FileWriter("D:\\Long\\Test\\src\\main\\java\\thuThapDuLieu\\project.json")) {
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String args[]) {
        InputProcessor i = new InputProcessor();
        i.addJsonObjectToArray();
    }
}
