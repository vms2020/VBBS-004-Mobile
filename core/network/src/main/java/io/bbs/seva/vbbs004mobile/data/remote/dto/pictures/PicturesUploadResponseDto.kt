package io.bbs.seva.vbbs004mobile.data.remote.dto.pictures


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
  "success": true,
  "message": "Picture processed and uploaded successfully",
  "data": {
    "storage_path": "55e53b15-257b-4616-872a-7074d5753b9b/0d3bc8bf-89ce-49b7-9e32-6d576ace1296.jpg",
    "original_file_name": "avatar.jpg",
    "url": "https://evuynobnxghhbbhhpqxd.supabase.co/storage/v1/object/public/user-uploads/55e53b15-257b-4616-872a-7074d5753b9b/0d3bc8bf-89ce-49b7-9e32-6d576ace1296.jpg"
  }
}
*/
@Serializable
data class PicturesUploadResponseDto(
    @SerialName("success")
    val success: Boolean? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("data")
    val `data`: FileUploadDataRespDto? = null
)