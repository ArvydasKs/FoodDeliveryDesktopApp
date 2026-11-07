package com.example.courseprifs.fxControllers;

import javafx.application.Platform;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.mockStatic;

/** Bendras FX kontrolerių testų pagrindas: JavaFX toolkit ir FxUtils mock'as (kad alert'ai neatsidarytų). */
abstract class FxTestBase {

    protected MockedStatic<FxUtils> fxUtils;

    @BeforeAll
    static void startFx() {
        try {
            Platform.startup(() -> { });
        } catch (IllegalStateException alreadyStarted) {
            // toolkit jau paleistas
        }
    }

    @BeforeEach
    void mockFxUtils() {
        fxUtils = mockStatic(FxUtils.class);
    }

    @AfterEach
    void closeFxUtils() {
        fxUtils.close();
    }
}
