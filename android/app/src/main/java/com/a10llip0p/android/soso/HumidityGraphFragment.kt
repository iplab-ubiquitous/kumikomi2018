package com.a10llip0p.android.soso

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.nio.channels.FileLock
import kotlin.math.log


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HumidityGraphFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HumidityGraphFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HumidityGraphFragment : Fragment(), OnChartValueSelectedListener {

    private var mListener: OnFragmentInteractionListener? = null

    private var vegetable: Vegetable? = null
    private var mChart: LineChart? = null
    private var chIndex = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let {
            vegetable = it.getSerializable(ARG_VEG) as Vegetable
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_humidity_graph, container, false)

        mChart = view.findViewById(R.id.line_chart)
        mChart!!.setOnChartValueSelectedListener(this)
        mChart!!.setTouchEnabled(true)
        mChart!!.setDragEnabled(true)
        mChart!!.setScaleEnabled(true)
        mChart!!.setPinchZoom(true)
        var data = LineData()
        mChart!!.data = data
        val xl = mChart!!.xAxis
        xl.textColor = Color.BLACK
        val leftAxis = mChart!!.axisLeft
        leftAxis.textColor = Color.BLACK
        leftAxis.axisMaximum = 100f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(true)

        vegetable?.let {
            it.db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val testText = view.findViewById<TextView>(R.id.testId2)
                    testText.text = p0.children.last().child("humidity").value.toString()
                    if(mChart!!.data.entryCount == 0) {
                        p0.children.forEach {
                            Log.d("fuga", it.child("time").value.toString())
                            addEntry(it.child("time").value.toString().toFloat(), it.child("humidity").value.toString().toFloat())
                        }
                    }
                    else {
                        Log.d("fuga", "fugafugafuga")
                        var latestItem = p0.children.last()
                        addEntry(latestItem.child("time").value.toString().toFloat(), latestItem.child("humidity").value.toString().toFloat())
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.d("dbCancelled", p0.toException().toString())
                }
            })
        }
        // Inflate the layout for this fragment
        return view
    }

    private fun addEntry(time: Float, humidity: Float) {
        val data = mChart!!.data

        val set = data.getDataSetByIndex(0)

        if (set == null) {
            val set = LineDataSet(null, "humidityData")
            set.axisDependency = YAxis.AxisDependency.LEFT
            set.color = Color.BLACK
            set.setDrawValues(false)
            data.addDataSet(set)
        }

        //Log.d("piyo", set.entryCount.toString())
        Log.d("piyo", data.dataSetCount.toString())
        data.addEntry(Entry(chIndex.toFloat(), humidity), 0)
        data.notifyDataChanged()
        mChart!!.notifyDataSetChanged()
        mChart!!.setVisibleXRangeMaximum(3f)
        mChart!!.moveViewToX(chIndex.toFloat())
        chIndex += 0.5
    }


    /*
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }
    */

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        private val ARG_VEG = "vegetable"

        fun newInstance(vegetable: Vegetable): HumidityGraphFragment {
            val fragment = HumidityGraphFragment()
            val args = Bundle()
            args.putSerializable(ARG_VEG, vegetable)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNothingSelected() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}// Required empty public constructor
