package io.bbs.seva.vbbs004mobile.data.remote.dto.cbr.daily


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** @formatter:off **
{
  "Date": "01.07.2026",
  "name": "Foreign Currency Market",
  "Valute": [
    {
      "ID": "R01010",
      "NumCode": 36,
      "CharCode": "AUD",
      "Nominal": 1,
      "Name": "Австралийский доллар",
      "Value": "53,7634",
      "VunitRate": "53,7634"
    },
    {
      "ID": "R01020A",
      "NumCode": 944,
      "CharCode": "AZN",
      "Nominal": 1,
      "Name": "Азербайджанский манат",
      "Value": "46,0409",
      "VunitRate": "46,0409"
    },
    {
      "ID": "R01030",
      "NumCode": 12,
      "CharCode": "DZD",
      "Nominal": 100,
      "Name": "Алжирских динаров",
      "Value": "58,7491",
      "VunitRate": "0,587491"
    },
    {
      "ID": "R01035",
      "NumCode": 826,
      "CharCode": "GBP",
      "Nominal": 1,
      "Name": "Фунт стерлингов",
      "Value": "103,6681",
      "VunitRate": "103,6681"
    },
    {
      "ID": "R01060",
      "NumCode": 51,
      "CharCode": "AMD",
      "Nominal": 100,
      "Name": "Армянских драмов",
      "Value": "21,2753",
      "VunitRate": "0,212753"
    },
    {
      "ID": "R01080",
      "NumCode": 48,
      "CharCode": "BHD",
      "Nominal": 1,
      "Name": "Бахрейнский динар",
      "Value": "208,1189",
      "VunitRate": "208,1189"
    },
    {
      "ID": "R01090B",
      "NumCode": 933,
      "CharCode": "BYN",
      "Nominal": 1,
      "Name": "Белорусский рубль",
      "Value": "26,9514",
      "VunitRate": "26,9514"
    },
    {
      "ID": "R01105",
      "NumCode": 68,
      "CharCode": "BOB",
      "Nominal": 10,
      "Name": "Боливиано",
      "Value": "80,1943",
      "VunitRate": "8,01943"
    },
    {
      "ID": "R01115",
      "NumCode": 986,
      "CharCode": "BRL",
      "Nominal": 1,
      "Name": "Бразильский реал",
      "Value": "15,1351",
      "VunitRate": "15,1351"
    },
    {
      "ID": "R01135",
      "NumCode": 348,
      "CharCode": "HUF",
      "Nominal": 100,
      "Name": "Форинтов",
      "Value": "25,1178",
      "VunitRate": "0,251178"
    },
    {
      "ID": "R01150",
      "NumCode": 704,
      "CharCode": "VND",
      "Nominal": 10000,
      "Name": "Донгов",
      "Value": "31,0520",
      "VunitRate": "0,0031052"
    },
    {
      "ID": "R01200",
      "NumCode": 344,
      "CharCode": "HKD",
      "Nominal": 10,
      "Name": "Гонконгских долларов",
      "Value": "99,7955",
      "VunitRate": "9,97955"
    },
    {
      "ID": "R01210",
      "NumCode": 981,
      "CharCode": "GEL",
      "Nominal": 1,
      "Name": "Лари",
      "Value": "29,5882",
      "VunitRate": "29,5882"
    },
    {
      "ID": "R01215",
      "NumCode": 208,
      "CharCode": "DKK",
      "Nominal": 1,
      "Name": "Датская крона",
      "Value": "11,9439",
      "VunitRate": "11,9439"
    },
    {
      "ID": "R01230",
      "NumCode": 784,
      "CharCode": "AED",
      "Nominal": 1,
      "Name": "Дирхам ОАЭ",
      "Value": "21,3123",
      "VunitRate": "21,3123"
    },
    {
      "ID": "R01235",
      "NumCode": 840,
      "CharCode": "USD",
      "Nominal": 1,
      "Name": "Доллар США",
      "Value": "78,2696",
      "VunitRate": "78,2696"
    },
    {
      "ID": "R01239",
      "NumCode": 978,
      "CharCode": "EUR",
      "Nominal": 1,
      "Name": "Евро",
      "Value": "89,2743",
      "VunitRate": "89,2743"
    },
    {
      "ID": "R01240",
      "NumCode": 818,
      "CharCode": "EGP",
      "Nominal": 10,
      "Name": "Египетских фунтов",
      "Value": "15,8990",
      "VunitRate": "1,5899"
    },
    {
      "ID": "R01270",
      "NumCode": 356,
      "CharCode": "INR",
      "Nominal": 100,
      "Name": "Индийских рупий",
      "Value": "82,7396",
      "VunitRate": "0,827396"
    },
    {
      "ID": "R01280",
      "NumCode": 360,
      "CharCode": "IDR",
      "Nominal": 10000,
      "Name": "Рупий",
      "Value": "43,8338",
      "VunitRate": "0,00438338"
    },
    {
      "ID": "R01300",
      "NumCode": 364,
      "CharCode": "IRR",
      "Nominal": 1000000,
      "Name": "Иранских риалов",
      "Value": "53,3746",
      "VunitRate": "5,33746E-05"
    },
    {
      "ID": "R01335",
      "NumCode": 398,
      "CharCode": "KZT",
      "Nominal": 100,
      "Name": "Тенге",
      "Value": "16,1108",
      "VunitRate": "0,161108"
    },
    {
      "ID": "R01350",
      "NumCode": 124,
      "CharCode": "CAD",
      "Nominal": 1,
      "Name": "Канадский доллар",
      "Value": "55,0962",
      "VunitRate": "55,0962"
    },
    {
      "ID": "R01355",
      "NumCode": 634,
      "CharCode": "QAR",
      "Nominal": 1,
      "Name": "Катарский риал",
      "Value": "21,5026",
      "VunitRate": "21,5026"
    },
    {
      "ID": "R01370",
      "NumCode": 417,
      "CharCode": "KGS",
      "Nominal": 100,
      "Name": "Сомов",
      "Value": "89,5021",
      "VunitRate": "0,895021"
    },
    {
      "ID": "R01375",
      "NumCode": 156,
      "CharCode": "CNY",
      "Nominal": 1,
      "Name": "Юань",
      "Value": "11,4932",
      "VunitRate": "11,4932"
    },
    {
      "ID": "R01395",
      "NumCode": 192,
      "CharCode": "CUP",
      "Nominal": 10,
      "Name": "Кубинских песо",
      "Value": "32,6123",
      "VunitRate": "3,26123"
    },
    {
      "ID": "R01500",
      "NumCode": 498,
      "CharCode": "MDL",
      "Nominal": 10,
      "Name": "Молдавских леев",
      "Value": "44,3030",
      "VunitRate": "4,4303"
    },
    {
      "ID": "R01503",
      "NumCode": 496,
      "CharCode": "MNT",
      "Nominal": 1000,
      "Name": "Тугриков",
      "Value": "21,8632",
      "VunitRate": "0,0218632"
    },
    {
      "ID": "R01520",
      "NumCode": 566,
      "CharCode": "NGN",
      "Nominal": 1000,
      "Name": "Найр",
      "Value": "56,5685",
      "VunitRate": "0,0565685"
    },
    {
      "ID": "R01530",
      "NumCode": 554,
      "CharCode": "NZD",
      "Nominal": 1,
      "Name": "Новозеландский доллар",
      "Value": "44,1949",
      "VunitRate": "44,1949"
    },
    {
      "ID": "R01535",
      "NumCode": 578,
      "CharCode": "NOK",
      "Nominal": 10,
      "Name": "Норвежских крон",
      "Value": "78,7983",
      "VunitRate": "7,87983"
    },
    {
      "ID": "R01540",
      "NumCode": 512,
      "CharCode": "OMR",
      "Nominal": 1,
      "Name": "Оманский риал",
      "Value": "203,5620",
      "VunitRate": "203,562"
    },
    {
      "ID": "R01565",
      "NumCode": 985,
      "CharCode": "PLN",
      "Nominal": 1,
      "Name": "Злотый",
      "Value": "20,7568",
      "VunitRate": "20,7568"
    },
    {
      "ID": "R01580",
      "NumCode": 682,
      "CharCode": "SAR",
      "Nominal": 1,
      "Name": "Саудовский риял",
      "Value": "20,8719",
      "VunitRate": "20,8719"
    },
    {
      "ID": "R01585F",
      "NumCode": 946,
      "CharCode": "RON",
      "Nominal": 1,
      "Name": "Румынский лей",
      "Value": "17,0114",
      "VunitRate": "17,0114"
    },
    {
      "ID": "R01589",
      "NumCode": 960,
      "CharCode": "XDR",
      "Nominal": 1,
      "Name": "СДР (специальные права заимствования)",
      "Value": "106,1500",
      "VunitRate": "106,15"
    },
    {
      "ID": "R01625",
      "NumCode": 702,
      "CharCode": "SGD",
      "Nominal": 1,
      "Name": "Сингапурский доллар",
      "Value": "60,4725",
      "VunitRate": "60,4725"
    },
    {
      "ID": "R01670",
      "NumCode": 972,
      "CharCode": "TJS",
      "Nominal": 10,
      "Name": "Сомони",
      "Value": "84,4105",
      "VunitRate": "8,44105"
    },
    {
      "ID": "R01675",
      "NumCode": 764,
      "CharCode": "THB",
      "Nominal": 10,
      "Name": "Батов",
      "Value": "23,5553",
      "VunitRate": "2,35553"
    },
    {
      "ID": "R01685",
      "NumCode": 50,
      "CharCode": "BDT",
      "Nominal": 100,
      "Name": "Так",
      "Value": "63,4048",
      "VunitRate": "0,634048"
    },
    {
      "ID": "R01700J",
      "NumCode": 949,
      "CharCode": "TRY",
      "Nominal": 10,
      "Name": "Турецких лир",
      "Value": "16,7971",
      "VunitRate": "1,67971"
    },
    {
      "ID": "R01710A",
      "NumCode": 934,
      "CharCode": "TMT",
      "Nominal": 1,
      "Name": "Новый туркменский манат",
      "Value": "22,3627",
      "VunitRate": "22,3627"
    },
    {
      "ID": "R01717",
      "NumCode": 860,
      "CharCode": "UZS",
      "Nominal": 10000,
      "Name": "Узбекских сумов",
      "Value": "65,1743",
      "VunitRate": "0,00651743"
    },
    {
      "ID": "R01720",
      "NumCode": 980,
      "CharCode": "UAH",
      "Nominal": 10,
      "Name": "Гривен",
      "Value": "17,4741",
      "VunitRate": "1,74741"
    },
    {
      "ID": "R01760",
      "NumCode": 203,
      "CharCode": "CZK",
      "Nominal": 10,
      "Name": "Чешских крон",
      "Value": "36,7687",
      "VunitRate": "3,67687"
    },
    {
      "ID": "R01770",
      "NumCode": 752,
      "CharCode": "SEK",
      "Nominal": 10,
      "Name": "Шведских крон",
      "Value": "80,5253",
      "VunitRate": "8,05253"
    },
    {
      "ID": "R01775",
      "NumCode": 756,
      "CharCode": "CHF",
      "Nominal": 1,
      "Name": "Швейцарский франк",
      "Value": "96,6888",
      "VunitRate": "96,6888"
    },
    {
      "ID": "R01800",
      "NumCode": 230,
      "CharCode": "ETB",
      "Nominal": 100,
      "Name": "Эфиопских быров",
      "Value": "49,5226",
      "VunitRate": "0,495226"
    },
    {
      "ID": "R01805F",
      "NumCode": 941,
      "CharCode": "RSD",
      "Nominal": 100,
      "Name": "Сербских динаров",
      "Value": "75,9625",
      "VunitRate": "0,759625"
    },
    {
      "ID": "R01810",
      "NumCode": 710,
      "CharCode": "ZAR",
      "Nominal": 10,
      "Name": "Рэндов",
      "Value": "47,7044",
      "VunitRate": "4,77044"
    },
    {
      "ID": "R01815",
      "NumCode": 410,
      "CharCode": "KRW",
      "Nominal": 1000,
      "Name": "Вон",
      "Value": "50,7750",
      "VunitRate": "0,050775"
    },
    {
      "ID": "R01820",
      "NumCode": 392,
      "CharCode": "JPY",
      "Nominal": 100,
      "Name": "Иен",
      "Value": "48,2550",
      "VunitRate": "0,48255"
    },
    {
      "ID": "R02005",
      "NumCode": 104,
      "CharCode": "MMK",
      "Nominal": 1000,
      "Name": "Кьятов",
      "Value": "37,2712",
      "VunitRate": "0,0372712"
    }
  ]
}
**  @formatter:on **/
@Serializable
data class ValuteDto(
    @SerialName("CharCode")
    val charCode: String? = null, // AUD
    @SerialName("ID")
    val iD: String? = null, // R01010
    @SerialName("Name")
    val name: String? = null, // Австралийский доллар
    @SerialName("Nominal")
    val nominal: Long? = null, // 1
    @SerialName("NumCode")
    val numCode: Int? = null, // 36
    @SerialName("Value")
    val value: String? = null, // 53,7634
    @SerialName("VunitRate")
    val vunitRate: String? = null // 53,7634
)