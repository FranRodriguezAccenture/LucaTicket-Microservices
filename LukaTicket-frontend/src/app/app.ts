// app.ts - VERSIÓN CORREGIDA
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule], // ← SOLO RouterModule, NO EventoListComponent
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class App {
  // Puedes quitar la lógica de ruta si no la usas
  // constructor(private router: Router) {}

  // Método simplificado si necesitas mostrar algo solo en home
  isHomePage(): boolean {
    return window.location.pathname === '/' || window.location.pathname === '/eventos';
  }
}
