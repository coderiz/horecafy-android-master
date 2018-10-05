package app.horecafy.com.services.responses

import app.horecafy.com.models.Category


data class CategoryGetResponse(override val totalRows: Int,
                               override val page: Int,
                               override val rows: Int,
                               override val error: String,
                               override val data: MutableList<Category>) : BaseResponse<Category>