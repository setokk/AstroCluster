import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import {RegisterUserRequest} from "../core/request/RegisterUserRequest";
import {UserService} from "../core/services/user-service";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registerUserRequest: RegisterUserRequest = {
    username: undefined,
    email: undefined,
    password: undefined
  }

  constructor(private userService: UserService, private router: Router) {}

  onClickLoginAnchor() {
    this.router.navigate(['/log-in']);
  }

  onSubmit(form: NgForm): void {
    this.registerUserRequest.username = form.form.value.username;
    this.registerUserRequest.email = form.form.value.email;
    this.registerUserRequest.password = form.form.value.password;

    this.userService.registerUser(this.registerUserRequest).subscribe({
      next: (response) => {
        window.alert(`Registration was successful! Welcome, ${this.registerUserRequest.username}!`);
      },
      error: (error) => {
        window.alert(`Status: ${error.status}\nErrors: ${error.error.errors.join(',\n')}`);
      }
    })
  }
}
