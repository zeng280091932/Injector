package com.beauney.injector;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beauney.injector.library.annotation.ContentView;
import com.beauney.injector.library.annotation.OnClick;
import com.beauney.injector.library.annotation.OnLongClick;
import com.beauney.injector.library.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.text)
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Debug", "textView-------->" + textView);
    }

    @OnClick({R.id.click})
    public void onClick(View v) {
        Toast.makeText(this, "点击事件触发", Toast.LENGTH_SHORT).show();
    }

    @OnLongClick({R.id.longClick})
    public boolean onLongClick(View v) {
        Toast.makeText(this, "长按事件触发", Toast.LENGTH_SHORT).show();
        return false;
    }
}
