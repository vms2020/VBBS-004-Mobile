package io.bbs.seva.vbbs004mobile.data.remote.dto.cbr.dynamic


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** @formatter:off **
{
  "name": "Foreign Currency Market Dynamic",
  "ID": "R01235",
  "DateRange1": "01.06.2026",
  "DateRange2": "04.06.2026",
  "Record": [
    {
      "Date": "02.06.2026",
      "Id": "R01235",
      "Nominal": 1,
      "Value": "71,5532",
      "VunitRate": "71,5532"
    },
    {
      "Date": "03.06.2026",
      "Id": "R01235",
      "Nominal": 1,
      "Value": "72,5597",
      "VunitRate": "72,5597"
    },
    {
      "Date": "04.06.2026",
      "Id": "R01235",
      "Nominal": 1,
      "Value": "73,3436",
      "VunitRate": "73,3436"
    }
  ]
}
 **  @formatter:on **/
@Serializable
data class RecordDto(
    @SerialName("Date")
    val date: String? = null, // 02.06.2026
    @SerialName("Id")
    val id: String? = null, // R01235
    @SerialName("Nominal")
    val nominal: Int? = null, // 1
    @SerialName("Value")
    val value: String? = null, // 71,5532
    @SerialName("VunitRate")
    val vunitRate: String? = null // 71,5532
)