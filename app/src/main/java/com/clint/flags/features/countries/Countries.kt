package com.clint.flags.features.countries

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Countries(
    @SerializedName("flag") var flag: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("region") var region: String? = null,
    @SerializedName("symbol") var symbol: String? = null,
    @SerializedName("capital") var capital: String? = null,
    @SerializedName("currency") var currency: String? = null,
    @SerializedName("subregion") var subregion: String? = null,
    @SerializedName("population") var population: String? = null
) : Parcelable {
    companion object {
        val empty = Countries()
    }
}
