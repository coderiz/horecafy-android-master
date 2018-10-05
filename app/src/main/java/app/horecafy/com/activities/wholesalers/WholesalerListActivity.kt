package app.horecafy.com.activities.wholesalers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import app.horecafy.com.R
import app.horecafy.com.models.Category
import app.horecafy.com.models.Family
import app.horecafy.com.models.WholesalerList
import app.horecafy.com.services.AuthService
import app.horecafy.com.services.CommonService
import app.horecafy.com.services.WholesalerListService
import app.horecafy.com.util.UiHelpers
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_wholesaler_list.*

class WholesalerListActivity : AppCompatActivity() {

    companion object {
        val INTENT_CATEGORY = "INTENT_REVIEW_OFFERS_LIST"
        val INTENT_FAMILY = "INTENT_FAMILY"
        val INTENT_WHOLESALERLIST = "INTENT_WHOLESALERLIST"

        fun intent(context: Context, category: Category, familyId: Int, wholesalerListId: Int?): Intent {
            val intent = Intent(context, WholesalerListActivity::class.java)
            intent.putExtra(INTENT_CATEGORY, category);
            intent.putExtra(INTENT_FAMILY, familyId);
            intent.putExtra(INTENT_WHOLESALERLIST, wholesalerListId);
            return intent
        }
    }

    var category: Category? = null
    var familyId: Int? = null
    var wholesalerListId: Int? = null

    var families: List<Family>? = null
    var wholesalerList: WholesalerList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wholesaler_list)

        // Get wholesalerListId from intent and load it from API
        category = intent.getSerializableExtra(INTENT_CATEGORY) as Category
        familyId = intent.getSerializableExtra(INTENT_FAMILY) as Int?
        wholesalerListId = intent.getSerializableExtra(INTENT_WHOLESALERLIST) as Int?

        // Bind useful data
        val uri = "${this.getString(R.string.IMAGE_URL)}${category!!.image}"
        Picasso.with(this).load(uri).into(imageCategory)
        textCategory.text = category!!.name

        loadFamilies()

        buttonSave.setOnClickListener(View.OnClickListener {
            UiHelpers.showProgessBar(window, progressBar)
            if (isValid()) {
                val brand = editBrand.text.toString()
                val comments = editComments.text.toString()

                if (wholesalerList == null) {
                    wholesalerList = WholesalerList(wholesalerId = AuthService.wholesaler?.hiddenId!!,
                    familyId = familyId!!,
                    brand = brand,
                    comments = comments)

                    WholesalerListService.create(this, wholesalerList!!) { status, data, error ->
                        UiHelpers.hideProgessBar(window, progressBar)
                        if (status) {
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Error desconocido", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    wholesalerList!!.brand = brand
                    wholesalerList!!.comments = comments

                    WholesalerListService.update(this, wholesalerList!!) { status, data, error ->
                        UiHelpers.hideProgessBar(window, progressBar)
                        if (status) {
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Error desconocido", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                UiHelpers.hideProgessBar(window, progressBar)
            }
        })
    }

    private fun loadFamilies() {
        UiHelpers.showProgessBar(window, progressBar)
        CommonService.families(this, category!!.id, { status, data ->
            if (status) {
                families = data

                val familyIndex = families!!.indexOfFirst { it.id == familyId }
                if (familyIndex != null) {
                    textFamily.text = families!![familyIndex].name
                }

                if (wholesalerListId != null) {
                    loadWholesalerList()
                } else {
                    UiHelpers.hideProgessBar(window, progressBar)
                }

            } else {
                // TODO Error loading families
            }
        })
    }

    private fun loadWholesalerList() {
        WholesalerListService.get(this, wholesalerListId!!, { status, data, error ->
            UiHelpers.hideProgessBar(window, progressBar)
            if (status) {
                wholesalerList = data
                bindWholesalerList(wholesalerList!!)
            } else {
                // TODO Error loading demand
            }
        })
    }

    private fun bindWholesalerList(wholesalerList: WholesalerList) {
        // Bind demand
        editBrand.setText(wholesalerList.brand)
        editComments.setText(wholesalerList.comments)
    }

    private fun isValid(): Boolean {
        var isValid = true

        val brand = editBrand.text.toString()

        if (brand.isNullOrEmpty()) {
            editBrand.setError("Este campo es obligatorio")
            isValid = false
        }

        return isValid
    }
}
