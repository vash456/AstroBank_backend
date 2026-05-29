package astrobankapp.domain;

import astrobankapp.domain.enums.TipoMovimiento;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movimiento {
    private int id;
    private String fechaHora;
    private TipoMovimiento tipo;
    private double valor;
    private double saldoPosterior;
    private String descripcion;
    private boolean persisted;

    // Contador estático para generar IDs únicos automáticamente
    private static int contadorId = 1;

    public Movimiento(TipoMovimiento tipo, double valor,
                      double saldoPosterior, String descripcion) {
        this.id = contadorId++;
        this.fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.tipo = tipo;
        this.valor = valor;
        this.saldoPosterior = saldoPosterior;
        this.descripcion = descripcion;
        this.persisted = false;
    }

    public Movimiento(int id, TipoMovimiento tipo, double valor,
                      double saldoPosterior, String descripcion, String fechaHora) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.tipo = tipo;
        this.valor = valor;
        this.saldoPosterior = saldoPosterior;
        this.descripcion = descripcion;
        this.persisted = true;
    }

    public int getId() { return id; }
    public String getFechaHora() { return fechaHora; }
    public TipoMovimiento getTipo() { return tipo; }
    public double getValor() { return valor; }
    public double getSaldoPosterior() { return saldoPosterior; }
    public String getDescripcion() { return descripcion; }
    public boolean isPersisted() { return persisted; }
    public void setPersisted(boolean persisted) { this.persisted = persisted; }
    public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s | $%.2f | Saldo: $%.2f | %s",
                id, fechaHora, tipo, valor, saldoPosterior, descripcion);
    }
}
