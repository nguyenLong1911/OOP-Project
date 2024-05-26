/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crawler;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Admin
 */
public class Crawler extends BaiViet{
    private String jsonFilePath;
    
    public void getDataFromUrl() {
        try {
            Document doc = Jsoup.connect(this.url).get();

            this.heading = getTitle(doc);
            this.date = getDate(doc);
            this.author = getAuthor(doc);
            this.website = getWebsite();
//            this.url = this.url;
            this.hashtags = getHashtags(doc);            
            this.summary = getSummary(doc);
            this.category = getCategory();
            this.detailedContent = getContent(doc);
            this.blogPost = getArticleType();

            JSONObject newJsonInfo = new JSONObject();
            JSONArray hashtagArray = new JSONArray();
            for (String hashtag : hashtags) {
                hashtagArray.add(hashtag);
            }            
            newJsonInfo.put("Link bài viết", this.url);
            newJsonInfo.put("Nguồn website", this.website);
            newJsonInfo.put("Loại bài viết", this.blogPost);
            newJsonInfo.put("Tóm tắt bài viết (nếu có)", this.summary);
            newJsonInfo.put("Tiêu đề bài viết", heading);
            newJsonInfo.put("Nội dung chi tiết bài viết", detailedContent);
            newJsonInfo.put("Ngày tạo", extractDate(date));
            newJsonInfo.put("Hashtag đi kèm", hashtagArray);
            newJsonInfo.put("Tên tác giả nếu có", extractAuthor(author));
            newJsonInfo.put("Chuyên mục mà bài viết thuộc về", category);

            // Đọc dữ liệu JSON hiện tại từ file
            JSONArray jsonArray = new JSONArray();
            try (FileReader reader = new FileReader("src/main/java/FileStorge/Contents.json")) {
                JSONParser jsonParser = new JSONParser();
                Object obj = jsonParser.parse(reader);
                if (obj instanceof JSONArray) {
                    jsonArray = (JSONArray) obj;
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            // Bổ sung thông tin mới vào mảng JSON
            jsonArray.add(newJsonInfo);

            // Ghi mảng JSON đã cập nhật trở lại vào file
            try (FileWriter fileWriter = new FileWriter("src/main/java/FileStorge/Contents.json")) {
                fileWriter.write(jsonArray.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getArticleType() {
        String source = this.website.toLowerCase();
        String content = this.detailedContent.toLowerCase();
        if (source.contains("x.com")) {
            return "Tweet";
        } else if (source.contains("blog")||content.contains("blog")) {
            return "Blog Post";
        } else if (source.contains("facebook.com")) {
            return "Facebook Post";
        } else {
            return "News Article";
        }
    }
    public String getTitle(Document doc) {
        Element titleElement = doc.selectFirst("h1, h2");
        return titleElement != null ? titleElement.text() : "Không có ";
    }
    public String getDate(Document doc) {
        Element lastUpdatedDiv = doc.selectFirst("div.last-updated-on");
        if (lastUpdatedDiv != null) {
            lastUpdatedDiv.select("span.view").remove();
        }
        String lastUpdatedDate = lastUpdatedDiv != null ? extractDate(lastUpdatedDiv.text()) : "";

        if (lastUpdatedDate.isEmpty()) {
            Element dateDiv = doc.selectFirst("div.writtenby_date");
            lastUpdatedDate = dateDiv != null ? extractDate(dateDiv.text()) : "";

            if (lastUpdatedDate.isEmpty()) {
                Element dateSpan = doc.selectFirst("span.date");
                lastUpdatedDate = dateSpan != null ? extractDate(dateSpan.text()) : "";
                lastUpdatedDate = lastUpdatedDate.replace("|", "").trim();
            }

            if (lastUpdatedDate.isEmpty()) {
                Element timeElement = doc.selectFirst("time.entry-date.updated.td-module-date");
                lastUpdatedDate = timeElement != null ? timeElement.text() : "";
            }

            if (lastUpdatedDate.isEmpty()) {
                Element dateElement = doc.selectFirst("div.font-barlow.text-gray-02.my-auto > div:nth-of-type(2)");
                lastUpdatedDate = dateElement != null ? extractDate(dateElement.text()) : "";
            }

            if (lastUpdatedDate.isEmpty()) {
                Element updatedDateElement = null;
                Elements paragraphs = doc.select("p[color=secondary]");
                for (Element p : paragraphs) {
                    if (p.text().contains("Updated")) {
                        updatedDateElement = p;
                        break;
                    }
                }

                if (updatedDateElement != null) {
                    lastUpdatedDate = updatedDateElement.text();
                }
            }
        }

        return lastUpdatedDate;
    }
    public String getAuthor(Document doc) {
        Element authorLink = doc.selectFirst("a[href*=/authors/]");
        String author = authorLink != null ? authorLink.text() : "";

        if (author.isEmpty()) {
            Element authorDiv = doc.selectFirst("div.ibody3-bold.author-dis-name");
            if (authorDiv != null) {
                Element authorAnchor = authorDiv.selectFirst("a");
                author = authorAnchor != null ? authorAnchor.text() : "";
            }
        }

        if (author.isEmpty()) {
            Element authorSpan = doc.selectFirst("span.written_by_name");
            author = authorSpan != null ? authorSpan.text() : "";
        }

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

        return author;
    }
    public String getWebsite() {
        try {
            URI uri = new URI(this.url);
            String domain = uri.getHost();
            if (domain != null) {
                return domain.startsWith("www.") ? domain.substring(4) : domain;
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Không xác định";
    }
    public String[] getHashtags(Document doc) {
        List<String> hashtagsList = new ArrayList<>();
        
        Element categoryUl = doc.selectFirst("ul.td-category");
        if (categoryUl != null) {
            Elements categoryList = categoryUl.select("li.entry-category");
            for (Element category : categoryList) {
                Element categoryAnchor = category.selectFirst("a");
                if (categoryAnchor != null && !categoryAnchor.text().isEmpty()) {
                    hashtagsList.add("#" + categoryAnchor.text());
                }
            }
        }

        Elements topicLinks = doc.select("a.font-barlow.breadcrumbs__link");
        for (Element topicLink : topicLinks) {
            if (!topicLink.text().isEmpty()) {
                hashtagsList.add("#" + topicLink.text());
            }
        }

        Elements tagLinks = doc.select("a[rel=tag]");
        for (Element tagLink : tagLinks) {
            if (!tagLink.text().isEmpty()) {
                hashtagsList.add("#" + tagLink.text());
            }
        }

        if (!hashtagsList.isEmpty()) {
            return hashtagsList.toArray(String[]::new);
        }

        return new String[]{"Không có"};
    }
    public String getSummary(Document doc) {
        Elements pElements = doc.select("p.sc-bbc06255-0.jvmCPb");
        StringBuilder allSpanContent = new StringBuilder();
        for (Element pElement : pElements) {
            Elements spanElements = pElement.select("span");
            for (Element spanElement : spanElements) {
                String spanContent = spanElement.text();
                allSpanContent.append(spanContent).append(" ");
            }
        }
        String summary = allSpanContent.toString().trim();
        return summary.isEmpty() ? "Không có" : summary;
    }
    public static String getContent(Document doc) {
        Element article = doc.select("article").first();
        if (article != null) {
            return extractContent(article.select("h2, p"));
        } else {
            Element div = doc.select("div.sc-9ed7d608-1.lgayrS").first();
            if (div != null) {
                return extractContent(div.select("h2, p"));
            } else {
            return "Không có";
            }
        }
    }
    public String getCategory() {
        try {
            URI uri = new URI(this.url);
            String path = uri.getPath();
            if (path != null) {
                String[] segments = path.split("/");
                // Lấy phần cuối của đường dẫn
                String lastSegment = segments[segments.length - 1];
                // Loại bỏ các từ không cần thiết và chuyển đổi thành Proper Case
                String category = toProperCase(lastSegment);
                return category.trim();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "Không xác định";
    }
    public static String extractContent(Elements elements) {
        StringBuilder contentBuilder = new StringBuilder();
        for (Element element : elements) {
        contentBuilder.append(element.text()).append("\n");
        }
        return contentBuilder.toString();
    }
    public String toProperCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        str = str.replace("-", " ");
        str = str.replace("how does", " ");
        str = str.replace("work blockchain having", " ");
        str = str.replace("what is", " ");
        str = str.replace("article", " ");
        str = str.replace("use cases for", " ");
        str = str.replace("article", " ");
        str = str.replace("examples", " ");
        str = str.replace("and its", " ");
    
        StringBuilder properCase = new StringBuilder();
        boolean capitalizeNext = true;
        for (char c : str.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                properCase.append(c);
            } else if (capitalizeNext) {
                properCase.append(Character.toTitleCase(c));
                capitalizeNext = false;
            } else {
                properCase.append(Character.toLowerCase(c));
            }
        }
        return properCase.toString();
    }
    public String extractAuthor(String originalauthor) {
        if (originalauthor == null) return null;
        return originalauthor.replaceAll("(?i)By", "").trim();
    }
    public String extractDate(String text) {
        if (text == null) return null;
        return text.replaceAll("(?i)Published on|last updated on|Updated|• 5 min read|• 3 min read|• 4 min read", "").trim();
    }
}