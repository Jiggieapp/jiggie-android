package com.jiggie.android.component.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.AccessToken;
import com.jiggie.android.App;
import com.jiggie.android.R;
import com.jiggie.android.activity.profile.ProfileDetailActivity;
import com.jiggie.android.component.Utils;
import com.jiggie.android.manager.FirebaseChatManager;
import com.jiggie.android.model.Common;
import com.jiggie.android.model.MessagesModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LTE on 6/19/2016.
 */
public class FirebaseChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    ArrayList<MessagesModel> data = new ArrayList<>();
    private final static String TAG = FirebaseChatAdapter.class.getSimpleName();
    private String event;
    private int type;
    private Dialog dialogLongClick;

    public FirebaseChatAdapter(Activity a, ArrayList<MessagesModel> data, String event, int type){
        this.activity = a;
        this.data = data;
        this.event = event;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == CHAT_HEADER)
        {
            return new ViewHolderHeader(inflater.inflate(R.layout.item_chat_header, parent, false));
        }
        else
        {
            return new ViewHolderBody(inflater.inflate(R.layout.item_chat, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderr, int position) {
        final MessagesModel item = data.get(position);
        if(holderr instanceof FirebaseChatAdapter.ViewHolderBody)
        {
            final FirebaseChatAdapter.ViewHolderBody holder = (FirebaseChatAdapter.ViewHolderBody) holderr;

                holder.txtMessage.setText(item.getMessage().trim());

                try {

                    int gmtOffset = TimeZone.getDefault().getRawOffset();
                    long date = item.getCreated_at() - gmtOffset;

                    String dates = getSimpleDate(Common.ISO8601_DATE_FORMAT.format(new Date(date)));


                    holder.txtLeftTime.setText(dates);
                    holder.txtRightTime.setText(dates);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                holder.imageView.setVisibility(isFromYou(item.getFb_id()) ? View.GONE : View.VISIBLE);
                holder.txtLeftTime.setVisibility(isFromYou(item.getFb_id()) ? View.VISIBLE : View.GONE);
                holder.txtRightTime.setVisibility(isFromYou(item.getFb_id()) ? View.GONE : View.VISIBLE);
                holder.txtMessage.setBackgroundResource(isFromYou(item.getFb_id()) ? R.drawable.bg_chat_self : R.drawable.bg_chat_blue);
                holder.txtMessage.setTextColor(ContextCompat.getColor(holder.root.getContext(), isFromYou(item.getFb_id()) ? R.color.textDarkGray : android.R.color.white));

                final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) holder.root.getLayoutParams();
                layoutParams.gravity = isFromYou(item.getFb_id()) ? Gravity.END : Gravity.START;

                if (!isFromYou(item.getFb_id())) {

                    //Added by Aga 12-2-2016---
                    String urlImage = getProfileImage(item.getFb_id());
                    //---------

                    Glide.with(this.activity).load(urlImage).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            final Resources resources = activity.getResources();
                            if (resources != null) {
                                final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, resource);
                                circularBitmapDrawable.setCircular(true);
                                super.getView().setImageDrawable(circularBitmapDrawable);
                            }
                        }
                    });

                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent intent = new Intent(activity, ProfileDetailActivity.class);
                            intent.putExtra(Common.FIELD_FACEBOOK_ID, item.getFb_id());
                            activity.startActivity(intent);
                        }
                    });
                }

            holder.txtMessage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showLongClickDialog(item.getMessage().trim());
                    return true;
                }
            });
        }
        else if(holderr instanceof ViewHolderHeader)
        {
            ViewHolderHeader holderHeader = (ViewHolderHeader) holderr;

            if(event.equalsIgnoreCase("generic")){
                holderHeader.lblChatHeader.setVisibility(View.GONE);
            }else{
                holderHeader.lblChatHeader.setVisibility(View.VISIBLE);
                holderHeader.lblChatHeader.setText(event);
            }

            holderHeader.txtMessage.setText(item.getMessage().trim());

            try {
                String dates = getSimpleDate(Common.ISO8601_DATE_FORMAT.format(new Date(item.getCreated_at())));

                holderHeader.txtLeftTime.setText(dates);
                holderHeader.txtRightTime.setText(dates);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            holderHeader.imageView.setVisibility(isFromYou(item.getFb_id()) ? View.GONE : View.VISIBLE);
            holderHeader.txtLeftTime.setVisibility(isFromYou(item.getFb_id()) ? View.VISIBLE : View.GONE);
            holderHeader.txtRightTime.setVisibility(isFromYou(item.getFb_id()) ? View.GONE : View.VISIBLE);
            holderHeader.txtMessage.setBackgroundResource(isFromYou(item.getFb_id()) ? R.drawable.bg_chat_self : R.drawable.bg_chat_blue);
            holderHeader.txtMessage.setTextColor(ContextCompat.getColor(holderHeader.root.getContext(), isFromYou(item.getFb_id()) ? R.color.textDarkGray : android.R.color.white));

            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holderHeader.root.getLayoutParams();
            layoutParams.gravity = isFromYou(item.getFb_id()) ? Gravity.END : Gravity.START;

            if (!isFromYou(item.getFb_id())) {

                //Added by Aga 12-2-2016---
                String urlImage = getProfileImage(item.getFb_id());
                //---------

                Glide.with(this.activity).load(urlImage).asBitmap().centerCrop().into(new BitmapImageViewTarget(holderHeader.imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        final Resources resources = activity.getResources();
                        if (resources != null) {
                            final RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, resource);
                            circularBitmapDrawable.setCircular(true);
                            super.getView().setImageDrawable(circularBitmapDrawable);
                        }
                    }
                });

                holderHeader.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent intent = new Intent(activity, ProfileDetailActivity.class);
                        intent.putExtra(Common.FIELD_FACEBOOK_ID, item.getFb_id());
                        activity.startActivity(intent);
                    }
                });
            }

            holderHeader.txtMessage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showLongClickDialog(item.getMessage().trim());
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolderBody extends RecyclerView.ViewHolder {
        @Bind(R.id.txtRightTime)
        TextView txtRightTime;
        @Bind(R.id.txtLeftTime)
        TextView txtLeftTime;
        @Bind(R.id.txtMessage)
        TextView txtMessage;
        @Bind(R.id.imageView)
        ImageView imageView;
        @Bind(R.id.root)
        View root;

        public ViewHolderBody(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ViewHolderHeader extends RecyclerView.ViewHolder {
        @Bind(R.id.lbl_chat_header)
        TextView lblChatHeader;
        @Bind(R.id.txtRightTime)
        TextView txtRightTime;
        @Bind(R.id.txtLeftTime)
        TextView txtLeftTime;
        @Bind(R.id.txtMessage)
        TextView txtMessage;
        @Bind(R.id.imageView)
        ImageView imageView;
        @Bind(R.id.root)
        View root;

        public ViewHolderHeader(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            if(type==FirebaseChatManager.TYPE_GROUP){
                return CHAT_BODY;
            }else{
                return CHAT_HEADER;
            }

        }
        else return CHAT_BODY;
    }

    private final int CHAT_HEADER = 0;
    private final int CHAT_BODY = 1;

    //var return-------------------------------
    public String getSimpleDate(String createdAt) throws ParseException {
        String simpleDate = Utils.BLANK;
        final Date date = Common.ISO8601_DATE_FORMAT_UTC.parse(createdAt);
        simpleDate = Common.SIMPLE_12_HOUR_FORMAT.format(date);
        return simpleDate;
    }

    private boolean isFromYou(String fb_id){
        boolean isFromYou = false;
        //if(fb_id.equals(AccessToken.getCurrentAccessToken().getUserId())){
        if(fb_id.equals(FirebaseChatManager.fb_id)){
            isFromYou = true;
        }
        return isFromYou;
    }

    private String getProfileImage(String fb_id){
        String profileImage = Utils.BLANK;
        for(int i=0;i<FirebaseChatManager.arrUser.size();i++){
            String fb_idMatch = FirebaseChatManager.arrUser.get(i).getFb_id();
            if(fb_id.equals(fb_idMatch)){
                profileImage = FirebaseChatManager.arrUser.get(i).getAvatar();
                break;
            }
        }
        return profileImage;
        //return "http://www.johndoe.pro/img/John_Doe.jpg";
    }
    //end of var return------------------------

    public void showLongClickDialog(final String text) {

        String[] menu = {"Copy Message"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity/*, R.style.fullHeightDialog*/)
                .setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                setClipboard(text);
                                dialogLongClick.dismiss();
                                break;
                            default:
                                dialogLongClick.dismiss();
                                break;
                        }
                    }
                });
        dialogLongClick = builder.create();
        dialogLongClick.show();
    }

    private void setClipboard(String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied referral link", text);
            clipboard.setPrimaryClip(clip);
        }
    }
}
