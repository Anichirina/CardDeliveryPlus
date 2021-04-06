import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class testCardPlus {
    LocalDate today = LocalDate.now();
    LocalDate newDate = today.plusDays(3);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    String formatDateNew = newDate.format(formatter);
    LocalDate NewWeek = today.plusDays(7);
    String FormatWeek = NewWeek.format(formatter);
    int dayPlusSeven = NewWeek.getDayOfMonth();
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
        $(".icon_name_calendar").click();
        $$(".popup__container .calendar__row .calendar__day").findBy(text(str1)).click();
        $("[data-test-id=name] input").setValue("Сергей");
        $("[data-test-id=phone] input").setValue("+79212223344");
        $("[data-test-id=agreement]").click();
        $(".button__text").click();
        $(withText("Успешно!"))
                .shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").shouldHave(exactText("Встреча успешно" +
                " забронирована на " + FormatWeek), Duration.ofSeconds(15));
    }
}
