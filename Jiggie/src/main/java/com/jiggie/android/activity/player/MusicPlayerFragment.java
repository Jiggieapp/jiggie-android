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
        implements PlayerView{

    private final String TAG = MusicPlayerFragment.class.getSimpleName();
    private Drawable playDrawable, pauseDrawable;
    private boolean isPlaying = false;
    private Messenger mPlayerMessenger;
    private boolean mIsBound = false;
    private Context mContext;
    PlayerPresenterImplementation implementation;
    private MusicService musicService;
    private SoundcloudModel soundcloudModel;

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

    private final Handler mUpdateProgressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final int position = musicService.getPosition();
            /*final int duration = mService.getDuration();*/
            //onUpdateProgress(position, duration);
            onUpdateProgress(position);
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
    }

    @OnClick(R.id.fab_play)
    public void onFabClick() {
        if(soundcloudModel != null)
        {
            if(!isPlaying)
            {
                isPlaying = true;
                fabPlay.setImageDrawable(playDrawable);
                if(playDrawable instanceof Animatable)
                {
                    ((Animatable) playDrawable).start();
                    ((Animatable) pauseDrawable).stop();
                    play();
                    circlePlay.start();
                }
            }
            else
            {
                isPlaying = false;
                fabPlay.setImageDrawable(pauseDrawable);
                if(playDrawable instanceof Animatable)
                {
                    ((Animatable) pauseDrawable).start();
                    ((Animatable) playDrawable).stop();
                    stop();
                    circlePlay.stop();
                }
            }
        }
    }

    private void stop()
    {
        Message msg = Message.obtain();
        msg.what = HandlerAction.STOP;
        sendMessage(msg);
    }

    private void play()
    {
        final String stringUri = "http://api.soundcloud.com/tracks/257659076/stream?client_id=9147700913ab2472e144035ab0d72b5f";
        Utils.d(TAG, "play ");
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
        musicService.setDuration(soundcloudModel.duration);
        lblTrackArtist.setText(soundcloudModel.user.username);
        lblTrackTitle.setText(soundcloudModel.title);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter(TAG));
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
            implementation.getTrackDetail("257659076");
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            mIsBound = false;
            mPlayerMessenger = null;
        }
    };

    private void onUpdateProgress(int position)
    {
        /*if (progressView != null) {
            progressView.setProgress(position);
        }*/
    }
}
