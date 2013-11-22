package com.yaponit.activity;

import java.util.*;

import com.example.yaponit.R;
import com.yaponit.app.App;
import com.yaponit.entity.Post;
import com.yaponit.listener.MenuItemClickListener;
import com.yaponit.service.ListenNetStateService;
import com.yaponit.utils.WebApiInteractor;
import com.yaponit.view.Alerter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.*;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	final int PAGE_COUNT = 10;
	PostListAdapter adapter;

	ListView list;
	View loadMoreView;
	Button byDate;
	Button byVotes;
	Button unanswered;
	Button help;
	Button menu;
	Button loadMore;

	WebApiInteractor poster;

	private String order = "date";
	private String orderby = "desc";
	private String type = "";
	private int currentPage = 0;
	SortMode sortMode = SortMode.Default;

	WindowManager wm;
	int width;

	// 手指当前在X轴的坐标位置
	float mNowMotionX;
	// 在X轴的初始坐标位置
	float sFirstMotionX;
	// 手指抬起时候，在X轴的坐标位置
	float sLastMotionX;

	// view and position are for getting current list itemView
	View view;
	int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		wm = this.getWindowManager();
		setContentView(R.layout.questions);
		poster = new WebApiInteractor(MainActivity.this);

		DisplayMetrics dMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dMetrics);
		width = dMetrics.widthPixels;

		byDate = (Button) findViewById(R.id.byDate);
		byDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sortMode == SortMode.ByVotes) {
					byVotes.setBackgroundDrawable(null);
					byVotes.setTextColor(getResources().getColor(
							R.color.app_deep_blue));
				}
				if (sortMode == SortMode.Unanswered) {
					unanswered.setBackgroundResource(R.drawable.right_rounded);
					unanswered.setTextColor(getResources().getColor(
							R.color.app_deep_blue));
				}
				if (sortMode != SortMode.ByDate) {
					byDate.setBackgroundResource(R.drawable.left_rounded_selected);
					byDate.setTextColor(Color.WHITE);
					sortMode = SortMode.ByDate;
					// sort by date
					sortQuestions("date", "desc", "");
				}
			}
		});

		byVotes = (Button) findViewById(R.id.byVotes);
		byVotes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sortMode == SortMode.ByDate) {
					byDate.setBackgroundResource(R.drawable.left_rounded);
					byDate.setTextColor(getResources().getColor(
							R.color.app_deep_blue));
				}
				if (sortMode == SortMode.Unanswered) {
					unanswered.setBackgroundResource(R.drawable.right_rounded);
					unanswered.setTextColor(getResources().getColor(
							R.color.app_deep_blue));
				}
				if (sortMode != SortMode.ByVotes) {
					byVotes.setBackgroundResource(R.drawable.default_selected);
					byVotes.setTextColor(Color.WHITE);
					sortMode = SortMode.ByVotes;
					// sort by votes
					sortQuestions("vote", "desc", "");
				}
			}
		});

		unanswered = (Button) findViewById(R.id.unanswered);
		unanswered.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sortMode == SortMode.ByVotes) {
					byVotes.setBackgroundDrawable(null);
					byVotes.setTextColor(getResources().getColor(
							R.color.app_deep_blue));
				}
				if (sortMode == SortMode.ByDate) {
					byDate.setBackgroundResource(R.drawable.left_rounded);
					byDate.setTextColor(getResources().getColor(
							R.color.app_deep_blue));
				}
				if (sortMode != SortMode.Unanswered) {
					unanswered
							.setBackgroundResource(R.drawable.right_rounded_selected);
					unanswered.setTextColor(Color.WHITE);
					sortMode = SortMode.Unanswered;
					// filter by unanswered
					sortQuestions("date", "desc", "unanswered");
				}
			}
		});

		list = (ListView) findViewById(R.id.post_list);
		loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
		loadMore = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
		loadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loadMore.setText("Loading more questions...");
				if (poster.preRequest()) {
					new QueryPostTask().execute(++currentPage);
				}
			}
		});
		list.addFooterView(loadMoreView);
		adapter = new PostListAdapter();
		list.setAdapter(adapter);
		list.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final float x = event.getX();
				int action = event.getAction();

				switch (action) {
				case MotionEvent.ACTION_DOWN:
					sFirstMotionX = 0;
					sLastMotionX = 0;
					sFirstMotionX = event.getX();
					mNowMotionX = sFirstMotionX;
					position = list.pointToPosition((int) event.getX(),
							(int) event.getY());
					int firstVisiblePosition = list.getFirstVisiblePosition();
					view = list.getChildAt(position - firstVisiblePosition);
					break;
				case MotionEvent.ACTION_MOVE:
					sLastMotionX = x;
					// 手指在当前x轴的偏移值
					final int deltaX = (int) (mNowMotionX - x);
					int wholeDeltaX = (int) Math.abs(sLastMotionX
							- sFirstMotionX);
					if (wholeDeltaX < width) {
						mNowMotionX = x;
						if (view != null)
							view.scrollBy(deltaX, 0);
					}
					break;
				case MotionEvent.ACTION_UP:
					sLastMotionX = x;
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					Post currentPost = (Post) adapter.getItem(position);
					if (currentPost != null) {
						bundle.putInt("id", currentPost.getId());
						bundle.putString("content", currentPost.getTitle());
						bundle.putString("fan", currentPost.getAuthor());
						intent.putExtras(bundle);
					}
					if (sLastMotionX - sFirstMotionX < -width / 2
							&& sLastMotionX != 0) {
						intent.setClass(MainActivity.this,
								TextAnswerActivity.class);
						startActivityForResult(intent, 0);
					} else if (sLastMotionX - sFirstMotionX > width / 2) {
						intent.setClass(MainActivity.this,
								AudioAnswerActivity.class);
						startActivityForResult(intent, 0);
					}

					if (view != null)
						view.scrollBy((int) (sLastMotionX - sFirstMotionX), 0);

					break;
				}
				return false;
			}
		});

		menu = (Button) findViewById(R.id.menu);
		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openOptionsMenu();
			}
		});

		byDate.performClick();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == RESULT_OK) {
			adapter.updateAnswerState(data.getIntExtra("postId", -1), true);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Intent intent = new Intent(MainActivity.this,
				ListenNetStateService.class);
		stopService(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add("Log Out");
		menu.add("Help");

		menu.getItem(0).setOnMenuItemClickListener(
				new MenuItemClickListener(this));
		menu.getItem(1).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Intent intent = new Intent();
						intent.putExtra("fromQuestions", true);
						intent.setClass(MainActivity.this,
								WelcomeActivity.class);
						MainActivity.this.startActivity(intent);
						return false;
					}
				});
		return true;
	}

	@Override
	public void onBackPressed() {
		App.getInstance().exit(this);
	}

	private void sortQuestions(String o, String oby, String t) {
		order = o;
		orderby = oby;
		type = t;
		currentPage = 0;
		adapter.clear();
		Alerter.show(MainActivity.this, "Questions", "Getting Questions...");
		new QueryPostTask().execute(0);
	}

	class QueryPostTask extends AsyncTask<Integer, Integer, List<Post>> {

		@Override
		protected List<Post> doInBackground(Integer... params) {
			int queryPage = params[0];
			return poster.queryPosts(queryPage, order, orderby, type);
		}

		protected void onPostExecute(List<Post> posts) {
			Alerter.dismiss();
			if (posts.size() != 0) {
				for (Post p : posts) {
					adapter.addItem(p);
				}
				adapter.notifyDataSetChanged();
			} else {
				String message = currentPage == 0 ? "No questions"
						: "No more questions";
				Alerter.showDialog(MainActivity.this, "Questions", message);
			}
			loadMore.setText("Show more questions");
		}
	}

	class PostListAdapter extends BaseAdapter {
		List<Post> postItems = new ArrayList<Post>();

		@Override
		public int getCount() {
			return postItems.size();
		}

		@Override
		public Object getItem(int position) {
			return postItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = getLayoutInflater().inflate(R.layout.list_item, null);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					width, LinearLayout.LayoutParams.MATCH_PARENT);
			LinearLayout.LayoutParams qParams = new LinearLayout.LayoutParams(
					width, LinearLayout.LayoutParams.WRAP_CONTENT);
			View audioView = view.findViewById(R.id.left_rec);
			audioView.setLayoutParams(params);
			View textView = view.findViewById(R.id.right_rec);
			textView.setLayoutParams(params);
			View questionModel = view.findViewById(R.id.question);
			questionModel.setLayoutParams(qParams);
			if (!postItems.get(position).getAnswered()) {
				((ImageView) questionModel.findViewById(R.id.anwsered_flag))
						.setVisibility(View.INVISIBLE);
			}
			((TextView) questionModel.findViewById(R.id.title))
					.setText(postItems.get(position).getTitle());
			((TextView) questionModel.findViewById(R.id.fan)).setText(postItems
					.get(position).getAuthor());
			view.scrollBy(width, 0);
			return view;
		}

		public void addItem(Post item) {
			postItems.add(item);
		}

		public void updateAnswerState(int id, boolean answered) {
			for (Post p : postItems) {
				if (p.getId() == id) {
					p.setAnswered(answered);
				}
			}
			notifyDataSetChanged();
		}

		public void clear() {
			postItems.clear();
			notifyDataSetChanged();
		}
	}

	enum SortMode {
		ByDate, ByVotes, Unanswered, Default
	}
}
