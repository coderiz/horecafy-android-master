package app.horecafy.com.models

import android.os.Parcel
import android.os.Parcelable


data class ReviewOfferItems(val hiddenId: Long? = null,
                            val id: String? = null,
                            val WholeSalerHiddenId: Long? = null,
                            val WholeSalerId: String? = null,
                            val WholeSalerName: String? = null,
                            val quantyPerMonth: Int,
                            val TypeOfFormatId: String? = null,
                            val TypeOfFormatName: String? = null,
                            val ProductId: String? = null,
                            val ProductName: String? = null,
                            val offerPrice: Double,
                            val brand: String,
                            val images: String,
                            val video: String,
                            val approvedByCustomer: String,
                            val fomat: String,
                            val comments: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(hiddenId)
        parcel.writeString(id)
        parcel.writeValue(WholeSalerHiddenId)
        parcel.writeString(WholeSalerId)
        parcel.writeString(WholeSalerName)
        parcel.writeInt(quantyPerMonth)
        parcel.writeString(TypeOfFormatId)
        parcel.writeString(TypeOfFormatName)
        parcel.writeString(ProductId)
        parcel.writeString(ProductName)
        parcel.writeDouble(offerPrice)
        parcel.writeString(brand)
        parcel.writeString(images)
        parcel.writeString(video)
        parcel.writeString(approvedByCustomer)
        parcel.writeString(fomat)
        parcel.writeString(comments)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReviewOfferItems> {
        override fun createFromParcel(parcel: Parcel): ReviewOfferItems {
            return ReviewOfferItems(parcel)
        }

        override fun newArray(size: Int): Array<ReviewOfferItems?> {
            return arrayOfNulls(size)
        }
    }
}