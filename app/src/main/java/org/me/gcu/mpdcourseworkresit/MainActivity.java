package org.me.gcu.mpdcourseworkresit;

//James Waller
//S1827990


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button Glasgow;
    Button London;
    Button NewYork;
    Button Oman;
    Button Mauritius;
    Button Bangladesh;



    String url1 = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579";
    String url2 = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2643743";
    String url3 = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581";
    String url4 = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/287286";
    String url5 = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/934154";
    String url6 = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/1185241";


    String result;
    String titles;
    String descriptions;
    String links;
    String pubDate;

    TextView urlInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlInput = (TextView) findViewById(R.id.urlInput);

        Glasgow = (Button) findViewById(R.id.Glasgow);
        Glasgow.setOnClickListener(this);

        London = (Button) findViewById(R.id.London);
        London.setOnClickListener(this);

        NewYork = (Button) findViewById(R.id.NewYork);
        NewYork.setOnClickListener(this);

        Oman = (Button) findViewById(R.id.Oman);
        Oman.setOnClickListener(this);

        Mauritius = (Button) findViewById(R.id.Mauritius);
        Mauritius.setOnClickListener(this);

        Bangladesh = (Button) findViewById(R.id.Bangladesh);
        Bangladesh.setOnClickListener(this);
    } // End of onCreate

    @Override
    public void onClick(View aview) {
        if (aview == Glasgow.findViewById(R.id.Glasgow)) {
            startProgress1();
        } else if (aview == London.findViewById(R.id.London)) {
            startProgress2();
        } else if (aview == NewYork.findViewById(R.id.NewYork)) {
            startProgress3();
        } else if (aview == Oman.findViewById(R.id.Oman)) {
            startProgress4();
        } else if (aview == Mauritius.findViewById(R.id.Mauritius)) {
            startProgress5();
        } else if (aview == Bangladesh.findViewById(R.id.Bangladesh)) {
            startProgress6();
        }
    }

    public void startProgress1() {
        Toast.makeText(getApplicationContext(), "Glasgow", Toast.LENGTH_LONG).show();
        new Thread(new Task(url1)).start();
    } //

    public void startProgress2() {
        Toast.makeText(getApplicationContext(), "London", Toast.LENGTH_LONG).show();
        new Thread(new Task(url2)).start();
    } //

    public void startProgress3() {
        Toast.makeText(getApplicationContext(), "New York", Toast.LENGTH_LONG).show();
        new Thread(new Task(url3)).start();
    } //

    public void startProgress4() {
        Toast.makeText(getApplicationContext(), "Oman", Toast.LENGTH_LONG).show();
        new Thread(new Task(url4)).start();
    } //

    public void startProgress5() {
        Toast.makeText(getApplicationContext(), "Mauritius", Toast.LENGTH_LONG).show();
        new Thread(new Task(url5)).start();
    } //

    public void startProgress6() {
        Toast.makeText(getApplicationContext(), "Bangladesh", Toast.LENGTH_LONG).show();
        new Thread(new Task(url6)).start();
    } //

    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    class Task implements Runnable {
        String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {

            result = "";

            try {
                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                xmlPullParserFactory.setNamespaceAware(false);
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(getInputStream(new URL(url)), null);
                xmlPullParser.next();

                boolean insideItem = false;

                int eventType = xmlPullParser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xmlPullParser.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xmlPullParser.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                titles = xmlPullParser.nextText();
                            }
                        } else if (xmlPullParser.getName().equalsIgnoreCase("description")) {
                            if (insideItem) {
                                descriptions = xmlPullParser.nextText();
                            }
                        } else if (xmlPullParser.getName().equalsIgnoreCase("link")) {
                            if (insideItem) {
                                links = xmlPullParser.nextText();
                            }
                        } else if (xmlPullParser.getName().equalsIgnoreCase("pubDate")) {
                            if (insideItem) {
                                pubDate = xmlPullParser.nextText();
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equalsIgnoreCase("item")) {

                        insideItem = false;
                    }

                    eventType = xmlPullParser.next();
                    result = titles + "\n \n" + descriptions + "\n \n" + links + "\n \n" + "Date Published: " + "\n" + pubDate + "\n \n";
                }

            } catch (IOException ae) {
                Log.e("MyTag", "ioexception");
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    urlInput.setText(result);
                }
            });
        }
    }
}