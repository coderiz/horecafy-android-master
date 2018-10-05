package app.horecafy.com.services.responses

import app.horecafy.com.models.Password


data class ForgotPasswordResponse(override val totalRows: Int,
                                  override val page: Int,
                                  override val rows: Int,
                                  override val error: String,
                                  override val data: List<Password>) : BaseResponse<Password>
