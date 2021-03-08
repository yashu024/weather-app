import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FavCityService } from './fav-city.service';
import { ServicesService } from './services.service';
import { MatDialog } from '@angular/material/dialog';
import { DeleteUserDialogComponent } from './delete-user-dialog/delete-user-dialog.component';
import { UserServiceService } from './user-service.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'weathersite';
  user_name:any;
  picName:string="def_avatar.png"
  profileAvatar = "/assets/images/" + this.picName;

  constructor(private services: ServicesService, private favcity: FavCityService, private router: Router, private userService: UserServiceService, private dialog: MatDialog) { }

  @Input()
  public loggedIn = false;
  @Input()
  public userName: any;

  ngOnInit(): void {
    this.loggedIn = this.services.isLoggedIn();

    // extract all user info for updating header
   // console.log("inside app comp");
    this.userService.getLoggedInUserInfo.subscribe((resp:any) => {
      //console.log("inside app comp subscribe :" + resp);
      this.user_name = resp.userName;
      this.picName = resp.profilePicture;
      if(this.picName == null || this.picName == ""){
        this.picName = "def_avatar.png";
      }
      this.profileAvatar = "/assets/images/" + this.picName;
    
    });

    if(localStorage.getItem("pic"))
    {
      this.picName = ""+localStorage.getItem("pic");
      this.profileAvatar = "/assets/images/" + this.picName;
    }

    if(localStorage.getItem("userName"))
    {
      this.user_name = ""+localStorage.getItem("userName");
    }

    
  }

  logOut() {

    this.services.logout().subscribe();
    localStorage.removeItem("pic");

    this.loggedIn = this.services.isLoggedIn();
  }

  

  delete() {

    this.favcity.removeAllFavsByUser().subscribe();

    this.services.deleteUser().subscribe();

    localStorage.removeItem("token");
    localStorage.removeItem("pic");
    localStorage.removeItem("userName");
    this.loggedIn = this.services.isLoggedIn();
  }
  
  openDialog() {
    let dialogRef = this.dialog.open(DeleteUserDialogComponent, { data: { name: localStorage.getItem("userName") } });
    dialogRef.afterClosed().subscribe(result => {
      console.log("Dialog result" + result);
      if (result == "true") {
        this.delete();
        this.router.navigate(["landing_page"]);
      }
    })
  }

}

