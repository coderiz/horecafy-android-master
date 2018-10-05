package app.horecafy.com.activities.wholesalers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import app.horecafy.com.R
import app.horecafy.com.models.Category
import app.horecafy.com.models.Family
import app.horecafy.com.models.WholesalerList
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.WholesalerListService
import app.horecafy.com.util.UiHelpers
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_wholesaler_create_list_offers.*


class WholesalerCreateListOffersActivity : AppCompatActivity() {

    companion object {
        val INTENT_CATEGORY = "INTENT_REVIEW_OFFERS_LIST"
        val REQUEST_FAMILY = 1
        val REQUEST_LIST = 2

        fun intent(context: Context, category: Category): Intent {
            val intent = Intent(context, WholesalerCreateListOffersActivity::class.java)
            intent.putExtra(INTENT_CATEGORY, category);
            return intent
        }
    }

    var category: Category? = null
    var lists: MutableList<WholesalerList> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wholesaler_create_list_offers)

        // Get category from intent
        if (category == null) {
            category = intent.getSerializableExtra(INTENT_CATEGORY) as Category?
        }

        // Bind useful data
        val uri = "${this.getString(R.string.IMAGE_URL)}${category!!.image}"
        Picasso.with(this).load(uri).into(imageCategory)
        textCategory.text = category!!.name

        // Load families in offer for the selected category
        loadLists()

        // Handle click
        imageCategory.setOnClickListener(View.OnClickListener {
            /*val intent = WholesalerListActivity.intent(this, category!!, null)
            startActivityForResult(intent, REQUEST_LIST)*/
            val intent = WholesalerFamiliesActivity.intent(this, category!!)
            startActivityForResult(intent, REQUEST_FAMILY)
        })

        // Handle click on image button
        ib_add_category.setOnClickListener(View.OnClickListener {
            val intent = WholesalerFamiliesActivity.intent(this, category!!)
            startActivityForResult(intent, REQUEST_FAMILY)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_FAMILY && resultCode == AppCompatActivity.RESULT_OK) {
            val family = data?.getSerializableExtra(WholesalerListActivity.INTENT_FAMILY) as? Family
            val intent = WholesalerListActivity.intent(this, category!!, family?.id!!, null)
            startActivityForResult(intent, REQUEST_LIST)
        }

        if (requestCode == REQUEST_LIST && resultCode == AppCompatActivity.RESULT_OK) {
            // Reload demands
            loadLists()
        }
    }

    private fun loadLists() {
        // Load demands
        UiHelpers.showProgessBar(window, progressBar)
        WholesalerListService.get(this, AuthService.wholesaler?.hiddenId!!, category!!.id, { status, data, error ->
            UiHelpers.hideProgessBar(window, progressBar)
            if (status && data != null) {
                lists = data.toMutableList()

                if (lists.isEmpty()) {
                    listWholesalerLists.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    listWholesalerLists.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    setListsAdapter()
                }
            } else {
                // TODO Error loading demands
                listWholesalerLists.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        })
    }

    private fun setListsAdapter() {
        listWholesalerLists.adapter = object : ArrayAdapter<WholesalerList>(this, R.layout.list_view_item_family, lists) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = convertView
                        ?: LayoutInflater.from(context).inflate(R.layout.list_view_item_family, parent, false)

                val text = view.findViewById<TextView>(R.id.textView)
                val image = view.findViewById<ImageButton>(R.id.imageButton)

                val list = lists[position]
                text.text = list.family?.name

                // Handle click
                text.setOnClickListener(View.OnClickListener {
                    // Update demand
                    val intent = WholesalerListActivity.intent(context, category!!, list.family!!.id, list.id)
                    startActivityForResult(intent, REQUEST_LIST)
                })

                image.setOnClickListener(View.OnClickListener {
                    // Remove demand
                    UiHelpers.showProgessBar(window, progressBar)
                    WholesalerListService.delete(view.context, list.id, { status, error ->
                        UiHelpers.hideProgessBar(window, progressBar)
                        if (status) {
                            // Remove item locally
                            lists.remove(list)
                            setListsAdapter()
                        } else {
                            // TODO Error deleting a demand
                        }
                    })
                })

                return view
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.activity_create, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_create -> {
//                val intent = WholesalerFamiliesActivity.intent(this, category!!)
//                startActivityForResult(intent, REQUEST_FAMILY)
//                return true
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }
}
