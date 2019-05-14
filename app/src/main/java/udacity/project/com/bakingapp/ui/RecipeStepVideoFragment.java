package udacity.project.com.bakingapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import udacity.project.com.bakingapp.R;
import udacity.project.com.bakingapp.database.Step;
import udacity.project.com.bakingapp.utils.BakingUtil;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepVideoFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = RecipeStepVideoFragment.class.getSimpleName();

    private static final String PLAYER_CURRENT_POSITION = "PLAYER_CURRENT_POSITION";

    interface Arguments {
        String LIST_OF_STEPS = "STEPS";
        String STEP = "STEP";
    }

    private ArrayList<Step> mSteps;
    private Step mStep;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;
    private TextView mRecipeStepDescription;
    private Button mPreviousStepButton;
    private Button mNextStepButton;
    private int mCurrentStep;
    private ProgressBar mProgressBar;
    private long mCurrentPositionInTheVideo;

    public static RecipeStepVideoFragment newInstance(List<Step> steps, Step step) {
        RecipeStepVideoFragment fragment = new RecipeStepVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Arguments.LIST_OF_STEPS, new ArrayList<>(steps));
        bundle.putSerializable(Arguments.STEP, step);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSteps = (ArrayList<Step>) getArguments().getSerializable(Arguments.LIST_OF_STEPS);
        mStep = (Step) getArguments().getSerializable(Arguments.STEP);
        if (savedInstanceState != null) {
            mCurrentPositionInTheVideo = savedInstanceState.getLong(PLAYER_CURRENT_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPlayerView = view.findViewById(R.id.playerView);
        mRecipeStepDescription = view.findViewById(R.id.recipe_step_description);
        mPreviousStepButton = view.findViewById(R.id.previous_step_btn);
        mNextStepButton = view.findViewById(R.id.next_step_btn);
        mCurrentStep = mSteps.indexOf(mStep);
        mProgressBar = view.findViewById(R.id.progress_bar);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            setUpPage();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            setUpPage();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(PLAYER_CURRENT_POSITION, mCurrentPositionInTheVideo);
        super.onSaveInstanceState(outState);
    }

    private void setUpPage() {
        setTitle();
        mRecipeStepDescription.setText(mStep.getDescription());
        initializeMediaSession();
        initializePlayer();
        wireListeners();
        setVisibilityBasedOnCurrentStep();
    }

    private void setVisibilityBasedOnCurrentStep() {
        if (BakingUtil.isTablet(getActivity())) {
            mPreviousStepButton.setVisibility(View.GONE);
            mNextStepButton.setVisibility(View.GONE);
        } else {
            mPreviousStepButton.setVisibility(mCurrentStep == 0 ? View.GONE : View.VISIBLE);
            mNextStepButton.setVisibility(mCurrentStep >= mSteps.size() - 1 ? View.GONE : View.VISIBLE);
        }
    }

    private void wireListeners() {
        mPreviousStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStep = mSteps.get(--mCurrentStep);
                setUpPage();
            }
        });
        mNextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStep = mSteps.get(++mCurrentStep);
                setUpPage();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    private void setTitle() {
        getActivity().setTitle(mStep.getShortDescription());
    }

    private void initializePlayer() {
        if (mPlayer == null) {
            mPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultTrackSelector(), new DefaultLoadControl());
            mPlayerView.setPlayer(mPlayer);
            mPlayer.addListener(this);
        }
        String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mStep.getVideoURL()).buildUpon()
                .build(), new DefaultDataSourceFactory(
                getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
        mPlayer.seekTo(mPlayer.getCurrentWindowIndex(), mCurrentPositionInTheVideo);
        mPlayer.prepare(mediaSource, true, false);
        mPlayer.setPlayWhenReady(true);
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mCurrentPositionInTheVideo = mPlayer.getCurrentPosition();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getActivity(), TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_BUFFERING) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            mPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mPlayer.seekTo(0);
        }

    }
}
