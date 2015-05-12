package hu.markcool.getpostdemo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")

public class MainActivity extends Activity {

    private TextView txtResult = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        txtResult = (TextView) findViewById(R.id.txtResult);

        // http get method 1
        makeGetRequest();


        // http get method 2
        initView();

    }

    private void makeGetRequest() {


        String resultString = null;

        String httpLink = "http://google.com";

        System.out.println("httpLink :" + httpLink);

        // Create a new HttpClient
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(httpLink);
        HttpResponse response;

        try {

            // Execute HTTP Post Request
            response = httpClient.execute(httpGet);

            int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("responseCode :" + responseCode);


            if (responseCode == 200) {
                // get response string, JSON format data
                resultString = EntityUtils.toString(response.getEntity());


            } else {
                resultString = null;

                System.out.println("response not ok, status :" + responseCode);

            }


        } catch (Exception e) {

            System.out.println("HttpPost Exception : " + e.toString());

        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        System.out.println("resultString :" + resultString);

        if(resultString.length() > 150) {
            resultString = "Http get method 1 : Google Data -> " + resultString.substring(0, 149);
        }

        resultString = "Http get method 1 : Google Data -> " + resultString;

        txtResult.setText(resultString);

    }

    private void loadPage() {

        Thread t1 = new Thread(r1);
        t1.start();

    }


    private void initView() {

        Button btnLoad = (Button) findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadPage();
            }

        });
    }


    private Runnable r1 = new Runnable() {

        public void run() {

            String html = getHtmlByGet("http://yahoo.com");

            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", html);
            msg.setData(data);
            handler.sendMessage(msg);

        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle data = msg.getData();
            String html = data.getString("value");

            if(html.length() > 150) {
                html = html.substring(0, 149);
            }

            html = "Http get method 1 : Yhaoo Data -> " + html;

            txtResult.setText(html);
        }
    };


    public String getHtmlByGet(String _url) {

        String result = "";

        HttpClient client = new DefaultHttpClient();

        try {

            HttpGet get = new HttpGet(_url);


            HttpResponse response = client.execute(get);

            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                result = EntityUtils.toString(resEntity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }


        return result;

    }


    public String getHtmlByPost(String _url, String _queryKey, String _queryValue) {

        String result = "";

        HttpClient client = new DefaultHttpClient();

        try {


            HttpPost post = new HttpPost(_url);

            // 參數
            if (_queryKey != "") {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(_queryKey, _queryValue));
                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);
            }

            HttpResponse responsePOST = client.execute(post);

            HttpEntity resEntity = responsePOST.getEntity();

            if (resEntity != null) {
                result = EntityUtils.toString(resEntity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }


        return result;
    }


}