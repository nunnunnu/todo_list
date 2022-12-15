package com.jh_project.todo.todoList.file.api;

import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jh_project.todo.member.entity.MemberImageEntity;
import com.jh_project.todo.member.service.MemberService;
import com.jh_project.todo.todoList.entity.TodoImageEntity;
import com.jh_project.todo.todoList.service.TodoInfoService;

@RestController
public class FileAPIController {
    // 파일 업로드, 다운로드는 어느 프로젝프든 거의 동일함. 복붙추천
    @Value("${file.image.todo}") String todo_img_path; //springframework.beans임
    @Value("${file.image.member}") String member_img_path;
    //이것도 DI임. 이미지 파일의 경로가 바뀌어도 application.properties만 고쳐주면 됨
    @Autowired TodoInfoService tService;
    @Autowired MemberService mService;

    @PutMapping("/{type}/upload") //todo이미지를 올릴것인지 file이미지를 올릴것인지
    public ResponseEntity < Object > putImageUpload(
        @PathVariable String type,
        @RequestPart MultipartFile file, //파일을 받는 객체. postman에 file이라고 변수 그대로 적어주어야함
        @RequestParam Long seq
    ) {
        Map < String, Object > map = new LinkedHashMap < > ();
        System.out.println(file.getOriginalFilename()); //업로드 할 파일의 원본이름 확장자까지 출력
        //Path - 폴더 및 파일의 위치를 나타내는 객체, Paths - 폴더 및 파일을 가져오고 경로를 만들기 위한 파일 유틸리티 클래스
        Path folderLocation = null; //todo_img_path 문자열로부터 실제 폴더 경로를 가져옴.
        if (type.equals("todo")) {
            folderLocation = Paths.get(todo_img_path);

        } else if (type.equals("member")) {
            folderLocation = Paths.get(member_img_path);
        } else {
            map.put("status", false);
            map.put("message", "타입정보가 잘못되었습니다. ex:/todo/upload, /member/upload");
            return new ResponseEntity < > (map, HttpStatus.BAD_REQUEST);
        }
        String originFileName = file.getOriginalFilename();
        String[] split = originFileName.split(("\\.")); //.을 기준으로 나눔
        String ext = split[split.length - 1]; //확장자
        String fileName = "";
        for (int i = 0; i < split.length - 1; i++) {
            fileName += split[i]; //원래 split[i]+"." 이렇게 해줘야함
        }
        String saveFileName = type + "_"; //보통 원본 이름을 저장하는것이아니라 시간대를 입력함
        Calendar c = Calendar.getInstance();
        saveFileName += c.getTimeInMillis() + "." + ext; // todo_161310135.png 이런식으로 저장됨

        Path targetFile = folderLocation.resolve(saveFileName); //폴더 경로와 파일의 이름을 합쳐서 목표 파일의 경로 생성
        try {
            //Files는 파일 처리에 대한 유틸리티 클래스
            //copy - 복사, file.getInputStream() - 파일을 열어서 파일의 내용을 읽는 준비
            //targetFile 경로로, standardCopyOption.REPLACE_EXISTING - 같은 파일이 있다면 덮어쓰기.
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (type.equals("todo")) {
            TodoImageEntity data = new TodoImageEntity();
            data.setFileName(saveFileName);
            data.setUri(fileName);
            tService.addTodoImage(data, seq);
        } else if (type.equals("member")) {
            MemberImageEntity data = new MemberImageEntity();
            data.setFileName(saveFileName);
            data.setUri(fileName);
            mService.addMemberImage(data, seq);

        }
        return new ResponseEntity < > (map, HttpStatus.OK);
    } //파일 업로드 메소드

    @GetMapping("/images/{type}/{uri}")
    public ResponseEntity getImage(
        @PathVariable String uri, HttpServletRequest request,
        @PathVariable String type
    ) throws Exception {
        // todo_img_path 문자열로부터 실제 폴더 경로를 가져온다. 
        Path folderLocation = null;
        if (type.equals("todo")) {
            folderLocation = Paths.get(todo_img_path);
        } else if (type.equals("member")) {
            folderLocation = Paths.get(member_img_path);
        }
        String filename = null;
        if (type.equals("todo")) {
            filename = tService.getFileNameByUri(uri);
        } else if (type.equals("member")) {
            filename = mService.getFileNameByUri(uri);
        }
        String[] split = filename.split("\\.");
        String ext = split[split.length - 1];
        String exportName = uri + "." + ext;
        // 내보낼 파일의 이름을 만든다. 
        // 폴더 경로와 파일의 이름을 합쳐서 목표 파일의 경로를 만든다. 
        Path targetFile = folderLocation.resolve(filename);
        // 다운로드 가능한 형태로 변환하기 위한 Resource 객체 생성 
        Resource r = null;
        try {
            // 일반파일 -> Url로 첨부 가능한 형태로 변환 
            r = new UrlResource(targetFile.toUri());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 첨부된 파일의 타입을 저장하기위한 변수 생성 
        String contentType = null;
        try {
            // 첨부할 파일의 타입 정보 산출 
            contentType = request.getServletContext().getMimeType(r.getFile().getAbsolutePath());
            // 산출한 파일의 타입이 null 이라면 
            if (contentType == null) {
                // 일반 파일로 처리한다. 
                contentType = "application/octet-stream";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
            // 응답의 코드를 200 OK로 설정하고 
            // 산출한 타입을 응답에 맞는 형태로 변환 
            .contentType(MediaType.parseMediaType(contentType))
            // 내보낼 내용의 타입을 설정 (파일), 
            // attachment; filename*=\""+r.getFilename()+"\" 요청한 쪽에서 다운로드 한 
            // 파일의 이름을 결정 
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(exportName, "UTF-8") + "\"")
            .body(r);
        // 변환된 파일을 ResponseEntity에 추가 }



    }
}