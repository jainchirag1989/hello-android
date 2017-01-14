package com.chiragjain.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    TodoDatabaseHelper mHelper;
    ArrayList<Todo> mTodoItems;
    TodoAdapter mToDoAdapter;
    ListView mLvItems;
    final int EDIT_REQUEST_CODE = 20;
    final int ADD_REQUEST_CODE = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = TodoDatabaseHelper.getInstance(this);
        populateTodos();
        mLvItems = (ListView) findViewById(R.id.lvItems);
        mLvItems.setAdapter(mToDoAdapter);
        mLvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
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
                i.putExtra("edittodo", (Serializable)todo);
                i.putExtra("todoPos", position);
                startActivityForResult(i, EDIT_REQUEST_CODE);
                }
        });
    }

    public void populateTodos()
    {
        readItems();
        mToDoAdapter = new TodoAdapter(this, mTodoItems);
    }

    private void readItems()
    {
        mTodoItems = mHelper.getAllTodos();
    }

    public void onAddItem(View view)
    {
        Intent i = new Intent(MainActivity.this, AddItemActivity.class);
        startActivityForResult(i, ADD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            if (requestCode == EDIT_REQUEST_CODE)
            {
                Todo editedTodo = (Todo) data.getSerializableExtra("editedTodo");
                int editedTodoPos = data.getExtras().getInt("todoPos");
                mTodoItems.set(editedTodoPos, editedTodo);
                mToDoAdapter.notifyDataSetChanged();
                mHelper.updateTodo(editedTodo);
            }
            else if(requestCode == ADD_REQUEST_CODE)
            {
                Todo newTodo = (Todo) data.getSerializableExtra("newTodo");
                newTodo = mHelper.addTodo(newTodo);
                mToDoAdapter.add(newTodo);
                mToDoAdapter.notifyDataSetChanged();
            }
        }
    }
}
