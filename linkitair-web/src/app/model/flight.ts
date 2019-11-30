import {AirportData} from "./airport-data";

export interface Flight {
  number: string,
  from: AirportData,
  to: AirportData,
  time: string,
  price: number
};
