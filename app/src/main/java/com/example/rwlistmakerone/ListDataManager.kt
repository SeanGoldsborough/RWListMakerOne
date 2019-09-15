package com.example.rwlistmakerone

import android.content.Context
import android.content.SharedPreferences



class ListDataManager(private val context: Context) {

    val PREF_NAME = "SharedPreferenceExample"

    val tasksArray = ArrayList<TaskList>()

    fun saveList(list: TaskList) {



    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()

        sharedPreferences.putStringSet(list.name, list.tasks.toHashSet())

        sharedPreferences.apply()
        sharedPreferences.commit()

    }

    fun readLists(): ArrayList<TaskList> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val sharedPreferenceContents = sharedPreferences.all
        val taskLists = ArrayList<TaskList>()

        for (taskList in sharedPreferenceContents) {
            val itemsHashSet = ArrayList(taskList.value as HashSet<String>)
            val list = TaskList(taskList.key, itemsHashSet)

            taskLists.add(list)

            tasksArray.add(list)

        }
        println("tasklists.size is:")
        println(taskLists.size)
        return taskLists
    }

    fun removeFromLists(taskListToDelete: TaskList): ArrayList<TaskList> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val sharedPreferenceContents = sharedPreferences.all
        val taskLists = ArrayList<TaskList>()

        taskLists.remove(taskListToDelete)
       // taskLists.removeAt(position.)
        //taskLists.remove(position)

//        for (taskList in sharedPreferenceContents) {
//            val itemsHashSet = ArrayList(taskList.value as HashSet<String>)
//            val list = TaskList(taskList.key, itemsHashSet)
//
//            taskLists.add(list)
//        }
        return taskLists
    }
}