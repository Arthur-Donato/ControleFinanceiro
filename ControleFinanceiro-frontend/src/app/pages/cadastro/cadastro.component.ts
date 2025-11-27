import { Component } from '@angular/core';
import {ReactiveFormsModule } from "@angular/forms";
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-cadastro',
  imports: [ReactiveFormsModule, RouterLink, CommonModule],
  templateUrl: './cadastro.component.html',
  styleUrl: './cadastro.component.css',
})
export class CadastroComponent {
  cadastroForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private service: ApiService,
    private router: Router
  ){
    this.cadastroForm = this.fb.group({
      nomeCompleto: ['', [Validators.required, Validators.minLength(3), Validators.pattern(/^[a-zA-ZÀ-ÿ]+\s+[a-zA-ZÀ-ÿ]+.*$/)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(5)]]
    })
  }

  onSubmit(){
    if (this.cadastroForm.valid){
      console.log('Enviando dados...', this.cadastroForm.value);

      this.service.fazerCadastro(this.cadastroForm.value).subscribe({
        next: (resposta: any) => {
          console.log('O usuário foi cadastrado com sucesso!');
          alert('Cadastro realizado com sucesso!');


        },
        error: (erro: any) => {
          console.log('Ocorreu um erro ao realizar o cadastro', erro);
          alert('Falha ao relizar cadastro');
        }

      });
    }
  }

}
