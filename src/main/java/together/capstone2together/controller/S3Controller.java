package together.capstone2together.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//@RestController
//@RequiredArgsConstructor
//public class S3Controller {
//
//    private final S3UploadService s3UploadService;
//    @GetMapping("/upload")
//    public void saveFile(@RequestParam("file")MultipartFile file) throws IOException {
//        String url = s3UploadService.saveFile(file);
//        System.out.println("url = " + url);
//    }
//
//}
