package com.example.fe.myapplication.model

/**
 * @author Chris
 * @version 1.0
 * @since 2024/09/14
 */
open class ApiResponse<T> : java.io.Serializable {
    constructor() { /* compiled code */
    }

    constructor(data: T?, statusCode: Int, comments: String?) { /* compiled code */
    }

    var comments: String? = null /* compiled code */

    var data: T? = null /* compiled code */

    var ext: Any? = null /* compiled code */

    val isSuccess: Boolean  /* compiled code */
        get() = this.statusCode == 200

    private var statusCode: Int = 200 /* compiled code */

}

