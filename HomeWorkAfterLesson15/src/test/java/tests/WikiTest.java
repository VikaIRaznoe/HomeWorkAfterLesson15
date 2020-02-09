package tests;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;


public class WikiTest {
    private AppiumDriver<MobileElement> driver;

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName","Android");
        caps.setCapability("platformVersion","8.0");
        caps.setCapability("deviceName","emulator-5554");
        caps.setCapability("appPackage","org.wikipedia");
        caps.setCapability("appActivity","org.wikipedia.main.MainActivity");
        caps.setCapability("app","/Users/viktoria/IdeaProjects/Java/HomeWorkAfterLesson14Part2/src/test/resources/Apps/wiki.apk");
        //caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,80);

        URL appiumURL = new URL("http://127.0.0.1:4723/wd/hub");

        driver = new AppiumDriver<MobileElement>(appiumURL,caps);

    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }

    //Метод ожидания появления элемента
    private WebElement waitForElementPresentRefact(By by, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver,  timeoutInSeconds);
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by) //ожидает элемент
        );
    }

    //Движение внутри элемента (движение влево)
    protected void swipeElementLeft(By by){
        //Ищем элемент
        WebElement element = waitForElementPresentRefact(by,5);

        //Определяем границы элемента

        //Получаем координаты верхнего левого угла элемента
        int left_x = element.getLocation().getX();
        int upper_y = element.getLocation().getY();

        //Находим верхнюю и нижнюю часть блока и делаем swipe по середине
        int right_x = left_x + element.getSize().getWidth();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) /2;

        //Все действие описываем внутри элемента
        //Здесь описывается движение по экрану влево
        TouchAction action = new TouchAction(driver);
        action
                .press(PointOption.point(right_x,middle_y))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .moveTo(PointOption.point(left_x,middle_y))
                .release()
                .perform();
    }

    //Движение внутри элемента (движение вправо)
    protected void swipeElementRight(By by){
        //Ищем элемент
        WebElement element = waitForElementPresentRefact(by,5);

        //Определяем границы элемента

        //Получаем координаты верхнего левого угла элемента
        int left_x = element.getLocation().getX();
        int upper_y = element.getLocation().getY();

        //Находим верхнюю и нижнюю часть блока и делаем swipe по середине
        int right_x = left_x + element.getSize().getWidth();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) /2;

        //Все действие описываем внутри элемента
        //Здесь описывается движение по экрану влево
        TouchAction action = new TouchAction(driver);
        action
                .press(PointOption.point(left_x,middle_y))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .moveTo(PointOption.point(right_x,middle_y))
                .release()
                .perform();
    }

    //SCROLL по всему экрану вверх
    protected void scrollUp(int swipeTime){
        //TouchAction - позволяет описать действие
        TouchAction action = new TouchAction(driver);
        //Получаем размер всего экрана
        Dimension size = driver.manage().window().getSize();
        //Определяем размер области,в котрой будет производиться scroll
        int x = size.width/2;
        //Задаем начальную и конечную точку по "x"
        int start_y = (int)(size.height*0.9);
        int finish_y = (int)(size.height*0.2);
        action
                //Имитируем нажатие
                .press(PointOption.point(x,start_y))
                //Ждем какое-то время
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(swipeTime)))
                //определяем конечную точку движения
                .moveTo(PointOption.point(x,finish_y))
                //отправляем все это драйверу
                .release()
                //исполняем
                .perform();
    }

    //SCROLL по всему экрану вниз
    protected void scrollDown(int swipeTime){
        //TouchAction - позволяет описать действие
        TouchAction action = new TouchAction(driver);
        //Получаем размер всего экрана
        Dimension size = driver.manage().window().getSize();
        //Определяем размер области,в котрой будет производиться scroll
        int x = size.width/2;
        //Задаем начальную и конечную точку по "x"
        int start_y = (int)(size.height*0.9);
        int finish_y = (int)(size.height*0.2);
        action
                //Имитируем нажатие
                .press(PointOption.point(x,finish_y))
                //Ждем какое-то время
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(swipeTime)))
                //определяем конечную точку движения
                .moveTo(PointOption.point(x,start_y))
                //отправляем все это драйверу
                .release()
                //исполняем
                .perform();
    }

    protected void scrollUpQuik() {
        scrollDown(100);
    }

    //SCROLL до элемента
    protected void scrollForElement(By by){
        while (driver.findElements(by).size() == 0){
            scrollUpQuik();
        }
    }

    //Возвращает true или false:Исчез элемент с экрана или присутствует
    private boolean waitForElementNotPresent(By by, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver,timeoutInSeconds);
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }



    //Тест на проверку SWIPE(перемещение курсора всторону)
    //Тест заходит в меню,кликает на Settings,Переключает "ON" в разделе "Collapse Tables"
    @Test
    public void swipeTest(){
        //Используем метод ожидания появления элемента
        //1)Проходим "SKIP"
        WebElement element = waitForElementPresentRefact(By.id("fragment_onboarding_skip_button"),5);
        element.click();
        //2)Кликаем на элемент "Меню"
        WebElement menu = waitForElementPresentRefact(By.id("drawer_icon_menu"),5);
        menu.click();
        //3)Переходим в раздел Settings, в меню
        WebElement settings = waitForElementPresentRefact(By.xpath("//*[contains(@text,'Settings')]"),5);
        settings.click();
        //4)Находим элемент "Collapse tables"
        //WebElement appTheme = waitForElementPresentRefact(By.xpath("//*[contains(@text,'App theme')]"),5);
        WebElement collapseTables = waitForElementPresentRefact(By.xpath("//*[contains(@text,'Collapse tables')]"),5);
        // ON
        swipeElementLeft(By.xpath("//*[contains(@text,'ON')]"));
    }

    //Тест на проверку SCROLL(прокрутка приложения)
    //Тест скроллит до элемента,который найден в результатах поиска на слово "java"
    @Test
    public void scrolllTest() {
        //Используем метод ожидания появления элемента
        //1)Проходим "SKIP"
        WebElement element = waitForElementPresentRefact(By.id("fragment_onboarding_skip_button"),5);
        element.click();
        //2)Ищем элемент ввода текста "Search Wikipedia" на главной странице приложения "Википедия"(wiki.apk)
        WebElement textMessage = waitForElementPresentRefact(By.xpath("//*[contains(@text,'Search Wikipedia')]"),5);
        textMessage.click();
        //3)Ищем элемент ввода текста "Search Wikipedia" еще раз,только уже с другим локатором
        WebElement searchClick = waitForElementPresentRefact(By.id("search_src_text"),5);
        searchClick.click();
        //4)Вводим слово Java в строку поиска
        driver.hideKeyboard();
        searchClick.sendKeys("java");
        //5)Скролим вниз в течении 20 сек
        scrollUp(25);
        //scrollDown(10);
        //6)Находим элемент с текстом 'Java virtual machine'
        WebElement searchText = waitForElementPresentRefact(By.xpath("//*[contains(@text,'Java virtual machine')]"),5);
        searchText.click();
    }

    //Тест на проверку РОТАЦИИ(переворот экрана)
    @Test
    public void rotationTest() throws InterruptedException {
        //Используем метод ожидания появления элемента
        //Проходим "SKIP"
        WebElement element = waitForElementPresentRefact(By.id("fragment_onboarding_skip_button"),5);
        element.click();
        //1)Кликаем на элемент "Меню"
        WebElement menu = waitForElementPresentRefact(By.id("drawer_icon_menu"),5);
        menu.click();
        //2)Меняем ориентацию экрана
        driver.rotate(ScreenOrientation.LANDSCAPE);
        //3)Проверяем как кликается элемент "Меню"
        WebElement menuAfterRotation = waitForElementPresentRefact(By.id("drawer_icon_menu"),5);
        menuAfterRotation.click();
        Thread.sleep(2000);
    }

    //Тест на проверку реакции элементов приложения с использованием отправки приложения в БЭКГРАУНД(свернуть приложение)
    @Test
    public void backGroundMyTest(){
        //Используем метод ожидания появления элемента
        //Проходим "SKIP"
        WebElement element = waitForElementPresentRefact(By.id("fragment_onboarding_skip_button"),5);
        element.click();
        //1)Кликаем на элемент "Меню"
        WebElement menu = waitForElementPresentRefact(By.id("drawer_icon_menu"),5);
        menu.click();
        //2)Переходим в раздел Settings, который находится в "Меню"
        WebElement settings = waitForElementPresentRefact(By.xpath("//*[contains(@text,'Settings')]"),5);
        settings.click();
        //3)Переварачиваем экран
        driver.runAppInBackground(Duration.ofSeconds(3));
        //4)Смотрим наличие кнопки "LOG IN/JOIN WIKIPEDIA" после переворота экрана.
        //WebElement searchElementAfterBackGroundAfterBackGround = waitForElementNotPresent(By.xpath("//*[contains(@text,'Settings')]"),5);
        Assert.assertTrue(waitForElementNotPresent(By.xpath("//*[contains(@text,'Settings')]"),5));
    }

    //Тест на ПРОВЕРКУ ИСЧЕЗНОВЕНИЯ ЭЛЕМЕНТА
    @Test
    public void searchTest() throws InterruptedException {
        //Используем метод ожидания появления элемента
        //Проходим "SKIP"
        WebElement element = waitForElementPresentRefact(By.id("fragment_onboarding_skip_button"),5);
        element.click();
        //1)Кликаем на элемент "Меню"
        WebElement menu = waitForElementPresentRefact(By.id("drawer_icon_menu"),5);
        menu.click();
        //Thread.sleep(5000);
        //2)Переходим в раздел Settings, в меню
        WebElement settings = waitForElementPresentRefact(By.xpath("//*[contains(@text,'Settings')]"),5);
        settings.click();
        //3)Вернуться обратно, из раздела Settings
        WebElement backSettings = waitForElementPresentRefact(By.xpath("//android.widget.ImageButton[@content-desc='Navigate up']"),5);
        backSettings.click();
        //4)Проверяем: Исчез ли раздел App theme из пункта Settings,из меню
        waitForElementNotPresent(By.xpath("//*[contains(@text,'App theme')]"),5);
    }

}
