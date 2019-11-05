package univ.lorraine.simpleChat.SimpleChat.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import univ.lorraine.simpleChat.SimpleChat.model.Role;
import univ.lorraine.simpleChat.SimpleChat.service.RoleService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/role-test")
public class RoleTestController {
	
	private final RoleService roleService;

	@Autowired
	public RoleTestController(RoleService roleService) {
		this.roleService = roleService;
	}

	@GetMapping("/add/role/{name}")
	public Role add(HttpServletRequest request, @PathVariable String name) 
	{
		return this.roleService.create(name);
	}
}
