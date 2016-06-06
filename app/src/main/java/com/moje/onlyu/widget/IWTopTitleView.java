package com.moje.onlyu.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.moje.onlyu.R;
import com.moje.onlyu.utils.CheckDoubleClick;
import com.moje.onlyu.utils.ImmersiveFullScreenUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class IWTopTitleView extends LinearLayout implements View.OnClickListener {

    @InjectView(R.id.title_root_layout)
    View titleLayout;
    @InjectView(R.id.title_left_back_icon)
    ImageButton backLeftIcon;
    @InjectView(R.id.top_title_back_textview)
    TextView backTxtView;
    @InjectView(R.id.title_left_custom_layout)
    LinearLayout titleLeftCustomLayout;
    @InjectView(R.id.top_title_right_icon)
    TextViewTF rightIcon;
    @InjectView(R.id.top_title_bottom_divider)
    View divider;
    @InjectView(R.id.title_middle_textview)
    TextView titleMiddleTextView;
    @InjectView(R.id.title_middle_customview)
    LinearLayout titleMiddleCustom;
    ColorDrawable colorFFFFFFAlpha;
    private String mTitleText;
    private boolean isShowLeftBtn;
    private boolean isShowCancelBtn;
    boolean isDark = false;
    /**
     * 是否显示下面的分割线
     */
    private boolean isShowBottomDivider = true;

    private int bg_color_id;
    private int backBtnBgId;
    private int backTxtColor;

    private TopTitleOnClickListener titleOnClickListener = new TopTitleOnClickListener() {

        @Override
        public boolean onBackClick() {
            return false;
        }
    };

    public IWTopTitleView(Context context) {
        this(context, null);
    }

    public IWTopTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFromAttributes(context, attrs);
        initView();
    }

    @Override
    public void onClick(View v) {
        if (CheckDoubleClick.isFastDoubleClick()) return;
        switch (v.getId()) {
            case R.id.top_title_back_textview:
            case R.id.title_left_back_icon:
                if (null != titleOnClickListener && !titleOnClickListener.onBackClick()) {
                    sendKeyBackEvent();
                }
                break;
            default:
                break;
        }

    }

    public void setTitleOnClickListener(TopTitleOnClickListener titleOnClickListener) {
        this.titleOnClickListener = titleOnClickListener;
    }

    private void initView() {

        colorFFFFFFAlpha = new ColorDrawable();
        colorFFFFFFAlpha.setColor(getResources().getColor(R.color.color_ffffff_alpha));
        View.inflate(getContext(), R.layout.iw_top_title_layout_l, this);

        ButterKnife.inject(this);

        rightIcon.setVisibility(View.GONE);
        backLeftIcon.setOnClickListener(this);
        backTxtView.setOnClickListener(this);
        titleMiddleTextView.setText(mTitleText);
        titleMiddleCustom.setVisibility(GONE);
        backLeftIcon.setVisibility(isShowLeftBtn ? VISIBLE : INVISIBLE);
        backTxtView.setVisibility(isShowCancelBtn ? VISIBLE : INVISIBLE);
        divider.setVisibility(isShowBottomDivider ? VISIBLE : INVISIBLE);

        backLeftIcon.setImageResource(backBtnBgId);
        backTxtView.setTextColor(backTxtColor);
        if (bg_color_id != -1) {
            titleLayout.setBackgroundColor(getResources().getColor(bg_color_id));
        }

        if (isDark) {
            titleMiddleTextView.setTextColor(getResources().getColor(android.R.color.white));
            rightIcon.setTextColor(getResources().getColor(android.R.color.white));
            divider.setBackgroundColor(getResources().getColor(R.color.main_top_deep_line_color));
        } else {
            ImmersiveFullScreenUtils.miDarkSystemBar((Activity) getContext());
        }

    }

    private void initFromAttributes(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.IWTopTitleView);
            mTitleText = array.getString(R.styleable.IWTopTitleView_titleview_title_text);
            isShowLeftBtn = array.getBoolean(R.styleable.IWTopTitleView_hasLeft, true);
            isShowCancelBtn = array.getBoolean(R.styleable.IWTopTitleView_hasCancel, false);
            if (isShowCancelBtn) {
                isShowLeftBtn = false;
            }
            isDark = array.getBoolean(R.styleable.IWTopTitleView_isDark, false);
            isShowBottomDivider = array.getBoolean(R.styleable.IWTopTitleView_hasDivider, true);
            bg_color_id = array.hasValue(R.styleable.IWTopTitleView_bg_color)
                    ? array.getResourceId(R.styleable.IWTopTitleView_bg_color, -1)
                    : isDark ? R.color.main_top_deep_color : R.color.color_ffffff;
            backBtnBgId = array.hasValue(R.styleable.IWTopTitleView_back_btn_bg)
                    ? array.getResourceId(R.styleable.IWTopTitleView_back_btn_bg, -1)
                    : isDark ? R.drawable.ic_arrow_back_white_24dp : R.drawable
                    .ic_arrow_back_gray_24dp;
            backTxtColor = isDark ? getResources().getColor(android.R.color.white) : getResources
                    ().getColor(R.color.main_gray_color);
            array.recycle();
        } else {
            isShowLeftBtn = true;
            isShowCancelBtn = false;
            isShowBottomDivider = true;
            mTitleText = "";
            bg_color_id = R.color.color_ffffff;
            backBtnBgId = R.drawable.ic_arrow_back_gray_24dp;
            backTxtColor = getResources().getColor(R.color.main_gray_color);
        }
    }


    public void setIsShowLeftBtn(boolean isShowLeftBtn) {
        this.isShowLeftBtn = isShowLeftBtn;
        backLeftIcon.setVisibility(isShowLeftBtn ? VISIBLE : INVISIBLE);
    }

    public void setShowCancelBtn(boolean isShowCancelBtn) {
        this.isShowCancelBtn = isShowCancelBtn;
        backTxtView.setVisibility(isShowCancelBtn ? VISIBLE : INVISIBLE);
        if (isShowCancelBtn) {
            setIsShowLeftBtn(false);
        }
    }

    public void setCancelText(String text) {
        backTxtView.setText(text);
    }

    private void sendKeyBackEvent() {
        final Context context = getContext();
//        if (context instanceof BaseActivity) {
//            ((BackOpFragmentActivity) context).clickBackTitleButton();
//        }
    }

    public void addRightText(int textId, OnClickListener event) {
        this.rightIcon.setVisibility(View.VISIBLE);
        this.rightIcon.setOnClickListener(event);
        this.rightIcon.setText(textId);
    }

    //定制title中间部分
    public void addMiddleView(View view, OnClickListener event) {
        view.setVisibility(VISIBLE);
        titleMiddleCustom.setVisibility(VISIBLE);
        titleMiddleTextView.setVisibility(GONE);
        titleMiddleCustom.addView(view);
        if (null != event) {
            view.setOnClickListener(event);
        }
    }

    public void showMiddleView(boolean show) {
        titleMiddleTextView.setVisibility(show ? GONE : VISIBLE);
        titleMiddleCustom.setVisibility(show ? VISIBLE : GONE);

    }

    public void addLeftView(View view, OnClickListener event) {
        view.setVisibility(VISIBLE);
        titleLeftCustomLayout.setVisibility(VISIBLE);
        titleLeftCustomLayout.addView(view);
        if (null != event) {
            view.setOnClickListener(event);
        }
    }

    public void showLeftCustomView(int visibility) {
        this.titleLeftCustomLayout.setVisibility(visibility);
    }

    public void addRightText(String text, OnClickListener event) {
        this.rightIcon.setVisibility(View.VISIBLE);
        this.rightIcon.setOnClickListener(event);
        this.rightIcon.setText(text);
    }

    public void addRightText(String text, OnClickListener event, LayoutParams ps) {
        this.rightIcon.setVisibility(View.VISIBLE);
        this.rightIcon.setLayoutParams(ps);
        this.rightIcon.setOnClickListener(event);
        this.rightIcon.setText(text);
    }

    public void addRightText(int resId, int style, OnClickListener event) {
        this.rightIcon.setVisibility(View.VISIBLE);
        this.rightIcon.setOnClickListener(event);
        this.rightIcon.setText(resId);
        this.rightIcon.setTextAppearance(getContext(), style);
    }

    public void addRightText(String text, int style, OnClickListener event) {
        this.rightIcon.setVisibility(View.VISIBLE);
        this.rightIcon.setOnClickListener(event);
        this.rightIcon.setText(text);
        this.rightIcon.setTextAppearance(getContext(), style);
    }

    public void showRightTextIcon(int visibility) {
        this.rightIcon.setVisibility(visibility);
    }

    public TextView getRightText() {
        return this.rightIcon;
    }

    public TextView getTitleText() {
        return this.titleMiddleTextView;
    }

    public void setTitleText(String titleText) {
        setTitleText(titleText, Gravity.CENTER);
    }

    public void setTitleText(String titleText, int gravity) {
        if (!TextUtils.isEmpty(titleText)) {
            titleMiddleCustom.setVisibility(GONE);
            mTitleText = titleText;
            titleMiddleTextView.setGravity(gravity);
            titleMiddleTextView.setText(mTitleText);
            titleMiddleTextView.setVisibility(VISIBLE);
        } else {
            titleMiddleTextView.setVisibility(GONE);
        }

    }

    public void setBackBtnBg(int resId) {
        if (resId == backBtnBgId) return;
        backBtnBgId = resId;
        backLeftIcon.setImageResource(resId);
    }

    public View getBackLeftIcon() {
        return backLeftIcon;
    }

    public void setDark(boolean dark) {
        isDark = dark;
        if (isDark) {
            titleMiddleTextView.setTextColor(getResources().getColor(android.R.color.white));
            rightIcon.setTextColor(getResources().getColor(android.R.color.white));
            backLeftIcon.setImageResource(R.drawable.ic_arrow_back_white_24dp);
            backTxtView.setTextColor(getResources().getColor(android.R.color.white));
            divider.setBackgroundColor(getResources().getColor(R.color.main_top_deep_line_color));
            bg_color_id = R.color.main_top_deep_color;
            ImmersiveFullScreenUtils.miWhiteSystemBar((Activity) getContext());
        } else {
            ImmersiveFullScreenUtils.miDarkSystemBar((Activity) getContext());
        }
        if (bg_color_id != -1) {
            titleLayout.setBackgroundColor(getResources().getColor(bg_color_id));
        }
    }

    public void showBackBtn(boolean show) {
        backLeftIcon.setVisibility(show ? VISIBLE : INVISIBLE);
    }

    /**
     * 0-1
     * 0.1,0.2,0.3,0.9,~1.0
     * 0   ~  255
     */
    public void setBgAlpha(float alpha) {
        alpha = alpha > 1 ? 1 : alpha < 0 ? 0 : alpha;
        int alphaValue = (int) (alpha * 255);
        if (colorFFFFFFAlpha.getAlpha() != alphaValue) {
            colorFFFFFFAlpha.setAlpha(alphaValue);
            int color = Color.argb((int) (alpha * 255), 255, 255, 255);
            titleLayout.setBackgroundColor(color);
        }

        /**
         *
         int color = Color.argb((int) (alpha * 255), 255, 255, 255);
         mTopTitleView.setBackgroundColor(color);
         */
    }

    public void setBgColor(int resid) {
        titleLayout.setBackgroundResource(resid);
    }

    public void setHasDivider(boolean has) {
        divider.setVisibility(has ? VISIBLE : INVISIBLE);
    }


    public interface TopTitleOnClickListener {
        /**
         * @param @return 返回false时，控件会发送onback事件。返回true，控件不再做任何事
         * @return boolean
         * @Title: onBackClick
         * @Description:
         */
        boolean onBackClick();
    }
}
