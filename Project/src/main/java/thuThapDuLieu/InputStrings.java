/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thuThapDuLieu;

import com.google.gson.annotations.SerializedName;
import java.util.Scanner;

/**
 *
 * @author ACER
 */
public class InputStrings {
    @SerializedName("Link bài viết")
    private String link;
    
    @SerializedName("Website")
    private String website;
    
    @SerializedName("Loại bài viết")
    private String postType;
    
    @SerializedName("Tóm tắt bài viết (nếu có)")
    private String summary;
    
    @SerializedName("Tiêu đề bài viết")
    private String title;
    
    @SerializedName("Nội dung chi tiết bài viết")
    private String content;
    
    @SerializedName("Tag/Hash tag đi kèm")
    private String hashtag;
    
    @SerializedName("Ngày tạo")
    private String date;
    
    @SerializedName("Tên tác giả nếu có")
    private String author;
    
    @SerializedName("Chuyên mục mà bài viết thuộc về")
    private String category;
    
    public void getInputStrings() {
        Scanner scanner = new Scanner(System.in);
        link = scanner.nextLine();
        website = scanner.nextLine();
        postType = scanner.nextLine();
        summary = scanner.nextLine();
        title = scanner.nextLine();
        content = scanner.nextLine();
        hashtag = scanner.nextLine();
        date = scanner.nextLine();
        author = scanner.nextLine();
        category = scanner.nextLine();
        scanner.close();
    }
    
    public static void main(String args[]) {
        InputStrings inputStrings = new InputStrings();
        inputStrings.getInputStrings();
    }
}
