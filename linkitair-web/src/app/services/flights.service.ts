import { Injectable } from '@angular/core';
import { Observable,of } from "rxjs";
import {AirportData} from "../model/airport-data";
import {catchError} from "rxjs/operators";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Flight} from "../model/flight";

@Injectable({
  providedIn: 'root'
})
export class FlightsService {
  private reqOptionsArgs = { headers: new HttpHeaders().set( 'Content-Type', 'application/json' ) };

  constructor(private http: HttpClient) { }

  public findAirports(from: string, to: string = '*', min: number = 2) :Observable<AirportData[]> {
    let url;
    if (from && from.length >= min && to == '*') {
      url = '/flights/airports/from/' + from;
    }
    else if (from && from.length >= min && to.length >= min) {
      url = '/flights/airports/from/' + from +  '/to/' + to;
    }
    else {
      return of([]);
    }

    return this.http.get<AirportData[]>(url, this.reqOptionsArgs).pipe(
      catchError(error => {
        console.error( JSON.stringify(error) );
        return Observable.throw(error);
      })
    );
  }

  public findFlights(from: string, to: string = undefined) :Observable<Flight[]> {
    let url; // = '/api/flights/from/' + from +  '/to/' + to;
    if (from && !to) {
      url = '/flights/from/' + from;
    }
    else if (from && to) {
      url = '/flights/from/' + from +  '/to/' + to;
    }
    else {
      return of([]);
    }

    return this.http.get<Flight[]>(url, this.reqOptionsArgs).pipe(
      catchError(error => {
        console.error( JSON.stringify(error) );
        return Observable.throw(error);
      })
    );
  }
}
