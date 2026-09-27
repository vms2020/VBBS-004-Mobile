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
data class CbrDynamicDto(
    @SerialName("DateRange1")
    val dateRange1: String? = null, // 01.06.2026
    @SerialName("DateRange2")
    val dateRange2: String? = null, // 04.06.2026
    @SerialName("ID")
    val iD: String? = null, // R01235
    @SerialName("name")
    val name: String? = null, // Foreign Currency Market Dynamic
    @SerialName("Record")
    val record: List<RecordDto?>? = null
)