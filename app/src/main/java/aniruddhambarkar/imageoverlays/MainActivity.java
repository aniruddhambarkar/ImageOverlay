package aniruddhambarkar.imageoverlays;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.aniruddhambarkar.ImageOverlay;
import com.aniruddhambarkar.PointView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageOverlay imageOverlay;
    ArrayList<PointView> pointViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageOverlay = findViewById(R.id.imageOverlay);

    }

    public void onViewClick(View view) {

        switch (view.getId()) {
            case R.id.btnClear:
                imageOverlay.clearOverlays();
                break;
            case R.id.btnSave:

                pointViews.clear();
                pointViews.addAll(imageOverlay.getPointViews());

                break;
            case R.id.btnSet:
                imageOverlay.setPointViews(pointViews);
                break;
        }

    }
}
