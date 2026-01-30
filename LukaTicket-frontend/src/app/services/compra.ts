import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs'; // ← Añade 'of'
import { catchError } from 'rxjs/operators';

export interface Compra {
  id?: number;
  eventoId: number;
  tipoEntrada: string;
  cantidad: number;
  precioUnitario: number;
  total: number;
  datosPersonales: {
    nombre: string;
    apellidos: string;
    email: string;
    telefono: string;
    newsletter: boolean;
  };
  metodoPago: string;
  referencia: string;
  fechaCompra: string;
  estado: 'pendiente' | 'completada' | 'cancelada';
}

@Injectable({
  providedIn: 'root'
})
export class CompraService {
  private apiUrl = 'http://localhost:8081/compras';

  constructor(private http: HttpClient) { }

  crearCompra(compra: Omit<Compra, 'id' | 'estado'>): Observable<Compra> {
    const compraCompleta: Compra = {
      ...compra,
      estado: 'completada',
      fechaCompra: new Date().toISOString()
    };

    console.log('Creando compra:', compraCompleta);
    
    // Para desarrollo: simulación
    if (!this.apiUrl.includes('localhost:8081')) {
      return this.http.post<Compra>(this.apiUrl, compraCompleta).pipe(
        catchError(this.handleError<Compra>('crearCompra'))
      );
    }
    
    // Simulación para desarrollo
    return new Observable<Compra>(observer => {
      setTimeout(() => {
        observer.next({
          ...compraCompleta,
          id: Math.floor(Math.random() * 1000)
        });
        observer.complete();
      }, 1000);
    }).pipe(
      catchError(this.handleError<Compra>('crearCompra'))
    );
  }

  getCompras(): Observable<Compra[]> {
    return this.http.get<Compra[]>(this.apiUrl).pipe(
      catchError(this.handleError<Compra[]>('getCompras', []))
    );
  }

  getCompra(id: number): Observable<Compra> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<Compra>(url).pipe(
      catchError(this.handleError<Compra>(`getCompra id=${id}`))
    );
  }

  // Método auxiliar para manejar errores
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed:`, error);
      
      // Mensajes de error más específicos
      if (error.status === 404) {
        console.error(`${operation}: Recurso no encontrado`);
      } else if (error.status === 500) {
        console.error(`${operation}: Error del servidor`);
      } else if (error.status === 0) {
        console.error(`${operation}: No hay conexión con el servidor`);
      }
      
      // Dejar que la app continúe devolviendo un resultado vacío
      return of(result as T);
    };
  }

  // Simulación de pago con Stripe (o similar)
  procesarPagoTarjeta(datosTarjeta: any, total: number): Observable<{ success: boolean, referencia: string }> {
    console.log('Procesando pago con tarjeta:', datosTarjeta);
    
    // En producción, aquí integrarías con Stripe, PayPal, etc.
    return new Observable<{ success: boolean, referencia: string }>(observer => {
      setTimeout(() => {
        observer.next({
          success: true,
          referencia: 'PAY-' + Math.random().toString(36).substring(2, 15).toUpperCase()
        });
        observer.complete();
      }, 1500);
    }).pipe(
      catchError(this.handleError<{ success: boolean, referencia: string }>('procesarPagoTarjeta', {
        success: false,
        referencia: 'ERROR-' + Math.random().toString(36).substring(2, 8).toUpperCase()
      }))
    );
  }

  // Métodos adicionales útiles
  validarTarjeta(numero: string): boolean {
    // Algoritmo de Luhn para validar tarjetas
    let sum = 0;
    let shouldDouble = false;
    
    for (let i = numero.length - 1; i >= 0; i--) {
      let digit = parseInt(numero.charAt(i));
      
      if (shouldDouble) {
        if ((digit *= 2) > 9) digit -= 9;
      }
      
      sum += digit;
      shouldDouble = !shouldDouble;
    }
    
    return (sum % 10) === 0;
  }

  calcularTotal(precioUnitario: number, cantidad: number): number {
    const subtotal = precioUnitario * cantidad;
    const tarifaServicio = subtotal * 0.05; // 5% de tarifa
    return subtotal + tarifaServicio;
  }

  generarReferencia(): string {
    const timestamp = Date.now().toString(36);
    const random = Math.random().toString(36).substring(2, 8);
    return `REF-${timestamp}-${random}`.toUpperCase();
  }
}