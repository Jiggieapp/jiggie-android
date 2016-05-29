package com.jiggie.android.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.jiggie.android.App;
import com.jiggie.android.R;

import java.util.Calendar;

import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * Created by LTE on 5/4/2016.
 */
public class TooltipsManager {

    public static Tooltip.TooltipView tooltip;
    public static boolean CAN_SHOW_TOOLTIP_EVENT_LIST = false;
    public static boolean CAN_SHOW_TOOLTIP_LIKE = false;
    public static boolean CAN_SHOW_TOOLTIP_SOCIAL_TAB = false;
    public static boolean CAN_SHOW_TOOLTIP_SHARE = false;
    public static boolean CAN_SHOW_YES_SUGGESTED = false;
    public static boolean CAN_SHOW_YES_INBOUND = false;
    public static final String latestTimeTooltip = "time_tooltip";
    public static final String TOOLTIP_EVENT_LIST = "tooltip_event_list";
    public static final String TOOLTIP_LIKE = "tooltip_like";
    public static final String TOOLTIP_SOCIAL_TAB = "tooltip_social_tab";
    public static final String TOOLTIP_SHARE = "tooltip_share";
    public static final String TOOLTIP_YES_SUGGESTED = "tooltip_yes_suggested";
    public static final String TOOLTIP_YES_INBOUND = "tooltip_yes_inbound";
    public static boolean ALREADY_SHOW_TOOLTIP_EVENT_LIST_TODAY = false;
    public static boolean ALREADY_SHOW_TOOLTIP_LIKE_TODAY = false;
    public static boolean ALREADY_SHOW_TOOLTIP_SOCIAL_TAB_TODAY = false;
    public static boolean ALREADY_SHOW_TOOLTIP_SHARE_TODAY = false;
    public static boolean ALREADY_SHOW_YES_SUGGESTED_TODAY = false;
    public static boolean ALREADY_SHOW_YES_INBOUND_TODAY = false;
    public static final String ALREADY_TOOLTIP_EVENT_LIST = "tooltip_event_list";
    public static final String ALREADY_TOOLTIP_LIKE = "tooltip_like";
    public static final String ALREADY_TOOLTIP_SOCIAL_TAB = "tooltip_social_tab";
    public static final String ALREADY_TOOLTIP_SHARE = "tooltip_share";
    public static final String ALREADY_TOOLTIP_YES_SUGGESTED = "tooltip_yes_suggested";
    public static final String ALREADY_TOOLTIP_YES_INBOUND = "tooltip_yes_inbound";

    public static void validateTime(long longTimeInMilis){
        long longTimeInMilisSaved = App.getSharedPreferences().getLong(latestTimeTooltip, 0);
        if(longTimeInMilisSaved==0){
            App.getSharedPreferences().edit().putLong(latestTimeTooltip, longTimeInMilis).commit();

            setDefaultTooltipsCondition();
        }else{
            Calendar calSaved = Calendar.getInstance();
            calSaved.setTimeInMillis(longTimeInMilisSaved);

            Calendar calNow = Calendar.getInstance();
            calNow.setTimeInMillis(longTimeInMilis);

            int daySaved = calSaved.get(Calendar.DAY_OF_MONTH);
            int dayNow = calNow.get(Calendar.DAY_OF_MONTH);
            int dayGap = dayNow-daySaved;

            CAN_SHOW_TOOLTIP_EVENT_LIST = App.getSharedPreferences().getBoolean(TOOLTIP_EVENT_LIST, false);
            CAN_SHOW_TOOLTIP_LIKE = App.getSharedPreferences().getBoolean(TOOLTIP_LIKE, false);
            CAN_SHOW_TOOLTIP_SOCIAL_TAB = App.getSharedPreferences().getBoolean(TOOLTIP_SOCIAL_TAB, false);
            CAN_SHOW_TOOLTIP_SHARE = App.getSharedPreferences().getBoolean(TOOLTIP_SHARE, false);
            CAN_SHOW_YES_SUGGESTED = App.getSharedPreferences().getBoolean(TOOLTIP_YES_SUGGESTED, false);
            CAN_SHOW_YES_INBOUND = App.getSharedPreferences().getBoolean(TOOLTIP_YES_INBOUND, false);

            if(dayGap>0){
                ALREADY_SHOW_TOOLTIP_EVENT_LIST_TODAY = false;
                ALREADY_SHOW_TOOLTIP_LIKE_TODAY = false;
                ALREADY_SHOW_TOOLTIP_SOCIAL_TAB_TODAY = false;
                ALREADY_SHOW_TOOLTIP_SHARE_TODAY = false;
                ALREADY_SHOW_YES_SUGGESTED_TODAY = false;
                ALREADY_SHOW_YES_INBOUND_TODAY = false;

                App.getSharedPreferences().edit().putBoolean(ALREADY_TOOLTIP_EVENT_LIST, ALREADY_SHOW_TOOLTIP_EVENT_LIST_TODAY).putBoolean(ALREADY_TOOLTIP_LIKE, ALREADY_SHOW_TOOLTIP_LIKE_TODAY).putBoolean(ALREADY_TOOLTIP_SOCIAL_TAB, ALREADY_SHOW_TOOLTIP_SOCIAL_TAB_TODAY)
                        .putBoolean(ALREADY_TOOLTIP_SHARE, ALREADY_SHOW_TOOLTIP_SHARE_TODAY).putBoolean(ALREADY_TOOLTIP_YES_SUGGESTED, ALREADY_SHOW_YES_SUGGESTED_TODAY).putBoolean(ALREADY_TOOLTIP_YES_INBOUND, ALREADY_SHOW_YES_INBOUND_TODAY).commit();
            }else{
                ALREADY_SHOW_TOOLTIP_EVENT_LIST_TODAY = App.getSharedPreferences().getBoolean(ALREADY_TOOLTIP_EVENT_LIST, false);
                ALREADY_SHOW_TOOLTIP_LIKE_TODAY = App.getSharedPreferences().getBoolean(ALREADY_TOOLTIP_LIKE, false);
                ALREADY_SHOW_TOOLTIP_SOCIAL_TAB_TODAY = App.getSharedPreferences().getBoolean(ALREADY_TOOLTIP_SOCIAL_TAB, false);
                ALREADY_SHOW_TOOLTIP_SHARE_TODAY = App.getSharedPreferences().getBoolean(ALREADY_TOOLTIP_SHARE, false);
                ALREADY_SHOW_YES_SUGGESTED_TODAY = App.getSharedPreferences().getBoolean(ALREADY_TOOLTIP_YES_SUGGESTED, false);
                ALREADY_SHOW_YES_INBOUND_TODAY = App.getSharedPreferences().getBoolean(ALREADY_TOOLTIP_YES_INBOUND, false);
            }
        }
    }

    public static void clearTimeTooltip(){
        App.getSharedPreferences().edit().putLong(latestTimeTooltip, 0).commit();
    }

    public static boolean canShowTooltipAt(String tootipAt){
        boolean can = false;

        if(tootipAt.equals(TOOLTIP_EVENT_LIST)){
            if(CAN_SHOW_TOOLTIP_EVENT_LIST){
                if (ALREADY_SHOW_TOOLTIP_EVENT_LIST_TODAY){
                    can = false;
                }else{
                    can = true;
                }
            }else{
                can = false;
            }
        }else if(tootipAt.equals(TOOLTIP_LIKE)){
            if(CAN_SHOW_TOOLTIP_EVENT_LIST){
                can = false;
            }else{
                if(CAN_SHOW_TOOLTIP_LIKE){
                    if (ALREADY_SHOW_TOOLTIP_LIKE_TODAY){
                        can = false;
                    }else{
                        can = true;
                    }
                }else{
                    can = false;
                }
            }
        }else if(tootipAt.equals(TOOLTIP_SOCIAL_TAB)){
            if(CAN_SHOW_TOOLTIP_EVENT_LIST){
                can = false;
            }else if(CAN_SHOW_TOOLTIP_LIKE){
                can = false;
            }else{
                if(CAN_SHOW_TOOLTIP_SOCIAL_TAB){
                    if (ALREADY_SHOW_TOOLTIP_SOCIAL_TAB_TODAY){
                        can = false;
                    }else{
                        can = true;
                    }
                }else{
                    can = false;
                }
            }
        }else if(tootipAt.equals(TOOLTIP_SHARE)){
            if(CAN_SHOW_TOOLTIP_EVENT_LIST){
                can = false;
            }else if(CAN_SHOW_TOOLTIP_LIKE){
                can = false;
            }else if(CAN_SHOW_TOOLTIP_SOCIAL_TAB){
                can = false;
            }else{
                if(CAN_SHOW_TOOLTIP_SHARE){
                    if (ALREADY_SHOW_TOOLTIP_SHARE_TODAY){
                        can = false;
                    }else{
                        can = true;
                    }
                }else{
                    can = false;
                }
            }
        }else if(tootipAt.equals(TOOLTIP_YES_SUGGESTED)){
            /*if(CAN_SHOW_TOOLTIP_EVENT_LIST){
                can = false;
            }else if(CAN_SHOW_TOOLTIP_LIKE){
                can = false;
            }else if(CAN_SHOW_TOOLTIP_SOCIAL_TAB){
                can = false;
            }*//*else if(CAN_SHOW_TOOLTIP_SHARE){
                can = false;
            }*//*else{
                if(CAN_SHOW_YES_SUGGESTED){
                    if (ALREADY_SHOW_YES_SUGGESTED_TODAY){
                        can = false;
                    }else{
                        can = true;
                    }
                }else{
                    can = false;
                }
            }*/

            if(CAN_SHOW_YES_SUGGESTED){
                if (ALREADY_SHOW_YES_SUGGESTED_TODAY){
                    can = false;
                }else{
                    can = true;
                }
            }else{
                can = false;
            }
        }else if(tootipAt.equals(TOOLTIP_YES_INBOUND)){
            /*if(CAN_SHOW_TOOLTIP_EVENT_LIST){
                can = false;
            }else if(CAN_SHOW_TOOLTIP_LIKE){
                can = false;
            }else if(CAN_SHOW_TOOLTIP_SOCIAL_TAB){
                can = false;
            }*//*else if(CAN_SHOW_TOOLTIP_SHARE){
                can = false;
            }else if(CAN_SHOW_YES_SUGGESTED){
                can = false;
            }*//*else{
                if(CAN_SHOW_YES_INBOUND){
                    if (ALREADY_SHOW_YES_INBOUND_TODAY){
                        can = false;
                    }else{
                        can = true;
                    }
                }else{
                    can = false;
                }
            }*/

            if(CAN_SHOW_YES_INBOUND){
                if (ALREADY_SHOW_YES_INBOUND_TODAY){
                    can = false;
                }else{
                    can = true;
                }
            }else{
                can = false;
            }
        }

        return can;
    }

    public static void setAlreadyShowTooltips(String action, boolean value){

        if(action.equals(ALREADY_TOOLTIP_EVENT_LIST)){
            ALREADY_SHOW_TOOLTIP_EVENT_LIST_TODAY = value;
            App.getSharedPreferences().edit().putBoolean(ALREADY_TOOLTIP_EVENT_LIST, ALREADY_SHOW_TOOLTIP_EVENT_LIST_TODAY).commit();
        }else if(action.equals(ALREADY_TOOLTIP_LIKE)){
            ALREADY_SHOW_TOOLTIP_LIKE_TODAY = value;
            App.getSharedPreferences().edit().putBoolean(ALREADY_TOOLTIP_LIKE, ALREADY_SHOW_TOOLTIP_LIKE_TODAY).commit();
        }else if(action.equals(ALREADY_TOOLTIP_SOCIAL_TAB)){
            ALREADY_SHOW_TOOLTIP_SOCIAL_TAB_TODAY = value;
            App.getSharedPreferences().edit().putBoolean(ALREADY_TOOLTIP_SOCIAL_TAB, ALREADY_SHOW_TOOLTIP_SOCIAL_TAB_TODAY).commit();
        }else if(action.equals(ALREADY_TOOLTIP_SHARE)){
            ALREADY_SHOW_TOOLTIP_SHARE_TODAY = value;
            App.getSharedPreferences().edit().putBoolean(ALREADY_TOOLTIP_SHARE, ALREADY_SHOW_TOOLTIP_SHARE_TODAY).commit();
        }else if(action.equals(ALREADY_TOOLTIP_YES_SUGGESTED)){
            ALREADY_SHOW_YES_SUGGESTED_TODAY = value;
            App.getSharedPreferences().edit().putBoolean(ALREADY_TOOLTIP_YES_SUGGESTED, ALREADY_SHOW_YES_SUGGESTED_TODAY).commit();
        }else if(action.equals(ALREADY_TOOLTIP_YES_INBOUND)){
            ALREADY_SHOW_YES_INBOUND_TODAY = value;
            App.getSharedPreferences().edit().putBoolean(ALREADY_TOOLTIP_YES_INBOUND, ALREADY_SHOW_YES_INBOUND_TODAY).commit();
        }
    }

    public static void setCanShowTooltips(String action, boolean value){

        if(action.equals(TOOLTIP_EVENT_LIST)){
            CAN_SHOW_TOOLTIP_EVENT_LIST = value;
            App.getSharedPreferences().edit().putBoolean(TOOLTIP_EVENT_LIST, CAN_SHOW_TOOLTIP_EVENT_LIST).commit();
        }else if(action.equals(TOOLTIP_LIKE)){
            CAN_SHOW_TOOLTIP_LIKE = value;
            App.getSharedPreferences().edit().putBoolean(TOOLTIP_LIKE, CAN_SHOW_TOOLTIP_LIKE).commit();
        }else if(action.equals(TOOLTIP_SOCIAL_TAB)){
            CAN_SHOW_TOOLTIP_SOCIAL_TAB = value;
            App.getSharedPreferences().edit().putBoolean(TOOLTIP_SOCIAL_TAB, CAN_SHOW_TOOLTIP_SOCIAL_TAB).commit();
        }else if(action.equals(TOOLTIP_SHARE)){
            CAN_SHOW_TOOLTIP_SHARE = value;
            App.getSharedPreferences().edit().putBoolean(TOOLTIP_SHARE, ALREADY_SHOW_TOOLTIP_SHARE_TODAY).commit();
        }else if(action.equals(TOOLTIP_YES_SUGGESTED)){
            CAN_SHOW_YES_SUGGESTED = value;
            App.getSharedPreferences().edit().putBoolean(TOOLTIP_YES_SUGGESTED, ALREADY_SHOW_YES_SUGGESTED_TODAY).commit();
        }else if(action.equals(TOOLTIP_YES_INBOUND)){
            CAN_SHOW_YES_INBOUND = value;
            App.getSharedPreferences().edit().putBoolean(TOOLTIP_YES_INBOUND, ALREADY_SHOW_YES_INBOUND_TODAY).commit();
        }
    }

    public static void setDefaultTooltipsCondition(){
        CAN_SHOW_TOOLTIP_EVENT_LIST = true;
        CAN_SHOW_TOOLTIP_LIKE = true;
        CAN_SHOW_TOOLTIP_SOCIAL_TAB = true;
        CAN_SHOW_TOOLTIP_SHARE = true;
        CAN_SHOW_YES_SUGGESTED = true;
        CAN_SHOW_YES_INBOUND = true;

        App.getSharedPreferences().edit().putBoolean(TOOLTIP_EVENT_LIST, true).putBoolean(TOOLTIP_LIKE, true).putBoolean(TOOLTIP_SOCIAL_TAB, true)
                .putBoolean(TOOLTIP_SHARE, true).putBoolean(TOOLTIP_YES_SUGGESTED, true).putBoolean(TOOLTIP_YES_INBOUND, true).commit();

        ALREADY_SHOW_TOOLTIP_EVENT_LIST_TODAY = false;
        ALREADY_SHOW_TOOLTIP_LIKE_TODAY = false;
        ALREADY_SHOW_TOOLTIP_SOCIAL_TAB_TODAY = false;
        ALREADY_SHOW_TOOLTIP_SHARE_TODAY = false;
        ALREADY_SHOW_YES_SUGGESTED_TODAY = false;
        ALREADY_SHOW_YES_INBOUND_TODAY = false;

        App.getSharedPreferences().edit().putBoolean(ALREADY_TOOLTIP_EVENT_LIST, ALREADY_SHOW_TOOLTIP_EVENT_LIST_TODAY).putBoolean(ALREADY_TOOLTIP_LIKE, ALREADY_SHOW_TOOLTIP_LIKE_TODAY).putBoolean(ALREADY_TOOLTIP_SOCIAL_TAB, ALREADY_SHOW_TOOLTIP_SOCIAL_TAB_TODAY)
                .putBoolean(ALREADY_TOOLTIP_SHARE, ALREADY_SHOW_TOOLTIP_SHARE_TODAY).putBoolean(ALREADY_TOOLTIP_YES_SUGGESTED, ALREADY_SHOW_YES_SUGGESTED_TODAY).putBoolean(ALREADY_TOOLTIP_YES_INBOUND, ALREADY_SHOW_YES_INBOUND_TODAY).commit();
    }

    public static void initTooltipWithAnchor(Context a, View viewAnchor, String text, int width, Tooltip.Gravity gravity){
        tooltip = Tooltip.make(a,
                new Tooltip.Builder(101)
                        .anchor(viewAnchor, gravity)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(true, false), 1 * 60 * 1000)
                                //.activateDelay(800)
                                //.showDelay(300)
                        .text(text)
                        .maxWidth(width)
                        .withArrow(true)
                                //.withOverlay(true)//.typeface(mYourCustomFont)
                        .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                        .withStyleId(R.style.ToolTipStyle)
                        .build()
        );
        tooltip.show();
    }

    public static void initTooltipWithPoint(Context a, Point point, String text, int width, Tooltip.Gravity gravity){
        tooltip = Tooltip.make(a,
                new Tooltip.Builder(101)
                        .anchor(point, gravity)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(true, false), 1 * 3600 * 1000)
                                //.activateDelay(800)
                                //.showDelay(300)
                        .text(text)
                        .maxWidth(width)
                        .withArrow(true)
                                //.withOverlay(true)//.typeface(mYourCustomFont)
                        .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                        .withStyleId(R.style.ToolTipStyle)
                        .build()
        );
        tooltip.show();
    }

    public static void hideTooltip(){
        if(tooltip!=null&&tooltip.isShown()){
            tooltip.hide();
        }
    }

    public static int[] getCenterPoint(Activity a){
        Point size = new Point();
        WindowManager w = a.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            w.getDefaultDisplay().getSize(size);
            return new int[]{size.x/2, size.y/2};
        }else{
            Display d = w.getDefaultDisplay();
            //noinspection deprecation
            return new int[]{d.getWidth()/2, d.getHeight()/2};
        }
    }

}
