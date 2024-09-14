package com.example.fe.myapplication.model


/**
 * @author Chris
 * @version 1.0
 * @since 2024/09/14
 */
class ApiResponseExt<T> : ApiResponse<T> {
    constructor() : super()
    constructor(data: T?, statusCode: Int, comments: String?) : super(data, statusCode, comments)

    var pagination: PaginationEn? = null
}