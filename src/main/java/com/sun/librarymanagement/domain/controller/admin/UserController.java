package com.sun.librarymanagement.domain.controller.admin;

import com.sun.librarymanagement.domain.dto.response.PaginatedResponseDto;
import com.sun.librarymanagement.domain.dto.response.UserResponseDto;
import com.sun.librarymanagement.domain.service.UserService;
import com.sun.librarymanagement.utils.ApiPaths;
import com.sun.librarymanagement.utils.Constant;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("adminUserController")
@RequestMapping(ApiPaths.USERS_ADMIN)
@AllArgsConstructor
public class UserController extends AdminController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<PaginatedResponseDto<UserResponseDto>> getUsers(
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_NUMBER, name = Constant.PAGE_NUMBER_PARAM) int pageNumber,
        @RequestParam(defaultValue = Constant.DEFAULT_PAGE_SIZE, name = Constant.PAGE_SIZE_PARAM) int pageSize
    ) {
        return ResponseEntity.ok(userService.getUsers(pageNumber, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping("/{id}/active")
    public ResponseEntity<UserResponseDto> activeUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.activeUser(id));
    }

    @PostMapping("/{id}/inactive")
    public ResponseEntity<UserResponseDto> inactiveUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.inactiveUser(id));
    }
}
