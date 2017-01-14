package com.chiragjain.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditItemActivity extends AppCompatActivity
{

    EditText etEditTodo;
    int todoPos, todoId, tododay, todomonth, todoyear;
    DatePicker dp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Todo edittodo = (Todo) getIntent().getSerializableExtra("edittodo");
        etEditTodo = (EditText) findViewById(R.id.mtEditItem);
        etEditTodo.setText(edittodo.text);
        etEditTodo.setSelection(edittodo.text.length());
        todoPos = getIntent().getIntExtra("todoPos", 0);
        todoId = edittodo.id;
        dp = (DatePicker) findViewById(R.id.todo_date_picker);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dp.updateDate(edittodo.tododate.getYear()+1900, edittodo.tododate.getMonth(), edittodo.tododate.getDate());
    }

    public void onEditItem(View view)
    {
        String editedTodo = etEditTodo.getText().toString();
        Intent data = new Intent();
        tododay = dp.getDayOfMonth();
        todomonth = dp.getMonth();
        todoyear = dp.getYear();
        Todo newtodo = new Todo();
        newtodo.text = editedTodo;
        newtodo.tododate = new Date(todoyear-1900, todomonth, tododay);
        newtodo.id = todoId;
        data.putExtra("editedTodo", (Serializable)newtodo);
        data.putExtra("todoPos", todoPos);
        setResult(RESULT_OK, data);
        finish();
    }
}
