//package com.nectux.mizan.hyban.paie.web;
//
//import com.nectux.mizan.hyban.paie.service.CarboneService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/report")
//public class ReportController {
//
//    @Autowired
//    private CarboneService carboneService;
//
//    @PostMapping
//    public ResponseEntity<byte[]> generate(@RequestBody Long  ibBull) throws Exception {
//
//      //  byte[] file = carboneService.generateBulletin(Map<String, Object> data,);
//        byte[] file = "".getBytes();
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bulletin.xlsx")
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(file);
//    }
//}
