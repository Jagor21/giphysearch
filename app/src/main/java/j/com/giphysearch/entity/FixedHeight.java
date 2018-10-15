package j.com.giphysearch.entity;


/*
 * This class using
 * to access gif urls
 */
public class FixedHeight {
    private String url;
    private String width;
    private String height;

    public FixedHeight() {
    }

    public FixedHeight(String url, String width, String height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
