package com.jiggie.android.activity.profile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiggie.android.R;
import com.jiggie.android.component.StringUtility;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.MemberInfoModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Wandy on 5/27/2016.
 */
public class ProfileLikedEventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<EventPojo> data;
    MemberInfoModel.Data.MemberInfo memberInfo;
    private final int HEADER = 0;
    private final int BODY = 1;
    private boolean isMe = false;
    private OnEventContainerClick onEventContainerClick;
    private final static String TAG = ProfileLikedEventsAdapter.class.getSimpleName();

    public ProfileLikedEventsAdapter(Context context, MemberInfoModel.Data.MemberInfo memberInfo
            , boolean isMe, OnEventContainerClick listener) {
        this.context = context;
        this.memberInfo = memberInfo;
        //this.data = this.memberInfo.getLikes_event();
        this.isMe = isMe;
        data = new ArrayList<>();
        setEventPojo(memberInfo);
        this.onEventContainerClick = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == HEADER) {
            return new ViewHolderHeader(inflater.inflate(R.layout.item_header_profile, parent, false));
        } else {
            return new ViewHolderBody(inflater.inflate(R.layout.item_body_profile, parent, false), onEventContainerClick);
        }
    }

    final String hasTable = "Has Table";
    final String hasTickets = "Has Tickets";
    final String liked = "Liked";

    private void setEventPojo(MemberInfoModel.Data.MemberInfo memberInfo) {
        for (MemberInfoModel.Data.MemberInfo.ListBookings listBookings : memberInfo.getList_bookings()) {
            data.add(new EventPojo(listBookings.getEvent_id(), listBookings.getTitle(), listBookings.getPhotos(), hasTable));
        }

        for (MemberInfoModel.Data.MemberInfo.ListTickets listTickets : memberInfo.getList_tickets()) {
            data.add(new EventPojo(listTickets.getEvent_id(), listTickets.getTitle(), listTickets.getPhotos(), hasTickets));
        }

        for (MemberInfoModel.Data.MemberInfo.LikesEvent likesEvent : memberInfo.getLikes_event()) {
            data.add(new EventPojo(likesEvent.getEvent_id(), likesEvent.getTitle(), likesEvent.getPhotos(), liked));
        }
    }

    private class EventPojo {
        String event_id;
        String title;
        ArrayList<String> photos;
        String type;

        EventPojo(String event_id, String title, ArrayList<String> photos, String type) {
            this.event_id = event_id;
            this.title = title;
            this.photos = photos;
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ArrayList<String> getPhotos() {
            return photos;
        }

        public void setPhotos(ArrayList<String> photos) {
            this.photos = photos;
        }

        public String getEvent_id() {
            return event_id;
        }

        public void setEvent_id(String event_id) {
            this.event_id = event_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder typeHolder, int position) {
        if (typeHolder instanceof ViewHolderHeader) {
            ViewHolderHeader holderHeader = (ViewHolderHeader) typeHolder;
            holderHeader.position = position;

            if(!isMe)
            {
                if (memberInfo.getBadge_booking()) {
                    holderHeader.imgHasTable.setVisibility(View.VISIBLE);
                    holderHeader.imgHasTable.bringToFront();
                } else {
                    holderHeader.imgHasTicket.setVisibility(View.GONE);
                    holderHeader.imgHasTicket.bringToFront();
                }

                if (memberInfo.getBadge_ticket())
                    holderHeader.imgHasTicket.setVisibility(View.VISIBLE);
                else holderHeader.imgHasTable.setVisibility(View.GONE);
            }

            final String age = StringUtility.getAge2(memberInfo.getBirthday());
            holderHeader.lblDescription.setText(memberInfo.getAbout());
            String name = memberInfo.getFirst_name() + " " + memberInfo.getLast_name();
            holderHeader.txtUser.setText(((TextUtils.isEmpty(age)) || (age.equals("0"))) ? name : String.format("%s, %s", name, age));


/*if (isMe) {
                holderHeader.btnEdit.setVisibility(View.VISIBLE);
            } else {
                holderHeader.btnEdit.setVisibility(View.GONE);
            }*/
            if(getItemCount() > 1)
            {
                Utils.d(TAG, "isMe " + isMe);
                if(isMe)
                {
                    holderHeader.lblUserEvent.setText(context.getResources().getString(R.string.your_event));
                }
                else
                {
                    holderHeader.lblUserEvent.setText(
                            context.getResources().getString(R.string.user_event, memberInfo.getFirst_name()));
                }

            }

            final String location = memberInfo.getLocation();
            if(location.isEmpty() || location.equalsIgnoreCase("n/a"))
            {
                holderHeader.lblLocation.setVisibility(View.GONE);
            }
            else
            {
                holderHeader.lblLocation.setText(location);
                holderHeader.lblLocation.setVisibility(View.VISIBLE);
            }
        } else if (typeHolder instanceof ViewHolderBody) {
            ViewHolderBody holderBody = (ViewHolderBody) typeHolder;
            holderBody.position = position;
            holderBody.likesEvent = getEvent(position);
            //holderBody.lblTag.setText(getEvent(position).g);
            holderBody.lblEventTitle.setText(holderBody.likesEvent.getTitle());
            if (holderBody.likesEvent.getPhotos().size() > 0)
                Glide.with(context).load(holderBody.likesEvent.getPhotos().get(0)).into(holderBody.imgEvent);

            holderBody.lblTag.setText(holderBody.likesEvent.getType());
            holderBody.eventId = holderBody.likesEvent.getEvent_id();

            /*for (MemberInfoModel.Data.MemberInfo.ListBookings listBookings : memberInfo.getList_bookings()) {
                if (holderBody.likesEvent.getEvent_id().equalsIgnoreCase(listBookings.getEvent_id())) {
                    holderBody.hasBook = true;
                    break;
                }
            }

            for (MemberInfoModel.Data.MemberInfo.ListTickets listTickets : memberInfo.getList_tickets()) {
                if (holderBody.likesEvent.getEvent_id().equalsIgnoreCase(listTickets.getEvent_id())) {
                    holderBody.hasTicket = true;
                    break;
                }
            }

            if ((!holderBody.hasBook) && (!holderBody.hasTicket)) {
                holderBody.lblTag.setText("Liked");
            } else if (holderBody.hasBook && (!holderBody.hasTicket)) {
                holderBody.lblTag.setText("Has Table");
            } else if (!holderBody.hasBook && (holderBody.hasTicket)) {
                holderBody.lblTag.setText("Has Tickets");
            } else if (holderBody.hasBook && holderBody.hasTicket) {
                holderBody.lblTag.setText("Has Table and Tickets");
            }*/
        }
    }

    public EventPojo getEvent(int position) {
        return data.get(position - 1);
    }


    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER;
        else return BODY;
    }

    public class ViewHolderBody extends RecyclerView.ViewHolder {
        @Bind(R.id.lbl_event_title)
        TextView lblEventTitle;
        @Bind(R.id.img_event)
        ImageView imgEvent;
        @Bind(R.id.lblTag)
        TextView lblTag;
        @Bind(R.id.event_container)
        LinearLayout eventContainer;
        String eventId;
        boolean hasBook = false;
        boolean hasTicket = false;

        OnEventContainerClick onEventClickListener;

        EventPojo likesEvent;
        int position;

        public ViewHolderBody(View itemView, OnEventContainerClick onEventContainerClick) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.onEventClickListener = onEventContainerClick;
            eventContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEventClickListener.onEventContainerClick(eventId);
                }
            });
        }
    }


    public interface OnEventContainerClick
    {
        public void onEventContainerClick(final String eventId);
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder {
        @Bind(R.id.img_has_table)
        ImageView imgHasTable;
        @Bind(R.id.img_has_ticket)
        ImageView imgHasTicket;
        @Bind(R.id.txtUser)
        TextView txtUser;
        @Bind(R.id.lblPhoneNumber)
        TextView lblPhoneNumber;
        @Bind(R.id.txtLocation)
        TextView lblLocation;
        @Bind(R.id.txtDescription)
        TextView lblDescription;
        /*@Bind(R.id.btnEdit)
        ImageButton btnEdit;*/
        @Bind(R.id.lbl_user_event)
        TextView lblUserEvent;

        int position;
        EventPojo likesEvent;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
