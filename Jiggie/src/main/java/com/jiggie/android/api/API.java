package com.jiggie.android.api;

import com.android.volley.VolleyError;
import com.jiggie.android.component.volley.VolleyHandler;
import com.jiggie.android.component.volley.VolleyRequestListener;

import org.json.JSONArray;

/**
 * Created by wandywijayanto on 2/1/16.
 */
public class API {
    private static VolleyRequestListener<String[], JSONArray> volleyRequestListener;
    public static OnResponseCompleted onResponseCompleted;

    public interface OnResponseCompleted
    {
        public void onResponse(String[] values);
    };

    public static void loadUserTagList(OnResponseCompleted listener)
    {
        onResponseCompleted = listener;
        VolleyHandler.getInstance().createVolleyArrayRequest("user/tagslist", new VolleyRequestListener<String[], JSONArray>() {
            @Override
            public String[] onResponseAsync(JSONArray jsonArray) {
                final int length = jsonArray.length();
                final String[] values = new String[length];
                for (int i = 0; i < length; i++)
                    values[i] = jsonArray.optString(i);
                return values;
            }

            @Override
            public void onResponseCompleted(String[] values) {
                onResponseCompleted.onResponse(values);
                /*if (isActive()) {
                    final LayoutInflater inflater = getLayoutInflater();
                    final int length = values.length;
                    selectedItems.clear();

                    for (int i = 0; i < length; i++) {
                        final View view = inflater.inflate(R.layout.item_setup_tag, flowLayout, false);
                        final ViewHolder holder = new ViewHolder(SetupTagsActivity.this, view, values[i]);

                        holder.textView.setText(holder.text);
                        selectedItems.add(holder.text);
                        flowLayout.addView(view);

                        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                holder.container.setMinimumWidth(holder.container.getMeasuredWidth());
                            }
                        });
                    }

                    progressBar.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                }*/
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                /*if (isActive()) {
                    Toast.makeText(SetupTagsActivity.this, App.getErrorMessage(error), Toast.LENGTH_SHORT).show();
                    failedView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }*/
            }
        });
    }
}
