package com.jh_project.todo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jh_project.todo.todoList.entity.TodoInfoEntity;
import com.jh_project.todo.todoList.repository.TodoRepository;
import com.jh_project.todo.todoList.service.TodoInfoService;

@SpringBootTest
class TodoApplicationTests {

	@Autowired TodoRepository tRepo;

	@Test
	void loadTodo() throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date start = format.parse("2022-12-27");
		Date end = format.parse("2022-12-30");
		List<TodoInfoEntity> list = tRepo.findByEndDtBetweenAndMiSeq(start, end, 2L);
		for(TodoInfoEntity t : list){
			System.out.println(t);
		}
	}

}
