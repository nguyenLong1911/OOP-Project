/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thuThapDuLieu;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
/**
 *
 * @author ACER
 */
public class InputProcessor {
    
    public void saveJsonObjectToJsonFile(InputData inputData) {
        String filename = "Data/project.json";

        // Tạo đối tượng JsonObject từ đối tượng inputData
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Link bài viết", inputData.getLink());
        jsonObject.addProperty("Website", inputData.getWebsite());
        jsonObject.addProperty("Loại bài viết", inputData.getPostType());
        jsonObject.addProperty("Tóm tắt bài viết", inputData.getSummary());
        jsonObject.addProperty("Tiêu đề bài viết", inputData.getTitle());
        jsonObject.addProperty("Nội dung chi tiết bài viết", inputData.getContent());
        jsonObject.addProperty("Ngày tạo", inputData.getDate());
        jsonObject.addProperty("Hashtag", inputData.getHashtag());
        jsonObject.addProperty("Tên tác giả nếu có", inputData.getAuthor());
        jsonObject.addProperty("Chuyên mục mà bài viết thuộc về", inputData.getCategory());

        // Đọc và cập nhật dữ liệu trong tệp JSON
        try (FileReader reader = new FileReader(filename)) {
            // Đọc dữ liệu từ tệp JSON hiện có nếu có
            JsonObject existingData = new Gson().fromJson(reader, JsonObject.class);

            // Kiểm tra xem tệp JSON có dữ liệu không
            if (existingData != null && existingData.has("News")) {
                // Tạo mảng JSON từ dữ liệu hiện có
                JsonArray jsonArray = existingData.getAsJsonArray("News");
                // Thêm dữ liệu mới vào mảng JSON
                jsonArray.add(jsonObject);
            } else {
                // Tạo một mảng JSON mới và thêm dữ liệu mới vào đó
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(jsonObject);
                // Tạo đối tượng JsonObject mới với mảng JSON
                existingData = new JsonObject();
                existingData.add("News", jsonArray);
            }

            // Ghi dữ liệu mới vào tệp JSON
            try (FileWriter writer = new FileWriter(filename)) {
                Gson gson = new Gson();
                gson.toJson(existingData, writer);
                System.out.println("Data saved to project.json successfully.");
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        InputData inputData = new InputData();
        // Set giá trị cho inputData từ đối tượng đã có
        inputData.getInputData();
        InputProcessor inputProcessor = new InputProcessor();
        inputProcessor.saveJsonObjectToJsonFile(inputData);
    }
}
