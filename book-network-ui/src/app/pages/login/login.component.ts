import { Component } from '@angular/core';
import { AuthenticationRequest } from '../../services/models';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services';
import { TokenService } from '../../services/token/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  authRequest: AuthenticationRequest = {
    email: '',
    password: ''
  };
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService
  ) {}

  login(): void {
    this.errorMsg = []; // if there's an error logging in, it's important to reset the errorMsg afterwards since the next login might be successful
    // since the authenticate service returns an 'Observable', we should use 'subscribe'
    this.authService.authenticate({
      body: this.authRequest
    }).subscribe({
      next: (res) => {
        this.tokenService.token = res.token as string; // need to cast since this token is of type 'string' | 'undefined'
        this.router.navigate(['books']);
      },
      error: (err) => {
        console.error(err);
        if (err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors;
        } else {
          this.errorMsg.push(err.error.error);
        }
      }
    });
  }

  register(): void {
    this.router.navigate(['register']);
  }
}
