package com.chiragjain.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jchirag on 1/3/17.
 */
public class TodoDatabaseHelper extends SQLiteOpenHelper
{
    // Database Info
    private static final String DATABASE_NAME = "todoDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TODO = "todos";
    private static final String KEY_ID = "id";
    private static final String KEY_TODO_TEXT = "text";
    private final String TAG = this.getClass().getSimpleName();

    private static TodoDatabaseHelper sInstance;

    private TodoDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized TodoDatabaseHelper getInstance(Context context)
    {
        if (sInstance == null)
        {
            sInstance = new TodoDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_TODO_TEXT + " TEXT" +
                ")";

        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion != newVersion)
        {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            onCreate(db);
        }
    }

    public void addTodo(String todo)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try
        {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_TEXT, todo);

            db.insertOrThrow(TABLE_TODO, null, values);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error while trying to add todo to database");
        }
        finally
        {
            db.endTransaction();
        }
    }

    public List<String> getAllTodos()
    {
        List<String> todos = new ArrayList<>();
        String TODOS_SELECT_QUERY = String.format("Select * from %s", TABLE_TODO);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TODOS_SELECT_QUERY, null);
        try
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    todos.add(cursor.getString(cursor.getColumnIndex(KEY_TODO_TEXT)));
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error while trying to get todos from database");
        }
        finally
        {
            if (cursor != null && !cursor.isClosed())
            {
                cursor.close();
            }
        }
        return todos;
    }

    public void deleteTodo(String todo)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try
        {
            String DELETE_TODO_QUERY =
                    String.format("DELETE FROM %s WHERE %s = '%s';", TABLE_TODO, KEY_TODO_TEXT, todo);
            db.execSQL(DELETE_TODO_QUERY);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error while trying to delete todo: " + todo);
        }
        finally
        {
            db.endTransaction();
        }
    }

    public void updateTodo(String oldTodo, String newTodo)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try
        {
            String UPDATE_TODO_QUERY =
                    String.format("UPDATE %s set %s='%s' where %s='%s';", TABLE_TODO, KEY_TODO_TEXT, newTodo,
                            KEY_TODO_TEXT,
                            oldTodo);
            db.execSQL(UPDATE_TODO_QUERY);
            db.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error while trying to update todo: " + oldTodo);
        }
        finally
        {
            db.endTransaction();
        }
    }
}
