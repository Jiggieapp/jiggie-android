package com.jiggie.android.component.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jiggie.android.model.Chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rangg on 23/12/2015.
 */
public class ChatTable {
    public static final String NAME = "chat";
    public static final String[] COLUMNS = new String[] { Chat._ID, Chat.FIELD_CREATED_AT, Chat.FIELD_FROM_ID, Chat.FIELD_MESSAGE, Chat.FIELD_FROM_NAME, Chat.FIELD_TOID };

    public static final String CREATE = "CREATE TABLE " + NAME + " (" +
            Chat._ID + " INTEGER PRIMARY KEY," +
            Chat.FIELD_CREATED_AT + " TEXT NOT NULL," +
            Chat.FIELD_FROM_ID + " TEXT NOT NULL," +
            Chat.FIELD_MESSAGE + " TEXT NOT NULL," +
            Chat.FIELD_FROM_NAME + " TEXT NOT NULL," +
            Chat.FIELD_TOID + " TEXT NOT NULL)";

    public static final String INDEX_CREATED_AT = "CREATE INDEX ix_chat_created_at ON chat(" + Chat.FIELD_CREATED_AT + " DESC)";
    public static final String INDEX_TOID = "CREATE INDEX ix_chat_toid ON chat(" + Chat.FIELD_TOID + ")";

    public static Chat read(Cursor cursor) {
        final Chat chat = new Chat();
        chat.setRowId(cursor.getLong(0));
        chat.setCreatedAt(cursor.getString(1));
        chat.setFromId(cursor.getString(2));
        chat.setMessage(cursor.getString(3));
        chat.setFromName(cursor.getString(4));
        chat.setToId(cursor.getString(5));
        return chat;
    }

    public static ContentValues getContentValues(Chat chat) {
        final ContentValues values = new ContentValues();
        if (chat.getRowId() != 0)
            values.put(Chat._ID, chat.getRowId());
        values.put(Chat.FIELD_CREATED_AT, chat.getCreatedAt());
        values.put(Chat.FIELD_FROM_ID, chat.getFromId());
        values.put(Chat.FIELD_MESSAGE, chat.getMessage());
        values.put(Chat.FIELD_FROM_NAME, chat.getFromName());
        values.put(Chat.FIELD_TOID, chat.getToId());
        return values;
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL(ChatTable.CREATE);
        db.execSQL(ChatTable.INDEX_CREATED_AT);
        db.execSQL(ChatTable.INDEX_TOID);
    }

    public static Chat getUnprocessed(DatabaseConnection database) {
        final SQLiteDatabase db = database.getWritableDatabase();
        final Cursor cursor = db.query(ChatTable.NAME, ChatTable.COLUMNS, null, null, null, null, Chat.FIELD_CREATED_AT + " DESC");
        if (cursor.moveToFirst())
            return ChatTable.read(cursor);
        cursor.close();
        return null;
    }

    public static List<Chat> getUnProcessedItems(DatabaseConnection database, String toId) {
        final SQLiteDatabase db = database.getWritableDatabase();
        final Cursor cursor = db.query(ChatTable.NAME, ChatTable.COLUMNS, Chat.FIELD_TOID + "=?", new String[] {toId}, null, null, Chat.FIELD_CREATED_AT + " DESC");
        final List<Chat> items = new ArrayList<>();

        while (cursor.moveToNext())
            items.add(read(cursor));

        cursor.close();
        return items;
    }

    public static void addUnprocessed(DatabaseConnection database, Chat chat) {
        final SQLiteDatabase db = database.getWritableDatabase();
        final ContentValues values = ChatTable.getContentValues(chat);
        chat.setRowId(db.insert(ChatTable.NAME, null, values));
    }

    public static void delete(DatabaseConnection connection, Chat chat) {
        connection.getWritableDatabase().delete(ChatTable.NAME, Chat._ID + "=?", new String[]{String.valueOf(chat.getRowId())});
    }
}
