import { Injectable } from '@angular/core';
import { UsuarioInterface } from '../interfaces/usuario';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private usuarioLogado: any = null;

  // Salva o usuário no Service e no Navegador
  setUsuario(usuario: any) {
    this.usuarioLogado = usuario;
    localStorage.setItem('usuarioInfo', JSON.stringify(usuario));
  }

  // Retornar um usuário válido para uso
  getUsuario() {

    const usuarioString = localStorage.getItem('usuarioInfo');

    if(!usuarioString){
      return null;
    }

    try{
      const primeiroParseUsuario = this.parseUsuario(usuarioString);

      this.usuarioLogado = this.parseUsuario(primeiroParseUsuario);

      return this.usuarioLogado;
    }
    catch(e){
      return usuarioString;
    
    }
  }

  parseUsuario(usuarioJSON: string){
    return JSON.parse(usuarioJSON);
  }

  logout() {
    this.usuarioLogado = null;
    localStorage.removeItem('usuarioInfo');
  }
}