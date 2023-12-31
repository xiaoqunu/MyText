package com.example.mytext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mytext.adapter.MyAdapter;
import com.example.mytext.bean.Note;
import com.example.mytext.util.SpfUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mBtnAdd;
    private List<Note> mNotes;
    private MyAdapter mMyAdapter;

    private NoteDbOpenHelper mNoteDbOpenHelper;

    public static final int MODE_LINEAR = 0;
    public static final int MODE_GRID = 1;
    public static final String KEY_LAYOUT_MODE = "key_layout_mode";
    private int currentListLayoutMode = MODE_LINEAR;    //保存当前设定的布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initEvent();

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDataFromDb();
        setListLayout();
    }

    private void setListLayout() {
        currentListLayoutMode = SpfUtil.getIntWithDefault(this, KEY_LAYOUT_MODE, MODE_LINEAR);
        if (currentListLayoutMode == MODE_LINEAR) {
            setToLinearList();
        }else {
            setToGridList();
        }
    }

    private void refreshDataFromDb() {
        mNotes = getDataFromDb();
        mMyAdapter.refreshData(mNotes);
    }

    private void initEvent() {
        mMyAdapter = new MyAdapter(this, mNotes);

        mRecyclerView.setAdapter(mMyAdapter);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mMyAdapter.setViewType(mMyAdapter.TYPE_LINEAR_LAYOUT);

        setListLayout();

    }

    private void initData() {
        mNotes = new ArrayList<>();
        mNoteDbOpenHelper = new NoteDbOpenHelper(this);
//        for(int i = 1; i <= 30;i++) {
//            Note note = new Note();
//            note.setTitle("标题" + i + "测试");
//            note.setContent("内容" + i + "测试");
//            note.setCreatedTime(getCurrentTimeFormat());
//            mNotes.add(note);
//        }

//        mNotes = getDataFromDb();

    }

    private List<Note> getDataFromDb() {

        return mNoteDbOpenHelper.queryAllFromDb();

    }

    private void initView() {
        mRecyclerView = findViewById(R.id.rlv);
    }

    public void add(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_research).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mNotes = mNoteDbOpenHelper.queryFromDbByTitle(newText);
                mMyAdapter.refreshData(mNotes);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        //通过if-else语句实现布局切换
        int itemId = item.getItemId();
        if (itemId == R.id.menu_linear) {

            setToLinearList();

            currentListLayoutMode = MODE_LINEAR;
            SpfUtil.saveInt(this, KEY_LAYOUT_MODE, MODE_LINEAR);

            return true;
        } else if (itemId == R.id.menu_grid) {

            setToGridList();

            currentListLayoutMode = MODE_GRID;
            SpfUtil.saveInt(this, KEY_LAYOUT_MODE, MODE_GRID);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setToLinearList() {
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mMyAdapter.setViewType(MyAdapter.TYPE_LINEAR_LAYOUT);
        mMyAdapter.notifyDataSetChanged();
    }

    private void setToGridList() {
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMyAdapter.setViewType(MyAdapter.TYPE_GRID_LAYOUT);
        mMyAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (currentListLayoutMode == MODE_LINEAR){
            MenuItem item = menu.findItem(R.id.menu_linear);
            item.setChecked(true);
        }else {
            menu.findItem(R.id.menu_grid).setChecked(true);
        }

        return super.onPrepareOptionsMenu(menu);

    }
}