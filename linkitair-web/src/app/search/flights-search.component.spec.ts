import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FlightsSearchComponent } from './flights-search.component';

describe('SearchComponent', () => {
  let component: FlightsSearchComponent;
  let fixture: ComponentFixture<FlightsSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FlightsSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FlightsSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
