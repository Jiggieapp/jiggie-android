package com.jiggie.android.activity.player;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiggie.android.R;
import com.jiggie.android.component.BundleArg;
import com.jiggie.android.component.HandlerAction;
import com.jiggie.android.component.Utils;
import com.jiggie.android.model.SoundcloudModel;
import com.jiggie.android.view.CirclePlay;
import com.jiggie.android.view.MusicService;
import com.jiggie.android.view.ProgressView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Wandy on 7/20/2016.
 */
public class MusicPlayerFragment extends Fragment
        implements PlayerView, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener{

    private final String TAG = MusicPlayerFragment.class.getSimpleName();
    private Drawable playDrawable, pauseDrawable;
    //private boolean isPlaying = false;
    private Messenger mPlayerMessenger;
    private boolean mIsBound = false;
    private Context mContext;
    PlayerPresenterImplementation implementation;
    private MusicService musicService;
    private SoundcloudModel soundcloudModel;
    private String streamUrl;

    @Bind(R.id.fab_play)
    FloatingActionButton fabPlay;

    /*@Bind(R.id.progress)
    ProgressView progressView;*/

    @Bind(R.id.circle_loading)
    CirclePlay circlePlay;

    @Bind(R.id.lbl_track_artist)
    TextView lblTrackArtist;

    @Bind(R.id.lbl_track_title)
    TextView lblTrackTitle;

    //soundcloud aga part-------------
    private final int STATE_IDLE = 0;
    private final int STATE_INITIALIZED = 1;
    private final int STATE_PREPARED = 2;
    private final int STATE_STARTED = 3;
    private final int STATE_PAUSED = 4;
    private final int STATE_STOPPED = 5;
    private int STATE_PLAYER = STATE_IDLE;
    MediaPlayer mediaPlayer;
    //----------------------------------

    private final Handler mUpdateProgressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final int position = musicService.getPosition();
            /*final int duration = mService.getDuration();*/
            //onUpdateProgress(position, duration);
            sendEmptyMessageDelayed(0, DateUtils.SECOND_IN_MILLIS);
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playDrawable = getActivity().getResources().getDrawable(R.drawable.ic_play_animatable);
        pauseDrawable =  getActivity().getResources().getDrawable(R.drawable.ic_pause_animatable);
        mContext = getActivity().getApplicationContext();
        mContext.bindService(new Intent(mContext, MusicService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        implementation = new PlayerPresenterImplementation(this);
        circlePlay.setCallbacks(new CirclePlay.Callbacks() {
            @Override
            public void onStopAnimation() {
                //supportFinishAfterTransition();
            }
        });

        initPlayer();
    }

    private void initPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);
        try {
            mediaPlayer.setDataSource(getActivity(), Uri.parse(streamUrl+Utils.URL_SOUNDCLOUD_CLIENT_ID));
            STATE_PLAYER = STATE_INITIALIZED;
        }catch (Exception e){
            Log.d(TAG, e.toString());
        }
    }

    @OnClick(R.id.fab_play)
    public void onFabClick() {
        if(soundcloudModel != null)
        {
            if(!mediaPlayer.isPlaying())
            {
                fabPlay.setImageDrawable(playDrawable);
                if(playDrawable instanceof Animatable)
                {
                    ((Animatable) playDrawable).start();
                    ((Animatable) pauseDrawable).stop();
                    //play(streamUrl);
                    circlePlay.start();
                }

                if(STATE_PLAYER==STATE_IDLE){
                    initPlayer();
                    mediaPlayer.prepareAsync();
                }else if(STATE_PLAYER==STATE_INITIALIZED||STATE_PLAYER==STATE_STOPPED){
                    mediaPlayer.prepareAsync();
                }else{
                    mediaPlayer.start();
                }

            }
            else
            {
                fabPlay.setImageDrawable(pauseDrawable);
                if(playDrawable instanceof Animatable)
                {
                    ((Animatable) pauseDrawable).start();
                    ((Animatable) playDrawable).stop();
                    //stop();
                    circlePlay.stop();
                }
                mediaPlayer.pause();
                STATE_PLAYER = STATE_PAUSED;
            }
        }else{
            Log.d(TAG, "song null");
        }
    }

    private void stop()
    {
        Message msg = Message.obtain();
        msg.what = HandlerAction.STOP;
        sendMessage(msg);

    }

    private void play(String stringUri)
    {
        //final String stringUri = "http://api.soundcloud.com/tracks/257659076/stream?client_id=9147700913ab2472e144035ab0d72b5f";
        /*if(mPlayerMessenger == null)
        {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service ;
            musicService = binder.getService();
            mPlayerMessenger = new Messenger(binder.getIBinder());
            mIsBound = true;
        }*/
        stringUri = stringUri + Utils.URL_SOUNDCLOUD_CLIENT_ID;
        Uri uri = Uri.parse(stringUri);
        Message msg = Message.obtain();
        msg.what = HandlerAction.PLAY_URI;
        Bundle data = new Bundle();
        data.putParcelable(BundleArg.PLAY_URI, uri);
        msg.setData(data);
        sendMessage(msg);
    }

    private void sendMessage(Message msg)
    {
        if (mIsBound && mPlayerMessenger != null)
        {
            try
            {
                mPlayerMessenger.send(msg);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getTrackDetail(SoundcloudModel soundcloudModel) {
        this.soundcloudModel = soundcloudModel;
        //progressView.setMax((int)(soundcloudModel.duration / 1000));
        if(this.soundcloudModel != null)
        {
            this.streamUrl = soundcloudModel.stream_url;
            musicService.setDuration(soundcloudModel.duration);
            lblTrackArtist.setText(soundcloudModel.user.username);
            lblTrackTitle.setText(soundcloudModel.title);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //getActivity().registerReceiver(broadcastReceiver,new IntentFilter(TAG));
    }


    private BroadcastReceiver broadcastReceiver =
            new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mUpdateProgressHandler.sendEmptyMessage(0);
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service ;
            musicService = binder.getService();
            mPlayerMessenger = new Messenger(binder.getIBinder());
            mIsBound = true;
            final String trackId = getArguments().getString("trackId");
            implementation.getTrackDetail(trackId);
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            Utils.d(TAG, "service disconnected");
            mIsBound = false;
            mPlayerMessenger = null;
        }
    };

    //SOUNDCLOUD AGA PART-------------------

    @Override
    public void onStop() {
        super.onStop();
        if(mediaPlayer.isPlaying()){
            fabPlay.setImageDrawable(pauseDrawable);
            if(playDrawable instanceof Animatable)
            {
                ((Animatable) pauseDrawable).start();
                ((Animatable) playDrawable).stop();
                circlePlay.stop();
            }
            mediaPlayer.stop();
            STATE_PLAYER = STATE_STOPPED;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*stop();
        getActivity().unregisterReceiver(broadcastReceiver);*/
        mediaPlayer.reset();
        mediaPlayer.release();
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        STATE_PLAYER = STATE_STOPPED;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        STATE_PLAYER = STATE_PREPARED;
        mediaPlayer.start();
        STATE_PLAYER = STATE_STARTED;
    }
    //END OF SOUNDCLOUD AGA PART----------------------------

}
