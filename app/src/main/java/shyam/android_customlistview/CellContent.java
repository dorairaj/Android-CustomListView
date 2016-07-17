package shyam.android_customlistview;

/**
 * Created by shyamsundardorairaj on 17/07/16.
 * The class CellContent is used to store the content parsed from JsonFile,
 * The objects of this class are later used to populate contentListView.
 *
 * Variables :
 * title - used to store name of  json items.
 * description - used to store description of json items.
 * iconUrl - contains url from which icon can be retrieved.
 * actualUrl - contains url for displaying full-blown content
 *
 * Methods :
 * getters for title,description,iconUrl,actualUrl
 */

public class CellContent
{

    private String title;
    private String description;
    private String iconUrl;
    private String actualUrl;

    public CellContent(String title, String description, String iconUrl, String actualUrl)
    {
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
