package app.horecafy.com.services.responses

import app.horecafy.com.models.ReviewOfferResponseItems


data class ReviewOfferResponse(override val totalRows: Int,
                               override val page: Int,
                               override val rows: Int,
                               override val error: String,
                               override val data: List<ReviewOfferResponseItems>) : BaseResponse<ReviewOfferResponseItems>
