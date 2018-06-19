package com.a10llip0p.android.soso

import android.app.Fragment
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.a10llip0p.android.soso.items.ItemContent
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*

import kotlinx.android.synthetic.main.item_list.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity(), View.OnClickListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var mTwoPane: Boolean = false
    private val items = listOf("tomato", "eggplant", "piment", "water")
    private var tomatoButton: Button? = null
    private var pimentButton: Button? = null
    private var eggplantButton: Button? = null
    private var waterButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        tomatoButton = findViewById<Button>(R.id.tomato)
        pimentButton = findViewById<Button>(R.id.piment)
        eggplantButton = findViewById<Button>(R.id.eggplant)
        waterButton = findViewById<Button>(R.id.water)

        tomatoButton!!.setOnClickListener(this)
        pimentButton!!.setOnClickListener(this)
        eggplantButton!!.setOnClickListener(this)
        waterButton!!.setOnClickListener(this)

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true
        }

        //setupRecyclerView(item_list)
    }

    override fun onClick(p0: View?) {
        val itemId = items.indexOfFirst {it == baseContext.resources.getResourceEntryName(p0!!.id)}.toString()
        if (itemId != "-1") {
            if (mTwoPane) {
                when (itemId) {
                    "3" -> {
                        val fragment = WaterFragment().apply {
                            arguments = Bundle().apply {
                                putString(WaterFragment.ARG_ITEM_ID, itemId)
                            }
                        }
                        supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit()
                    }
                    else -> {
                        val fragment = TabFragment().apply {
                            arguments = Bundle().apply {
                                putString(TabFragment.ARG_ITEM_ID, itemId)
                            }
                        }
                        supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit()
                    }
                }
            } else {
                val intent = Intent(this, ItemDetailActivity::class.java).apply {
                    putExtra(TabFragment.ARG_ITEM_ID, itemId)
                }
                startActivity(intent)
            }
        }
    }

    /*
    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, ItemContent.ITEMS, mTwoPane)
    }

    class SimpleItemRecyclerViewAdapter(private val mParentActivity: ItemListActivity,
                                        private val mValues: List<ItemContent.Item>,
                                        private val mTwoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val mOnClickListener: View.OnClickListener

        init {
            mOnClickListener = View.OnClickListener { v ->
                val item = v.tag as ItemContent.Item
                if (mTwoPane) {
                    if (item.content == "水") {
                        val fragment = WaterFragment().apply {
                            arguments = Bundle().apply {
                                putString(WaterFragment.ARG_ITEM_ID, item.id)
                            }
                        }
                        mParentActivity.supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit()
                    } else {
                        val fragment = TabFragment().apply {
                            arguments = Bundle().apply {
                                putString(TabFragment.ARG_ITEM_ID, item.id)
                            }
                        }
                        mParentActivity.supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit()
                    }
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(TabFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = mValues[position]
            holder.mIdView.text = item.id
            holder.mContentView.text = item.content

            with(holder.itemView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
        }

        override fun getItemCount(): Int {
            return mValues.size
        }

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
            val mIdView: TextView = mView.id_text
            val mContentView: TextView = mView.content
        }
    }
        */
}
