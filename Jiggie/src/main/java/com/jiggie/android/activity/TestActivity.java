package com.jiggie.android.activity;

import android.os.Bundle;

import com.jiggie.android.R;
import com.jiggie.android.component.activity.ToolbarActivity;

/**
 * Created by LTE on 2/1/2016.
 */
public class TestActivity extends ToolbarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //String data = "[{Jiggie=setiady:cui cui cui cui BARUU, fromId=10153311635578981, toId=10205867667234984, collapse_key=do_not_collapse}]";

        Bundle bundle = new Bundle();
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
        }
    }
}
