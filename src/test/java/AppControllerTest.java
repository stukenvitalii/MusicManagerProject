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
    void validateInputGroup_emptyFieldName() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInputGroup("","Name"));
    }
    @Test
    void validateInputGroup_emptyFieldGenre() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInputGroup("","Genre"));
    }
    @RepeatedTest(5)
    void validateInputGroup_NaNYear() {
        Assertions.assertThrows(NumberFormatException.class, () -> appController.validateInputGroup(randomString,"Year of foundation"));
    }

    @RepeatedTest(5)
    void validateInputGroup_NaNPlace() {
        Assertions.assertThrows(NumberFormatException.class, () -> appController.validateInputGroup(randomString,"Place in chart"));
    }
}
