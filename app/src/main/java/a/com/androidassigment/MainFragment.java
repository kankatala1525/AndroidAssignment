package a.com.androidassigment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Objects;

import a.com.androidassigment.adapter.SwipeListAdapter;
import a.com.androidassigment.app.MyApplication;
import a.com.androidassigment.model.RowsItems;
import a.com.androidassigment.utils.Utils;

public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private SwipeListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    List<RowsItems> rowsItemsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);


        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        adapter = new SwipeListAdapter(getActivity());

        final ListView listView = view. findViewById(R.id.listView);
        listView.setAdapter(adapter);


        swipeRefreshLayout.setOnRefreshListener(this);


        /*
          Showing Swipe Refresh animation on activity create
          As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        getDateFromServer();

                                    }
                                }
        );


        return view;
    }

    private void getDateFromServer() {

        if (Utils.isInternetAvailable(getActivity())) {
            getData();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    getActivity()).create();

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
    public void onDestroy() {

        super.onDestroy();
    }

    private void getData() {
        rowsItemsList = new ArrayList<>();
        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);


        // Volley's json array request object
        String url = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());



                        try {

                            // Set title bar
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Objects.requireNonNull(((HomeActivity) getActivity())
                                        .getSupportActionBar()).setTitle(Utils.getJsonString(response, "title"));
                            }else{
                                ((HomeActivity) getActivity())
                                        .getSupportActionBar().setTitle(Utils.getJsonString(response, "title"));
                            }

                            JSONArray jsonArray = response.getJSONArray("rows");
                            // looping through json and adding to list

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

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

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
