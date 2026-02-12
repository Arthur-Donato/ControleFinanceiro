import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root',
})
export class ApiCategoryService {

  private apiCategoryUrl = "http://localhost:8081/category";

  constructor(private http: HttpClient){}

  criarCategoria(dadosCategoria: any, userID: any): Observable<any>{

    console.log('passei aqui com o id', userID);

    return this.http.post(`${this.apiCategoryUrl}/add/${userID}`, dadosCategoria, {responseType: 'text' as 'json'});
  }


}
