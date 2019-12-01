import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {AbstractAirportsComponent, FromAirportsComponent, ToAirportsComponent} from './airports/airports.component';
import { SearchComponent } from './search/search.component';
import {FlightsService} from "./services/flights.service";

@NgModule({
  declarations: [
    AppComponent,
    AbstractAirportsComponent,
    FromAirportsComponent,
    ToAirportsComponent,
    SearchComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
  ],
  providers: [
    FlightsService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
