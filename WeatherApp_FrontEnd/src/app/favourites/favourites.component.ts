import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SearchServiceService } from 'src/_service/search-service.service';
import { FavCityService } from '../fav-city.service';

@Component({
  selector: 'app-favourites',
  templateUrl: './favourites.component.html',
  styleUrls: ['./favourites.component.css']
})
export class FavouritesComponent implements OnInit {

  cities: any = [];
  totalResults: any;
  favColor = "red"
  found = false;
  loading= true;
  flag = false;
  constructor(public favcity: FavCityService, public searchService: SearchServiceService, private snack: MatSnackBar) { }

  ngOnInit(): void 
  { 
    this.cities = []
    this.loading = true;
    this.found = false;
    this.flag = false;
    this.favcity.getAllFavCitiesOfUser().subscribe((resp:any) =>
      {
        console.log(resp);
        for(var key in resp)
        {
          console.log(typeof(resp[key]));
          var data  = resp[key];
          // get all fav cities
          for(var city of data)
          {
            console.log(city);
            // get each fav city weather
            this.searchService.getWeatherByCity(city).subscribe((cityWeather:any)=>
            {
              var time = this.convertTime(cityWeather.sys.sunrise);
              cityWeather.sys.sunrise = time;
              cityWeather.visibility = this.getVisibility(cityWeather.visibility);
              this.cities.push(cityWeather);

            }
          ,
          error =>
          {
            console.log(error.error);
          });
          
        }   
      } 

      
    },
      error =>
      {
        console.log(error);
      });

     
      setTimeout(() => 
      {
      this.loading = false;
      this.found = true;
      if(this.cities.length === 0)
      {
        this.flag = true;
      }
      },
      1000);
  
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


convertTime(t: any){
  var date = new Date(t * 1000);
  var hours = date.getHours();
  var minutes = "0" + date.getMinutes();
  var seconds = "0" + date.getSeconds();
  var formattedTime = hours + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);
  console.log(formattedTime);
  return formattedTime;
}

favToggle(cityWeather: any) {
  this.loading=true;
  
  this.favcity.favToggle(cityWeather.name).subscribe(resp => {
    console.log(resp);
    this.ngOnInit();
    
    this.cities.pop(cityWeather);
    this.snack.open("Removed from Favourites", "OK",{duration:2000,
      panelClass: ['mat-toolbar', 'mat-warn']});
    

  },
    error => {
       console.log(error.error)
       this.snack.open(error.error, "OK",{duration:3000,
        panelClass: ['mat-toolbar', 'mat-warn']})
    }
  );
}
}

