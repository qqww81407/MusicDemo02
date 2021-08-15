package tw.com.musicdemo02;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{

    private ImageButton btnPlayPause,btnStop;
    private MediaPlayer mediaPlayer = null;
    private boolean isInit = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        btnPlayPause.setImageResource(android.R.drawable.ic_media_play);

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.release();
        mp = null;
        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.seekTo(0);
        mp.start();
    }

    private void findViews(){
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnStop = findViewById(R.id.btnStop);

        btnPlayPause.setOnClickListener(PlayPauseListner);
        btnStop.setOnClickListener(StopListner);
    }

    protected void onResume(){
        super.onResume();
        mediaPlayer = new MediaPlayer();
        Uri uri = Uri.parse("android.resource://"+ getPackageName() + "/" + R.raw.videoplayback);
        try {
            mediaPlayer.setDataSource(this, uri);
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"檔案讀取錯誤",Toast.LENGTH_SHORT).show();
        }
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);

    }

    protected  void onStop(){
        super.onStop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private View.OnClickListener PlayPauseListner = v -> {
        if (mediaPlayer.isPlaying()){
            btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
            mediaPlayer.pause();
        }else{
            btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            if(isInit){
                mediaPlayer.prepareAsync();
                isInit = false;
            }else{
                mediaPlayer.start();
            }

        }
    };

    private View.OnClickListener StopListner = v -> {
      mediaPlayer.stop();
      isInit = true;
      btnPlayPause.setImageResource(android.R.drawable.ic_media_play);

    };
}