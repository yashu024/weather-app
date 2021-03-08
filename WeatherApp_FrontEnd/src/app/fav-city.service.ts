import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ServicesService } from './services.service';

@Injectable({
  providedIn: 'root'
})
export class FavCityService {

  constructor(private httpClient: HttpClient, private services: ServicesService) { }
  private baseUrl = "/favApi";

  favToggle(cityName: string): Observable<any> {

    const requestOptions: Object = {
      headers: new HttpHeaders().append('Authorization', ''+this.services.getToken()),
      responseType: 'text'
    }
    
    
    return this.httpClient.put(`${this.baseUrl}`+"/toggle/"+cityName, null, requestOptions); 
  }

  getAllFavCitiesOfUser():Observable<any>
  {
    let header = {
      headers: new HttpHeaders()
        .set('Authorization', "" + this.services.getToken())
    }
    return this.httpClient.get<any>(`${this.baseUrl}` + "/user/all", header);

  }

  removeAllFavsByUser(): Observable<any> {

    let header = {
      headers: new HttpHeaders()
        .set('Authorization', "" + this.services.getToken())
    }
    return this.httpClient.delete<any>(`${this.baseUrl}` + "/delete/user", header);
  }

}
