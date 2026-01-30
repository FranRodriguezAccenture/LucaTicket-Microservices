import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { EventoService } from '../../services/evento';
import { CompraService } from '../../services/compra';

@Component({
  selector: 'app-comprar-entrada',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './comprar-entrada.html',
  styleUrls: ['./comprar-entrada.scss']
})
export class ComprarEntradaComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private eventoService = inject(EventoService);
  private compraService = inject(CompraService);

  // --- ESTADO ---
  evento: any = null;
  isLoading: boolean = true;
  error: string = '';
  eventoId: number = 0;
  usandoDatosSimulados: boolean = false;
  showDebug: boolean = false;

  // --- PROCESO DE COMPRA ---
  pasoActual: number = 1;
  tipoSeleccionado: string = 'general';
  cantidad: number = 1;
  metodoPagoSeleccionado: string = 'tarjeta';
  referencia: string = '';
  hoy: Date = new Date();
  procesandoPago: boolean = false;

  // --- FORMULARIOS ---
  formDatosPersonales!: FormGroup;
  formTarjeta!: FormGroup;

  // --- DATOS ESTÁTICOS ---
  tiposEntrada = [
    { id: 'general', nombre: 'Entrada General', precio: 45 },
    { id: 'vip', nombre: 'Entrada VIP', precio: 90 },
    { id: 'platea', nombre: 'Platea', precio: 65 },
    { id: 'palco', nombre: 'Palco', precio: 120 }
  ];

  metodosPago = [
    { id: 'tarjeta', nombre: 'Tarjeta de crédito/débito' },
    { id: 'paypal', nombre: 'PayPal' },
    { id: 'transferencia', nombre: 'Transferencia bancaria' }
  ];

  ngOnInit(): void {
  console.log('🎫 ComprarEntradaComponent - INICIANDO');
  
  const idParam = this.route.snapshot.params['id'];
  console.log('📌 ID de la URL:', idParam);
  
  // Inicializar formularios y referencia primero
  this.inicializarFormularios();
  this.generarReferencia();
  
  if (idParam) {
    this.eventoId = Number(idParam);
    console.log('🎯 Evento ID:', this.eventoId);
    this.cargarEvento();
  } else {
    this.eventoId = 1;
    console.warn('⚠️ No hay ID, usando valor por defecto');
    this.crearEventoSimulado();
  }
  
  // Reducir timeout a 5 segundos
  setTimeout(() => {
    if (this.isLoading) {
      console.warn('⚠️ Timeout de carga (5s), forzando datos simulados');
      this.crearEventoSimulado();
    }
  }, 5000); // Cambiado de 8000 a 5000
}

 cargarEvento(): void {
  console.log(`🔄 Intentando cargar evento ${this.eventoId} del backend...`);
  this.isLoading = true;
  this.usandoDatosSimulados = false;
  this.error = '';
  
  // Forzar un timeout más corto para simulación si la API es lenta
  const timeoutPromise = new Promise((_, reject) => {
    setTimeout(() => reject(new Error('Timeout al cargar evento')), 3000);
  });

  // Intentar cargar del backend con timeout
  Promise.race([
    new Promise((resolve, reject) => {
      this.eventoService.getEvento(this.eventoId).subscribe({
        next: (eventoReal) => resolve(eventoReal),
        error: (error) => reject(error)
      });
    }),
    timeoutPromise
  ]).then((eventoReal: any) => {
    console.log('✅ Evento cargado del backend:', eventoReal);
    this.evento = eventoReal;
    this.isLoading = false;
    this.ajustarPreciosSiEsNecesario(eventoReal);
  }).catch((error) => {
    console.error('❌ Error/timeout al cargar:', error);
    this.error = `No se pudo conectar al servidor. Usando modo demostración.`;
    console.log('🔄 Usando datos simulados...');
    this.crearEventoSimulado();
  });
}

  private ajustarPreciosSiEsNecesario(eventoReal: any): void {
    if (eventoReal.precioMin && eventoReal.precioMax) {
      this.tiposEntrada = [
        { id: 'general', nombre: 'Entrada General', precio: eventoReal.precioMin },
        { id: 'vip', nombre: 'Entrada VIP', precio: Math.round((eventoReal.precioMin + eventoReal.precioMax) / 2) },
        { id: 'platea', nombre: 'Platea', precio: Math.round(eventoReal.precioMin * 1.5) },
        { id: 'palco', nombre: 'Palco', precio: eventoReal.precioMax }
      ];
      console.log('✅ Precios ajustados:', this.tiposEntrada);
    }
  }

  crearEventoSimulado(): void {
    console.log('🎭 Creando evento simulado...');
    this.usandoDatosSimulados = true;
    
    setTimeout(() => {
      this.evento = {
        id: this.eventoId,
        nombre: `🎵 Concierto Especial ${this.eventoId}`,
        descripcion: 'Un evento increíble con artistas de primer nivel. ¡No te lo pierdas!',
        fechaEvento: this.getFechaFutura(30),
        horaEvento: '20:30:00',
        precioMin: 40,
        precioMax: 120,
        localidad: this.getLocalidad(),
        genero: this.getGenero(),
        recinto: this.getRecinto()
      };
      
      this.isLoading = false;
      console.log('✅ Evento simulado creado');
    }, 1000);
  }

  private getFechaFutura(dias: number): string {
    const fecha = new Date();
    fecha.setDate(fecha.getDate() + dias);
    return fecha.toISOString().split('T')[0];
  }

  private getLocalidad(): string {
    const localidades = ['Madrid', 'Barcelona', 'Valencia', 'Sevilla', 'Bilbao'];
    return localidades[this.eventoId % localidades.length] || 'Madrid';
  }

  private getGenero(): string {
    const generos = ['Rock', 'Pop', 'Electrónica', 'Jazz', 'Indie'];
    return generos[this.eventoId % generos.length] || 'Música';
  }

  private getRecinto(): string {
    const recintos = ['WiZink Center', 'Palau Sant Jordi', 'Auditorio', 'Sala Razzmatazz', 'Teatro Principal'];
    return recintos[this.eventoId % recintos.length] || 'Auditorio Municipal';
  }

  inicializarFormularios(): void {
    console.log('📝 Inicializando formularios...');
    
    this.formDatosPersonales = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      apellidos: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.required, Validators.pattern(/^[0-9]{9}$/)]],
      newsletter: [true]
    });

    this.formTarjeta = this.fb.group({
      numero: ['4242424242424242', [Validators.required, Validators.pattern(/^[0-9]{16}$/)]],
      expiracion: ['12/25', [Validators.required, Validators.pattern(/^(0[1-9]|1[0-2])\/([0-9]{2})$/)]],
      cvv: ['123', [Validators.required, Validators.pattern(/^[0-9]{3,4}$/)]],
      nombreTarjeta: ['', [Validators.required]]
    });
  }

  generarReferencia(): void {
    const random = Math.random().toString(36).substring(2, 8).toUpperCase();
    this.referencia = `TKT-${this.eventoId}-${random}`;
    console.log('🎫 Referencia generada:', this.referencia);
  }

  validarCantidad(): void {
    if (this.cantidad < 1) this.cantidad = 1;
    if (this.cantidad > 10) this.cantidad = 10;
  }

  recargar(): void {
    console.log('🔄 Recargando...');
    this.isLoading = true;
    this.error = '';
    this.evento = null;
    this.cargarEvento();
  }

  toggleDebug(): void {
    this.showDebug = !this.showDebug;
  }

  siguientePaso(): void {
    if (this.pasoActual < 4) {
      if (this.pasoActual === 2 && !this.formDatosPersonales.valid) {
        alert('Por favor, completa todos los campos requeridos');
        return;
      }
      this.pasoActual++;
    }
  }

  anteriorPaso(): void {
    if (this.pasoActual > 1) this.pasoActual--;
  }

  getInstruccionesPago(): string {
    switch(this.metodoPagoSeleccionado) {
      case 'tarjeta': return 'Completa los datos de tu tarjeta y haz clic en "Confirmar y pagar"';
      case 'paypal': return 'Serás redirigido a PayPal para completar el pago de forma segura';
      case 'transferencia': return 'Recibirás los datos bancarios para realizar la transferencia en 24 horas';
      default: return 'Selecciona un método de pago';
    }
  }

  iniciarProcesoPago(): void {
  console.log('💰 Procesando pago inmediatamente...');
  
  // Ir DIRECTAMENTE al paso 4 (confirmación)
  this.pasoActual = 4;
  console.log('✅ Compra completada inmediatamente');
}

  // Elimina o comenta estos métodos:
simularPagoPayPal(): void {
  // Ya no necesitas simular, ir directo a confirmación
  this.pasoActual = 4;
}

mostrarDatosTransferencia(): void {
  // Ya no necesitas mostrar datos, ir directo a confirmación
  this.pasoActual = 4;
}

  procesarCompra(): void {
  console.log('💰 Procesando compra...');
  
  // Ir DIRECTAMENTE al paso 4
  this.pasoActual = 4;
  console.log('✅ Compra completada');
}

  descargarEntrada(): void {
    console.log('📥 Descargando entrada HTML...');
    
    const htmlContent = `
      <!DOCTYPE html>
      <html lang="es">
      <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Entrada - ${this.referencia}</title>
        <style>
          body { font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto; padding: 20px; }
          .header { text-align: center; border-bottom: 3px solid #0d6efd; padding-bottom: 20px; margin-bottom: 30px; }
          .ticket-info { background: #f8f9fa; padding: 20px; border-radius: 10px; margin-bottom: 20px; }
          .section { margin-bottom: 20px; padding: 15px; border-left: 4px solid #0d6efd; background: white; }
          .qr-code { text-align: center; margin: 30px 0; }
          .footer { text-align: center; margin-top: 30px; color: #6c757d; font-size: 0.9em; }
          .warning { background: #fff3cd; border: 1px solid #ffc107; padding: 10px; border-radius: 5px; margin: 20px 0; }
        </style>
      </head>
      <body>
        <div class="header">
          <h1 style="color: #0d6efd;">🎟️ ENTRADA VIRTUAL</h1>
          <h2>${this.evento.nombre}</h2>
          <p style="font-size: 1.2em; color: #198754;">Referencia: <strong>${this.referencia}</strong></p>
        </div>
        
        <div class="ticket-info">
          <h3>📋 Información del Evento</h3>
          <div class="section">
            <p><strong>📍 Lugar:</strong> ${this.evento.recinto}, ${this.evento.localidad}</p>
            <p><strong>📅 Fecha:</strong> ${this.formatearFecha(this.evento.fechaEvento)}</p>
            <p><strong>⏰ Hora:</strong> ${this.evento.horaEvento}</p>
            <p><strong>🎵 Género:</strong> ${this.evento.genero}</p>
          </div>
          
          <h3>🎫 Detalles de la Entrada</h3>
          <div class="section">
            <p><strong>Tipo:</strong> ${this.nombreTipoEntrada}</p>
            <p><strong>Cantidad:</strong> ${this.cantidad} entrada(s)</p>
            <p><strong>Precio unitario:</strong> ${this.precioUnitario}€</p>
            <p><strong>Total pagado:</strong> <span style="font-size: 1.3em; color: #198754;">${this.total}€</span></p>
          </div>
          
          <h3>👤 Datos del Comprador</h3>
          <div class="section">
            <p><strong>Nombre:</strong> ${this.formDatosPersonales.value.nombre} ${this.formDatosPersonales.value.apellidos}</p>
            <p><strong>Email:</strong> ${this.formDatosPersonales.value.email}</p>
            <p><strong>Teléfono:</strong> ${this.formDatosPersonales.value.telefono}</p>
          </div>
        </div>
        
        <div class="qr-code">
          <div style="border: 2px dashed #ccc; padding: 20px; display: inline-block;">
            <p>🔳 Código QR para validación</p>
            <p style="font-family: monospace; font-size: 1.2em;">${this.referencia}</p>
          </div>
        </div>
        
        <div class="warning">
          <h4>⚠️ Instrucciones importantes:</h4>
          <ul>
            <li>Presenta esta entrada en la entrada del evento</li>
            <li>Llega 30 minutos antes del inicio</li>
            <li>Trae tu documento de identificación</li>
            <li>Conserva esta entrada hasta el final del evento</li>
          </ul>
        </div>
        
        <div class="footer">
          <p>Fecha de emisión: ${new Date().toLocaleDateString('es-ES', { 
            year: 'numeric', 
            month: 'long', 
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
          })}</p>
          ${this.usandoDatosSimulados ? '<p style="color: #fd7e14;"><strong>⚠️ ENTRADA DE DEMOSTRACIÓN</strong></p>' : '<p>✅ Entrada válida</p>'}
        </div>
      </body>
      </html>
    `;
    
    const blob = new Blob([htmlContent], { type: 'text/html;charset=utf-8' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `entrada-${this.referencia}.html`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
    
    alert(`✅ Entrada ${this.referencia} descargada (HTML)`);
  }

  volverAlEvento(): void {
    this.router.navigate(['/eventos', this.eventoId]);
  }

  irAEventos(): void {
    this.router.navigate(['/eventos']);
  }

  get precioUnitario(): number {
    const tipo = this.tiposEntrada.find(t => t.id === this.tipoSeleccionado);
    return tipo ? tipo.precio : 45;
  }

  get subtotal(): number {
    return this.precioUnitario * this.cantidad;
  }

  get tarifaServicio(): number {
    return Math.round(this.subtotal * 0.05 * 100) / 100;
  }

  get total(): number {
    return this.subtotal + this.tarifaServicio;
  }

  get nombreTipoEntrada(): string {
    const tipo = this.tiposEntrada.find(t => t.id === this.tipoSeleccionado);
    return tipo ? tipo.nombre : 'General';
  }

  get nombreMetodoPago(): string {
    const metodo = this.metodosPago.find(m => m.id === this.metodoPagoSeleccionado);
    return metodo ? metodo.nombre : 'No seleccionado';
  }

  formatearFecha(fecha: string): string {
    if (!fecha) return 'Fecha no disponible';
    try {
      const date = new Date(fecha);
      return date.toLocaleDateString('es-ES', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    } catch {
      return fecha;
    }
  }

  get debugInfo(): string {
    return `ID: ${this.eventoId} | Cargando: ${this.isLoading} | Paso: ${this.pasoActual} | Modo: ${this.usandoDatosSimulados ? 'SIMULADO' : 'REAL'} | Procesando: ${this.procesandoPago}`;
  }
}