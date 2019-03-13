package com.dyhdyh.view.svgshapelayout.example;

/**
 * @author dengyuhan
 * created 2018/12/17 18:41
 */
public class ExampleInfo {
    private int svgRes;
    private int strokeWidth;
    private int shadowRadius;

    public ExampleInfo(int svgRes) {
        this.svgRes = svgRes;
    }

    public int getSvgRes() {
        return svgRes;
    }

    public void setSvgRes(int svgRes) {
        this.svgRes = svgRes;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getShadowRadius() {
        return shadowRadius;
    }

    public void setShadowRadius(int shadowRadius) {
        this.shadowRadius = shadowRadius;
    }

}
