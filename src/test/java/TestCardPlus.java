import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;
import static java.time.format.DateTimeFormatter.ofPattern;

public class TestCardPlus {
    LocalDate today = LocalDate.now();
    LocalDate newDate = today.plusDays(3);
    DateTimeFormatter formatter = ofPattern("dd.MM.yyyy");
    String formatDateNew = newDate.format(formatter);
    LocalDate newWeek = today.plusDays(7);
    String formatWeek = newWeek.format(formatter);
    int dayPlusSeven = newDate.getDayOfMonth();
    String str1 = Integer.toString(dayPlusSeven);

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }

    //календарь и город сложно
    @Test
    void shouldSubitRequestCalendarAndCity() {
        $("[data-test-id=city] input").setValue("Вол");
        $$(".menu-item__control").findBy(text("Вологда")).click();
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.click();
        dateField.sendKeys(Keys.CONTROL, "a");
        dateField.sendKeys(Keys.BACK_SPACE);
        String dateOfMeeting = LocalDate.now().plusDays(3).format(ofPattern("dd.MM.yyyy"));
        dateField.setValue(dateOfMeeting);
        $("[data-test-id=name] input").setValue("Сергей");
        $("[data-test-id=phone] input").setValue("+79212223344");
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(withText("Успешно!"))
                .shouldBe(visible, ofSeconds(15));
        $("[data-test-id=notification] .notification__content").shouldHave(exactText("Встреча успешно" +
                " забронирована на " + dateOfMeeting), ofSeconds(15));
    }
    @Test
    void shouldTestDateDropList() {
        $("[data-test-id=city] input").setValue("Вол");
        $$(".menu-item__control").findBy(text("Вологда")).click();
        SelenideElement dateField = $("[placeholder='Дата встречи']");
        dateField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        LocalDate currentDate = LocalDate.now();
        LocalDate dateOfMeeting = LocalDate.now().plusDays(7);
        if (currentDate.getMonthValue() != dateOfMeeting.getMonthValue()) {
            $(".calendar__arrow_direction_right[data-step='1']").click();
        }
        $$("td.calendar__day").find(exactText(String.valueOf(dateOfMeeting.getDayOfMonth()))).click();
        $("[data-test-id='name'] input").setValue("Василий");
        $("[data-test-id='phone'] input").setValue("+12345678999");
        $("[data-test-id=agreement] .checkbox__box").click();
        $(withText("Забронировать")).click();
        String dateOfMeetingFormatted = dateOfMeeting.format(ofPattern("dd.MM.yyyy"));
        $("[data-test-id=notification] .notification__content").shouldBe(visible, ofSeconds(15))
                .shouldHave(exactText("Встреча успешно забронирована на " + dateOfMeetingFormatted));
    }
}
