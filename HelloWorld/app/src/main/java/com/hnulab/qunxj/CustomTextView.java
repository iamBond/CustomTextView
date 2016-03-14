package com.hnulab.qunxj;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2016/3/11.
 */
public class CustomTextView extends View {
    private Context mContext;
    //字体大小
    private int mTextSize;
    //字体颜色
    private int mTextColor;
    //文字内容
    private String mText = "你好";

    //画笔
    private Paint mPaint;
    //文本宽度,高度,行间距
    private float mWidth,mHeight,leading;
    //view的高度
    private int mViewWidth,mViewHeight;
    //文本左右的间距
    private int mRightPadding,mLeftPadding;
    //文字应该画出几行
    private int mLine = 1;
    //各行的文字对应文本中字符的下标
    private String [] mStrArrary;

    public CustomTextView(Context context) {
        super(context);
        mContext = context;
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //获取自定义的属性值
        TypedArray _TA = context.obtainStyledAttributes(attrs,R.styleable.CustomTextView);
        int count = _TA.getIndexCount();
        for(int i = 0;i<count;i++){
            int atts = _TA.getIndex(i);
            switch (atts){
                case R.styleable.CustomTextView_text:
                    mText = _TA.getString(atts);
                    break;
                case R.styleable.CustomTextView_textSize:
                    //默认字体大小为15sp
                    mTextSize = _TA.getDimensionPixelSize(atts,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomTextView_textColor:
                    mTextColor = _TA.getColor(atts, Color.BLACK);
                    break;
                case R.styleable.CustomTextView_leftPadding:
                    mLeftPadding = _TA.getDimensionPixelSize(atts,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomTextView_rightPadding:
                    mRightPadding = _TA.getDimensionPixelSize(atts,
                            (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,0,
                                    getResources().getDisplayMetrics()));
                    break;
                default:
                    break;

            }
        }
        _TA.recycle();



    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mTextColor);
        //先设置字体大小
        mPaint.setTextSize(mTextSize);
        //后测量文字宽度
        mWidth =  mPaint.measureText(mText);
        //再测量文字的高度
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        mHeight = fm.bottom-fm.top;
        leading = fm.leading;
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0;i < mLine ;i++){

            canvas.drawText(mStrArrary[i],0,leading+mHeight*i+mHeight/2+10,mPaint);
//            canvas.drawText(mStrArrary[1],0,leading+mHeight+mHeight/2+10,mPaint);
        }
        super.onDraw(canvas);
    }

    /**
     * 如果这个自定义view支持wrap_content,则必须重写这个方法来处理wrap_content情况
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //获取父控件期望view宽度，如果是match_parent,则这个宽度便是父控件的宽度
        int _specMode = MeasureSpec.getMode(widthMeasureSpec);
        int _specSize = MeasureSpec.getSize(widthMeasureSpec);
        //必须在这个方法中调用初始化画笔和计算字符宽度代码
        //因为当在代码中调用setmText()时，在这里才能捕捉到设置进来的文本内容并重新测量
        init();
        //view的宽度等于父控件的宽度减去左右的间距
        mLine =(int)Math.ceil(mWidth/(_specSize-mLeftPadding-mRightPadding));
        //view的高度 = 行数乘上字符的高度
        mViewHeight = (int)(mLine*mHeight);
        //设置view的高度
        //如果是exactly模式，则传进来的宽度就是组件的宽度，一般是准确值或者match_parent
        if (_specMode == MeasureSpec.EXACTLY) {

            mViewWidth = _specSize;
        } else {
            //取最小值，因为预定的result可能大于父控件大小
            //主要应对宽度为warp_content时，
            if(_specMode == MeasureSpec.AT_MOST){
                mViewWidth = Math.min(600,_specSize);
            }
        }
        //view的宽度 = 父控件的宽度减去两端的宽度
        mViewWidth = mViewWidth - mLeftPadding - mRightPadding;
        setMeasuredDimension(mViewWidth,mViewHeight);
        mStrArrary = new String[mLine];
        int j = 0;
        int start = 0;
        //将文本分割成数组
        for(int i = 1;i <= mText.length();i++){
            if (mPaint.measureText(mText.substring(start,i))>=mViewWidth){
                mStrArrary[j] = mText.substring(start,i-1);
                j++;
                start = i-1;
            }
        }
        mStrArrary[mLine-1] = mText.substring(start,mText.length());
    }

    /**
     * 测量宽度
     * @param pwidthMeasureSpec
     * @return
     */
    private int meassureWidth(int pwidthMeasureSpec){
        int result = 0;
        int _specMode = MeasureSpec.getMode(pwidthMeasureSpec);
        int _specSize = MeasureSpec.getSize(pwidthMeasureSpec);
        //如果是exactly模式，则传进来的宽度就是组件的宽度，一般是准确值或者match_parent
        if (_specMode == MeasureSpec.EXACTLY) {

            result = _specSize;
        } else {//，预定一个宽度200px，
            result = 200;
            //取最小值，因为预定的result可能大于父控件大小
            //主要应对宽度为warp_content时，
            //
            if(_specMode == MeasureSpec.AT_MOST){
                result = Math.min(result,_specSize);
            }
        }
        return result;
    }

    /**
     * 测量高度
     * @param pheightMeasureSpec
     * @return
     */
    private int meassureHeight(int pheightMeasureSpec){
        int result = 0;
        int _specMode = MeasureSpec.getMode(pheightMeasureSpec);
        int _specSize = MeasureSpec.getSize(pheightMeasureSpec);
        //如果是exactly模式，则传进来的宽度就是组件的宽度，一般是准确值或者match_parent
        if (_specMode == MeasureSpec.EXACTLY) {

            result = _specSize;
        } else {//，预定一个宽度200px，
            result = 200;
            //取最小值，因为预定的result可能大于父控件大小
            //主要应对宽度为warp_content时，
            if(_specMode == MeasureSpec.AT_MOST){
                result = Math.min(result,_specSize);
            }
        }
        return result;
    }


}
