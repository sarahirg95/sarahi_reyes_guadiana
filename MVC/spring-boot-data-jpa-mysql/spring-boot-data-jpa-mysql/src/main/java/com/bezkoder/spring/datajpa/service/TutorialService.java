package com.bezkoder.spring.datajpa.service;

import com.bezkoder.spring.datajpa.model.Tutorial;
import com.bezkoder.spring.datajpa.repository.TutorialRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TutorialService {

    private static final Logger logger = LoggerFactory.getLogger(TutorialService.class);

    @Autowired
    private TutorialRepository tutorialRepository;

    // Método original para crear y actualizar el tutorial
    @Transactional
    public void crearYActualizarTutorial() {
        // Crear un tutorial
        Tutorial tutorial = tutorialRepository.save(
                new Tutorial("Bloqueado", "Desc inicial", false)
        );
        logger.info("Tutorial creado con ID: " + tutorial.getId());

        // Buscar el tutorial con bloqueo pesimista
        Tutorial bloqueado = tutorialRepository.findByIdForUpdate(tutorial.getId())
                .orElseThrow(() -> new RuntimeException("Tutorial no encontrado"));

        logger.info("Tutorial bloqueado con ID: " + bloqueado.getId());

        // Simular trabajo largo
        try {
            logger.info("⏳ Simulando trabajo largo...");
            Thread.sleep(30000); // 30 segundos
        } catch (InterruptedException e) {
            logger.error("Error durante el bloqueo", e);
            Thread.currentThread().interrupt();
        }

        // Actualizar el tutorial después del bloqueo
        bloqueado.setDescription("Actualizado con bloqueo");
        tutorialRepository.save(bloqueado);

        logger.info("✅ Transacción terminada con éxito");
    }

    // Método para probar el bloqueo pesimista (simplificado)
    @Transactional
    public void probarBloqueoPesimista() {
        // Intentar bloquear el tutorial con ID 1 (ajusta este ID según los datos existentes)
        Tutorial tutorial = tutorialRepository.findByIdForUpdate(1L)
                .orElseThrow(() -> new RuntimeException("Tutorial no encontrado"));
        logger.info("Tutorial bloqueado con ID: " + tutorial.getId());

        // Simular trabajo largo
        try {
            logger.info("⏳ Simulando trabajo largo...");
            Thread.sleep(30000); // 30 segundos
        } catch (InterruptedException e) {
            logger.error("Error durante el bloqueo", e);
            Thread.currentThread().interrupt();
        }
    }
}
