package org.jcr.entidades;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
@Getter

public class Departamento implements Serializable {
    private final String nombre;
    private final EspecialidadMedica especialidad;
    private Hospital hospital;
    // Campos excluidos de Lombok, se mantienen manuales
    @Getter(AccessLevel.NONE)
    private final List<Medico> medicos = new ArrayList<>();
    // Campos excluidos de Lombok, se mantienen manuales
    @Getter(AccessLevel.NONE)
    private final List<Sala> salas = new ArrayList<>();

    public Departamento(String nombre, EspecialidadMedica especialidad) {
        this.nombre = validarString(nombre, "El nombre del departamento no puede ser nulo ni vacío");
        this.especialidad = Objects.requireNonNull(especialidad, "La especialidad no puede ser nula");
    }

    public List<Medico> getMedicos() {
        return Collections.unmodifiableList(medicos);
    }

    public List<Sala> getSalas() {
        return Collections.unmodifiableList(salas);
    }

    public void setHospital(Hospital hospital) {
        if (this.hospital != hospital) {
            if (this.hospital != null) {
                this.hospital.getInternalDepartamentos().remove(this);
            }
            this.hospital = hospital;
            if (hospital != null) {
                hospital.getInternalDepartamentos().add(this);
            }
        }
    }

    public void agregarMedico(Medico medico) {
        if (medico != null && !medicos.contains(medico)) {
            medicos.add(medico);
            medico.setDepartamento(this);
        }
    }

    public Sala crearSala(String numero, String tipo) {
        Sala sala = new Sala(numero, tipo, this);
        salas.add(sala);
        return sala;
    }

    @Override
    public String toString() {
        return "Departamento{" +
                "nombre='" + nombre + '\'' +
                ", especialidad=" + especialidad.getDescripcion() +
                ", hospital=" + (hospital != null ? hospital.getNombre() : "null") +
                '}';
    }

    private String validarString(String valor, String mensajeError) {
        Objects.requireNonNull(valor, mensajeError);
        if (valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }
        return valor;
    }
}
