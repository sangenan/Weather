package com.example.sangenan.weather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WeatherAnimView weather;
    private GyroscopeObserver gyroscopeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weather = (WeatherAnimView) findViewById(R.id.weather);
        gyroscopeObserver = new GyroscopeObserver();
        weather.setGyroscopeObserver(gyroscopeObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gyroscopeObserver.register(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        gyroscopeObserver.unregister();

    }
}
