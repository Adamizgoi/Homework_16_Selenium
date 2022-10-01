import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppOrderTest {
    WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    /** Отправка формы, где все поля соответствуют требованиям
     */
    void shouldSendFormSuccessfullyIfAllInputsAreOk() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }
}

