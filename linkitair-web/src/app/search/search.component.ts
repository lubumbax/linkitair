import { Component, OnInit } from '@angular/core';
import {Flight} from "../model/flight";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  fromAirport: string;  // TODO: type AirportData
  toAirport: string;    // TODO: type AirportData

  flights: Flight[] = [];

  constructor() { }

  ngOnInit() {
    this.flights = this.flightResults;
  }

  onFrom(from: string) {
    console.log("Airport selected: " + from);
    this.fromAirport = from;
  }

  onTo(to: string) {
    console.log("Airport selected: " + to);
    this.toAirport = to;
  }

  onSearch() {
    console.log("Searching from [ " + this.fromAirport + " ] to [ " + this.toAirport + " ]");
  }

  flightResults: Flight[] = [
    {
      number: 'LK0001',
      from: {
        code: 'AMS',
        description: 'Amsterdam'
      },
      to: {
        code: 'LON',
        description: 'London'
      },
      time: "12:05",
      price: 123.5
    },

    {
      number: 'LK0002',
      from: {
        code: 'AMS',
        description: 'Amsterdam'
      },
      to: {
        code: 'FRA',
        description: 'Frankfurt'
      },
      time: "09:30",
      price: 99.0
    },
  ];
}
