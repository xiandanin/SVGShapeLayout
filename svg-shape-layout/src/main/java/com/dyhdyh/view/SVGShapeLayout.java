package com.dyhdyh.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.models.PathModel;
import com.sdsmdg.harjot.vectormaster.models.VectorModel;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author dengyuhan
 * created 2018/12/17 14:47
 */
public class SVGShapeLayout extends RelativeLayout {
    private Paint mPaint;
    private RectF mLayer = new RectF();

    private VectorDrawableCompat mVectorDrawable;
    private VectorMasterDrawable mVectorMasterDrawable;
    private int mStrokeColor;
    private int mStrokeWidth;
    private int mSVGResourcesId;

    public SVGShapeLayout(Context context) {
        this(context, null);
    }

    public SVGShapeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SVGShapeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SVGShapeLayout);
        mSVGResourcesId = a.getResourceId(R.styleable.SVGShapeLayout_svg, 0);
        mStrokeColor = a.getColor(R.styleable.SVGShapeLayout_strokeColor, Color.TRANSPARENT);
        mStrokeWidth = a.getDimensionPixelSize(R.styleable.SVGShapeLayout_strokeWidth, 0);
        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        applyAttributes();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLayer.set(0, 0, w, h);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mSVGResourcesId <= 0) {
            super.dispatchDraw(canvas);
        } else {
            if (mVectorMasterDrawable != null) {
                drawShapeAndStroke(canvas);
            } else {
                drawShape(canvas);
            }
        }
    }


    public void setSVGResources(@DrawableRes int svgResId) {
        if (mSVGResourcesId != svgResId) {
            mVectorMasterDrawable = null;
            mVectorDrawable = null;
        }
        this.mSVGResourcesId = svgResId;
        applyAttributes();
        invalidate();
    }


    protected void drawShape(Canvas canvas) {
        if (mVectorDrawable != null) {
            canvas.saveLayer(mLayer, null, Canvas.ALL_SAVE_FLAG);
        }
        super.dispatchDraw(canvas);

        if (mVectorDrawable != null) {
            final int measuredWidth = getMeasuredWidth();
            final int measuredHeight = getMeasuredHeight();
            Bitmap maskBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
            Canvas maskCanvas = new Canvas(maskBitmap);
            mVectorDrawable.setBounds(getPaddingLeft(), getPaddingTop(), measuredWidth - getPaddingRight(), measuredHeight - getPaddingBottom());
            mVectorDrawable.draw(maskCanvas);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(maskBitmap, 0.0f, 0.0f, mPaint);
        }
    }

    protected void drawShapeAndStroke(Canvas canvas) {
        try {
            mVectorMasterDrawable.setBounds(0, 0, getWidth(), getHeight());
            final Field vectorModelField = VectorMasterDrawable.class.getDeclaredField("vectorModel");
            vectorModelField.setAccessible(true);
            VectorModel vectorModel = (VectorModel) vectorModelField.get(mVectorMasterDrawable);
            final ArrayList<PathModel> pathModels = vectorModel.getPathModels();
            for (PathModel outline : pathModels) {
                outline.setFillColor(Color.TRANSPARENT);
                //保存画布状态
                canvas.save();
                //把画布裁剪成形状
                canvas.clipPath(outline.getPath());
                super.dispatchDraw(canvas);
                //描边
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mStrokeColor);
                mPaint.setStrokeWidth(mStrokeWidth);
                canvas.drawPath(outline.getPath(), mPaint);
                //恢复之前保存的状态
                canvas.restore();
                //再画一个1像素的形状 把锯齿遮掉
                mPaint.setStrokeWidth(1f);
                canvas.drawPath(outline.getPath(), mPaint);
            }
        } catch (Exception e) {
            e.printStackTrace();
            super.dispatchDraw(canvas);
        }
    }

    public void setStrokeColor(int strokeColor) {
        this.mStrokeColor = strokeColor;
        applyAttributes();
        invalidate();
    }

    public void setStrokeWidth(int strokeWidth) {
        this.mStrokeWidth = strokeWidth;
        applyAttributes();
        invalidate();
    }


    public void applyAttributes() {
        if (mSVGResourcesId <= 0) {
            return;
        }
        if (mStrokeWidth > 0 && mStrokeColor != Color.TRANSPARENT) {
            //如果有边框 就得用裁剪的方式
            if (mVectorMasterDrawable == null) {
                mVectorMasterDrawable = new VectorMasterDrawable(getContext(), mSVGResourcesId);
            }
        } else {
            //没有边框就用 遮罩的方式
            if (mVectorDrawable == null) {
                mVectorDrawable = VectorDrawableCompat.create(getResources(), mSVGResourcesId, getContext().getTheme());
            }
        }
    }
}
