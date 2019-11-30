import { Injectable } from '@angular/core';
import { Observable,of } from "rxjs";
import {AirportData} from "../model/airport-data";
import {catchError} from "rxjs/operators";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class FlightsService {

  private reqOptionsArgs = { headers: new HttpHeaders().set( 'Content-Type', 'application/json' ) };

  constructor(private http: HttpClient) { }

  public findAirport(match: string) :Observable<AirportData[]> {
    if(!match) {
      return of([]);
    }
    return this.http.get<AirportData[]>(
        '/api/flights/airports?from=' + match,
        this.reqOptionsArgs
      )
      .pipe(catchError(error => {
        console.error( JSON.stringify( error ) );
        return Observable.throw( error );
      }));
  }
}
