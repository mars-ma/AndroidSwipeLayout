package dev.mars.swipelayout.widget;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class SwipeLayoutContainer extends ScrollView {
	
	LinearLayout childLayout;

	public SwipeLayoutContainer(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public SwipeLayoutContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SwipeLayoutContainer(Context context) {
		super(context);
		init();
	}

	private void init() {
		childLayout = new LinearLayout(getContext());
		childLayout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		addView(childLayout, params);
	}

	public void addSwipeLayout(View contentView, ArrayList<View> menuViews) {
		SwipeLayout layout = createSwipeLayout(contentView, menuViews);
		childLayout.addView(layout);
	}
	
	public void closeAllSwipeLayout(){
		for(int i = 0;i<childLayout.getChildCount();i++){
			View childView =childLayout.getChildAt(i);
			if(childView instanceof SwipeLayout){
				((SwipeLayout)childView).closeMenu();
			}
		}
	}

	private SwipeLayout createSwipeLayout(View contentView, ArrayList<View> menuViews) {
		return new SwipeLayout(getContext(), contentView, menuViews,this);
	}
//	
//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		Log.e("test","SwipeLayoutContainer onInterceptTouchEvent "+ev.getAction());
//		if(ev.getAction() == MotionEvent.ACTION_DOWN){
//			Log.e("test","SwipeLayoutContainer onInterceptTouchEvent ACTION_DOWN");
//		}
//		return super.onInterceptTouchEvent(ev);
//	}
}
