import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import ConstRoutes from "../../shared/contants/const-routes";
import {FormsModule} from "@angular/forms";
import {
  storeUser,
  isOkResponse,
  loadResponseData,
  loadResponseError
} from "../../core/services/utils.service";
import { LoginService } from 'src/app/core/services/login.service';
import { ButtonComponent } from 'src/app/shared/components/button-component/button-component';
import {UserResponseDTO} from "../../interfaces/UserResponseDTO";
import {CommonModule} from "@angular/common";
import {InputComponent} from "../../shared/components/input.component/input.component";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [
      FormsModule, ButtonComponent, CommonModule, InputComponent
  ],
  standalone: true,
})
export class LoginComponent implements OnInit {
  username = '';
  password = '';
  error = '';
  usuarioLogado: UserResponseDTO;
  focused: boolean;

  constructor(private loginService: LoginService, private router: Router) {

  }

  ngOnInit(): void {
    localStorage.clear();
  }

  onLogin() {
     this.loginService.doLogin(this.username, this.password).subscribe({
       next: (response) => {
          if (isOkResponse(response)) {
            this.usuarioLogado = loadResponseData(response);
            storeUser(this.usuarioLogado);
            this.router.navigate([ConstRoutes.PATH_HOME]);
          } else {
            this.error = loadResponseError(response);
          }
        },
        error: (err) => {
          if (err.status === 404) {
            alert("Usuario o password no validos");
          }
        }
    });
  }

  onchangeUsername($event) {
    this.username = $event;
  }
  onchangePassword($event) {
    this.password = $event;
  }


  goToRegister() {
    this.router.navigate(['/register']);
  }
}
