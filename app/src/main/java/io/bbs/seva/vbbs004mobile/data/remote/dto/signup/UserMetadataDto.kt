package io.bbs.seva.vbbs004mobile.data.remote.dto.signup


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
{
    "access_token": "eyJhbGciOiJFUzI1NiIsImtpZCI6IjVlOGI0Y2JhLTYwNWEtNGExZC1hODI1LTA5ZWVhNWE0ZWFmMCIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2V2dXlub2JueGdoaGJiaGhwcXhkLnN1cGFiYXNlLmNvL2F1dGgvdjEiLCJzdWIiOiJkNTQ0ODQ1Ni00ODk2LTRmYWItODcxNy0zZmI1OTUxZjIzYTAiLCJhdWQiOiJhdXRoZW50aWNhdGVkIiwiZXhwIjoxNzg4MDI4MTM1LCJpYXQiOjE3ODgwMjQ1MzUsImVtYWlsIjoidXNlcjAwN0BlbWFpbC5jb20iLCJwaG9uZSI6IiIsImFwcF9tZXRhZGF0YSI6eyJwcm92aWRlciI6ImVtYWlsIiwicHJvdmlkZXJzIjpbImVtYWlsIl19LCJ1c2VyX21ldGFkYXRhIjp7ImVtYWlsIjoidXNlcjAwN0BlbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZnVsbF9uYW1lIjoiVXNlciBTZXZlbiIsInBob25lX3ZlcmlmaWVkIjpmYWxzZSwic3ViIjoiZDU0NDg0NTYtNDg5Ni00ZmFiLTg3MTctM2ZiNTk1MWYyM2EwIn0sInJvbGUiOiJhdXRoZW50aWNhdGVkIiwiYWFsIjoiYWFsMSIsImFtciI6W3sibWV0aG9kIjoicGFzc3dvcmQiLCJ0aW1lc3RhbXAiOjE3ODgwMjQ1MzV9XSwic2Vzc2lvbl9pZCI6ImM3NWFmZWQ4LWNlYTAtNGQ2ZS1iNDE1LWUyMDNjMGZkNjBhMCIsImlzX2Fub255bW91cyI6ZmFsc2V9.DkExpYF9e8M7a1hj7OEYu3zdnp72g-ABDxQECHYqOcuQm7C5RXKe0-Qp1Zr86RB4Qx-wCFaT_OBXZWta9NAZQQ",
    "refresh_token": "uoizpl5hovah",
    "user": {
        "id": "d5448456-4896-4fab-8717-3fb5951f23a0",
        "aud": "authenticated",
        "role": "authenticated",
        "email": "user007@email.com",
        "email_confirmed_at": "2026-08-29T17:28:55.753909378Z",
        "phone": "",
        "last_sign_in_at": "2026-08-29T17:28:55.773543225Z",
        "app_metadata": {
            "provider": "email",
            "providers": [
                "email"
            ]
        },
        "user_metadata": {
            "email": "user007@email.com",
            "email_verified": true,
            "full_name": "User Seven",
            "phone_verified": false,
            "sub": "d5448456-4896-4fab-8717-3fb5951f23a0"
        },
        "identities": [
            {
                "identity_id": "0ac12433-d78a-45cd-bdc2-a72cb2059b8d",
                "id": "d5448456-4896-4fab-8717-3fb5951f23a0",
                "user_id": "d5448456-4896-4fab-8717-3fb5951f23a0",
                "identity_data": {
                    "email": "user007@email.com",
                    "email_verified": true,
                    "full_name": "User Seven",
                    "phone_verified": false,
                    "sub": "d5448456-4896-4fab-8717-3fb5951f23a0"
                },
                "provider": "email",
                "last_sign_in_at": "2026-08-29T17:28:55.743935124Z",
                "created_at": "2026-08-29T17:28:55.743988Z",
                "updated_at": "2026-08-29T17:28:55.743988Z",
                "email": "user007@email.com"
            }
        ],
        "created_at": "2026-08-29T17:28:55.702095Z",
        "updated_at": "2026-08-29T17:28:55.798991Z",
        "is_anonymous": false
    }
}
*/
@Serializable
data class UserMetadataDto(
    @SerialName("email")
    val email: String? = null,
    @SerialName("email_verified")
    val emailVerified: Boolean? = null,
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    val age: Int? = null,
    @SerialName("phone_verified")
    val phoneVerified: Boolean? = null,
    @SerialName("sub")
    val sub: String? = null
)