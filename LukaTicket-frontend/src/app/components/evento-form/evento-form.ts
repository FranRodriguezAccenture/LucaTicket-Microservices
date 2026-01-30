// evento-form.ts - VERSIÓN CON DEBUG COMPLETO
import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { EventoService } from '../../services/evento';
import { Evento } from '../../models/evento';
import { catchError, timeout } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-evento-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './evento-form.html',
  styleUrls: ['./evento-form.scss']
})
export class EventoFormComponent implements OnInit {
  eventoForm: FormGroup;
  isEditMode: boolean = false;
  eventoId: number | null = null;
  isLoading: boolean = false;
  isSubmitting: boolean = false;

  // Géneros musicales predefinidos
  generosMusicales: string[] = [
    'Rock', 'Pop', 'Jazz', 'Electrónica', 'Hip-Hop', 'Reggae',
    'Metal', 'Indie', 'Clásica', 'Folk', 'Salsa', 'Blues',
    'Country', 'R&B', 'Reggaeton', 'Techno', 'House', 'Disco'
  ];

  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private eventoService = inject(EventoService);
  private cd = inject(ChangeDetectorRef); // ← AÑADIR ChangeDetectorRef

  constructor() {
    console.log('🔍 EventoFormComponent - Constructor');

    this.eventoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      descripcion: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(500)]],
      fechaEvento: ['', [Validators.required]],
      horaEvento: ['', [Validators.required]],
      precioMin: [0, [Validators.required, Validators.min(0), Validators.max(9999)]],
      precioMax: [0, [Validators.required, Validators.min(0), Validators.max(9999)]],
      localidad: ['', [Validators.required, Validators.minLength(2)]],
      genero: ['', [Validators.required]],
      recinto: ['', [Validators.required, Validators.minLength(3)]]
    });

    console.log('✅ Formulario inicializado');
  }

  ngOnInit(): void {
    console.log('🔍 EventoFormComponent - ngOnInit');
    console.log('🔍 Ruta actual:', this.router.url);
    console.log('🔍 Params snapshot:', this.route.snapshot.params);

    // Primero verificar si estamos en modo edición por la ruta
    const url = this.router.url;
    this.isEditMode = url.includes('/editar');
    console.log('🔍 Modo edición detectado:', this.isEditMode);

    this.route.params.subscribe(params => {
      console.log('🔍 Params subscription:', params);

      if (params['id']) {
        this.isEditMode = true;
        this.eventoId = +params['id'];
        console.log('🔍 Modo EDITAR activado');
        console.log('🔍 ID del evento:', this.eventoId);
        console.log('🔍 URL completa para API:', `http://localhost:8081/eventos/${this.eventoId}`);

        this.cargarEvento(this.eventoId);
      } else {
        console.log('🔍 Modo CREAR (sin ID)');
        this.isLoading = false;
        this.cd.detectChanges();
      }
    });
  }

  cargarEvento(id: number): void {
    console.log(`🔍 Cargando evento para edición ID: ${id}`);
    console.log(`🔍 URL API: http://localhost:8081/eventos/${id}`);

    this.isLoading = true;
    this.cd.detectChanges(); // ← Forzar detección

    // Timeout de seguridad
    const timeoutMs = 10000;
    console.log(`⏰ Timeout configurado: ${timeoutMs}ms`);

    this.eventoService.getEvento(id)
      .pipe(
        timeout(timeoutMs),
        catchError(error => {
          console.error('❌ Error cargando evento para editar:', error);
          console.error('❌ Status:', error.status);
          console.error('❌ Message:', error.message);

          if (error.name === 'TimeoutError') {
            alert('⏰ El servidor no responde. Inténtalo de nuevo.');
          } else if (error.status === 404) {
            alert(`❌ Evento con ID ${id} no encontrado.`);
          } else if (error.status === 0) {
            alert('❌ No se puede conectar con el servidor. Verifica que el backend esté corriendo.');
          } else {
            alert(`❌ Error al cargar el evento: ${error.message || 'Error desconocido'}`);
          }

          this.isLoading = false;
          this.cd.detectChanges();
          this.router.navigate(['/eventos']);
          return of(null);
        })
      )
      .subscribe({
        next: (evento) => {
          if (!evento) {
            console.log('⚠️ No se recibieron datos del evento');
            return;
          }

          console.log('✅ Evento cargado para editar:', evento);
          console.log('📊 Datos recibidos:');
          console.log('- Nombre:', evento.nombre);
          console.log('- Fecha original:', evento.fechaEvento);
          console.log('- Tipo de fecha:', typeof evento.fechaEvento);

          // Ajustar formato de fecha (YYYY-MM-DD)
          let fechaEvento = evento.fechaEvento;
          if (fechaEvento.includes('T')) {
            fechaEvento = fechaEvento.split('T')[0];
            console.log('🔍 Fecha ajustada (quitando T):', fechaEvento);
          }

          console.log('🔍 Patching form values...');

          this.eventoForm.patchValue({
            ...evento,
            fechaEvento: fechaEvento
          });

          this.isLoading = false;
          this.cd.detectChanges(); // ← CRÍTICO: Forzar detección después de cargar

          console.log('✅ Formulario cargado con datos');
          console.log('🔍 Valores del formulario:', this.eventoForm.value);
          console.log('🔍 isLoading después de carga:', this.isLoading);
        },
        error: (error) => {
          console.error('❌ Error en subscribe (inesperado):', error);
          this.isLoading = false;
          this.cd.detectChanges();
        },
        complete: () => {
          console.log('🔍 Carga de evento completada');
        }
      });
  }

  onSubmit(): void {
    console.log('🔍 Submit del formulario');
    console.log('🔍 Form válido?:', this.eventoForm.valid);
    console.log('🔍 Valores del form:', this.eventoForm.value);

    if (this.eventoForm.invalid) {
      console.log('❌ Formulario inválido, marcando campos...');
      this.marcarCamposComoTocados();
      return;
    }

    this.isSubmitting = true;
    this.cd.detectChanges();

    const eventoData: Evento = this.eventoForm.value;
    console.log('🔍 Datos a enviar:', eventoData);

    if (this.isEditMode && this.eventoId) {
      console.log(`🔍 Actualizando evento ID: ${this.eventoId}`);

      this.eventoService.updateEvento(this.eventoId, eventoData).subscribe({
        next: (response) => {
          console.log('✅ Evento actualizado:', response);
          alert('✅ Evento actualizado correctamente');
          this.router.navigate(['/eventos', this.eventoId]);
        },
        error: (error) => {
          console.error('❌ Error actualizando evento:', error);
          alert(`❌ Error al actualizar el evento: ${error.message || 'Error desconocido'}`);
          this.isSubmitting = false;
          this.cd.detectChanges();
        }
      });
    } else {
      console.log('🔍 Creando nuevo evento');

      this.eventoService.createEvento(eventoData).subscribe({
        next: (eventoCreado) => {
          console.log('✅ Evento creado:', eventoCreado);
          alert('✅ Evento creado correctamente');
          this.router.navigate(['/eventos', eventoCreado.id]);
        },
        error: (error) => {
          console.error('❌ Error creando evento:', error);
          alert(`❌ Error al crear el evento: ${error.message || 'Error desconocido'}`);
          this.isSubmitting = false;
          this.cd.detectChanges();
        }
      });
    }
  }

  marcarCamposComoTocados(): void {
    console.log('🔍 Marcando todos los campos como touched');
    Object.keys(this.eventoForm.controls).forEach(key => {
      const control = this.eventoForm.get(key);
      control?.markAsTouched();
      console.log(`🔍 Campo ${key}: touched=${control?.touched}, valid=${control?.valid}`);
    });
    this.cd.detectChanges();
  }

  // Validación personalizada para precios
  validarPrecios(): boolean {
    const precioMin = this.eventoForm.get('precioMin')?.value;
    const precioMax = this.eventoForm.get('precioMax')?.value;
    const valido = precioMax >= precioMin;
    console.log(`🔍 Validación precios: ${precioMin} <= ${precioMax} = ${valido}`);
    return valido;
  }

  get tituloPagina(): string {
    return this.isEditMode ? 'Editar Evento' : 'Nuevo Evento';
  }

  get botonTexto(): string {
    return this.isSubmitting
      ? (this.isEditMode ? 'Actualizando...' : 'Creando...')
      : (this.isEditMode ? 'Actualizar Evento' : 'Crear Evento');
  }

  formatearHoraParaSpring(hora: string): string {
  console.log('🔍 === FORMATEADOR HORA SPRING ===');
  console.log('🔍 Input hora:', hora);
  console.log('🔍 Input tipo:', typeof hora);

  if (!hora || hora.trim() === '') {
    console.warn('⚠️ Hora vacía, usando 18:00:00');
    return '18:00:00';
  }

  // Caso 1: Ya está en formato HH:mm:ss exacto
  if (/^\d{2}:\d{2}:\d{2}$/.test(hora)) {
    console.log('✅ Hora ya en formato HH:mm:ss perfecto');
    return hora;
  }

  // Caso 2: Viene de input type="time" - normalmente "HH:mm"
  if (/^\d{2}:\d{2}$/.test(hora)) {
    const horaConSegundos = hora + ':00';
    console.log(`✅ Convertido HH:mm → HH:mm:ss: ${hora} → ${horaConSegundos}`);
    return horaConSegundos;
  }

  // Caso 3: Viene con milisegundos "HH:mm:ss.SSS"
  if (/^\d{2}:\d{2}:\d{2}\.\d+$/.test(hora)) {
    const horaSinMillis = hora.split('.')[0];
    console.log(`✅ Quitados milisegundos: ${hora} → ${horaSinMillis}`);
    return horaSinMillis;
  }

  // Caso 4: Formato irregular, intentar parsear
  console.log('⚠️ Formato irregular, intentando parsear...');

  // Extraer números de la hora
  const numeros = hora.match(/\d+/g);
  if (numeros && numeros.length >= 2) {
    const horas = numeros[0].padStart(2, '0');
    const minutos = numeros[1].padStart(2, '0');
    const segundos = (numeros[2] || '00').padStart(2, '0');
    const horaFormateada = `${horas}:${minutos}:${segundos}`;

    console.log(`✅ Parseado: ${hora} → ${horaFormateada}`);
    return horaFormateada;
  }

  // Caso 5: Fallback a hora por defecto
  console.warn('⚠️ No se pudo parsear la hora, usando 18:00:00');
  return '18:00:00';
}
}
