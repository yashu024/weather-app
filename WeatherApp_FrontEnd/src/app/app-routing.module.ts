import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from 'src/_service/auth-guard.service';
import { AppComponent } from './app.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { FavouritesComponent } from './favourites/favourites.component';
import { LandingpageComponent } from './landingpage/landingpage.component';
import { LoginComponent } from './login/login.component';
import { SearchComponent } from './search/search.component';
import { SignupComponent } from './signup/signup.component';
import { UpdateAccountComponent } from './update-account/update-account.component';

const routes: Routes = [
  { path: "", redirectTo: "landing_page", pathMatch: "full" },
  { path: "landing_page", component: LandingpageComponent },
  { path: "login", component: LoginComponent },
  { path: "signup", component: SignupComponent },
  { path: "dashboard", component: DashboardComponent, canActivate: [AuthGuardService]},
  { path: "favourites", component: FavouritesComponent, canActivate: [AuthGuardService]},
  { path: "dashboard", component: DashboardComponent, canActivate: [AuthGuardService]},
  { path: "search", component: SearchComponent, canActivate: [AuthGuardService]},
  { path: "updateProfile", component: UpdateAccountComponent, canActivate: [AuthGuardService]},
  { path: "changePassword", component: ChangePasswordComponent, canActivate: [AuthGuardService]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
