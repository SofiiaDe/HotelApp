//package com.epam.javacourse.hotelapp.controller;
//
//import com.epam.javacourse.hotelapp.service.TranslationService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/index")
//public class IndexController {
//
//    private final TranslationService translationService;
//
//    public IndexController(TranslationService translationService) {
//        this.translationService = translationService;
//    }
//
//
//    @GetMapping("/translation")
//    public ResponseEntity<String> getTranslation() {
//        String translation = translationService.translate();
//        return ResponseEntity.ok(translation);
//    }
//}
