/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thuThapDuLieu;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
/**
 *
 * @author ACER
 */
public class InputProcessorChatGPT {
    
    public void getInputAndSaveToJson() {
        Scanner scanner = new Scanner(System.in);
        JsonArray jsonArray = new JsonArray();
        
        for (int i = 0; i < 10; i++) {
            System.out.println("Input String " + (i + 1) + ":");
            String input = scanner.nextLine();
            jsonArray.add(input);
        }
        
        scanner.close();
        
        // Create JSON object to hold the array
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("strings", jsonArray);
        
        // Write JSON to file
        try (FileWriter writer = new FileWriter("D:\\Long\\Test\\src\\main\\java\\thuThapDuLieu\\project.json")) {
            Gson gson = new Gson();
            gson.toJson(jsonObject, writer);
            System.out.println("Data saved to output.json successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        InputProcessorChatGPT inputProcessor = new InputProcessorChatGPT();
        inputProcessor.getInputAndSaveToJson();
    }
}
