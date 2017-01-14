package com.chiragjain.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.Serializable;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity
{
    private EditText newTodoEditText;
    private DatePicker todoDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        newTodoEditText = (EditText) findViewById(R.id.newTodoEditText);
        todoDatePicker = (DatePicker) findViewById(R.id.addTodoDatePicker);
    }

    public void onAddItem(View view)
    {
        String todoText = newTodoEditText.getText().toString();
        int day = todoDatePicker.getDayOfMonth();
        int month = todoDatePicker.getMonth();
        int year = todoDatePicker.getYear()-1900;
        Log.i("TAG", "Year: " + Integer.toString(year));
        Date todoDate = new Date(year, month, day);
        Todo newTodo = new Todo();
        newTodo.text = todoText;
        newTodo.tododate = todoDate;
        Intent data = new Intent();
        data.putExtra("newTodo", (Serializable) newTodo);
        setResult(RESULT_OK, data);
        finish();
    }
}
