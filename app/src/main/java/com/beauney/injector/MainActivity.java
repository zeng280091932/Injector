package com.beauney.injector;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.beauney.injector.library.annotation.ContentView;
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
}
