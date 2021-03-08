import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { City } from 'src/app/city';
import { ServicesService } from 'src/app/services.service';

@Injectable({
  providedIn: 'root'
})
export class SearchServiceService {

  private weatherApiUrl="/weatherApi";
  private favApiUrl="/favApi";
  token = "";
  constructor(private httpClient:HttpClient, private services:ServicesService) { }


  getWeatherByCity(cityName:string): Observable<City> {
    let header = {
      headers: new HttpHeaders()
        .set('Authorization',""+this.services.getToken())
    }
    console.log(this.services.getToken());
    
    return this.httpClient.get<any>(`${this.weatherApiUrl}`+"/city/"+cityName, header);
    }

    getAllFavCitiesOfUser() :Observable<any>
    {
      let header = {
        headers: new HttpHeaders()
          .set('Authorization',""+this.services.getToken())
      }
      
      return this.httpClient.get<any>(`${this.favApiUrl}`+"/user/all", header);
    }
}
