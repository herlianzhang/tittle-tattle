package com.latihangoding.tittle_tattle

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.latihangoding.tittle_tattle.utils.FirebaseConfiguration
import com.latihangoding.tittle_tattle.vo.User
import timber.log.Timber

class WidgetService: RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return WidgetItemFactory(applicationContext)
    }

    // befungsi mirip adapter pada recyclerview
    class WidgetItemFactory(private val context: Context) : RemoteViewsFactory {
        private var list = listOf<User?>()
        override fun onCreate() {
            try {
                // observe data yang ada pada realtime database firebase
                Firebase.database.getReference(FirebaseConfiguration.USER)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val myUserId = Firebase.auth.uid
                            val mList = mutableListOf<User?>()
                            for (d in snapshot.children) {
                                val k = d.key
                                val v = d.getValue(User::class.java)
                                if (myUserId != k)
                                    mList.add(v?.copy(uid = k))
                            } // mengambil semua data yang ada pada pada field user
                            list = mList
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        override fun onDataSetChanged() {

        }

        override fun onDestroy() {

        }

        override fun getCount() = list.size // memasukkan jumlah item

        override fun getViewAt(position: Int): RemoteViews {
            val views =
                RemoteViews(context.packageName, R.layout.item_widget) // membuat view pada item
            views.setTextViewText(
                R.id.tv_name,
                list[position]?.name
            ) // memasukkan teks pada textview
            views.setOnClickFillInIntent(
                R.id.ll_view,
                Intent().putExtra("data", list[position])
            )
            try { // memasukkan image yang berupa link ke imageview
                val bitmap = Glide.with(context)
                    .asBitmap()
                    .load(list[position]?.photoUrl)
                    .submit(512, 512)
                    .get() // meload gambar yang berupa bitmap menggunakan glide
                views.setImageViewBitmap(
                    R.id.iv_avatar,
                    bitmap
                ) // memasukkan bitmap tadi pada imageview
            } catch (e: Exception) {

            }
            return views
        }

        override fun getLoadingView() = RemoteViews(context.packageName, R.layout.item_widget)

        override fun getViewTypeCount() = 1

        override fun getItemId(position: Int) = position.toLong()

        override fun hasStableIds() = true
    }
}