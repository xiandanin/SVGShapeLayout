package com.dyhdyh.view.svgshapelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @author dengyuhan
 * created 2018/12/17 14:47
 */
public class SVGShapeLayout extends RelativeLayout {
    private Paint mPaint;
    private RectF mLayer = new RectF();

    private VectorDrawableCompat mSVGDrawable;
    //private int mStrokeColor;
    //private int mStrokeWidth;

    public SVGShapeLayout(Context context) {
        this(context, null);
    }

    public SVGShapeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SVGShapeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SVGShapeLayout);
        int svgResId = a.getResourceId(R.styleable.SVGShapeLayout_svg, 0);
        if (svgResId > 0) {
            setSVGResources(svgResId);
        }
        //mStrokeColor = a.getColor(R.styleable.SVGShapeLayout_strokeColor, Color.TRANSPARENT);
        //mStrokeWidth = a.getDimensionPixelSize(R.styleable.SVGShapeLayout_strokeWidth, 0);
        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLayer.set(0, 0, w, h);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mSVGDrawable != null) {
            canvas.saveLayer(mLayer, null, Canvas.ALL_SAVE_FLAG);
        }
        super.dispatchDraw(canvas);

        if (mSVGDrawable != null) {

            final int measuredWidth = getMeasuredWidth();
            final int measuredHeight = getMeasuredHeight();
            Bitmap maskBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
            Canvas maskCanvas = new Canvas(maskBitmap);
            //mSVGDrawable.setTint(mStrokeColor);
            mSVGDrawable.setBounds(getPaddingLeft(), getPaddingTop(), measuredWidth - getPaddingRight(), measuredHeight - getPaddingBottom());
            mSVGDrawable.draw(maskCanvas);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(maskBitmap, 0.0f, 0.0f, mPaint);

            /*
            if (mStrokeWidth > 0 && mStrokeColor != Color.TRANSPARENT) {
                canvas.saveLayer(mLayer, null, Canvas.ALL_SAVE_FLAG);
                mPaint.setXfermode(null);
                canvas.drawBitmap(maskBitmap, 0.0f, 0.0f, mPaint);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mStrokeColor);
                mPaint.setStrokeWidth(mStrokeWidth);
                final Bitmap strokeBitmap = Bitmap.createScaledBitmap(maskBitmap, maskBitmap.getWidth() - mStrokeWidth, maskBitmap.getHeight() - mStrokeWidth, true);
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
                canvas.drawBitmap(strokeBitmap, mStrokeWidth / 2, mStrokeWidth / 2, mPaint);
            }
            */
        }
    }

    public void setSVGResources(@DrawableRes int drawableRes) {
        setSVGDrawable(VectorDrawableCompat.create(getResources(), drawableRes, getContext().getTheme()));
    }

    public void setSVGDrawable(VectorDrawableCompat drawable) {
        this.mSVGDrawable = drawable;
        invalidate();
    }
}
