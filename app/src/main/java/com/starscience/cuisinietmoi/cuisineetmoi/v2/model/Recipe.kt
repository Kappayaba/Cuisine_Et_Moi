package com.starscience.cuisinietmoi.cuisineetmoi.v2.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.ArrayList

@Entity
data class Recipe(
    @field:PrimaryKey
    var id: String,
    var name: String?,
    var imageUrl: String?,
    val userId: String,
    val postDate: String?,
    var likesCount: Int,
    var replyCount: Int,
    val person: String,
    val preparation: String?,
    val cooking: String?,
    val ingredients: List<String>?,
    val steps: List<String>?

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.createStringArrayList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(imageUrl)
        parcel.writeString(userId)
        parcel.writeString(postDate)
        parcel.writeInt(likesCount)
        parcel.writeInt(replyCount)
        parcel.writeString(person)
        parcel.writeString(preparation)
        parcel.writeString(cooking)
        parcel.writeStringList(ingredients)
        parcel.writeStringList(steps)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Recipe> {
        override fun createFromParcel(parcel: Parcel): Recipe {
            return Recipe(parcel)
        }

        override fun newArray(size: Int): Array<Recipe?> {
            return arrayOfNulls(size)
        }
    }
}