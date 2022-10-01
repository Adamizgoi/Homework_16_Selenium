import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
        driver.get("http://localhost:9999/");
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
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Jonson John");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"name\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ содержится только одно слово (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfInNameSurnameFieldHasNoSurname() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"name\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле ФИ есть слово(имя) с дефисом
     */
    void shouldSendFormSuccessfullyIfInNameFieldNameWithHyphen() {
        driver.get("http://localhost:9999/");
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
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("-Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"name\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ последним знаком стоит дефис (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfInNameFieldHasHyphenInLastLetter() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана-");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"name\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ стоит только дефис (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfInNameFieldHasOnlyHyphen() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("-");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"name\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле ФИ имя или фамилия состоят из нескольких слов
     */
    void shouldSendFormSuccessfullyIfNameFieldNameHasMoreThenTwoWords() {
        driver.get("http://localhost:9999/");
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
        driver.get("http://localhost:9999/");
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
        driver.get("http://localhost:9999/");
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
        driver.get("http://localhost:9999/");
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
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("С");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"name\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле ФИ имя и фамилия состоят из 1 буквы (итого 2 буквы через пробел)
     */
    void shouldSendFormSuccessfullyIfNameFieldHasTwoWordsFromOneLetter() {
        driver.get("http://localhost:9999/");
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
        driver.get("http://localhost:9999/");
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
        driver.get("http://localhost:9999/");
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
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Светлана д'Артаньян");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"name\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ пусто (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyNameFieldIsEmpty() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"name\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ только пробелы (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfNameFieldHasOnlySpaces() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("     ");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"name\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле ФИ перед ФИ стоит пробел (должен триматься)
     */
    void shouldSendFormSuccessfullyIfNameFieldHasSpaceBeforeNameSurname() {
        driver.get("http://localhost:9999/");
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
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана ");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("p[data-test-id = \"order-success\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ вставлен очень-очень длинный текст, должно быть ограничение по символам
     * (негативный тест)
     */
    void shouldNotSendFormSuccessfullyAndCrashSystemIfNameFieldHasExtremelyLongText() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla ut blandit nisi. Nulla ut tempus ex. Praesent ultrices vestibulum mi quis dictum. Nulla malesuada nec nunc eu congue. Etiam convallis sapien ipsum, id euismod metus fermentum sed. Vestibulum pharetra, erat ut sollicitudin tincidunt, lacus tortor imperdiet diam, vitae malesuada nisl ante quis enim. Interdum et malesuada fames ac ante ipsum primis in faucibus.\n" +
                "\n" +
                "Vestibulum vulputate urna at libero vehicula condimentum. Fusce turpis quam, vehicula ultrices placerat a, efficitur id risus. Cras dapibus ligula magna, quis feugiat sem sagittis nec. Ut et urna posuere, volutpat est in, bibendum purus. Etiam scelerisque consectetur est, bibendum tempor lorem pulvinar nec. Sed vitae posuere elit. Curabitur egestas hendrerit enim blandit sodales. Donec sed lobortis ligula.\n" +
                "\n" +
                "Nulla quis nibh sit amet erat hendrerit pulvinar. Sed commodo tristique blandit. Etiam eleifend semper enim, eu volutpat libero maximus nec. Sed vitae tortor luctus, malesuada lorem ultrices, auctor enim. Nunc aliquam vitae metus at elementum. Pellentesque ut lacinia erat. Suspendisse nec bibendum ligula. Morbi a arcu posuere, pretium dolor sit amet, vehicula nisi. Donec rhoncus sodales libero.\n" +
                "\n" +
                "Nam augue odio, tristique et dui et, laoreet accumsan quam. Suspendisse potenti. Sed erat velit, rhoncus non sodales at, hendrerit et ex. Morbi in mollis arcu, vitae placerat velit. Nullam aliquet posuere luctus. Curabitur placerat ante eget velit accumsan convallis. Ut nec viverra justo. Curabitur tincidunt tortor odio. Pellentesque in venenatis ante, ac venenatis nisi. Maecenas purus nisi, tempus id vestibulum at, egestas sit amet est. Cras nunc elit, egestas sed ultrices non, faucibus et velit. Aliquam luctus, neque a porttitor semper, dui nisi tempus eros, sed semper quam dui ut est.\n" +
                "\n" +
                "Morbi venenatis, urna quis pretium aliquet, nunc ante tincidunt neque, id maximus nulla leo eget purus. Aenean vehicula a tortor sed iaculis. Curabitur luctus nulla at felis accumsan, sed semper augue pulvinar. Fusce dapibus lorem id massa malesuada facilisis. Nullam dignissim justo ac justo mattis dapibus a vel eros. Suspendisse et risus a lacus aliquet auctor. Etiam et erat sapien. In augue risus, tincidunt quis faucibus sed, congue vel magna. Maecenas sollicitudin auctor sapien, nec malesuada lectus feugiat sit amet. Duis dictum lacus sit amet consequat finibus. Integer efficitur mauris vel urna posuere blandit. Mauris ultrices dui vitae turpis euismod egestas. Aenean enim ante, facilisis id nisi ut, imperdiet imperdiet dolor. Nunc ornare eleifend tellus, in iaculis leo ultrices id. Praesent quis aliquet nibh. Fusce scelerisque lorem accumsan felis tempus, ac tincidunt tortor interdum.\n" +
                "\n" +
                "Sed at interdum ex. Sed commodo, arcu et congue blandit, neque nisl luctus ipsum, a volutpat ante justo at nulla. Suspendisse potenti. Sed tellus tellus, scelerisque at sollicitudin rhoncus, cursus id est. Integer sem enim, congue sit amet augue quis, auctor consequat urna. Fusce euismod nulla non erat hendrerit, ullamcorper dapibus mauris porttitor. Aenean eget tempor libero. Maecenas scelerisque ex finibus blandit iaculis. Nulla facilisi. In egestas quam at tincidunt vestibulum.");
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
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("12344567");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"name\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле ФИ есть спецсимволы ASCII (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyAndCrashSystemIfNameFieldHasSpecialSymbols() {
        driver.get("http://localhost:9999/");
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
    /** Попытка отправки формы, где в поле ФИ есть спецсимволы не из ASCII (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyAndCrashSystemIfNameFieldHasSpecialSymbolsSmiles() {
        driver.get("http://localhost:9999/");
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
    /** Отправка формы, где в поле номера указан код, отличающийся от 9 (в номерах 8 906 048... 9 - федеральный код
     * мобильных операторов
     */
    void shouldSendFormSuccessfullyIfNumberHaveNotMobileOperatorCode() {
        driver.get("http://localhost:9999/");
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
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("7906+0483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера плюс стоит в конеце (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfNumberHasPlusAsLastSymbol() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("79060483535+");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера плюс стоит и в начале, и в середине, а знаков все равно 11 (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfNumberHasTwoPluses() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7906+483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера нет плюса, а знаков все равно 11 (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfThereAreNoPluses() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("79060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера знаков 10, а плюс в правильном месте (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfThereAreLessThenElevenNumbers() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7906048355");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера знаков 12, а плюс в правильном месте (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfThereAreMoreThenElevenNumbers() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+790604835355");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера только плюсы, но число знаков верное (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfThereAreOnlyPlusInPhone() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+++++++++++");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера пусто (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldIsEmpty() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера вместо кода России +7 на месте 7 другое число (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasWrongRussianPhoneCode() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+89060483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера есть пробелы (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasSpaces() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7 906 048 3535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера есть пробелы, но знаков все равно 11 (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasSpacesButAmountOfNumbersIsOk() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7 906 04 8");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера есть скобки () (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasParentheses() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7(906)0483535");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера есть скобки (), но знаков все равно 11 (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasParenthesesButAmountOfNumbersIsOk() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7(906)04835");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера есть дефисы - (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasHyphen() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7906048-35-35");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера есть дефисы -, но знаков все равно 11 (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasHyphenButAmountOfNumbersIsOk() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7906-048-35");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера вместо цифр буквы - кириллица, но знаков все равно 11(негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasRussianLettersButAmountOfNumbersIsOk() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7абвпоквлпл");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера вместо цифр буквы - латиница, но знаков все равно 11 (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyIfPhoneFieldHasEnglishLettersButAmountOfNumbersIsOk() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7ghktjgnbnb");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Попытка отправки формы, где в поле номера вместо цифр спецсимволы ASCII, но символов все-равно 11 (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyAndCrashSystemIfPhoneFieldHasSpecialSymbolsAmountOfNumbersIsOk() {
        driver.get("http://localhost:9999/");
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
    /** Попытка отправки формы, где в поле номера вместо цифр спецсимволы не из ASCII, но символов все-равно 11 (негативный кейс)
     */
    void shouldNotSendSuccessfullyFormAndCrashSystemIfPhoneFieldHasSpecialSymbolsSmilesAmountOfNumbersIsOk() {
        driver.get("http://localhost:9999/");
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
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("           ");
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
    /** Попытка отправки формы, где в поле номера вставили длинный текст из буфера обмена (негативный кейс)
     */
    void shouldNotSendFormSuccessfullyAndCrashSystemIfPhoneFieldHasExtremelyLongText() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla ut blandit nisi. Nulla ut tempus ex. Praesent ultrices vestibulum mi quis dictum. Nulla malesuada nec nunc eu congue. Etiam convallis sapien ipsum, id euismod metus fermentum sed. Vestibulum pharetra, erat ut sollicitudin tincidunt, lacus tortor imperdiet diam, vitae malesuada nisl ante quis enim. Interdum et malesuada fames ac ante ipsum primis in faucibus.\n" +
                "\n" +
                "Vestibulum vulputate urna at libero vehicula condimentum. Fusce turpis quam, vehicula ultrices placerat a, efficitur id risus. Cras dapibus ligula magna, quis feugiat sem sagittis nec. Ut et urna posuere, volutpat est in, bibendum purus. Etiam scelerisque consectetur est, bibendum tempor lorem pulvinar nec. Sed vitae posuere elit. Curabitur egestas hendrerit enim blandit sodales. Donec sed lobortis ligula.\n" +
                "\n" +
                "Nulla quis nibh sit amet erat hendrerit pulvinar. Sed commodo tristique blandit. Etiam eleifend semper enim, eu volutpat libero maximus nec. Sed vitae tortor luctus, malesuada lorem ultrices, auctor enim. Nunc aliquam vitae metus at elementum. Pellentesque ut lacinia erat. Suspendisse nec bibendum ligula. Morbi a arcu posuere, pretium dolor sit amet, vehicula nisi. Donec rhoncus sodales libero.\n" +
                "\n" +
                "Nam augue odio, tristique et dui et, laoreet accumsan quam. Suspendisse potenti. Sed erat velit, rhoncus non sodales at, hendrerit et ex. Morbi in mollis arcu, vitae placerat velit. Nullam aliquet posuere luctus. Curabitur placerat ante eget velit accumsan convallis. Ut nec viverra justo. Curabitur tincidunt tortor odio. Pellentesque in venenatis ante, ac venenatis nisi. Maecenas purus nisi, tempus id vestibulum at, egestas sit amet est. Cras nunc elit, egestas sed ultrices non, faucibus et velit. Aliquam luctus, neque a porttitor semper, dui nisi tempus eros, sed semper quam dui ut est.\n" +
                "\n" +
                "Morbi venenatis, urna quis pretium aliquet, nunc ante tincidunt neque, id maximus nulla leo eget purus. Aenean vehicula a tortor sed iaculis. Curabitur luctus nulla at felis accumsan, sed semper augue pulvinar. Fusce dapibus lorem id massa malesuada facilisis. Nullam dignissim justo ac justo mattis dapibus a vel eros. Suspendisse et risus a lacus aliquet auctor. Etiam et erat sapien. In augue risus, tincidunt quis faucibus sed, congue vel magna. Maecenas sollicitudin auctor sapien, nec malesuada lectus feugiat sit amet. Duis dictum lacus sit amet consequat finibus. Integer efficitur mauris vel urna posuere blandit. Mauris ultrices dui vitae turpis euismod egestas. Aenean enim ante, facilisis id nisi ut, imperdiet imperdiet dolor. Nunc ornare eleifend tellus, in iaculis leo ultrices id. Praesent quis aliquet nibh. Fusce scelerisque lorem accumsan felis tempus, ac tincidunt tortor interdum.\n" +
                "\n" +
                "Sed at interdum ex. Sed commodo, arcu et congue blandit, neque nisl luctus ipsum, a volutpat ante justo at nulla. Suspendisse potenti. Sed tellus tellus, scelerisque at sollicitudin rhoncus, cursus id est. Integer sem enim, congue sit amet augue quis, auctor consequat urna. Fusce euismod nulla non erat hendrerit, ullamcorper dapibus mauris porttitor. Aenean eget tempor libero. Maecenas scelerisque ex finibus blandit iaculis. Nulla facilisi. In egestas quam at tincidunt vestibulum.");
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
    /** Попытка отправки формы, где не выставлен чекбокс
     */
    void shouldNotSendFormSuccessfullyIfThereAreNoCheckBox() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "rgba(255, 92, 92, 1)";
        String actual = driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__text")).getCssValue("color");
        assertEquals(expected, actual);
    }

    @Test
    /** Отправка формы, где в поле имя нажали ENTER
     */
    void shouldSendFormSuccessfullyAndNotCrashSystemIfUserClickEnterInFieldName() {
        driver.get("http://localhost:9999/");
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
        driver.get("http://localhost:9999/");
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
     * форма еще не отправляется из-за ошибки
     */
    void shouldChangeErrorIfUserFixErrorInFieldName() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Atljhjdf Cdtnkfyf");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7906048353");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(Keys.CONTROL + "a");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys(Keys.DELETE);
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"name\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Текст ошибок в форме должен исчезать, если юзер исправил ошибку в поле имя, но форма еще не отправляется
     *  из-за ошибки в другом поле
     */
    void shouldRemoveErrorIfUserFixErrorInFieldName() {
        driver.get("http://localhost:9999/");
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
     * форма еще не отправляется из-за ошибки
     */
    void shouldChangeErrorIfUserFixErrorInFieldPhone() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+7906048353");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys(Keys.CONTROL + "a");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys(Keys.DELETE);
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("span[data-test-id = \"phone\"] span[class = \"input__sub\"]")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    /** Текст ошибок в форме должен исчезать, если юзер исправил ошибку в поле телефон, но форма еще не отправляется
     *  из-за ошибки в другом поле
     */
    void shouldRemoveErrorIfUserFixErrorInFieldPhone() {
        driver.get("http://localhost:9999/");
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
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("span[data-test-id= name] input")).sendKeys("Федорова Светлана");
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+79060483535");
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        driver.findElement(By.cssSelector("span[data-test-id=\"phone\"] input")).sendKeys("+++");
        driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__box")).click();
        driver.findElement(By.cssSelector("div[class*=\"form-field\"]:last-child button")).click();
        String expected = "rgba(11, 31, 53, 0.95)";
        String actual = driver.findElement(By.cssSelector("label[data-test-id=\"agreement\"] .checkbox__text")).getCssValue("color");
        assertEquals(expected, actual);
    }
}

