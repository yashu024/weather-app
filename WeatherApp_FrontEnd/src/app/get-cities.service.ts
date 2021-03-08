import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
 
import { City } from './city';
import { ServicesService } from './services.service';

@Injectable({
  providedIn: 'root'
})
export class GetCitiesService {

  private baseUrl="/weatherApi";
  token = "";
  constructor(private httpClient: HttpClient, private services: ServicesService) { }
  getAll(): Observable<City> {
    let header = {
      headers: new HttpHeaders()
        .set('Authorization',""+this.services.getToken())
    }
    console.log(this.services.getToken());
    
    return this.httpClient.get<any>(`${this.baseUrl}`+"/dashboard", header);
    }

}
