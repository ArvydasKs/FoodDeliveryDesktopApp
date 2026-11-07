package com.example.courseprifs.fxControllers;

import com.example.courseprifs.HibernateControl.CustomHibernate;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.mockito.Mockito.mock;

/**
 * Bendras MainForm testų setup'as: visi public @FXML laukai sukuriami automatiškai,
 * o private customHibernate pakeičiamas mock'u (setData() nekviečiamas, nes jis inicializuoja visus tab'us).
 */
abstract class MainFormTestBase extends FxTestBase {

    protected MainForm form;
    protected CustomHibernate db;

    @BeforeEach
    void setUpMainForm() throws Exception {
        form = new MainForm();
        for (Field field : MainForm.class.getDeclaredFields()) {
            int mods = field.getModifiers();
            if (Modifier.isPublic(mods) && !Modifier.isStatic(mods)) {
                field.set(form, field.getType().getDeclaredConstructor().newInstance());
            }
        }
        db = mock(CustomHibernate.class);
        setPrivate("customHibernate", db);
    }

    protected void setPrivate(String name, Object value) throws Exception {
        Field field = MainForm.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(form, value);
    }

    protected void invokePrivate(String methodName) throws Exception {
        Method method = MainForm.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(form);
    }

    @SuppressWarnings("unchecked")
    protected <T> T getPrivate(String name) throws Exception {
        Field field = MainForm.class.getDeclaredField(name);
        field.setAccessible(true);
        return (T) field.get(form);
    }
}
