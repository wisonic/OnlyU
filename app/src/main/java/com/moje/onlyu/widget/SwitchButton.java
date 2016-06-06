package com.moje.onlyu.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * �Զ���View�����ذ�ť
 * �Զ���view�����̣�1.�̳�view��2��дonDraw�����л��ƣ�3.��дonMeasure�޸ĳߴ磬4. ��xml���޸�����
 * @author Administrator
 * 
 */
public class SwitchButton extends View {

	private String NAMESPACE = "http://schemas.android.com/apk/res/cn.ifavor.switchbutton";
	private String ATTR_IS_OPENED = "isOpened";
	
	public interface OnOpenedListener {
		void onChecked(View v, boolean isOpened);
	}

	/*
	 * �����Ǹ��ӵĶ���������Ҫ���á� ���봴���ڳ�Ա�����ڳ�ʼ��ʱ��ֵ
	 */
	Paint mPaint;
	Bitmap mSwitchBackground;
	Bitmap mSlideButton;
	int mMaxLeft;
	int mCurrLeft;
	boolean isOpen = false;
	

	public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}

	public SwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public SwitchButton(Context context) {
		super(context);
		init(null);
	}

	/* ��ʼ��������� */
	private void init(AttributeSet attrs) {
		// ��ʼ������
		mPaint = new Paint();
		mPaint.setColor(Color.BLUE);

		// ��ʼ������ͼƬ
//		mSwitchBackground = BitmapFactory.decodeResource(getResources(),
//				R.drawable.switch_background);
//		mSlideButton = BitmapFactory.decodeResource(getResources(),
//				R.drawable.slide_button);
		
		if (attrs != null){
			int  slideButtonResId= attrs.getAttributeResourceValue(NAMESPACE, "slide_button", -1);
			mSlideButton = BitmapFactory.decodeResource(getResources(), slideButtonResId);
			
			int  switchBackgroundResId= attrs.getAttributeResourceValue(NAMESPACE, "switch_background", -1);
			mSwitchBackground = BitmapFactory.decodeResource(getResources(), switchBackgroundResId);
			
			if (mSlideButton == null || mSwitchBackground == null){
				throw new NullPointerException("��ԴͼƬ����Ϊ��");
			}
		} 
		
		// �������ɻ�������
		mMaxLeft = mSwitchBackground.getWidth() - mSlideButton.getWidth();
		
		// ���ÿ����¼�
		// �ѽ�����¼����߼������� onTouchEvent �� ACTION_UP ��
		
		// ��ʼ������״̬
		initStatus(attrs);
	}

	/* ��ʼ������״̬ */
	private void initStatus(AttributeSet attrs) {
		if (attrs != null){
			boolean isInitOpened = attrs.getAttributeBooleanValue(NAMESPACE, ATTR_IS_OPENED, false);
			setStatus(isInitOpened);
		}
	}
	
	/* ����״̬ */
	private void setStatus(boolean status){
		if (status){
			mCurrLeft = mMaxLeft;
			isOpen = true;
		} else {
			mCurrLeft = 0;
			isOpen = false;
		}
	}

@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	setMeasuredDimension(mSwitchBackground.getWidth(), mSwitchBackground.getHeight());
}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

@Override
protected void onDraw(Canvas canvas) {
	canvas.drawBitmap(mSwitchBackground, 0, 0, mPaint);
	canvas.drawBitmap(mSlideButton, mCurrLeft, 0, mPaint);
}

private OnOpenedListener mOpenedkListener;
public void setOnCheckChangedListener(OnOpenedListener checkedkListener) {
	this.mOpenedkListener = checkedkListener;
}
	
	int startX;
	int moveX;
	
	/**
	 * event.getX() ���ڿؼ�����
	 * event.getRawX() ����������Ļ
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int distance = (int) (event.getX() - startX);
			mCurrLeft += distance;
			startX = (int) event.getX();
			
			/*
			 * �����δ������ƶ��ľ��룬�����þ���ֵ���������ػ���moveX����С
			 */
			moveX += Math.abs(distance);
			break;
		case MotionEvent.ACTION_UP:
			if (moveX < 5){
				// �û��ı����ǵ��
				if (isOpen){
					setStatus(!isOpen);
				} else {
					setStatus(!isOpen);
				}
				
				//����ص�
				if (mOpenedkListener != null){
					mOpenedkListener.onChecked(this, isOpen);
				}
				
			} else {
				// �û��ı����ǻ���
				setStatus(mCurrLeft >= mMaxLeft / 2);
				
				//����ص�
				if (mOpenedkListener != null){
					mOpenedkListener.onChecked(this, isOpen);
				}
			}
			
			moveX = 0;
			break;
		}
		
		// �߽紦��
		if (mCurrLeft < 0){
			mCurrLeft = 0;
		}
		
		if (mCurrLeft > mMaxLeft){
			mCurrLeft = mMaxLeft;
		}
		
		// �����ػ�
		invalidate();
		
		/*
		 * ��������˵���¼���
		 *  ����true���ܼ��� ACTION_MOVE����������Ӧ����¼�
		 *  ����true�����ܼ��� ACTION_MOVE��Ҳ������Ӧ����¼�
		 *  ����super.onTouchEvent(event)���ܼ��� ACTION_MOVE��Ҳ���Լ��� ����¼������ǵ���¼���onTouchEvent֮��
		 *  
		 *   ���û�����õ���¼���
		 *  ����true���ܼ��� ACTION_MOVE
		 *  ����true�����ܼ��� ACTION_MOVE
		 *  ����super.onTouchEvent(event)�����ܼ��� ACTION_MOVE
		 */
		return true;
	}
	
}
