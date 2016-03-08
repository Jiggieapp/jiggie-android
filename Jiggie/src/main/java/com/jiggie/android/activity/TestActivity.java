package com.jiggie.android.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.jiggie.android.R;
import com.jiggie.android.component.Utils;
import com.jiggie.android.component.activity.ToolbarActivity;

import java.util.Map;

/**
 * Created by LTE on 2/1/2016.
 */
public class TestActivity extends ToolbarActivity{
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testflyer);
        txt = (TextView)findViewById(R.id.txt);
        AppsFlyerLib.sendTracking(super.getApplicationContext());

        registerAppsFlyerConversion();


        //String data = "[{Jiggie=setiady:cui cui cui cui BARUU, fromId=10153311635578981, toId=10205867667234984, collapse_key=do_not_collapse}]";

        /*Bundle bundle = new Bundle();
        bundle.putString("Jiggie","setiady:cui cui cui cui BARUU");
        bundle.putString("fromId","10153311635578981");
        bundle.putString("toId","10205867667234984");
        bundle.putString("collapse_key","do_not_collapse");
        String name = "Jiggie";
        String message = "";
        boolean chat = false;

        String fromId = "";
        String toId = "";

        for (String key : bundle.keySet()) {
            if (key.equalsIgnoreCase("Jiggie")) {
                final String content = bundle.getString(key);
                if (content != null) {
                    final String[] values = content.split(":");
                    message = values.length > 1 ? values[1].trim() : content;
                    name = values.length > 1 ? values[0].trim() : getString(R.string.app_name);
                    chat = values.length > 1;

                    String s = "";
                }
            }
        }

        for (String key : bundle.keySet()) {
            if (key.equalsIgnoreCase("fromId")) {
                final String content = bundle.getString(key);
                fromId = content;
            }
        }

        for (String key : bundle.keySet()) {
            if (key.equalsIgnoreCase("toId")) {
                final String content = bundle.getString(key);
                toId = content;
            }
        }*/
    }

    private void registerAppsFlyerConversion(){
        AppsFlyerLib.registerConversionListener(super.getApplicationContext(), new AppsFlyerConversionListener() {
            @Override
            public void onInstallConversionDataLoaded(Map<String, String> map) {
                String media_source = map.get("media_source") == null ? "null" : map.get("media_source");
                String campaign = map.get("campaign") == null ? "null" : map.get("campaign");
                String af_status = map.get("af_status") == null ? "null" : map.get("af_status");
                if (!media_source.equals("null"))
                    Utils.AFmedia_source = media_source;
                if (!campaign.equals("null"))
                    Utils.AFcampaign = campaign;
                if (!af_status.equals("null"))
                    Utils.AFinstall_type = af_status;

                /*String d = map.toString();
                String a = Utils.AFmedia_source+" "+Utils.AFcampaign+" "+Utils.AFinstall_type;
                Toast.makeText(MainActivity.this, a, Toast.LENGTH_LONG).show();*/

                final String s = map.toString();

                final String appsfl = "media_source = "+Utils.AFmedia_source + ", campaign = " + Utils.AFcampaign + ", install_type = " + Utils.AFinstall_type + "**" + media_source + " " + campaign + " " + af_status;
                //Toast.makeText(MainActivity.this, appsfl, Toast.LENGTH_LONG).show();
                Log.d("123appsflyer", appsfl);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txt.setText(s);
                    }
                });


                /*AlertDialog al = new AlertDialog.Builder(MainActivity.this).setMessage(a).create();
                al.show();*/


            }

            @Override
            public void onInstallConversionFailure(String s) {
                //Toast.makeText(MainActivity.this, "a", Toast.LENGTH_LONG).show();
                Log.d("123appsflyer", "a");
                txt.setText("a");
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {
                //Toast.makeText(MainActivity.this, "b", Toast.LENGTH_LONG).show();
                Log.d("123appsflyer", "b");
                final String s = map.toString();
            }

            @Override
            public void onAttributionFailure(String s) {
                //Toast.makeText(MainActivity.this, "c", Toast.LENGTH_LONG).show();
                Log.d("123appsflyer", "c");
                txt.setText("c");
            }
        });
    }
}
