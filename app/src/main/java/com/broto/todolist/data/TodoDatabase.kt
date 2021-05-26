package com.broto.todolist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.broto.todolist.data.models.ToDoData

@Database(entities = [ToDoData::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun getTodoDAO(): TodoDAO

    companion object {

        @Volatile
        private var mInstance: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase {
            synchronized(this) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "todo_database"
                    ).build()
                }
                return mInstance!!
            }
        }
    }
}