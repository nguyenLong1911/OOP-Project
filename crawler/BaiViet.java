/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crawler;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Admin
 */
public class BaiViet {
    protected String url;
    //Các thông tin cần thu thập kể cả url ở trên
    protected String website;
    protected String blogPost;
    protected String summary;
    protected String heading;
    protected String detailedContent;
    protected String date;
    protected String[] hashtags;
    protected String author;
    protected String category;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    
    public String getWebsite() {
        return website;
    }

    public String getDetailedContent() {
        return detailedContent;
    }
    
    
    
    public boolean isValidUrl() {
        try {
        URI uri = new URI(this.url);
        return uri != null && uri.getScheme() != null && (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https"));
    } catch (URISyntaxException e) {
        e.printStackTrace();
        return false;
    }
    }
    public boolean isUrlInJson() {
        try (FileReader reader = new FileReader("src/main/java/FileStorge/Contents.json")) {
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(reader);
        if (obj instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) obj;
            for (Object o : jsonArray) {
                if (o instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) o;
                    String existingUrl = (String) jsonObject.get("Link bài viết");
                    if (this.url.equals(existingUrl)) {
                        return true;
                    }
                }
            }
        }
    } catch (IOException | ParseException e) {
        e.printStackTrace();
    }
    return false;
    }
}
