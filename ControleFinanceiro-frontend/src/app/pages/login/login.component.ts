import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink], 
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private service: ApiService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(5)]]
    });
  }

  onSubmit() {
    if (this.isValidForm(this.loginForm)) {
      console.log('Enviando dados...', this.loginForm.value);

      this.service.fazerLogin(this.loginForm.value).subscribe({
        next: (resposta: any) => {
          console.log('Sucesso:', resposta);
          alert('Login realizado com sucesso! (Veja o console)');


          this.authService.setUsuario(resposta);

          this.router.navigate(['/home']);
        },
        error: (erro: any) => {
          console.error('Erro:', erro);
          alert('Falha no login. Verifique email e senha.');
        }
      });
    }
  }

  isValidForm(form: FormGroup){
      return form.valid;
    }
}