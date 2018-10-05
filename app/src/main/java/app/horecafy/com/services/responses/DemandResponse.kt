package app.horecafy.com.services.responses

import app.horecafy.com.models.Demand


data class DemandResponse(override val totalRows: Int,
                          override val page: Int,
                          override val rows: Int,
                          override val error: String,
                          override val data: List<Demand>) : BaseResponse<Demand>