package com.sun.uefascore.data.source.remote.fetchjson

import com.sun.uefascore.BuildConfig
import com.sun.uefascore.data.model.FixtureEntry
import com.sun.uefascore.data.model.StandingGroupEntry
import com.sun.uefascore.data.model.StandingLeagueEntry
import com.sun.uefascore.data.model.TeamDetailEntry
import com.sun.uefascore.utils.Constant
import com.sun.uefascore.utils.TypeModel
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ParseDataWithJson {

    @Throws(Exception::class)
    fun getJsonFromUrl(urlString: String): String {
        val url = URL(urlString)
        val httpURLConnect = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = METHOD_GET
            connectTimeout = TIME_OUT
            readTimeout = TIME_OUT
            setRequestProperty(HEADER_API_KEY, BuildConfig.API_KEY)
            connect()
        }
        val stringBuilder = StringBuilder()
        if (httpURLConnect.responseCode == 200) {
            val bufferedInputStream = BufferedInputStream(httpURLConnect.inputStream) as InputStream
            val bufferedReader = BufferedReader(InputStreamReader(bufferedInputStream.buffered()))
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            bufferedInputStream.close()
            bufferedReader.close()
            httpURLConnect.disconnect()
        }
        return stringBuilder.toString()
    }

    fun parseJson(jsonString: String, typeModel: TypeModel): Any? =
        try {
            when (typeModel) {
                TypeModel.FIXTURE -> {
                    parseJsonToList(
                        JSONObject(jsonString).getJSONArray(FixtureEntry.RESPONSE),
                        typeModel
                    )
                }
                TypeModel.STANDING_LEAGUE -> {
                    parseJsonToObject(
                        JSONObject(jsonString).getJSONArray(
                            Constant.RESPONSE
                        ).getJSONObject(0).getJSONObject(StandingLeagueEntry.LEAGUE),
                        typeModel
                    )
                }
                TypeModel.STANDING_GROUP -> {
                    parseJsonToListJson(
                        JSONObject(jsonString).getJSONArray(StandingGroupEntry.STANDING_GROUP),
                        typeModel
                    )
                }
                TypeModel.STANDING -> {
                    parseJsonToList(
                        JSONArray(jsonString),
                        typeModel
                    )
                }
                TypeModel.TOP_SCORER -> {
                    parseJsonToList(
                        JSONObject(jsonString).getJSONArray(Constant.RESPONSE),
                        typeModel
                    )
                }
                TypeModel.TEAM_DETAIL -> {
                    parseJsonToObject(
                        JSONObject(jsonString).getJSONArray(
                            Constant.RESPONSE
                        ).getJSONObject(0).getJSONObject(TeamDetailEntry.TEAM),
                        TypeModel.TEAM_DETAIL
                    )
                }
                TypeModel.PLAYER_DETAIL -> {
                    parseJsonToList(
                        JSONObject(jsonString).getJSONArray(Constant.RESPONSE),
                        TypeModel.PLAYER_DETAIL
                    )
                }
                TypeModel.SEASON -> {
                    parseJsonToListString(
                        JSONObject(jsonString).getJSONArray(Constant.RESPONSE)
                    )
                }
                TypeModel.TEAM_SEARCH -> {
                    parseJsonToList(
                        JSONObject(jsonString).getJSONArray(FixtureEntry.RESPONSE),
                        typeModel
                    )
                }
                else -> null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    @Throws(Exception::class)
    private fun parseJsonToObject(json: Any?, typeModel: TypeModel): Any? {
        val parseJsonToModel = ParseJsonToModel()
        return when (typeModel) {
            TypeModel.FIXTURE -> {
                parseJsonToModel.parseJsonToFixture(json as JSONObject?)
            }
            TypeModel.STANDING_LEAGUE -> {
                parseJsonToModel.parseJsonToStandingLeague(json as JSONObject?)
            }
            TypeModel.STANDING_GROUP -> {
                parseJsonToModel.parseJsonToStandingGroup(json as JSONArray?)
            }
            TypeModel.STANDING -> {
                parseJsonToModel.parseJsonToStanding(json as JSONObject?)
            }
            TypeModel.TOP_SCORER -> {
                parseJsonToModel.parseJsonToTopScorer(json as JSONObject?)
            }
            TypeModel.PLAYER_DETAIL -> {
                parseJsonToModel.parseJsonToPLayerDetail(json as JSONObject?)
            }
            TypeModel.TEAM_DETAIL -> {
                parseJsonToModel.parseJsonToTeamDetail(json as JSONObject?)
            }
            TypeModel.TEAM_SEARCH -> {
                parseJsonToModel.parseJsonToTeamSearch(json as JSONObject)
            }
            else -> null
        }
    }

    @Throws(Exception::class)
    private fun parseJsonToList(jsonArray: JSONArray?, typeModel: TypeModel): Any {
        val data = mutableListOf<Any?>()
        for (i in 0 until (jsonArray?.length() ?: 0)) {
            val jsonObject = jsonArray?.getJSONObject(i)
            data.add(parseJsonToObject(jsonObject, typeModel))
        }
        return data.filterNotNull()
    }

    private fun parseJsonToListString(jsonArray: JSONArray?): Any {
        val data = mutableListOf<String>()
        for (i in 0 until (jsonArray?.length() ?: 0)) {
            val stringSeason = jsonArray?.getInt(i).toString()
            data.add(stringSeason)
        }
        return data
    }

    @Throws(Exception::class)
    private fun parseJsonToListJson(jsonArray: JSONArray?, typeModel: TypeModel): Any {
        val data = mutableListOf<Any?>()
        for (i in 0 until (jsonArray?.length() ?: 0)) {
            val jsonObject = jsonArray?.getJSONArray(i)
            data.add(parseJsonToObject(jsonObject, typeModel))
        }
        return data.filterNotNull()
    }

    companion object {
        private const val HEADER_API_KEY = "x-rapidapi-key"
        private const val TIME_OUT = 20000
        private const val METHOD_GET = "GET"
    }
}
