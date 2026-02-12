import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})

export class ApiUserService {

  private apiUrl = "http://localhost:8081/auth";

  constructor(private http: HttpClient){ }

  fazerLogin(dadosLogin: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, dadosLogin, {responseType: 'text' as 'json'});
  }

  fazerCadastro(dadosCadastro: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/cadastro`, dadosCadastro, {responseType: 'text' as 'json'});
  }

  updateUserInfos(dadosUpdate: any, userID: any): Observable<any> {

    return this.http.put(`${this.apiUrl}/update/${userID}`, dadosUpdate, {responseType: 'text' as 'json'});
  }
  
}
