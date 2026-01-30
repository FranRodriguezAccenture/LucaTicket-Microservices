// models/evento.ts
export interface Evento {
  id?: number;
  nombre: string;
  descripcion: string;
  fechaEvento: string;  // formato: YYYY-MM-DD
  horaEvento: string;   // formato: HH:mm:ss
  precioMin: number;
  precioMax: number;
  localidad: string;
  genero: string;
  recinto: string;
  createdAt?: string;
  updatedAt?: string;
}
