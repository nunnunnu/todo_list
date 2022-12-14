package com.jh_project.todo.todoList.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.jh_project.todo.member.entity.MemberInfoEntity;
import com.jh_project.todo.todoList.entity.TodoInfoEntity;
import com.jh_project.todo.todoList.repository.TodoRepository;

@Service
public class TodoInfoService {
    @Autowired TodoRepository t_repo;

    public Map<String, Object> addTodoList(TodoInfoEntity data, HttpSession session){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        MemberInfoEntity loginUser = (MemberInfoEntity)session.getAttribute("loginUser");
        if(loginUser==null){
            map.put("status", false);
            map.put("message", "로그인이 필요합니다.");
            map.put("code", HttpStatus.FORBIDDEN);
        }else{
            data.setMiSeq(loginUser.getSeq());
            t_repo.save(data);
            map.put("status", true);
            map.put("message", "일정이 추가되었습니다.");
            map.put("code", HttpStatus.CREATED);
        }
        return map;
    }
    public Map<String, Object> getTodoList(HttpSession session){
        Map<String, Object> map = new LinkedHashMap<>();
        MemberInfoEntity loginUser = (MemberInfoEntity)session.getAttribute("loginUser");
        if(loginUser==null){
            map.put("status", false);
            map.put("message", "로그인이 필요합니다.");
            map.put("code", HttpStatus.FORBIDDEN);
        }else{
            map.put("list", t_repo.findAllByMiSeq(loginUser.getSeq()));
            map.put("status", true);
            map.put("message", "조회하였습니다.");
            map.put("code", HttpStatus.OK);
            
        }
        return map;
    }
    public Map<String, Object> updateTodoStatus(Integer status, Long seq){
        Map<String, Object> map = new LinkedHashMap<>();
        TodoInfoEntity todo = t_repo.findBySeq(seq);
        if(todo==null){
            map.put("status", false);
            map.put("message", "잘못된 todo번호입니다.");
            map.put("code", HttpStatus.FORBIDDEN);
        }else{
            todo.setStatus(status);
            t_repo.save(todo);
            map.put("status", true);
            map.put("message", "todo 상태가 변경되었습니다.");
            map.put("code", HttpStatus.OK);
        }
        
        return map;
    }
    public Map<String, Object> updateTodoContent(String content, Long seq){
        Map<String, Object> map = new LinkedHashMap<>();
        TodoInfoEntity todo = t_repo.findBySeq(seq);
        if(todo==null){
            map.put("status", false);
            map.put("message", "잘못된 todo번호입니다.");
            map.put("code", HttpStatus.FORBIDDEN);
        }else{
            todo.setContent(content);
            t_repo.save(todo);
            map.put("status", true);
            map.put("message", "todo 상태가 변경되었습니다.");
            map.put("code", HttpStatus.OK);
        }
        return map;
    }
    // public Map<String, Object> deleteTodo(Long seq, Long miSeq){
    //     Map<String, Object> map = new LinkedHashMap<>();
    //     TodoInfoEntity todo = t_repo.findBySeqAndMiSeq(seq, miSeq);
    //     if(todo==null){
    //         map.put("status", false);
    //         map.put("message", "회원번호가 일치하지않습니다.");
    //         map.put("code", HttpStatus.FORBIDDEN);
    //     }else{
    //         t_repo.deleteBySeqAndMiSeq(seq, miSeq);
    //         map.put("status", true);
    //         map.put("message", "todo가 삭제되었습니다.");
    //         map.put("code", HttpStatus.OK);
    //     }
    //     return map;
    // }
    @Transactional
    public Map<String, Object> deleteTodo(Long seq, HttpSession session){
        Map<String, Object> map = new LinkedHashMap<>();
        MemberInfoEntity loginUser = (MemberInfoEntity)session.getAttribute("loginUser");
        if(loginUser==null){
            map.put("status", false);
            map.put("message", "로그인 후 사용가능한 기능입니다.");
            map.put("code", HttpStatus.FORBIDDEN);
            return map;
        }
        TodoInfoEntity todo = t_repo.findBySeqAndMiSeq(seq, loginUser.getSeq());
        if(todo==null){
            map.put("status", false);
            map.put("message", "잘못된 Todo 번호입니다..");
            map.put("code", HttpStatus.FORBIDDEN);
        }else{
            t_repo.deleteBySeqAndMiSeq(seq, loginUser.getSeq());
            map.put("status", true);
            map.put("message", "todo가 삭제되었습니다.");
            map.put("code", HttpStatus.OK);
        }
        return map;
    }
    public Map<String, Object> selectTodoListByTrem(HttpSession session, String start, String end){
        Map<String, Object> map = new LinkedHashMap<>();
        MemberInfoEntity loginUser = (MemberInfoEntity)session.getAttribute("loginUser");
        if(loginUser==null){
            map.put("status", false);
            map.put("message", "로그인 후 사용가능한 기능입니다.");
            map.put("code", HttpStatus.FORBIDDEN);
            return map;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
        Date startDt=null;
        Date endDt=null;
        try{
            startDt = format.parse(start);
            endDt = format.parse(end);
        }catch(Exception e){
            map.put("status", false);
            map.put("message", "날짜 형식을 확인해주세요(yyMMdd ex:221214)");
            map.put("code", HttpStatus.BAD_REQUEST);
            return map;
            
        }
        List<TodoInfoEntity> list = t_repo.findByEndDtBetweenAndMiSeq(startDt, endDt, loginUser.getSeq());
        if(list.size()==0){
            map.put("status", false);
            map.put("message", "일치하는 Todo가 없습니다. 날짜를 확인해주세요");
            map.put("code", HttpStatus.FORBIDDEN);
        }else{
            map.put("list", list);
            map.put("status", true);
            map.put("message", "조회완료되었습니다.");
            map.put("code", HttpStatus.OK);
        }
        return map;
    }
}
