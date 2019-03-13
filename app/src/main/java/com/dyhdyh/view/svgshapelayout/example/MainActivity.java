package com.dyhdyh.view.svgshapelayout.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    SeekBar seekbar_stroke,seekbar_shadow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        final ExampleAdapter adapter = new ExampleAdapter();
        rv.setAdapter(adapter);
        seekbar_stroke = findViewById(R.id.seekbar_stroke);
        seekbar_shadow = findViewById(R.id.seekbar_shadow);
        final SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                for (int i = 0; i < adapter.getData().size(); i++) {
                    ExampleInfo item = adapter.getData().get(i);
                    item.setStrokeWidth(seekbar_stroke.getProgress() / 3);
                    item.setShadowRadius(seekbar_shadow.getProgress() / 3);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        seekbar_stroke.setOnSeekBarChangeListener(seekBarChangeListener);
        seekbar_shadow.setOnSeekBarChangeListener(seekBarChangeListener);
    }

}
