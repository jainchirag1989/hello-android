package com.chiragjain.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    TodoDatabaseHelper mHelper;
    List<String> mTodoItems;
    ArrayAdapter<String> mToDoAdapter;
    ListView mLvItems;
    EditText mEtEditText;
    final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = TodoDatabaseHelper.getInstance(this);
        populateArrayItems();
        mLvItems = (ListView)findViewById(R.id.lvItems);
        mLvItems.setAdapter(mToDoAdapter);
        mEtEditText = (EditText)findViewById(R.id.etEditText);
        mLvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String todoText = mTodoItems.get(position);
                mTodoItems.remove(position);
                mToDoAdapter.notifyDataSetChanged();
                mHelper.deleteTodo(todoText);
                return true;
            }
        });

        mLvItems.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String todoText = mTodoItems.get(position);
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("todoText", todoText);
                i.putExtra("todoPos", position);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    public void populateArrayItems() {
        readItems();
        mToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTodoItems);
    }

    private void readItems() {
        mTodoItems = mHelper.getAllTodos();
    }

    /*
    private void writeItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {

        }
    }*/


    public void onAddItem(View view)
    {
        String todoText = mEtEditText.getText().toString();
        mToDoAdapter.add(todoText);
        mEtEditText.setText("");
        mHelper.addTodo(todoText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String editedTodoText = data.getExtras().getString("editedTodo");
            int editedTodoPos = data.getExtras().getInt("todoPos");
            String oldTodoText = mTodoItems.get(editedTodoPos);
            mTodoItems.set(editedTodoPos, editedTodoText);
            mToDoAdapter.notifyDataSetChanged();
            mHelper.updateTodo(oldTodoText, editedTodoText);
        }
    }
}
