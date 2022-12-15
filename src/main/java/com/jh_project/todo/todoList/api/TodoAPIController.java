package com.jh_project.todo.todoList.api;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jh_project.todo.todoList.entity.TodoInfoEntity;
import com.jh_project.todo.todoList.service.TodoInfoService;

@RestController
@RequestMapping("/api/todo")
public class TodoAPIController {
    @Autowired TodoInfoService tService;
    @PutMapping("/add")
    public ResponseEntity<Object> addTodo(@RequestBody TodoInfoEntity data, HttpSession session){
        Map<String, Object> map = tService.addTodoList(data, session);
        
        return new ResponseEntity<>(map, (HttpStatus)map.get("code"));
    }
    @GetMapping("/list")
    public ResponseEntity<Object> getTodoList(HttpSession session){
        Map<String, Object> map = tService.getTodoList(session);
        return new ResponseEntity<>(map, (HttpStatus)map.get("code"));
    }
    @PatchMapping("/update/{type}")
    public ResponseEntity<Object> updateTodo(
        @RequestParam Long seq,
        @PathVariable String type,
        @RequestParam String value
    ){
        if(type.equals("status")){
            Map<String, Object> map = tService.updateTodoStatus(Integer.parseInt(value), seq);
            return new ResponseEntity<>(map, (HttpStatus)map.get("code"));
        }else if(type.equals("content")){
            Map<String, Object> map = tService.updateTodoContent(value, seq);
            return new ResponseEntity<>(map, (HttpStatus)map.get("code"));
        }else{
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("stauts", false);
            map.put("message", "type은 status, content 둘 중 한가지만 가능합니다");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            
        }
    }
    // @DeleteMapping("/delete")
    // @Transactional
    // public ResponseEntity<Object> deleteTodo(@RequestParam Long seq, @RequestParam Long miSeq){
    //     Map<String, Object> map = tService.deleteTodo(seq, miSeq);
    //     return new ResponseEntity<>(map, (HttpStatus)map.get("code"));
    // }
    
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteTodo(@RequestParam Long seq, HttpSession session){
        Map<String, Object> map = tService.deleteTodo(seq, session);
        return new ResponseEntity<>(map, (HttpStatus)map.get("code"));
    }
    @GetMapping("/list/term")
    public ResponseEntity<Object> getDetailTodoList(HttpSession session, String start, String end){
        Map<String, Object> map = tService.selectTodoListByTrem(session, start, end);
        return new ResponseEntity<>(map, (HttpStatus)map.get("code"));
    }
    
}
