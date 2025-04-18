package com.sun.librarymanagement.domain.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ADMIN')")
public abstract class AdminController {
}
