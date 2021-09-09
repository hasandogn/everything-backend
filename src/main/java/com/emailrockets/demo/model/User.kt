package com.emailrockets.demo.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

// Eğer var yaparsak gettter methodlarını getirir
// ama eğer val yaparsak full immutable class yapmış oluruz
// class yazarsak gerçek class oluır içerisinde compltıbıl yapabileceğimiz şekilde
// ama setter getter methodlar gelmez
// ama classın başına data eklersek ( data class) içerisinde sadece data barındıran model sınıfına dönüşür ve bütün boilerplateni getirir.

data class Userss(
        @field:Id
        @field:GeneratedValue(strategy = GenerationType.IDENTITY)
        val Id : Long? = null,
        val mail: String,
        val firstName: String,
        val lastName: String,
        val middleName: String
) {
        constructor(mail:String,
                    firstName: String,
                    lastName:String,
                    middleName: String
                    ) : this(null, mail, firstName, lastName, middleName){
                            
                    }
}