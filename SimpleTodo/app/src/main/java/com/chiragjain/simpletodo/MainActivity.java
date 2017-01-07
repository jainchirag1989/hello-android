package com.chiragjain.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    TodoDatabaseHelper mHelper;
    ArrayList<Todo> mTodoItems;
    TodoAdapter mToDoAdapter;
    ListView mLvItems;
    EditText mEtEditText;
    final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = TodoDatabaseHelper.getInstance(this);
        populateTodos();
        mLvItems = (ListView)findViewById(R.id.lvItems);
        mLvItems.setAdapter(mToDoAdapter);
        mEtEditText = (EditText)findViewById(R.id.etEditText);
        mLvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Todo todo = mTodoItems.get(position);
                mTodoItems.remove(position);
                mToDoAdapter.notifyDataSetChanged();
                mHelper.deleteTodo(todo);
                return true;
            }
        });

        mLvItems.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Todo todo = mTodoItems.get(position);
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("todoText", todo.text);
                i.putExtra("todoPos", position);
                i.putExtra("todoId", todo.id);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    public void populateTodos() {
        readItems();
        mToDoAdapter = new TodoAdapter(this, mTodoItems);
    }

    private void readItems() {
        mTodoItems = mHelper.getAllTodos();
    }

    public void onAddItem(View view)
    {
        String todoText = mEtEditText.getText().toString();
        Todo todo = mHelper.addTodo(todoText);
        mToDoAdapter.add(todo);
        mEtEditText.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String editedTodoText = data.getExtras().getString("editedTodo");
            int editedTodoPos = data.getExtras().getInt("todoPos");
            int editedTodoId = data.getExtras().getInt("todoId");
            Todo newtodo = new Todo();
            newtodo.id = editedTodoId;
            newtodo.text = editedTodoText;
            mTodoItems.set(editedTodoPos, newtodo);
            mToDoAdapter.notifyDataSetChanged();
            mHelper.updateTodo(editedTodoId, editedTodoText);
        }
    }
}
