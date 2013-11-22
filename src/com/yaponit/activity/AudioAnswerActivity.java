package com.yaponit.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.example.yaponit.R;
import com.yaponit.app.App;
import com.yaponit.listener.MenuItemClickListener;
import com.yaponit.utils.AudioPlayer;
import com.yaponit.utils.AudioRecorder;
import com.yaponit.utils.TimeFormatter;
import com.yaponit.utils.WebApiInteractor;
import com.yaponit.view.Alerter;
import com.yaponit.view.TimeScaleView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class AudioAnswerActivity extends Activity {

	AudioRecorder aRecorder = null;
	AudioPlayer mPlayer = null;

	Button submit;
	Button back;
	Button review;
	Button submit2;
	Button recorder;
	LinearLayout timeBar;
	TextView tvTimer;
	View timerSlider;

	int postId;
	WebApiInteractor api;
	Timer timer;
	Handler handler;

	final int DEFAULT = 0;
	final int RECORDING = 1;
	final int REVIEWING = 2;
	int AudioState = DEFAULT;
	long initTime = 0;
	long time = 0;
	long timeScaleCount = 0;
	int width;
	int sliderWholeScrollX = 0;
	int scaleWholeScrollX = 0;
	final int PIXEL_PER_SECOND = 100;
	final int TIME_PER_PIXEL = 1000 / PIXEL_PER_SECOND;

	int nowPixel = 0;
	int lastPixel = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		api = new WebApiInteractor(this);

		DisplayMetrics dMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		width = dMetrics.widthPixels;

		aRecorder = new AudioRecorder();
		mPlayer = new AudioPlayer();
		mPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				mPlayer.stopPlayRecord();
				timer.cancel();
				AudioState = DEFAULT;
			}
		});

		Bundle bundle = this.getIntent().getExtras();
		postId = bundle.getInt("id");

		setContentView(R.layout.audio_answer);
		((TextView) findViewById(R.id.title)).setText(bundle
				.getString("content"));
		((TextView) findViewById(R.id.fan)).setText(bundle.getString("fan"));

		submit = (Button) findViewById(R.id.submit_button);
		submit2 = (Button) findViewById(R.id.submit_button2);
		OnClickListener submitListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AudioState == RECORDING) {
					aRecorder.stopRecording();
					AudioState = DEFAULT;
				}
				if (AudioRecorder.exitRecordFile()) {
					if (AudioState == REVIEWING) {
						mPlayer.stopPlayRecord();
						AudioState = DEFAULT;
					}
					new SendRecordTask().execute();
					Alerter.show(AudioAnswerActivity.this,
							getString(R.string.submit_title),
							getString(R.string.submit_text));
				} else {
					Alerter.showDialog(AudioAnswerActivity.this,
							getString(R.string.no_answer_title),
							getString(R.string.no_answer_text1),
							getString(R.string.no_answer_text2));
				}
			}
		};
		submit.setOnClickListener(submitListener);
		submit2.setOnClickListener(submitListener);
		back = (Button) findViewById(R.id.back_button);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AudioState == RECORDING) {
					aRecorder.stopRecording();
					AudioState = DEFAULT;
				}
				if (AudioRecorder.exitRecordFile()) {
					if (AudioState == REVIEWING) {
						mPlayer.stopPlayRecord();
						AudioState = DEFAULT;
					}
					Alerter.showDialog(AudioAnswerActivity.this,
							getString(R.string.forget_submit_title),
							getString(R.string.forget_submit_text1),
							getString(R.string.forget_submit_text2));
				} else {
					AudioAnswerActivity.this.finish();
				}

			}
		});
		recorder = (Button) findViewById(R.id.record_button);
		recorder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AudioState == DEFAULT) {
					startRecording();

				} else if (AudioState == RECORDING) {
					recorder.setBackgroundResource(R.drawable.record);
					stopRcording();
					if (AudioRecorder.exitRecordFile()) {
						review.setTextColor(Color.WHITE);
						review.setClickable(true);
						submit2.setTextColor(Color.WHITE);
						submit2.setClickable(true);
					}
					AudioState = DEFAULT;
				} else if (AudioState == REVIEWING) {
					// stop review
					mPlayer.stopPlayRecord();
					// start recording
					startRecording();
				}

			}
		});
		review = (Button) findViewById(R.id.review_button);
		review.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AudioRecorder.exitRecordFile() && AudioState != REVIEWING) {
					AudioState = REVIEWING;
					mPlayer.playRecordFile(AudioRecorder.getRecordFile());
					timerReset();
				}
			}
		});

		review.setTextColor(Color.GRAY);
		review.setClickable(false);
		submit2.setTextColor(Color.GRAY);
		review.setClickable(false);

		handler = new Handler() {

			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					time = System.currentTimeMillis() - initTime;
					tvTimer.setText(TimeFormatter.millisFormat(time));
					if (PIXEL_PER_SECOND * timeScaleCount - scaleWholeScrollX < width) {
						addNewTimeScale(timeScaleCount++);
					}
					if (time % TIME_PER_PIXEL <= 10) {
						nowPixel = (int) (time / TIME_PER_PIXEL);
						int deltaPixel = nowPixel - lastPixel;
						lastPixel = nowPixel;
						if (sliderWholeScrollX < width / 2) {
							timerSlider.scrollBy(-deltaPixel, 0);
							sliderWholeScrollX += deltaPixel;
						} else {
							timeBar.scrollBy(deltaPixel, 0);
							scaleWholeScrollX += deltaPixel;
						}
					}
				}
				super.handleMessage(msg);
			}
		};
		tvTimer = (TextView) findViewById(R.id.timer);
		tvTimer.setText(getString(R.string.timer_text));
		timerSlider = (View) findViewById(R.id.timer_slider);

		timeBar = (LinearLayout) findViewById(R.id.time_bar);
		while (timeScaleCount <= width / PIXEL_PER_SECOND) {
			addNewTimeScale(timeScaleCount++);
		}
	}

	private void startRecording() {
		AudioRecorder.deleteRecordFile();
		if (aRecorder.startRecording()) {
			recorder.setBackgroundResource(R.drawable.stop);
			AudioState = RECORDING;
			timerReset();
		} else {
			Toast.makeText(AudioAnswerActivity.this, "No SD Card!",
					Toast.LENGTH_LONG).show();
		}
	}

	private void stopRcording() {
		timer.cancel();
		aRecorder.stopRecording();
	}

	private void timerReset() {
		timerSlider.scrollBy(sliderWholeScrollX, 0);
		sliderWholeScrollX = 0;
		timeBar.scrollBy(-scaleWholeScrollX, 0);
		scaleWholeScrollX = 0;
		nowPixel = lastPixel = 0;
		initTime = System.currentTimeMillis();
		timer = new Timer(true);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
		}, 0, 10);
	}

	private void addNewTimeScale(long i) {
		TimeScaleView t = new TimeScaleView(this);
		t.setText(TimeFormatter.secondFormat(i));
		timeBar.addView(t);
	}

	class SendRecordTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return sendSoundResponse();
		}

		protected void onPostExecute(Boolean success) {
			Alerter.dismiss();
			if (success) {
				Toast.makeText(AudioAnswerActivity.this, "Send Success!",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent();  
                intent.putExtra("postId", postId);
                setResult(RESULT_OK, intent); 
				finish();
			} else {
				Toast.makeText(AudioAnswerActivity.this, "Send Failed!",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private boolean sendSoundResponse() {
		if (AudioRecorder.getRecordFile() == null) {
			return false;
		}
		return api.sendRecordResponse(postId, 0, "audio",
				AudioRecorder.getRecordFile());

	}

	@Override
	public void finish() {
		AudioRecorder.deleteRecordFile();
		// timer.cancel();
		super.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add("Log out");
		menu.getItem(0).setOnMenuItemClickListener(
				new MenuItemClickListener(this));
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AudioRecorder.deleteRecordFile();
	}
}
