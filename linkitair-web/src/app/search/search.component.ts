import { Component, OnInit } from '@angular/core';
import {Flight} from "../model/flight";
import {AirportData} from "../model/airport-data";
import {Observable} from "rxjs";
import {FlightsService} from "../services/flights.service";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  fromAirport: AirportData;
  toAirport: AirportData;

  flights$: Observable<Flight[] | Observable<Flight[]>>;

  constructor(private flightsService: FlightsService) { }

  ngOnInit() {}

  setFrom(from: AirportData) {
    console.log("[SearchComponent]: Airport FROM selected: " + from + ", previous value: " + this.fromAirport);
    this.fromAirport = from;
  }

  setTo(to: AirportData) {
    console.log("[SearchComponent]: Airport TO selected: " + to);
    this.toAirport = to;
  }

  onSearch() {
    //console.log("Searching from [ " + this.fromAirport.code + " ] to [ " + this.toAirport.code + " ]");
    console.log("[SearchComponent]: onSearch [ " + (this.fromAirport? this.fromAirport.code : "") + " ] to [ " + (this.toAirport? this.toAirport.code : "") + " ]");
    if (this.fromAirport) {
      if (this.toAirport) {
        console.log("[SearchComponent]: - searching from [ " + this.fromAirport.code + " ] to [ " + this.toAirport.code + " ]");
        this.flights$ = this.flightsService.findFlights(this.fromAirport.code, this.toAirport.code);
      }
      else {
        console.log("[SearchComponent]: - searching from [ " + this.fromAirport.code + " ]");
        this.flights$ = this.flightsService.findFlights(this.fromAirport.code);
      }
    }
    //this.flights$ = this.flightsService.findFlights(this.fromAirport.code, this.toAirport.code);
  }
}
