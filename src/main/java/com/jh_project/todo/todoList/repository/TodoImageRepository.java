package com.jh_project.todo.todoList.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jh_project.todo.todoList.entity.TodoImageEntity;

@Repository
public interface TodoImageRepository extends JpaRepository<TodoImageEntity, Long> {
    public List<TodoImageEntity> findByTiSeq(Long tiSeq);
    //select * from todo_image_info where tii_uri = uri order by tii_seq desc limit 1;
    //가장 나중에 입력된 이미지를 가져옴.
    public List<TodoImageEntity> findTopByUriOrderBySeqDesc(String uri);
}
