package co.tink.todoapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.daimajia.swipe.util.Attributes;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

import co.tink.todoapp.R;
import co.tink.todoapp.adapter.AdapterTodo;
import co.tink.todoapp.item.Item;

public class MainActivity extends AppCompatActivity {

  private FloatingActionButton fab;
  private RecyclerView recycler;
  private LinearLayout inputs;
  private EditText editText;

  private AdapterTodo adapter;
  private DB snappydb;

  private List<Item> list = new ArrayList<>();

  private boolean inEditMode = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
    fab();
    recycler();

  }

  private void initViews() {
    fab = (FloatingActionButton) findViewById(R.id.fab);
    recycler = (RecyclerView) findViewById(R.id.recycler);
    inputs = (LinearLayout) findViewById(R.id.inputs);
    editText = (EditText) findViewById(R.id.edit_text);
  }

  private void fab() {
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!inEditMode) {
          fab.setSize(FloatingActionButton.SIZE_MINI);
          inputs.setVisibility(View.VISIBLE);
          editText.requestFocus();
          imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        } else {
          fab.setSize(FloatingActionButton.SIZE_NORMAL);
          inputs.setVisibility(View.GONE);
          fab.requestFocus();
          addNewItem();
          imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        inEditMode = !inEditMode;
      }
    });
  }

  private void recycler() {
    getTodoList();

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

    adapter = new AdapterTodo(this, list);
    recycler.setLayoutManager(linearLayoutManager);
    adapter.setMode(Attributes.Mode.Single);
    recycler.setAdapter(adapter);
  }

  public void getTodoList() {
    try {
      snappydb = DBFactory.open(this);
      list = snappydb.get("list", ArrayList.class);
      snappydb.close();
    } catch (SnappydbException e) {

    }
  }

  private void addNewItem() {

    list.add(new Item(
        editText.getText().toString(),
        false,
        0
    ));

    adapter.notifyDataSetChanged();
    saveList();

  }

  private void saveList() {
    try {
      snappydb = DBFactory.open(this);
      snappydb.put("list", list);
      snappydb.close();
    } catch (SnappydbException e) {

    }
  }

  public void removeItem(int position) {
    list.remove(position);
    saveList();
  }

  public void setItem(int position, Item item) {
    list.set(position, item);
    saveList();
  }
}
