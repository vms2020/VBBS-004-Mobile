package io.bbs.seva.vbbs004mobile.data.remote.dto.pictures


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
    "user": {
        "id": "1095cd3d-12a9-4059-9185-4fd79490eaa9",
        "email": "user002@email.com",
        "avatar_url": "https://my.vsevolod.dynv6.net:8443/storage/v1/object/public/user-uploads/1095cd3d-12a9-4059-9185-4fd79490eaa9/6f7d18d2-7420-4241-a9d6-c5f7852c35d0.jpg",
        "base_storage_path": "https://my.vsevolod.dynv6.net:8443/storage/v1/object/public/user-uploads/"
    },
    "pictures": [
        {
            "id": "0d4821cf-80a4-409c-9938-efce8712c824",
            "user_id": "1095cd3d-12a9-4059-9185-4fd79490eaa9",
            "storage_path": "1095cd3d-12a9-4059-9185-4fd79490eaa9/6f7d18d2-7420-4241-a9d6-c5f7852c35d0.jpg",
            "original_file_name": "x06.jpg",
            "created_at": "2026-09-03T09:20:44.348391+00:00"
        },
        {
            "id": "9e498c46-53f9-4ac8-9f2c-2841d6fa8c22",
            "user_id": "1095cd3d-12a9-4059-9185-4fd79490eaa9",
            "storage_path": "1095cd3d-12a9-4059-9185-4fd79490eaa9/c1053b81-2a8a-4b5a-8f03-c94b8458ce46.jpg",
            "original_file_name": "avatar.jpg",
            "created_at": "2026-09-03T07:35:01.997569+00:00"
        },
        {
            "id": "d9fa7456-5ce6-43d1-aeb7-8c2046712e23",
            "user_id": "1095cd3d-12a9-4059-9185-4fd79490eaa9",
            "storage_path": "1095cd3d-12a9-4059-9185-4fd79490eaa9/5d6f1f84-f733-4ecf-8396-bc5ad9c53b64.png",
            "original_file_name": "x07Nik.png",
            "created_at": "2026-08-16T19:44:35.042566+00:00"
        },
        {
            "id": "ff129b87-7cd1-4baf-abd3-57dc29c29b3e",
            "user_id": "1095cd3d-12a9-4059-9185-4fd79490eaa9",
            "storage_path": "1095cd3d-12a9-4059-9185-4fd79490eaa9/fd9b5d59-78a6-43d8-923e-f393aa91595a.jpeg",
            "original_file_name": "x08.jpeg",
            "created_at": "2026-08-16T18:26:12.538625+00:00"
        }
    ]
}
*/
@Serializable
data class AvaPictureDto(
    @SerialName("id")
    val id: String? = null,
    @SerialName("user_id")
    val userId: String? = null,
    @SerialName("storage_path")
    val storagePath: String? = null,
    @SerialName("original_file_name")
    val originalFileName: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)