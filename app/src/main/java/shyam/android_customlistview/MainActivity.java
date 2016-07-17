package shyam.android_customlistview;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    private List<CellContent> contentList= new ArrayList<>();
    ArrayAdapter <CellContent> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getCellContent();
        fillContentOnView();
    }



    private void getCellContent()
    {
        contentList.clear();
        try
        {
            JSONObject jsonFile = new GetJson().execute("https://api.myjson.com/bins/1quht").get();
            Log.i("JsonTest",jsonFile.toString());
            JSONArray jsonCellContents=jsonFile.getJSONArray("solutions");
            System.out.println(jsonCellContents.toString());
            for (int i=0; i<jsonCellContents.length();i++)

            {
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
            e.printStackTrace();
        }

    }

    private void fillContentOnView()
    {
        adapter= new contentListViewAdapter();
        ListView listView=(ListView) findViewById(R.id.contentListView);
        listView.setAdapter(adapter);
    }



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

                //Todo Load Image here

            }

            holder.icon.setLayoutParams(lparams);
            holder.title.setText(currentContent.getTitle());
            holder.description.setText(currentContent.getDescription());

            return convertView;
        }



    }

    static class ViewHolder
    {
        TextView title;
        TextView description;
        ImageView icon;

    }









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
                e.printStackTrace();
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
                e.printStackTrace();
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
