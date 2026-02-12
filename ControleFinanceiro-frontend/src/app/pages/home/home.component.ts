import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators, FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { UsuarioInterface } from '../../interfaces/usuario';

@Component({
  selector: 'app-home',
  imports: [RouterLink, CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit{
  usuarioStorage: UsuarioInterface | null = null;
  userEditForm: FormGroup;
  categoryForm: FormGroup;

  exibirModal = false;
  tipoModal: 'usuario' | 'receita' | 'despesa' | 'categoria' = 'usuario';

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private cd: ChangeDetectorRef,
    private service: ApiService,
    private router: Router
  ){
    this.userEditForm = this.fb.group({
      name: ['', [Validators.minLength(3), Validators.pattern(/^[a-zA-ZÀ-ÿ]+\s+[a-zA-ZÀ-ÿ]+.*$/)]],
      email: ['', [Validators.email]],
      password: ['', [Validators.minLength(5)]]
  }),
    this.categoryForm = this.fb.group({
      name: ['', Validators.pattern(/^[a-zA-ZÀ-ÿ]+\s+[a-zA-ZÀ-ÿ]+.*$/)]
    })
  }

  ngOnInit() {
    this.usuarioStorage = this.authService.getUsuario();

    console.log('Aqui esta como é armazenado o usuario', this.usuarioStorage?.name);
    console.log('O tipo de usuarioStorage é', typeof this.usuarioStorage);
  }

  abrirModal(tipo: 'usuario' | 'receita' | 'despesa' | 'categoria'){
    this.tipoModal = tipo;
    this.exibirModal = true;
  }

  fecharModal(){
    this.exibirModal = false;
  }

  onSubmit(){

    if(this.isFormValid(this.userEditForm)){

      this.service.updateUserInfos(this.userEditForm.value, this.usuarioStorage?.id).subscribe({
        next: (resposta: any) => {
          alert('Seus dados foram atualizados com sucesso');
          console.log('Os seus dados agora são', resposta);

          this.router.navigate(['/login']);
        },
        error: (erro: any) => {
          alert('Ocorreu um erro ao atualizar seus dados');
        }
      }); 
    }
  }

  isFormValid(form: FormGroup){
    return form.valid;
  }

  logOut(){
    this.authService.logout();

    this.router.navigate(['/login']);
  }
}
