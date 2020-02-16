package at.fh.swengb.mattlschweiger.notes

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthRequest(val username: String, val password: String) {
}