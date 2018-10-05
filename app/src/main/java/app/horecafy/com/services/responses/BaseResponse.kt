package app.horecafy.com.services.responses


interface BaseResponse<T> {
    val totalRows: Int
    val page: Int
    val rows: Int
    val error: String
    val data: List<T>
}

