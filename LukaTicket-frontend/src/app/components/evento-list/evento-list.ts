// evento-list.ts - Añadir botones de acciones
import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { EventoService } from '../../services/evento';
import { Router } from '@angular/router';

@Component({
  selector: 'app-evento-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './evento-list.html',
  styleUrls: ['./evento-list.scss']
})
export class EventoListComponent implements OnInit {
  eventos = signal<any[]>([]);
  isLoading = signal<boolean>(true);

  private eventoService = inject(EventoService);
  private router = inject(Router);

  ngOnInit(): void {
    this.cargarEventos();
  }

  cargarEventos(): void {
    this.isLoading.set(true);
    this.eventoService.getEventos().subscribe({
      next: (data) => {
        this.eventos.set(data);
        this.isLoading.set(false);
      },
      error: (error) => {
        console.error('Error:', error);
        this.eventos.set([]);
        this.isLoading.set(false);
      }
    });
  }

// En evento-list.ts - ACTUALIZAR método comprarEntrada
comprarEntrada(id: number): void {
  console.log('Función compra llamada para evento ID:', id);

  // TEMPORAL: Mostrar modal básico
  const evento = this.eventos().find(e => e.id === id);
  if (evento) {
    const confirmar = confirm(
      `¿Comprar entrada para: ${evento.nombre}?\n` +
      `Precio: ${evento.precioMin}€ - ${evento.precioMax}€\n\n` +
      `Esta función se implementará completamente en la FASE 6.`
    );

    if (confirmar) {
      // Aquí irá la lógica de compra en FASE 6
      alert('Función de compra en desarrollo. Se implementará en FASE 6.');
    }
  }
}

  // NUEVO: Eliminar evento
  eliminarEvento(id: number, nombre: string): void {
    if (confirm(`¿Estás seguro de eliminar el evento "${nombre}"?`)) {
      this.eventoService.deleteEvento(id).subscribe({
        next: () => {
          alert('Evento eliminado correctamente');
          this.cargarEventos(); // Recargar lista
        },
        error: (error) => {
          console.error('Error eliminando evento:', error);
          alert('Error al eliminar el evento');
        }
      });
    }
  }
}
