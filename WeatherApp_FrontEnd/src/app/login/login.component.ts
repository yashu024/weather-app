import { HttpResponse } from '@angular/common/http';
import { Component, getModuleFactory, OnInit } from '@angular/core';
import { Login } from '../login';
import { ServicesService } from '../services.service';
import { Router } from '@angular/router';
import { AppComponent } from '../app.component';
import { UserServiceService } from '../user-service.service';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {


  login: Login = new Login();

  constructor(private services: ServicesService, private router: Router, private appComp: AppComponent, private userService:UserServiceService) { }

  ngOnInit(): void {
  }

  onSubmit() {
    if (this.login.userName != '' && this.login.password != '') {

      //console.log("We have to submit the form to server");

      this.services.loginUser(this.login).subscribe(
        resp => {
          this.services.token = resp.headers.get('Authorization');
          this.services.saveToken(this.services.token);
          this.services.saveUserName(this.login.userName);
          this.appComp.loggedIn = this.services.isLoggedIn();

          this.userService.savePic();
          

          

          this.goToDashboard();

        }
      );
    }
    else {
      console.log("Please enter username/password");
    }
  }

  goToDashboard() {
    this.router.navigate(["dashboard"]);
  }
}
