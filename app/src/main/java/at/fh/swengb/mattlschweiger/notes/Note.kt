package at.fh.swengb.mattlschweiger.notes

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.util.*


@Entity
@JsonClass(generateAdapter = true)
data class Note(@PrimaryKey val id:String, var title: String, var text: String, var toUpload: Boolean)
{

}