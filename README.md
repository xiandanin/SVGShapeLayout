![](screenshot/1.gif)


### Gradle引入
```
implementation 'com.dyhdyh.view:svg-shape-layout:1.0.1-beta'
```

### 将SVG转成Vector XML
<img src="screenshot/2.jpg"  width="800"/>

### 在XML使用
```
<com.dyhdyh.view.SVGShapeLayout
    android:id="@+id/svg_layout"
    android:layout_width="100dp"
    android:layout_height="100dp"
    app:strokeWidth="5dp"
    app:svg="@drawable/ic_love"
    app:strokeColor="@color/colorAccent">

    <ImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:src="@drawable/test" />
        
</com.dyhdyh.view.SVGShapeLayout>
```

### 在Java中使用
```
svgLayout.setSVGResources(R.drawable.ic_qq);
svgLayout.setStrokeWidth(10);
svgLayout.setStrokeColor(Color.BLUE);
```