import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
// Trabalhar com os imports de formularios

@Component({
  selector: 'app-home',
  imports: [RouterLink, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {

  exibirModal = false;
  tipoModal: 'usuario' | 'receita' | 'despesa' | 'categoria' = 'usuario';

  constructor(

    private service: ApiService,
    private router: Router
  ){}

  abrirModal(tipo: 'usuario' | 'receita' | 'despesa' | 'categoria'){
    this.tipoModal = tipo;
    this.exibirModal = true;
  }

  fecharModal(){
    this.exibirModal = false;
  }

  //Método para enviar formulário com as informações para edição de um usuário
}
