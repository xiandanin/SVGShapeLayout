package com.dyhdyh.view.svgshapelayout.example;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyhdyh.view.svgshapelayout.SVGShapeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dengyuhan
 * created 2018/12/17 18:40
 */
public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleHolder> {
    private List<ExampleInfo> mData;

    public ExampleAdapter() {
        mData = new ArrayList<>();
        mData.add(new ExampleInfo(R.drawable.ic_circle));
        mData.add(new ExampleInfo(R.drawable.ic_triangle));
        mData.add(new ExampleInfo(R.drawable.ic_rhombus));
        mData.add(new ExampleInfo(R.drawable.ic_pentagram));
        mData.add(new ExampleInfo(R.drawable.ic_diamond));
        mData.add(new ExampleInfo(R.drawable.ic_sun));
        mData.add(new ExampleInfo(R.drawable.ic_qq));
        mData.add(new ExampleInfo(R.drawable.ic_wechat));
        mData.add(new ExampleInfo(R.drawable.ic_weibo));
        mData.add(new ExampleInfo(R.drawable.ic_github));
    }

    @NonNull
    @Override
    public ExampleHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ExampleHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_example, viewGroup, false)) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleHolder holder, int i) {
        final ExampleInfo item = mData.get(i);
        holder.svgLayout.setSVGResources(item.getSvgRes());
        //holder.svgLayout.setStrokeWidth(item.getStrokeWidth());
    }

    public List<ExampleInfo> getData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ExampleHolder extends RecyclerView.ViewHolder {
        SVGShapeLayout svgLayout;

        public ExampleHolder(@NonNull View itemView) {
            super(itemView);
            svgLayout = itemView.findViewById(R.id.svg_layout);
        }
    }
}
