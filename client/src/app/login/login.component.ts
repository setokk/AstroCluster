import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import {LoginUserRequest} from "../core/request/LoginUserRequest";
import {UserService} from "../core/services/user-service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginUserRequest: LoginUserRequest = {
    username: undefined,
    password: undefined
  }

  constructor(private userService: UserService, private router: Router) {}

  onClickRegisterAnchor() {
    this.router.navigate(['/register']);
  }

  onSubmit(form: NgForm): void {
    this.loginUserRequest.username = form.form.value.username;
    this.loginUserRequest.password = form.form.value.password;

    this.userService.loginUser(this.loginUserRequest).subscribe({
      next: (response) => {
        window.alert(`Login was successful! Welcome back, ${this.loginUserRequest.username}!`);
      },
      error: (error) => {
        window.alert(`Status: ${error.status}\nErrors: ${error.error.errors.join(',\n')}`);
      }
    })
  }
}
