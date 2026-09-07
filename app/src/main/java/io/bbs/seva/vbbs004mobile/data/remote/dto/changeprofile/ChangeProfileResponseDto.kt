package io.bbs.seva.vbbs004mobile.data.remote.dto.changeprofile


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
    "success": true,
    "message": "Profile updated",
    "user": {
        "full_name": "Second User",
        "avatar_url": "https://my.vsevolod.dynv6.net:8443/storage/v1/object/public/user-uploads/1095cd3d-12a9-4059-9185-4fd79490eaa9/6f7d18d2-7420-4241-a9d6-c5f7852c35d0.jpg",
        "age": 21
    }
}
*/
@Serializable
data class ChangeProfileResponseDto(
    @SerialName("success")
    val success: Boolean? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("user")
    val user: ChangeProfileUserDto? = null
)
