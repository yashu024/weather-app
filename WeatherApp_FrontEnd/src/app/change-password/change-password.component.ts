import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { User } from '../user';
import { UserServiceService } from '../user-service.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  oldPassword:string = "";
  newPassword:string = "";
  confirmNewPassword:string = "";
  user:User = new User();

  constructor(private userService: UserServiceService, private snack: MatSnackBar) { }

  ngOnInit(): void {
    
    this.oldPassword = "";
    this.newPassword = "";
    this.confirmNewPassword = "";

    this.userService.getAccount().subscribe((data: User) => {
      this.user = data;
    },
     error => { console.log(error) }
    );
  }
  

  onSubmit() {

    if(this.oldPassword === "" || this.newPassword === "" || this.confirmNewPassword === "")
    {
      this.snack.open("Field cannot be left empty","OK",{duration:3000});
    }
    else if(this.oldPassword != this.user.password)
    {
      this.snack.open("Please enter correct old password","OK",{duration:3000});
    }
    else if(this.oldPassword === this.newPassword)
    {
      this.snack.open("Your new password cannot be same as your old password","OK",{duration:3000});
    }
    else if(this.newPassword != this.confirmNewPassword)
    {
      this.snack.open("Confirm password didn't match with new password","OK",{duration:3000});
    }
    else
    {
      this.user.password = this.newPassword;

      this.userService.updateUser(this.user).subscribe((data:User) =>
      {
          this.snack.open("Your password changed Successfully","OK",{duration:3000});
          this.ngOnInit();
      },
      
      error=>
      {
        console.log(error);
        this.snack.open(error.error,"CANCEL");
      })
    }
    
  
  }

}
