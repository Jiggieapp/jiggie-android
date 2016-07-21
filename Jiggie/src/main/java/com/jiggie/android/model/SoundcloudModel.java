package com.jiggie.android.model;

/**
 * Created by Wandy on 7/19/2016.
 */
public final class SoundcloudModel {
    public final String kind;
    public final long id;
    public final String created_at;
    public final long user_id;
    public final long duration;
    public final boolean commentable;
    public final String state;
    public final long original_content_size;
    public final String last_modified;
    public final String sharing;
    public final String tag_list;
    public final String permalink;
    public final boolean streamable;
    public final String embeddable_by;
    public final boolean downloadable;
    public final String purchase_url;
    public final String label_id;
    public final String purchase_title;
    public final String genre;
    public final String title;
    public final String description;
    public final String label_name;
    public final String release;
    public final String track_type;
    public final String key_signature;
    public final String isrc;
    public final String video_url;
    public final String bpm;
    public final long release_year;
    public final long release_month;
    public final long release_day;
    public final String original_format;
    public final String license;
    public final String uri;
    public final User user;
    public final String permalink_url;
    public final String artwork_url;
    public final String waveform_url;
    public final String stream_url;
    public final long playback_count;
    public final long download_count;
    public final long favoritings_count;
    public final long comment_count;
    public final String attachments_uri;
    public final String policy;
    public final String monetization_model;

    public SoundcloudModel(String kind, long id, String created_at, long user_id, long duration, boolean commentable, String state, long original_content_size, String last_modified, String sharing, String tag_list, String permalink, boolean streamable, String embeddable_by, boolean downloadable, String purchase_url, String label_id, String purchase_title, String genre, String title, String description, String label_name, String release, String track_type, String key_signature, String isrc, String video_url, String bpm, long release_year, long release_month, long release_day, String original_format, String license, String uri, User user, String permalink_url, String artwork_url, String waveform_url, String stream_url, long playback_count, long download_count, long favoritings_count, long comment_count, String attachments_uri, String policy, String monetization_model){
        this.kind = kind;
        this.id = id;
        this.created_at = created_at;
        this.user_id = user_id;
        this.duration = duration;
        this.commentable = commentable;
        this.state = state;
        this.original_content_size = original_content_size;
        this.last_modified = last_modified;
        this.sharing = sharing;
        this.tag_list = tag_list;
        this.permalink = permalink;
        this.streamable = streamable;
        this.embeddable_by = embeddable_by;
        this.downloadable = downloadable;
        this.purchase_url = purchase_url;
        this.label_id = label_id;
        this.purchase_title = purchase_title;
        this.genre = genre;
        this.title = title;
        this.description = description;
        this.label_name = label_name;
        this.release = release;
        this.track_type = track_type;
        this.key_signature = key_signature;
        this.isrc = isrc;
        this.video_url = video_url;
        this.bpm = bpm;
        this.release_year = release_year;
        this.release_month = release_month;
        this.release_day = release_day;
        this.original_format = original_format;
        this.license = license;
        this.uri = uri;
        this.user = user;
        this.permalink_url = permalink_url;
        this.artwork_url = artwork_url;
        this.waveform_url = waveform_url;
        this.stream_url = stream_url;
        this.playback_count = playback_count;
        this.download_count = download_count;
        this.favoritings_count = favoritings_count;
        this.comment_count = comment_count;
        this.attachments_uri = attachments_uri;
        this.policy = policy;
        this.monetization_model = monetization_model;
    }

    public static final class User {
        public final long id;
        public final String kind;
        public final String permalink;
        public final String username;
        public final String last_modified;
        public final String uri;
        public final String permalink_url;
        public final String avatar_url;

        public User(long id, String kind, String permalink, String username, String last_modified, String uri, String permalink_url, String avatar_url){
            this.id = id;
            this.kind = kind;
            this.permalink = permalink;
            this.username = username;
            this.last_modified = last_modified;
            this.uri = uri;
            this.permalink_url = permalink_url;
            this.avatar_url = avatar_url;
        }
    }
}
