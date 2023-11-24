import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class AppControllerTest {
    private AppController appController;
    private String randomString;
    @BeforeEach
    void setUp() {
        appController = new AppController();
        randomString = RandomStringUtils.randomAlphabetic(10);
    }
    @Test
    void validateInput_emptyFieldName() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInput("","Name"));
    }
    @Test
    void validateInput_emptyFieldGenre() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInput("","Genre"));
    }
    @RepeatedTest(5)
    void validateInput_NaNYear() {
        Assertions.assertThrows(NumberFormatException.class, () -> appController.validateInput(randomString,"Year of foundation"));
    }

    @RepeatedTest(5)
    void validateInput_NaNPlace() {
        Assertions.assertThrows(NumberFormatException.class, () -> appController.validateInput(randomString,"Place in chart"));
    }
}
