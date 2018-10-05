package app.horecafy.com.services.responses

import app.horecafy.com.models.NotificationsDetails


data class NotificationsResponse(override val totalRows: Int,
                                 override val page: Int,
                                 override val rows: Int,
                                 override val error: String,
                                 override val data: List<NotificationsDetails>) : BaseResponse<NotificationsDetails>
