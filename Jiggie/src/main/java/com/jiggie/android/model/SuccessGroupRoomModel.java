package com.jiggie.android.model;

/**
 * Created by LTE on 7/1/2016.
 */
public final class SuccessGroupRoomModel {
    public final int response;
    public final String msg;
    public final Data data;

    public SuccessGroupRoomModel(int response, String msg, Data data){
        this.response = response;
        this.msg = msg;
        this.data = data;
    }

    public static final class Data {
        public final Group group;

        public Data(Group group){
            this.group = group;
        }

        public static final class Group {
            public final String room_id;

            public Group(String room_id){
                this.room_id = room_id;
            }

            public String getRoom_id() {
                return room_id;
            }
        }

        public Group getGroup() {
            return group;
        }
    }

    public int getResponse() {
        return response;
    }

    public String getMsg() {
        return msg;
    }

    public Data getData() {
        return data;
    }
}
