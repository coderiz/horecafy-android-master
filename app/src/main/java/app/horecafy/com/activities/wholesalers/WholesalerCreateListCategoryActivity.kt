package app.horecafy.com.activities.wholesalers

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import app.horecafy.com.OnClickWithCheckListener
import app.horecafy.com.R

import app.horecafy.com.adapters.CategoryRecyclerViewAdapter
import app.horecafy.com.models.Category
import app.horecafy.com.services.CommonService
import kotlinx.android.synthetic.main.activity_wholesaler_create_list_category.*
import java.io.File.separator
import java.util.ArrayList
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import app.horecafy.com.Retrofit.APIService
import app.horecafy.com.Retrofit.ApiClient
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.WholesalerListService
import app.horecafy.com.util.UiHelpers
import kotlinx.android.synthetic.main.activity_wholesaler_list.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class WholesalerCreateListCategoryActivity : AppCompatActivity() {

    var categories: MutableList<Category> = mutableListOf()
    var Member_id = ArrayList<String>()
    private var menu: Menu? = null

    var isType = 1 //0 = single select, 1 = multiselect
    var adapter: CategoryRecyclerViewAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wholesaler_create_list_category)

        // Configure recyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 3) as RecyclerView.LayoutManager?
        recyclerView.itemAnimator = DefaultItemAnimator()
        setAdapter()

        // Load mAvailabilityList
        CommonService.categories(this, AuthService.wholesaler?.id!!, { status, data ->
            if (status && data != null) {
//                mAvailabilityList = data.filter { it.id != -1 }
                categories = data
                categories.removeAt(0)
                setAdapter()
            } else {
                // TODO Error loading mAvailabilityList
            }
        })

        buttonaddCatList.setOnClickListener(View.OnClickListener {
            Log.e("AAAAAAAAAAA", "CLick Array : : "+Member_id.toString())
            if(Member_id.size > 0){
                val s = TextUtils.join(", ", Member_id)
                Log.e("REQ", "Category : : "+s+" Wholesaler ID: "+AuthService.wholesaler?.hiddenId!! )
                ADDCatAPI(s,AuthService.wholesaler?.hiddenId!!)
            }else{
                Toast.makeText(this@WholesalerCreateListCategoryActivity,"Seleccione al menos una categoría",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun ADDCatAPI(s: String?, hiddenId: Long) {
        val apiService = ApiClient.getClient(this).create(APIService::class.java)
        val call = apiService.AddCategory(hiddenId.toString(), s)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        var res: String? = null
                        try {
                            res = response.body().string()
                            Log.e("res ", "Add CAT : " + res!!)
                            val jsonObject = JSONObject(res)
                            if(jsonObject.getString("error").equals("")){
                                Toast.makeText(this@WholesalerCreateListCategoryActivity, "Exitoso", Toast.LENGTH_LONG).show()
                                finish()
                            }else{
                                Toast.makeText(this@WholesalerCreateListCategoryActivity, ""+jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                            }

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                } else {
                    Log.e("res ", "Add CAT ERROR: ")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("CCC", "Failure: $call")
            }
        })
    }
    private fun setAdapter() {
        // Set the adapter
        adapter = CategoryRecyclerViewAdapter(this@WholesalerCreateListCategoryActivity, categories,isType)
        adapter!!.onClickPositionListener(onClickCheckListner)
        recyclerView.adapter = adapter

        // Handle click
        adapter!!.onClickListener = View.OnClickListener { v: View? ->
            // Get selected category
            val position = recyclerView.getChildAdapterPosition(v)
            val category = categories[position]

            // Start family activity
            val intent = WholesalerCreateListOffersActivity.intent(this, category)
            startActivity(intent)
        }

    }

   /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu
        menuInflater.inflate(R.menu.activity_category, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.action_select) {
            buttonaddCatList.visibility = View.VISIBLE
            isType = 1
            setAdapter()
            Log.e("ISTYPE","111111000 : "+isType)
            menu!!.getItem(1).setVisible(true)
            menu!!.getItem(0).setVisible(false)
            return true;
        }else if (id == R.id.action_deselect) {
            buttonaddCatList.visibility = View.GONE
            isType = 0
            setAdapter()
            Log.e("ISTYPE","1111111000 : "+isType)
            menu!!.getItem(0).setVisible(true)
            menu!!.getItem(1).setVisible(false)
            return true;
        }
        return super.onOptionsItemSelected(item);



    }*/

    var onClickCheckListner: OnClickWithCheckListener = object : OnClickWithCheckListener {
        override fun onClick(pos: Int, isSelection: Boolean) {
            val member = categories.get(pos)
            if (isSelection) {
                Member_id.add(member.id.toString())
            } else {
                Member_id.remove(member.id.toString())
            }
        }
    }

}
