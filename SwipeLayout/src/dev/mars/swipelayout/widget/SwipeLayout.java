package dev.mars.swipelayout.widget;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SwipeLayout extends ViewGroup {

	private View contentView;
	private ArrayList<View> menuViews = new ArrayList<View>();
	private int STATE = 0; // 0:close ;1:open
	private static final int STATE_OPEN = 1;
	private static final int STATE_CLOSE = 0;
	SwipeLayoutContainer swipeLayoutContainer;
	private static final int MIN_SWIPE_INTERREPT_DISTANCE = 30;

	public SwipeLayout(Context context, AttributeSet attrs, int defStyleRes) {
		super(context, attrs, defStyleRes);
	}

	public SwipeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SwipeLayout(Context context, View contentView, ArrayList<View> menuViews,
			SwipeLayoutContainer swipeLayoutContainer) {
		super(context);
		this.contentView = contentView;
		this.menuViews.addAll(menuViews);
		this.swipeLayoutContainer = swipeLayoutContainer;
		// setBackgroundColor(Color.BLUE);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// �����μ�����View�Ŀ��
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		// �ҳ����߶�
		int maxHeight = 0;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			int height = child.getMeasuredHeight();
			LayoutParams params = child.getLayoutParams();
			if (height > maxHeight) {
				maxHeight = height;
			}
		}
		// MeasureSpec.AT_MOST��ʾ��ģʽ��������������ֵ
		int heightMode = MeasureSpec.AT_MOST;
		int heightSize = maxHeight;
		// ����mode��size�����specֵ
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
		log("heightSize " + heightSize);
		// ���������Ŀ��
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}

	private void init() {
		// ���ÿ�Ϊռ������������Ϊ����Ӧ
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		// ������View��˵�View��ӵ�����
		LayoutParams contentViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		addView(contentView, contentViewParams);
		for (View menuView : menuViews) {
			LayoutParams menuViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			addView(menuView, menuViewParams);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// ������View�Ĳ���
		layoutChildViewsBySwipeDistance(0);
	}

	private int downX;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		log("onTouchEvent ");
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			if (STATE == 0) {
				if (swipeLayoutContainer != null) {
					swipeLayoutContainer.closeAllSwipeLayout();
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			int dis = (int) (downX - event.getX());
			if (STATE == 1) {
				// ��״̬���������ʼֵΪMenu��Width
				dis += getMenusWidth();
			}
			layoutChildViewsBySwipeDistance(dis);
			if (Math.abs(dis) >= MIN_SWIPE_INTERREPT_DISTANCE) {
				getParent().requestDisallowInterceptTouchEvent(true); //���߸�ViewGroup����¼����ٱ��ж�
			}
			break;
		case MotionEvent.ACTION_UP:
			if ((int) (downX - event.getX()) > getMenusWidth() / 2) {
				openMenu();
			} else {
				closeMenu();
			}
			break;
		}
		return true;
	}

	public void closeMenu() {
		STATE = STATE_CLOSE;
		layoutChildViewsBySwipeDistance(0);

	}

	public void openMenu() {
		STATE = STATE_OPEN;
		int menusWidth = getMenusWidth();
		layoutChildViewsBySwipeDistance(menusWidth);
	}

	private int getMenusWidth() {
		int menusWidth = 0;
		for (View menuView : menuViews) {
			menusWidth += menuView.getWidth();
		}
		return menusWidth;
	}

	private void layoutChildViewsBySwipeDistance(int dis) {
		log("swipe " + dis);
		int menusWidth = getMenusWidth();
		if (dis > menusWidth) {
			dis = menusWidth;
		}
		if (dis < 0) {
			dis = 0;
		}

		// ��ContentView���в���,����ʼλ���ǻ����ľ���
		contentView.layout(-dis, (getMeasuredHeight() - contentView.getMeasuredHeight()) / 2,
				contentView.getMeasuredWidth() - dis, (getMeasuredHeight() + contentView.getMeasuredHeight()) / 2);

		// ��menuView���в���,����ʼλ���ǻ����ľ������ContentView�Ŀ��

		for (int i = 0; i < menuViews.size(); i++) {
			View menuView = menuViews.get(i);
			int leftPosition = 0;
			if (i == 0) {
				leftPosition = contentView.getMeasuredWidth() - dis;
			} else {
				leftPosition = contentView.getMeasuredWidth() - dis;
				for (int j = 0; j < i; j++) {
					leftPosition += menuViews.get(j).getMeasuredWidth();
				}
			}
			int rightPosition = leftPosition + menuView.getMeasuredWidth();
			menuView.layout(leftPosition, (getMeasuredHeight() - menuView.getMeasuredHeight()) / 2, rightPosition,
					(getMeasuredHeight() + menuView.getMeasuredHeight()) / 2);

			log(i + " leftPosition = " + leftPosition + " rightPosition = " + rightPosition);
		}
	}

	private void log(String tag, String text) {
		Log.e(tag, text);
	}

	private void log(String msg) {
		log("test", msg);
	}
}
