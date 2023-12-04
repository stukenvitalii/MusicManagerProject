import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

public class AppControllerTest {
    private AppController appController;
    private String randomString;
    @BeforeEach
    void setUp() {
        appController = new AppController();
        randomString = RandomStringUtils.randomAlphabetic(10);
    }

    @Tag("validateGroup")
    @Test
    void validateInputGroup_emptyFieldName() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInputGroup("","Name"));
    }

    @Tag("validateGroup")
    @Test
    void validateInputGroup_emptyFieldGenre() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInputGroup("","Genre"));
    }

    @Tag("validateGroup")
    @RepeatedTest(5)
    void validateInputGroup_NaNYear() {
        Assertions.assertThrows(NumberFormatException.class, () -> appController.validateInputGroup(randomString,"Year of foundation"));
    }

    @Tag("validateGroup")
    @RepeatedTest(5)
    void validateInputGroup_NaNPlace() {
        Assertions.assertThrows(NumberFormatException.class, () -> appController.validateInputGroup(randomString,"Place in chart"));
    }

    @Tag("validateTour")
    @Test
    void validateInputTour_BeginAfterEnd() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInputTour(randomString,LocalDate.now().plusDays(1),LocalDate.now()));
    }

    @Tag("validateTour")
    @Test
    void validateInputTour_EmptyName() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInputTour("",LocalDate.now(),LocalDate.now().plusDays(1)));
    }

    @Tag("validateTour")
    @Test
    void validateInputTour_EmptyStartDate() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInputTour("",LocalDate.now(),LocalDate.now().plusDays(1)));
    }

    @Tag("validateTour")
    @Test
    void validateInputTour_EmptyEndDate() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInputTour("",LocalDate.now(),LocalDate.now().plusDays(1)));
    }

    @Tag("validateSong")
    @Test
    void validateInputSong_EmptyName() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInputSong("","10",randomString));
    }

    @Tag("validateSong")
    @Test
    void validateInputSong_NaNDuration() {
        Assertions.assertThrows(NumberFormatException.class, () -> appController.validateInputSong(randomString,randomString,randomString));
    }

    @Tag("validateSong")
    @Test
    void validateInputSong_NegativeDuration() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInputSong(randomString,"-10",randomString));
    }

    @Tag("validateMember")
    @Test
    void validateInputMember_EmptyAge() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInputMember(randomString,randomString,""));
    }

    @Tag("validateMember")
    @Test
    void validateInputMember_NaNAge() {
        Assertions.assertThrows(NumberFormatException.class, () -> appController.validateInputMember(randomString,randomString,randomString));
    }

    @Tag("validateMember")
    @Test
    void validateInputMember_NegativeAge() {
        Assertions.assertThrows(AppController.IllegalArgumentException.class, () -> appController.validateInputMember(randomString,randomString,"-10"));
    }
}
