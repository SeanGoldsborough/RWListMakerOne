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
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener {

    private val TAG = MainActivity::class.java.simpleName

    lateinit var listsRecyclerView: RecyclerView

    val listDataManager: ListDataManager = ListDataManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val lists = listDataManager.readLists()

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

        //1
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder1: RecyclerView.ViewHolder): Boolean {
                //2
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                //3
                val position = viewHolder.adapterPosition
                val pref : SharedPreferences = getApplicationContext().getSharedPreferences("E", Context.MODE_PRIVATE)

                val editor = pref.edit()


                val list = listDataManager

                val allLists = list.readLists()
                    //list.removeFromLists(position)
               allLists.removeAt(position)

                //allLists.removeAt(0)
               // (allLists.count() - 3)
                val arrayCount = allLists.count()
                //val listCount = list.count()
                Log.d(TAG, "onCreate called. Score is: $arrayCount & $position)")

                val recyclerView = listsRecyclerView
                val recyclerAdapter = recyclerView.adapter
                    //listsRecyclerView.adapter as ListSelectionRecyclerViewAdapter

                recyclerAdapter!!.notifyItemRemoved(position)
               // recyclerView.adapter!!.notifyItemRemoved(position)
              // editor.remove("position")
                //allLists.removeAt(0)
                editor.commit()
            }
        }

        //4
        //val recyclerAdapter = listsRecyclerView.adapter as ListSelectionRecyclerViewAdapter
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(listsRecyclerView)
        val pref : SharedPreferences = getApplicationContext().getSharedPreferences("E", Context.MODE_PRIVATE)

        val editor = pref.edit()
        editor.commit()
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
