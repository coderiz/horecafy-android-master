package app.horecafy.com.services.responses

import app.horecafy.com.models.SearchProductResponse


data class RequestProductResponse(override val totalRows: Int,
                                  override val page: Int,
                                  override val rows: Int,
                                  override val error: String,
                                  override val data: List<SearchProductResponse>) : BaseResponse<SearchProductResponse>
