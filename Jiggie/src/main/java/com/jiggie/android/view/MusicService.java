package com.jiggie.android.view;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

import com.jiggie.android.activity.player.MusicPlayerFragment;
import com.jiggie.android.component.BroadcastAction;
import com.jiggie.android.component.BundleArg;
import com.jiggie.android.service.IPlayer;
import com.jiggie.android.service.MusicHandler;
import com.vodyasov.amr.AudiostreamMetadataManager;
import com.vodyasov.amr.OnNewMetadataListener;
import com.vodyasov.amr.UserAgent;

import java.io.IOException;
import java.util.List;

import rx.Scheduler;

public class MusicService extends Service implements IPlayer, MediaPlayer.OnPreparedListener, OnNewMetadataListener
{
    public static final String LOG_TAG = MusicService.class.getName();

    private Context mContext;
    private Messenger mMessenger;

    private MediaPlayer mPlayer;
    private Uri mUri;
    private boolean mIsPlaying = false;
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();


    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = getApplication().getApplicationContext();
        mMessenger = new Messenger(new MusicHandler(this));


        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(this);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        //return mMessenger.getBinder();
        return mBinder;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mPlayer.release();
    }

    @Override
    public void play(Uri uri)
    {
        if (uri == null)
        {
            Log.e(LOG_TAG, "Uri must be non-empty");
            return;
        }
        if ((uri.equals(mUri) && !mIsPlaying) || !uri.equals(mUri))
        {
            stop();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try
            {
                Log.v(LOG_TAG, String.format("Play: Uri - %s", uri));
                mPlayer.setDataSource(mContext, uri);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return;
            }
            mUri = uri;
            mIsPlaying = true;

            AudiostreamMetadataManager.getInstance()
                    .setUri(mUri)
                    .setOnNewMetadataListener(this)
                    .setUserAgent(UserAgent.WINDOWS_MEDIA_PLAYER)
                    .start();

            mPlayer.prepareAsync();
        }
    }

    @Override
    public void play(String stringUri)
    {
        play(Uri.parse(stringUri));
    }

    @Override
    public void play()
    {
        play(mUri);
    }

    @Override
    public void stop()
    {
        if (mIsPlaying)
        {
            mPlayer.reset();
            AudiostreamMetadataManager.getInstance().stop();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        mp.start();
        //di sini waktu sudah play
        if (mWorker == null) {
            mWorker = new Worker();
            mWorker.start();
            mWorker.paused = false;

            Intent i = new Intent(MusicPlayerFragment.class.getSimpleName());
            sendBroadcast(i);
        } else {
            mWorker.doResume();
        }
    }

    @Override
    public void onNewHeaders(String stringUri, List<String> name, List<String> desc, List<String> br, List<String> genre, List<String> info)
    {
        StringBuilder sName = new StringBuilder();
        for (String item: name)
        {
            sName.append(item).append(" ");
        }

        StringBuilder sDesc = new StringBuilder();
        for (String item: desc)
        {
            sDesc.append(item).append(" ");
        }

        StringBuilder sBr = new StringBuilder();
        for (String item: br)
        {
            sBr.append(item).append(" ");
        }

        StringBuilder sGenre = new StringBuilder();
        for (String item: genre)
        {
            sGenre.append(item).append(" ");
        }

        StringBuilder sInfo = new StringBuilder();
        for (String item: info)
        {
            sInfo.append(item).append(" ");
        }

        Log.v(LOG_TAG, String.format("onNewHeaders: Uri - %s", stringUri));
        Log.v(LOG_TAG, String.format("Icy-name: %s", sName.toString()));
        Log.v(LOG_TAG, String.format("Icy-description: %s", sDesc.toString()));
        Log.v(LOG_TAG, String.format("Icy-br: %s", sBr.toString()));
        Log.v(LOG_TAG, String.format("Icy-genre: %s", sGenre.toString()));
        Log.v(LOG_TAG, String.format("Ice-audio-info: %s", sInfo.toString()));

        Intent intent = new Intent(BroadcastAction.HEADERS);
        intent.putExtra(BundleArg.HEADER_NAME, sName.toString());
        intent.putExtra(BundleArg.HEADER_DESCRIPTION, sDesc.toString());
        intent.putExtra(BundleArg.HEADER_BR, sBr.toString());
        intent.putExtra(BundleArg.HEADER_GENRE, sGenre.toString());
        intent.putExtra(BundleArg.HEADER_INFO, sInfo.toString());
        sendBroadcast(intent);
    }

    @Override
    public void onNewStreamTitle(String stringUri, String streamTitle)
    {
        Log.v(LOG_TAG, String.format("Uri: %1$s # streamTitle: %2$s", stringUri, streamTitle));
        Intent intent = new Intent(BroadcastAction.STREAM_TITLE);
        intent.putExtra(BundleArg.STREAM_TITLE, streamTitle);
        sendBroadcast(intent);
    }

    private Worker mWorker;
    public int getPosition()
    {
        return mWorker.getPosition();
    }

    private static long duration;
    public void setDuration(long duration) {
        this.duration = duration;
    }

    private long getDuration()
    {
        return this.duration;
    }

    private static class Worker extends Thread {

        boolean paused = false;
        int position = 0;

        @Override
        public void run() {
            try {
                while (position < duration) {
                    sleep(1000);
                    if (!paused) {
                        position++;
                    }
                }
            } catch (InterruptedException e) {
                //Log.d(TAG, "Player unbounded");
            }
        }

        void doResume() {
            paused = false;
        }

        void doPause() {
            paused = true;
        }

        boolean isPlaying() {
            return !paused;
        }

        int getPosition() {
            return position;
        }
    }

    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {

        public MusicService getService() {
            // Return this instance of PlayerService so clients can call public methods
            return MusicService.this;
            //return mMessenger.getBinder();
        }

        public IBinder getIBinder()
        {
            return mMessenger.getBinder();
        }
    }

}
