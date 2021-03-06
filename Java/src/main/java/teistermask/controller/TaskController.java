package teistermask.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import teistermask.bindingModel.TaskBindingModel;
import teistermask.entity.Task;
import teistermask.repository.TaskRepository;

import java.util.List;

@Controller
public class TaskController {
	private final TaskRepository taskRepository;

	@Autowired
	public TaskController(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@GetMapping("/")
	public String index(Model model) {
		List<Task> openTasks = taskRepository.findAllByStatus("Open");
		List<Task> inProgressTasks = taskRepository.findAllByStatus("In Progress");
		List<Task> finishedTasks = taskRepository.findAllByStatus("Finished");

		model.addAttribute("openTasks",openTasks);
		model.addAttribute("inProgressTasks",inProgressTasks);
		model.addAttribute("finishedTasks",finishedTasks);
		model.addAttribute("view","task/index");
		return "base-layout";
	}

	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("view","task/create");
		return "base-layout";
	}

	@PostMapping("/create")
	public String createProcess(Model model, TaskBindingModel taskBindingModel) {
		Task taskToAdd = new Task(taskBindingModel.getTitle(),
									taskBindingModel.getStatus());
		taskRepository.saveAndFlush(taskToAdd);


		return "redirect:/";
	}

	@GetMapping("/edit/{id}")
	public String edit(Model model, @PathVariable int id) {
		Task taskToEdit = taskRepository.findOne(id);
		model.addAttribute("task",taskToEdit);
		model.addAttribute("view","task/edit");
		return "base-layout";
	}

	@PostMapping("/edit/{id}")
	public String editProcess(@PathVariable int id, TaskBindingModel taskBindingModel) {
		Task taskToEdit = taskRepository.findOne(id);
		taskToEdit.setStatus(taskBindingModel.getStatus());
		taskToEdit.setTitle(taskBindingModel.getTitle());
		taskRepository.saveAndFlush(taskToEdit);
		return "redirect:/";
	}
}
