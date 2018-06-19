package com.a10llip0p.android.soso

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HumidityFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HumidityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HumidityFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    private var vegetable: Vegetable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            vegetable = arguments.getSerializable(ARG_VEGE) as Vegetable?
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_humidity, container, false)
        vegetable?.let {
            it.db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val humidityText = view.findViewById<TextView>(R.id.humidity_text)
                    val humidity = p0.children.last().child("humidity").value.toString()
                    if (humidity.toFloat() < 30f) {
                        when (it.name) {
                            "トマト" -> {
                                view.findViewById<ImageView>(R.id.humidity_img).setImageResource(R.drawable.tomato_dry)
                            }
                            "ナス" -> {
                                view.findViewById<ImageView>(R.id.humidity_img).setImageResource(R.drawable.eggplant_dry)
                            }
                            "ピーマン" -> {
                                view.findViewById<ImageView>(R.id.humidity_img).setImageResource(R.drawable.piment_dry)
                            }
                        }
                        humidityText.text = "乾燥しています"
                    }
                    else if (humidity.toFloat() < 70f) {
                        when (it.name) {
                            "トマト" -> {
                                view.findViewById<ImageView>(R.id.humidity_img).setImageResource(R.drawable.tomato_good)
                            }
                            "ナス" -> {
                                view.findViewById<ImageView>(R.id.humidity_img).setImageResource(R.drawable.eggplant_good)
                            }
                            "ピーマン" -> {
                                view.findViewById<ImageView>(R.id.humidity_img).setImageResource(R.drawable.piment_good)
                            }
                        }
                        humidityText.text = "いい感じです"
                    }
                    else {
                        when (it.name) {
                            "トマト" -> {
                                view.findViewById<ImageView>(R.id.humidity_img).setImageResource(R.drawable.tomato_flood)
                            }
                            "ナス" -> {
                                view.findViewById<ImageView>(R.id.humidity_img).setImageResource(R.drawable.eggplant_flood)
                            }
                            "ピーマン" -> {
                                view.findViewById<ImageView>(R.id.humidity_img).setImageResource(R.drawable.piment_flood)
                            }
                        }
                        humidityText.text = "水をあげすぎです"
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
        private val ARG_VEGE = "vegetable"

        fun newInstance(vegetable: Vegetable): HumidityFragment {
            val fragment = HumidityFragment()
            val args = Bundle()
            args.putSerializable(ARG_VEGE, vegetable)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
