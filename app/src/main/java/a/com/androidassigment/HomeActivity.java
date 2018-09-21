package a.com.androidassigment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FragmentManager myfragmentManager = getFragmentManager ();
        FragmentTransaction myfragmentTransaction = myfragmentManager.beginTransaction ();

        MainFragment myfragment = new MainFragment();

        myfragmentTransaction.add(R.id.fragment_frame, myfragment);

        /*You've to create a frame layout or any other layout with id inside your activity layout and then use that id in java*/

        myfragmentTransaction.commit();


    }

}
