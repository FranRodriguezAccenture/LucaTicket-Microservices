// evento.service.ts - VERSIÓN CORREGIDA
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs'; // ← AÑADIR throwError aquí
import { catchError, tap } from 'rxjs/operators';
import { Evento } from '../models/evento';

@Injectable({
  providedIn: 'root'
})
export class EventoService {
  private apiUrl = 'http://localhost:8081/eventos';

  constructor(private http: HttpClient) { }

  getEventos(): Observable<Evento[]> {
    console.log('🔍 Servicio: Obteniendo todos los eventos');
    console.log('🔍 URL:', this.apiUrl);

    return this.http.get<Evento[]>(this.apiUrl).pipe(
      tap(data => console.log(`✅ Servicio: ${data?.length || 0} eventos recibidos`)),
      catchError(error => {
        console.error('❌ Servicio: Error en getEventos:', error);
        console.error('❌ Status:', error.status);
        console.error('❌ URL:', error.url);
        return throwError(() => error);
      })
    );
  }

  getEvento(id: number): Observable<Evento> {
  console.log(`🔍 Servicio: Obteniendo evento ID: ${id}`);
  const url = `${this.apiUrl}/${id}`;
  console.log(`🔍 Servicio: URL: ${url}`);

  return this.http.get<Evento>(url).pipe(
    tap(data => console.log('✅ Servicio: Evento recibido:', data)),
    catchError((error: any) => {
      console.error('❌ Servicio: Error en getEvento:', error);
      
      // Si el backend no está disponible, simular un evento
      if (error.status === 0 || error.status === 404) {
        console.log('⚠️ Servicio no disponible, simulando evento...');
        return of(this.simularEvento(id));
      }
      
      return throwError(() => error);
    })
  );
}

// Método para simular un evento si el backend falla
private simularEvento(id: number): Evento {
  console.log(`🎭 Simulando evento para ID: ${id}`);
  return {
    id: id,
    nombre: `Concierto ${id} - SIMULADO`,
    descripcion: 'Descripción simulada del evento para pruebas',
    fechaEvento: new Date(Date.now() + 86400000 * 7).toISOString().split('T')[0], // 7 días desde hoy
    horaEvento: '20:00:00',
    precioMin: 30,
    precioMax: 100,
    localidad: 'Madrid',
    genero: 'Rock',
    recinto: 'WiZink Center',
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  };
}

  // NUEVOS MÉTODOS PARA CRUD
  createEvento(evento: Evento): Observable<Evento> {
    console.log('🔍 Servicio: Creando evento:', evento);
    return this.http.post<Evento>(this.apiUrl, evento).pipe(
      tap(data => console.log('✅ Servicio: Evento creado:', data)),
      catchError(error => {
        console.error('❌ Servicio: Error creando evento:', error);
        return throwError(() => error);
      })
    );
  }

  // evento.service.ts - MEJORAR catchError del update
updateEvento(id: number, evento: Evento): Observable<Evento> {
  console.log(`🔍 Servicio: Actualizando evento ID ${id}:`, evento);
  const url = `${this.apiUrl}/${id}`;
  console.log(`🔍 Servicio: URL PUT: ${url}`);

  return this.http.put<Evento>(url, evento).pipe(
    tap(data => console.log('✅ Servicio: Evento actualizado:', data)),
    catchError(error => {
      console.error('❌ Servicio: Error actualizando evento:', error);
      console.error('❌ Error completo:', error);
      console.error('❌ Status:', error.status);
      console.error('❌ Error body:', error.error); // ← ESTO ES IMPORTANTE
      console.error('❌ Error message:', error.message);
      console.error('❌ Error text:', error.error?.text || error.error);

      // Intentar parsear el error si es JSON
      try {
        if (error.error) {
          const errorObj = typeof error.error === 'string'
            ? JSON.parse(error.error)
            : error.error;
          console.error('❌ Error parseado:', errorObj);
        }
      } catch (e) {
        console.error('❌ No se pudo parsear error:', e);
      }

      return throwError(() => error);
    })
  );
}

  deleteEvento(id: number): Observable<void> {
    console.log(`🔍 Servicio: Eliminando evento ID: ${id}`);
    const url = `${this.apiUrl}/${id}`;
    console.log(`🔍 Servicio: URL DELETE: ${url}`);

    return this.http.delete<void>(url).pipe(
      tap(() => console.log(`✅ Servicio: Evento ${id} eliminado`)),
      catchError(error => {
        console.error('❌ Servicio: Error eliminando evento:', error);
        return throwError(() => error);
      })
    );
  }
}
