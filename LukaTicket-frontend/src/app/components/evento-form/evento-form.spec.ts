// evento-form.component.spec.ts
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { EventoFormComponent } from './evento-form';
import { EventoService } from '../../services/evento';
import { of, throwError } from 'rxjs';

// Mock del servicio
class MockEventoService {
  getEvento = jasmine.createSpy('getEvento').and.returnValue(of({
    id: 1,
    nombre: 'Test Event',
    descripcion: 'Test Description',
    fechaEvento: '2026-01-01',
    horaEvento: '20:00:00',
    precioMin: 20,
    precioMax: 50,
    localidad: 'Madrid',
    genero: 'Rock',
    recinto: 'Test Venue'
  }));

  createEvento = jasmine.createSpy('createEvento').and.returnValue(of({
    id: 2,
    nombre: 'New Event',
    descripcion: 'New Description',
    fechaEvento: '2026-02-01',
    horaEvento: '21:00:00',
    precioMin: 30,
    precioMax: 60,
    localidad: 'Barcelona',
    genero: 'Pop',
    recinto: 'New Venue'
  }));

  updateEvento = jasmine.createSpy('updateEvento').and.returnValue(of({
    id: 1,
    nombre: 'Updated Event',
    descripcion: 'Updated Description',
    fechaEvento: '2026-01-01',
    horaEvento: '20:00:00',
    precioMin: 25,
    precioMax: 55,
    localidad: 'Madrid',
    genero: 'Rock',
    recinto: 'Updated Venue'
  }));
}

describe('EventoFormComponent', () => {
  let component: EventoFormComponent;
  let fixture: ComponentFixture<EventoFormComponent>;
  let eventoService: EventoService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        RouterTestingModule
      ],
      declarations: [EventoFormComponent],
      providers: [
        { provide: EventoService, useClass: MockEventoService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(EventoFormComponent);
    component = fixture.componentInstance;
    eventoService = TestBed.inject(EventoService);
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty values', () => {
    expect(component.eventoForm.get('nombre')?.value).toBe('');
    expect(component.eventoForm.get('descripcion')?.value).toBe('');
    expect(component.eventoForm.get('precioMin')?.value).toBe(0);
  });

  it('should have invalid form when empty', () => {
    expect(component.eventoForm.valid).toBeFalsy();
  });

  it('should have valid form with correct data', () => {
    // Arrange
    const formData = {
      nombre: 'Concierto de prueba',
      descripcion: 'Descripción de prueba para el concierto',
      fechaEvento: '2026-12-31',
      horaEvento: '20:00',
      precioMin: 20,
      precioMax: 50,
      localidad: 'Madrid',
      genero: 'Rock',
      recinto: 'Estadio de prueba'
    };

    // Act
    component.eventoForm.patchValue(formData);

    // Assert
    expect(component.eventoForm.valid).toBeTruthy();
  });

  it('should validate required fields', () => {
    // Arrange
    const nombreControl = component.eventoForm.get('nombre');
    const descripcionControl = component.eventoForm.get('descripcion');

    // Act
    nombreControl?.setValue('');
    descripcionControl?.setValue('');
    nombreControl?.markAsTouched();
    descripcionControl?.markAsTouched();

    // Assert
    expect(nombreControl?.hasError('required')).toBeTruthy();
    expect(descripcionControl?.hasError('required')).toBeTruthy();
  });

  it('should validate min length for nombre', () => {
    // Arrange
    const nombreControl = component.eventoForm.get('nombre');

    // Act
    nombreControl?.setValue('AB');
    nombreControl?.markAsTouched();

    // Assert
    expect(nombreControl?.hasError('minlength')).toBeTruthy();
  });

  it('should validate price ranges', () => {
    // Arrange
    const precioMinControl = component.eventoForm.get('precioMin');
    const precioMaxControl = component.eventoForm.get('precioMax');

    // Act - Test invalid (max < min)
    precioMinControl?.setValue(50);
    precioMaxControl?.setValue(20);

    // Assert
    expect(component.validarPrecios()).toBeFalsy();
  });

  it('should load evento in edit mode', () => {
    // Arrange
    component.isEditMode = true;
    component.eventoId = 1;

    // Act
    component.cargarEvento(1);

    // Assert
    expect(eventoService.getEvento).toHaveBeenCalledWith(1);
  });

  it('should submit form for new evento', () => {
    // Arrange
    const formData = {
      nombre: 'Nuevo Evento',
      descripcion: 'Descripción del nuevo evento',
      fechaEvento: '2026-12-31',
      horaEvento: '20:00',
      precioMin: 20,
      precioMax: 50,
      localidad: 'Madrid',
      genero: 'Rock',
      recinto: 'Estadio'
    };

    component.eventoForm.patchValue(formData);
    component.isEditMode = false;

    // Act
    component.onSubmit();

    // Assert
    expect(eventoService.createEvento).toHaveBeenCalled();
  });

  it('should submit form for edit evento', () => {
    // Arrange
    const formData = {
      nombre: 'Evento Editado',
      descripcion: 'Descripción editada',
      fechaEvento: '2026-12-31',
      horaEvento: '20:00',
      precioMin: 30,
      precioMax: 70,
      localidad: 'Barcelona',
      genero: 'Pop',
      recinto: 'Nuevo Estadio'
    };

    component.eventoForm.patchValue(formData);
    component.isEditMode = true;
    component.eventoId = 1;

    // Act
    component.onSubmit();

    // Assert
    expect(eventoService.updateEvento).toHaveBeenCalledWith(1, formData);
  });

  it('should mark all fields as touched when form is invalid', () => {
    // Arrange
    spyOn(component, 'marcarCamposComoTocados');

    // Act
    component.onSubmit();

    // Assert
    expect(component.marcarCamposComoTocados).toHaveBeenCalled();
  });
});
