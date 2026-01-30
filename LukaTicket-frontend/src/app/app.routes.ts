// app.routes.ts - VERSIÓN CORREGIDA
import { Routes } from '@angular/router';

export const routes: Routes = [
  // RUTAS ESPECÍFICAS primero
  { path: 'eventos/nuevo', loadComponent: () => import('./components/evento-form/evento-form').then(m => m.EventoFormComponent) },
  { path: 'eventos/:id/editar', loadComponent: () => import('./components/evento-form/evento-form').then(m => m.EventoFormComponent) },

  // RUTAS DINÁMICAS después
  { path: 'eventos/:id', loadComponent: () => import('./components/evento-detail/evento-detail').then(m => m.EventoDetailComponent) },

  // RUTA GENERAL última
  { path: 'eventos', loadComponent: () => import('./components/evento-list/evento-list').then(m => m.EventoListComponent) },

  { path: 'comprar/:id', loadComponent: () => import('./components/comprar-entrada/comprar-entrada').then(m => m.ComprarEntradaComponent) },
  // Redirecciones
  { path: '', redirectTo: '/eventos', pathMatch: 'full' },
  { path: '**', redirectTo: '/eventos' },

];
