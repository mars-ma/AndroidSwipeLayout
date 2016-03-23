package dev.mars.swipelayout;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import dev.mars.swipelayout.widget.SwipeLayoutContainer;

public class MainActivity extends Activity {
	SwipeLayoutContainer rootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rootView = (SwipeLayoutContainer) findViewById(R.id.swipeLayoutContainer);
		for (int i = 0; i < 20; i++) {
			addSwipeLayoutToContent();
		}
	}

	private void addSwipeLayoutToContent() {
		View contentView = getLayoutInflater().inflate(R.layout.contentview, null, false);
		View menuView = getLayoutInflater().inflate(R.layout.menuview, null, false);
		View menuView2 = getLayoutInflater().inflate(R.layout.menuview, null, false);
		ArrayList<View> menuViews = new ArrayList<View>();
		menuViews.add(menuView);
		menuViews.add(menuView2);
		rootView.addSwipeLayout(contentView, menuViews);
	}

}
