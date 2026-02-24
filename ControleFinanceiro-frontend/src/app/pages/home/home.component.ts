import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiUserService } from '../../services/api-user.service';
import { ApiCategoryService } from '../../services/api-category';
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
    private userService: ApiUserService,
    private categoryService: ApiCategoryService,
    private router: Router
  ){
    this.userEditForm = this.fb.group({
      name: ['', [Validators.minLength(3), Validators.pattern(/^[a-zA-ZÀ-ÿ]+\s+[a-zA-ZÀ-ÿ]+.*$/)]],
      email: ['', [Validators.email]],
      password: ['', [Validators.minLength(5)]]
  }),
    this.categoryForm = this.fb.group({
      name: ['', []]
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

  onSubmitUserForm(){

    if(this.isFormValid(this.userEditForm)){

      this.userService.updateUserInfos(this.userEditForm.value, this.usuarioStorage?.idUser).subscribe({
        next: (resposta: any) => {
          alert('Seus dados foram atualizados com sucesso');

          this.router.navigate(['/login']);
        },
        error: (erro: any) => {
          alert('Ocorreu um erro ao atualizar seus dados');
        }
      }); 
    }
  }

  onSubmitCategoryForm(){

    console.log(this.categoryForm.value);

    if(this.categoryForm.valid){

      console.log('Passei aq');
      this.categoryService.criarCategoria(this.categoryForm.value, this.usuarioStorage?.idUser).subscribe({
        next: (resposta: any) => {
          alert('Categoria criada com sucesso!');

          this.fecharModal();
        },

        error: (erro: any) => {
          alert('Ocorreu um erro ao criar categoria');
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
