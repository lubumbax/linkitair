import {Component, EventEmitter, Input, OnInit, Output, SimpleChange, SimpleChanges} from '@angular/core';
import {FormControl} from "@angular/forms";
import {Observable, of} from "rxjs";
import {debounceTime, distinctUntilChanged, flatMap, switchMap, tap} from "rxjs/operators";
import {AirportData} from "../model/airport-data";
import {FlightsService} from "../services/flights.service";

@Component({template: ''})
export class AbstractAirportsComponent {
  @Output() changed = new EventEmitter();
  airports$: Observable<AirportData[] | Observable<AirportData[]>>;
  inputControl = new FormControl();
  airportsLoading = false;
  searchEnabled: boolean = true;
  airportSelected: boolean = false;

  constructor(protected flightsService: FlightsService) { }

  onInputFocus() {
    this.searchEnabled = true;
    //let val = this.inputControl.value;
    //this.inputControl.setValue('<mark>' + val + '</mark>');
  }

  onInputBlur() {
    this.airportsLoading = false;
    //console.log("onInputBlur(): airport is " + airport.code);
  }

  onSelect(airport: AirportData) {
    this.searchEnabled = false;
    this.inputControl.setValue(airport.description);
    this.airportSelected = true;
    this.changed.emit(airport);
  }
}

@Component({
  selector: 'from-airports',
  templateUrl: './airports.component.html',
  styleUrls: ['./airports.component.scss']
})
export class FromAirportsComponent extends AbstractAirportsComponent implements OnInit {
  ngOnInit() {
    this.airports$ = this.inputControl.valueChanges.pipe(
      tap(() => console.log("[FromAirportsComponent]: value CHANGED")),
      debounceTime(300),
      //distinctUntilChanged(),
      tap(() => this.airportsLoading = true),
      switchMap(match => {
        console.log("[FromAirportsComponent]: searchEnabled?: " + (this.searchEnabled? "true" : "false"))
        if (this.searchEnabled) {
          this.airportSelected = false;
          this.changed.emit(undefined);
          return this.flightsService.findAirports(match)
        } else {
          return of([])
        }
      }),
      tap(() => this.airportsLoading = false)
    );
  }
}

@Component({
  selector: 'to-airports',
  templateUrl: './airports.component.html',
  styleUrls: ['./airports.component.scss']
})
export class ToAirportsComponent extends AbstractAirportsComponent implements OnInit {
  @Input() filterFrom: AirportData;

  ngOnInit() {
    this.airports$ = this.inputControl.valueChanges.pipe(
      tap(() => console.log("[ToAirportsComponent]: value CHANGED")),
      debounceTime(300),
      //distinctUntilChanged(),
      tap(() => this.airportsLoading = true),
      switchMap(match => {
        console.log("[ToAirportsComponent]: searchEnabled: " + this.searchEnabled + ",  filterFrom: " + this.filterFrom);
        if (this.searchEnabled && this.filterFrom) {
          this.airportSelected = false;
          this.changed.emit(undefined);
          return this.flightsService.findAirports(this.filterFrom.code, match)
        } else {
          return of([])
        }
      }),
      tap(() => this.airportsLoading = false)
    );
  }

  ngOnChanges(changes: SimpleChanges) {
    const before: AirportData = changes.filterFrom.previousValue;
    const after: AirportData = changes.filterFrom.currentValue;
    if (before !== after) {
      console.log('[ToAirportsComponent]: filterFrom changed');
      this.airportSelected = false;
      this.inputControl.setValue('');
    }
  }
}
