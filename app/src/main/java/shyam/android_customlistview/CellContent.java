package shyam.android_customlistview;

/**
 * Created by shyamsundardorairaj on 17/07/16.
 */

public class CellContent {

    private String title;
    private String description;
    private String iconUrl;
    private String actualUrl;

    public CellContent(String title, String description, String iconUrl, String actualUrl) {
        this.title = title;
        this.description = description;
        this.iconUrl = iconUrl;
        this.actualUrl = actualUrl;
    }



    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getActualUrl() {
        return actualUrl;
    }


}
