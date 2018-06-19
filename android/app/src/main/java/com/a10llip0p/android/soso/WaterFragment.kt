package com.a10llip0p.android.soso

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.a10llip0p.android.soso.items.ItemContent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [WaterFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [WaterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WaterFragment : Fragment() {

    // TODO: Rename and change types of parameters

    private var mListener: OnFragmentInteractionListener? = null
    private var mItem: ItemContent.Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                mItem = ItemContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_water, container, false)
        mItem.let {
            FirebaseDatabase.getInstance().reference.child(it!!.details).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val waterLevel = p0.child("amount").value.toString()
                    val waterLevelVal = waterLevel.toInt()
                    val waterText = view.findViewById<TextView>(R.id.water_text)
                    if (waterLevelVal < 30) {
                        view.findViewById<ImageView>(R.id.water_img).setImageResource(R.drawable.water_red)
                        waterText.setTextColor(Color.RED)
                    } else if (waterLevelVal < 50) {
                        view.findViewById<ImageView>(R.id.water_img).setImageResource(R.drawable.water_yellow)
                        waterText.setTextColor(Color.rgb(255, 204, 0))
                    } else {
                        view.findViewById<ImageView>(R.id.water_img).setImageResource(R.drawable.water_blue)
                        waterText.setTextColor(Color.rgb(0, 204, 255))
                    }
                    waterText.textSize = 50F
                    waterText.text = waterLevel + "%"
                }

                override fun onCancelled(p0: DatabaseError) {
                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }
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
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        const val ARG_ITEM_ID = "item_id"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WaterFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): WaterFragment {
            val fragment = WaterFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
