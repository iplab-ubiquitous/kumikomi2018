package com.a10llip0p.android.soso

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.Serializable

/**
 * Created by yuto on 2018/06/16.
 */
class Vegetable(name: String, ref: String) : Serializable {
    var name: String
    var db: DatabaseReference

    init {
        this.name = name
        this.db = FirebaseDatabase.getInstance().reference.child(ref)
    }
}