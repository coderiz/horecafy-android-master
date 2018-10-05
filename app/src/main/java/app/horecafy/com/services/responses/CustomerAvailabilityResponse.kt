package app.horecafy.com.services.responses

import app.horecafy.com.models.CustomerAvailability


data class CustomerAvailabilityResponse(val totalRows: Int,
                                        val page: Int,
                                        val rows: Int,
                                        val error: String,
                                        val data: CustomerAvailability)
