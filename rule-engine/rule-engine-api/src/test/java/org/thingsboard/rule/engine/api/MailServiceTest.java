package org.thingsboard.rule.engine.api;

import org.junit.jupiter.api.Test;
import org.thingsboard.server.common.data.exception.ThingsboardException;

import static org.junit.jupiter.api.Assertions.*;
// trigger ci pipeline
public class MailServiceTest {

    @Test
    public void validateEmail_shouldPass_forValidEmail() {
        assertDoesNotThrow(() -> MailService.validateEmail("test@example.com"));
    }

    @Test
    public void validateEmail_shouldThrow_forInvalidEmail() {
        Exception exception = assertThrows(ThingsboardException.class, () ->
                MailService.validateEmail("invalid-email")
        );

        assertTrue(exception.getMessage().contains("Invalid email address"));
    }

    @Test
    public void validateEmail_shouldThrow_forEmptyEmail() {
        assertThrows(ThingsboardException.class, () ->
                MailService.validateEmail("")
        );
    }

    @Test
    public void validateEmail_shouldThrow_forNullEmail() {
        assertThrows(ThingsboardException.class, () ->
                MailService.validateEmail(null)
        );
    }
}
