package com.emailrockets.demo.model

import java.time.LocalDateTime

class BaseEntity(val createdDate: LocalDateTime? = null,
                 val updatedDate: LocalDateTime? = null) {
}