package a.com.androidassigment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import a.com.androidassigment.adapter.SwipeListAdapter;
import a.com.androidassigment.app.MyApplication;
import a.com.androidassigment.model.RowsItems;
import a.com.androidassigment.utils.Utils;

public class HomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private SwipeListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String url = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json";
    List<RowsItems> rowsItemsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        adapter = new SwipeListAdapter(HomeActivity.this);

        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);


        swipeRefreshLayout.setOnRefreshListener(this);


        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        getDateFromServer();

                                    }
                                }
        );


    }


    private void getDateFromServer() {

        if (Utils.isInternetAvailable(HomeActivity.this)) {
            getData();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    HomeActivity.this).create();

            // Setting Dialog Title
            alertDialog.setTitle(getResources().getString(R.string.no_internet));

            // Setting Dialog Message
            alertDialog.setMessage(getResources().getString(R.string.no_internet_msg));

            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    swipeRefreshLayout.setRefreshing(false);
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private void getData() {
        rowsItemsList = new ArrayList<>();
        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);


        // Volley's json array request object
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());


                        // looping through json and adding to movies list

                        try {

                            getSupportActionBar().setTitle(Utils.getJsonString(response, "title"));

                            JSONArray jsonArray = response.getJSONArray("rows");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject movieObj = jsonArray.getJSONObject(i);

                                    RowsItems rowsItems = new RowsItems(Utils.getJsonString(movieObj, "title"), Utils.getJsonString(movieObj, "description"), Utils.getJsonString(movieObj, "imageHref"));

                                    rowsItemsList.add(rowsItems);
                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }
                            }

                            adapter.setRows(rowsItemsList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server Error: " + error.getMessage());

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req);
    }

    @Override
    public void onRefresh() {
        getDateFromServer();
    }
}
