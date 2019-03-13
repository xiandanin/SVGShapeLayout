package com.dyhdyh.view.svgshape;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author dengyuhan
 * created 2018/12/17 14:47
 */
public class SVGShapeImageView extends AppCompatImageView implements SVGShapeView {
    private SVGShapeHelper mShapeHelper;

    public SVGShapeImageView(Context context) {
        this(context, null);
    }

    public SVGShapeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SVGShapeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mShapeHelper = new SVGShapeHelper(this, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mShapeHelper.onSizeChanged(this, w, h);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        try {
            mShapeHelper.draw(canvas, new Runnable() {
                @Override
                public void run() {
                    SVGShapeImageView.super.onDraw(canvas);
                }
            });
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setVectorResources(int vectorResId) {
        mShapeHelper.setVectorResources(getContext(), vectorResId);
        invalidate();
    }

    @Override
    public void setShadowColor(int shadowColor) {
        mShapeHelper.setShadowColor(shadowColor);
        invalidate();
    }

    @Override
    public void setShadowRadius(int shadowRadius) {
        mShapeHelper.setShadowRadius(this, shadowRadius);
        invalidate();
    }

    @Override
    public void setStrokeColor(int strokeColor) {
        mShapeHelper.setStrokeColor(strokeColor);
        invalidate();
    }

    @Override
    public void setStrokeWidth(int strokeWidth) {
        mShapeHelper.setStrokeWidth(strokeWidth);
        invalidate();
    }

}
