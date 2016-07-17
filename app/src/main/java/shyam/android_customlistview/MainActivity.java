package shyam.android_customlistview;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

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
