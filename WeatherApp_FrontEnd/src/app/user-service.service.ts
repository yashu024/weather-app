import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { ServicesService } from './services.service';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {

  constructor( private httpClient: HttpClient, private services: ServicesService ) { }

  private baseUrl = "/user";

  public getLoggedInUserInfo = new Subject();
  
  getAccount(): Observable<User> {
    let header = {
      headers: new HttpHeaders()
        .set('Authorization',""+this.services.getToken())
    }
    return this.httpClient.get<User>(`${this.baseUrl}`+"/account", header);
  }

  updateUser(user: User): Observable<any> {
    const requestOptions: Object = {
      headers: new HttpHeaders().append('Authorization', ''+this.services.getToken()),
      responseType: 'text'
    }
    return this.httpClient.put(`${this.baseUrl}` + "/update", user, requestOptions);
  }

  public savePic() {
    this.getAccount().subscribe((data:User)=>
    {
      if(localStorage.getItem("pic"))
      {
/***** */console.log("profile picture exists: "+ localStorage.getItem("pic"));
      }
      else{
        localStorage.setItem("pic",data.profilePicture);
      console.log("saved profilePicture");

      this.getLoggedInUserInfo.next(data);

      }
      

    },
    error=>
    {
      console.log(error.error);
    });

  }

}
