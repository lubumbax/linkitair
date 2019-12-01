import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {FlightsSearchComponent} from "./search/flights-search.component";


const routes: Routes = [
  {path: '**', component: FlightsSearchComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
