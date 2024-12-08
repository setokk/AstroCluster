import { Component } from '@angular/core';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import { BackgroundVideoComponent } from './background-video/background-video.component';
import { FormsModule } from '@angular/forms';
import {Constants} from "./core/services/constants";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    BackgroundVideoComponent,
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    FormsModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  protected readonly localStorage = localStorage;
  protected readonly Constants = Constants;

  constructor(private router: Router) {}

  onClickLogout() {
    localStorage.removeItem(Constants.JWT_TOKEN_STORAGE_KEY);
    this.router.navigate(['/']);
  }
}
