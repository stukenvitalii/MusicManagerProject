import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class AppControllerTest {
    private AppController appController;
    @BeforeEach
    void setUp() {
        appController = new AppController();
    }
    @Test
    void validateInput_emptyFieldName() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInput("","Name"));
    }
    @Test
    void validateInput_emptyFieldGenre() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInput("","Genre"));
    }
    @Test
    void validateInput_NaNYear() {
        Assertions.assertThrows(NumberFormatException.class, () -> appController.validateInput("string","Year of foundation"));
    }
    @Test
    void validateInput_NaNPlace() {
        Assertions.assertThrows(NumberFormatException.class, () -> appController.validateInput("string","Place in chart"));
    }
}
