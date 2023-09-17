package rockyouapi.routes

import com.mysql.cj.jdbc.MysqlDataSource
import database.external.DatabaseFeature
import rockyouapi.route.Routes
import declaration.entity.Picture
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import rockyouapi.*
import rockyouapi.runTestInConfiguredApplicationWithDBConnection
import kotlin.test.Test

/** @see rockyouapi.route.picture.pictureListRoute */
internal class ReportTest {

    @Test
    fun pictures_list_without_arguments_must_return_400() {

        val db = DatabaseFeature.connectToTestDatabase1()

        GlobalScope.launch {
            val res1 = db.report(1, "", null)
        }
    }


    private suspend fun HttpClient.makePictureListRequest(langID: String?, limit: String?, offset: String?) =
        get(Routes.PictureList.path) {
            url {
                langID?.let { parameters.append(Routes.PictureList.getLangIDArgName(), langID) }
                limit?.let { parameters.append(Routes.PictureList.getLimitArgName(), limit) }
                offset?.let { parameters.append(Routes.PictureList.getOffsetArgName(), offset) }
            }
        }
}