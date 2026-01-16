import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { RouterLink } from '@angular/router';

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
    private fb: FormBuilder,
    private service: ApiService,
    private router: Router
  ) {
    // Definimos os campos que o Java espera (email e password)
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(5)]]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      console.log('Enviando dados...', this.loginForm.value);

      this.service.fazerLogin(this.loginForm.value).subscribe({
        next: (resposta: any) => {
          console.log('Sucesso:', resposta);
          alert('Login realizado com sucesso! (Veja o console)');
          this.router.navigate(['/home']); // Descomente quando criar a home
        },
        error: (erro: any) => {
          // Erro
          console.error('Erro:', erro);
          alert('Falha no login. Verifique email e senha.');
        }
      });
    }
  }
}