package net.gogetout.go;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import net.gogetout.go.library.DatabaseHandler;


public class SplashScreenActivity extends ActionBarActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Thread splashTimerThread = new Thread() {

            public void run() {
                try {
                    DatabaseHandler DH = new DatabaseHandler(getBaseContext());
                    if (DH.getRowCount() > 0) {
                        Intent LE = new Intent(getApplicationContext(),
                                MainActivity.class);
                        LE.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(LE);
                    } else {
                        setContentView(R.layout.activity_splash_screen);
                        sleep(SPLASH_TIME_OUT);
                        Intent LI = new Intent(getApplicationContext(),
                                LoginActivity.class);
                        LI.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(LI);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    finish();
                }

            }

        };

        splashTimerThread.start();
    }
}