package com.emailrockets.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Exception yerine RuntimeException sınıfından extends etmemizin sebebi
// GeneralExceptionda karşılık gelen hatayı oluşturuyoruz ve hatayı fırlatırken bunu gösterebiliyoruz. Diğer türlü thereadı öldürdüğümüzden hata fırlatma sıkıntı olacaktı.
// Yani usernotfound hatası fırlatsın ama benim belirlediğim body gitmsin demiş oluyoruz. Runtime olursa thread öleceğinden hata fırlatacaktı.
// Diğer bir sonuç olarak; normal Exception classıyla extend ettiğimizde userservicede hata olarak döndüğümüzde bu therad biteceğinden alttaki farklı işlemleri bu threadda yapamayacağımızdan yeni thread oluşturmamız lazımdır.

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
