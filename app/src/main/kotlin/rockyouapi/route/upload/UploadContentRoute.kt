package rockyouapi.route.upload

import common.utils.generateRandomTextByUUID
import common.utils.megabytesToBytes
import database.external.contract.ProductionDatabaseAPI
import database.external.result.common.SimpleUnitResult
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import rockyouapi.route.Routes
import rockyouapi.security.CLAIM_KEY_TOKEN_TYPE
import rockyouapi.security.checkTokenType
import rockyouapi.security.tryToExtractUserID
import rockyouapi.utils.*
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * Route to upload content to server.
 *
 * Respond as:
 * - [HttpStatusCode.OK] Comment added. Respond only with code.
 * - [HttpStatusCode.InternalServerError] If smith unexpected happens on database query stage.
 * */
internal fun Route.uploadContentRoute(
    productionDatabaseAPI: ProductionDatabaseAPI,
    maximumFilesToUploadAtOnceNumber: Int,
    availableFileSizeInMB: Int,
    uploadFolderPath: String
) {

    authenticate(Routes.JWT_AUTHORIZATION_PROVIDER_KEY, optional = true) {

        post(Routes.Upload.path) {

            val multipart = call.receiveMultipart()

            val bodyParts = multipart.readAllParts()
            if (bodyParts.countFilePartData() > maximumFilesToUploadAtOnceNumber) {
                call.respondAsTooMuchFiles(maximumFilesToUploadAtOnceNumber)
                return@post
            }

            val principal = call.principal<JWTPrincipal>()

            principal?.checkTokenType(
                tokenTypeClaimKey = CLAIM_KEY_TOKEN_TYPE,
                onClaimNotPresented = {
                    call.respondAsTokenHasNoType()
                    return@post
                },
                onRefreshType = {
                    call.respondAsTokenHasRefreshType()
                    return@post
                },
                onUnknownType = {
                    call.respondAsTokenHasInvalidType()
                    return@post
                }
            )

            val userID = principal?.tryToExtractUserID {
                call.respondAsTokenHasNoOwner()
                return@post
            }

            val fileSizeLimit = megabytesToBytes(availableFileSizeInMB)

            bodyParts.forEach { partData ->
                if (partData is PartData.FileItem) {

                    // Create file
                    val fileName = generateRandomTextByUUID()
                    val file = File("$uploadFolderPath$fileName")

                    // Create data streams
                    val partDataInputStream = partData.streamProvider()
                    val fileOutputStream = file.outputStream().buffered()

                    // Write content to file
                    partDataInputStream.use { userFileInputStream ->
                        fileOutputStream.use { serverFileOutputStream ->
                            userFileInputStream.copyTo(
                                out = serverFileOutputStream,
                                onBytesRead = { totalBytes ->
                                    if (totalBytes > fileSizeLimit) {
                                        call.respondAsFileTooBig(
                                            fileName = partData.originalFileName.orEmpty(),
                                            sizeLimit = availableFileSizeInMB
                                        )
                                        return@post
                                    }
                                }
                            )
                        }
                    }

                    val createUploadContentRequestResult = productionDatabaseAPI.createUploadContentRequest(
                        fileName = fileName,
                        userID = userID
                    )

                    when (createUploadContentRequestResult) {
                        is SimpleUnitResult.Error -> {
                            val exception = createUploadContentRequestResult.t
                            call.logErrorToFile("Failed to insert record about downloaded file to database.", exception)
                            call.respondAsErrorByException(createUploadContentRequestResult.t)
                        }

                        is SimpleUnitResult.Ok -> Unit
                    }
                }
            }

            call.respondAsOkWithUnit()
        }
    }
}

private fun List<PartData>.countFilePartData() = count { it is PartData.FileItem }

private suspend fun ApplicationCall.respondAsTooMuchFiles(maximumFilesNumber: Int) {
    respondAsError(
        errorCode = HttpStatusCode.BadRequest,
        errorText = "Too much files to upload. Only $maximumFilesNumber at once available."
    )
}

private suspend fun ApplicationCall.respondAsFileTooBig(fileName: String, sizeLimit: Int) {
    respondAsError(
        errorCode = HttpStatusCode.BadRequest,
        errorText = "File $fileName too big. Maximum file size: $sizeLimit MB."
    )
}

/**
 * Same as [kotlin.io.copyTo], but invoke action with total copied bytes after every read bytes action.
 * Needed to caller can know how much exactly bytes was read.
 * */
private inline fun InputStream.copyTo(out: OutputStream, onBytesRead: (totalCopiedBytes: Long) -> Unit): Long {
    var bytesCopied: Long = 0
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    var bytes = read(buffer)
    while (bytes >= 0) {
        out.write(buffer, 0, bytes)
        bytesCopied += bytes
        bytes = read(buffer)
        onBytesRead(bytesCopied)
    }
    return bytesCopied
}