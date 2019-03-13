package com.dyhdyh.view.svgshape;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.dyhdyh.view.R;
import com.sdsmdg.harjot.vectormaster.VectorMasterDrawable;
import com.sdsmdg.harjot.vectormaster.models.PathModel;
import com.sdsmdg.harjot.vectormaster.models.VectorModel;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author dengyuhan
 * created 2019/3/12 13:52
 */
public class SVGShapeHelper {

    private Paint mPaint;
    private RectF mLayer = new RectF();
    private Rect mRegion = new Rect();

    private VectorMasterDrawable mVectorMasterDrawable;
    private int mStrokeColor;
    private int mStrokeWidth;
    private int mShadowColor;
    private int mShadowRadius;
    private int mSVGResourcesId;

    public SVGShapeHelper(View v, AttributeSet attrs) {
        //v.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        final TypedArray a = v.getContext().obtainStyledAttributes(attrs, R.styleable.SVGShapeLayout);
        int svgResourcesId = a.getResourceId(R.styleable.SVGShapeLayout_svg, 0);
        mStrokeColor = a.getColor(R.styleable.SVGShapeLayout_strokeColor, Color.TRANSPARENT);
        mStrokeWidth = a.getDimensionPixelSize(R.styleable.SVGShapeLayout_strokeWidth, 0);
        mShadowColor = a.getColor(R.styleable.SVGShapeLayout_shadowColor, Color.TRANSPARENT);
        mShadowRadius = a.getDimensionPixelSize(R.styleable.SVGShapeLayout_shadowRadius, 0);

        a.recycle();

        if (svgResourcesId != 0) {
            setVectorResources(v.getContext(), svgResourcesId);
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    protected void onSizeChanged(View v, int w, int h) {
        mLayer.set(0, 0, w, h);
        mRegion.set(v.getPaddingLeft() + mShadowRadius, v.getPaddingTop() + mShadowRadius,
                w - v.getPaddingRight() - mShadowRadius, h - v.getPaddingBottom() - mShadowRadius);
    }


    protected void draw(Canvas canvas, Runnable drawRun) throws NoSuchFieldException, IllegalAccessException {
        if (mVectorMasterDrawable == null) {
            drawRun.run();
        } else {
            mVectorMasterDrawable.setBounds(mRegion);
            final Field vectorModelField = VectorMasterDrawable.class.getDeclaredField("vectorModel");
            vectorModelField.setAccessible(true);
            VectorModel vectorModel = (VectorModel) vectorModelField.get(mVectorMasterDrawable);
            final ArrayList<PathModel> pathModels = vectorModel.getPathModels();

            //转path
            Path path = new Path();
            for (PathModel outline : pathModels) {
                outline.setFillColor(Color.TRANSPARENT);
                path.addPath(outline.getPath(), mRegion.left, mRegion.top);
            }
            if (hasStroke()) {
                drawShapeAndStroke(path, canvas, drawRun);
            } else {
                drawShape(path, canvas, drawRun);
            }
        }
    }

    /**
     * 无描边的方法
     *
     * @param path
     * @param canvas
     * @param drawRun
     */
    protected void drawShape(Path path, Canvas canvas, Runnable drawRun) {
        mPaint.reset();
        mPaint.setAntiAlias(true);

        canvas.saveLayer(mLayer, null, Canvas.ALL_SAVE_FLAG);

        drawRun.run();

        //形状
        mPaint.setColor(Color.WHITE);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        final Path outPath = new Path();
        outPath.addRect(0, 0, (int) mLayer.width(), (int) mLayer.height(), Path.Direction.CW);
        outPath.op(path, Path.Op.DIFFERENCE);
        canvas.drawPath(outPath, mPaint);

        //阴影
        if (hasShadow()) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mShadowColor);
            mPaint.setShadowLayer(mShadowRadius, 0, 0, mShadowColor);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
            canvas.drawPath(path, mPaint);
        }
    }


    /**
     * 有描边的方法
     *
     * @param path
     * @param canvas
     * @param drawRun
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    protected void drawShapeAndStroke(Path path, Canvas canvas, Runnable drawRun) {
        mPaint.reset();
        mPaint.setAntiAlias(true);

        //阴影
        if (hasShadow()) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mShadowColor);
            mPaint.setShadowLayer(mShadowRadius, 0, 0, mShadowColor);
            canvas.drawPath(path, mPaint);
        }

        //保存画布状态
        canvas.save();

        //把画布裁剪成形状
        canvas.clipPath(path);
        drawRun.run();

        //描边
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mStrokeColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        canvas.drawPath(path, mPaint);

        //恢复之前保存的状态
        canvas.restore();

        //再画一个1像素的形状 把锯齿遮掉
        mPaint.setColor(mStrokeColor);
        mPaint.setStrokeWidth(1f);
        canvas.drawPath(path, mPaint);
    }

    protected void setVectorResources(Context context, @DrawableRes int svgResId) {
        try {
            if (mSVGResourcesId != svgResId) {
                mVectorMasterDrawable = new VectorMasterDrawable(context, svgResId);
                this.mSVGResourcesId = svgResId;
            }
        } catch (Exception e) {
            Log.e("SVGShapeLayout", "SVG初始化失败");
            e.printStackTrace();
        }
    }


    protected void setStrokeColor(int strokeColor) {
        this.mStrokeColor = strokeColor;
    }

    protected void setStrokeWidth(int strokeWidth) {
        this.mStrokeWidth = strokeWidth;
    }

    protected void setShadowColor(int shadowColor) {
        this.mShadowColor = shadowColor;
    }

    protected void setShadowRadius(View v, int shadowRadius) {
        this.mShadowRadius = shadowRadius;
    }


    private boolean hasStroke() {
        return mStrokeWidth > 0 && mStrokeColor != Color.TRANSPARENT;
    }

    private boolean hasShadow() {
        return mShadowRadius > 0 && Color.TRANSPARENT != mShadowColor;
    }

}
