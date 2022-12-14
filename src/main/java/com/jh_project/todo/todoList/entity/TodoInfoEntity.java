package com.jh_project.todo.todoList.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor
@Entity(name = "todo_info")
public class TodoInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ti_seq") private Long seq;
    @Column(name = "ti_mi_seq") private Long miSeq;
    @Column(name = "ti_content") private String content;
    @Column(name = "ti_status") private Integer status;
    @Column(name = "ti_start_dt") private Date startDt;
    @Column(name = "ti_end_dt") private Date endDt;

    
}
