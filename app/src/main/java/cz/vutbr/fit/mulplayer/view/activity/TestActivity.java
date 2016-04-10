package cz.vutbr.fit.mulplayer.view.activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.model.Song;

public class TestActivity extends AppCompatActivity {
	private Button b1, b2, b3, b4;
	private MediaPlayer mediaPlayer;
	private double startTime = 0;
	private double finalTime = 0;
	private Handler myHandler = new Handler();
	;
	private int forwardTime = 5000;
	private int backwardTime = 5000;
	private SeekBar seekbar;
	private TextView tx1, tx2, tx3;

	public static int oneTimeOnly = 0;

	// SDCard Path
	final String MEDIA_PATH = MediaStore.Audio.Media.getContentUri("external").toString();
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	/**
	 * Function to read all mp3 files from sdcard
	 * and store the details in ArrayList
	 */
	public ArrayList<HashMap<String, String>> getPlayList() {
		File home = new File(MEDIA_PATH);

		if (home.listFiles(new FileExtensionFilter()).length > 0) {
			for (File file : home.listFiles(new FileExtensionFilter())) {
				HashMap<String, String> song = new HashMap<String, String>();
				song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
				song.put("songPath", file.getPath());

				// Adding each song to SongList
				songsList.add(song);
			}
		}
		// return songs list array
		return songsList;
	}

	/**
	 * Class to filter files which are having .mp3 extension
	 */
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}

	private List<Song> songs = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_test);


		b1 = (Button) findViewById(R.id.button);
		b2 = (Button) findViewById(R.id.button2);
		b3 = (Button) findViewById(R.id.button3);
		b4 = (Button) findViewById(R.id.button4);

		tx1 = (TextView) findViewById(R.id.textView2);
		tx2 = (TextView) findViewById(R.id.textView3);
		tx3 = (TextView) findViewById(R.id.textView4);


		//Some audio may be explicitly marked as not being music
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

		String[] projection = {
				MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DURATION
		};

		Cursor cursor = this.managedQuery(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				projection,
				selection,
				null,
				null);

		while (cursor.moveToNext()) {
			String artist = cursor.getString(1);
			String title = cursor.getString(2);
			String data = cursor.getString(3);
			String duration = cursor.getString(5);
			Song song = new Song(artist, title, duration, data);

			songs.add(song);
		}

//        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//        mmr.setDataSource(R.raw.song);

//        String albumName =
//                mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);


		Song latestSong = this.songs.get(this.songs.size() - 1);

		tx3.setText(latestSong.mArtist + " - " + latestSong.mTitle);

//        Uri uri = MediaStore.Audio.Media.getContentUriForPath(latestSong.mData)
//        Uri uri = MediaStore.Audio.Media.getContentUriForPath(latestSong.mData);
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(latestSong.mData);
		} catch (IOException e) {
			e.printStackTrace();
		}

		seekbar = (SeekBar) findViewById(R.id.seekBar);
		seekbar.setClickable(false);
		b2.setEnabled(false);

		b3.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View v) {
				try {
					mediaPlayer.prepare();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
				mediaPlayer.start();

				finalTime = mediaPlayer.getDuration();
				startTime = mediaPlayer.getCurrentPosition();

				if (oneTimeOnly == 0) {
					seekbar.setMax((int) finalTime);
					oneTimeOnly = 1;
				}
				tx2.setText(String.format("%d min, %d sec",
						TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
						TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
								TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
				);

				tx1.setText(String.format("%d min, %d sec",
						TimeUnit.MILLISECONDS.toMinutes((long) startTime),
						TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
								TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
				);

				seekbar.setProgress((int) startTime);
				myHandler.postDelayed(UpdateSongTime, 100);
				b2.setEnabled(true);
				b3.setEnabled(false);
			}
		});

		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
				mediaPlayer.pause();
				b2.setEnabled(false);
				b3.setEnabled(true);
			}
		});

		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int temp = (int) startTime;

				if ((temp + forwardTime) <= finalTime) {
					startTime = startTime + forwardTime;
					mediaPlayer.seekTo((int) startTime);
					Toast.makeText(getApplicationContext(), "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
				}
			}
		});

		b4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int temp = (int) startTime;

				if ((temp - backwardTime) > 0) {
					startTime = startTime - backwardTime;
					mediaPlayer.seekTo((int) startTime);
					Toast.makeText(getApplicationContext(), "You have Jumped backward 5 seconds", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}


	private Runnable UpdateSongTime = new Runnable() {
		@SuppressLint("DefaultLocale")
		public void run() {
			startTime = mediaPlayer.getCurrentPosition();
			tx1.setText(String.format("%d min, %d sec",
					TimeUnit.MILLISECONDS.toMinutes((long) startTime),
					TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
							TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
									toMinutes((long) startTime)))
			);
			seekbar.setProgress((int) startTime);
			myHandler.postDelayed(this, 100);
		}
	};

}
