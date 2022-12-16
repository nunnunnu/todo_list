package com.jh_project.todo.todoList.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "todo_images_info")
public class TodoImageEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tii_seq")       private Long seq;
    @Column(name = "tii_ti_seq")    private Long tiSeq;
    @Column(name = "tii_file_name") private String fileName;
    @Column(name = "tii_uri")       private String uri;
}
