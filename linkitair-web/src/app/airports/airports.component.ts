import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl} from "@angular/forms";
import {Observable} from "rxjs";
import {debounceTime, distinctUntilChanged, switchMap, tap} from "rxjs/operators";
import {AirportData} from "../model/airport-data";
import {FlightsService} from "../services/flights.service";

@Component({
  selector: 'app-airports',
  templateUrl: './airports.component.html',
  styleUrls: ['./airports.component.css']
})
export class AirportsComponent implements OnInit {
  @Input() code;
  @Output() changed = new EventEmitter();
  airportDescription = new FormControl();
  airports: Observable<AirportData[] | Observable<AirportData[]>>;
  airportsLoading = false;

  constructor(private flightsService: FlightsService) { }

  ngOnInit() {
    this.airports = this.airportDescription.valueChanges.pipe(
      debounceTime( 400 ),
      distinctUntilChanged(),
      tap(() => this.airportsLoading = true ),
      switchMap( m => this.flightsService.findAirport( m ) ),
      tap(() => this.airportsLoading = false )
    );
  }

  onInputFocus() {
    // window.alert('Component gets the focus');
  }

  // (focusout)="onFocusOut()
  onInputBlur() {
    this.airportsLoading = false;
    this.changed.emit("LK0002");
  }

  //TODO: when an airport gets selected, notify the id
}
