import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { Login } from './login';
import { User } from './user';
import { UserServiceService } from './user-service.service';

@Injectable({
  providedIn: 'root'
})
export class ServicesService {

  private baseUrl = "/user";
  token = "";
  httpHeader: HttpHeaders = new HttpHeaders();

  constructor(private httpClient: HttpClient) { }

  //For logging in the app
  loginUser(login: Login): Observable<any> {
    return this.httpClient.post(`${this.baseUrl}` + "/login", login, { observe: 'response', responseType: 'text' });
  }

  //For saving the token after login
  saveToken(token: any) {
    localStorage.setItem("token", token);
    return true;
  }

  //For saving the username in localstorage after login
  saveUserName(userName: string) {
    localStorage.setItem("userName", userName);

    return true;
  }

  //For signing up a new user
  signUpUser(user: User): Observable<any> {
    return this.httpClient.post(`${this.baseUrl}` + "/signup", user, { observe: 'response', responseType: 'text' });
  }

  //To check if the user is logged in or not
  isLoggedIn() {
    let token = localStorage.getItem("token");
    if (token == undefined || token == '' || token == null) {
      return false;
    }
    else {
      return true;
    }
  }

  //Logout the user
  logout(): Observable<any> {

    const requestOptions: Object = {
      headers: new HttpHeaders().append('Authorization', ''+this.getToken()),
      responseType: 'text'
    }

    localStorage.removeItem("token");
    localStorage.removeItem("userName");
    localStorage.removeItem("pic");
    return this.httpClient.get(`${this.baseUrl}` + "/logout", requestOptions);
  }

  //getting the token
  public getToken() {
    return localStorage.getItem("token");
  }

  //update user
 

  //delete user
  public deleteUser() {
    let header = {
      headers: new HttpHeaders()
        .set('Authorization', "" + this.getToken())
    }
    return this.httpClient.delete<any>(`${this.baseUrl}` + "/delete", header);

  }
  //getting the username
  public getUserName() {
    return localStorage.getItem("userName");
  }



}
