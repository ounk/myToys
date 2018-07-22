package com.example.ounk.challenge;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int TYPE_Section=1;
    public static final int TYPE_Node=2;
    public static final int TYPE_Link=3;

    public static final String STARTSITE="https://www.mytoys.de";

    public static final String NAVI_Start="navigationEntries";
    public static final String NAVI_Type="type";
    public static final String NAVI_Label="label";
    public static final String NAVI_Section="section";
    public static final String NAVI_Node="node";
    public static final String NAVI_Children="children";
    public static final String NAVI_Url="url";

    private DrawerLayout mDrawerLayout;
    private ArrayList<Navigation> navList = new ArrayList<>();
    private List<Integer> prev = new ArrayList<>();
    private JSONArray jsonNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        final WebView webView = findViewById(R.id.wbToys);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(STARTSITE);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        try {
            JSONObject jsonObj = new JSONObject(ServiceHandler.LoadJson());
            jsonNav = jsonObj.getJSONArray(NAVI_Start);
            navList = AddEntry(navList, jsonNav);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final NavigationAdapter nAdapter = new NavigationAdapter(this, navList);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(nAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Navigation currentNavItem = (Navigation) adapterView.getItemAtPosition(i);
                switch (currentNavItem.getType()) {
                    case TYPE_Section:
                        break;
                    case TYPE_Node:
                        navList.clear();
                        navList = AddEntry(navList, currentNavItem.getChildren());
                        nAdapter.notifyDataSetChanged();
                        TextView textView = findViewById(R.id.drawer_header);
                        textView.setText(currentNavItem.getName());
                        (findViewById(R.id.back)).setVisibility(View.VISIBLE);
                        setPrev(i);
                        break;
                    case TYPE_Link:
                        webView.loadUrl(currentNavItem.getLink());
                        mDrawerLayout.closeDrawers();
                        break;
                }
            }
        });

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBack(nAdapter);
            }
        });

    }

    public ArrayList<Navigation> AddEntry(ArrayList<Navigation> navList, JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject c = jsonArray.getJSONObject(i);

                Navigation currentNavItem = new Navigation(0, c.getString(NAVI_Label), null, null);
                switch (c.getString(NAVI_Type)) {
                    case NAVI_Section:
                        currentNavItem.setType(TYPE_Section);
                        break;
                    case NAVI_Node:
                        currentNavItem.setType(TYPE_Node);
                        currentNavItem.setChildren(c.getJSONArray(NAVI_Children));
                        break;
                    default:
                        currentNavItem.setType(TYPE_Link);
                        currentNavItem.setLink(c.getString(NAVI_Url));
                }
                navList.add(currentNavItem);
                if (currentNavItem.getType() == 1)
                    navList = AddEntry(navList, c.getJSONArray(NAVI_Children));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return navList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setPrev(int n) {
        int sum = 0;
        int sumBefore = 0;
        if (prev.isEmpty()) {
            try {
                for (int i = 0; sum + i < n; i++) {
                    sumBefore = sum + i + 1;
                    sum = sum + jsonNav.getJSONObject(i).getJSONArray(NAVI_Children).length();

                    if (sum + i >= n)
                        prev.add(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            prev.add(n - sumBefore);
        } else {
            prev.add(n);
        }
    }

    public void getBack(NavigationAdapter nAdapter) {
        try {
            //reset
            if (prev.size() <= 2) {
                navList.clear();
                navList = AddEntry(navList, jsonNav);
                nAdapter.notifyDataSetChanged();
                prev.clear();
                (findViewById(R.id.back)).setVisibility(ImageButton.GONE);
                TextView textView = findViewById(R.id.drawer_header);
                textView.setText(R.string.app_name);
            } else {
                JSONArray jArr = jsonNav;
                JSONObject jOb = jArr.getJSONObject(prev.get(0));

                for (int i = 0; i < prev.size() - 1; i++) {
                    jOb = jArr.getJSONObject(prev.get(i));
                    jArr = jOb.getJSONArray(NAVI_Children);
                }
                navList.clear();
                navList = AddEntry(navList, jArr);
                nAdapter.notifyDataSetChanged();
                TextView textView = findViewById(R.id.drawer_header);
                textView.setText(jOb.getString(NAVI_Label));
                prev.remove(prev.size() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close(View v) {
        mDrawerLayout.closeDrawers();
    }
}
