package astrobankapp.services;

import astrobankapp.domain.Cuenta;

import java.util.ArrayList;

public interface CuentaService {
    public Cuenta buscarPorNumeroCuenta(String numeroCuenta);
}
