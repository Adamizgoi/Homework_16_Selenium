import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

public class AppOrderTest {
    WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    /**
     * Чек-лист тестов <a href="https://docs.google.com/spreadsheets/d/1VeXVAZ2laJDae4BMmVrJ2io40n5KZA-WLXbVvc8JK_Y/edit?usp=sharing">...</a>
     */

    @Test
    /** Отправка формы, где все поля соответствуют требованиям
     */
    void shouldSendFormSuccessfullyIfAllInputsAreOk() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ значения написаны латиницей (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfInNameFieldHasNotRussianLetters() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Jonson John");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ содержится только одно слово (в России можно жить без фамилий,
     *  хоть эта практика и не распространена https://www.kommersant.ru/doc/2931691)
     */
    void shouldSendFormSuccessfullyIfInNameSurnameFieldHasNoSurname() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле ФИ есть слово(имя) с дефисом
     */
    void shouldSendFormSuccessfullyIfInNameFieldNameWithHyphen() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Анна-Мария");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ первым знаком стоит дефис (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfInNameFieldHasHyphenInFirstLetter() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("-Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ последним знаком стоит дефис (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfInNameFieldHasHyphenInLastLetter() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана-");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ стоит только дефис (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfInNameFieldHasOnlyHyphen() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("-");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле ФИ имя или фамилия состоят из нескольких слов
     */
    void shouldSendFormSuccessfullyIfNameFieldNameHasMoreThenTwoWords() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Хуан Карлос де Ла Пампадур");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле ФИ будет слово с буквой ё
     */
    void shouldSendFormSuccessfullyIfNameFieldHasWordWithSpecificRussianLetter() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федоров Пётр");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле имя использовались только буквы верхнего регистра
     */
    void shouldSendFormSuccessfullyIfNameFieldAllLettersUppercase() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("ФЕДОРОВА СВЕТЛАНА");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле имя использовались только буквы нижнего регистра
     */
    void shouldSendFormSuccessfullyIfNameFieldAllLettersLowercase() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("федорова светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ содержится только одна буква (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfInNameSurnameFieldHasNoSurnameAndNameHasOnlyOneLetter() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("С");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле ФИ имя и фамилия состоят из 1 буквы (итого 2 буквы через пробел)
     */
    void shouldSendFormSuccessfullyIfNameFieldHasTwoWordsFromOneLetter() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Ф с");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле ФИ имя и фамилия состоят из 2 букв (итого 4 буквы через пробел)
     */
    void shouldSendFormSuccessfullyIfNameFieldHasTwoWordsFromTwoLetter() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Фе Св");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле ФИ имя и фамилия довольно длинный текст (например, 100 знаков)
     */
    void shouldSendFormSuccessfullyIfNameFieldHasManyLetters() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Фееееееееееееееедороваааааааааааааааааааааааааааааа Свеееееееееееееееетттлаааааннааааааааааааааааааа");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ содержится апостроф / одинарная кавычка (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfInNameSurnameFieldHasApostrophe() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Светлана д'Артаньян");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ пусто (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyNameFieldIsEmpty() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ только пробелы (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfNameFieldHasOnlySpaces() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("     ");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле ФИ перед ФИ стоит пробел (должен триматься)
     */
    void shouldSendFormSuccessfullyIfNameFieldHasSpaceBeforeNameSurname() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(" Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле ФИ после ФИ стоит пробел (должен триматься)
     */
    void shouldSendFormSuccessfullyIfNameFieldHasSpaceAfterNameSurname() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана ");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ вставлен очень-очень длинный текст (негативный тест)
     */
    void shouldNotSendFormSuccessfullyIfNameFieldHasExtremelyLongText() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        assertTrue(driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid")).isDisplayed());
    }

    @Test
    /** Проверка, что если попробовать отправить форму с очень длинным текстом, а потом исправить ошибку, форма не зависнет и успешно отправится
     * (негативный тест)
     */
    void shouldNotCrashSystemIfNameFieldHasExtremelyLongText() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана" +
                "Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(Keys.CONTROL + "a");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(Keys.DELETE);
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ есть цифры (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfNameFieldHasNumbers() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("12344567");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ есть спецсимволы ASCII (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyAndCrashSystemIfNameFieldHasSpecialSymbols() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("''(){};#*?!&@-+^$\"\";:~```<>//\\\\||");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Проверка, что после ввода символов из ASCII в поле ФИ, а затем исправления ошибки форма не ломается (негативный кейс)
     */
    void shouldNotCrashSystemIfNameFieldHasSpecialSymbols() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("''(){};#*?!&@-+^$\"\";:~```<>//\\\\||");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(Keys.CONTROL + "a");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(Keys.DELETE);
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Проверка, что при вводе символов не из ASCII в поле ФИ, а затем исправления ошибки, форма не ломается / зависает  (негативный кейс)
     */
    void shouldNotСrashSystemIfNameFieldHasSpecialSymbolsSmiles() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("☹✖ў§©¤љЌЃў§©¤љ");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(Keys.CONTROL + "a");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(Keys.DELETE);
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ есть спецсимволы не из ASCII (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyAndCrashSystemIfNameFieldHasSpecialSymbolsSmiles() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("☹✖ў§©¤љЌЃў§©¤љ");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле номера указан код, отличающийся от 9 (в номерах 8 906 048... 9 - федеральный код
     * мобильных операторов
     */
    void shouldSendFormSuccessfullyIfNumberHaveNotMobileOperatorCode() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+74060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__bx")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера плюс стоит в середине (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfNumberHasPlusNotAsTheFirstSymbol() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("7906+0483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера плюс стоит в конце (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfNumberHasPlusAsLastSymbol() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("79060483535+");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера плюс стоит и в начале, и в середине, а знаков все равно 11 (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfNumberHasTwoPluses() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7906+483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера нет плюса, а знаков все равно 11 (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfThereAreNoPluses() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера знаков 10, а плюс в правильном месте (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfThereAreLessThenElevenNumbers() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7906048355");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера знаков 12, а плюс в правильном месте (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfThereAreMoreThenElevenNumbers() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+790604835355");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера только плюсы, но число знаков верное (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfThereAreOnlyPlusInPhone() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+++++++++++");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера пусто (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldIsEmpty() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера вместо кода России +7 на месте 7 другое число (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasWrongRussianPhoneCode() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+89060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера есть пробелы (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasSpaces() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7 906 048 3535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера есть пробелы, но знаков все равно 11 (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasSpacesButAmountOfNumbersIsOk() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7 906 04 8");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера есть скобки () (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasParentheses() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7(906)0483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера есть скобки (), но знаков все равно 11 (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasParenthesesButAmountOfNumbersIsOk() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7(906)04835");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера есть дефисы - (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasHyphen() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7906048-35-35");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера есть дефисы -, но знаков все равно 11 (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasHyphenButAmountOfNumbersIsOk() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7906-048-35");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера вместо цифр буквы - кириллица, но знаков все равно 11(негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasRussianLettersButAmountOfNumbersIsOk() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7абвпоквлпл");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера вместо цифр буквы - латиница, но знаков все равно 11 (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasEnglishLettersButAmountOfNumbersIsOk() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7ghktjgnbnb");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера вместо цифр спецсимволы ASCII, но символов все-равно 11 (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasSpecialSymbolsAmountOfNumbersIsOk() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7'(};#*?!&");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Проверка, что если вставить вместо цифр спецсимволы ASCII (11 символов) при отправке формы в поле номера, а затем исправлении
     *  ошибки, форма не сломается / не зависнет (негативный кейс)
     */
    void shouldNotCrashSystemIfPhoneFieldHasSpecialSymbolsAmountOfNumbersIsOk() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7'(};#*?!&");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys(Keys.CONTROL + "a");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys(Keys.DELETE);
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Проверка, что если вставить вместо цифр спецсимволы не из ASCII + эмоджи (11 символов) при отправке формы в поле номера, а затем исправления
     *  ошибки, форма не сломается / не зависнет (негативный кейс)
     */
    void shouldNotSendSuccessfullyFormIfPhoneFieldHasSpecialSymbolsSmilesAmountOfNumbersIsOk() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7☹✖ў§©¤љЌЃ");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера вместо цифр спецсимволы не из ASCII, но символов все-равно 11 (негативный кейс)
     */
    void shouldNotSendSuccessfullyFormAndCrashSystemIfPhoneFieldHasSpecialSymbolsSmilesAmountOfNumbersIsOk() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7☹✖ў§©¤љЌЃ");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys(Keys.CONTROL + "a");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys(Keys.DELETE);
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера вместо цифр 11 пробелов(негативный кейс)
     */
    void shouldNotSendFormSuccessfullyAndCrashSystemIfPhoneFieldHasElevenSpaces() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("           ");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера вставили длинный текст из буфера обмена (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyAndCrashSystemIfPhoneFieldHasExtremelyLongText() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана\" +\n" +
                "\"Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана\" +\n" +
                "\"Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана\" +\n" +
                "\"Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана\" +\n" +
                "\"Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана\" +\n" +
                "\"Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана\" +\n" +
                "\"Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана\" +\n" +
                "\"Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова Светлана\" +\n" +
                "\"Федорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова СветланаФедорова");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys(Keys.CONTROL + "a");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys(Keys.DELETE);
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где не выставлен чекбокс (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfThereAreNoCheckBox() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        assertTrue(driver.findElement(By.cssSelector("[data-test-id = \"agreement\"].input_invalid")).isDisplayed());
    }

    @Test
    /** Отправка формы, где в поле имя нажали ENTER
     */
    void shouldSendFormSuccessfullyAndNotCrashSystemIfUserClickEnterInFieldName() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(Keys.ENTER);
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле номер нажали ENTER
     */
    void shouldSendFormSuccessfullyAndNotCrashSystemIfUserClickEnterInFieldPhone() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys(Keys.ENTER);
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Текст ошибок в форме должен изменяться, если юзер исправил ошибку в поле имя, но допустил другую ошибку и
     *  форма еще не отправляется из-за ошибки (негативный кейс)
     */
    void shouldChangeErrorIfUserFixErrorInFieldName() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Atljhjdf Cdtnkfyf");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7906048353");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(Keys.CONTROL + "a");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(Keys.DELETE);
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Текст ошибок в форме должен исчезать, если юзер исправил ошибку в поле имя, но форма еще не отправляется
     *  из-за ошибки в другом поле (негативный кейс)
     */
    void shouldRemoveErrorIfUserFixErrorInFieldName() throws NoSuchElementException {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Atljhjdf Cdtnkfyf");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7906048353");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(Keys.CONTROL + "a");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(Keys.DELETE);
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Укажите точно как в паспорте";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"name\"] :last-child span:last-child")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Текст ошибок в форме должен изменяться, если юзер исправил ошибку в поле номер, но допустил другую ошибку и
     * форма еще не отправляется из-за ошибки (негативный кейс)
     */
    void shouldChangeErrorIfUserFixErrorInFieldPhone() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7906048353");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys(Keys.CONTROL + "a");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys(Keys.DELETE);
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Текст ошибок в форме должен исчезать, если юзер исправил ошибку в поле телефон, но форма еще не отправляется
     *  из-за ошибки в другом поле
     */
    void shouldRemoveErrorIfUserFixErrorInFieldPhone() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Atljhjdf Cdtnkfyf");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7906048353");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(Keys.CONTROL + "a");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(Keys.DELETE);
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "На указанный номер моб. тел. будет отправлен смс-код для подтверждения заявки на карту. Проверьте, что номер ваш и введен корректно.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] :last-child span:last-child")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Цвет ошибок в чек-боксе должен исчезать, если юзер исправил ошибку, но форма еще не отправляется
     *  из-за ошибки в другом поле
     */
    void shouldRemoveErrorColorIfUserFixErrorInFieldCheckBox() {
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+++");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        assertFalse(driver.findElement(By.cssSelector("[data-test-id = \"agreement\"].input_invalid")).isDisplayed());
    }
}

