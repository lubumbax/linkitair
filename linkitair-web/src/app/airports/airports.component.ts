import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl} from "@angular/forms";
import {Observable, of} from "rxjs";
import {debounceTime, distinctUntilChanged, switchMap, tap} from "rxjs/operators";
import {AirportData} from "../model/airport-data";
import {FlightsService} from "../services/flights.service";

@Component({
  selector: 'app-airports',
  templateUrl: './airports.component.html',
  styleUrls: ['./airports.component.scss']
})
export class AirportsComponent implements OnInit {
  @Output() changed = new EventEmitter();

  airports$: Observable<AirportData[] | Observable<AirportData[]>>;
  inputControl = new FormControl();
  airportsLoading = false;

  constructor(private flightsService: FlightsService) { }

  searchEnabled: boolean = true;

  ngOnInit() {
    this.airports$ = this.inputControl.valueChanges.pipe(
      debounceTime(400),
      distinctUntilChanged(),
      tap(( ) => this.airportsLoading = true),
      switchMap( match => {
        if (this.searchEnabled) {
          return this.flightsService.findAirport(match)
        } else {
          return of([])
        }
      }),
      tap(() => this.airportsLoading = false)
    );
  }

  onInputFocus() {
    this.searchEnabled = true;
    //let val = this.inputControl.value;
    //this.inputControl.setValue('<mark>' + val + '</mark>');
  }

  // (focusout)="onFocusOut()
  onInputBlur() {
    this.airportsLoading = false;
  }

  onSelect(airport: AirportData) {
    this.searchEnabled = false;
    this.inputControl.setValue(airport.description);
    this.changed.emit(airport.code);
  }
}
