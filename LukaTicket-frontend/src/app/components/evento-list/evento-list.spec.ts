import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventoListComponent } from './evento-list';

describe('EventoList', () => {
  let component: EventoListComponent;
  let fixture: ComponentFixture<EventoListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventoListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EventoListComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
