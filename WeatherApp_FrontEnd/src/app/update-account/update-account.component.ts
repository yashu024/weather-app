import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { User } from '../user';
import { UserServiceService } from '../user-service.service';

@Component({
  selector: 'app-update-account',
  templateUrl: './update-account.component.html',
  styleUrls: ['./update-account.component.css']
})
export class UpdateAccountComponent implements OnInit {

  user: User= new User();
  url = "assets/images/def_avatar.png"
  removePic_flag = false;

  constructor(private userService: UserServiceService, private snack: MatSnackBar) { }

  ngOnInit(){

    this.removePic_flag = false;

    this.userService.getAccount().subscribe((data: User) => {
      this.user=data;
      this.url = "/assets/images/"+data.profilePicture;
    },
     error => { console.log(error) }
    );
  }

  onSubmit() {
    console.log(this.user);

    if(this.user.fullName === "" || this.user.email === "")
    {
      this.snack.open("Field cannot be left empty","OK",{duration:3000});
      return;
    }
    var userDetails = this.user;
    if(this.removePic_flag)
    {
      userDetails.profilePicture = "def_avatar.png";
      this.removePic_flag = false;
    }
    this.userService.updateUser(userDetails).subscribe((data:User) =>
    {
        console.log(data);
        this.snack.open("Account Updated Successfully","OK",{duration:3000});

        // update userInfo in local storage
        localStorage.removeItem("pic");
        this.userService.savePic();

        setTimeout(() => 
      {
       // console.log("change---pic---> "+localStorage.getItem("pic"));
        this.url = "/assets/images/"+localStorage.getItem("pic");
      },
      500);
        
    },
    
    error=>
    {
      console.log(error);
      this.snack.open(error.error,"CANCEL");
    })
  }



  onSelectFile(event:any) { // called each time file input changes
    if (event.target.files && event.target.files[0]) {
      var reader = new FileReader();

      reader.readAsDataURL(event.target.files[0]); // read file as data url
      this.user.profilePicture = event.target.files[0].name;
      console.log(event.target.files[0].name);
      reader.onload = (event:any) => { // called once readAsDataURL is completed
        this.url = event.target.result;
      }
    }
}

removePic():void{
  this.removePic_flag = true;
  this.url = "assets/images/def_avatar.png";
}

}
