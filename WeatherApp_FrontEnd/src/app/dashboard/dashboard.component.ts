import { Component, OnInit, ViewChild } from '@angular/core';
import { FavCityService } from '../fav-city.service';
import { GetCitiesService } from '../get-cities.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SearchServiceService } from 'src/_service/search-service.service';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  userCities: any = [];
  cities: any = [];
  city: any = [];
  totalResults: any;
  loading= true;

  constructor(public cityService: GetCitiesService, public favcity: FavCityService, public searchService: SearchServiceService, 
              private snack: MatSnackBar) {
               }

  ngOnInit(): void {

    this.updateFav();

    this.cityService.getAll().subscribe((data: any) => {

      var time ;
      for (var key in data) {
        if (data.hasOwnProperty(key)) {
          var val = data[key];

          let isFound:boolean = false;
          
          if(this.userCities.includes(val.name)){
            val.toggleString = 'Unfavourite';
            val.toggleColor = "red";
          }
          else{
            val.toggleString = 'Favourite';
            val.toggleColor = "green";
          }

          val.visibility = this.getVisibility(val.visibility);

          time = this.convertTime(val.sys.sunrise);
          val.sys.sunrise = time;
          this.city.push(val);
      
        }
      }
      this.totalResults=this.city.length;
      this.loading=false;

    },
    error=>
    {
      this.loading=false;
      console.log(error.error);
    })
    this.cities = this.city;

   
  }

  convertTime(t: any){
    var date = new Date(t * 1000);
    var hours = date.getHours();
    var minutes = "0" + date.getMinutes();
    var seconds = "0" + date.getSeconds();
    var formattedTime = hours + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);
    //console.log(formattedTime);
    return formattedTime;
  }

  favToggle(city: any = []) {

    this.favcity.favToggle(city.name).subscribe(resp => {

      if(city.toggleString === 'Favourite')
      {
        city.toggleString = 'Unfavourite'
        city.toggleColor = "red"
        this.snack.open("Added to favourites","OK",{duration:2000,
          panelClass: ['mat-toolbar', 'mat-primary']});
      }
      else
      {
        city.toggleString = 'Favourite';
        city.toggleColor = "green"
        this.snack.open("Removed from favourites","OK",{duration:2000,
          panelClass: ['mat-toolbar', 'mat-warn']});
      }

      console.log(resp);
    },
      error => {// console.log(error)
      }
    );
  }

  getVisibility(metre:any)
  {
    var miles = metre * 0.00062131;
    var visibility:string;
    if(miles < 1)
      visibility = "Dense fog";
    else if(miles < 2)
      visibility = "Thick Fog";
    else if(miles < 3)
      visibility = "Fog";
    else if(miles < 4)
      visibility = "Moderate Fog";
    else if(miles < 5)
      visibility = "Thin Fog";
    else if(miles < 6)
      visibility = "Visibility poor";
    else if(miles < 7)
      visibility = "Visibility moderate";
    else if(miles < 8)
      visibility = "Visibility good";
    else if(miles < 9)
      visibility = "Visibility very good";
    else
      visibility = "Visibility Excellent";
  
      return visibility;
    
  }

  updateFav():void {
    this.searchService.getAllFavCitiesOfUser().subscribe((data:any)=>
    {
      for(var user in data)
      {
        this.userCities = data[user];
      }
      
    },
    error=>
    {
      console.log(error.error);
    });

  }

  public showRandomly(bias:number) {
    return Math.random() < bias;
}
}
