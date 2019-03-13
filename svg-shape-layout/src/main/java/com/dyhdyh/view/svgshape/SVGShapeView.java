package com.dyhdyh.view.svgshape;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

/**
 * @author dengyuhan
 * created 2019/3/12 15:27
 */
public interface SVGShapeView {
    void setVectorResources(@DrawableRes int vectorResId);

    void setShadowColor(@ColorInt int shadowColor);

    void setShadowRadius(int shadowRadius);

    void setStrokeColor(@ColorInt int strokeColor);

    void setStrokeWidth(int strokeWidth);
}
