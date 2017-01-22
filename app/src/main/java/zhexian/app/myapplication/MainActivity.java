package zhexian.app.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    ParallelViewHelper parallelViewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        parallelViewHelper = new ParallelViewHelper(this, findViewById(R.id.main_image_background));
    }

    @Override
    protected void onResume() {
        super.onResume();
        parallelViewHelper.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        parallelViewHelper.stop();
    }
}
