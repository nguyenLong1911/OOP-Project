/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.jsoup.nodes.Element;
import org.json.simple.JSONObject;
import java.io.FileWriter;


/**
 *
 * @author HP
 */
public class NewClass {
         public static void main(String[] args) {
             
        // URL của trang web cần lấy thông tin
        String url = "https://builtin.com/articles/utility-nft";

        try {
            // Parse HTML từ URL sử dụng Jsoup
            Document doc = Jsoup.connect(url).get();

            // Lấy tiêu đề từ cả thẻ h1 và h2
            Element titleElement = doc.selectFirst("h1, h2");
            String title = titleElement != null ? titleElement.text() : "Không có tiêu đề!";

            // Lấy ngày tạo từ thẻ div có class là "last-updated-on"
               Element lastUpdatedDiv = doc.selectFirst("div.last-updated-on");
               if (lastUpdatedDiv != null) {
             // Loại bỏ các phần tử con có class là "view"
               lastUpdatedDiv.select("span.view").remove();
                  }
               String lastUpdatedDate = lastUpdatedDiv != null ? extractDate(lastUpdatedDiv.text()) : "";

             // Nếu không tìm thấy ngày tạo từ thẻ div.last-updated-on, tiếp tục tìm ở các nơi khác
               if (lastUpdatedDate.isEmpty()) {
             // Lấy ngày tạo từ thẻ div có class là "writtenby_date"
               Element dateDiv = doc.selectFirst("div.writtenby_date");
               lastUpdatedDate = dateDiv != null ? extractDate(dateDiv.text()) : "";

               // Kiểm tra nếu không có ngày tạo thì lấy từ thẻ span có class là "date"
               if (lastUpdatedDate.isEmpty()) {
                   Element dateSpan = doc.selectFirst("span.date");
                   lastUpdatedDate = dateSpan != null ? extractDate(dateSpan.text()) : "";
                // Loại bỏ dấu "|"
                   lastUpdatedDate = lastUpdatedDate.replace("|", "").trim();
                 }
                 // Nếu vẫn không tìm thấy ngày tạo
               if (lastUpdatedDate.isEmpty()) {
                 // Lấy phần tử  chứa thông tin về ngày tạo
                   Element timeElement = doc.selectFirst("time.entry-date.updated.td-module-date");
                   lastUpdatedDate = timeElement != null ? timeElement.text() : "";
                 }
                
               // Nếu vẫn không tìm thấy ngày tạo
               if (lastUpdatedDate.isEmpty()) {
                 // Lấy phần tử  chứa thông tin về ngày tạo
                   Element dateElement = doc.selectFirst("div.font-barlow.text-gray-02.my-auto > div:nth-of-type(2)");
                   lastUpdatedDate = dateElement != null ? extractDate(dateElement.text()) : "";
                 }
                             
                // Nếu vẫn không tìm thấy ngày tạo
               if (lastUpdatedDate.isEmpty()) {
                 // Lấy phần tử div chứa thông tin về ngày tạo

                Element updatedDateElement = null;
                Elements paragraphs = doc.select("p[color=secondary]");
                for (Element p : paragraphs) {
                      if (p.text().contains("Updated")) {
                         updatedDateElement = p;
                         break;
                    }
                     }

                // Kiểm tra nếu tìm thấy thẻ chứa thông tin về ngày tạo
                  if (updatedDateElement != null) {
                  lastUpdatedDate = updatedDateElement.text();            
                }
                }
               }
               
           // Lấy tác giả
           Element authorLink = doc.selectFirst("a[href*=/authors/]");
           String author = authorLink != null ? authorLink.text() : "";

          // Kiểm tra nếu không tìm thấy tên tác giả 
       if (author.isEmpty()) {
         Element authorDiv = doc.selectFirst("div.ibody3-bold.author-dis-name");
       if (authorDiv != null) {
        Element authorAnchor = authorDiv.selectFirst("a");
        author = authorAnchor != null ? authorAnchor.text() : "";
           }
         }

          // Kiểm tra nếu không tìm thấy tên tác giả 
        if (author.isEmpty()) {
         Element authorSpan = doc.selectFirst("span.written_by_name");
         author = authorSpan != null ? authorSpan.text() : "";
           }

          // Kiểm tra nếu không tìm thấy tên tác giả 
          if (author.isEmpty()) {        
         Element authorElement = null;
         Elements paragraphs = doc.select("p[color=secondary]");
          for (Element p : paragraphs) {
          if (p.text().contains("By")) {
            authorElement = p;
            break;
          }
        }   
        if (authorElement != null) {
         author = authorElement.text();            
              }
        }


            // Lấy nguồn website từ URL
            String source = getSourceFromUrl(url);

            // Lấy liên kết bài viết
            String articleLink = url;

            //Lấy hashtag
           Element categoryUl = doc.selectFirst("ul.td-category");
           StringBuilder hashtagsBuilder = new StringBuilder(); // Sử dụng StringBuilder để tối ưu hiệu suất
boolean foundHashtags = false;

if (categoryUl != null) {
    Elements categoryList = categoryUl.select("li.entry-category");
    for (Element category : categoryList) {
        Element categoryAnchor = category.selectFirst("a");
        if (categoryAnchor != null && !categoryAnchor.text().isEmpty()) { 
            hashtagsBuilder.append("#").append(categoryAnchor.text()).append(", "); 
            foundHashtags = true;
        }
    }
}

// Nếu không tìm thấy trong thẻ ul.td-category, tìm trong các thẻ div.field__item
if (!foundHashtags) {
    Elements fieldItems = doc.select("div.field__item");
    for (Element fieldItem : fieldItems) {
    Element aTag = fieldItem.selectFirst("a[hreflang=en]");
    if (aTag != null) {
        String hashtag = aTag.text().trim();
        if (!hashtag.isEmpty()) {
            hashtagsBuilder.append("#").append(hashtag).append(", ");
            foundHashtags = true;
        }
    }
}
}

String hashtags;
if (foundHashtags) {
    hashtags = hashtagsBuilder.toString().trim();
    if (hashtags.endsWith(", ")) { // Kiểm tra xem chuỗi kết thúc bằng dấu phẩy và khoảng trắng
        hashtags = hashtags.substring(0, hashtags.length() - 2); // Loại bỏ dấu phẩy và khoảng trắng cuối cùng
    }
} else {
    hashtags = "Không có";
}


          
// Biến để lưu trữ nội dung của các thẻ <span>
String allSpanContent = "";

// Tìm và ghép nội dung của các thẻ <span> trong các thẻ <p> có class là "sc-bbc06255-0 jvmCPb"
Elements pElements = doc.select("p.sc-bbc06255-0.jvmCPb");
for (Element pElement : pElements) {
    Elements spanElements = pElement.select("span");
    for (Element spanElement : spanElements) {
        // Lấy nội dung của thẻ <span>
        String spanContent = spanElement.text();
        // Thêm nội dung của thẻ <span> vào biến allSpanContent và thêm dấu cách giữa mỗi nội dung
        allSpanContent += spanContent + " ";
    }
}

// Kiểm tra nếu allSpanContent rỗng thì gán giá trị "Không có"
if (allSpanContent.isEmpty()) {
    allSpanContent = "Không có";
}

        // Lấy chuyên mục từ URL
            String category = getCategoryFromUrl(url);

        
 
          // Lấy nội dung
Element article = doc.select("article").first();
String content;
if (article != null) {
    // Lấy ra các thẻ <h2> và các thẻ <p> con của <article> và lưu vào biến content
    content = extractContent(article.select("h2, p"));
} else {
    // Nếu không tìm thấy thẻ <article>, thử tìm trong thẻ <div> cụ thể
    Element div = doc.select("div.sc-9ed7d608-1.lgayrS").first();
    // Lấy ra các thẻ <h2> và các thẻ <p> con của <div> và lưu vào biến content
    content = extractContent(div.select("h2, p"));
}
          // In ra 
            System.out.println("Nội dung: " + content);
            System.out.println("Chuyên mục: " + category);
            System.out.println("Tóm tắt bài viết: " + allSpanContent);
            System.out.println("Hashtags: " + hashtags);
            System.out.println("Tiêu đề: " + title);
            System.out.println("Ngày tạo: " + extractDate(lastUpdatedDate));
            System.out.println("Tác giả: " + extractauthor(author));
            System.out.println("Nguồn website: " + source);
            System.out.println("Link bài viết: " + articleLink);

            
           // Tạo một đối tượng JSONObject để lưu thông tin
            JSONObject jsonInfo = new JSONObject();

            // Thêm thông tin vào đối tượng JSONObject
            jsonInfo.put("Link bài viết", articleLink);
            jsonInfo.put("Nguồn website", source);
            jsonInfo.put("Tóm tắt bài viết", allSpanContent);
            jsonInfo.put("Tiêu đề", title);
            jsonInfo.put("Nội dung", content);
            jsonInfo.put("Ngày tạo", extractDate(lastUpdatedDate));
            jsonInfo.put("Hashtags", hashtags);
            jsonInfo.put("Tác giả", extractauthor(author));
            jsonInfo.put("Chuyên mục", category);

            // Chuyển đối tượng JSONObject thành chuỗi JSON
            String jsonString = jsonInfo.toJSONString();

            // Ghi chuỗi JSON vào một tệp
            FileWriter fileWriter = new FileWriter("output.json");
            fileWriter.write(jsonString);
            fileWriter.close();
          
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
                
           
         
         
         // Phương thức để lấy chuyên mục từ URL và viết hoa chữ cái đầu của mỗi từ
    private static String getCategoryFromUrl(String url) {
        String[] parts = url.split("/");
        if (parts.length >= 2) {
            String category = parts[parts.length - 1].replace("-", " ");
            // Chia chuỗi thành các từ riêng biệt
            String[] words = category.split("\\s+");
            // Viết hoa chữ cái đầu của mỗi từ
            StringBuilder sb = new StringBuilder();
            for (String word : words) {
                sb.append(word.substring(0, 1).toUpperCase() + word.substring(1)).append(" ");
            }
            return sb.toString().trim();
        }
        return "Không xác định";
    }

         
    // Phương thức để lấy nguồn website từ URL
    private static String getSourceFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            if (domain != null) {
                return domain.startsWith("www.") ? domain.substring(4) : domain;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "Không xác định";
    }
    
    // Phương thức để trích xuất và kết hợp nội dung từ các phần tử
private static String extractContent(Elements elements) {
    StringBuilder contentBuilder = new StringBuilder();
    for (Element element : elements) {
        contentBuilder.append(element.text()).append("\n");
    }
    return contentBuilder.toString();
}
    
  // Phương thức để loại bỏ phần "Published on" và "Last updated on" và chỉ lấy ngày tạo
private static String extractDate(String originalDate) {
    return originalDate.replaceAll("(?i)Published on|last updated on|Updated|• 5 min read|• 3 min read|• 4 min read", "").trim();
}  
private static String extractauthor(String originalauthor) {
    return originalauthor.replaceAll("(?i)By", "").trim();
}
    
    }

