package com.chiragjain.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;

public class EditItemActivity extends AppCompatActivity
{

    EditText etEditTodo;
    int todoPos, todoId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String todoText = getIntent().getStringExtra("todoText");
        etEditTodo = (EditText) findViewById(R.id.mtEditItem);
        etEditTodo.setText(todoText);
        etEditTodo.setSelection(todoText.length());
        todoPos = getIntent().getIntExtra("todoPos", 0);
        todoId = getIntent().getIntExtra("todoId", 0);
    }

    public void onEditItem(View view)
    {
        String editedTodo = etEditTodo.getText().toString();
        Intent data = new Intent();

        data.putExtra("editedTodo", editedTodo);
        data.putExtra("todoPos", todoPos);
        data.putExtra("todoId", todoId);
        setResult(RESULT_OK, data);
        finish();
    }
}
