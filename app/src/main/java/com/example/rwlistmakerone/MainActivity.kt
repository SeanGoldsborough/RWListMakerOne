package com.example.rwlistmakerone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import java.nio.file.Files.find

class MainActivity : AppCompatActivity(), ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener {
//
//    private var listSelectionFragment: ListSelectionFragment = ListSelectionFragment()
//    private var fragmentContainer: FrameLayout? = null

    private val TAG = MainActivity::class.java.simpleName

    lateinit var listsRecyclerView: RecyclerView

    val listDataManager: ListDataManager = ListDataManager(this)

    val array = ArrayList<TaskList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val PREF_NAME = "SharedPreferenceExample"

        val pref : SharedPreferences = getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//        val editor = pref.edit()
//        editor.clear()

        val lists = listDataManager.readLists()
        array.addAll(lists)

        listsRecyclerView = findViewById<RecyclerView>(R.id.lists_recyclerview)
        listsRecyclerView.layoutManager = LinearLayoutManager(this)
        //listsRecyclerView.layoutManager = GridLayoutManager(this, 2)
        listsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(lists, this)


        setRecyclerViewItemTouchListener()

        fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
             //   .setAction("Action", null).show()

            showCreateListDialog()
        }

//        fragmentContainer = findViewById(R.id.fragment_container)
//        supportFragmentManager.beginTransaction()
//            .add(R.id.fragment_container, listSelectionFragment)
//            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LIST_DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                listDataManager.saveList(data.getParcelableExtra(INTENT_LIST_KEY))

                updateLists()
            }
        }
    }

    private fun updateLists() {
        val lists = listDataManager.readLists()

        listsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(lists, this)

    }

    private fun showCreateListDialog() {
        val dialogTitle = getString(R.string.name_of_list)
        val positiveButtonTitle = getString(R.string.create_list)

        val builder = AlertDialog.Builder(this)
        val listTitleEditText = EditText(this)
        listTitleEditText.inputType = InputType.TYPE_CLASS_TEXT



        builder.setTitle(dialogTitle)
        builder.setView(listTitleEditText)

        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            val list = TaskList(listTitleEditText.text.toString())
            listDataManager.saveList(list)

            val recyclerAdapter = listsRecyclerView.adapter as ListSelectionRecyclerViewAdapter
                recyclerAdapter.addList(list)

            dialog.dismiss()
            showListDetail(list)

        }
        builder.create().show()

    }

    private fun setRecyclerViewItemTouchListener() {

        val lists = listDataManager.tasksArray
        val allLists = ArrayList<TaskList>()
        allLists.addAll(lists)
        //1
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder1: RecyclerView.ViewHolder): Boolean {
                //2
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                //3
                val position = viewHolder.adapterPosition


                if (swipeDir == ItemTouchHelper.LEFT) {  //if swipe left

                    val tmp = lists[position] //temporary variable
                    val PREF_NAME = "SharedPreferenceExample"

                    println(tmp)
                    println(tmp.name)
                    //lists.removeAt(position)
                  //  Log.d(TAG, "array.itemCount is: (${lists.size})")


                    // listsRecyclerView.removeViewAt(position)
//                    listsRecyclerView.adapter!!.notifyItemRemoved(position)



                    val pref : SharedPreferences = getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    //println(pref.getString(R.string.name_of_list,""))

                    val editor = pref.edit()
                    editor.remove(tmp.name)
                   // editor.remove(getString(R.string.name_of_list))
                   // editor.commit()

                    //listDataManager.removeFromLists(tmp)
                    editor.apply()

                   updateLists()

                    var RVItemCount = listsRecyclerView.adapter!!.itemCount
                    Log.d(TAG, "recyclerAdapter.itemCount is: ($RVItemCount)")
                    //allLists.removeAt(position)


                    //listsRecyclerView.adapter!!.notifyItemRemoved(position)

                    //RVItemCount = RVItemCount - 1


                    Log.d(TAG, "recyclerAdapter.itemCount is: ($RVItemCount)")





                    //Log.d(TAG, "array.itemCount is: (${lists.size})")







                    Log.d(TAG, "recyclerAdapter.itemCount is: ($RVItemCount)")

                }

//                val allLists2 = listDataManager.readLists()
//
//                println("print works and says:")
//                println(allLists2.size)
//                allLists2.removeAt(position)
//                println("print works and says:")
//                println(allLists2.size)
//                listDataManager.removeFromLists(allLists2[position], allLists2)
//                println("print works and says:")
//                println(allLists2.size)
//
//               // updateLists()
//                val pref : SharedPreferences = getApplicationContext()
//                    .getSharedPreferences("E", Context.MODE_PRIVATE)
//
//
//
//                val editor = pref.edit()
//                editor.remove(allLists2[position].name)
//
//
//                val list = listDataManager
//
//                val allLists = list.readLists()
//                   // list.removeFromLists(position)
//               //allLists.removeAt(position)
//
//                //allLists.removeAt(0)
//               // (allLists.count() - 3)
//                val arrayCount = allLists.count()
//                //val listCount = list.count()
//                Log.d(TAG, "swipe to delete called. array count is: $arrayCount & position is: $position and array list val is: $allLists[position])")
//                println("print works and says:")
//                println(allLists[position])
//
//                val itemToRemove = allLists[position]
//
//                allLists.remove(itemToRemove)
//
//                list.removeFromLists(itemToRemove)
//
//                Log.d(TAG, "swipe to delete called. array count is: $arrayCount & position is: $position and array list val is: $allLists[position])")
//
//               // val recyclerView = listsRecyclerView
//                //val recyclerAdapter = recyclerView.adapter
//                val recyclerAdapter = listsRecyclerView.adapter as ListSelectionRecyclerViewAdapter
//
//                val RVItemCount = recyclerAdapter!!.itemCount
//                Log.d(TAG, "recyclerAdapter.itemCount is: ($RVItemCount)")
//                recyclerAdapter.removeList(allLists2[position])
//                recyclerAdapter!!.notifyItemRemoved(position)
//                recyclerAdapter!!.notifyDataSetChanged()
//
//                Log.d(TAG, "recyclerAdapter.itemCount is Now: ($RVItemCount)")
//               // recyclerView.adapter!!.notifyItemRemoved(position)
//              // editor.remove("position")
//                //allLists.removeAt(0)
//                //updateLists()
//                //editor.commit()
//                editor.clear()
//                editor.remove(allLists2[position].name)
//                editor.apply()
            }
        }

        //4
        //val recyclerAdapter = listsRecyclerView.adapter as ListSelectionRecyclerViewAdapter
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(listsRecyclerView)
        val pref : SharedPreferences = getApplicationContext().getSharedPreferences("SharedPreferenceExample", Context.MODE_PRIVATE)


//        val editor = pref.edit()
//        editor.commit()
    }



    private fun showListDetail(list: TaskList) {
        val listDetailIntent = Intent(this, ListDetailActivity::class.java)
        //listDetailIntent.putExtra(INTENT_LIST_KEY, list)
        listDetailIntent.putExtra(INTENT_LIST_KEY, list)
        startActivityForResult(listDetailIntent, LIST_DETAIL_REQUEST_CODE)

    }

    override fun listItemClicked(list: TaskList) {
        showListDetail(list)
    }
    companion object {
        const val INTENT_LIST_KEY = "list"
        const val LIST_DETAIL_REQUEST_CODE = 123
    }
}
