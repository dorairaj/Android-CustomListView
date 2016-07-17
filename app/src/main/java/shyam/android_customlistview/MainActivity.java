package shyam.android_customlistview;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private List<CellContent> contentList= new ArrayList<>(); // List to store collection of contents to be displayed
    ArrayAdapter <CellContent> adapter; // custom adapter for contentListView
    Toast toast; // Toast object to provide feedback to user.
    String refreshToastText ="Please Wait! Refreshing..";
    String errorToastText ="Something Went Wrong,Please try again.";
    String loadToastText = "Loading..Please wait..";
    String imageToastText = "Unable to load some images.";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getCellContent(); // Get Json content from file and store in collection.
        fillContentOnView(); // populate listview.
        contentClickListener(); // listview click Listener.
        refreshViewListener();  // pull down to refresh event listener.
    }


    /**
     * getCellContent() gets Json File from URL, Parses the JSON File and creates objects
     * of CellContent, which is later used to populate contentListView.
     */
    private void getCellContent()
    {
        contentList.clear();
        try
        {
            JSONObject jsonFile = new GetJson().execute("https://api.myjson.com/bins/1quht").get();
            toast=Toast.makeText(getApplicationContext(),loadToastText,Toast.LENGTH_SHORT);
            toast.show();
            JSONArray jsonCellContents=jsonFile.getJSONArray("solutions");
            System.out.println(jsonCellContents.toString());
            for (int i=0; i<jsonCellContents.length();i++)

            {
                // Parsing all items of Json array.
                JSONObject subJson = (JSONObject) jsonCellContents.get(i);

                String title,description,iconUrl,actualUrl;

                title=subJson.getString("name");
                description=subJson.getString("description");
                iconUrl=subJson.getString("icon");
                actualUrl=subJson.getString("url");

                contentList.add(new CellContent(title,description,iconUrl,actualUrl));

            }

        }
        catch (Exception e)
        {
            toast=Toast.makeText(getApplicationContext(),errorToastText,Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    /**
     * fillContentView() sets the adapter for contentListView, so that contentListView
     * can be populated
     */
    private void fillContentOnView()
    {
        adapter= new contentListViewAdapter();
        ListView listView=(ListView) findViewById(R.id.contentListView);
        listView.setAdapter(adapter);
    }

    /**
     *  contentClickListener() listens to onClick events on contentListView,
     *  In event of a click, It calls an Intent which provides a webview to see full-blown content,
     *  related to the cell.
     */
    private void contentClickListener()
    {
        ListView listview = (ListView) findViewById(R.id.contentListView);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CellContent clickedContent= contentList.get(position);
                try
                {
                    Intent intent=new Intent(MainActivity.this,WebActivity.class);
                    intent.putExtra("url",clickedContent.getActualUrl());
                    toast=Toast.makeText(getApplicationContext(),loadToastText,Toast.LENGTH_SHORT);
                    toast.show();
                    startActivity(intent);
                }
                catch(Exception e)
                {
                    toast=Toast.makeText(getApplicationContext(),errorToastText,Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
    }


    /**
     * refreshViewListener() provides pull to refresh feature.
     * On detecting pull-down gesture, It updates the list view with latest content.
     */

    private void refreshViewListener()
    {
        final SwipeRefreshLayout refreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCellContent();
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }});
    }


    /**
     * The following classes provide us with a custom adapter which is used to populate
     * contents on the listview.
     */

    /**
     *  To load images asynchronously "picasso" library is used.
     *  (https://github.com/square/picasso)
     *  This library provides a simple one line code for loading images asynchronously and
     *  is generally well-known for its efficiency and performance.
     *  It is well suited for this project as it provides a simple implementation and does
     *  the job effectively.
     *
     */

    private class contentListViewAdapter extends ArrayAdapter <CellContent>
    {
        public contentListViewAdapter()
        {
            super(MainActivity.this,R.layout.row_holder,contentList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder;
            final float scale = getResources().getDisplayMetrics().density;
            int iconDimension  = (int) (100 * scale);
            if (convertView==null)
            {
                convertView= getLayoutInflater().inflate(R.layout.row_holder,null);
                holder=new ViewHolder();
                holder.title= (TextView) convertView.findViewById(R.id.titleView);
                holder.description= (TextView) convertView.findViewById(R.id.descriptionView);
                holder.icon=(ImageView) convertView.findViewById(R.id.iconView);
                convertView.setTag(holder);
            }
            else
            {
                holder=(ViewHolder) convertView.getTag();
            }
            CellContent currentContent=contentList.get(position);
            RelativeLayout.LayoutParams lparams= new RelativeLayout.LayoutParams(0,0);
            String url =currentContent.getIconUrl().trim();

            if (url.isEmpty()==false)
            {
                lparams.height=iconDimension;
                lparams.width=iconDimension;
                // To load images asynchronously picasso library from github is used
                try
                {
                    Picasso.with(getContext()).load(url).resize(100, 100).into(holder.icon);
                }
                catch (Exception e)
                {
                    // Some cells may be malformed if this exception occurs (broken URL)
                    toast=Toast.makeText(getApplicationContext(),imageToastText,Toast.LENGTH_SHORT);
                    toast.show();
                }

            }

            holder.icon.setLayoutParams(lparams);
            holder.title.setText(currentContent.getTitle());
            holder.description.setText(currentContent.getDescription());

            return convertView;
        }



    }

    /**
     * extension to our custom adapter class above
     * It is implemented to improve peerformance by recycling contents of List View.
     */
    static class ViewHolder
    {
        TextView title;
        TextView description;
        ImageView icon;

    }


    /**
     * The GetJson class below reads a Json file from a given Url and returns a Json Object.
     * The class is implemnted in a seperate thread as network operations cannot be performed
     * on main thread.
     * The class and its methods are used while calling,
     *    JSONObject jsonFile = new GetJson().execute("https://api.myjson.com/bins/1quht").get();
     *              in getCellContent()
     *
     */


    private class GetJson extends AsyncTask<String, Integer, JSONObject>
    {
        @Override
        protected JSONObject doInBackground(String... params)
        {
            JSONObject json=null;
            try
            {
                json = readUrl(params[0]);
            }
            catch (Exception e)
            {
                toast=Toast.makeText(getApplicationContext(),errorToastText,Toast.LENGTH_SHORT);
                toast.show();
            }
            finally
            {
                return json;
            }
        }
        private JSONObject readUrl(String url) throws Exception
        {
            InputStream inputStream = new URL(url).openStream();
            JSONObject json=null;
            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                String jsonText = readAll(reader);
                json = new JSONObject(jsonText);
                return json;
            }
            catch (Exception e)
            {
                toast=Toast.makeText(getApplicationContext(),errorToastText,Toast.LENGTH_SHORT);
                toast.show();
            }
            finally
            {
                inputStream.close();
                return json;
            }
        }
        private  String readAll(Reader rd) throws IOException
        {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1)
            {
                sb.append((char) cp);
            }
            return sb.toString();
        }
    }



}
