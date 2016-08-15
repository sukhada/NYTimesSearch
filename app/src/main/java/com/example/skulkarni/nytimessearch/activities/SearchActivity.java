package com.example.skulkarni.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;


import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.skulkarni.nytimessearch.Article;
import com.example.skulkarni.nytimessearch.ArticleArrayAdapter;
import com.example.skulkarni.nytimessearch.EndlessRecyclerViewScrollListener;
import com.example.skulkarni.nytimessearch.R;
import com.example.skulkarni.nytimessearch.Filter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import com.example.skulkarni.nytimessearch.activities.FilterFragment.FilterDialogListener;

import android.app.DialogFragment;

public class SearchActivity extends ActionBarActivity implements FilterDialogListener {

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;
    int page = 0;
    String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

    RequestParams p;
    Filter f = new Filter();
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Enter search query here!");
        setSupportActionBar(toolbar);
        setupViews();
    }

    public void setupViews(){
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        RecyclerView rvArticles = (RecyclerView) findViewById(R.id.rvArticles);
        rvArticles.setAdapter(adapter);
        StaggeredGridLayoutManager s = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        rvArticles.setLayoutManager(s);
        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(s) {
            @Override
            public void onLoadMore(int pg, int totalItemsCount) {
                p.put("page", page++);
                makeAPICall(url, p);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                articles.clear();
                adapter.notifyDataSetChanged();
                if (p == null) {
                    RequestParams params = new RequestParams();
                    params.put("api-key", "fe2089cd1c5c48e89846f7ea3bc4fe9c");
                    params.put("page", page);
                    p = params;

                }

                p.put("q", query);
                makeAPICall(url, p);

                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        ActionBarActivity self = this;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentManager fm = this.getSupportFragmentManager();
            FilterFragment fdf = FilterFragment.newInstance(f);
            fdf.show(fm, "filter");
        }
        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();

    }

    public void makeAPICall(String url, RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("DEBUG", params.toString());
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onFinishDialog(Filter newFilter) {
        f = newFilter;
        RequestParams params = new RequestParams();
        String sort = "newest";
        p.put("begin_date", f.getFormattedBeginDate());
        if (f.getSortOrder()) {
            sort = "oldest";
        }
        p.put("sort", sort);
        p.put("fq", "news_desk:(" + f.getNewsDesk().trim() + ")");
        articles.clear();
        adapter.notifyDataSetChanged();
        makeAPICall(url, p);

    }
}
