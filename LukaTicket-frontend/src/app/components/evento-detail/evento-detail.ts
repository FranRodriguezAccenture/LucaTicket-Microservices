// evento-detail.ts - AÑADE ChangeDetectorRef
import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core'; // ← Añadir ChangeDetectorRef
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { EventoService } from '../../services/evento';
import { catchError, timeout } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-evento-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './evento-detail.html',
  styleUrls: ['./evento-detail.scss']
})
export class EventoDetailComponent implements OnInit {
  evento: any = null;
  isLoading: boolean = true;
  error: string = '';
  eventoId: number = 0;

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private eventoService = inject(EventoService);
  private cd = inject(ChangeDetectorRef); // ← INYECTAR ChangeDetectorRef

  ngOnInit(): void {
    console.log('🔍 EventoDetailComponent - ngOnInit');

    this.route.params.subscribe(params => {
      const idParam = params['id'];
      console.log('🔍 Params recibidos:', params);
      console.log('🔍 ID param:', idParam);

      const id = +idParam;
      console.log('🔍 ID convertido a número:', id);

      if (!id || isNaN(id) || id <= 0) {
        console.error('❌ ID inválido:', idParam);
        this.error = `ID de evento inválido: "${idParam}"`;
        this.isLoading = false;
        this.cd.detectChanges(); // ← Forzar detección
        return;
      }

      this.eventoId = id;
      this.cargarEvento(id);
    });
  }

  cargarEvento(id: number): void {
    console.log(`🔍 Cargando evento con ID: ${id}`);

    this.isLoading = true;
    this.error = '';
    this.cd.detectChanges(); // ← Forzar detección inicial

    this.eventoService.getEvento(id)
      .pipe(
        timeout(8000),
        catchError(error => {
          console.error('❌ Error en la petición:', error);

          if (error.name === 'TimeoutError') {
            this.error = 'El servidor no responde. Tiempo de espera agotado.';
          } else if (error.status === 404) {
            this.error = `Evento con ID ${id} no encontrado.`;
          } else if (error.status === 0) {
            this.error = 'No se puede conectar con el servidor.';
          } else {
            this.error = `Error al cargar el evento: ${error.message || 'Error desconocido'}`;
          }

          this.isLoading = false;
          this.cd.detectChanges(); // ← Forzar detección
          return of(null);
        })
      )
      .subscribe({
        next: (data) => {
          if (!data) {
            console.log('⚠️ No se recibieron datos');
            return;
          }

          console.log('✅ Evento cargado correctamente:', data);

          // ASIGNAR los datos
          this.evento = data;
          this.isLoading = false;

          // ✅ CRÍTICO: Forzar la detección de cambios
          this.cd.detectChanges();
          console.log('🔍 Change Detection forzado');

          // Verificar que los datos se asignaron
          console.log('🔍 Verificación:');
          console.log('- this.evento:', this.evento);
          console.log('- this.isLoading:', this.isLoading);
          console.log('- this.error:', this.error);
        },
        error: (error) => {
          console.error('❌ Error en subscribe:', error);
          this.isLoading = false;
          this.cd.detectChanges();
        },
        complete: () => {
          console.log('🔍 Observable completado');
        }
      });
  }
  formatearFecha(fecha: string): string {
    if (!fecha) {
      console.warn('⚠️ Fecha vacía o undefined');
      return 'Fecha no disponible';
    }

    console.log('🔍 Formateando fecha:', fecha);

    try {
      // Intentar varios formatos de fecha
      let date: Date;

      if (fecha.includes('T')) {
        // Formato ISO: "2026-01-30T00:00:00.000+00:00"
        date = new Date(fecha);
      } else if (fecha.includes('-')) {
        // Formato simple: "2026-01-30"
        date = new Date(fecha + 'T00:00:00');
      } else {
        // Otro formato
        date = new Date(fecha);
      }

      if (isNaN(date.getTime())) {
        console.warn('⚠️ Fecha inválida después de parsear:', fecha);
        return fecha; // Devuelve la fecha original
      }

      return date.toLocaleDateString('es-ES', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    } catch (error) {
      console.error('❌ Error formateando fecha:', error);
      return fecha;
    }
  }

  comprarEntrada(id: number): void {
  console.log('🎟️ Redirigiendo a compra para evento ID:', id);
  this.router.navigate(['/comprar', id]);
}

  eliminarEvento(): void {
    if (!this.evento) {
      alert('No hay evento para eliminar');
      return;
    }

    const confirmar = confirm(
      `¿ESTÁS SEGURO de eliminar el evento?\n\n` +
      `🎵 "${this.evento.nombre}"\n` +
      `📅 ${this.formatearFecha(this.evento.fechaEvento)}\n\n` +
      `⚠️ Esta acción NO se puede deshacer.`
    );

    if (confirmar) {
      console.log(`🗑️ Eliminando evento ID: ${this.evento.id} - "${this.evento.nombre}"`);

      this.eventoService.deleteEvento(this.evento.id).subscribe({
        next: () => {
          console.log('✅ Evento eliminado correctamente');
          alert(`✅ Evento "${this.evento.nombre}" eliminado correctamente`);
          this.router.navigate(['/eventos']);
        },
        error: (error) => {
          console.error('❌ Error eliminando evento:', error);
          alert(`❌ Error al eliminar el evento: ${error.message || 'Error desconocido'}`);
        }
      });
    }
  }
}
